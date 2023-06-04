import java.util.ArrayDeque;
import java.util.Deque;

public class Eval {
    public static void main(String[] args) {
        test(" 3 + 4", 7);
        test(" 5 + 2 * 6", 17);
        test(" 10 * 7 + 5", 75);
        test(" 111 * ( 2 + 3 )", 555);
        test(" 112 * ( 2 + 3 )", 560);
        test(" 222 * ( 2 + 5 ) / 14", 111);
        test(" 222 * ( 12 + ( 1 - 3 ) * 2 ) / 8", 222);
        test("4+2*(5-2)", 10);
    }

    /**
     * Passing through the expression, it gradually allocates its parts, distributing numbers and operators along the walls.
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
    public static double evaluate(String expression) {
        Deque<Double> values = new ArrayDeque<>();
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

        return values.pop();
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
    private static int pushAndSkip(String expression, Deque<Double> values, int i) {
        // There may be more than one digit in a number
        int j = i + 1;
        while (j < expression.length() && Character.isDigit(expression.charAt(j)))
            j++;

        values.push(Double.parseDouble(expression.substring(i, j)));
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
    private static double applyOp(char op, double b, double a) {
        switch (op) {
            case '-':
                return a - b;
            case '+':
                return a + b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    /**
     * Calculator Test
     *
     * @param str a string with a mathematical expression.
     * @param expect the expected result of calculating the mathematical expression specified in "str".
     */
    public static void test(String str, double expect) {
        double result = evaluate(str);
        System.out.println(result == expect
                ? "CORRECT!"
                : str + " should be evaluated to " + expect + ", but was " + result);
    }
}