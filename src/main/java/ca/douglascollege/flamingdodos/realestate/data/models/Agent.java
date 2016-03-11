package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.realestate.data.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.realestate.data.enums.SqliteDataTypes;

public class Agent extends SimpleModel {
    @SqliteColumn(SqliteDataTypes.Text)
    public String firstName;

    @SqliteColumn(SqliteDataTypes.Text)
    public String lastName;
}
