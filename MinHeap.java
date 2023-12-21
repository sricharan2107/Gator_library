//Data Structure to store the ride details in a Red Black Tree

import java.util.Comparator;
import java.util.PriorityQueue;

public class MinHeap{
    public int bookId;
    public String bookName;
    public String authorName;
    public String availability; 
    public String borrowedBy;
    public PriorityQueue<UserNode> reservationHeap;

    public MinHeap right;
    public MinHeap left;
    public MinHeap parent;

    public int color;
    public MinHeap(){}
    public MinHeap(int bookId, String bookName, String authorName, String availability) {
        this.left = null;
        this.right = null;
        this.parent = null;

        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.availability = availability;
        this.borrowedBy = "None";

        reservationHeap = new PriorityQueue<UserNode>(new Comparator<UserNode>() {
            @Override
            public int compare(UserNode u1, UserNode u2) {
                if(u1.priority == u2.priority)
                    return u1.timestamp.compareTo(u2.timestamp);
                else {
                    return u1.priority-u2.priority;
                }
            }
        });
        this.color = 1;

    }
}
