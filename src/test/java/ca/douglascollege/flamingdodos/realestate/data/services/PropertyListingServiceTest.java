package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListing;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import static org.junit.Assert.assertEquals;

public class PropertyListingServiceTest extends BaseServiceTest<PropertyListing> {
    @Override
    protected BaseService<PropertyListing> createService(SqlJetDb db) {
        return new PropertyListingService(db);
    }

    @Override
    protected PropertyListing populateModelWithEvaulatableData(PropertyListing model) {
        model.agentId = 1;

        return model;
    }

    @Override
    protected PropertyListing populateModelWithPhonyData(PropertyListing model) {
        model.agentId = 2;

        return model;
    }

    @Override
    protected void evaluateModelData(PropertyListing model) throws Exception {
        assertEquals(1, model.agentId);
    }
}
