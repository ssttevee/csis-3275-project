package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.realestate.data.models.Agent;
import org.junit.Test;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import static org.junit.Assert.assertEquals;

public class AgentServiceTest extends BaseServiceTest<Agent> {
    public static final String TEST_DATA_FIRST_NAME = "Victor";
    public static final String TEST_DATA_LAST_NAME = "Choong";

    @Override
    protected BaseService<Agent> createService(SqlJetDb db) {
        return new AgentService(db);
    }

    @Override
    protected Agent populateModelWithEvaulatableData(Agent agent) {
        agent.firstName = TEST_DATA_FIRST_NAME;
        agent.lastName = TEST_DATA_LAST_NAME;

        return agent;
    }

    @Override
    protected Agent populateModelWithPhonyData(Agent agent) {
        agent.firstName = "Phony";
        agent.lastName = "Data";

        return agent;
    }

    @Override
    protected void evaluateModelData(Agent agent) throws Exception {
        assertEquals(TEST_DATA_FIRST_NAME, agent.firstName);
        assertEquals(TEST_DATA_LAST_NAME, agent.lastName);
    }
}
