package ca.douglascollege.flamingdodos.database.interfaces;

import java.util.Map;

public interface IDatabaseQueryFilter {
    boolean evaluate(Object key, Map<String, Object> item);
}
