package Feed;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import Article.Article;
import User.User;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import static Util.Authentication.getUserByToken;

public class FeedService {

    private final FeedDao feedDao;

    public FeedService() {
        feedDao = new FeedDao();
    }

    public String getAllFeeds(String token) {
        return feedDao.getAllFeeds(token);
    }

    public String getArticles(String token, String id) {
        getUserByToken(token);
        return feedDao.getArticles(id);
    }

    public Feed createFeed(String token, SyndFeed feed) {
        User user = getUserByToken(token);
        return feedDao.createFeed(user, feed);
    }

    private void failIfInvalid(String param) {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException("Missing parameters");
        }
    }

    public void deleteFeed(String token, String feed_id) {
        failIfInvalid(feed_id);
        User user = getUserByToken(token);
        feedDao.deleteFeed(user, feed_id);
    }

    public String getTrending(String token, String page) {
        failIfInvalid(page);
        User user = getUserByToken(token);
        return feedDao.getTrending(user, page);
    }
}