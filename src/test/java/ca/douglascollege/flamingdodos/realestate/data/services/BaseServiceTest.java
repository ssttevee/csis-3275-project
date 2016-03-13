package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.models.BaseModel;
import ca.douglascollege.flamingdodos.database.services.BaseService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import static org.junit.Assert.*;

public abstract class BaseServiceTest<T extends BaseModel> {
    private SqlJetDb mDatabase;
    private BaseService<T> mService;

    protected abstract BaseService<T> createService(SqlJetDb db);

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

    @Test
    public void testNewModel() throws Exception {
        assertNotNull(mService.newModel());
    }

    protected abstract T populateModelWithEvaulatableData(T model);

    protected abstract T populateModelWithPhonyData(T model);

    protected abstract void evaluateModelData(T model) throws Exception;
}