package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class BlobInfo implements Serializable{
    private HashMap<String, String> hashMap;

    BlobInfo() {
        hashMap = new HashMap<>();
    }
    public void add(String fileName, String sha1) {
        hashMap.put(fileName,sha1);
    }

    public boolean isExist(String fileName) {
        return hashMap.containsKey(fileName);
    }

    public boolean isEmpty(){
        return hashMap.isEmpty();
    }

    public Set<String> getAllFilename() {
        return hashMap.keySet();
    }

    public String find(String fn) {
        if (!isExist(fn)){
            return null;
        }
        return hashMap.get(fn);
    }
}
