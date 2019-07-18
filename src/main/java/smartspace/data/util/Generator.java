package smartspace.data.util;

import net.bytebuddy.utility.RandomString;

public class Generator {


    public static String email(String username) {
        if (username==null)
            username=new RandomString(6).nextString();
        String email = "";
        email = username + "@gmail.com";
        return email;
    }
}
