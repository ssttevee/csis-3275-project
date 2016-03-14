package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.database.models.SimpleModel;

public class CustomerModel extends SimpleModel {
    @SqliteColumn(SqliteDataTypes.Text)
    public String firstName;

    @SqliteColumn(SqliteDataTypes.Text)
    public String lastName;

    @Override
    protected String getTableName() {
        return "Customer";
    }
}
