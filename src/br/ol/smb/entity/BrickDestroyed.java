package br.ol.smb.entity;

import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Map;
import br.ol.smb.infra.Tile;
import br.ol.smb.infra.Vec2;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

/**
 * BrickDestroyed class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BrickDestroyed extends Entity {

    private Tile tile;
    private double positionY;
    private double maxPositionY;
    private static final Vec2[] velocities = { new Vec2(-1, -7), new Vec2(1, -7), new Vec2(-1, -3), new Vec2(1, -3) };
    private static final int[][] offsets = { { 0, 0 }, { 8, 0 }, { 0, 8 }, { 8, 8 } };
    private int part;
    private double angle;
    private double angularVelocity;
    
    // part:
    // 0|1
    // -+-
    // 2|3
    public BrickDestroyed(Game game, int col, int row, int part) {
        super(game);
        tile = map.getTileByCell(col, row);
        tileId = tile.getId();
        int positionX = col * Map.TILE_SIZE + offsets[part][0];
        positionY = row * Map.TILE_SIZE + offsets[part][1];
        maxPositionY = positionX + 5 * Map.TILE_SIZE;
        position.set(positionX, positionY);
        velocity.set(velocities[part]);
        this.part = part;
        visible = true;
        angularVelocity = velocity.getX() > 0 ? 0.2 : -0.2;
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
        angle += angularVelocity;
        velocity.add(Game.GRAVITY);
        position.add(velocity);
        if (position.getY() >= maxPositionY && velocity.getY() > 0) {
            setDestroyed(true);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // TODO: think another way draw this without getTransfom() (?)
        AffineTransform at = g.getTransform();
        g.translate((int) position.getX(), (int) position.getY());
        g.rotate(angle);
        g.translate(-4, -4);
        
        int id = tile.getId() - 1;
        int dx1 = 0;
        int dy1 = 0;
        int dx2 = dx1 + 8;
        int dy2 = dy1 + 8;
        int scol = id % spriteSheet.getCols();
        int srow = id / spriteSheet.getCols(); 
        int sx1 = scol * Map.TILE_SIZE + offsets[part][0];
        int sy1 = srow * Map.TILE_SIZE + offsets[part][1];
        int sx2 = sx1 + 8;
        int sy2 = sy1 + 8;
        Image image = spriteSheet.getImage();
        g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        
        g.setTransform(at);
        
//        int id = tile.getId() - 1;
//        int dx1 = (int) position.getX();
//        int dy1 = (int) position.getY();
//        int dx2 = dx1 + 8;
//        int dy2 = dy1 + 8;
//        int scol = id % spriteSheet.getCols();
//        int srow = id / spriteSheet.getCols(); 
//        int sx1 = scol * Map.TILE_SIZE + offsets[part][0];
//        int sy1 = srow * Map.TILE_SIZE + offsets[part][1];
//        int sx2 = sx1 + 8;
//        int sy2 = sy1 + 8;
//        Image image = spriteSheet.getImage();
//        g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }
    
}
