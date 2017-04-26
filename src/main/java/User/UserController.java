package User;

import Util.ResponseError;
import org.json.JSONObject;

import static spark.Spark.*;

import static Util.JsonUtil.*;

public class UserController {

    public UserController(final UserService userService) {

        post("/users", (req, res) -> {
            JSONObject jsonObject = new JSONObject(req.body());
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            User user = userService.createUser(username, password);
            return user.toJson(true);
        });

        post("/users/token", (req, res) -> {
            JSONObject jsonObject = new JSONObject(req.body());
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            String token = userService.getToken(username, password);
            JSONObject json = new JSONObject();
            json.put("token", token);
            return json.toString();
        });

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.type("application/json");
            res.body(toJson(new ResponseError(e)));
        });
    }
}