package br.ol.smb.entity;

import br.ol.smb.infra.Display;
import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
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
 * Screen class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Screen extends Entity {
    
    private BufferedImage image;
    private double startShowTime;
    
    public Screen(Game game) {
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
        setzOrder(99);
    }

    @Override
    protected void fixedUpdateLivesPresentation() {
        updateScreen();
    }

    @Override
    protected void fixedUpdateLevelCleared() {
        updateScreen();
    }

    @Override
    protected void fixedUpdateTimeUp() {
        updateScreen();
    }
    
    @Override
    protected void fixedUpdateGameOver() {
        updateScreen();
    }

    @Override
    protected void fixedUpdateGameCleared() {
        updateScreen();
    }
    
    private void updateScreen() {
        if (Time.getCurrentTime() - startShowTime < 4) {
            return;
        }
        switch (game.getGameState()) {
            case LIVES_PRESENTATION:
                game.playWorld();
                break;
            //case LEVEL_CLEARED:
            //    break;
            case TIME_UP:
                game.tryNextLife();
                break;
            case GAME_OVER:
                game.returnToTitle();
                break;
            case GAME_CLEARED:
                break;
        }        
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Display.SCREEN_WIDTH, Display.SCREEN_HEIGHT);
        
        // g.drawImage(image, 0, 0, null);
        
        switch (game.getGameState()) {
            case LIVES_PRESENTATION:
                bitmapFont.drawText(g, "WORLD " + game.getWorld(), 11, 9);
                bitmapFont.drawText(g, "*  " + game.getLiveStr(), 15, 13);
                spriteSheet.drawTileByCell(g, 4505, 6, 6);
                break;
            //case LEVEL_CLEARED:
            //    break;
            case TIME_UP:
                bitmapFont.drawText(g, "TIME UP", 12, 15);
                break;
            case GAME_OVER:
                bitmapFont.drawText(g, "GAME OVER", 11, 15);
                break;
            case GAME_CLEARED:
                bitmapFont.drawText(g, "GAME CLEARED", 10, 15);
                break;
        }
    }
    
    @Override
    public void onGameStateChanged(Game.GameState newGameState) {
        setVisible(false);
        switch (newGameState) {
            case LIVES_PRESENTATION:
            //case LEVEL_CLEARED:
            case TIME_UP:
            case GAME_OVER:
            case GAME_CLEARED:
                setVisible(true);
                startShowTime = Time.getCurrentTime();
        }
    }
    
}
