package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.annotations.SqliteIndex;
import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.database.models.SimpleModel;
import ca.douglascollege.flamingdodos.database.services.BaseService;
import org.tmatesoft.sqljet.core.SqlJetException;

import java.sql.Date;

public class SaleTransactionModel extends SimpleModel {
    public final static String LISTING_ID_INDEX = "listing_id_index";
    public final static String BUYER_ID_INDEX = "buyer_id_index";

    @SqliteColumn(SqliteDataTypes.Integer)
    @SqliteForeignKey(table = PropertyListingModel.class, column = "id")
    @SqliteIndex(LISTING_ID_INDEX)
    public long listingId;

    @SqliteColumn(SqliteDataTypes.Integer)
    @SqliteForeignKey(table = CustomerModel.class, column = "id")
    @SqliteIndex(BUYER_ID_INDEX)
    public long buyerId;

    @SqliteColumn(SqliteDataTypes.Integer)
    public Date date;

    @SqliteColumn(SqliteDataTypes.Real)
    public double amount;

    @Override
    protected String getTableName() {
        return "Sale Transaction";
    }

    public double getAgentFee() {
        double fee;

        if (amount < 100000) {
            fee = amount * 0.08;
        } else {
            fee = 8000 + (amount - 100000) * 0.03;
        }

        return fee;
    }

    public PropertyListingModel getPropertyListing(BaseService<PropertyListingModel> service) {
        try {
            return service.lookup(listingId);
        } catch (SqlJetException e) {
            return null;
        }
    }
}
