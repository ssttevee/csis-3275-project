package ca.douglascollege.flamingdodos.realestate.data.models;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.schema.SqlJetConflictAction;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.util.Map;

public abstract class BaseModel {
    // private SqlJetDb mDatabase;
    protected String mTableName;

    protected long mRowId;

    protected BaseModel() {
        mTableName = this.getClass().getSimpleName();
    }

    protected abstract String getCreateTableStatement();

    protected abstract Map<String, Object> toDataMap();

    protected abstract void fromCursor(ISqlJetCursor readCursor) throws SqlJetException;

    public long getRowId() {
        return mRowId;
    }

    public ISqlJetTable getTable(SqlJetDb db) throws SqlJetException {
        try {
            return db.getTable(mTableName);
        } catch (SqlJetException e1) {
            db.createTable(getCreateTableStatement());
            return db.getTable(mTableName);
        }
    }

    public final long save(SqlJetDb db) throws SqlJetException {
        ISqlJetTable table = getTable(db);

        if (mRowId == 0) {
            mRowId = table.insertByFieldNamesOr(SqlJetConflictAction.REPLACE, toDataMap());
        } else {
            // open the table and traverse to the correct row
            ISqlJetCursor updateCursor = table.open();
            updateCursor.goToRow(mRowId);

            // update the row's fields
            save(updateCursor);

            // finally close the cursor
            updateCursor.close();
        }

        return mRowId;
    }

    public final void save(ISqlJetCursor updateCursor) throws SqlJetException {
        updateCursor.updateByFieldNamesOr(SqlJetConflictAction.REPLACE, toDataMap());
    }

    public final boolean load(ISqlJetCursor readCursor) throws SqlJetException {
        try {
            mRowId = readCursor.getRowId();
            fromCursor(readCursor);
        } catch (SqlJetException e) {
            return false;
        }

        return true;
    }
}
