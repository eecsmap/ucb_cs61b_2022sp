package blocks;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;


/** A playable sound clip taken from (typically) a .wav file.
 *  @author P. N. Hilfinger */
class SoundEffect {

    /** A SoundEffect taken from resource named SOURCE. */
    SoundEffect(String source) {
        try {
            _clip = AudioSystem.getClip();
            InputStream sourceStream = getClass().getResourceAsStream(source);
            ByteArrayInputStream sourceByteStream =
                new ByteArrayInputStream(sourceStream.readAllBytes());
            sourceStream.close();
            AudioInputStream audioStream =
                AudioSystem.getAudioInputStream(sourceByteStream);
            _clip.open(audioStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException
                 | IOException | NullPointerException
                 | IllegalArgumentException excp) {
            _clip = null;
        }
    }

    /** Play this clip (if valid).  Wait for it to end. */
    void play() {
        if (_clip != null) {
            _clip.stop();
            _clip.setFramePosition(0);
            _clip.start();
        }
    }

    /** The sound clip containing this effect. */
    private Clip _clip;
}
