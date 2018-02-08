package br.ol.smb.entity;

import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import static br.ol.smb.infra.Game.GameState.*;
import br.ol.smb.infra.Time;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * HUD class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class HUD extends Entity {
    
    private BufferedImage image;
    private int[] coinAnimation = { 6159, 6159, 6159, 6159, 6160, 6161, 6160 };
    private String timeLeft = "";
    
    public HUD(Game game) {
        super(game);
        coordinate = Coordinate.SCREEN_SPACE;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/graphic/screen_model.png"));
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        setUnremovable(true);
    }

    @Override
    public void start() {
        setzOrder(100);
    }
    
    @Override
    public void fixedUpdatePlaying() {
        timeLeft = game.getTimeLeftStr();
    }

    @Override
    protected void fixedUpdateLevelCleared() {
        timeLeft = game.getTimeLeftStr();
    }

    @Override
    public void draw(Graphics2D g) {
        // g.drawImage(image, 0, 0, null);
        bitmapFont.drawText(g, "MARIO", 3, 1);
        bitmapFont.drawText(g, "WORLD", 18, 1);
        bitmapFont.drawText(g, "TIME", 25, 1);
        bitmapFont.drawText(g, game.getScoreStr(), 3, 2);
        bitmapFont.drawText(g, "*" + game.getCoinsStr(), 12, 2);
        bitmapFont.drawText(g, game.getWorld(), 19, 2);
        bitmapFont.drawText(g, timeLeft, 26, 2);
        
        // animated coin
        int currentFrame = (int) (10 * Time.getCurrentTime());
        currentFrame = currentFrame % coinAnimation.length;
        currentFrame = coinAnimation[currentFrame];
        spriteSheet.drawTileByCell(g, currentFrame, 2, 11, 8);
    }
    
    @Override
    public void onGameStateChanged(Game.GameState newGameState) {
        switch (newGameState) {
            case TITLE:
            case LIVES_PRESENTATION:
            // case LEVEL_CLEARED:
            case TIME_UP:
            case GAME_OVER:
            case GAME_CLEARED:
                timeLeft = "";
                setVisible(true);
        }
    }
    
}
