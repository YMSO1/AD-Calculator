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

    public static double evaluate(String expression) {
        // Stack for numbers: 'values'
        Deque<Double> values = new ArrayDeque<>();
        // Stack for Operators: 'ops'
        Deque<Character> ops = new ArrayDeque<>();

        for (int i = 0; i < expression.length(); i++) {
            // Current token is a whitespace, skip it
            if (expression.charAt(i) == ' ') {
                continue;
            }

            // Current token is a number, push it to stack for numbers
            if (Character.isDigit(expression.charAt(i))) {
                // There may be more than one digit in a number
                int j = i + 1;
                while (j < expression.length() && Character.isDigit(expression.charAt(j))){
                    j++;
                }
                values.push(Double.parseDouble(expression.substring(i, j)));
                i = j - 1;
            }

            // Current token is an opening brace, push it to 'ops'
            else if (expression.charAt(i) == '(') {
                ops.push(expression.charAt(i));
            }

            // Closing brace encountered, solve entire brace
            else if (!ops.isEmpty() && expression.charAt(i) == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            }

            // Current token is an operator.
            else if (expression.charAt(i) == '+'
                    || expression.charAt(i) == '-'
                    || expression.charAt(i) == '*'
                    || expression.charAt(i) == '/') {
                while (!ops.isEmpty() && hasPrecedence(expression.charAt(i), ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(expression.charAt(i));
            }
        }

        // Entire expression has been parsed at this point, apply remaining ops to remaining values
        while (!ops.isEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }
        return values.pop();
    }

    // Returns true if 'op2' has higher or same precedence as 'op1', otherwise returns false.
    public static boolean hasPrecedence(char op1, char op2) {
        return op2 != '(' && op2 != ')' && op1 != '*' && op1 != '/';
    }

    // A utility method to apply an operator 'op' on operands 'a' and 'b'. Return the result.
    public static double applyOp(char op, double b, double a) {
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

    private static void test(String str, double expect) {
        double result = evaluate(str);
        if (result == expect) {
            System.out.println("CORRECT!");
        } else {
            System.out.println(str + " should be evaluated to " + expect + ", but was " + result);
        }
    }
}