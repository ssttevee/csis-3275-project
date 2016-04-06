package ca.douglascollege.flamingdodos.database.sqlite.interfaces;

import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseQueryFilter;

public interface IPropertyFilter extends IDatabaseQueryFilter {
    String getPropertyName();
}
