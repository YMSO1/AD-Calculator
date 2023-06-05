package com.github.ymso1.calculator;

public class CalculatorTest {

    /**
     * Calculator Test
     *
     * @param str a string with a mathematical expression.
     * @param expect the expected result of calculating the mathematical expression specified in "str".
     */
    public static void test(String str, String expect) {
        String result = Calculator.evaluate(str);

        System.out.println(result.equals(expect)
                ? "CORRECT!"
                : str + " should be evaluated to " + expect + ", but was " + result);
    }
}
