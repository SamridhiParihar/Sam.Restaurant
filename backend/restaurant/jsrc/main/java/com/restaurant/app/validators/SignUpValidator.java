//
//package com.restaurant.app.validators;
//
//import com.restaurant.app.models.auth.SignUpRequest;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SignUpValidator {
//
//    public static List<String> validate(SignUpRequest request) {
//        List<String> errors = new ArrayList<>();
//
//        if (request.getFirstName() == null || !isValidName(request.getFirstName())) {
//            errors.add("First name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.");
//        }
//        if (request.getLastName() == null || !isValidName(request.getLastName())) {
//            errors.add("Last name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.");
//        }
//        if (request.getEmail() == null || !isValidEmail(request.getEmail())) {
//            errors.add("Invalid email format. Please ensure it follows the format username@domain.com.");
//        }
//        if (request.getPassword() == null || !isValidPassword(request.getPassword())) {
//            errors.add("Password must be 8-16 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.");
//        }
//        return errors;
//    }
//
//    public static boolean isValidName(String name) {
//        return name.matches("^[A-Za-z'\\\\-]{1,50}$");
//    }
//
//    public static boolean isValidEmail(String email) {
//        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
//        //^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$
//
//    }
//
//    public static boolean isValidPassword(String password) {
//        return password.length() >= 8 && password.length() <= 16 &&
//                password.chars().anyMatch(Character::isUpperCase) &&
//                password.chars().anyMatch(Character::isLowerCase) &&
//                password.chars().anyMatch(Character::isDigit) &&
//                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
//    }
//
//
//}

package com.restaurant.app.validators;

import com.restaurant.app.models.auth.SignUpRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for validating user sign-up details.
 * <p>
 * Validates fields such as first name, last name, email, and password
 * in the {@link SignUpRequest} object against business rules and formatting requirements.
 */
public class SignUpValidator {

    /**
     * Validates a {@link SignUpRequest} object for required fields and proper formatting.
     *
     * @param request The sign-up request object containing user input.
     * @return A list of validation error messages. The list is empty if all fields are valid.
     */
    public static List<String> validate(SignUpRequest request) {
        List<String> errors = new ArrayList<>();

        // Validate first name
        if (request.getFirstName() == null || !isValidName(request.getFirstName())) {
            errors.add("First name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.");
        }

        // Validate last name
        if (request.getLastName() == null || !isValidName(request.getLastName())) {
            errors.add("Last name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.");
        }

        // Validate email format
        if (request.getEmail() == null || !isValidEmail(request.getEmail())) {
            errors.add("Invalid email format. Please ensure it follows the format username@domain.com.");
        }

        // Validate password complexity
        if (request.getPassword() == null || !isValidPassword(request.getPassword())) {
            errors.add("Password must be 8-16 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.");
        }

        return errors;
    }

    /**
     * Checks whether a name is valid.
     * <p>
     * A valid name:
     * <ul>
     *   <li>Is 1–50 characters long</li>
     *   <li>Contains only Latin letters, hyphens (-), or apostrophes (')</li>
     * </ul>
     *
     * @param name The name to validate.
     * @return {@code true} if the name is valid; {@code false} otherwise.
     */
    public static boolean isValidName(String name) {
        return name.matches("^[A-Za-z'\\-]{1,50}$");
    }

    /**
     * Validates the format of an email address.
     * <p>
     * Expected format: username@domain.com
     *
     * @param email The email address to validate.
     * @return {@code true} if the email format is valid; {@code false} otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Validates password strength based on the following criteria:
     * <ul>
     *   <li>8–16 characters in length</li>
     *   <li>At least one uppercase letter</li>
     *   <li>At least one lowercase letter</li>
     *   <li>At least one digit</li>
     *   <li>At least one special character from [!@#$%^&*(),.?":{}|<>]</li>
     * </ul>
     *
     * @param password The password to validate.
     * @return {@code true} if the password meets all criteria; {@code false} otherwise.
     */
    public static boolean isValidPassword(String password) {
        return password.length() >= 8 && password.length() <= 16 &&
                password.chars().anyMatch(Character::isUpperCase) &&
                password.chars().anyMatch(Character::isLowerCase) &&
                password.chars().anyMatch(Character::isDigit) &&
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
}
