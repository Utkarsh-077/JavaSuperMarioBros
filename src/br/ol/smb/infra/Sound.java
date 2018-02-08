package br.ol.smb.infra;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * Sound class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Sound implements Runnable {
    
    private static Thread soundThread;
    private static final List<Clip> pool = new ArrayList<Clip>();
            
    public static void main(String[] args) throws Exception {
        for (int i = 0;i < 100; i++) {
            play("coin");
            Thread.sleep(100);
        }
        
//        for (int i = 0; i < 1000; i++) {
//            try {
//                Clip clip = null;
//                InputStream bis = new BufferedInputStream(Sound.class.getResourceAsStream("/res/sound/fireball.wav"));
//                AudioInputStream inputStream = AudioSystem.getAudioInputStream(bis);
//                // AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url));
//                DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
//                clip = (Clip)AudioSystem.getLine(info);
//                clip.open(inputStream);
//                clip.setFramePosition(0);
//                clip.start();
//                
//                Thread.sleep(100);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
    }

    private static final java.util.Map<String, AudioInputStream> audios = new HashMap<String, AudioInputStream>();
    private static final java.util.Map<String, Clip> clips;

    static {
        clips = Collections.synchronizedMap(new HashMap<String, Clip>());
    }
    public static void play(String sound) {
        try {
            if (soundThread == null) {
                soundThread = new Thread(new Sound());
                soundThread.start();
            }
            Clip clip = clips.get(sound);
            if (clip == null) {
                String resource = "/res/sound/" + sound + ".wav";
                InputStream is = Sound.class.getResourceAsStream(resource);
                InputStream bis = new BufferedInputStream(is);
                AudioInputStream audio = AudioSystem.getAudioInputStream(bis);
                audios.put(sound, audio);
                DataLine.Info info = new DataLine.Info(Clip.class, audio.getFormat());
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(audio);
                clips.put(sound, clip);
            }
            synchronized (pool) {
                pool.add(clip);
                pool.notifyAll();
            }
        } catch (Exception e) {
            System.out.println(e);
        }        
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (pool) {
                    if (!pool.isEmpty()) {
                        Clip clip = pool.remove(pool.size() - 1);
                        clip.stop();
                        clip.setFramePosition(0);
                        clip.start();
                    }
                    else {
                        pool.wait();
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            //Thread.yield();
        }
    }

}
