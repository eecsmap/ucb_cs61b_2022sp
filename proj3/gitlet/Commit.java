package gitlet;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class Commit implements Dumpable {

    private String parentCommitId;
    private String logMessage;
    private long commitTime;
    private TreeMap<String, String> files;

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
