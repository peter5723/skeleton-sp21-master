package gitlet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");


    /* TODO: fill in the rest of this class. */

    //private static String master;
    //当前节点
    //注意，我一直想错了
    //master也是要“离线储存”的

    public static String getMaster() {
        File masterFile = Utils.join(GITLET_DIR, "master");
        String s = readObject(masterFile, String.class);
        return s;
    }

    public static void setMaster(String m) {
        File masterFile = Utils.join(GITLET_DIR, "master");
        writeObject(masterFile,m);
    }

    public static void initGitlet() throws IOException {
        //首先看是否有已经存在的gitlet系统，有的话报错退出
        File f = Utils.join(GITLET_DIR);
        if(f.exists()){
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        //创建Gitlet文件夹
        f.mkdir();
        //创建一个新的空的commit
        Commit m = new Commit();
        StringBuilder stringBuilder = new StringBuilder(m.getMessage());
        stringBuilder.append(m.getDate());
        //sha1只能对字符串使用求解hash？？？
        String msha = Utils.sha1(stringBuilder.toString());


        //下面构建blob文件夹、commit文件夹
        //index文件则是文件记录
        File blobDir = Utils.join(GITLET_DIR, "blob");
        File commitDir = Utils.join(GITLET_DIR, "commit");
        //File indexFile = Utils.join(GITLET_DIR, "index");
        //indexFile.createNewFile();
        blobDir.mkdir();
        commitDir.mkdir();

        File fm = Utils.join(GITLET_DIR,"commit",msha);
        setMaster(msha);
        //master指向当前commit
        Utils.writeObject(fm,m);
        //存储第一个commit
    }

    public static void addGitlet(String arg) {
        //首先在文件列表中查找文件，找不到则退出
        File f = Utils.join(CWD, arg);
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        String a = Utils.readContentsAsString(f);
        Blob b = new Blob();
        b.setContents(a);
        b.setFilename(arg);
        String bSha1 = b.getSha1();
        File f2 = Utils.join(GITLET_DIR,"blob",bSha1);
        Utils.writeObject(f2, b);
        //将暂存的blob存储到blob文件夹中

        File indexFile = Utils.join(GITLET_DIR, "index");
        BlobInfo bInfo = new BlobInfo();
        if(indexFile.exists()) {
            bInfo = Utils.readObject(indexFile, BlobInfo.class);
        }
        bInfo.add(arg,bSha1);
        Utils.writeObject(indexFile,bInfo);

        //需要暂存的文件名和它的sha hash记录到index当中
        //感觉blobinfo用hashmap存储或许更好

        //TODO,最后，若存储的blob和前面一个commit中的完全一致，则不应add
    }


    //判断有无init
    public static void JudgeInit() {
        File f = Utils.join(GITLET_DIR);
        if(!f.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    public static void commitGitlet(String arg) {

        Commit m = new Commit();
        m.setParent1(Repository.getMaster());

        File parentCommitFile = Utils.join(GITLET_DIR,"commit", Repository.getMaster());
        File indexFile = Utils.join(GITLET_DIR, "index");

        Commit parentCommit = Utils.readObject(parentCommitFile, Commit.class);
        BlobInfo index = Utils.readObject(indexFile, BlobInfo.class);
        if (index==null){
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        //上面我们已经得到了index，commit的意思：以index为准，没有的再继承parent commit
        BlobInfo newBlobInfo = index;
        BlobInfo oldBlobInfo = parentCommit.getBlobInfo();

        Set<String> oldFileNames = oldBlobInfo.getAllFilename();
        for(String fn:oldFileNames){
            if(!newBlobInfo.isExist(fn)){
                newBlobInfo.add(fn,oldBlobInfo.find(fn));
            }
        }

        m.setBlobInfo(newBlobInfo);
        //TODO:这里只有add和继承，还要考虑rm后，有些键值需要删除
        //至此添加完了文件和hash的信息，再设置一下日期和信息即可
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat  =new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        String strDate = dateFormat.format(date);
        m.setDate(strDate);
        m.setMessage(arg);
        //日期设置完，根据massage和date信息设置commit的hash
        String mSha1 = m.getHash();
        //储存commit
        File commitNewFile = Utils.join(GITLET_DIR,"commit",mSha1);
        Utils.writeObject(commitNewFile, m);
        //设置新的master
        Repository.setMaster(mSha1);
        //TODO: 更新index的情况。可以直接删除？
    }

    public static void log() {
        //先从master找当前commit
        File masterFile = Utils.join(GITLET_DIR, "master");
        String s = Repository.getMaster();
        File lastCommit = Utils.join(GITLET_DIR, "commit", s);
        Commit m = readObject(lastCommit, Commit.class);
        while (m.getParent1()!=null) {
            System.out.println("===");
            System.out.println("commit "+ m.getHash());
            System.out.println("Date: "+m.getDate());
            System.out.println(m.getMessage());
            System.out.println("");
            String sParent = m.getParent1();
            File nextCommit = join(GITLET_DIR,"commit",sParent);
            m = readObject(nextCommit, Commit.class);
        }
        //print first commit
        System.out.println("===");
        System.out.println("commit "+ m.getHash());
        System.out.println("Date: "+m.getDate());
        System.out.println(m.getMessage());
        System.out.println("");
        //TODO:MERGE log.
    }
}
