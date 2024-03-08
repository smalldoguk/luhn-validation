package com.ubs.marketregs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestLuhnValidator {

    public static final int ARABIC_ZERO = 1632;

    public static final int TIBETAN_ZERO = 3872;

    @Test
    public void testValid() {
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("2378"));
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("49927398716"));
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("2220859221412918"));
    }

    @Test
    public void testInvalid() {
        ValidationResult result = LuhnValidator.validate("59927398716");
        assertFalse(result.validationSucceeded());
        assertEquals(ValidationResult.ValidationResultCode.NUMBER_FAILED_VALIDATION, result.resultCode());
        assertEquals("Invalid: card number failed validation", result.message());
        result = LuhnValidator.validate("12378");
        assertFalse(result.validationSucceeded());
        assertEquals(ValidationResult.ValidationResultCode.NUMBER_FAILED_VALIDATION, result.resultCode());
        assertEquals("Invalid: card number failed validation", result.message());
        result = LuhnValidator.validate("12378");
        assertFalse(result.validationSucceeded());
        assertEquals(ValidationResult.ValidationResultCode.NUMBER_FAILED_VALIDATION, result.resultCode());
        assertEquals("Invalid: card number failed validation", result.message());
        result = LuhnValidator.validate("3220859221412918");
        assertFalse(result.validationSucceeded());
        assertEquals(ValidationResult.ValidationResultCode.NUMBER_FAILED_VALIDATION, result.resultCode());
        assertEquals("Invalid: card number failed validation", result.message());
    }

    @Test
    public void testValidUnicode()  {
        String arabicNumber = convertToLocaleDigits("49927398716", ARABIC_ZERO);
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate(arabicNumber));
        String tibetanNumber = convertToLocaleDigits("49927398716", TIBETAN_ZERO);
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate(tibetanNumber));
    }

    @Test
    public void testValidWithSpaces() {
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("4992 7398 716"));
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("2220 8592 2141 2918"));
    }

    @Test
    public void testValidWithHyphens() {
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("4992-7398-716"));
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("2220-8592-2141-2918"));
    }

    @Test
    public void testValidWithPoints() {
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("4992.7398.716"));
    }

    @Test
    public void testValidWithUnderscores() {
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("4992_7398_716"));
        assertEquals(LuhnValidator.VALID_RESULT, LuhnValidator.validate("2220_8592-2141_2918"));
    }

    @Test
    public void testInvalidASCIICharacter() {
        ValidationResult result = LuhnValidator.validate("A4B992C73D98E716F");
        assertFalse(result.validationSucceeded());
        assertEquals(ValidationResult.ValidationResultCode.INVALID_FORMAT, result.resultCode());
        assertEquals("Invalid: card number must be numeric", result.message());
    }

    @Test
    public void testInvalidNonASCIIUnicode() {
        ValidationResult result = LuhnValidator.validate("4éaà7398716");
        assertFalse(result.validationSucceeded());
        assertEquals(ValidationResult.ValidationResultCode.INVALID_FORMAT, result.resultCode());
        assertEquals("Invalid: card number must be numeric", result.message());
    }

    private static String convertToLocaleDigits(String number, int localeZero) {
        return number.chars().boxed()
                .map(c->c-'0' + localeZero)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();

    }

}
