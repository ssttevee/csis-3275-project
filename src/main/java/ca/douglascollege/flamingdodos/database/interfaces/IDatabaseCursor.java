package ca.douglascollege.flamingdodos.database.interfaces;

import ca.douglascollege.flamingdodos.database.exceptions.DatabaseException;

public interface IDatabaseCursor<T> {

    boolean hasNext() throws DatabaseException;

    T next() throws DatabaseException;

    void close() throws DatabaseException;
}
