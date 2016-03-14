package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.models.CustomerModel;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class CustomerService extends BaseService<CustomerModel> {
    public CustomerService(SqlJetDb db) {
        super(db, CustomerModel.class);
    }
}
