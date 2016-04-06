package ca.douglascollege.flamingdodos.database.interfaces;

public class DatabaseQuery {
    private Class<? extends IDatabaseModel> mModelClass;

    private IDatabaseQueryFilter mFilter;

    /**
     *
     * @param modelClass The model class
     */
    public DatabaseQuery(Class<? extends IDatabaseModel> modelClass) {
        mModelClass = modelClass;
    }

    private DatabaseQuery(DatabaseQuery q) {
        mModelClass = q.mModelClass;
        mFilter = q.mFilter;
    }

    public Class<? extends IDatabaseModel> getModelClass() {
        return mModelClass;
    }

    public IDatabaseQueryFilter getFilter() {
        return mFilter;
    }

    public DatabaseQuery setFilter(IDatabaseQueryFilter filter) {
        DatabaseQuery query = new DatabaseQuery(this);
        query.mFilter = filter;

        return query;
    }
}
