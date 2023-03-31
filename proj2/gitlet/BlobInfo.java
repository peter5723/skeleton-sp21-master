package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class BlobInfo implements Serializable {
    private HashMap<String, String> hashMap;
    //hashmap: filename -> sha1
    private HashMap<String, Boolean> isRemove;
    //isRemove: filename -> isRemoved
    //store the information about the file is deleted

    BlobInfo() {
        hashMap = new HashMap<>();
        isRemove = new HashMap<>();
    }

    public void add(String fileName, String sha1) {
        hashMap.put(fileName, sha1);
        isRemove.put(fileName, false);
    }

    public void add(String fileName, String sha1, boolean removema) {
        hashMap.put(fileName, sha1);
        isRemove.put(fileName, removema);
    }

    public boolean isExist(String fileName) {
        return hashMap.containsKey(fileName);
    }

    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    public Set<String> getAllFilename() {
        return hashMap.keySet();
    }

    public String find(String fn) {
        if (!isExist(fn)) {
            return "";
        }
        return hashMap.get(fn);
    }

    public boolean findIsRemoved(String fn) {
        if (!isExist(fn)) {
            return false;
        }
        return isRemove.get(fn);
    }

    public void remove(String arg) {
        if (isExist(arg)) {
            hashMap.remove(arg);
            isRemove.remove(arg);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlobInfo blobInfo = (BlobInfo) o;
        return Objects.equals(hashMap, blobInfo.hashMap)
                && Objects.equals(isRemove, blobInfo.isRemove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashMap, isRemove);
    }
}
