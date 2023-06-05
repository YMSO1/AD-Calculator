package com.github.ymso1.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * Counts the expression passed as a string. Any numbers, brackets "(", ")"
 * and mathematical operators can be used in the expression "+", "-", "*", "/".
 */

public class Calculator {

    /**
     * Passing through the expression, it gradually allocates its parts, distributing numbers
     * and operators along the walls.
     * The operators include: "(", ")", "+", "-", "*", "/").
     *<p>
     *  - Current token is a whitespace, skip it.
     *<p>
     *  - Current token is a number, push it to stack for numbers.
     *<p>
     *  - Current token is an opening brace, push it to 'ops'.
     *<p>
     *  - Closing brace encountered, solve entire brace.
     *<p>
     *  - Current token is an operator, depending on the priority, we push it into the stack or calculate.
     *<p>
     *  - When the expression has ended, apply remaining ops to remaining values
     *<p>
     * @param expression - mathematical expression.
     * @return the result of a mathematical expression.
     * @throws IllegalArgumentException if incorrect entry of a mathematical expression.
     */
    public static String evaluate(String expression) {
        Deque<BigDecimal> values = new ArrayDeque<>();
        Deque<Character> ops = new ArrayDeque<>();
        char ch;

        for (int i = 0; i < expression.length(); i++) {
            ch = expression.charAt(i);

            if (ch == ' ')
                continue;

            if (Character.isDigit(ch))
                i = pushAndSkip(expression, values, i);

            else if (ch == '(')
                ops.push(ch);

            else if (!ops.isEmpty() && ch == ')') {
                while (!ops.isEmpty() && ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));

                ops.pop();
            }
            else if (isOperator(ch)) {
                while (!ops.isEmpty() && hasPrecedence(ch, ops.peek()))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));

                ops.push(ch);
            }
            else
                throw new IllegalArgumentException("Incorrect entry of a mathematical expression");

        }
        while (!ops.isEmpty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));

        return values.pop().stripTrailingZeros().toPlainString();
    }

    /**
     * Determines all encountered numbers, pushes it into the stack of numbers and moves
     * the index to the last element of the number.
     *<p>
     * @param expression mathematical expression.
     * @param values stack of numbers.
     * @param i index of the current iteration.
     * @return index of the last element of the number in the expression.
     */
    private static int pushAndSkip(String expression, Deque<BigDecimal> values, int i) {
        // There may be more than one digit in a number
        int j = i + 1;
        while (j < expression.length() && Character.isDigit(expression.charAt(j)))
            j++;

        values.push(new BigDecimal(expression.substring(i, j)));
        i = j - 1;
        return i;
    }

    /**
     * Determines whether a symbol is a mathematical operator
     * <p>
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
     *<p>
     * @param op1 current operator.
     * @param op2 previous operator
     * @return true if 'op2' has higher or same precedence as 'op1', otherwise returns false.
     */
    private static boolean hasPrecedence(char op1, char op2) {
        return op2 != '('
                && op2 != ')'
                && op1 != '*'
                && op1 != '/';
    }

    /**
     * A utility method to apply an operator 'op' on operands 'a' and 'b'. Return the result.
     *
     * @param op the last operator on the stack.
     * @param b the last number on the stack.
     * @param a the penultimate number on the stack.
     * @return the result of applying the operator to numbers.
     * @throws UnsupportedOperationException if divide by zero.
     */
    private static BigDecimal applyOp(char op, BigDecimal b, BigDecimal a) {
        switch (op) {
            case '-':
                return a.subtract(b);
            case '+':
                return a.add(b);
            case '*':
                return a.multiply(b);
            case '/':
                if (Objects.equals(b, BigDecimal.ZERO))
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a.divide(b, 4, RoundingMode.HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }
}
