package br.ol.smb.entity;

import br.ol.smb.infra.Camera;
import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Map;
import br.ol.smb.infra.Tile;
import br.ol.smb.infra.Time;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Actor class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Actor extends Entity {

    public static enum ActorState { APPEARING, ALIVE, DYING, DEAD };
    public static enum LastDirection { LEFT, RIGHT };
    private ActorState actorState = ActorState.ALIVE;
    protected double restitution = 0;
    protected boolean affectedByGravity = true;
    protected double gravityScale = 1;
    protected Camera camera;
    protected Tilemap tilemap;
    protected int minX;
    protected double dyingTime;
    protected LastDirection lastDirection = LastDirection.RIGHT;
    protected boolean flipSpriteAccordingToDirection = true;
    public Actor() {
    }
    
    public Actor(Game game) {
        super(game);
    }

    public LastDirection getLastDirection() {
        return lastDirection;
    }

    public ActorState getActorState() {
        return actorState;
    }

    public void setActorState(ActorState actor) {
        if (this.actorState != actor) {
            this.actorState = actor;
            onActorStateChanged(actor);
        }
    }

    public boolean isAffectedByGravity() {
        return affectedByGravity;
    }

    @Override
    public void start() {
        camera = game.getEntity(Camera.class);
        tilemap = game.getEntity(Tilemap.class);
    }
    
    @Override
    public void fixedUpdatePlaying() {
        switch (actorState) {
            case APPEARING: updateAppearing(); break;
            case ALIVE: updateAlive(); break;
            case DYING: updateDying(); break;
            case DEAD: updateDead(); break;
        }
        updateAnimation();
        updateAnimationFrame();
    }

    protected void updateAppearing() {
        if (isCollidingWithTerrain()) {
            double vy = -12 * Time.getFixedDeltaTime();
            position.translate(0, vy);
        }
        else {
            setActorState(ActorState.ALIVE);
            setzOrder(2);
        }
    }

    protected final List<Entity> entitiesTmp = new ArrayList<Entity>();
    protected final Point cellPositionTmp = new Point();
    
    protected void updateAlive() {
        updateMovement();
        updatePhysics();
    }
    
    protected void updatePhysics() {
        if (affectedByGravity && !isCollidingWithFloor()) {
            double gx = gravityScale * Game.GRAVITY.getX();
            double gy = gravityScale * Game.GRAVITY.getY();
            velocity.translate(gx, gy);
        }
        
        double vx = velocity.getX();
        position.translate(vx, 0);

        // limit min x
        if (position.getX() < minX) {
            position.setX(minX);
            velocity.setX(0);
        }
        
        if (rigid) {
            checkHorizontalCollision(vx);
        }
        
        double vy = velocity.getY();
        position.translate(0, vy);
        if (rigid) {
            checkVerticalCollision(vy);
        }
        
        ensureStop();
        checkLastDirection();
        flipSpriteAccordingToDirection();
    }
    
    protected void ensureStop() {
        if (Math.abs(velocity.getX()) < 0.01) {
            velocity.setX(0);
        }
        if (Math.abs(velocity.getY()) < 0.01) {
            velocity.setY(0);
        }
    }
    
    protected void checkLastDirection() {
        if (velocity.getX() < 0) {
            lastDirection = LastDirection.LEFT;
        }
        else if (velocity.getX() > 0) {
            lastDirection = LastDirection.RIGHT;
        }
    }
    
    protected void flipSpriteAccordingToDirection() {
        if (flipSpriteAccordingToDirection) {
            flipSpriteHorizontal = lastDirection == LastDirection.LEFT;
        }
    }
    
    protected void updateAnimation() {
    }
    
    private void checkHorizontalCollision(double vx) {
        boolean collidedWithTerrain = false;
        
        // correct position horizontally
        while (isCollidingWithTerrain()) {
            position.translate(vx > 0 ? -1 : 1, 0);
            velocity.setX(0);
            collidedWithTerrain = true;
        }
        
        if (collidedWithTerrain) {
            onHorizontalTerrainCollision();
        }        

        game.retrieveEntities(getCollider(), entitiesTmp);
        for (Entity entity : entitiesTmp) {
            if (entity != this) {
                entity.onHorizontalEntityCollision(this);
            }
        }
    }

    private void checkVerticalCollision(double vy) {
        leftCornerAdjust();
        rightCornerAdjust();
        
        boolean collidedWithTerrain = false;
        
        // correct position vertically
        while (isCollidingWithTerrain()) {
            position.translate(0, vy > 0 ? -1 : 1);
            velocity.scale(1, -restitution);
            collidedWithTerrain = true;
        }
        
        if (collidedWithTerrain) {
            checkImpactToTileWhenJumping();
            onVerticalTerrainCollision();
        }

        game.retrieveEntities(getCollider(), entitiesTmp);
        for (Entity entity : entitiesTmp) {
            if (entity != this) {
                entity.onVerticalEntityCollision(this);
            }
        }
    }
    
    protected void updateMovement() {
    }
    
    protected void updateDying() {
        if (affectedByGravity) {
            double gx = gravityScale * Game.GRAVITY.getX();
            double gy = gravityScale * Game.GRAVITY.getY();
            velocity.translate(gx, gy);
        }
        position.add(velocity);
        if (isOutOfView()) {
            setActorState(ActorState.DEAD);
        }
    }

    protected void updateDead() {
    }

    protected void checkImpactToTileWhenJumping() {
    }

    protected void leftCornerAdjust() {
    }

    protected void rightCornerAdjust() {
    }
    
    protected boolean canMoveLeft() {
        int hw = getWidth() / 2;
        int l = (int) (position.getX() - hw);
        int t = (int) (position.getY() - getHeight() - 2);
        int m = (int) (position.getY() - getHeight() / 2 - 2);
        int b = (int) (position.getY() - 2);
        return !(map.getTileByWorld(l - 1, t).isRigid()
            || map.getTileByWorld(l - 1, m).isRigid()
            || map.getTileByWorld(l - 1, b).isRigid());
    }
    
    protected boolean canMoveRight() {
        int hw = getWidth() / 2;
        int r = (int) (position.getX() + hw - 1);
        int t = (int) (position.getY() - getHeight() - 2);
        int m = (int) (position.getY() - getHeight() / 2 - 2);
        int b = (int) (position.getY() - 2);
        return !(map.getTileByWorld(r + 1, t).isRigid()
            || map.getTileByWorld(r + 1, m).isRigid()
            || map.getTileByWorld(r + 1, b).isRigid());
    }
    
    protected boolean isCollidingWithFloor() {
        int hw = getWidth() / 2;
        int wlx = (int) (position.getX() - hw);
        int wrx = (int) (position.getX() + hw - 1);
        int wy = (int) (position.getY() - 1);
        return map.getTileByWorld(wlx, wy).isRigid()
            || map.getTileByWorld(wrx, wy).isRigid();
    }

    protected boolean isCollidingWithCeil() {
        int hw = getWidth() / 2;
        int wlx = (int) (position.getX() - hw);
        int wrx = (int) (position.getX() + hw - 1);
        int wy = (int) (position.getY() - getHeight() - 3);
        return map.getTileByWorld(wlx, wy).isRigid()
            || map.getTileByWorld(wrx, wy).isRigid();
    } 
    
    protected boolean isCollidingWithTerrain() {
        int hw = getWidth() / 2;
        int l = (int) (position.getX() - hw);
        int r = (int) (position.getX() + hw - 1);
        int t = (int) (position.getY() - getHeight() - 2);
        int m = (int) (position.getY() - getHeight() / 2 - 2);
        int b = (int) (position.getY() - 2);
        return map.getTileByWorld(l, t).isRigid()
            || map.getTileByWorld(l, m).isRigid()
            || map.getTileByWorld(l, b).isRigid()
            || map.getTileByWorld(r, t).isRigid()
            || map.getTileByWorld(r, m).isRigid()
            || map.getTileByWorld(r, b).isRigid();
    }
    
    protected boolean isOutOfView() {
        return !camera.getCollider().intersects(getCollider());
    }
    
    public void applyDamage() {
    }

    public void kill() {
        if (isDead()) {
            return;
        }
        setActorState(ActorState.DYING);
        dyingTime = Time.getCurrentTime();
        rigid = false;
    }
    
    public boolean isDead() {
        return actorState != ActorState.ALIVE;
    }

    protected void onActorStateChanged(ActorState newState) {
    }
    
}
