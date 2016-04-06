package ca.douglascollege.flamingdodos.database.sqlite.util;

import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseQueryFilter;

import java.util.Map;

public class CompositeFilter implements IDatabaseQueryFilter {

    public enum Operator {
        AND, OR
    }

    private IDatabaseQueryFilter[] mFilters;
    private Operator mOperator;

    public CompositeFilter(Operator operator, IDatabaseQueryFilter... filters) {
        mFilters = filters;
        mOperator = operator;
    }

    @Override
    public boolean evaluate(Object key, Map<String, Object> item) {
        if (mOperator == Operator.AND) {
            for (IDatabaseQueryFilter filter : mFilters) {
                if (!filter.evaluate(key, item)) {
                    return false;
                }
            }
            return true;
        } else if (mOperator == Operator.OR) {
            for (IDatabaseQueryFilter filter : mFilters) {
                if (filter.evaluate(key, item)) {
                    return true;
                }
            }
            return false;
        }

        return false;
    }
}
