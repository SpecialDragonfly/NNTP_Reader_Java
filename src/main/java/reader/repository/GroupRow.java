package reader.repository;

public class GroupRow {
    private String groupName;
    private boolean canPost;
    private long articleCount;

    public GroupRow(String groupName, boolean canPost, long articleCount) {

        this.groupName = groupName;
        this.canPost = canPost;
        this.articleCount = articleCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isPostingAllowed() {
        return canPost;
    }

    public long getArticleCount() { return articleCount; }
}
