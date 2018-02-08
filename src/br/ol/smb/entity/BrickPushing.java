package br.ol.smb.entity;

import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Map;
import br.ol.smb.infra.Tile;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * BrickPushing class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BrickPushing extends Entity {
    
    private Tile tile;
    private double positionY;
    
    public BrickPushing(Game game) {
        super(game);
        setUnremovable(true);
    }

    @Override
    public void start() {
        setzOrder(3);
        debugColor = Color.RED;
    }
    
    @Override
    public void fixedUpdatePlaying() {
        if (!visible) {
            return;
        }
        velocity.add(Game.GRAVITY);
        position.add(velocity);
        if (position.getY() >= positionY && velocity.getY() > 0) {
            position.setY(positionY);
            visible = false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (tile != null) {
            int wx = tile.getCol() * Map.TILE_SIZE;
            int wy = tile.getRow() * Map.TILE_SIZE;
            g.setColor(game.getBackgroundColor());
            g.fillRect(wx, wy, Map.TILE_SIZE, Map.TILE_SIZE);
        }
        super.draw(g);
    }
    
    public void spawn(int col, int row) {
        tile = map.getTileByCell(col, row);
        tileId = tile.getId();
        positionY = row * 16 + 8;
        position.set(col * 16 + 8, positionY);
        velocity.set(0, -3);
        visible = true;
    }

    @Override
    public void onGameStateChanged(Game.GameState newGameState) {
        visible = false;
    }
    
}
