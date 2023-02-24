package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {
    //具体的文件内容
    private String contents;
    private String filename;

    private String sha1;
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getSha1() {
        if (sha1 == null){
            sha1 = Utils.sha1(this.contents);
        }
        return sha1;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
