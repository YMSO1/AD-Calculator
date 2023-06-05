package com.github.ymso1.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Counts the expression passed as a string.
 * <p>
 * Any numbers, brackets ({@code (}, {@code )}) and mathematical operators ({@code +}, {@code -}, {@code *}, {@code /}) can be used in the expression.
 */
public class Calculator {

    /**
     * Passing through the expression, it gradually allocates its parts, distributing numbers and operators along the walls.
     * <p>
     * The operators include: "(", ")", "+", "-", "*", "/").
     * <ul>
     * <li>Current token is a whitespace, skip it.</li>
     * <li>Current token is a number, push it to stack for numbers.</li>
     * <li>Current token is an opening brace, push it to 'ops'.</li>
     * <li>Closing brace encountered, solve entire brace.</li>
     * <li>Current token is an operator, depending on the priority, we push it into the stack or calculate.</li>
     * <li>When the expression has ended, apply remaining ops to remaining values.</li>
     * </ul>
     *
     * @param expression - mathematical expression.
     * @return the result of a mathematical expression.
     * @throws IllegalArgumentException if incorrect entry of a mathematical expression.
     */
    public static BigDecimal evaluate(String expression) {
        Deque<BigDecimal> values = new ArrayDeque<>();
        Deque<Character> ops = new ArrayDeque<>();

        for (int i = 0; i < expression.length(); i++) {
            char token = expression.charAt(i);

            if (token == ' ') {
                continue;
            }

            if (isDigit(token)) {
                i += readValue(expression, values, i);
            } else if (token == '(') {
                ops.push(token);
            } else if (token == ')') {
                applyOps(ops, values, op -> op != '(');
                ops.pop();
            } else if (isOperator(token)) {
                applyOps(ops, values, Calculator::hasPrecedence);
                ops.push(token);
            } else {
                throw new IllegalArgumentException("Incorrect entry of a mathematical expression: " + token);
            }

        }
        applyOps(ops, values, __ -> true);

        return values.pop().stripTrailingZeros();
    }

    private static boolean isDigit(char token) {
        return token == '.' || Character.isDigit(token);
    }

    /**
     * Determines all encountered numbers, pushes it into the stack of numbers and moves
     * the index to the last element of the number.
     *
     * @param expression mathematical expression.
     * @param values     stack of numbers.
     * @param start      index of first digit.
     * @return count of processed symbols.
     */
    private static int readValue(String expression, Deque<BigDecimal> values, int start) {
        // There may be more than one digit in a number
        int end = start;
        do {
            end++;
        } while (end < expression.length() && isDigit(expression.charAt(end)));

        values.push(new BigDecimal(expression.substring(start, end)));
        return end - start - 1;
    }

    /**
     * Determines whether a symbol is a mathematical operator
     *
     * @param ch the character being checked.
     * @return true if this char is operator, false otherwise.
     */
    private static boolean isOperator(char ch) {
        return ch == '+'
            || ch == '-'
            || ch == '*'
            || ch == '/';
    }

    /**
     * Defines the priority operator
     *
     * @param op2 previous operator
     * @return true if 'op2' has higher or same precedence as 'op1', otherwise returns false.
     */
    private static boolean hasPrecedence(char op2) {
        return op2 == '*' || op2 == '/';
    }

    private static void applyOps(Deque<Character> ops, Deque<BigDecimal> values, Predicate<Character> opTest) {
        while (!ops.isEmpty() && opTest.test(ops.peek())) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }
    }

    /**
     * A utility method to apply an operator 'op' on operands 'a' and 'b'. Return the result.
     *
     * @param op the last operator on the stack.
     * @param b  the last number on the stack.
     * @param a  the penultimate number on the stack.
     * @return the result of applying the operator to numbers.
     * @throws UnsupportedOperationException if divide by zero.
     */
    private static BigDecimal applyOp(char op, BigDecimal b, BigDecimal a) {
        return switch (op) {
            case '-' -> a.subtract(b);
            case '+' -> a.add(b);
            case '*' -> a.multiply(b);
            case '/' -> {
                if (Objects.equals(b, BigDecimal.ZERO)) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                yield a.divide(b, 4, RoundingMode.HALF_EVEN);
            }
            default -> throw new UnsupportedOperationException("Invalid operator: " + op);
        };
    }

}
