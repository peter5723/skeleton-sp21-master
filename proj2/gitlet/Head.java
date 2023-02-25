package gitlet;

import java.io.Serializable;

public class Head implements Serializable {
    private String sha1;
    private String BranchName;

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }
}
