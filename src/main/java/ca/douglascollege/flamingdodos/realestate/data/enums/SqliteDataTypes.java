package ca.douglascollege.flamingdodos.realestate.data.enums;

public enum SqliteDataTypes {
    Text("TEXT"),
    Integer("INTEGER"),
    Numeric("NUMERIC"),
    Real("REAL"),
    None("NONE"),
    Boolean("BOOLEAN"),
    Byte("TINYINT"),
    Short("SMALLINT"),
    Long("BIGINT"),
    Blob("BLOB"),
    Date("DATE"),
    Time("TIME"),
    Timestamp("TIMESTAMP");

    private String mName;

    SqliteDataTypes(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
