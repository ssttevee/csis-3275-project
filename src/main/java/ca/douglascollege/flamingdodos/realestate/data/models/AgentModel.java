package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.models.SimpleModel;
import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;

import java.sql.Date;

public class AgentModel extends SimpleModel {
    @SqliteColumn(SqliteDataTypes.Text)
    public String firstName;

    @SqliteColumn(SqliteDataTypes.Text)
    public String lastName;

    @SqliteColumn(SqliteDataTypes.Integer)
    public Date hireDate;

    @Override
    protected String getTableName() {
        return "Agent";
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
    }
}
