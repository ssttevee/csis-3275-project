package ca.douglascollege.flamingdodos.database.interfaces;

import ca.douglascollege.flamingdodos.database.exceptions.DatabaseException;
import com.sun.istack.internal.Nullable;

public interface IDatabase {
    /**
     * Gets data from the database with the given key and loads it into the given model
     *
     * @param modelClass     The model class
     * @param key            The key of the model to retrieve (i.e Row Id)
     * @throws DatabaseException
     */
    <T extends IDatabaseModel> T lookup(Class<T> modelClass, Object key) throws DatabaseException;

    /**
     * Inserts data into the database
     *
     * @param key        The key of the model to insert, null if new (i.e Row Id)
     * @param source     The model which contains the data to insert
     * @return           The key of the inserted model (i.e Row Id)
     * @throws DatabaseException
     */
    <T extends IDatabaseModel> Object insert(@Nullable Object key, T source) throws DatabaseException;

    /**
     * Gets all the items within the given collection
     * @param modelClass The model class
     * @return All the items in the given collection within a cursor
     * @throws DatabaseException
     */
    <T extends IDatabaseModel> IDatabaseCursor<T> getAll(Class<T> modelClass) throws DatabaseException;

    /**
     * Executes a query and returns the results in a database cursor
     * @param query The name of the collection to query (i.e. Table Name)
     * @return The query results within a cursor
     * @throws DatabaseException
     */
    <T extends IDatabaseModel> IDatabaseCursor<T> execute(Class<T> modelClass, DatabaseQuery query) throws DatabaseException;

    boolean delete(Class<? extends IDatabaseModel> modelClass, Object key) throws DatabaseException;
}
