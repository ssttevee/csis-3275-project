package ca.douglascollege.flamingdodos.database.models;

import ca.douglascollege.flamingdodos.database.services.BaseService;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.schema.SqlJetConflictAction;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.util.Map;

/**
 * <p>
 *     Similar like a POJO, but handles some logic
 * </p>
 *
 * <p>
 *     Models should not be accessed directly, rather by a subclass of {@link BaseService}
 * </p>
 *
 * @author Steve Lam (hello@stevelam.ca)
 */
public abstract class BaseModel {
    // private SqlJetDb mDatabase;
    protected String mTableName;

    protected long mRowId;

    protected BaseModel() {
        mTableName = getTableName();
    }

    /**
     * Get the table name
     * @return table name
     */
    protected abstract String getTableName();

    /**
     * Get the `CREATE TABLE` SQL statement for this model
     * @return SQL statement beginning with `CREATE TABLE`
     */
    protected abstract String getCreateTableStatement();

    /**
     * Get all the data in the model in a {@link Map}
     * @return {@link Map} of all the data in the model
     */
    protected abstract Map<String, Object> toDataMap();

    /**
     * Extract data from the cursor and replace this model's data with said data
     * @param readCursor the cursor from a read call or transaction
     * @throws SqlJetException
     */
    protected abstract void fromCursor(ISqlJetCursor readCursor) throws SqlJetException;

    /**
     * Get the id of the model
     * @return the id of the model
     */
    public long getRowId() {
        return mRowId;
    }

    /**
     * Open an instance of the table; create it if it doesn't exist
     * @param db an instance of the database
     * @return the opened table
     * @throws SqlJetException
     */
    public ISqlJetTable getTable(SqlJetDb db) throws SqlJetException {
        try {
            return db.getTable(mTableName);
        } catch (SqlJetException e1) {
            db.createTable(getCreateTableStatement());
            return db.getTable(mTableName);
        }
    }

    /**
     * Insert or update this model in the database
     * @param db an instance of the database
     * @return the id of the model
     * @throws SqlJetException
     */
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

    /**
     * Update the cursor with the data from this model
     * @param updateCursor a cursor to update
     * @throws SqlJetException
     */
    public final void save(ISqlJetCursor updateCursor) throws SqlJetException {
        updateCursor.updateByFieldNamesOr(SqlJetConflictAction.REPLACE, toDataMap());
    }

    /**
     * Read data from the cursor into this model
     * @param readCursor a cursor to read from
     * @return whether or not the read was successful
     * @throws SqlJetException
     */
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
