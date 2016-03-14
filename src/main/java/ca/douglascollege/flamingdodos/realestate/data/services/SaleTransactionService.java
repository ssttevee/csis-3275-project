package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.models.SaleTransactionModel;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class SaleTransactionService extends BaseService<SaleTransactionModel> {
    public SaleTransactionService(SqlJetDb db) {
        super(db, SaleTransactionModel.class);
    }
}
