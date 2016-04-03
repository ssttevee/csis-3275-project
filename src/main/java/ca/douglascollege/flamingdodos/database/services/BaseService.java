package ca.douglascollege.flamingdodos.database.services;

import ca.douglascollege.flamingdodos.database.models.BaseModel;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class BaseService<T extends BaseModel> {
    private SqlJetDb mDatabase;
    private Class<T> mModelClass;

    protected BaseService(SqlJetDb db, Class<T> modelClass) {
        mDatabase = db;
        mModelClass = modelClass;
    }

    public long save(T model) throws SqlJetException {
        return (long) mDatabase.runWriteTransaction(new SaveTransaction(model));
    }

    public T lookup(long rowId) throws SqlJetException {
        @SuppressWarnings("unchecked")
        T[] models = (T[]) mDatabase.runReadTransaction(new LookupTransaction(rowId));

        if (models.length > 0) {
            return models[0];
        }

        return null;
    }

    public T[] lookup(String colName, Object key) throws SqlJetException {
        @SuppressWarnings("unchecked")
        T[] models = (T[]) mDatabase.runReadTransaction(new LookupTransaction(colName, key));

        return models;
    }

    public T[] getAll() throws SqlJetException {
        @SuppressWarnings("unchecked")
        T[] models = (T[]) mDatabase.runReadTransaction(new GetAllTransaction());

        return models;
    }

    public boolean delete(T model) throws SqlJetException {
        return (boolean) mDatabase.runWriteTransaction(new DeleteTransaction(model.getRowId()));
    }

    public T newModel() throws SqlJetException {
        T model;
        try {
            model = mModelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // this would only happen if the model subclass has a non-public default constructor
            throw new AssertionError(mModelClass.getSimpleName() + " has a non-public default constructor", e);
        }

        return model;
    }

    private ISqlJetTable getTable(SqlJetDb sqlJetDb) throws SqlJetException {
        return newModel().getTable(sqlJetDb);
    }

    private abstract class IterativeTransaction implements ISqlJetTransaction {
        public List<T> readCursor(ISqlJetCursor cursor) throws SqlJetException {
            List<T> models = new ArrayList<>();
            while (!cursor.eof()) {
                T model = newModel();
                if (model != null && model.load(cursor)) {
                    models.add(model);
                }

                cursor.next();
            }
            return models;
        }
    }

    private class SaveTransaction implements ISqlJetTransaction {
        private T mModel;

        public SaveTransaction(T model) {
            mModel = model;
        }

        @Override
        public Object run(SqlJetDb db) throws SqlJetException {
            return mModel.save(db);
        }
    }

    private class GetAllTransaction extends IterativeTransaction {
        @Override
        public Object run(SqlJetDb db) throws SqlJetException {
            ISqlJetTable table = getTable(db);
            if (table != null) {
                List<T> models = readCursor(table.open());

                @SuppressWarnings("unchecked")
                T[] retArr = (T[]) Array.newInstance(mModelClass, models.size());

                return models.toArray(retArr);
            }

            return Array.newInstance(mModelClass, 0);
        }
    }

    private class LookupTransaction extends IterativeTransaction {
        private String mColumnName;
        private Object mKey;

        public LookupTransaction(long rowId) {
            mColumnName = null;
            mKey = rowId;
        }

        public LookupTransaction(String columnName, Object key) {
            mColumnName = columnName;
            mKey = key;
        }

        @Override
        public Object run(SqlJetDb db) throws SqlJetException {
            ISqlJetTable table = getTable(db);

            List<T> models = new ArrayList<>();
            if (table != null) {
                ISqlJetCursor cursor = table.open();

                while (!cursor.eof()) {
                    if (mColumnName == null) {
                        if (cursor.getRowId() == ((Long) mKey)) {
                            T model = newModel();
                            model.load(cursor);
                            models.add(model);
                        }
                    } else if (cursor.getValue(mColumnName) == mKey) {
                        T model = newModel();
                        model.load(cursor);
                        models.add(model);
                    }

                    cursor.next();
                }

                @SuppressWarnings("unchecked")
                T[] retArr = (T[]) Array.newInstance(mModelClass, models.size());

                return models.toArray(retArr);
            }

            return Array.newInstance(mModelClass, 0);
        }
    }

    private class DeleteTransaction implements ISqlJetTransaction {
        private long mRowId;

        public DeleteTransaction(long rowId) {
            mRowId = rowId;
        }

        @Override
        public Object run(SqlJetDb db) throws SqlJetException {
            ISqlJetTable table = getTable(db);

            ISqlJetCursor deleteCursor = table.open();
            while (!deleteCursor.eof()) {
                if (deleteCursor.getRowId() == mRowId) {
                    deleteCursor.delete();
                    return true;
                }
                deleteCursor.next();
            }

            return false;
        }
    }
}
