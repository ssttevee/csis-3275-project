package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.models.SimpleModel;
import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;

public class AgentModel extends SimpleModel {
    @SqliteColumn(SqliteDataTypes.Text)
    public String firstName;

    @SqliteColumn(SqliteDataTypes.Text)
    public String lastName;
}
