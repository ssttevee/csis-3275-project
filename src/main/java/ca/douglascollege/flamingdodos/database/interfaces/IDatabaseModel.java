package ca.douglascollege.flamingdodos.database.interfaces;

import java.util.Map;

public interface IDatabaseModel {
    void load(Map<String, Object> data);

    /**
     * Wraps all the data in the model into a {@link Map}
     * @return {@link Map} of all the data in the model
     */
    Map<String, Object> export();
}
