package gitlet;

import java.io.File;

public class Gitlet {

    /** Repository base folder name. */
    public static final String REPO_BASE_FOLDER_NAME = ".gitlet";
    public static final String ERROR_REPO_EXISTS
            = "Gitlet version-control system already exists in the current directory.";

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
        File repo = new File(REPO_BASE_FOLDER_NAME);
        if (repo.exists()) {
            throw Utils.error(ERROR_REPO_EXISTS);
        }
        repo.mkdir();
    }
}
