package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import static org.junit.Assert.assertEquals;

public class PropertyListingServiceTest extends BaseServiceTest<PropertyListingModel> {
    @Override
    protected BaseService<PropertyListingModel> createService(SqlJetDb db) {
        return new PropertyListingService(db);
    }

    @Override
    protected PropertyListingModel populateModelWithEvaulatableData(PropertyListingModel model) {
        model.agentId = 1;

        return model;
    }

    @Override
    protected PropertyListingModel populateModelWithPhonyData(PropertyListingModel model) {
        model.agentId = 2;

        return model;
    }

    @Override
    protected void evaluateModelData(PropertyListingModel model) throws Exception {
        assertEquals(1, model.agentId);
    }
}
