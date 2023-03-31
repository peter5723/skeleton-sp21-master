package gitlet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;



/**
 * Represents a gitlet repository.
 *  It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author peter
 */
public class Repository {
    /**
     *  add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File HEAD_DIR = Utils.join(GITLET_DIR, "head");

    public static final File BLOB_DIR = Utils.join(GITLET_DIR, "blob");

    public static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commit");

    public static final File INDEX_DIR = Utils.join(GITLET_DIR, "index");

    public static final File BRANCH_DIR = join(GITLET_DIR, "branch");

    /*  fill in the rest of this class. */


    public static String getHead() {
        //return head's Commit's sha1
        File headFile = HEAD_DIR;
        Head s = readObject(headFile, Head.class);
        return s.getSha1();
    }

    public static void setHead(String sha1) {
        //point to the same branch (default)
        File headFile = join(HEAD_DIR);
        Head head = readObject(headFile, Head.class);
        head.setSha1(sha1);
        writeObject(headFile, head);
    }

    public static void setHeadWithBranch(String branchName) {
        File branchFile = join(BRANCH_DIR, branchName);

        if (branchFile.exists()) {
            String branchSha1 = readObject(branchFile, String.class);
            File headFile = HEAD_DIR;
            Head newHead = new Head();
            newHead.setBranchName(branchName);
            newHead.setSha1(branchSha1);
            writeObject(headFile, newHead);
        }


    }


    public static void initGitlet() throws IOException {
        File f = Utils.join(GITLET_DIR);
        if (f.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        //create folds
        f.mkdir();
        //create empty commit
        Commit m = new Commit();
        StringBuilder stringBuilder = new StringBuilder(m.getMessage());
        stringBuilder.append(m.getDate());
        //sha1 can only get hash of a string
        String msha = Utils.sha1(stringBuilder.toString());


        File blobDir = BLOB_DIR;
        File commitDir = COMMIT_DIR;
        File branchDir = BRANCH_DIR;
        //File indexFile = Utils.join(GITLET_DIR, "index");
        //indexFile.createNewFile();
        blobDir.mkdir();
        commitDir.mkdir();
        branchDir.mkdir();

        File fm = Utils.join(COMMIT_DIR, msha);
        File masterBranch = join(BRANCH_DIR, "master");
        writeObject(masterBranch, msha);
        setHeadWithBranch("master");
        //Head to the current commit
        Utils.writeObject(fm, m);
        //store first commit
        BlobInfo newIndex = new BlobInfo();
        writeObject(INDEX_DIR, newIndex);
    }

    public static void addGitlet(String arg) {
        //firstly, search for the file and quit if fails.
        File f = Utils.join(CWD, arg);
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        String a = Utils.readContentsAsString(f);
        //only when file is a wenbenwenjian you can use readContentsAsString
        Blob b = new Blob();
        b.setContents(a);
        b.setFilename(arg);
        String bSha1 = b.getSha1();


        File f2 = Utils.join(BLOB_DIR, bSha1);
        Utils.writeObject(f2, b);

        File indexFile = Utils.join(INDEX_DIR);
        BlobInfo bInfo = new BlobInfo();
        if (indexFile.exists()) {
            bInfo = Utils.readObject(indexFile, BlobInfo.class);
        }
        bInfo.add(arg, bSha1);


        Commit headCommit = headCommit();
        BlobInfo oldBlobInfo = headCommit.getBlobInfo();
        String oldSha1 = oldBlobInfo.find(arg);
        if (oldSha1.isEmpty() && oldSha1.equals(bSha1)) {
            bInfo.remove(arg);
        }


        Utils.writeObject(indexFile, bInfo);
    }


    //judge if init
    public static void judgeInit() {
        File f = Utils.join(GITLET_DIR);
        if (!f.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    public static void commitGitlet(String arg) {
        commitGitletWithParent2(arg, null);
    }

    private static void commitGitletWithParent2(String arg, String parent2Sha) {
        if (arg.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        //m is the new commit
        Commit m = new Commit();
        m.setParent1(Repository.getHead());
        m.setParent2(parent2Sha);
        File parentCommitFile = Utils.join(COMMIT_DIR, Repository.getHead());
        File indexFile = Utils.join(INDEX_DIR);

        Commit parentCommit = Utils.readObject(parentCommitFile, Commit.class);
        BlobInfo index = Utils.readObject(indexFile, BlobInfo.class);
        if (index.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }


        BlobInfo newBlobInfo = index;
        BlobInfo oldBlobInfo = parentCommit.getBlobInfo();

        Set<String> oldFileNames = oldBlobInfo.getAllFilename();
        for (String fn : oldFileNames) {
            if (!newBlobInfo.isExist(fn)) {
                newBlobInfo.add(fn, oldBlobInfo.find(fn), oldBlobInfo.findIsRemoved(fn));
            }
        }
        m.setBlobInfo(newBlobInfo);

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        String strDate = dateFormat.format(date);
        m.setDate(strDate);
        m.setMessage(arg);

        String mSha1 = m.getHash();
        //store commit
        File commitNewFile = Utils.join(GITLET_DIR, "commit", mSha1);
        Utils.writeObject(commitNewFile, m);
        //set new Head
        Head head = readObject(HEAD_DIR, Head.class);
        Repository.setHead(mSha1);
        //update branch
        File branchFile = join(BRANCH_DIR, head.getBranchName());
        //head has been updated, so it should be read again. or just use the new commit's sha
        writeObject(branchFile, mSha1);
        // clean index
        BlobInfo newIndex = new BlobInfo();
        writeObject(INDEX_DIR, newIndex);
    }


    public static void log() {
        //get commit from head
        Commit m = headCommit();
        while (m.getParent1() != null) {
            System.out.println("===");
            System.out.println("commit " + m.getHash());
            if (m.getParent2()!=null) {
                printMerge(m);
            }
            System.out.println("Date: " + m.getDate());
            System.out.println(m.getMessage());
            System.out.println("");
            String sParent = m.getParent1();
            File nextCommit = join(GITLET_DIR, "commit", sParent);
            m = readObject(nextCommit, Commit.class);
        }
        //print first commit
        System.out.println("===");
        System.out.println("commit " + m.getHash());
        System.out.println("Date: " + m.getDate());
        System.out.println(m.getMessage());
        System.out.println("");
    }

    private static void printMerge(Commit m) {
        String parent1 = m.getParent1().substring(0,7);
        String parent2 = m.getParent2().substring(0,7);
        System.out.println("Merge: "+parent1+" "+parent2);
    }

    public static void globalLog() {
        List<String> allCommitName = plainFilenamesIn(COMMIT_DIR);
        for (String name : allCommitName) {
            File commitFile = join(COMMIT_DIR, name);
            Commit m = readObject(commitFile, Commit.class);
            System.out.println("===");
            System.out.println("commit " + m.getHash());
            System.out.println("Date: " + m.getDate());
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
        //If a file is staged for removal, it is an untracked file.
        Commit master = headCommit();
        BlobInfo oldBlob = master.getBlobInfo();
        BlobInfo indexBlob = indexBlob();

        if ((!oldBlob.isExist(arg) || oldBlob.isExist(arg) && oldBlob.findIsRemoved(arg))
                && (!indexBlob.isExist(arg) || indexBlob.isExist(arg) && indexBlob().findIsRemoved(arg))) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        indexBlob.remove(arg);
        if (oldBlob.isExist(arg) && !oldBlob.findIsRemoved(arg)) {
            indexBlob.add(arg, oldBlob.find(arg), true);
            File argFile = join(CWD, arg);
            restrictedDelete(argFile);
        }

        writeObject(INDEX_DIR, indexBlob);

    }

    public static void find(String arg) {
        int isPrint = 0;
        List<String> allCommitName = plainFilenamesIn(COMMIT_DIR);
        for (String name : allCommitName) {
            File commitFile = join(COMMIT_DIR, name);
            Commit m = readObject(commitFile, Commit.class);
            if (arg.equals(m.getMessage())) {
                System.out.println(m.getHash());
                isPrint++;
            }
        }
        if (isPrint == 0) {
            System.out.println("Found no commit with that message");
        }
    }

    public static void getStatus() {
        System.out.println("=== Branches ===");
        Head head = readObject(HEAD_DIR, Head.class);
        List<String> branchList = Utils.plainFilenamesIn(BRANCH_DIR);
        Collections.sort(branchList);
        for (String oneBranch : branchList) {
            if (oneBranch.equals(head.getBranchName())) {
                System.out.print("*");
            }
            System.out.println(oneBranch);
        }
        System.out.println("");
        Commit headCommit = headCommit();
        BlobInfo oldBlob = headCommit.getBlobInfo();
        BlobInfo indexBlob = indexBlob();
        Set<String> stageFile = new HashSet<>();
        Set<String> removeFile = new HashSet<>();
        Set<String> allFile = indexBlob.getAllFilename();
        for (String oneFile : allFile) {
            File thisFile = join(CWD, oneFile);
            if (indexBlob.findIsRemoved(oneFile)) {
                removeFile.add(oneFile);
            } else if (!indexBlob.findIsRemoved(oneFile)
                    && thisFile.exists()
                    && sha1(readContentsAsString(thisFile))
                    .equals(indexBlob.find(oneFile))) {
                stageFile.add(oneFile);
            }
        }
        System.out.println("=== Staged Files ===");
        for (String i : stageFile) {
            System.out.println(i);
        }
        System.out.println("");
        System.out.println("=== Removed Files ===");
        for (String i : removeFile) {
            System.out.println(i);
        }
        System.out.println("");
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println("");
        System.out.println("=== Untracked Files ===");
        System.out.println("");
        //TODO
    }

    private static String findContents(String filename, Commit c) {
        BlobInfo currentBlob = c.getBlobInfo();
        if (!currentBlob.isExist(filename)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String shaOfFile = currentBlob.find(filename);
        File contextFile = join(BLOB_DIR, shaOfFile);
        Blob b = readObject(contextFile, Blob.class);
        String s = b.getContents();
        return s;
    }

    public static void checkout(String filename) {
        Commit headCommit = headCommit();
        String s = findContents(filename, headCommit);
        File neededFile = join(CWD, filename);
        writeContents(neededFile, s);

    }

    private static String findTheCommit(String commitID) {
        List<String> allCommitName = Utils.plainFilenamesIn(COMMIT_DIR);
        int lengthOfID = commitID.length();
        int hasMatched = 0;
        String theCommit = new String();
        for (String commitName : allCommitName) {
            if (commitName.substring(0, lengthOfID).equals(commitID)) {
                hasMatched++;
                theCommit = commitName;
                break;
            }
        }
        if (hasMatched == 0) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        return theCommit;
    }

    public static void checkout(String commitID, String filename) {

        String theCommit = findTheCommit(commitID);
        File CommitFile = join(COMMIT_DIR, theCommit);
        Commit c = readObject(CommitFile, Commit.class);
        String s = findContents(filename, c);
        File neededFile = join(CWD, filename);
        writeContents(neededFile, s);

    }

    public static void checkoutWithBranch(String branchName) {
        File branchFile = join(BRANCH_DIR, branchName);
        if (!branchFile.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        Head head = readObject(HEAD_DIR, Head.class);
        String currentBranch = head.getBranchName();
        if (currentBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        Commit oldCommit = headCommit();
        BlobInfo oldBlobInfo = oldCommit.getBlobInfo();

        String newCommitSha1 = readObject(branchFile, String.class);
        File newCommitFile = join(COMMIT_DIR, newCommitSha1);
        Commit newCommit = readObject(newCommitFile, Commit.class);
        BlobInfo newBlobInfo = newCommit.getBlobInfo();

        List<String> cwdList = plainFilenamesIn(CWD);
        for (String oneFilename : cwdList) {
            if (newBlobInfo.isExist(oneFilename) && !oldBlobInfo.isExist(oneFilename)
                    || oldBlobInfo.isExist(oneFilename) && oldBlobInfo.findIsRemoved(oneFilename)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        for (String oneOldFile : oldBlobInfo.getAllFilename()) {
            //delete if not exist
            if ((!newBlobInfo.isExist(oneOldFile)||newBlobInfo.findIsRemoved(oneOldFile))
                    && !oldBlobInfo.findIsRemoved(oneOldFile)) {
                File file = join(CWD, oneOldFile);
                if (file.exists()) {
                    restrictedDelete(file);
                }
            }
        }

        for (String oneNewFile : newBlobInfo.getAllFilename()) {
            if (!newBlobInfo.findIsRemoved(oneNewFile)) { //don't consider removed
                Blob b = findBlob(newBlobInfo, oneNewFile);
                String contents = b.getContents();
                File file = join(CWD, oneNewFile);
                writeContents(file, contents);
            }
        }

        //head's branch
        setHeadWithBranch(branchName);
        //clean index
        BlobInfo newIndex = new BlobInfo();
        writeObject(INDEX_DIR, newIndex);

    }

    private static Blob findBlob(BlobInfo blobInfo, String filename) {
        if (!blobInfo.isExist(filename)) {
            return null;
        }
        String sha1 = blobInfo.find(filename);
        File blobFile = join(BLOB_DIR, sha1);
        Blob b = readObject(blobFile, Blob.class);
        return b;
    }

    public static void setNewBranch(String branchName) {
        List<String> BranchList = Utils.plainFilenamesIn(BRANCH_DIR);
        if (BranchList.contains(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        File newBranchFile = join(BRANCH_DIR, branchName);
        String headCommitSha1 = getHead();
        writeObject(newBranchFile, headCommitSha1);
    }

    public static void deleteBranch(String branchName) {
        Head head = readObject(HEAD_DIR, Head.class);
        String currentBranch = head.getBranchName();
        if (branchName.equals(currentBranch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        File waitToDelete = join(BRANCH_DIR, branchName);
        if (!waitToDelete.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        waitToDelete.delete();
    }

    public static void reset(String commitID) {
        //correct head and corresponding branch at the same time
        String theCommit = findTheCommit(commitID);
        Head head = readObject(HEAD_DIR, Head.class);
        String currentBranch = head.getBranchName();
        File branchFile = join(BRANCH_DIR, currentBranch);

        setNewBranch("tempBranch");
        checkoutWithBranch("tempBranch");

        writeObject(branchFile, theCommit);
        checkoutWithBranch(currentBranch);

        deleteBranch("tempBranch");


    }

    public static void merge(String branchName) {
        //zero: manage failure cases
        BlobInfo indexBlob = indexBlob();
        if (!indexBlob.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        File branchFile = join(BRANCH_DIR, branchName);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        Head head = readObject(HEAD_DIR, Head.class);
        if (head.getBranchName().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        Commit splitCommit = findSplitCommit(branchName);
        Commit branchCommit = getCommitFromBranch(branchName);
        Commit headCommit = headCommit();

        List<String> cwdFiles = plainFilenamesIn(CWD);
        Set<String> nowTrackedFiles = headCommit.getBlobInfo().getAllFilename();
        List<String> unTrackedFiles = new ArrayList<>();
        unTrackedFiles.addAll(cwdFiles);
        unTrackedFiles.removeAll(nowTrackedFiles);
        for (String untrack : unTrackedFiles) {
            if (branchCommit.getBlobInfo().getAllFilename().contains(untrack)) {
                System.out.println("There is an untracked file in the way; " +
                        "delete it, or add and commit it first.");
                System.exit(0);
            }
        }


        //first we need to find split commit

        if (splitCommit.equals(branchCommit)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (splitCommit.equals(headCommit)) {
            checkoutWithBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }

        //second, consider the logic, we should check the file
        //information in given branch and compare to the current
        //and split node.
        BlobInfo infoGiven = branchCommit.getBlobInfo();
        BlobInfo infoSplit = splitCommit.getBlobInfo();
        BlobInfo infoCur = headCommit.getBlobInfo();
        boolean isConflicted = false;
        for (String fileInGiven : infoGiven.getAllFilename()) {

            Boolean isRemovedInGiven = infoGiven.findIsRemoved(fileInGiven);
            Boolean isRemovedInCur = infoCur.findIsRemoved(fileInGiven);
            String CommitID = readObject(join(BRANCH_DIR, branchName), String.class);
            //apart from sha1(contents) you should also consider whether the file is removed
            if (!equalState(fileInGiven, infoGiven, infoCur) && equalState(fileInGiven, infoSplit, infoCur)) {
                if (!isRemovedInGiven) {
                    checkout(CommitID, fileInGiven);
                    addGitlet(fileInGiven);
                } else {
                    if (infoCur.isExist(fileInGiven) && !isRemovedInCur) {
                        remove(fileInGiven);
                    }
                }
            } else if (!equalState(fileInGiven, infoSplit, infoCur) && !equalState(fileInGiven, infoCur, infoGiven)
                    && !equalState(fileInGiven, infoGiven, infoSplit)) {
                //all modify so there will be conflicts
                String curContent = new String();
                String givenContent = new String();
                if (infoCur.isExist(fileInGiven)&&!isRemovedInCur) {
                    curContent = findContents(fileInGiven, headCommit);
                }
                if (infoGiven.isExist(fileInGiven)&&!isRemovedInGiven) {
                    givenContent = findContents(fileInGiven, branchCommit);
                }
                String mergeContent = mergeConflict(curContent, givenContent);
                writeContents(join(CWD, fileInGiven), mergeContent);
                addGitlet(fileInGiven);
                isConflicted = true;
            }
        }

        for (String curFile:infoCur.getAllFilename()) {
            //just remain unless conflict.

            Boolean isRemovedInGiven = infoGiven.findIsRemoved(curFile);
            Boolean isRemovedInCur = infoCur.findIsRemoved(curFile);
            if (!equalState(curFile, infoSplit, infoCur) && !equalState(curFile, infoCur, infoGiven)
                    && !equalState(curFile, infoGiven, infoSplit)) {
                String curContent = new String();
                String givenContent = new String();
                if (infoCur.isExist(curFile)&&!isRemovedInCur) {
                    curContent = findContents(curFile, headCommit);
                }
                if (infoGiven.isExist(curFile)&&!isRemovedInGiven) {
                    givenContent = findContents(curFile, branchCommit);
                }
                String mergeContent = mergeConflict(curContent, givenContent);
                writeContents(join(CWD, curFile), mergeContent);
                addGitlet(curFile);
                isConflicted = true;
            }
        }
        //commit
        String commitMsg = "Merged " + branchName + " into " + head.getBranchName()+".";
        StringBuilder sbMsg = new StringBuilder(commitMsg);
        if (isConflicted) {
            System.out.println("Encountered a merge conflict.");
        }
        String parent2Sha = branchCommit.getHash();
        commitGitletWithParent2(sbMsg.toString(), parent2Sha);
    }

    private static boolean equalState(String filename, BlobInfo infoA, BlobInfo infoB) {
        //return whether the file's state is the same in two BlobInfos
        return infoA.find(filename).equals(infoB.find(filename))
                && infoA.findIsRemoved(filename) == infoB.findIsRemoved(filename);
    }

    private static String mergeConflict(String curContent, String givenContent) {
        StringBuilder sb = new StringBuilder();
        sb.append("<<<<<<< HEAD\n");
        sb.append(curContent);
        sb.append("=======\n");
        sb.append(givenContent);
        sb.append(">>>>>>>");
        return sb.toString();
    }

    private static Commit findSplitCommit(String branchName) {
        //given the branch name, return the ancestor of given branch and head.
        Commit headCommit = headCommit();
        Commit branchCommit = getCommitFromBranch(branchName);
        ArrayList<Commit> allAncestorHead = getAncestors(headCommit);
        ArrayList<Commit> allAncestorBranch = getAncestors(branchCommit);
        Collections.reverse(allAncestorHead);
        Collections.reverse(allAncestorBranch);
        ArrayList<Commit> sameAncestor = new ArrayList<>();
        sameAncestor.addAll(allAncestorHead);
        sameAncestor.retainAll(allAncestorBranch);
        //the last same ancestor is the split commit
        return sameAncestor.get(sameAncestor.size() - 1);
    }

    private static Commit getCommitFromBranch(String branchName) {
        //get the commit instance from the branch points.
        List<String> BranchList = Utils.plainFilenamesIn(BRANCH_DIR);
        if (!BranchList.contains(branchName)) {
            System.out.println("branch does not exist.");
            System.exit(0);
        }
        File branchFile = join(BRANCH_DIR, branchName);
        String commitSha = readObject(branchFile, String.class);
        return getCommitFromSha(commitSha);
    }

    private static Commit getCommitFromSha(String sha1) {
        File commitFile = join(COMMIT_DIR, sha1);
        if (!commitFile.exists()) {
            System.out.println("file not exists");
        }
        return readObject(commitFile, Commit.class);
    }

    private static Commit getFirstParentCommit(Commit commit) {
        //get the first parent Commit
        String parentCommitSha = commit.getParent1();
        if (parentCommitSha == null) {
            return null;
        }
        return getCommitFromSha(parentCommitSha);
    }

    private static Commit getSecondParentCommit(Commit commit) {
        //get the 2nd parent Commit
        String parentCommitSha = commit.getParent2();
        if (parentCommitSha == null) {
            return null;
        }
        return getCommitFromSha(parentCommitSha);
    }

    private static ArrayList<Commit> getAncestors(Commit commit) {
        // get all the ancestors of Commit(include commit itself) using bfs.
        ArrayDeque<Commit> queue = new ArrayDeque<>();
        ArrayList<Commit> ancestors = new ArrayList<>();
        queue.push(commit);
        while (!queue.isEmpty()) {
            Commit currentCommit = queue.pop();
            if (!ancestors.contains(currentCommit)) {
                ancestors.add(currentCommit);
            }
            Commit parent1 = getFirstParentCommit(currentCommit);
            Commit parent2 = getSecondParentCommit(currentCommit);
            if (parent1 != null) {
                queue.push(parent1);
            }
            if (parent2 != null) {
                queue.push(parent2);
            }
        }
        return ancestors;
    }

}
