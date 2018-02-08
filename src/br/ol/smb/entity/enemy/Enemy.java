package br.ol.smb.entity.enemy;

import br.ol.smb.entity.Actor;
import br.ol.smb.entity.Mario;
import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Map;
import br.ol.smb.infra.Sound;
import br.ol.smb.infra.Time;
import java.awt.Color;

/**
 * Enemy class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Enemy extends Actor {
    
    protected Mario mario;
    protected double direction = -0.5;
    protected boolean insideView = false;
    
    protected int killPoint = 100;
    protected boolean deadByGround;
    protected boolean dangerous = true;
    protected boolean canKillOtherEnemies = false;
    
    public Enemy() {
    }
    
    public void set(Game game, int wx, int wy) {
        position.set(wx, wy);
        debugColor = Color.RED;
        setGame(game);
    }

    @Override
    public void start() {
        super.start();
        mario = game.getEntity(Mario.class);
        setzOrder(3);
        tileId = 2225;
        setAnimation(2225, 2226);
        animationTimeScale = 5;
        setVisible(true);
    }
    
    @Override
    protected void updateMovement() {
        velocity.setX(direction);
        if (!insideView && camera.getViewArea().contains(getCollider())) {
            insideView = true;
        }
        else if (insideView && isOutOfView()) {
            setDestroyed(true);
        }
    }    
    
    @Override
    protected void flipSpriteAccordingToDirection() {
        if (flipSpriteAccordingToDirection) {
            flipSpriteHorizontal = lastDirection == LastDirection.RIGHT;
        }
    }
    
    @Override
    protected void updateDying() {
        if (deadByGround) {
            updatePhysics();
        }
        else if (Time.getCurrentTime() - dyingTime > 2) {
            setDestroyed(true);
        }

        if (isOutOfView()) {
            setDestroyed(true);
        }
    }

    @Override
    protected void updateDead() {
        setDestroyed(true);
    }
    
    @Override
    public void onImpactFromGround(ImpactStrength strength) {
        if (strength == ImpactStrength.WEAK) {
            velocity.setY(-6);
            direction *= -1;
        }
        else if (strength == ImpactStrength.STRONG) {
            killedFromGround();
        }
    }

    @Override
    public void onHorizontalEntityCollision(Entity otherEntity) {
        if (otherEntity instanceof Mario && !mario.isDead() && !isDead()) {
            if (mario.isInvincibleMario()) {
                killedByInvencibleMario();
            }
            else if (dangerous && !mario.isImmortal()) {
                mario.applyDamage();
            }
        }
        
        if (otherEntity instanceof Enemy) {
            Enemy enemy = (Enemy) otherEntity;
            if (isDead() || enemy.isDead()) {
                return;
            }
            if (enemy.canKillOtherEnemies) {
                killedByOtherEnemy();
            }
            else if (canKillOtherEnemies) {
                enemy.killedByOtherEnemy();
            }
            else  {
                while (enemy.getCollider().intersects(getCollider())) {
                    enemy.getPosition().translate(direction, 0);
                    getPosition().translate(-direction, 0);
                }
                direction *= -1;
                enemy.direction *= -1;
            }
        }
    }

    @Override
    public void onVerticalEntityCollision(Entity otherEntity) {
        if (otherEntity instanceof Mario && !isDead()) {
            if (!mario.isDead() && mario.getVelocity().getY() > 0) {
                onStampedByMario();
            }
        }
        else if (canKillOtherEnemies && otherEntity instanceof Enemy && !isDead()) {
            Enemy enemy = (Enemy) otherEntity;
            if (!enemy.isDead()) {
                enemy.killedByOtherEnemy();
            }
        }
    }

    @Override
    public void onHorizontalTerrainCollision() {
        direction *= -1;
    }    

    protected void onStampedByMario() {
        killedByMarioStamp();
        mario.onEnemyKilledByStump();
        Sound.play("squish");
    }
    
    protected void killedFromGround() {
        deadByGround = true;
        rigid = false;
        flipSpriteVertical = true;
        velocity.setY(-7);
        kill();
    }

    protected void killedByOtherEnemy() {
        killedFromGround();
        Sound.play("kick");
    }
    
    protected void killedByInvencibleMario() {
        killedFromGround();
        Sound.play("kick");
    }

    public void killedByFireball() {
        killedFromGround();
        Sound.play("kick");
    }
    
    protected void killedByMarioStamp() {
        deadByGround = false;
        kill();
    }

    @Override
    public void kill() {
        super.kill();
        game.spawnPoint(killPoint, position.getX(), position.getY() - Map.TILE_SIZE);
        setzOrder(2);
    }

}
