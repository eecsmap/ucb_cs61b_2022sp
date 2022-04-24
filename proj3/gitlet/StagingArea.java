package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StagingArea implements Serializable {

    private Map<String, String> filesToAdd;

    public StagingArea() {
        filesToAdd = new HashMap<>();
    }

    public void add(String filename, String blobKey) {
        filesToAdd.put(filename, blobKey);
    }

    public Map<String, String> getFilesToAdd() {
        return filesToAdd;
    }
}
