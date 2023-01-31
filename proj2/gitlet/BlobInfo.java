package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class BlobInfo implements Serializable{
    private HashMap<String, String> hashMap;

    public void add(String fileName, String sha1) {
        hashMap.put(fileName,sha1);
    }

    public boolean find(String fileName) {
        return hashMap.containsKey(fileName);
    }

    public boolean isEmpty(){
        return hashMap.isEmpty();
    }
}
