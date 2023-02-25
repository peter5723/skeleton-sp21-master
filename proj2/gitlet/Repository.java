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

    public static final File HEAD_DIR = Utils.join(GITLET_DIR, "head");

    public static final File BLOB_DIR = Utils.join(GITLET_DIR, "blob");

    public static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commit");

    public static final File INDEX_DIR = Utils.join(GITLET_DIR, "index");

    public static final File BRANCH_DIR = join(GITLET_DIR, "branch");

    /* TODO: fill in the rest of this class. */

    //private static String master;
    //当前节点
    //注意，我一直想错了
    //master也是要“离线储存”的

    public static String getHead() {
        File headFile = HEAD_DIR;
        Head s = readObject(headFile, Head.class);
        return s.getSha1();
    }

    public static void setHead(String sha1) {
        //默认指向同一个branch
        File headFile = join(HEAD_DIR);
        Head head = readObject(headFile, Head.class);
        head.setSha1(sha1);
        writeObject(headFile, head);
    }

    public static void setHeadWithBranch(String branchName) {
        File branchFile = join(BRANCH_DIR, branchName);

        if(branchFile.exists()){
            String branchSha1 =  readObject(branchFile, String.class);
            File headFile = HEAD_DIR;
            Head newHead = new Head();
            newHead.setBranchName(branchName);
            newHead.setSha1(branchSha1);
            writeObject(headFile, newHead);
        }



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
        //sha1只能对字符串使用求解hash
        String msha = Utils.sha1(stringBuilder.toString());


        //下面构建blob文件夹、commit文件夹
        //index文件则是文件记录
        File blobDir = BLOB_DIR;
        File commitDir = COMMIT_DIR;
        File branchDir = BRANCH_DIR;
        //File indexFile = Utils.join(GITLET_DIR, "index");
        //indexFile.createNewFile();
        blobDir.mkdir();
        commitDir.mkdir();
        branchDir.mkdir();

        File fm = Utils.join(COMMIT_DIR,msha);
        File masterBranch = join(BRANCH_DIR, "master");
        writeObject(masterBranch, msha);
        setHeadWithBranch("master");
        //Head指向当前commit
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
        //只有在文件是文本文件的时候才可以使用readContentsAsString。
        Blob b = new Blob();
        b.setContents(a);
        b.setFilename(arg);
        String bSha1 = b.getSha1();


        File f2 = Utils.join(BLOB_DIR,bSha1);
        Utils.writeObject(f2, b);
        //将暂存的blob存储到blob文件夹中,不用管是否重复了

        File indexFile = Utils.join(INDEX_DIR);
        BlobInfo bInfo = new BlobInfo();
        if(indexFile.exists()) {
            bInfo = Utils.readObject(indexFile, BlobInfo.class);
        }
        bInfo.add(arg,bSha1);
        //如果add的文件binfo中已经存在，就会自动更新成新版本的
        //最后，若存储的blob和前面一个commit中的完全一致，则不应add，将之从blobinfo列表中删除

        Commit headCommit = headCommit();
        BlobInfo oldBlobInfo = headCommit.getBlobInfo();
        String oldSha1 = oldBlobInfo.find(arg);
        if(oldSha1!=null&&oldSha1.equals(bSha1)) {
            bInfo.remove(arg);
        }
        //这很简单，因为内容相同文本的sha1是必然相同的


        Utils.writeObject(indexFile,bInfo);
    }


    //判断有无init
    public static void judgeInit() {
        File f = Utils.join(GITLET_DIR);
        if(!f.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    public static void commitGitlet(String arg) {

        Commit m = new Commit();
        m.setParent1(Repository.getHead());

        File parentCommitFile = Utils.join(COMMIT_DIR, Repository.getHead());
        File indexFile = Utils.join(INDEX_DIR);

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
                newBlobInfo.add(fn,oldBlobInfo.find(fn),oldBlobInfo.findIsRemoved(fn));
            }
        }
        m.setBlobInfo(newBlobInfo);
        //至此添加完了文件和hash和是否被删除的信息，再设置一下日期和信息即可
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
        //设置新的Head
        Head head = readObject(HEAD_DIR, Head.class);
        Repository.setHead(mSha1);
        File branchFile = join(BRANCH_DIR, head.getBranchName());
        //TODO:branch不存在
        writeObject(branchFile, head.getSha1());
        // 更新index的情况。可以直接清空
        BlobInfo newIndex = new BlobInfo();
        writeObject(INDEX_DIR, newIndex);


    }

    public static void log() {
        //先从head找当前commit
        Commit m = headCommit();
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

    public static void globalLog() {
        List<String> allCommitName = plainFilenamesIn(COMMIT_DIR);
        for (String name:allCommitName) {
            File commitFile = join(COMMIT_DIR, name);
            Commit m = readObject(commitFile, Commit.class);
            System.out.println("===");
            System.out.println("commit "+ m.getHash());
            System.out.println("Date: "+m.getDate());
            System.out.println(m.getMessage());
            System.out.println("");
        }
    }

    private static Commit headCommit() {

        String s = Repository.getHead();
        File lastCommit = Utils.join(GITLET_DIR, "commit", s);
        Commit m = readObject(lastCommit, Commit.class);
        return m;
    }

    private static BlobInfo indexBlob() {
        File index = join(INDEX_DIR);
        BlobInfo b = readObject(index, BlobInfo.class);
        return b;
    }
    public static void remove(String arg) {

        Commit master = headCommit();
        BlobInfo oldBlob = master.getBlobInfo();
        BlobInfo indexBlob = indexBlob();

        if (!oldBlob.isExist(arg) && !indexBlob.isExist(arg)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        indexBlob.remove(arg);
        if(oldBlob.isExist(arg)) {
            indexBlob.add(arg, oldBlob.find(arg), true);
            File argFile = join(CWD, arg);
            restrictedDelete(argFile);
        }

        writeObject(INDEX_DIR, indexBlob);

    }

    public static void find(String arg) {
        int isPrint = 0;
        List<String> allCommitName = plainFilenamesIn(COMMIT_DIR);
        for (String name:allCommitName) {
            File commitFile = join(COMMIT_DIR, name);
            Commit m = readObject(commitFile, Commit.class);
            if (arg.equals(m.getMessage())) {
                System.out.println(m.getHash());
                isPrint++;
            }
        }
        if (isPrint==0) {
            System.out.println("Found no commit with that message");
        }
    }

    public static void getStatus() {
        System.out.println("=== Branches ===");
        //TODO 等我完善了branch再加
        System.out.println("*master");
        System.out.println("");
        Commit headCommit = headCommit();
        BlobInfo oldBlob = headCommit.getBlobInfo();
        BlobInfo indexBlob = indexBlob();
        Set<String> stageFile = new HashSet<>();
        Set<String> removeFile = new HashSet<>();
        Set<String> allFile = indexBlob.getAllFilename();
        for(String oneFile:allFile) {
            File thisFile = join(CWD, oneFile);
            if(indexBlob.findIsRemoved(oneFile)) {
                removeFile.add(oneFile);
            } else if(!indexBlob.findIsRemoved(oneFile)
                    &&thisFile.exists()
                    &&sha1(readContentsAsString(thisFile))
                    .equals(indexBlob.find(oneFile))) {
                stageFile.add(oneFile);
            }
        }
        System.out.println("=== Staged Files ===");
        for(String i:stageFile) {
            System.out.println(i);
        }
        System.out.println("");
        System.out.println("=== Removed Files ===");
        for(String i:removeFile) {
            System.out.println(i);
        }
        System.out.println("");
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println("");
        System.out.println("=== Untracked Files ===");
        System.out.println("");
        //TODO:剩下的条件判断有点麻烦
    }
}
