package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteTable;
import ca.douglascollege.flamingdodos.database.sqlite.enums.SqliteDataType;
import ca.douglascollege.flamingdodos.database.sqlite.models.SimpleSqliteModel;

import static ca.douglascollege.flamingdodos.realestate.data.models.CustomerModel.TABLE_CUSTOMER;

@SqliteTable(name = TABLE_CUSTOMER)
public class CustomerModel extends SimpleSqliteModel {
    public static final String TABLE_CUSTOMER = "Customer";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_DELETED = "deleted";

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_ID, primaryKey = true)
    public Long id;

    @SqliteColumn(type = SqliteDataType.Text, name = COLUMN_FIRST_NAME)
    public String firstName;

    @SqliteColumn(type = SqliteDataType.Text, name = COLUMN_LAST_NAME)
    public String lastName;

    @SqliteColumn(type = SqliteDataType.Text, name = COLUMN_PHONE_NUMBER)
    public String phoneNumber;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_DELETED)
    public Boolean deleted = false;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
