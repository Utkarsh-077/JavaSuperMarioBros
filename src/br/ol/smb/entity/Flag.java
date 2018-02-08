package br.ol.smb.entity;

import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Map;

/**
 * Explosion class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Flag extends Entity {
    
    private boolean show;
    private int col;
    private int row;
    
    public Flag(Game game) {
        super(game);
        setUnremovable(true);
    }

    @Override
    public void start() {
        rigid = false;
        tileId = 979;
        animation = null;
        setzOrder(0);
        setVisible(true);
    }
    
    public void setPositionByCell(int col, int row) {
        this.col = col;
        this.row = row;
        double wx = col * Map.TILE_SIZE + Map.TILE_SIZE / 2;
        double wy = row * Map.TILE_SIZE + Map.TILE_SIZE;
        position.set(wx, wy);
        show = false;
    }

    
    @Override
    public void fixedUpdate() {
        double targetWy = (row - 1) * Map.TILE_SIZE;
        if (!show || position.getY() <= targetWy) {
            return;
        }
        position.translate(0, -1);
    }

    public void show() {
        show = true;
    }
    
}
