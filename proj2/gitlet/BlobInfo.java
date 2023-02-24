package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class BlobInfo implements Serializable{
    private HashMap<String, String> hashMap;
    private HashMap<String, Boolean> isRemove;
    //存储文件是否被删除
    BlobInfo() {
        hashMap = new HashMap<>();
        isRemove = new HashMap<>();
    }
    public void add(String fileName, String sha1) {
        hashMap.put(fileName,sha1);
        //如果连续两次传入相同的filename，put是自动更新的，故无妨
        isRemove.put(fileName, false);
    }

    public void add(String fileName, String sha1, boolean removema) {
        hashMap.put(fileName, sha1);
        isRemove.put(fileName, removema);
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

    public boolean findIsRemoved(String fn) {
        if(!isExist(fn)) {
            return false;
        }
        return isRemove.get(fn);
    }
    public void remove(String arg) {
        if (isExist(arg)){
            hashMap.remove(arg);
        }
    }
}
