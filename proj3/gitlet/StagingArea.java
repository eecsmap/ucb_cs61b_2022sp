package gitlet;

import java.util.HashMap;
import java.util.Map;

public class StagingArea implements Dumpable {

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

    @Override
    public void dump() {
        System.out.println("files to add:");
        for (String filename : filesToAdd.keySet()) {
            System.out.println(filename + ": " + filesToAdd.get(filename));
        }
    }
}
