package ca.douglascollege.flamingdodos.database.sqlite.util;

import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseQueryFilter;

import java.util.Map;

/**
 * Created by Steve on 2016-04-05.
 */
public abstract class BasePropertyFilter implements IDatabaseQueryFilter {
    private String mPropName;

    public BasePropertyFilter(String propertyName) {
        mPropName = propertyName;
    }

    @Override
    public boolean evaluate(Object key, Map<String, Object> item) {
        if (mPropName == null) {
            return evaluate(key);
        } else {
            return item.containsKey(mPropName) && evaluate(item.get(mPropName));
        }
    }

    protected abstract boolean evaluate(Object operand);
}
