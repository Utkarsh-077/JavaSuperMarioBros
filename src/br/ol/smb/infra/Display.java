package br.ol.smb.infra;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Display class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Display extends Canvas {

    public static final int SCREEN_WIDTH = 256;
    public static final int SCREEN_HEIGHT = 240;
    private BufferedImage backbuffer;
    private Graphics2D bg;
    private final Vec2 scale = new Vec2();
    private BufferStrategy bs;
    private boolean running;
    private Game game;
            
    public Display() {
        int imageType = BufferedImage.TYPE_INT_RGB;
        backbuffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, imageType);
        bg = (Graphics2D) backbuffer.getGraphics();
        game = new Game();
        addKeyListener(new Input());
    }

    public void start() {
        double sx = (double) getWidth() / SCREEN_WIDTH;
        double sy = (double) getHeight() / SCREEN_HEIGHT;
        scale.set(sx, sy);
        createBufferStrategy(2);
        bs = getBufferStrategy();
        running = true;
        new Thread(new MainLoop()).start();
    }
    
    private class MainLoop implements Runnable {

        @Override
        public void run() {
            Time.start();
            game.start();
            while (running) {
                Time.update();
                while (Time.needsFixedUpdate()) {
                    game.fixedUpdate();
                }
                game.update();
                game.lateUpdate();
                game.addRemovePendingEntities();
                // rendering
                bg.setBackground(game.getBackgroundColor());
                bg.clearRect(0, 0, getWidth(), getHeight());
                game.draw(bg);
                game.drawDebug(bg);
                Graphics2D g = (Graphics2D) bs.getDrawGraphics();
                g.scale(scale.getX(), scale.getY());
                g.drawImage(backbuffer, 0, 0, null);
                g.dispose();
                bs.show();
                
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
        }
        
    }
    
}
