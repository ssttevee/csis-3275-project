package ca.douglascollege.flamingdodos.realestate.data.annotations;

import ca.douglascollege.flamingdodos.realestate.data.enums.SqliteDataTypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SqliteColumn {
    SqliteDataTypes value();

    boolean         primaryKey() default false;
}
