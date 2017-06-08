package reader.repository;

import reader.MysqlConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReaderDatabase {
    private MysqlConnection connection;

    public ReaderDatabase(MysqlConnection connection) {

        this.connection = connection;
    }

    public void saveGroup(GroupRow group) throws SQLException {
        String sql = "INSERT INTO groups (group_name, can_post, article_count) VALUES (?, ?, ?)";
        PreparedStatement statement = this.connection.getConnection().prepareStatement(sql);
        statement.setString(1, group.getGroupName());
        statement.setBoolean(2, group.isPostingAllowed());
        statement.setLong(3, group.getArticleCount());
        statement.execute();
    }

    public void saveArticle(ArticleRow article) {

    }
}
