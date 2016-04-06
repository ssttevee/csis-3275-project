package ca.douglascollege.flamingdodos.database.sqlite.models;

import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseModel;

import java.util.Map;

/**
 * <p>
 *     Similar like a POJO
 * </p>
 *
 * @author Steve Lam (hello@stevelam.ca)
 */
public abstract class BaseSqliteModel implements IDatabaseModel {
    private long mRowId;

    @Override
    public abstract Map<String, Object> export();

    /**
     * Get the id of the model
     * @return the id of the model
     */
    public long getRowId() {
        return mRowId;
    }

    /**
     * Set the id of the model
     */
    public void setRowId(long rowId) {
        mRowId = rowId;
    }
}
