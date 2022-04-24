package gitlet;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Gitlet {

    public static final String ERROR_REPO_EXISTS
            = "Gitlet version-control system already exists in the current directory.";
    public static final String ERROR_INCORRECT_OPERANDS = "Incorrect operands.";
    public static final String ERROR_NO_COMMIT_MESSAGE = "Please enter a commit message.";

    private final Path repoBasePath;
    private final Path commitBasePath;
    private final Path blobBasePath;
    private final Path stagingAreaPath;
    private final Path headCommitPath;
    private StagingArea stagingArea;
    private String headCommitId;

    private Gitlet() {
        repoBasePath = Paths.get(".gitlet").toAbsolutePath();
        commitBasePath = repoBasePath.resolve("commits");
        blobBasePath = repoBasePath.resolve("blobs");
        stagingAreaPath = repoBasePath.resolve("staging");
        headCommitPath = repoBasePath.resolve("head");
    }

    public static void init() {
        Gitlet gitlet = new Gitlet();
        File repoBaseDir = gitlet.repoBasePath.toFile();
        if (repoBaseDir.exists()) {
            throw Utils.error(ERROR_REPO_EXISTS);
        }
        repoBaseDir.mkdir();
        gitlet.blobBasePath.toFile().mkdir();
        gitlet.commitBasePath.toFile().mkdir();
        gitlet.stagingArea = new StagingArea();
        gitlet.commit("initial commit");
    }

    private void saveHead() {
        Utils.writeContents(headCommitPath.toFile(), headCommitId);
    }

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
        return blobBasePath.resolve(blobKey).toFile();
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

    public void commit(String commitMessage) {
        Commit headCommit = null;
        if (headCommitId != null) {
            headCommit = loadCommit(headCommitId);
        }
        Commit commit = new Commit(commitMessage, headCommitId, headCommit, stagingArea);
        String commitId = Commit.getId(commit);
        File commitFile = getCommitFile(commitId);
        Utils.writeObject(commitFile, commit);
        stagingArea = new StagingArea();
        saveStagingArea();
        headCommitId = commitId;
        saveHead();
    }

    private File getCommitFile(String commitId) {
        return commitBasePath.resolve(commitId).toFile();
    }

    public void log() {
        String commitId = headCommitId;
        while (commitId != null) {
            Commit currentCommit = loadCommit(commitId);
            System.out.println(currentCommit);
            commitId = currentCommit.getParent();
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
        Commit commit = loadCommit(commitId);
        Map<String, String> files = commit.getFiles();
        Utils.writeContents(Paths.get(filename).toFile(), Utils.readContents(getBlobFile(files.get(filename))));
    }
}
