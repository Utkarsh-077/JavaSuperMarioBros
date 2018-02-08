package br.ol.smb.infra;

import br.ol.smb.entity.Mario;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Camera class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Camera extends Entity {
    
    private Mario mario;
    private double minX;

    public Camera(Game scene) {
        super(scene);
        setUnremovable(true);
    }

    @Override
    public void start() {
        mario = game.getEntity(Mario.class);
        tileId = 0;
        reset();
    }

    private void reset() {
        position.set(Display.SCREEN_WIDTH * 0.5, Display.SCREEN_HEIGHT * 0.5);
        minX = getWidth() / 2;
    }
    
    @Override
    protected int getWidth() {
        return Display.SCREEN_WIDTH;
    }

    @Override
    protected int getHeight() {
        return  Display.SCREEN_HEIGHT;
    }

    @Override
    public Rectangle getCollider() {
        int cw = getWidth();
        int ch = getHeight();
        int cx = (int) (position.getX() - cw / 2);
        int cy = (int) (position.getY() - ch / 2);
        collider.setBounds(cx, cy, cw, ch);
        return collider;
    }
    
    public Rectangle getViewArea() {
        return getCollider();
    }

    @Override
    public void updatePlaying() {
        updateFollowMario();
    }

    @Override
    protected void updateLevelCleared() {
        updateFollowMario();
    }

    private void updateFollowMario() {
        if (position.getX() > minX) {
            minX = position.getX();
        }
        double cx = position.getX();
        cx += (mario.getPosition().getX() - position.getX()) * 0.1;
        cx = MathUtil.clamp(cx, minX, map.getWidth() - getWidth() / 2);
        position.setX(cx);
    }
    
    @Override
    public void draw(Graphics2D g) {
    }

    @Override
    public void drawDebug(Graphics2D g) {
    }

    @Override
    public void onGameStateChanged(Game.GameState newGameState) {
        switch (newGameState) {
            case TITLE:
                reset();
                break;
            case START_GAME:
            case START_NEXT_LIFE:
                position.setX((5 + game.getLastCheckpoint().getCol()) * Map.TILE_SIZE);
                minX = getWidth() / 2;
                break;
        }
    }    
    
}
