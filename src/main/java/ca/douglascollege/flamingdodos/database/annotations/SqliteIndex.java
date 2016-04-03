package ca.douglascollege.flamingdodos.database.annotations;

import ca.douglascollege.flamingdodos.database.models.SimpleModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     The <code>SqliteIndex</code> is used in {@link SimpleModel} subclasses to
 *     tell the {@link SimpleModel} that the field should be indexes
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqliteIndex {
    /**
     * The name of the index
     */
    String value();
}
