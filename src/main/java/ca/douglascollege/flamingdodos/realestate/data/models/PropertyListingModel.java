package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteTable;
import ca.douglascollege.flamingdodos.database.sqlite.enums.SqliteDataType;
import ca.douglascollege.flamingdodos.database.sqlite.models.SimpleSqliteModel;

import java.sql.Date;

import static ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel.TABLE_PROPERTY_LISTING;

@SqliteTable(name = TABLE_PROPERTY_LISTING)
public class PropertyListingModel extends SimpleSqliteModel {
    public static final String TABLE_PROPERTY_LISTING = "Property Listing";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AGENT_ID = "agent_id";
    public static final String COLUMN_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_ASKING_PRICE = "asking_price";
    public static final String COLUMN_PROPERTY_TYPE = "property_type";
    public static final String COLUMN_BUILDING_TYPE = "building_type";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_FLOOR_AREA = "floor_area";
    public static final String COLUMN_LAND_AREA = "land_area";
    public static final String COLUMN_BEDROOM_COUNT = "bedroom_count";
    public static final String COLUMN_BATHROOM_COUNT = "bathroom_count";
    public static final String COLUMN_BUILD_YEAR = "build_year";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_LIST_DATE = "list_date";
    public static final String COLUMN_DELETED = "deleted";

    public enum PropertyType {
        RESIDENTIAL, COMMERCIAL, OTHER
    }

    public enum BuildingType {
        CONDOMINIUM, TOWN_HOUSE, SINGLE_FAMILY_HOME, DUPLEX, OTHER
    }

    public enum PropertyStatus {
        DRAFT, FOR_SALE, SOLD
    }

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_ID, primaryKey = true)
    public Long id;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_AGENT_ID)
    @SqliteForeignKey(table = AgentModel.class, column = "id")
    public Long agentId;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_CUSTOMER_ID)
    @SqliteForeignKey(table = CustomerModel.class, column = "id")
    public Long customerId;

    @SqliteColumn(type = SqliteDataType.Real, name = COLUMN_ASKING_PRICE)
    public double askingPrice;

    @SqliteColumn(type = SqliteDataType.Text, name = COLUMN_PROPERTY_TYPE)
    public PropertyType propertyType;

    @SqliteColumn(type = SqliteDataType.Text, name = COLUMN_BUILDING_TYPE)
    public BuildingType buildingType;

    @SqliteColumn(type = SqliteDataType.Text, name = COLUMN_ADDRESS)
    public String address;

    @SqliteColumn(type = SqliteDataType.Real, name = COLUMN_FLOOR_AREA)
    public double floorArea;

    @SqliteColumn(type = SqliteDataType.Real, name = COLUMN_LAND_AREA)
    public double landArea;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_BEDROOM_COUNT)
    public Long bedroomCount;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_BATHROOM_COUNT)
    public Long bathroomCount;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_BUILD_YEAR)
    public Long buildYear;

    @SqliteColumn(type = SqliteDataType.Text, name = COLUMN_STATUS)
    public PropertyStatus status;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_LIST_DATE)
    public Date listDate;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_DELETED)
    public Boolean deleted = false;

    @Override
    public String toString() {
        return address;
    }
}
