package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {
    //具体的文件内容
    private String contents;
    private String filename;
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getSha1() {
        return Utils.sha1(this.contents);
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
