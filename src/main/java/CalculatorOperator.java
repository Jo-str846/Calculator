package com.example.calculator;

interface Computable{
long execute();
}

public abstract class CalculatorOperator implements Computable{
    protected long left;
    protected long right;

    public CalculatorOperator(long left, long right) {
        this.left = left;
        this.right = right;
    }

    public abstract long execute();

    public static class Addition extends CalculatorOperator {
        public Addition(long left, long right){super(left, right);}

        @Override
        public long execute() {return left + right;}
    }

    public static class Subtraction extends CalculatorOperator {
        public Subtraction(long left, long right) {super(left, right);}

        @Override
        public long execute() {return left - right;}
    }

    public static class Multiplication extends CalculatorOperator {
        public Multiplication(long left, long right) {super(left, right);}

        @Override
        public long execute() {return left * right;}
    }

    public static class Division extends CalculatorOperator {
        public Division(long left, long right) {super(left, right);}

        @Override
        public long execute() {
            if (right == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return left / right;
        }
        }
}
