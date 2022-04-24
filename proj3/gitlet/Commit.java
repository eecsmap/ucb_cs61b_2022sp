package gitlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Commit.
 */
public class Commit implements Dumpable {

    /** Parent reference. */
    private String parentCommitId = null;
    /** Log message. */
    private String logMessage = null;
    /** Commit time. */
    private long commitTime = 0;

    /** Tracked files. */
    private Map<String, String> files;

    public Commit(String message, String parentCommitId, Commit parentCommit, StagingArea stagingArea) {
        this.files = new HashMap<>();
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
        // this is a hack since the serialized object once read back will have a different hash
        ByteArrayInputStream in = new ByteArrayInputStream(Utils.serialize(commit));
        try {
            ObjectInputStream is = new ObjectInputStream(in);
            return Utils.sha1(Utils.serialize((Commit) is.readObject()));
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
        return null;
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
                .ofPattern(DATE_PATTERN);
        return ZonedDateTime.ofInstant(instantTime, ZoneId.systemDefault())
                .format(formatter);
    }

    /** Date format pattern used to generate the required date string. */
    public static final String DATE_PATTERN = "EEE MMM dd HH:mm:ss yyyy Z";

    public Map<String, String> getFiles() {
        return files;
    }

    @Override
    public void dump() {
        System.out.println("parent commit id: " + parentCommitId);
        System.out.println("log message: " + logMessage);
        System.out.println("commit time: " + commitTime);
        System.out.println("files:");
        for (String filename : files.keySet()) {
            System.out.println(filename + ": " + files.get(filename));
        }
        System.out.println("commit id: " + Commit.getId(this));
    }
}
