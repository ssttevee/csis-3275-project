package ca.douglascollege.flamingdodos.database.annotations;

import ca.douglascollege.flamingdodos.database.models.BaseModel;
import ca.douglascollege.flamingdodos.database.models.SimpleModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     The <code>SqliteForeignKey</code> tells the {@link SimpleModel}
 *     that the column that field represents is also a foreign key
 * </p>
 * <p>
 *     Note: This must be used with {@link SqliteColumn} or it will be ignored
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqliteForeignKey {
    /**
     * A subclass of {@link BaseModel}
     */
    Class<? extends BaseModel> table();

    /**
     * The name of the column in the table
     */
    String column();
}
