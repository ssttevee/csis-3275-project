package ca.douglascollege.flamingdodos.realestate.data;

import org.junit.Test;

public class NewCenturyDatabaseTest {
    @Test
    public void testGetAgentService() throws Exception {
        NewCenturyDatabase ncdb = new NewCenturyDatabase();
        ncdb.getAgentService();
    }

    @Test
    public void testCommit() throws Exception {
        NewCenturyDatabase ncdb = new NewCenturyDatabase();
        ncdb.commit();
    }

    @Test
    public void testClose() throws Exception {
        NewCenturyDatabase ncdb = new NewCenturyDatabase();
        ncdb.close();
    }
}