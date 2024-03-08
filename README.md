## Credit card number validator using Luhn algorithm

An implementation of credit card validation using the Luhn algorithm.

- Build: mvn clean compile
- Run tests: mvn clean test

The Luhn algorithm checks a credit card number by starting from the right-most digit (the check digit) add each odd-placed 
digit to the total, even-placed digits should first be doubled before being added to the total unless doubling results in 
a product > 10 in which case the two digits of the product are added together. The final sum is then checked against modulo 10, 
if it is congruent to 0, the number is valid. 

e.g. if we have a card number 2378, then 8 is the check digit so this is just added to the overall total, 7 is the next digit, 
but as this is the second digit from the right it is in an even position so must be doubled to 14. But products greater than 
10 must be further resolved by adding the two digits of the product together i.e. 1+4=5 so 5 is added to the overall total, 
not 14. The next number is 3 which is just added directly, then 2 which is doubled to 4
i.e. the overal sum is then 8 + (2*7 = 14 which resolves to 1+4 =5) + 3 + (2*2=4) = 20; 20 % 10 ==0 so the card number is valid. 
If the card number was 12378, then 1 would be added to the overall sum of the digits, making 21 which does not end in 0, so would 
not be valid.

The validator class allows (and parses out) spaces, hypens, underscores and points (full stops) as separators, and also supports
non-ASCII digits but will reject strings which contain other non-numeric characters, even if the digits within the string
would be a valid credit card number. So it allows some degree of latitude to account for human behaviour on text entry, but 
attempts to disallow validation of random strings which are accidentally valid. 
