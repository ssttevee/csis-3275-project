package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteTable;
import ca.douglascollege.flamingdodos.database.sqlite.models.SimpleSqliteModel;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.sqlite.enums.SqliteDataTypes;

import java.sql.Date;

import static ca.douglascollege.flamingdodos.realestate.data.models.AgentModel.TABLE_AGENT;

@SqliteTable(name = TABLE_AGENT)
public class AgentModel extends SimpleSqliteModel {
    public static final String TABLE_AGENT = "Agent";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_HIRE_DATE = "hire_date";

    @SqliteColumn(type = SqliteDataTypes.Integer, name = COLUMN_ID, primaryKey = true)
    protected long id;

    @SqliteColumn(type = SqliteDataTypes.Text, name = COLUMN_FIRST_NAME)
    public String firstName;

    @SqliteColumn(type = SqliteDataTypes.Text, name = COLUMN_LAST_NAME)
    public String lastName;

    @SqliteColumn(type = SqliteDataTypes.Integer, name = COLUMN_HIRE_DATE)
    public Date hireDate;

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
    }
}
