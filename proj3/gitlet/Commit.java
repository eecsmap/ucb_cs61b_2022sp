package gitlet;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Commit.
 */
public class Commit implements Dumpable {

    /** Parent reference. */
    private String parentCommitId;
    /** Log message. */
    private String logMessage;
    /** Commit time. */
    private long commitTime;
    /** Tracked files. */
    private TreeMap<String, String> files;

    /**
     * Create a commit.
     * @param message
     * @param parentCommitId
     * @param parentCommit
     * @param stagingArea
     */
    public Commit(String message, String parentCommitId, Commit parentCommit, StagingArea stagingArea) {
        this.files = new TreeMap<>();
        this.parentCommitId = parentCommitId;
        if (parentCommitId != null) {
            assert(parentCommit != null);
            this.files.putAll(parentCommit.files);
        }
        this.files.putAll(stagingArea.getFilesToAdd());
        this.logMessage = message;
        this.commitTime = 0;
    }

    /**
     * Get SHA-1 id of a given commit.
     * Each commit is identified by its SHA-1 id, which must include
     * the file (blob) references of its files, parent reference,
     * log message, and commit time.
     */
    public static String getId(Commit commit) {
        return Utils.sha1(Utils.serialize(commit));
    }

    public String getParent() {
        return parentCommitId;
    }

    public String toString() {
        return "===\n"
                + "commit " + Commit.getId(this) + '\n'
                + "Date: " + getDate() + '\n'
                + logMessage + '\n';
    }

    private String getDate() {
        Instant instantTime = Instant.ofEpochSecond(this.commitTime / 1000);
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("EEE MMM dd HH:mm:ss yyyy Z");
        return ZonedDateTime.ofInstant(instantTime, ZoneId.systemDefault())
                .format(formatter);
    }

    public Map<String, String> getFiles() {
        return files;
    }

    @Override
    public void dump() {
        System.out.println("parent: " + parentCommitId);
        System.out.println("message: " + logMessage);
        System.out.println("commit time: " + commitTime);
        System.out.println("files:");
        for (String filename : files.keySet()) {
            System.out.println(filename + ": " + files.get(filename));
        }
    }
}
