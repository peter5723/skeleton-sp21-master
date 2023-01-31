package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date; // TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */

    private String message;
    private BlobInfo blobInfo;
    //这个我得改一下， 不要存储blobs而是存储blobs的hash。
    //然后文件夹里分开存放Commit和blobs

    //may all restart。
    //and date
    private String date;
    /* TODO: fill in the rest of this class. */
    //我想了想，应该让commit就直接作为树的Node比较好
    private String parent1;
    private String parent2;
    private String child1;
    private String child2;
    //这个可能也是存储Hash字符就可以了。

    //题目要求二叉树即可；
    public Commit() {
        message = "initial commit";
        date = "00:00:00 UTC, Thursday, 1 January 1970";
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
}
