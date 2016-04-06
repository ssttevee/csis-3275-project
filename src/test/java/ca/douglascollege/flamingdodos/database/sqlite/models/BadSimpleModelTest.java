package ca.douglascollege.flamingdodos.database.sqlite.models;

import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.sqlite.enums.SqliteDataTypes;
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
        new SimpleSqliteModel() {
            @SqliteColumn(type = SqliteDataTypes.Integer)
            @SqliteForeignKey(table = SimpleModelSansPublicNullaryConstructor.class, column = "id")
            public long sansPublicNullaryId;
        }.getCreateTableStatement(mDatabase);
    }

    @Test(expected = AssertionError.class)
    public void testForeignKeySansNullaryConstructor() throws Exception {
        new SimpleSqliteModel() {
            @SqliteColumn(type = SqliteDataTypes.Integer)
            @SqliteForeignKey(table = SimpleSqliteModel.class, column = "id")
            public long abstractId;
        }.getCreateTableStatement(mDatabase);
    }

    @Test(expected = AssertionError.class)
    public void testForeignKeySansPublicNullaryConstructor() throws Exception {
        new SimpleSqliteModel() {
            @SqliteColumn(type = SqliteDataTypes.Integer)
            @SqliteForeignKey(table = SimpleModelSansNullaryConstructor.class, column = "id")
            public long sansNullaryId;
        }.getCreateTableStatement(mDatabase);
    }

    private static class SimpleModelSansNullaryConstructor extends SimpleSqliteModel {
        public SimpleModelSansNullaryConstructor(Object obj) {}
    }

    private static class SimpleModelSansPublicNullaryConstructor extends SimpleSqliteModel {
        private SimpleModelSansPublicNullaryConstructor() {}
    }
}
