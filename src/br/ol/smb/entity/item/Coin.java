package br.ol.smb.entity.item;

import br.ol.smb.infra.Map;
import br.ol.smb.infra.Sound;

/**
 * Coin class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Coin extends Item {

    @Override
    public void start() {
        super.start(); 
        tileId = 1369;
        setAnimation(1369, 1370, 1371, 1372);
        rigid = false;
        double wx = tile.getCol() * Map.TILE_SIZE + getWidth() / 2;
        double wy = (tile.getRow() - 1) * Map.TILE_SIZE + getHeight() / 2;
        position.set(wx, wy);
        velocity.set(0, -8.25);
        animationTimeScale = 30;
        setzOrder(2);
        Sound.play("coin");
    }

    @Override
    protected void updateMovement() {
        double wy = (tile.getRow() - 1.5) * Map.TILE_SIZE;
        if (!isDestroyed() && velocity.getY() > 0 && position.getY() >= wy) {
            game.addCoins(1);
            game.spawnPoint(200, position.getX(), position.getY());
            setDestroyed(true);
        }
    }
    
}
