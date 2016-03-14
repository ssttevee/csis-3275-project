package ca.douglascollege.flamingdodos.database.enums;

import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;

/**
 * This enum is used with {@link SqliteColumn} to identify the type of the column
 */
public enum SqliteDataTypes {
    Text("TEXT"),
    Integer("INTEGER"),
    Numeric("NUMERIC"),
    Real("REAL"),
    None("NONE");

    /**
     * The SQLite Keyword
     */
    private String mName;

    SqliteDataTypes(String name) {
        mName = name;
    }

    /**
     * Get the SQLite Keyword for this data type
     * @return SQLite Keyword
     */
    public String getName() {
        return mName;
    }
}
