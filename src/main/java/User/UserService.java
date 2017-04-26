package User;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public User createUser(String username, String password) {
        failIfInvalid(username);
        failIfInvalid(password);
        return userDao.createUser(username, password);
    }

    public String getToken(String username, String password) {
        failIfInvalid(username);
        failIfInvalid(password);
        return userDao.getToken(username, password);
    }

    private void failIfInvalid(String param) {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException("Missing parameters");
        }
    }
}