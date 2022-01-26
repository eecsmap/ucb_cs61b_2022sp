package blocks;

/** A type of InputSource that receives commands from a GUI.
 *  @author P. N. Hilfinger
 */
class GUISource implements CommandSource {

    /** Provides input from SOURCE. */
    GUISource(GUI source) {
        _source = source;
    }

    @Override
    public String getCommand() {
        return _source.readCommand().toUpperCase();
    }

    /** Input source. */
    private GUI _source;

}
