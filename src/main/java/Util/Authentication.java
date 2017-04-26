package Util;

import User.User;
import org.javalite.activejdbc.Base;

public class Authentication {

    public static User getUserByToken(String token) {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://ec2-54-75-235-2.eu-west-1.compute.amazonaws.com:5432/de1pf520uoh97?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "nxhigkkgenuvdx",  "ccbcd00eacce253153d7c2bef421e065072541d4168d562035ae5a99fd9dcd41");
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
