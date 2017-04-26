package Feed;

import Article.Article;
import Util.ResponseError;
import com.google.gson.Gson;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static Util.JsonUtil.toJson;
import static spark.Spark.*;

public class FeedController {

    public FeedController(final FeedService feedService) {

        get("/feeds", (req, res) -> feedService.getAllFeeds(req.headers("Authorization")));

        get("/feeds/:id", (req, res) -> feedService.getArticles(req.headers("Authorization"), req.params(":id")));

        get("/trending/:page", (req, res) -> {
            String page = req.params(":page");

            String articles = feedService.getTrending(req.headers("Authorization"), page);

            System.err.println(articles);

            JSONObject response = new JSONObject();

            try {
                response.put("articles", new JSONArray(articles));
                response.put("next", Integer.parseInt(page) + 1);
                response.put("current", Integer.parseInt(page));
            }
            catch (Exception e) {

                System.err.println(e.getMessage());
            }
            return response;
        });

        delete("/feeds/:id", (req, res) -> {
            feedService.deleteFeed(req.headers("Authorization"), req.params(":id"));
            return "{\"response\":\"true\"}";
        });


        post("/feeds", (req, res) -> {
            JSONObject jsonObject = new JSONObject(req.body());
            String url = jsonObject.getString("url");

            SyndFeed fee = null;
            try {
                fee = new SyndFeedInput().build(new XmlReader(new URL(url)));
            } catch (FeedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            String title = jsonObject.getString("title");
            String token = req.headers("Authorization");
            Feed feed = feedService.createFeed(token, fee);
            return feed.toJson(true);

        });

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new ResponseError(e)));
        });
    }
}