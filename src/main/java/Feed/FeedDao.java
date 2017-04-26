package Feed;

import Article.Article;
import User.User;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.javalite.activejdbc.Base;

import java.util.List;

public class FeedDao {

    public String getAllFeeds(String token) {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://ec2-54-75-235-2.eu-west-1.compute.amazonaws.com:5432/de1pf520uoh97?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "nxhigkkgenuvdx",  "ccbcd00eacce253153d7c2bef421e065072541d4168d562035ae5a99fd9dcd41");
        try {
            User user = User.findFirst("token = ?", token);
            String feeds = Feed.where("user_id = ?", user.get("id")).toJson(true);
            Base.close();
            return feeds;
        } catch (Exception e) {
            Base.close();
            System.out.println(e.getMessage());
        }
        Base.close();
        return null;
    }

    public String getArticles(String feed_id) {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://ec2-54-75-235-2.eu-west-1.compute.amazonaws.com:5432/de1pf520uoh97?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "nxhigkkgenuvdx",  "ccbcd00eacce253153d7c2bef421e065072541d4168d562035ae5a99fd9dcd41");

        try {
            String articles = Article.where("feed_id = ?", Integer.parseInt(feed_id)).toJson(true);
            Base.close();
            return articles;
        } catch (Exception e) {
            Base.close();
            System.out.println(e.getMessage());
        }

        Base.close();
        return null;
    }

    public Feed createFeed(User user, SyndFeed fee) {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://ec2-54-75-235-2.eu-west-1.compute.amazonaws.com:5432/de1pf520uoh97?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "nxhigkkgenuvdx",  "ccbcd00eacce253153d7c2bef421e065072541d4168d562035ae5a99fd9dcd41");
        Feed feed = new Feed();
        try {
            feed.set("title", fee.getTitle());
            feed.set("url", fee.getLink());
            feed.set("user_id", user.get("id"));
            user.add(feed);

            List<SyndEntry> articles = fee.getEntries();
            for (SyndEntry entry : articles) {
                Article article = new Article();
                article.set("title", entry.getTitle());
                article.set("url", entry.getLink());
                article.set("description", entry.getDescription().getValue());
                article.set("read", false);

                feed.add(article);
            }
        }
        catch (Exception e) {
            Base.close();
            throw new IllegalArgumentException(e.getMessage());
        }
        Base.close();
        return feed;
    }

    public void deleteFeed(User user, String feed_id) {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://ec2-54-75-235-2.eu-west-1.compute.amazonaws.com:5432/de1pf520uoh97?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "nxhigkkgenuvdx",  "ccbcd00eacce253153d7c2bef421e065072541d4168d562035ae5a99fd9dcd41");
        try {
            Feed.delete("id = ? and user_id = ?", Integer.parseInt(feed_id), user.get("id"));
        }
        catch (Exception e) {
            Base.close();
            throw new IllegalArgumentException("Error on delete");
        }
        Base.close();
    }

    public String getTrending(User user, String page) {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://ec2-54-75-235-2.eu-west-1.compute.amazonaws.com:5432/de1pf520uoh97?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "nxhigkkgenuvdx",  "ccbcd00eacce253153d7c2bef421e065072541d4168d562035ae5a99fd9dcd41");

        try {
            String articles = Article.findBySQL("select articles.* from articles join feeds on feeds.id = articles.feed_id join users on feeds.user_id = users.id WHERE users.id = ? ORDER BY published LIMIT ? OFFSET ?", user.get("id"), 2, (2*Integer.parseInt(page))).toJson(true);
            Base.close();
            return articles;
        }
        catch (Exception e) {
            Base.close();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
