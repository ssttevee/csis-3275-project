package ca.douglascollege.flamingdodos.database.interfaces;

import ca.douglascollege.flamingdodos.database.exceptions.DatabaseException;
import ca.douglascollege.flamingdodos.database.sqlite.util.PropertyFilter;

public interface IDatabaseService<T extends IDatabaseModel> {
    /**
     * Inserts or updates the given model into the database
     * @param model The model to be saved
     * @return the id of the model
     * @throws DatabaseException
     */
    long save(T model) throws DatabaseException;

    /**
     * Looks up the given model by the id
     * @param id The id to look up
     * @return The found model or null if not found
     * @throws DatabaseException
     */
    T lookup(long id) throws DatabaseException;

    /**
     * Searches for the
     * @param filters
     * @return
     */
    T[] filter(PropertyFilter... filters);
}
