package gitlet;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Map;

public class Gitlet {

    /** Repository base folder name. */
    public static final String DEFAULT_REPO_BASE_NAME = ".gitlet";
    public static final String COMMIT_BASE_NAME = "commits";
    public static final String BLOB_BASE_NAME = "blobs";
    private static final String STAGING_AREA_NAME = "staging";
    public static final String ERROR_REPO_EXISTS
            = "Gitlet version-control system already exists in the current directory.";
    public static final String ERROR_INCORRECT_OPERANDS = "Incorrect operands.";
    public static final String ERROR_NO_COMMIT_MESSAGE = "Please enter a commit message.";

    /** repo base path. */
    private final Path repoBasePath;
    private final Path commitBasePath;
    private final Path blobBasePath;
    private final Path stagingAreaPath;
    private final Path headCommitPath;
    private StagingArea stagingArea;
    private String headCommitId = null;

    private Gitlet() {
        repoBasePath = Paths.get(DEFAULT_REPO_BASE_NAME).toAbsolutePath();
        commitBasePath = repoBasePath.resolve(COMMIT_BASE_NAME);
        blobBasePath = repoBasePath.resolve(BLOB_BASE_NAME);
        stagingAreaPath = repoBasePath.resolve(STAGING_AREA_NAME);
        headCommitPath = repoBasePath.resolve("head");
    }

    /**
     * Init gitlet repository.
     * Usage: java gitlet.Main init
     * Description:
     *  Creates a new Gitlet version-control system in the current directory.
     *  This system will automatically start with one commit: a commit that
     *  contains no files and has the commit message initial commit (just like
     *  that, with no punctuation). It will have a single branch: master,
     *  which initially points to this initial commit, and master will be the
     *  current branch. The timestamp for this initial commit will be
     *  00:00:00 UTC, Thursday, 1 January 1970 in whatever format you choose
     *  for dates (this is called "The (Unix) Epoch", represented internally
     *  by the time 0.) Since the initial commit in all repositories created
     *  by Gitlet will have exactly the same content, it follows that all
     *  repositories will automatically share this commit (they will all have
     *  the same UID) and all commits in all repositories will trace back to
     *  it.
     * Failure cases:
     *  If there is already a Gitlet version-control system in the current
     *  directory, it should abort. It should NOT overwrite the existing
     *  system with a new one. Should print the error message A Gitlet
     *  version-control system already exists in the current directory.
     */
    public static void init() {
        Gitlet gitlet = new Gitlet();
        File repoBaseDir = gitlet.repoBasePath.toFile();
        if (repoBaseDir.exists()) {
            throw Utils.error(ERROR_REPO_EXISTS);
        }
        repoBaseDir.mkdir();
        gitlet.blobBasePath.toFile().mkdir();
        gitlet.commitBasePath.toFile().mkdir();
        gitlet.headCommitId = null;
        gitlet.stagingArea = new StagingArea();
        gitlet.commit("initial commit");
        gitlet.saveHead();
    }

    private void saveHead() {
        Utils.writeContents(headCommitPath.toFile(), headCommitId);
    }

    /**
     * Add a given file into staging area.
     * Usage: java gitlet.Main add [file name].
     * Description:
     *  Adds a copy of the file as it currently exists to the staging area
     *  (see the description of the commit command). For this reason, adding
     *  a file is also called staging the file for addition. Staging an
     *  already-staged file overwrites the previous entry in the staging area
     *  with the new contents. The staging area should be somewhere in .gitlet.
     *  If the current working version of the file is identical to the version
     *  in the current commit, do not stage it to be added, and remove it from
     *  the staging area if it is already there (as can happen when a file is
     *  changed, added, and then changed back). The file will no longer be
     *  staged for removal (see gitlet rm), if it was at the time of the
     *  command.
     * @param filename
     */
    public void add(String filename) {
        File fileToAdd = Paths.get(filename).toFile();
        if (!fileToAdd.exists()) {
            throw Utils.error("File does not exist.");
        }
        byte[] fileContent = Utils.readContents(fileToAdd);
        String blobKey = Utils.sha1(fileContent);
        File blobFile = getBlobFile(blobKey);
        if (!blobFile.exists()) {
            Utils.writeContents(blobFile, fileContent);
        }
        stagingArea.add(filename, blobKey);
        saveStagingArea();
    }

    private void saveStagingArea() {
        Utils.writeObject(stagingAreaPath.toFile(), stagingArea);
    }

    private File getBlobFile(String blobKey) {
        return repoBasePath.resolve(BLOB_BASE_NAME).resolve(blobKey).toFile();
    }

    public static Gitlet load() {
        Gitlet gitlet = new Gitlet();
        if (!gitlet.repoBasePath.toFile().exists()) {
            throw Utils.error("Not in an initialized Gitlet directory.");
        }
        try {
            gitlet.stagingArea = Utils.readObject(gitlet.stagingAreaPath.toFile(), StagingArea.class);
            gitlet.headCommitId = Utils.readContentsAsString(gitlet.headCommitPath.toFile());
            return gitlet;
        } catch (Exception e) {
            throw Utils.error("internal error");
        }
    }

    /**
     * Commit a change.
     * Usage: java gitlet.Main commit [message]
     * Description:
     *  Saves a snapshot of tracked files in the current commit and staging
     *  area so they can be restored at a later time, creating a new commit.
     *  The commit is said to be tracking the saved files. By default, each
     *  commit's snapshot of files will be exactly the same as its parent
     *  commit's snapshot of files; it will keep versions of files exactly as
     *  they are, and not update them. A commit will only update the contents
     *  of files it is tracking that have been staged for addition at the time
     *  of commit, in which case the commit will now include the version of the
     *  file that was staged instead of the version it got from its parent. A
     *  commit will save and start tracking any files that were staged for
     *  addition but weren't tracked by its parent. Finally, files tracked in
     *  the current commit may be untracked in the new commit as a result
     *  being staged for removal by the rm command (below).
     *  The bottom line: By default a commit is the same as its parent.
     *  Files staged for addition and removal are the updates to the commit.
     *  Of course, the date (and likely the message) will also be different
     *  from the parent.
     * @param commitMessage
     */
    public void commit(String commitMessage) {
        Commit headCommit = null;
        if (headCommitId != null) {
            headCommit = loadCommit(headCommitId);
        }
        Commit commit = new Commit(commitMessage, headCommitId, headCommit, stagingArea);
        String commitId = Commit.getId(commit);
        File commitFile = getCommitFile(commitId);
        Utils.writeObject(commitFile, commit);
        loadCommit(commitId);
        stagingArea = new StagingArea();
        saveStagingArea();
        headCommitId = commitId;
        saveHead();
    }

    private File getCommitFile(String commitId) {
        return commitBasePath.resolve(commitId).toFile();
    }

    /**
     * Show the log of commits.
     * Usage: java gitlet.Main log
     * Description:
     *  Starting at the current head commit, display information about each
     *  commit backwards along the commit tree until the initial commit,
     *  following the first parent commit links, ignoring any second parents
     *  found in merge commits. (In regular Git, this is what you get with git
     *  log --first-parent). This set of commit nodes is called the commit's
     *  history. For every node in this history, the information it should
     *  display is the commit id, the time the commit was made, and the commit
     *  message. Here is an example of the exact format it should follow:
     *
     *    ===
     *    commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
     *    Date: Thu Nov 9 20:00:05 2017 -0800
     *    A commit message.
     *
     *    ===
     *    commit 3e8bf1d794ca2e9ef8a4007275acf3751c7170ff
     *    Date: Thu Nov 9 17:01:33 2017 -0800
     *    Another commit message.
     *
     *    ===
     *    commit e881c9575d180a215d1a636545b8fd9abfb1d2bb
     *    Date: Wed Dec 31 16:00:00 1969 -0800
     *    initial commit
     *
     * There is a === before each commit and an empty line after it. As in
     * real Git, each entry displays the unique SHA-1 id of the commit object.
     * The timestamps displayed in the commits reflect the current timezone,
     * not UTC; as a result, the timestamp for the initial commit does not
     * read Thursday, January 1st, 1970, 00:00:00, but rather the equivalent
     * Pacific Standard Time. Display commits with the most recent at the top.
     * By the way, you'll find that the Java classes java.util.Date and
     * java.util.Formatter are useful for getting and formatting times.
     * Look into them instead of trying to construct it manually yourself!
     *
     * For merge commits (those that have two parent commits), add a line just
     * below the first, as in
     *
     *    ===
     *    commit 3e8bf1d794ca2e9ef8a4007275acf3751c7170ff
     *    Merge: 4975af1 2c1ead1
     *    Date: Sat Nov 11 12:30:00 2017 -0800
     *    Merged development into master.
     *
     * where the two hexadecimal numerals following "Merge:" consist of the
     * first seven digits of the first and second parents' commit ids, in that
     * order. The first parent is the branch you were on when you did the
     * merge; the second is that of the merged-in branch. This is as in
     * regular Git.
     */
    public void log() {
        String currentCommitId = headCommitId;
        while (currentCommitId != null) {
            Commit currentCommit = loadCommit(currentCommitId);
            System.out.println(currentCommit);
            currentCommitId = currentCommit.getParent();
        }
    }

    private Commit loadCommit(String commitId) {
        Commit commit = Utils.readObject(getCommitFile(commitId), Commit.class);
        assert(Commit.getId(commit).equals(commitId));
        return commit;
    }

    public void checkout(String commitId, String filename) {
        if (commitId == null) {
            commitId = headCommitId;
        }
        Commit currentCommit = loadCommit(commitId);
        Map<String, String> files = currentCommit.getFiles();
        Utils.writeContents(Paths.get(filename).toFile(), Utils.readContents(getBlobFile(files.get(filename))));
    }
}
