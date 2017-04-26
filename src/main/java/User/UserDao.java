package User;

import org.apache.commons.codec.digest.DigestUtils;
import org.javalite.activejdbc.Base;

public class UserDao {

    public UserDao() {
    }

    public User createUser(String username, String password) {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://ec2-54-75-235-2.eu-west-1.compute.amazonaws.com:5432/de1pf520uoh97?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "nxhigkkgenuvdx",  "ccbcd00eacce253153d7c2bef421e065072541d4168d562035ae5a99fd9dcd41");
        User user = new User();
        try {
            user.set("username", username);
            user.set("password", password);
            user.set("token", DigestUtils.sha1Hex(username+password));
            user.saveIt();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            Base.close();
            throw new IllegalArgumentException("Error on save");
        }
        Base.close();
        return user;
    }

    public String getToken(String username, String password) {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://ec2-54-75-235-2.eu-west-1.compute.amazonaws.com:5432/de1pf520uoh97?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "nxhigkkgenuvdx",  "ccbcd00eacce253153d7c2bef421e065072541d4168d562035ae5a99fd9dcd41");
        User user;
        try {
            user = User.findFirst("username = ? and password = ?", username, password);
        }
        catch (Exception e) {
            Base.close();
            throw new IllegalArgumentException("Error connection DB");
        }
        Base.close();
        if (user == null)
            throw new IllegalArgumentException("Bad username or password");
        return (String) user.get("token");
    }
}
