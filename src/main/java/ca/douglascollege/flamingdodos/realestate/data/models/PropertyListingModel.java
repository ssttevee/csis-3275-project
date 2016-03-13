package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.database.models.SimpleModel;

public class PropertyListingModel extends SimpleModel {
    @SqliteColumn(SqliteDataTypes.Integer)
    @SqliteForeignKey(table = AgentModel.class, column = "id")
    public long agentId;
}
