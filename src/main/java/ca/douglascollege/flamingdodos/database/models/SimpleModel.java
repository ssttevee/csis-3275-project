package ca.douglascollege.flamingdodos.database.models;

import ca.douglascollege.flamingdodos.database.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.database.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.database.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.realestate.utils.CaseTools;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A Simple implementation of the {@link BaseModel}
 * </p>
 *
 * <p>
 * Use &#64;{@link SqliteColumn} and &#64;{@link SqliteForeignKey} annotations to identify columns in the table
 * </p>
 *
 * <p>
 * Usage:
 * <pre><code>
 * public class PersonModel extends SimpleModel {
 *     &#64;SqliteColumn(value = SqliteDataTypes.Integer, primaryKey = true)
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
 *     Note: The <code>id</code> field is implemented by {@link SimpleModel}, do not implement it yourself!
 * </p>
 */
public abstract class SimpleModel extends BaseModel {
    @SqliteColumn(value = SqliteDataTypes.Integer, primaryKey = true)
    protected long id;

    protected SimpleModel() {}

    @Override
    protected String getTableName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected String getCreateTableStatement() {
        String createStmt = "CREATE TABLE " + mTableName + "(";

        for (Field f : getFields()) {
            String name = CaseTools.camelToSnake(f.getName());

            SqliteColumn column = f.getAnnotation(SqliteColumn.class);
            createStmt += name + " `" + column.value().getName() + "`";

            if (column.primaryKey()) createStmt += " PRIMARY KEY";

            createStmt += ",";

            SqliteForeignKey foreignKey = f.getAnnotation(SqliteForeignKey.class);
            if (foreignKey != null) {

                try {
                    createStmt += " FOREIGN KEY(" + column.value() + ") REFERENCES " +
                            foreignKey.table().newInstance().getTableName() + "(" + foreignKey.column() + "),";
                } catch (InstantiationException e) {
                    if (Modifier.isAbstract(foreignKey.table().getModifiers())) {
                        throw new AssertionError("Foreign Key model class must not be abstract", e);
                    }

                    throw new AssertionError(foreignKey.table().getName() + " does not have a nullary constructor", e);
                } catch (IllegalAccessException e) {
                    throw new AssertionError(foreignKey.table().getName() + " nullary constructor is inaccessible", e);
                }
            }
        }

        return createStmt.substring(0, createStmt.length() - 1) + ");";
    }

    @Override
    protected Map<String, Object> toDataMap() {
        Map<String, Object> dataMap = new HashMap<>();

        for (Field f : getFields()) {
            try {
                dataMap.put(CaseTools.camelToSnake(f.getName()), f.get(this));
            } catch (IllegalAccessException e) {
                // This should never happen
                throw new AssertionError("Got an inaccessible field", e);
            }
        }

        return dataMap;
    }

    @Override
    protected void fromCursor(ISqlJetCursor readCursor) throws SqlJetException {
        for (Field f : getFields()) {
            try {
                f.set(this, readCursor.getValue(CaseTools.camelToSnake(f.getName())));
            } catch (IllegalAccessException e) {
                // This should never happen
                throw new AssertionError("Got an inaccessible field", e);
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
            if (f.getAnnotation(SqliteColumn.class) != null && !Modifier.isPrivate(f.getModifiers())) {
                fields.add(f);
            }
        }

        return fields.toArray(new Field[fields.size()]);
    }
}
