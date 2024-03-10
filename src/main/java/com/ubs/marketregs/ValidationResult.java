package com.ubs.marketregs;

/**
 * Simple record class to hold the outcome of a validation request with a success/fail flag, a message explaining the outcome
 * and a result code
 * @param validationSucceeded
 * @param message
 * @param resultCode
 */
public record ValidationResult(boolean validationSucceeded, String message, ValidationResultCode resultCode) {
    static final String INVALID_CARD_NUMBER_MUST_BE_NUMERIC = "Invalid: card number must be numeric";

    static final String INVALID_CARD_NUMBER_FAILED = "Invalid: card number failed validation";

    static final String INVALID_CARD_NUMBER_EMPTY = "Invalid: card number cannot be null, empty or blank";

    static final String VALID = "Valid";

    public enum ValidationResultCode {VALID,INVALID_FORMAT, NUMBER_FAILED_VALIDATION, NUMBER_EMPTY}
}
