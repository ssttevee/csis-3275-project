package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteTable;
import ca.douglascollege.flamingdodos.database.sqlite.enums.SqliteDataType;
import ca.douglascollege.flamingdodos.database.sqlite.models.SimpleSqliteModel;

import java.sql.Date;

import static ca.douglascollege.flamingdodos.realestate.data.models.SaleTransactionModel.TABLE_SALE_TRANSACTION;

@SqliteTable(name = TABLE_SALE_TRANSACTION)
public class SaleTransactionModel extends SimpleSqliteModel {
    public static final String TABLE_SALE_TRANSACTION = "Sale Transaction";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LISTING_ID = "listing_id";
    public static final String COLUMN_BUYER_ID = "buyer_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DELETED = "deleted";

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_ID, primaryKey = true)
    public Long id;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_LISTING_ID)
    @SqliteForeignKey(table = PropertyListingModel.class, column = "id")
    public Long listingId;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_BUYER_ID)
    @SqliteForeignKey(table = CustomerModel.class, column = "id")
    public Long buyerId;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_DATE)
    public Date date;

    @SqliteColumn(type = SqliteDataType.Real, name = COLUMN_AMOUNT)
    public double amount;

    @SqliteColumn(type = SqliteDataType.Integer, name = COLUMN_DELETED)
    public Boolean deleted = false;

    public double getAgentFee() {
        double fee;

        if (amount < 100000) {
            fee = amount * 0.08;
        } else {
            fee = 8000 + (amount - 100000) * 0.03;
        }

        return fee;
    }
}
