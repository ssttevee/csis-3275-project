package ca.douglascollege.flamingdodos.realestate.data.models;

import ca.douglascollege.flamingdodos.realestate.data.annotations.SqliteColumn;
import ca.douglascollege.flamingdodos.realestate.data.annotations.SqliteForeignKey;
import ca.douglascollege.flamingdodos.realestate.data.enums.SqliteDataTypes;
import ca.douglascollege.flamingdodos.realestate.utils.CaseTools;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleModel extends BaseModel {
    @SqliteColumn(value = SqliteDataTypes.Integer, primaryKey = true)
    protected long id;

    protected SimpleModel() {}

    @Override
    protected String getCreateTableStatement() {
        String createStmt = "CREATE TABLE " + mTableName + "(";

        for (Field f : getFields()) {
            String name = CaseTools.camelToSnake(f.getName());

            SqliteColumn column = f.getAnnotation(SqliteColumn.class);
            createStmt += name + " " + column.value().getName();

            if (column.primaryKey()) createStmt += " PRIMARY KEY";

            createStmt += ",";

            SqliteForeignKey foreignKey = f.getAnnotation(SqliteForeignKey.class);
            if (foreignKey != null) {
                createStmt += " FOREIGN KEY(" + column.value() + ") REFERENCES " + foreignKey.table().getSimpleName() + "(" + foreignKey.column() + "),";
            }
        }

        createStmt = createStmt.substring(0, createStmt.length() - 1) + ");";

        return createStmt;
    }

    @Override
    protected Map<String, Object> toDataMap() {
        Map<String, Object> dataMap = new HashMap<>();

        for (Field f : getFields()) {
            try {
                dataMap.put(CaseTools.camelToSnake(f.getName()), f.get(this));
            } catch (IllegalAccessException e) {
                // This should never happen
                // TODO handle this exception...
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
                // TODO handle this exception...
            }
        }

    }

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
