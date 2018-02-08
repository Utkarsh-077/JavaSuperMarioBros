package br.ol.smb.entity;

import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Time;
import java.awt.Graphics2D;

/**
 * Explosion class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Explosion extends Entity {
    
    private int[] explosionAnimation = { 1491, 1548, 1605, 1548, 1491 };
    private double currentFrame;
    
    public Explosion(Game game) {
        super(game);
    }

    @Override
    public void start() {
        super.start();
        rigid = false;
        tileId = 1491;
        setAnimation(1491, 1548, 1605);
        setzOrder(4);
        setVisible(true);
    }

    public void spawn(double wx, double wy) {
        position.set(wx, wy);
        setVisible(true);
        destroyed = false;
        currentFrame = 0;
        game.addEntity(this);
    }

    @Override
    public void fixedUpdate() {
        currentFrame += 20 * Time.getFixedDeltaTime();
        if ((int) currentFrame > explosionAnimation.length - 1) {
            setDestroyed(true);
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        int spriteId = explosionAnimation[(int) currentFrame];
        int wx = (int) (position.getX() - getWidth() / 2);
        int wy = (int) (position.getY() - getHeight() / 2);
        spriteSheet.drawTileByWorld(g, spriteId, wx, wy);
    }
    
}
