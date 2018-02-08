package br.ol.smb.entity;

import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import static br.ol.smb.infra.Game.GameState.*;
import br.ol.smb.infra.Time;
import java.awt.Color;

/**
 * Initializer class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Initializer extends Entity {

    private FadeEffect fadeEffect;
    
    public Initializer(Game game) {
        super(game);
        game.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void start() {
        fadeEffect = game.getEntity(FadeEffect.class);
        fadeEffect.setIntensity(1);
        fadeEffect.fadeIn();
    }
    
    private int ip = 0;
    private double waitTime;
    
    @Override
    protected void updateInitializing() {
        //game.setGameState(TITLE);
        //if (1 == 1) return;
                
        switch (ip) {
            case 0:
                if (Time.getFixedFrames() > 180) {
                    fadeEffect.fadeOut();
                    ip = 1;
                }
                return;
            case 1:
                if (!fadeEffect.isFinished()) {
                    return;
                }
                waitTime = Time.getCurrentTime();
                ip = 2;
            case 2:
                if (Time.getCurrentTime() - waitTime < 1) {
                    return;
                }
                game.setGameState(OL_PRESENTS);
                setDestroyed(true);
        }
    }
    
}
