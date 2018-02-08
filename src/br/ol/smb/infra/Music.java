package br.ol.smb.infra;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * Music class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Music {
    
    private static Sequencer sequencer;

    static {
        try {
            // MidiDevice device = MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[0]);
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static Map<String, Sequence> sequencesCache = new HashMap<String, Sequence>();
    
    public static void play(String music) {
        try {
            Sequence sequence = sequencesCache.get(music);
            if (sequence == null) {
                String resource = "/res/music/world_" + music + ".mid";
                InputStream is = Music.class.getResourceAsStream(resource);
                sequence = MidiSystem.getSequence(is);
                sequencesCache.put(music, sequence);
            }
            sequencer.stop();
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(999999);
            sequencer.start();
        } catch (Exception e) {
            System.out.println(e);
        }        
    }

    public static void stop() {
        if (sequencer != null && sequencer.isRunning()) {
            sequencer.stop();
            sequencer.setTickPosition(0);
        }
    }

    public static void main(String[] args) throws Exception {
        Music.play("invincible");
    }

}
