package com.github.ymso1.calculator;

public class Eval {

    public static void main(String[] args) {
        CalculatorTest.test(" 3 + 4", "7");
        CalculatorTest.test(" 5 + 2 * 6", "17");
        CalculatorTest.test(" 10 * 7 + 5", "75");
        CalculatorTest.test(" 111 * ( 2 + 3 )", "555");
        CalculatorTest.test(" 112 * ( 2 + 3 )", "560");
        CalculatorTest.test(" 222 * ( 2 + 5 ) / 14", "111");
        CalculatorTest.test(" 222 * ( 12 + ( 1 - 3 ) * 2 ) / 8", "222");
        CalculatorTest.test("4+2*(5-2)", "10");
        CalculatorTest.test("""
                        12345678901234567890
                        12345678901234567890
                        12345678901234567890
                        12345678901234567890
                        *
                        12345678901234567890
                        12345678901234567890
                        12345678901234567890
                        12345678901234567890
                        """
                      , """
                        15241578753238836750
                        49535156256668194500
                        83828733760097552251
                        18122311263526910001
                        21932731260478594250
                        87639153757049236500
                        53345576253619878750
                        1905199875019052100
                        """);
    }

}
