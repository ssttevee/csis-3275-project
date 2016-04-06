package ca.douglascollege.flamingdodos.database.interfaces;

public class DatabaseQuery {
    private IDatabaseQueryFilter mFilter;
    private int mLimit = -1;

    public DatabaseQuery() {

    }

    public DatabaseQuery(DatabaseQuery q) {
        mFilter = q.mFilter;
        mLimit = q.mLimit;
    }

    public IDatabaseQueryFilter getFilter() {
        return mFilter;
    }

    public DatabaseQuery setFilter(IDatabaseQueryFilter filter) {
        DatabaseQuery query = new DatabaseQuery(this);
        query.mFilter = filter;

        return query;
    }

    public int getLimit() {
        return mLimit;
    }

    public DatabaseQuery setLimit(int limit) {
        DatabaseQuery query = new DatabaseQuery(this);
        query.mLimit = limit;

        return query;
    }

    public void decrementLimit() {
        mLimit--;
    }
}
