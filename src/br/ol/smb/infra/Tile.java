package br.ol.smb.infra;

import br.ol.smb.entity.enemy.Enemy;
import br.ol.smb.entity.item.Item;
import br.ol.smb.infra.Entity.ImpactStrength;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Tile class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Tile {
    
    protected static final Rectangle collider = new Rectangle();
    
    protected Game game;
    protected SpriteSheet spriteSheet;
    protected Map map;
    protected Color debugColor = Color.BLACK;
    
    protected int id;
    protected int col;
    protected int row;
    protected int colliderX;
    protected int colliderY;

    protected boolean rigid;
    protected boolean pushable;
    protected boolean destroyable;
    protected boolean collectable;
    
    protected int itemsCount;
    protected Item[] items;
    
    protected Enemy[] enemies;
    
    protected int[] animation;

    protected boolean checkpoint;
    
    public Tile(Game game) {
        this.game = game;
        this.spriteSheet = game.getSpriteSheet();
        this.map = game.getMap();
    }
    
    private static final Point pointTmp = new Point();
    
    public Tile(Game game, int id, int col, int row, int colliderX, int colliderY, Color debugColor) {
        this(game);
        this.id = id;
        this.col = col;
        this.row = row;
        this.colliderX = colliderX;
        this.colliderY = colliderY;
        this.rigid = map.getRigidTiles().contains(id);
        this.pushable = map.getPushableTiles().contains(id);
        this.destroyable = map.getDestroyableTiles().contains(id);
        this.collectable = map.getCollectableTiles().contains(id);
        this.animation = map.getAnimations().get(id);
        this.debugColor = debugColor;
    }

    public void setItems(Item[] items) {
        this.items = items;
        itemsCount = items.length;
    }

    public int getItemsCount() {
        return itemsCount;
    }
    
    public void nextItem() {
        if (itemsCount > 0) {
            itemsCount--;
        }
    }

    public boolean hasEnemies() {
        return enemies != null;
    }
    
    public Enemy[] retrieveEnemies() {
        Enemy[] enemiesTmp = enemies;
        enemies = null;
        return enemiesTmp;
    }

    public void setEnemies(Enemy[] enemies) {
        this.enemies = enemies;
    }
    
    public Game getGame() {
        return game;
    }

    public Color getDebugColor() {
        return debugColor;
    }

    public void setDebugColor(Color debugColor) {
        this.debugColor = debugColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void convertToWorldPosition(Vec2 position) {
        double wx = col * Map.TILE_SIZE;
        double wy = row * Map.TILE_SIZE;
        position.set(wx, wy);
    }
    
    public int getColliderX() {
        return colliderX;
    }

    public void setColliderX(int colliderX) {
        this.colliderX = colliderX;
    }

    public int getColliderY() {
        return colliderY;
    }

    public void setColliderY(int colliderY) {
        this.colliderY = colliderY;
    }

    public Rectangle getCollider() {
        int x = col * Map.TILE_SIZE + colliderX;
        int y = row * Map.TILE_SIZE + colliderY;
        int w = Map.TILE_SIZE - 2 * colliderX;
        int h = Map.TILE_SIZE - 2 * colliderY;
        collider.setBounds(x, y, w, h);
        return collider;
    }

    public boolean isRigid() {
        return rigid;
    }

    public void setRigid(boolean rigid) {
        this.rigid = rigid;
    }

    public boolean isPushable() {
        return pushable;
    }

    public void setPushable(boolean pushable) {
        this.pushable = pushable;
    }

    public boolean isDestroyable() {
        return destroyable;
    }

    public void setDestroyable(boolean destroyable) {
        this.destroyable = destroyable;
    }

    public boolean isCollectable() {
        return collectable;
    }

    public void setCollectable(boolean collectable) {
        this.collectable = collectable;
    }
    
    public int[] getAnimation() {
        return animation;
    }

    public void setAnimation(int[] animation) {
        this.animation = animation;
    }

    public boolean isCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(boolean checkpoint) {
        this.checkpoint = checkpoint;
    }
    
    public void start() {
    }
    
    public void destroy() {
        Tile tile = new Tile(game, 0, col, row, 0, 0, null);
        map.setTileByCell(col, row, tile);
    }
    
    public void draw(Graphics2D g) {
        if (animation == null) {
            spriteSheet.drawTileByCell(g, id, row, col);
        }
        else {
            int currentFrame = (int) (10 * Time.getCurrentTime());
            currentFrame = currentFrame % animation.length;
            currentFrame = animation[currentFrame];
            spriteSheet.drawTileByCell(g, currentFrame, row, col);
        }
    }
    
    public void drawDebug(Graphics2D g) {
        if (debugColor == null) {
            return;
        }
        g.setColor(debugColor);
        g.draw(getCollider());
    }
    
    public void onImpact() {
        if (itemsCount > 0) {
            boolean lastObject = itemsCount == 1;
            items[itemsCount - 1].onImpact(lastObject);
        }
    }
    
    public void onImpactFromGround(ImpactStrength strength) {
        if (itemsCount > 0) {
            items[itemsCount - 1].onImpactFromGround(strength);
        }
    }
    
    public void onCollectedByPlayer() {
        if (itemsCount > 0) {
            items[itemsCount - 1].onCollectedByPlayer();
        }
    }
    
}
