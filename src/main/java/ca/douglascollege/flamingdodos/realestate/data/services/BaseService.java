package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.realestate.data.models.BaseModel;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
        T model = (T) mDatabase.runReadTransaction(new LookupTransaction(rowId));

        return model;
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

    private class GetAllTransaction implements ISqlJetTransaction {
        @Override
        public Object run(SqlJetDb db) throws SqlJetException {
            List<T> models = new ArrayList<>();

            ISqlJetTable table = getTable(db);
            if (table != null) {

                ISqlJetCursor cursor = table.open();

                while (!cursor.eof()) {
                    T model = newModel();
                    if (model != null && model.load(cursor)) {
                        models.add(model);
                    }

                    cursor.next();
                }
            }

            @SuppressWarnings("unchecked")
            T[] retArr = (T[]) Array.newInstance(mModelClass, models.size());

            return models.toArray(retArr);
        }
    }

    private class LookupTransaction implements ISqlJetTransaction {
        private long mRowId;

        public LookupTransaction(long rowId) {
            mRowId = rowId;
        }

        @Override
        public Object run(SqlJetDb db) throws SqlJetException {
            ISqlJetTable table = getTable(db);
            if (table != null) {
                ISqlJetCursor cursor = table.open();
                cursor.goTo(mRowId);

                T model = newModel();
                if (model.load(cursor)) {
                    return model;
                }
            }

            return null;
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
            deleteCursor.goTo(mRowId);
            deleteCursor.delete();

            return true;
        }
    }
}
