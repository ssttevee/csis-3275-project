package ca.douglascollege.flamingdodos.realestate.data;

import ca.douglascollege.flamingdodos.database.exceptions.DatabaseException;
import ca.douglascollege.flamingdodos.database.sqlite.SqliteDatabase;
import ca.douglascollege.flamingdodos.realestate.data.models.AgentModel;
import ca.douglascollege.flamingdodos.realestate.data.models.CustomerModel;
import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel;
import ca.douglascollege.flamingdodos.realestate.data.models.SaleTransactionModel;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NewCenturyDatabase extends SqliteDatabase {
    private static NewCenturyDatabase instance;

    public static NewCenturyDatabase getInstance() throws DatabaseException {
        if (instance == null)
            instance = new NewCenturyDatabase();

        return instance;
    }

    public static NewCenturyDatabase getTestInstance() throws DatabaseException {
        return new NewCenturyDatabase(SqlJetDb.IN_MEMORY);
    }

    public static final String DATABASE_PATH =
            System.getProperty("user.home") + "/.flamingdodos/new-century-realty/data.bin";

    private NewCenturyDatabase(File file) throws DatabaseException {
        super(file);
        boolean newdb = !file.exists();

        if (newdb) {
            try {
                initialize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private NewCenturyDatabase() throws DatabaseException {
        this(new File(DATABASE_PATH));
    }

    protected void initialize() throws IOException, DatabaseException {
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

        int pId = 0;

        // add agents
        AgentModel[] agents = new AgentModel[5];
        for (int i = 0; i < 5; i++) {
            String[] person = data.get(pId++);

            AgentModel agent = new AgentModel();
            agent.firstName = person[0];
            agent.lastName = person[1];

            agent.hireDate = getRandomDate(Timestamp.valueOf("2016-01-01 00:00:00").getTime(), System.currentTimeMillis());

            insert(null, agent);

            agents[i] = agent;
        }

        // add property listings
        PropertyListingModel[] listings = new PropertyListingModel[20];
        for (int i = 0; i < 20; i++) {
            String[] person = data.get(pId++);

            CustomerModel customer = new CustomerModel();
            customer.firstName = person[0];
            customer.lastName = person[1];

            insert(null, customer);

            PropertyListingModel propertyListing = new PropertyListingModel();

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

            insert(null, propertyListing);

            listings[i] = propertyListing;
        }

        int soldListings = 0;
        while (soldListings < 10) {
            PropertyListingModel listing = listings[(int) (Math.random() * listings.length)];

            if (listing.status == PropertyListingModel.PropertyStatus.FOR_SALE) {
                String[] person = data.get(pId++);

                CustomerModel customer = new CustomerModel();
                customer.firstName = person[0];
                customer.lastName = person[1];

                insert(null, customer);

                SaleTransactionModel txn = new SaleTransactionModel();
                txn.listingId = listing.getRowId();
                txn.buyerId = customer.getRowId();
                txn.amount = listing.askingPrice * (1 + Math.random() * 0.5);
                txn.date = getRandomDate(listing.listDate.getTime(), System.currentTimeMillis());

                insert(null, txn);

                listing.status = PropertyListingModel.PropertyStatus.SOLD;

                insert(listing.getRowId(), listing);

                soldListings++;
            }
        }
    }

    private Date getRandomDate(long start, long end) {
        long diff = end - start + 1;
        Timestamp rand = new Timestamp(start + (long)(Math.random() * diff));

        return new Date(rand.getTime());
    }
}
