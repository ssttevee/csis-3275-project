package ca.douglascollege.flamingdodos.database.models;

import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class BadSimpleModelTest {
    private SqlJetDb mDatabase;

    @Before
    public void setUp() throws Exception {
        mDatabase = new SqlJetDb(SqlJetDb.IN_MEMORY, false);
    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
        mDatabase = null;
    }

    @Test(expected = AssertionError.class)
    public void testForeignKeyWithAbstractModel() throws Exception {
        new SimpleModel() {
            @SqliteColumn(value = SqliteDataTypes.Integer)
            @SqliteForeignKey(table = SimpleModelSansPublicNullaryConstructor.class, column = "id")
            public long sansPublicNullaryId;
        }.createTable(mDatabase);
    }

    @Test(expected = AssertionError.class)
    public void testForeignKeySansNullaryConstructor() throws Exception {
        new SimpleModel() {
            @SqliteColumn(value = SqliteDataTypes.Integer)
            @SqliteForeignKey(table = SimpleModel.class, column = "id")
            public long abstractId;
        }.createTable(mDatabase);
    }

    @Test(expected = AssertionError.class)
    public void testForeignKeySansPublicNullaryConstructor() throws Exception {
        new SimpleModel() {
            @SqliteColumn(value = SqliteDataTypes.Integer)
            @SqliteForeignKey(table = SimpleModelSansNullaryConstructor.class, column = "id")
            public long sansNullaryId;
        }.createTable(mDatabase);
    }

    private static class SimpleModelSansNullaryConstructor extends SimpleModel {
        public SimpleModelSansNullaryConstructor(Object obj) {}
    }

    private static class SimpleModelSansPublicNullaryConstructor extends SimpleModel {
        private SimpleModelSansPublicNullaryConstructor() {}
    }
}
