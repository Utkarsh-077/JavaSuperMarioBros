package br.ol.smb.entity;

import br.ol.smb.entity.enemy.Enemy;
import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Time;
import java.awt.Graphics2D;

/**
 * Fireball class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Fireball extends Actor {
    
    private boolean free = true;
    private double direction;
    private int[] fireAnimation = { 5943, 5944, 6057, 6058 };
    private Explosion explosion;
    
    public Fireball(Game game) {
        super(game);
        explosion = new Explosion(game);
    }

    @Override
    public void start() {
        super.start();
        setColliderSize(8, 8);
        spriteHeight = 8;
        tileId = 6057;
        restitution = 0.98;
        setzOrder(4);
        setVisible(true);
    }

    public boolean isFree() {
        return free;
    }

    public void spawn(Mario mario) {
        double wx = mario.getPosition().getX();
        double wy = mario.getPosition().getY() - mario.getCollider().getHeight() / 2;
        direction = mario.getLastDirection() == LastDirection.RIGHT ? 3 : -3;
        velocity.set(direction, -2);
        position.set(wx + 3 * direction, wy);
        setVisible(true);
        free = false;
        destroyed = false;
        game.addEntity(this);
    }

    @Override
    protected void updateMovement() {
        super.updateMovement();
        if (isOutOfView()) {
            setDestroyed(true);
            free = true;
        }
    }
    
    @Override
    public void onHorizontalEntityCollision(Entity other) {
        if (other instanceof Enemy) {
            Enemy enemy = (Enemy) other;
            if (!enemy.isDead()) {
                enemy.killedByFireball();
                explosion.spawn(position.getX(), position.getY());
                setDestroyed(true);
            }
        }
    }

    @Override
    public void onVerticalEntityCollision(Entity other) {
        if (other instanceof Enemy) {
            Enemy enemy = (Enemy) other;
            if (!enemy.isDead()) {
                enemy.killedByFireball();
                explosion.spawn(position.getX(), position.getY());
                setDestroyed(true);
            }
        }
    }

    @Override
    public void onHorizontalTerrainCollision() {
        explosion.spawn(position.getX(), position.getY());
        setDestroyed(true);
        free = true;
    }

    @Override
    public void onVerticalTerrainCollision() {
    }

    @Override
    public void draw(Graphics2D g) {
        int currentFrame = (int) (30 * Time.getCurrentTime());
        currentFrame = currentFrame % fireAnimation.length;
        currentFrame = fireAnimation[currentFrame];
        
        int wx = (int) (position.getX() - getWidth() / 2);
        int wy = (int) (position.getY() - getHeight());
        spriteSheet.drawTileByWorld(g, currentFrame, wx, wy, 8);
    }
    
}
