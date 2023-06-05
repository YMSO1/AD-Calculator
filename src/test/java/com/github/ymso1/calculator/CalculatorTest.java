package com.github.ymso1.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

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
        //'\u0660' through '\u0669', Arabic-Indic digits
        "\u0661 + \u0662,3",
        //'\u06F0' through '\u06F9', Extended Arabic-Indic digits
        "\u06F1 + \u06F2,3",
        //'\u0966' through '\u096F', Devanagari digits
        "\u0967 + \u0968,3",
        //'\uFF10' through '\uFF19', Fullwidth digits
        "\uFF11 + \uFF12,3",
    })
    void test1(String expression, BigDecimal expected) {
        assertThat(Calculator.evaluate(expression)).isCloseTo(expected, within(BigDecimal.ZERO));
    }

    @DisplayName("Should fail on evaluate expression")
    @ParameterizedTest(name = "[{index}] Expression `{0}` should fail: {1}({2})")
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, value = {
        " 3 + ,java.util.NoSuchElementException,",
        " 3 + a,java.lang.IllegalArgumentException,Incorrect entry of a mathematical expression: a",
        " 3 ) 4,java.util.NoSuchElementException,",
        " 2 + 3 ) 4,java.util.NoSuchElementException,",
        "3++,java.util.NoSuchElementException,",
        "3 / 0,java.lang.UnsupportedOperationException,Cannot divide by zero",
        "1 2 3 + 4 ),java.util.NoSuchElementException,",
        "1(2+3,java.lang.UnsupportedOperationException,Invalid operator: (",
    })
    void test2(String expression, Class<?> exceptionType, String message) {
        var throwable = assertThatThrownBy(() -> Calculator.evaluate(expression))
            .isInstanceOf(exceptionType);
        if (nonNull(message)) {
            throwable.hasMessageStartingWith(message);
        }
    }

}
