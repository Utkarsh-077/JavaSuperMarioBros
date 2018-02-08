package br.ol.smb.entity;

import br.ol.smb.infra.Display;
import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Game.GameState;
import static br.ol.smb.infra.Game.GameState.*;
import br.ol.smb.infra.Input;
import br.ol.smb.infra.Map;
import br.ol.smb.infra.Time;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Title class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Title extends Entity {
    
    private FadeEffect fadeEffect;
    private BufferedImage titleImage;
    private String textPressSpaceToStart = "PRESS SPACE TO START";
    private String textCredit1 = "@1985 NINTENDO";
    private String textCredit2 = "@2018 PROGRAMMED BY O.L.";
    
    public Title(Game game) {
        super(game);
        setUnremovable(true);
    }

    @Override
    public void start() {
        try {
            String res = "/res/graphic/title.png";
            titleImage = ImageIO.read(getClass().getResourceAsStream(res));
        } catch (IOException ex) {
            Logger.getLogger(Title.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        fadeEffect = game.getEntity(FadeEffect.class);
        coordinate = Coordinate.SCREEN_SPACE;
        position.set(Display.SCREEN_WIDTH / 2, titleImage.getHeight() / 2 + 2 * Map.TILE_SIZE);
        setzOrder(900);
    }

    private int ip = 0;
    private double waitTime;
    
    @Override
    protected void fixedUpdateTitle() {
        switch (ip) {
            case 0:
                fadeEffect.fadeOut();
                ip = 1;
            case 1:
                if (!fadeEffect.isFinished()) {
                    return;
                }
                ip = 2;
            case 2:
                if (Input.isKeyPressed(KeyEvent.VK_SPACE)) {
                    ip = 3;
                }
                return;
            case 3:
                game.startGame();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        int wx = (int) (getPosition().getX() - titleImage.getWidth() / 2);
        int wy = (int) (getPosition().getY() - titleImage.getHeight() / 2);
        g.drawImage(titleImage, wx, wy, null);
        
        boolean blink = (Time.getFixedFrames() % 20) < 13;
        if (blink) {
            bitmapFont.drawText(g, textPressSpaceToStart, 6, 18);
        }
        bitmapFont.drawText(g, "TOP- " + game.getHiscoreStr(), 10, 20);
        bitmapFont.drawText(g, textCredit1, 13, 15);
        bitmapFont.drawText(g, textCredit2, 4, 23);
    }

    
    @Override
    public void onGameStateChanged(GameState newGameState) {
        if (newGameState == TITLE) {
            ip = 0;
            map.load("1-1");
            setVisible(true);
        }
        else {
            setVisible(false);
        }
    }
    
}
