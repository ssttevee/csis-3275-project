package ca.douglascollege.flamingdodos.database.sqlite.annotations;

import ca.douglascollege.flamingdodos.database.sqlite.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.database.sqlite.models.SimpleSqliteModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     The <code>SqliteColumn</code> is used in {@link SimpleSqliteModel} subclasses to
 *     tell the {@link SimpleSqliteModel} that the field should be handled by the database
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqliteColumn {
    /**
     * The data type of the column
     */
    SqliteDataTypes type();

    /**
     * The name of the column
     */
    String          name() default "";

    /**
     * Whether or not the column is a primary key
     */
    boolean         primaryKey() default false;
}
