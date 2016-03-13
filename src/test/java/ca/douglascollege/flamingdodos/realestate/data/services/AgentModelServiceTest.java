package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.models.AgentModel;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import static org.junit.Assert.assertEquals;

public class AgentModelServiceTest extends BaseServiceTest<AgentModel> {
    public static final String TEST_DATA_FIRST_NAME = "Victor";
    public static final String TEST_DATA_LAST_NAME = "Choong";

    @Override
    protected BaseService<AgentModel> createService(SqlJetDb db) {
        return new AgentService(db);
    }

    @Override
    protected AgentModel populateModelWithEvaulatableData(AgentModel agent) {
        agent.firstName = TEST_DATA_FIRST_NAME;
        agent.lastName = TEST_DATA_LAST_NAME;

        return agent;
    }

    @Override
    protected AgentModel populateModelWithPhonyData(AgentModel agent) {
        agent.firstName = "Phony";
        agent.lastName = "Data";

        return agent;
    }

    @Override
    protected void evaluateModelData(AgentModel agent) throws Exception {
        assertEquals(TEST_DATA_FIRST_NAME, agent.firstName);
        assertEquals(TEST_DATA_LAST_NAME, agent.lastName);
    }
}
