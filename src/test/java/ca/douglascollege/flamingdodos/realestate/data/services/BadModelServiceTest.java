package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.sqlite.models.SimpleSqliteModel;
import org.junit.After;
import org.junit.Before;
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

    private static class BadModel extends SimpleSqliteModel {
        private BadModel() {}
    }
}

