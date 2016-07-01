package ca.douglascollege.flamingdodos.database.sqlite;

import ca.douglascollege.flamingdodos.database.exceptions.DatabaseException;
import ca.douglascollege.flamingdodos.database.interfaces.DatabaseQuery;
import ca.douglascollege.flamingdodos.database.interfaces.IDatabase;
import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseCursor;
import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseModel;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteTable;
import ca.douglascollege.flamingdodos.database.sqlite.models.BaseSqliteModel;
import ca.douglascollege.flamingdodos.realestate.utils.CaseTools;
import com.sun.istack.internal.Nullable;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.schema.ISqlJetColumnDef;
import org.tmatesoft.sqljet.core.schema.SqlJetConflictAction;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class SqliteDatabase implements IDatabase {
    protected SqlJetDb mDatabase;

    public SqliteDatabase(File file) throws DatabaseException {
        boolean isNew = !file.exists();

        try {
            this.mDatabase = SqlJetDb.open(file, true);
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }

        if (isNew)
            onCreate();
    }

    protected void onCreate() throws DatabaseException {

    }

    @Override
    public <T extends IDatabaseModel> T lookup(Class<T> modelClass, final Object lookupKey) throws DatabaseException {
        try {
            ISqlJetTable table = getTable(modelClass);

            Map<String, Object> found = new SqlJetItemTraverser(table) {
                @Override
                protected boolean traverse(Object key, Map<String, Object> item) {
                    return key == lookupKey;
                }
            }.getOne();

            if (found != null) {
                T model = modelClass.newInstance();
                model.load(found);

                if (model instanceof BaseSqliteModel) {
                    ((BaseSqliteModel) model).setRowId((Long) found.get("__key__"));
                }

                return model;
            } else {
                return null;
            }

        } catch (SqlJetException | IllegalAccessException | InstantiationException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public <T extends IDatabaseModel> Object insert(@Nullable Object key, T source) throws DatabaseException {
        try {
            ISqlJetTable table = getTable(source.getClass());

            String[] colNames = getColNames(table);

            Map<String, Object> exported = source.export();
            Object[] data = new Object[colNames.length];

            for (int i = 0; i < colNames.length; i++) {
                data[i] = exported.get(colNames[i]);
            }

            long id;
            if (key == null) {
                id = table.insertOr(SqlJetConflictAction.FAIL, data);
            } else {
                id = table.insertWithRowIdOr(SqlJetConflictAction.REPLACE, (Long) key, data);
            }

            if (source instanceof BaseSqliteModel)
                ((BaseSqliteModel) source).setRowId(id);

            return id;
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public <T extends IDatabaseModel> IDatabaseCursor<T> getAll(final Class<T> modelClass) throws DatabaseException {
        try {
            ISqlJetTable table = getTable(modelClass);

            mDatabase.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            final ISqlJetCursor cursor = table.open();
            final String[] colNames = getColNames(table);

            return new IDatabaseCursor<T>() {
                private boolean first = true;

                @Override
                public boolean hasNext() {
                    try {
                        if (first) {
                            first = false;
                            return !cursor.eof();
                        } else {
                            return cursor.next();
                        }
                    } catch (SqlJetException e) {
                        return false;
                    }
                }

                @Override
                public T next() throws DatabaseException {
                    try {
                        T model = modelClass.newInstance();
                        Map<String, Object> itemMap = getItemMap(cursor, colNames);
                        model.load(itemMap);

                        if (model instanceof BaseSqliteModel) {
                            ((BaseSqliteModel) model).setRowId((Long) itemMap.get("__key__"));
                        }

                        return model;
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new DatabaseException(e);
                    }
                }

                @Override
                public void close() throws DatabaseException {
                    try {
                        cursor.close();
                    } catch (SqlJetException e) {
                        throw new DatabaseException(e);
                    }
                }
            };
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public <T extends IDatabaseModel> IDatabaseCursor<T> execute(final Class<T> modelClass, DatabaseQuery query) throws DatabaseException {
        try {
            ISqlJetTable table = getTable(modelClass);


            mDatabase.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            final DatabaseQuery q = new DatabaseQuery(query);
            final String[] colNames = getColNames(table);
            final ISqlJetCursor cursor = table.open();

            return new IDatabaseCursor<T>() {
                private Map<String, Object> mCurrentItem;

                @Override
                public boolean hasNext() throws DatabaseException {
                    try {
                        if (q.getLimit() == 0)
                            return false;

                        if (cursor.eof())
                            return false;

                        do {
                            mCurrentItem = getItemMap(cursor, colNames);
                            if (q.getFilter() != null) {
                                if (q.getFilter().evaluate(cursor.getRowId(), mCurrentItem)) {
                                    q.decrementLimit();
                                    cursor.next();
                                    return true;
                                }
                            } else {
                                cursor.next();
                                return true;
                            }
                        } while(cursor.next());
                        return false;
                    } catch (SqlJetException e) {
                        throw new DatabaseException(e);
                    }
                }

                @Override
                public T next() throws DatabaseException {
                    try {
                        T model = modelClass.newInstance();
                        model.load(mCurrentItem);

                        if (model instanceof BaseSqliteModel) {
                            ((BaseSqliteModel) model).setRowId((Long) mCurrentItem.get("__key__"));
                        }
                        return model;
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new DatabaseException(e);
                    }
                }

                @Override
                public void close() throws DatabaseException {
                    try {
                        cursor.close();
                    } catch (SqlJetException e) {
                        throw new DatabaseException(e);
                    }
                }
            };
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean delete(Class<? extends IDatabaseModel> modelClass, final Object lookupKey) throws DatabaseException {
        try {
            ISqlJetTable table = getTable(modelClass);

            mDatabase.beginTransaction(SqlJetTransactionMode.WRITE);
            new SqlJetCursorTraverser(table) {
                @Override
                protected boolean traverse(Object key, Map<String, Object> item) {
                    return key == lookupKey;
                }

                @Override
                protected boolean onFound(ISqlJetCursor cursor) throws SqlJetException {
                    cursor.delete();
                    return true;
                }
            }.run();
        } catch (SqlJetException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void commit() throws DatabaseException {
        try {
            mDatabase.commit();
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }
    }

    public void close() throws DatabaseException {
        try {
            mDatabase.close();
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Open an instance of the table; create it if it doesn't exist
     * @param modelClass The model
     * @return the opened table
     * @throws SqlJetException
     */
    private ISqlJetTable getTable(Class<? extends IDatabaseModel> modelClass) throws SqlJetException {
        SqliteTable tableAnnotation = modelClass.getAnnotation(SqliteTable.class);

        String tableName;
        if (tableAnnotation == null) {
            tableName = modelClass.getSimpleName();
        } else {
            tableName = tableAnnotation.name();
        }

        try {
            return mDatabase.getTable(tableName);
        } catch (SqlJetException e) {
            String createStmt = "CREATE TABLE \"" + tableName + "\" (";

            Map<String, SqliteForeignKey> fks = new HashMap<>();

            for (Field f : modelClass.getDeclaredFields()) {
                if (!Modifier.isPublic(f.getModifiers()))
                    continue;

                SqliteColumn column = f.getAnnotation(SqliteColumn.class);
                if (column == null)
                    continue;

                String name = column.name();
                if (name.equals(""))
                    CaseTools.camelToSnake(f.getName());

                createStmt += "\"" + name + "\" " + column.type().getName();

                if (column.primaryKey())
                    createStmt += " PRIMARY KEY";

                createStmt += ",";

                SqliteForeignKey foreignKey = f.getAnnotation(SqliteForeignKey.class);
                if (foreignKey != null) {
                    fks.put(name, foreignKey);
                }
            }

            for (Map.Entry<String, SqliteForeignKey> entry : fks.entrySet()) {
                String name = entry.getKey();
                SqliteForeignKey foreignKey = entry.getValue();

                createStmt += " FOREIGN KEY(\"" + name + "\") REFERENCES " + "\"" +
                        foreignKey.table().getAnnotation(SqliteTable.class).name() + "\" (\"" + foreignKey.column() + "\"),";
            }

            mDatabase.createTable(createStmt.substring(0, createStmt.length() - 1) + ");");

            return mDatabase.getTable(tableName);
        }
    }

    private String[] getColNames(ISqlJetTable table) throws SqlJetException {
        List<ISqlJetColumnDef> colDefs = table.getDefinition().getColumns();
        String[] ret = new String[colDefs.size()];
        for (ISqlJetColumnDef colDef : colDefs) {
            ret[colDef.getIndex()] = colDef.getName();
        }

        return ret;
    }

    private Map<String, Object> getItemMap(ISqlJetCursor cursor, String[] colNames) throws DatabaseException {
        Map<String, Object> ret = new HashMap<>();

        try {
            ret.put("__key__", cursor.getRowId());
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }

        for (String col : colNames) {
            try {
                ret.put(col, cursor.getValue(col));
            } catch (SqlJetException e) {
                return null;
            }
        }

        return ret;
    }

    private abstract class SqlJetCursorTraverser {
        private ISqlJetTable mTable;
        protected String[] mColNames;

        public SqlJetCursorTraverser(ISqlJetTable table) throws SqlJetException {
            mTable = table;
            mColNames = getColNames(table);
        }

        protected abstract boolean traverse(Object key, Map<String, Object> item);

        protected abstract boolean onFound(ISqlJetCursor cursor) throws SqlJetException, DatabaseException;

        public void run() throws DatabaseException {
            try {
                mDatabase.beginTransaction(SqlJetTransactionMode.READ_ONLY);
                ISqlJetCursor cursor = mTable.open();
                if (!cursor.eof()) {
                    do {
                        Map<String, Object> itemMap = getItemMap(cursor, mColNames);

                        if (traverse(cursor.getRowId(), itemMap)) {
                            if (onFound(cursor)) {
                                break;
                            }
                        }
                    } while (cursor.next());
                }
                cursor.close();
            } catch (SqlJetException e) {
                throw new DatabaseException(e);
            }
        }
    }

    private abstract class SqlJetItemTraverser extends SqlJetCursorTraverser {
        private List<Map<String, Object>> mItems;
        private boolean mSingle;

        public SqlJetItemTraverser(ISqlJetTable table) throws SqlJetException {
            super(table);
        }

        @Override
        protected boolean onFound(ISqlJetCursor cursor) throws DatabaseException {
            mItems.add(getItemMap(cursor, mColNames));
            return mSingle;
        }

        public Map<String, Object> getOne() throws DatabaseException {
            mItems = new ArrayList<>();
            mSingle = true;

            run();

            if (mItems.size() > 0)
                return mItems.get(0);

            return null;
        }

        public List<Map<String, Object>> getAll() throws DatabaseException {
            mItems = new ArrayList<>();
            mSingle = false;

            run();

            return mItems;
        }
    }
}
