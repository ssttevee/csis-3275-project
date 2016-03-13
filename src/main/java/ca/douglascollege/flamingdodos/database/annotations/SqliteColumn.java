package ca.douglascollege.flamingdodos.database.annotations;

import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.database.models.SimpleModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     The <code>SqliteColumn</code> is used in {@link SimpleModel} subclasses to
 *     tell the {@link SimpleModel} that the field should be handled by the database
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqliteColumn {
    /**
     * The data type of the column
     */
    SqliteDataTypes value();

    /**
     * Whether or not the column is a primary key
     */
    boolean         primaryKey() default false;
}
