package com.ubs.marketregs;

/**
 * Simple record class to hold the outcome of a validation request with a success/fail flag, a message explaining the outcome
 * and a result code
 * @param validationSucceeded
 * @param message
 * @param resultCode
 */
public record ValidationResult(boolean validationSucceeded, String message, ValidationResultCode resultCode) {
    public static final String INVALID_CARD_NUMBER_MUST_BE_NUMERIC = "Invalid: card number must be numeric";

    public static final String INVALID_CARD_NUMBER_FAILED = "Invalid: card number failed validation";
    public static final String VALID = "Valid";

    public enum ValidationResultCode {VALID,INVALID_FORMAT, NUMBER_FAILED_VALIDATION}
}
