/*
package smartspace.data.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars ))
            return false;
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars ))
            return false;
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers ))
            return false;
        if (password.contains(" "))
            return false;
        return true;
    }
}
*/
