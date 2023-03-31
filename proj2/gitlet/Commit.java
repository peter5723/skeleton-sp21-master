package gitlet;

// any imports you need here

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a gitlet commit object.
 * It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author peter
 */
//commit is the tree node.
public class Commit implements Serializable {
    /**
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */

    private String message;
    private BlobInfo blobInfo;
    //store blob's hash
    //store Commit and blobs separately


    private String date;
    /*  fill in the rest of this class. */
    //commit is the node of tree
    private String parent1;
    private String parent2;
    private String child1;
    private String child2;
    //just store hash

    //just binary tree
    public Commit() {
        message = "initial commit";
        date = "Wed Dec 31 16:00:00 1969 -0800";
        blobInfo = new BlobInfo();
        //empty and full
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BlobInfo getBlobInfo() {
        return blobInfo;
    }

    public void setBlobInfo(BlobInfo blobInfo) {
        this.blobInfo = blobInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParent1() {
        return parent1;
    }

    public void setParent1(String parent1) {
        this.parent1 = parent1;
    }

    public String getParent2() {
        return parent2;
    }

    public void setParent2(String parent2) {
        this.parent2 = parent2;
    }

    public String getChild1() {
        return child1;
    }

    public void setChild1(String child1) {
        this.child1 = child1;
    }

    public String getChild2() {
        return child2;
    }

    public void setChild2(String child2) {
        this.child2 = child2;
    }

    public String getHash() {
        if (message == null || date == null) {
            return null;
        }
        StringBuilder s = new StringBuilder(message);
        s.append(date);
        return Utils.sha1(s.toString());
        //each commit has its unique hash.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Commit commit = (Commit) o;
        return Objects.equals(message, commit.message)
                && Objects.equals(blobInfo, commit.blobInfo)
                && Objects.equals(date, commit.date)
                && Objects.equals(parent1, commit.parent1)
                && Objects.equals(parent2, commit.parent2)
                && Objects.equals(child1, commit.child1)
                && Objects.equals(child2, commit.child2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, blobInfo, date, parent1, parent2, child1, child2);
    }
}
