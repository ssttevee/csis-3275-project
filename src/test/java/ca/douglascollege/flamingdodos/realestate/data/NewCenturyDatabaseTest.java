package ca.douglascollege.flamingdodos.realestate.data;

import org.junit.Test;

public class NewCenturyDatabaseTest {
    @Test
    public void testInitialize() throws Exception {
        NewCenturyDatabase ncdb = NewCenturyDatabase.getTestInstance();
        ncdb.initialize();
    }

    @Test
    public void testGetAgentService() throws Exception {
        NewCenturyDatabase ncdb = NewCenturyDatabase.getTestInstance();
        ncdb.getAgentService();
    }

    @Test
    public void testGetCustomerService() throws Exception {
        NewCenturyDatabase ncdb = NewCenturyDatabase.getTestInstance();
        ncdb.getCustomerService();
    }

    @Test
    public void testGetPropertyListingService() throws Exception {
        NewCenturyDatabase ncdb = NewCenturyDatabase.getTestInstance();
        ncdb.getPropertyListingService();
    }

    @Test
    public void testGetSaleTransactionService() throws Exception {
        NewCenturyDatabase ncdb = NewCenturyDatabase.getTestInstance();
        ncdb.getSaleTransactionService();
    }

    @Test
    public void testClose() throws Exception {
        NewCenturyDatabase ncdb = NewCenturyDatabase.getTestInstance();
        ncdb.close();
    }
}