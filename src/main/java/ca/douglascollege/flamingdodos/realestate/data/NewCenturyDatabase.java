package ca.douglascollege.flamingdodos.realestate.data;

import ca.douglascollege.flamingdodos.realestate.data.models.Agent;
import ca.douglascollege.flamingdodos.realestate.data.services.AgentService;
import ca.douglascollege.flamingdodos.realestate.data.services.BaseService;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;

public class NewCenturyDatabase {
    public static final String DATABASE_PATH =
            System.getProperty("user.home") + "/.flamingdodos/new-century-realty/data.bin";

    private SqlJetDb mDatabase;

    public NewCenturyDatabase() throws SqlJetException {
        File file = new File(DATABASE_PATH);
        mDatabase = SqlJetDb.open(file, true);
    }

    public BaseService<Agent> getAgentService() {
        return new AgentService(mDatabase);
    }

    public void commit() throws SqlJetException {
        mDatabase.commit();
    }

    public void close() throws SqlJetException {
        mDatabase.close();
    }
}
