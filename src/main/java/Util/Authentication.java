package Util;

import User.User;
import org.javalite.activejdbc.Base;

public class Authentication {

    public static User getUserByToken(String token) {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://ec2-79-125-2-69.eu-west-1.compute.amazonaws.com:5432/daraubdcs0bdl0?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "jrvhxomssbksdv",  "d964711045873dd954a82074cbddaeaa8bdaac4a44a01e4946398a489d70c763");
        User user;
        try {
            user = User.findFirst("token = ?", token);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error connection DB");
        }
        if (user == null)
            throw new IllegalArgumentException("Bad token");
        Base.close();
        return user;
    }

}
