package com.ubs.marketregs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ubs.marketregs.ValidationResult.*;
import static com.ubs.marketregs.ValidationResult.ValidationResultCode.*;

/**
 * Simple class to validate a card number using the Luhn algorithm - starting from the right-most digit (the check digit)
 * add each odd-placed digit to the total, even-placed digits should first be doubled before being added to the total
 * unless doubling results in a product > 10 in which case the two digits of the product are added together. The final sum
 * is then checked against modulo 10, if it is congruent to 0, the number is valid
 * e.g. if we have a card number 2378, then 8 is the check digit so this is just added to the overall total, 7 is the
 * next digit, but as this is the second digit from the right it is in an even position so must be doubled to 14. But
 * products greater than 10 must be further resolved by adding the two digits of the product together i.e. 1+4=5 so 5
 * is added to the overall total, not 14. The next number is 3 which is just added directly, then 2 which is doubled to 4
 * i.e. 8 + (2*7 = 14 which resolves to 1+4 =5) + 3 + (2*2=4) = 20; 20 % 10 ==0 so the card number is valid. If the card
 * number was 12378, then 1 would be added to the overall sum of the digits, making 21 which does not end in 0, so would
 * not be valid.
 */
@Slf4j
public class LuhnValidator {

    public static final ValidationResult VALID_RESULT = new ValidationResult(true, ValidationResult.VALID, ValidationResult.ValidationResultCode.VALID);

    /**
     * Valid separators are space,underscore, hyphen and point.
     */
    private final static String SEPARATORS_REGEX = "[ \\-_\\.]";

    /**
     * Validate a given card number using the Luhn Algorithm as described in the Javadoc for the class
     * 
     * @param cardNumber - the number to be checked to see if it is a valid card number
     * @return ValidationResult containing the outcome of the check
     */
    public static ValidationResult validate(String cardNumber) {
        log.debug("Validating {}", cardNumber);

        // make sure there's actually a card number
        if (cardNumber == null || cardNumber.isBlank()) {
            log.debug("{} failed validation - no card number specified", cardNumber);
            return new ValidationResult(false, INVALID_CARD_NUMBER_EMPTY, NUMBER_EMPTY);
        }

        // remove permitted separators
        cardNumber = cardNumber.trim().replaceAll(SEPARATORS_REGEX, "");

        // now check that what remains is just numeric digits, fail early if not
        if (!StringUtils.isNumeric(cardNumber)) {
            log.debug("{} failed validation - not a valid numeric format", cardNumber);
            return new ValidationResult(false, INVALID_CARD_NUMBER_MUST_BE_NUMERIC, INVALID_FORMAT);
        }

        // reverse the card number so we start count from the check character
        String reverseCardNumber = StringUtils.reverse(cardNumber);
        // get the sum of each of the digits, counting back from the check character with each second digit doubled and,
        // if the product of the doubling > 10 then the two digits of the product are also added together i.e. if the second
        // digit after the check digit is 8 then 8x2 =16 = (1+6) =7. so 7 is added to the overall total not 16
        int luhnResult = IntStream.range(0, reverseCardNumber.length())
                .map(i -> {
                    int digit = Character.digit(reverseCardNumber.charAt(i), 10);
                    if ((i+1)%2==0) {
                        digit = String.valueOf( digit*2).chars().boxed().collect( Collectors.summingInt( (c) -> c - '0' ) );
                    }
                    return digit;
                })
                .sum();
        // if the result ends in 0 then it's valid, otherwise we have an invalid card number
        if (luhnResult % 10==0) {
            return VALID_RESULT;
        } else {
            log.debug("{} failed validation - not a valid card number", cardNumber);
            return new ValidationResult(false, INVALID_CARD_NUMBER_FAILED, NUMBER_FAILED_VALIDATION);
        }
    }
}