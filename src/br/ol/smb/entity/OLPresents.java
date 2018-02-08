package br.ol.smb.entity;

import br.ol.smb.infra.Display;
import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Game.GameState;
import static br.ol.smb.infra.Game.GameState.*;
import br.ol.smb.infra.Time;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * OLPresents class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class OLPresents extends Entity {
    
    private FadeEffect fadeEffect;
    
    private BufferedImage olpresentsImage;
    private BufferedImage offscreen1Image;
    private BufferedImage offscreen2Image;

    private Graphics2D osig1;
    private Graphics2D osig2;

    private static final double MAX_AMPLITUDE = 30;
    private double amplitude = MAX_AMPLITUDE;
    private double distortionOffset;
    
    public OLPresents(Game game) {
        super(game);
    }

    @Override
    public void start() {
        try {
            String res = "/res/graphic/ol_presents.png";
            olpresentsImage = ImageIO.read(getClass().getResourceAsStream(res));
            int w = olpresentsImage.getWidth();
            int h = olpresentsImage.getHeight();
            offscreen1Image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            offscreen2Image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            osig1 = (Graphics2D) offscreen1Image.getGraphics();
            osig2 = (Graphics2D) offscreen2Image.getGraphics();
            
        } catch (IOException ex) {
            Logger.getLogger(OLPresents.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        fadeEffect = game.getEntity(FadeEffect.class);
        coordinate = Coordinate.SCREEN_SPACE;
    }

    private int ip = 0;
    private double waitTime;
    
    @Override
    protected void fixedUpdateOLPresents() {
        switch (ip) {
            case 0:
                distortionOffset += 2 * Time.getFixedDeltaTime();
                amplitude = amplitude * 0.975;
                if (amplitude < 1) {
                    amplitude = 0;
                    waitTime = Time.getCurrentTime();
                    ip = 1;
                }
                return;
            case 1:
                if (Time.getCurrentTime() - waitTime < 4) {
                    return;
                }
                fadeEffect.fadeIn();
                ip = 2;
            case 2:
                if (!fadeEffect.isFinished()) {
                    return;
                }
                waitTime = Time.getCurrentTime();
                ip = 3;
            case 3:
                if (Time.getCurrentTime() - waitTime < 1) {
                    return;
                }
                game.setGameState(TITLE);
                setDestroyed(true);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        processVerticalDistortion(osig1, olpresentsImage);
        processHorizontalDistortion(osig2, offscreen1Image);
        g.drawImage(offscreen2Image, 0, 0, null);
        drawFadeEffect(g);
    }

    private void drawFadeEffect(Graphics2D g) {
        int alpha = (int) ((amplitude / MAX_AMPLITUDE) * 255);
        g.setColor(new Color(255, 255, 255, alpha));
        g.fillRect(0, 0, Display.SCREEN_WIDTH, Display.SCREEN_HEIGHT);
    }

    public void processVerticalDistortion(Graphics2D g, BufferedImage image) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        for (int x = 0; x < image.getWidth(); x++) {
            int dx1 = x;
            int dy1 = -5 + (int) (amplitude * Math.sin(x * 0.1 + distortionOffset));
            int dx2 = dx1 + 1;
            int dy2 = dy1 + image.getHeight();
            int sx1 = x;
            int sy1 = 0;
            int sx2 = sx1 + 1;
            int sy2 = sy1 + image.getHeight();
            g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        }
    }

    public void processHorizontalDistortion(Graphics2D g, BufferedImage image) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        for (int y = 0; y < image.getHeight(); y++) {
            int dx1 = (int) (amplitude * Math.cos(y * 0.1 + distortionOffset));
            int dy1 = y;
            int dx2 = dx1 + image.getWidth();
            int dy2 = dy1 + 1;
            int sx1 = 0;
            int sy1 = y;
            int sx2 = sx1 + image.getWidth();
            int sy2 = sy1 + 1;
            g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        }
    }
    
    @Override
    public void onGameStateChanged(GameState newGameState) {
        setVisible(newGameState == OL_PRESENTS);
    }
    
}
