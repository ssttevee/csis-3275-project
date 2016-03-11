package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListing;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class PropertyListingService extends BaseService<PropertyListing> {

    public PropertyListingService(SqlJetDb db) {
        super(db, PropertyListing.class);
    }

}
