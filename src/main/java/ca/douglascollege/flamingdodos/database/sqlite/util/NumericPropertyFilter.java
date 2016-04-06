package ca.douglascollege.flamingdodos.database.sqlite.util;


import java.math.BigDecimal;

public class NumericPropertyFilter extends BasePropertyFilter {
    public enum Operator {
        LESS_THAN_OR_EQUAL,
        LESS_THAN,
        EQUAL,
        NOT_EQUAL,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL
    }

    private Operator mOperator;
    private Number mOperand;

    public NumericPropertyFilter(String propertyName, Operator operator, Number operand) {
        super(propertyName);
        mOperator = operator;
        mOperand = operand;
    }

    @Override
    public boolean evaluate(Object operand) {
        if (!(operand instanceof Number)) {
            // this isn't a number
            throw new NumberFormatException();
        }

        int cmp = BigDecimal.valueOf(((Number) operand).doubleValue()).compareTo(BigDecimal.valueOf(mOperand.doubleValue()));
        switch (mOperator) {
            case LESS_THAN:
                return cmp == -1;
            case LESS_THAN_OR_EQUAL:
                return cmp == -1 || cmp == 0;
            case EQUAL:
                return cmp == 0;
            case NOT_EQUAL:
                return cmp != 0;
            case GREATER_THAN_OR_EQUAL:
                return cmp == 1 || cmp == 0;
            case GREATER_THAN:
                return cmp == 1;
        }

        return false;
    }
}
