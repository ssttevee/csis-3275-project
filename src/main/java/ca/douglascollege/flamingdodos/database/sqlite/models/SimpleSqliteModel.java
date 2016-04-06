package ca.douglascollege.flamingdodos.database.sqlite.models;

import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.sqlite.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.realestate.utils.CaseTools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A Simple implementation of the {@link BaseSqliteModel}
 * </p>
 *
 * <p>
 * Use &#64;{@link SqliteColumn} and &#64;{@link SqliteForeignKey} annotations to identify columns in the table
 * </p>
 *
 * <p>
 * Usage:
 * <pre><code>
 * public class PersonModel extends SimpleSqliteModel {
 *     &#64;SqliteColumn(type = SqliteDataTypes.Integer, primaryKey = true)
 *     public long id;
 *
 *     &#64;SqliteColumn(SqliteDataTypes.Text)
 *     public String firstName;
 *
 *     &#64;SqliteColumn(SqliteDataTypes.Integer)
 *     &#64;SqliteForeignKey(table = AnotherModel.class, column = "id")
 *     public long anotherId;
 * }
 * </code></pre>
 * will be make a table with
 * <pre><code>
 * CREATE TABLE PersonModel (
 *     id INTEGER PRIMARY KEY,
 *     first_name TEXT,
 *     another_id INTEGER,
 *     FOREIGN KEY(another_id) REFERENCES AnotherModel(id)
 * );
 * </code></pre>
 * </p>
 * <p>
 *     Note: The <code>id</code> field is implemented by {@link SimpleSqliteModel}, do not implement it yourself!
 * </p>
 */
public abstract class SimpleSqliteModel extends BaseSqliteModel {
    protected SimpleSqliteModel() {}

    @Override
    public Map<String, Object> export() {
        Map<String, Object> dataMap = new HashMap<>();

        for (Field f : getFields()) {
            try {
                Object value = f.get(this);
                 if (value != null) {
                    if (f.getType().isEnum()) {
                        value = value.toString();
                    } else if (f.getType() == Date.class) {
                        value = ((Date) value).getTime();
                    }
                }

                dataMap.put(CaseTools.camelToSnake(f.getName()), value);
            } catch (IllegalAccessException e) {
                // This should never happen
                throw new AssertionError("Got an inaccessible field", e);
            }
        }

        return dataMap;
    }

    @Override
    public void load(Map<String, Object> data) {
        for (Field f : getFields()) {
            try {
                String colName = f.getAnnotation(SqliteColumn.class).name();
                if (colName.length() <= 0) {
                    colName = CaseTools.camelToSnake(f.getName());
                }

                if (!data.containsKey(colName))
                    continue;

                Object colValue = data.get(colName);

                if (colValue == null)
                    continue;

                if (f.getType().isEnum()) {
                    colValue = (f.getType()).getMethod("valueOf", String.class).invoke(null, (String) colValue);
                } else if (f.getType() == Date.class) {
                    colValue = new Date((Long) colValue);
                }

                f.set(this, colValue);
            } catch (IllegalAccessException e) {
                // This should never happen
                throw new AssertionError("Got an inaccessible field", e);
            } catch (NoSuchMethodException e) {
                // This should never happen
                throw new AssertionError("Got an enum without 'valueOf'", e);
            } catch (InvocationTargetException e) {
                // This should never happen
                throw new AssertionError("Got an exception from 'valueOf'", e);
            }
        }
    }

    /**
     * Get a the list of non-private fields annotated with <code>@{@link SqliteColumn}</code>
     * @return the fields annotated with <code>@{@link SqliteColumn}</code>
     */
    private Field[] getFields() {
        List<Field> fields = new ArrayList<>();

        for (Field f : this.getClass().getFields()) {
            // Only non-private fields annotated with {@link SqliteColumn} are recognized as columns in the database
            if (f.getAnnotation(SqliteColumn.class) != null && Modifier.isPublic(f.getModifiers())) {
                fields.add(f);
            }
        }

        return fields.toArray(new Field[fields.size()]);
    }
}
