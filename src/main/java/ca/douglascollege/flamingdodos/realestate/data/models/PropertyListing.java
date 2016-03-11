package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.realestate.data.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.realestate.data.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.realestate.data.enums.SqliteDataTypes;

public class PropertyListing extends SimpleModel {
    @SqliteColumn(SqliteDataTypes.Integer)
    @SqliteForeignKey(table = Agent.class, column = "id")
    public long agentId;
}
