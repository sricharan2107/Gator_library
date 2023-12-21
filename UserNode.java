import java.sql.Timestamp;

public class UserNode {
    public String userId;
    public int priority;
    public Timestamp timestamp;

    public UserNode(String userId, int priority, Timestamp timestamp){
        this.userId = userId;
        this.priority = priority;
        this.timestamp = timestamp;
    }
}
