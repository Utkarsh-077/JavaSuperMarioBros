package br.ol.smb.entity;

import br.ol.smb.entity.enemy.Enemy;
import br.ol.smb.infra.Camera;
import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import static br.ol.smb.infra.Game.GameState.*;
import br.ol.smb.infra.Sound;
import br.ol.smb.infra.Tile;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Tilemap class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Tilemap extends Entity {
    
    private Camera camera;
    private Mario mario;
    private BrickPushing block;
    private final List<Tile> tilesTmp = new ArrayList<Tile>();
    
    public Tilemap(Game game) {
        super(game);
        setUnremovable(true);
    }

    @Override
    public void start() {
        setzOrder(1);
        camera = game.getEntity(Camera.class);
        mario = game.getEntity(Mario.class);
        block = game.getEntity(BrickPushing.class);
    }
    
    @Override
    public void updatePlaying() {
        Rectangle playerCollider = mario.getCollider();
        map.retrieveTiles(playerCollider, tilesTmp);
        for (Tile tile : tilesTmp) {
            if (tile.isCollectable() 
                    && playerCollider.intersects(tile.getCollider())) {
                
                tile.onCollectedByPlayer();
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        drawWithCulling(g);
    }
    
    public void drawWithCulling(final Graphics2D g) {
        map.retrieveTiles(camera.getViewArea(), tilesTmp);        
        for (Tile tile : tilesTmp) {
            if (tile.isCheckpoint()) {
                game.setLastCheckpoint(tile);
            }
            checkNewEnemies(tile);
            if (tile.getId() != 0) {
                tile.draw(g);
            }
        }
    }
    
    private void checkNewEnemies(Tile tile) {
        if (tile.hasEnemies()) {
            Enemy[] enemies = tile.retrieveEnemies();
            for (Enemy enemy : enemies) {
                game.addEntity(enemy);
            }
        }
    }
    
    @Override
    public void drawDebug(Graphics2D g) {
        drawDebugWithCulling(g);
    }
    
    public void drawDebugWithCulling(final Graphics2D g) {
        map.retrieveTiles(camera.getViewArea(), tilesTmp);        
        for (Tile tile : tilesTmp) {
            tile.drawDebug(g);
        }
    }

    private final List<Entity> entitiesTmp = new ArrayList<Entity>();
    private final Rectangle blockArea = new Rectangle();
    
    public void applyImpactToTile(int col, int row, ImpactStrength strength) {
        Tile targetTile = map.getTileByCell(col, row);
        targetTile.onImpact();
        
        if (targetTile.isPushable() || targetTile.isDestroyable()) {
            targetTile = map.getTileByCell(col, row);
            
            block.spawn(col, row);
            blockArea.setBounds(block.getCollider());
            blockArea.y -= 2;

            // destroy tile
            if (targetTile.getItemsCount() == 0 && targetTile.isDestroyable()
                    && strength == ImpactStrength.STRONG) {
                
                destroyTile(targetTile);
                block.setVisible(false);
                Sound.play("break");
            }
            else {
                Sound.play("bump");
            }
            
            // entities
            game.retrieveEntities(blockArea, entitiesTmp);
            for (Entity entity : entitiesTmp) {
                if (blockArea.intersects(entity.getCollider())) {
                    entity.onImpactFromGround(mario.getImpactStrength());
                }
            }
            // tile
            Tile tile = map.getTileByCell(col, row - 1);
            tile.onImpactFromGround(mario.getImpactStrength());
        }
    }

    public void destroyTile(Tile tile) {
        int col = tile.getCol();
        int row = tile.getRow();
        for (int part = 0; part < 4; part++) {
            game.addEntity(new BrickDestroyed(game, col, row, part));
        }
        map.replaceTile(tile, 0);
    }
    
    @Override
    public void onGameStateChanged(Game.GameState newGameState) {
        if (!isVisible() && newGameState == TITLE) {
            setVisible(true);
        }
    }
    
}
