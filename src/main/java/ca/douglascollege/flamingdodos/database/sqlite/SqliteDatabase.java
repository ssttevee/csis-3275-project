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
    private SqlJetDb mDatabase;

    public SqliteDatabase(File file) throws DatabaseException {
        try {
            this.mDatabase = SqlJetDb.open(file, true);
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public IDatabaseModel lookup(Class<? extends IDatabaseModel> modelClass, final Object lookupKey) throws DatabaseException {
        try {
            ISqlJetTable table = getTable(modelClass);

            Map<String, Object> found = new SqlJetItemTraverser(table) {
                @Override
                protected boolean traverse(Object key, Map<String, Object> item) {
                    return key == lookupKey;
                }
            }.getOne();

            if (found != null) {
                IDatabaseModel model = modelClass.newInstance();
                model.load(found);

                return model;
            } else {
                return null;
            }

        } catch (SqlJetException | IllegalAccessException | InstantiationException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Object insert(@Nullable Object key, IDatabaseModel source) throws DatabaseException {
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
                id = table.insertWithRowIdOr(SqlJetConflictAction.FAIL, (Long) key, data);
            }

            if (source instanceof BaseSqliteModel)
                ((BaseSqliteModel) source).setRowId(id);

            return id;
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public IDatabaseCursor getAll(Class<? extends IDatabaseModel> modelClass) throws DatabaseException {
        try {
            ISqlJetTable table = getTable(modelClass);

            final ISqlJetCursor cursor = table.open();
            final String[] colNames = getColNames(table);

            return new IDatabaseCursor() {
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
                public Map<String, Object> next() {
                    return getItemMap(cursor, colNames);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } catch (SqlJetException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public IDatabaseCursor execute(final DatabaseQuery query) throws DatabaseException {
        try {
            ISqlJetTable table = getTable(query.getModelClass());

            final Iterator<Map<String, Object>> it = new SqlJetItemTraverser(table) {
                @Override
                protected boolean traverse(Object key, Map<String, Object> item) {
                    return query.getFilter().evaluate(key, item);
                }
            }.getAll().iterator();

            return new IDatabaseCursor() {
                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public Map<String, Object> next() {
                    return it.next();
                }

                @Override
                public void remove() {
                    it.remove();
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

    private Map<String, Object> getItemMap(ISqlJetCursor cursor, String[] colNames) {
        Map<String, Object> ret = new HashMap<>();

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

        protected abstract boolean onFound(ISqlJetCursor cursor) throws SqlJetException;

        public void run() throws SqlJetException {
            ISqlJetCursor cursor = mTable.open();
            if (!cursor.eof()) {
                do {
                    Map<String, Object> itemMap = getItemMap(cursor, mColNames);

                    if (traverse(cursor.getRowId(), itemMap)) {
                        if (onFound(cursor)) {
                            break;
                        }
                    }
                } while(cursor.next());
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
        protected boolean onFound(ISqlJetCursor cursor) {
            mItems.add(getItemMap(cursor, mColNames));
            return mSingle;
        }

        public Map<String, Object> getOne() throws SqlJetException {
            mItems = new ArrayList<>();
            mSingle = true;

            run();

            if (mItems.size() > 0)
                return mItems.get(0);

            return null;
        }

        public List<Map<String, Object>> getAll() throws SqlJetException {
            mItems = new ArrayList<>();
            mSingle = false;

            run();

            return mItems;
        }
    }
}
