package ca.douglascollege.flamingdodos.realestate.data.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SqliteForeignKey {
    Class<?> table();
    String column();
}
