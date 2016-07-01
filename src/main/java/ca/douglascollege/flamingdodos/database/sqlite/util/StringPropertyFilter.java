package ca.douglascollege.flamingdodos.database.sqlite.util;


public class StringPropertyFilter extends BasePropertyFilter {
    public enum Operator {
        STARTS_WITH,
        CONTAINS,
        EQUAL,
        ENDS_WITH
    }

    private Operator mOperator;
    private String mOperand;

    public StringPropertyFilter(String propertyName, Operator operator, String operand) {
        super(propertyName);
        mOperator = operator;
        mOperand = operand;
    }

    @Override
    public boolean evaluate(Object operand) {
        if (!(operand instanceof String)) {
            // this isn't a string
            throw new RuntimeException("This isn't a string");
        }

        switch (mOperator) {
            case STARTS_WITH:
                return ((String) operand).startsWith(mOperand);
            case CONTAINS:
                return ((String) operand).toLowerCase().contains(mOperand.toLowerCase());
            case EQUAL:
                return operand.equals(mOperand);
            case ENDS_WITH:
                return ((String) operand).endsWith(mOperand);
        }

        return false;
    }
}
