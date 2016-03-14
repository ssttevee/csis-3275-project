package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.database.models.SimpleModel;

import java.sql.Date;

public class SaleTransactionModel extends SimpleModel {
    @SqliteColumn(SqliteDataTypes.Integer)
    @SqliteForeignKey(table = PropertyListingModel.class, column = "id")
    public long listingId;

    @SqliteColumn(SqliteDataTypes.Integer)
    @SqliteForeignKey(table = CustomerModel.class, column = "id")
    public long buyerId;

    @SqliteColumn(SqliteDataTypes.Integer)
    public Date date;

    @SqliteColumn(SqliteDataTypes.Real)
    public double amount;

    @Override
    protected String getTableName() {
        return "Sale Transaction";
    }
}
