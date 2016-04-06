package ca.douglascollege.flamingdodos.database.sqlite.util;


public class PropertyFilter extends BasePropertyFilter {
    public enum Operator {
        EQUAL,
        NOT_EQUAL
    }

    private Operator mOperator;
    private Object mOperand;

    public PropertyFilter(String propertyName, Operator operator, Object operand) {
        super(propertyName);
        mOperator = operator;
        mOperand = operand;
    }

    @Override
    public boolean evaluate(Object operand) {
        boolean result = operand.equals(mOperand);
        switch (mOperator) {
            case EQUAL:
                return result;
            case NOT_EQUAL:
                return !result;
        }

        return false;
    }
}
