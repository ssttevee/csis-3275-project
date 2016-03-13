package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class PropertyListingService extends BaseService<PropertyListingModel> {

    public PropertyListingService(SqlJetDb db) {
        super(db, PropertyListingModel.class);
    }

}
