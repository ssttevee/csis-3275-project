package ca.douglascollege.flamingdodos.database.sqlite.annotations;

import ca.douglascollege.flamingdodos.database.sqlite.models.SimpleSqliteModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     The <code>SqliteTable</code> is used in {@link SimpleSqliteModel} subclasses to
 *     tell the {@link SimpleSqliteModel} that the field should be indexes
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqliteTable {
    /**
     * The name of the index
     */
    String name();
}
