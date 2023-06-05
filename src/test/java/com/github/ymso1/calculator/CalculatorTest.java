package com.github.ymso1.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class CalculatorTest {

    @DisplayName("Should evaluate expression")
    @ParameterizedTest(name = "[{index}] Expression `{0}` should evaluate to `{1}`")
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, value = {
        " 3 + 4,7",
        " 5 + 2 * 6,17",
        " 10 * 7 + 5,75",
        " 111 * ( 2 + 3 ),555",
        " 112 * ( 2 + 3 ),560",
        " 222 * ( 2 + 5 ) / 14,111",
        " 222 * ( 12 + ( 1 - 3 ) * 2 ) / 8,222",
        "4+2*(5-2),10",
        "12345678901234567890123456789012345678901234567890123456789012345678901234567890 * 12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
            ",152415787532388367504953515625666819450083828733760097552251181223112635269100012193273126047859425087639153757049236500533455762536198787501905199875019052100",
    })
    void test1(String expression, String expected) {
        assertThat(Calculator.evaluate(expression)).isEqualTo(expected);
    }

}
