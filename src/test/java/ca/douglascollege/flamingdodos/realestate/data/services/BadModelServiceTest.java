package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.models.SimpleModel;
import ca.douglascollege.flamingdodos.database.services.BaseService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class BadModelServiceTest {
    private SqlJetDb mDatabase;

    @Before
    public void setUp() throws Exception {
        mDatabase = SqlJetDb.open(SqlJetDb.IN_MEMORY, true);
    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
    }

    @Test(expected = AssertionError.class)
    public void testNewBadModel() throws Exception {
        BaseService<BadModel> badModelService = new BaseService<BadModel>(mDatabase, BadModel.class) {};

        badModelService.newModel();
    }

    private static class BadModel extends SimpleModel {
        private BadModel() {}
    }
}

