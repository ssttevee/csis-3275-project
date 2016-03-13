package ca.douglascollege.flamingdodos.realestate.data.services;

import ca.douglascollege.flamingdodos.database.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.models.AgentModel;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class AgentService extends BaseService<AgentModel> {

    public AgentService(SqlJetDb db) {
        super(db, AgentModel.class);
    }

}
