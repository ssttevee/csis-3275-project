package ca.douglascollege.flamingdodos.realestate.data;

import ca.douglascollege.flamingdodos.realestate.data.models.AgentModel;
import ca.douglascollege.flamingdodos.realestate.data.models.CustomerModel;
import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel;
import ca.douglascollege.flamingdodos.realestate.data.models.SaleTransactionModel;
import ca.douglascollege.flamingdodos.realestate.data.services.AgentService;
import ca.douglascollege.flamingdodos.database.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.services.CustomerService;
import ca.douglascollege.flamingdodos.realestate.data.services.PropertyListingService;
import ca.douglascollege.flamingdodos.realestate.data.services.SaleTransactionService;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NewCenturyDatabase {
    private static NewCenturyDatabase instance;

    public static NewCenturyDatabase getInstance() throws SqlJetException {
        if (instance == null)
            instance = new NewCenturyDatabase();

        return instance;
    }

    public static NewCenturyDatabase getTestInstance() throws SqlJetException {
        return new NewCenturyDatabase(SqlJetDb.IN_MEMORY);
    }

    public static final String DATABASE_PATH =
            System.getProperty("user.home") + "/.flamingdodos/new-century-realty/data.bin";

    private SqlJetDb mDatabase;

    private NewCenturyDatabase(File file) throws SqlJetException {
        boolean newdb = !file.exists();

        mDatabase = SqlJetDb.open(file, true);

        if (newdb) {
            try {
                initialize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private NewCenturyDatabase() throws SqlJetException {
        this(new File(DATABASE_PATH));
    }

    public BaseService<AgentModel> getAgentService() {
        return new AgentService(mDatabase);
    }

    public BaseService<CustomerModel> getCustomerService() {
        return new CustomerService(mDatabase);
    }

    public BaseService<PropertyListingModel> getPropertyListingService() {
        return new PropertyListingService(mDatabase);
    }

    public BaseService<SaleTransactionModel> getSaleTransactionService() {
        return new SaleTransactionService(mDatabase);
    }

    public void commit() throws SqlJetException {
        mDatabase.commit();
    }

    public void close() throws SqlJetException {
        mDatabase.close();
    }

    protected void initialize() throws IOException, SqlJetException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("generated_names.csv");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        List<String[]> data = new ArrayList<>();
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            List<String> lineData = new ArrayList<>();
            String valueBuffer = "";

            boolean inQuotes = false;
            boolean escNext = false;
            char[] chars = line.toCharArray();
            for (char c : chars) {
                if (c == '\\') {
                    escNext = true;
                    continue;
                }

                if (c == '"' && !escNext) {
                    inQuotes = !inQuotes;
                    continue;
                }

                if (c == ',' && !inQuotes) {
                    lineData.add(valueBuffer);
                    valueBuffer = "";
                    continue;
                }

                valueBuffer += c;

                if (escNext) escNext = false;
            }

            if (valueBuffer.length() > 0) lineData.add(valueBuffer);

            data.add(lineData.toArray(new String[lineData.size()]));
        }

        // create services
        BaseService<AgentModel> agentService = getAgentService();
        BaseService<CustomerModel> customerService = getCustomerService();
        BaseService<PropertyListingModel> propertyListingService = getPropertyListingService();
        BaseService<SaleTransactionModel> saleTransactionService = getSaleTransactionService();

        int pId = 0;

        // add agents
        AgentModel[] agents = new AgentModel[5];
        for (int i = 0; i < 5; i++) {
            String[] person = data.get(pId++);

            AgentModel agent = agentService.newModel();
            agent.firstName = person[0];
            agent.lastName = person[1];

            agent.hireDate = getRandomDate(Timestamp.valueOf("2016-01-01 00:00:00").getTime(), System.currentTimeMillis());

            agentService.save(agent);

            agents[i] = agent;
        }

        // add property listings
        PropertyListingModel[] listings = new PropertyListingModel[20];
        for (int i = 0; i < 20; i++) {
            String[] person = data.get(pId++);

            CustomerModel customer = customerService.newModel();
            customer.firstName = person[0];
            customer.lastName = person[1];

            customerService.save(customer);

            PropertyListingModel propertyListing = propertyListingService.newModel();

            propertyListing.agentId = agents[(int) (Math.random() * 5)].getRowId();
            propertyListing.customerId = customer.getRowId();
            propertyListing.askingPrice = (500000 + Math.random()*1500000);
            propertyListing.propertyType = PropertyListingModel.PropertyType.RESIDENTIAL;
            propertyListing.buildingType = PropertyListingModel.BuildingType.values()[(int) (Math.random()*PropertyListingModel.BuildingType.values().length)];
            propertyListing.address = person[2] + ", " + person[3] + ", " + person[4] + ", " + person[6] + ", " + person[5];
            propertyListing.floorArea = 400 + Math.random() * 21000;
            propertyListing.landArea = 600 + Math.random() * 24000;
            propertyListing.bedroomCount = (long) (1 + Math.random() * 7);
            propertyListing.bathroomCount = (long) (1 + Math.random() * 4);
            propertyListing.buildYear = (long) (1900 + Math.random() * 115);
            propertyListing.status = PropertyListingModel.PropertyStatus.FOR_SALE;
            propertyListing.listDate = getRandomDate(Timestamp.valueOf("2016-01-01 00:00:00").getTime(), System.currentTimeMillis());

            propertyListingService.save(propertyListing);

            listings[i] = propertyListing;
        }

        int soldListings = 0;
        while (soldListings < 10) {
            PropertyListingModel listing = listings[(int) (Math.random() * listings.length)];

            if (listing.status == PropertyListingModel.PropertyStatus.FOR_SALE) {
                String[] person = data.get(pId++);

                CustomerModel customer = customerService.newModel();
                customer.firstName = person[0];
                customer.lastName = person[1];

                customerService.save(customer);

                SaleTransactionModel txn = saleTransactionService.newModel();
                txn.listingId = listing.getRowId();
                txn.buyerId = customer.getRowId();
                txn.amount = listing.askingPrice * (1 + Math.random() * 0.5);
                txn.date = getRandomDate(listing.listDate.getTime(), System.currentTimeMillis());

                saleTransactionService.save(txn);

                listing.status = PropertyListingModel.PropertyStatus.SOLD;

                propertyListingService.save(listing);

                soldListings++;
            }
        }

        commit();
    }

    private Date getRandomDate(long start, long end) {
        long diff = end - start + 1;
        Timestamp rand = new Timestamp(start + (long)(Math.random() * diff));

        return new Date(rand.getTime());
    }
}
