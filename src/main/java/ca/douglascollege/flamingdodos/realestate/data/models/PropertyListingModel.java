package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.database.models.SimpleModel;

public class PropertyListingModel extends SimpleModel {
    public enum PropertyType {
        RESIDENTIAL, COMMERCIAL, OTHER
    }

    public enum BuildingType {
        CONDOMINIUM, TOWN_HOUSE, SINGLE_FAMILY_HOME, DUPLEX, OTHER
    }

    public enum PropertyStatus {
        DRAFT, FOR_SALE, SOLD
    }

    @SqliteColumn(SqliteDataTypes.Integer)
    @SqliteForeignKey(table = AgentModel.class, column = "id")
    public long agentId;

    @SqliteColumn(SqliteDataTypes.Integer)
    @SqliteForeignKey(table = CustomerModel.class, column = "id")
    public long customerId;

    @SqliteColumn(SqliteDataTypes.Real)
    public double askingPrice;

    @SqliteColumn(SqliteDataTypes.Text)
    public PropertyType propertyType;

    @SqliteColumn(SqliteDataTypes.Text)
    public BuildingType buildingType;

    @SqliteColumn(SqliteDataTypes.Text)
    public String address;

    @SqliteColumn(SqliteDataTypes.Real)
    public double floorArea;

    @SqliteColumn(SqliteDataTypes.Real)
    public double landArea;

    @SqliteColumn(SqliteDataTypes.Integer)
    public long bedroomCount;

    @SqliteColumn(SqliteDataTypes.Integer)
    public long bathroomCount;

    @SqliteColumn(SqliteDataTypes.Integer)
    public long buildYear;

    @SqliteColumn(SqliteDataTypes.Text)
    public PropertyStatus status;

    @Override
    protected String getTableName() {
        return "Property Listing";
    }
}
