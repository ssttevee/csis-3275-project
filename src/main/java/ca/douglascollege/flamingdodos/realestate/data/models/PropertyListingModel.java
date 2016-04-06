package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteTable;
import ca.douglascollege.flamingdodos.database.sqlite.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.database.sqlite.models.SimpleSqliteModel;

import java.sql.Date;

import static ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel.TABLE_PROPERTY_LISTING;

@SqliteTable(name = TABLE_PROPERTY_LISTING)
public class PropertyListingModel extends SimpleSqliteModel {
    public static final String TABLE_PROPERTY_LISTING = "Property Listing";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AGENT_ID = "id";
    public static final String COLUMN_CUSTOMER_ID = "id";
    public static final String COLUMN_ASKING_PRICE = "id";
    public static final String COLUMN_PROPERTY_TYPE = "id";
    public static final String COLUMN_BUILDING_TYPE = "id";
    public static final String COLUMN_ADDRESS = "id";
    public static final String COLUMN_FLOOR_AREA = "id";
    public static final String COLUMN_LAND_AREA = "id";
    public static final String COLUMN_BEDROOM_COUNT = "id";
    public static final String COLUMN_BATHROOM_COUNT = "id";
    public static final String COLUMN_BUILD_YEAR = "id";
    public static final String COLUMN_STATUS = "id";
    public static final String COLUMN_LIST_DATE = "id";

    public enum PropertyType {
        RESIDENTIAL, COMMERCIAL, OTHER
    }

    public enum BuildingType {
        CONDOMINIUM, TOWN_HOUSE, SINGLE_FAMILY_HOME, DUPLEX, OTHER
    }

    public enum PropertyStatus {
        DRAFT, FOR_SALE, SOLD
    }

    @SqliteColumn(type = SqliteDataTypes.Integer, name = COLUMN_ID, primaryKey = true)
    protected long id;

    @SqliteColumn(type = SqliteDataTypes.Integer, name = COLUMN_AGENT_ID)
    @SqliteForeignKey(table = AgentModel.class, column = "id")
    public long agentId;

    @SqliteColumn(type = SqliteDataTypes.Integer, name = COLUMN_CUSTOMER_ID)
    @SqliteForeignKey(table = CustomerModel.class, column = "id")
    public long customerId;

    @SqliteColumn(type = SqliteDataTypes.Real, name = COLUMN_ASKING_PRICE)
    public double askingPrice;

    @SqliteColumn(type = SqliteDataTypes.Text, name = COLUMN_PROPERTY_TYPE)
    public PropertyType propertyType;

    @SqliteColumn(type = SqliteDataTypes.Text, name = COLUMN_BUILDING_TYPE)
    public BuildingType buildingType;

    @SqliteColumn(type = SqliteDataTypes.Text, name = COLUMN_ADDRESS)
    public String address;

    @SqliteColumn(type = SqliteDataTypes.Real, name = COLUMN_FLOOR_AREA)
    public double floorArea;

    @SqliteColumn(type = SqliteDataTypes.Real, name = COLUMN_LAND_AREA)
    public double landArea;

    @SqliteColumn(type = SqliteDataTypes.Integer, name = COLUMN_BEDROOM_COUNT)
    public long bedroomCount;

    @SqliteColumn(type = SqliteDataTypes.Integer, name = COLUMN_BATHROOM_COUNT)
    public long bathroomCount;

    @SqliteColumn(type = SqliteDataTypes.Integer, name = COLUMN_BUILD_YEAR)
    public long buildYear;

    @SqliteColumn(type = SqliteDataTypes.Text, name = COLUMN_STATUS)
    public PropertyStatus status;

    @SqliteColumn(type = SqliteDataTypes.Integer, name = COLUMN_LIST_DATE)
    public Date listDate;

    @Override
    public String toString() {
        return address;
    }
}
