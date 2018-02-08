package br.ol.smb.entity.item;

import static br.ol.smb.entity.Mario.Transformation.*;
import br.ol.smb.infra.Map;
import br.ol.smb.infra.Sound;

/**
 * Flower class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Flower extends Item {

    @Override
    public void start() {
        super.start();
        tileId = 1085;
        setAnimation(1085, 1086, 1087, 1088);
        setActorState(ActorState.APPEARING);
        double wx = tile.getCol() * Map.TILE_SIZE + getWidth() / 2;
        double wy = tile.getRow() * Map.TILE_SIZE + getHeight() / 2;
        position.set(wx, wy);
    }
    
    @Override
    public void onImpactFromGround(ImpactStrength strength) {
        velocity.setY(-6);
    }

    @Override
    protected void onGetByMario() {
        game.spawnPoint(1000, position.getX(), position.getY() - Map.TILE_SIZE);
        mario.transform(FIRE);
        setDestroyed(true);
    }

    @Override
    protected void onActorStateChanged(ActorState newState) {
        if (newState == ActorState.APPEARING) {
            Sound.play("vine");
        }
    }
    
}
