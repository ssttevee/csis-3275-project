package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.models.BaseModel;
import ca.douglascollege.flamingdodos.database.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.models.AgentModel;
import ca.douglascollege.flamingdodos.realestate.data.models.CustomerModel;
import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel;
import ca.douglascollege.flamingdodos.realestate.data.models.SaleTransactionModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BaseServiceTest<T extends BaseModel> {
    @Parameterized.Parameters(name = "{3}")
    public static Collection<Object[]> data() {
        List<Object[]> parameters = new ArrayList<>();

        BaseModel evalModel;
        BaseModel phonyModel;

        {
            evalModel = new AgentModel();
            phonyModel = new AgentModel();

            ((AgentModel) evalModel).firstName = "Victor";
            ((AgentModel) phonyModel).firstName = "Simon";

            ((AgentModel) evalModel).lastName = "Choong";
            ((AgentModel) phonyModel).lastName = "Liiii";

            ((AgentModel) evalModel).hireDate = new Date(1457993215547L);
            ((AgentModel) phonyModel).hireDate = new Date(1448732418975L);

            parameters.add(new Object[]{AgentService.class, evalModel, phonyModel, "Agent"});
        }

        {
            evalModel = new CustomerModel();
            phonyModel = new CustomerModel();

            ((CustomerModel) evalModel).firstName = "Donald";
            ((CustomerModel) phonyModel).firstName = "Ronald";

            ((CustomerModel) evalModel).lastName = "Trump";
            ((CustomerModel) phonyModel).lastName = "Reagan";

            parameters.add(new Object[]{CustomerService.class, evalModel, phonyModel, "Customer"});
        }

        {
            evalModel = new PropertyListingModel();
            phonyModel = new PropertyListingModel();

            ((PropertyListingModel) evalModel).agentId = 4;
            ((PropertyListingModel) phonyModel).agentId = 1;

            ((PropertyListingModel) evalModel).customerId = 5;
            ((PropertyListingModel) phonyModel).customerId = 2;

            ((PropertyListingModel) evalModel).askingPrice = 657432.48;
            ((PropertyListingModel) phonyModel).askingPrice = 1574357.67;

            ((PropertyListingModel) evalModel).propertyType = PropertyListingModel.PropertyType.RESIDENTIAL;
            ((PropertyListingModel) phonyModel).propertyType = PropertyListingModel.PropertyType.COMMERCIAL;

            ((PropertyListingModel) evalModel).buildingType = PropertyListingModel.BuildingType.SINGLE_FAMILY_HOME;
            ((PropertyListingModel) phonyModel).buildingType = PropertyListingModel.BuildingType.OTHER;

            ((PropertyListingModel) evalModel).address = "700 Royal Avenue, New Westminister, BC, V3M 5Z5";
            ((PropertyListingModel) phonyModel).address = "1250 Pinetree Way, Coquitlam, BC, V3B 7X3";

            ((PropertyListingModel) evalModel).floorArea = 2687.2;
            ((PropertyListingModel) phonyModel).floorArea = 8561.8;

            ((PropertyListingModel) evalModel).landArea = 2973.7;
            ((PropertyListingModel) phonyModel).landArea = 9065.5;

            ((PropertyListingModel) evalModel).bedroomCount = 2;
            ((PropertyListingModel) phonyModel).bedroomCount = 6;

            ((PropertyListingModel) evalModel).bathroomCount = 2;
            ((PropertyListingModel) phonyModel).bathroomCount = 4;

            ((PropertyListingModel) evalModel).buildYear = 2004;
            ((PropertyListingModel) phonyModel).buildYear = 2011;

            ((PropertyListingModel) evalModel).status = PropertyListingModel.PropertyStatus.FOR_SALE;
            ((PropertyListingModel) phonyModel).status = PropertyListingModel.PropertyStatus.SOLD;

            ((PropertyListingModel) evalModel).listDate = new Date(System.currentTimeMillis());
            ((PropertyListingModel) phonyModel).listDate = new Date(System.currentTimeMillis() - 1927865);

            parameters.add(new Object[]{PropertyListingService.class, evalModel, phonyModel, "PropertyListing"});
        }

        {
            evalModel = new SaleTransactionModel();
            phonyModel = new SaleTransactionModel();

            ((SaleTransactionModel) evalModel).listingId = 7;
            ((SaleTransactionModel) phonyModel).listingId = 3;

            ((SaleTransactionModel) evalModel).buyerId = 3;
            ((SaleTransactionModel) phonyModel).buyerId = 6;

            ((SaleTransactionModel) evalModel).date = new Date(1457987387657L);
            ((SaleTransactionModel) phonyModel).date = new Date(1448675435795L);

            ((SaleTransactionModel) evalModel).amount = 897535.68;
            ((SaleTransactionModel) phonyModel).amount = 685745.76;

            parameters.add(new Object[]{SaleTransactionService.class, evalModel, phonyModel, "SaleTransaction"});
        }

        return parameters;
    }

    private SqlJetDb mDatabase;
    private BaseService<T> mService;

    private Class<? extends BaseService<T>> mServiceClass;
    private T mEvaluationModel;
    private T mPhonyModel;

    public BaseServiceTest(Class<? extends BaseService<T>> serviceClass, T evaulationModel, T phonyModel, String name) {
        mServiceClass = serviceClass;
        mEvaluationModel = evaulationModel;
        mPhonyModel = phonyModel;
    }

    private BaseService<T> createService(SqlJetDb db) throws Exception {
        return mServiceClass.getConstructor(SqlJetDb.class).newInstance(db);
    }

    private T populateModelWithEvaulatableData(T model) throws Exception {
        return populateModelWithData(model, mEvaluationModel);
    }

    private T populateModelWithPhonyData(T model) throws Exception {
        return populateModelWithData(model, mPhonyModel);
    }

    private T populateModelWithData(T model, T reference) throws Exception {
        for (Field f : model.getClass().getDeclaredFields()) {
            SqliteColumn column = f.getAnnotation(SqliteColumn.class);
            if (column != null) {
                f.set(model, f.get(reference));
            }
        }

        return model;
    }

    private void evaluateModelData(T model) throws Exception {
        for (Field f : model.getClass().getDeclaredFields()) {
            SqliteColumn column = f.getAnnotation(SqliteColumn.class);
            if (column != null) {
                assertEquals(f.get(model), f.get(mEvaluationModel));
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        mDatabase = SqlJetDb.open(SqlJetDb.IN_MEMORY, true);
        mService = createService(mDatabase);
    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
    }

    @Test
    public void testSaveAndLookup() throws Exception {
        long rowId = mService.save(populateModelWithEvaulatableData(mService.newModel()));

        T model = mService.lookup(rowId);

        assertEquals(rowId, model.getRowId());
        evaluateModelData(model);
    }

    @Test
    public void testUpdate() throws Exception {
        T model = populateModelWithPhonyData(mService.newModel());

        assertEquals("Row ID must be 0 before it is inserted", 0, model.getRowId());

        mService.save(model);

        assertNotEquals("Row ID must not be 0 after it is inserted", 0, model.getRowId());

        populateModelWithEvaulatableData(model);

        long rowId = mService.save(model);

        evaluateModelData(model);

        evaluateModelData(mService.lookup(rowId));
    }

    @Test
    public void testGetAll() throws Exception {
        int count = 10;

        for (int i = 0; i < count; i++) mService.save(populateModelWithEvaulatableData(mService.newModel()));

        T[] models = mService.getAll();

        assertEquals("The number of agents inserted must equal the number of agents returned", count, models.length);

        for (T model : models) evaluateModelData(model);
    }

    @Test
    public void testDelete() throws Exception {
        T model = populateModelWithEvaulatableData(mService.newModel());

        long rowId = mService.save(model);

        assertNotNull(mService.lookup(rowId));

        mService.delete(model);

        assertNull(mService.lookup(rowId));
    }

    public void testEmptyModel() throws Exception {
        T model = mService.newModel();

        long rowId = mService.save(model);

        assertNotNull(mService.lookup(rowId));
    }

    @Test
    public void testNewModel() throws Exception {
        assertNotNull(mService.newModel());
    }
}