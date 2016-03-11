package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.realestate.data.models.Agent;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class AgentService extends BaseService<Agent> {

    public AgentService(SqlJetDb db) {
        super(db, Agent.class);
    }

}
