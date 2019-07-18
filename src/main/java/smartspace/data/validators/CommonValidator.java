package smartspace.data.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CommonValidator is a static class with method to validate a lot of fields.
 * Every method return true if the validation is ok and Throw RuntimeException if the validation is fail
 */
public class CommonValidator {

    public static boolean isEmail(String... emails){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        for (String email:emails) {
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches())
                throw new RuntimeException("email "+email+" is not a valid email");
        }
        return true;

    }

    public static boolean isNull(Object... objects){
        for (Object obj:objects)
            if (!obj.equals(null))
                throw new RuntimeException("object " + obj + " is not null");
        return true;
    }

    public static boolean isNotNull(Object... objects){
        for (Object obj:objects)
            if (obj==null)
                throw new RuntimeException("object "+obj+" is null");
        return true;
    }

    public static boolean minSize(int size,String... strs){
       for (String str:strs)
           if (str.length()<size)
                throw new RuntimeException("The size of string "+str+" is "+str.length()+" and the min required is "+size);
           return true;
    }

}
