public class Eval {
    public static void main(String[] args) {
        CalculatorTest.test(" 3 + 4", 7);
        CalculatorTest.test(" 5 + 2 * 6", 17);
        CalculatorTest.test(" 10 * 7 + 5", 75);
        CalculatorTest.test(" 111 * ( 2 + 3 )", 555);
        CalculatorTest.test(" 112 * ( 2 + 3 )", 560);
        CalculatorTest.test(" 222 * ( 2 + 5 ) / 14", 111);
        CalculatorTest.test(" 222 * ( 12 + ( 1 - 3 ) * 2 ) / 8", 222);
        CalculatorTest.test("4+2*(5-2)", 10);
    }
}