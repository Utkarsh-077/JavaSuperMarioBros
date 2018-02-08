package br.ol.smb.entity.item;

import static br.ol.smb.entity.Mario.Transformation.*;
import br.ol.smb.infra.Map;
import br.ol.smb.infra.Sound;

/**
 * Mushroom class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Mushroom extends Item {

    private double direction = 1;

    @Override
    public void start() {
        super.start();
        tileId = 971;
        setActorState(ActorState.APPEARING);
        double wx = tile.getCol() * Map.TILE_SIZE + getWidth() / 2;
        double wy = tile.getRow() * Map.TILE_SIZE + getHeight() / 2;
        position.set(wx, wy);
        gravityScale = 0.5;
    }

    @Override
    protected void updateMovement() {
        velocity.setX(direction);
        if (isOutOfView()) {
            setDestroyed(true);
        }
    }    

    @Override
    public void onImpactFromGround(ImpactStrength strength) {
        velocity.setY(-6);
        direction *= -1;
    }
    
    @Override
    protected void onGetByMario() {
        game.spawnPoint(1000, position.getX(), position.getY() - 1.5 * Map.TILE_SIZE);
        mario.transform(SUPER);
        setDestroyed(true);
    }
    
    @Override
    public void onHorizontalTerrainCollision() {
        direction *= -1;
    }    

    @Override
    protected void onActorStateChanged(ActorState newState) {
        if (newState == ActorState.APPEARING) {
            Sound.play("vine");
        }
    }
    
}
