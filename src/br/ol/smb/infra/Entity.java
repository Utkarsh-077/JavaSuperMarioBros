package br.ol.smb.infra;

import br.ol.smb.infra.Game.GameState;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Entity class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Entity implements Comparable<Entity> {

    public static enum Coordinate { SCREEN_SPACE, WORLD_SPACE };
    public static enum ImpactStrength { WEAK, STRONG };
    protected Game game;
    protected boolean unremovable = false;
    protected BitmapFont bitmapFont;
    protected SpriteSheet spriteSheet;
    protected Map map;
    protected boolean visible = false;
    protected boolean rigid = true;
    protected boolean destroyed = false;
    protected Coordinate coordinate = Coordinate.WORLD_SPACE;
    protected final Vec2 position = new Vec2();
    protected final Vec2 velocity = new Vec2();
    protected final Rectangle collider = new Rectangle(16, 16);
    protected Color debugColor = Color.BLACK;
    protected int tileId;
    protected double animationFrame;
    protected int[] animation;
    protected double animationTimeScale = 1;
    protected int spriteHeight = 16;
    protected boolean flipSpriteHorizontal = false;
    protected boolean flipSpriteVertical = false;
    private int zOrder;
    
    public Entity() {
    }

    public Entity(Game game) {
        setGamePrivate(game);
    }

    public Game getGame() {
        return game;
    }

    protected void setGame(Game game) {
        setGamePrivate(game);
    }

    private void setGamePrivate(Game game) {
        this.game = game;
        this.bitmapFont = game.getBitmapFont();
        this.spriteSheet = game.getSpriteSheet();
        this.map = game.getMap();
    }

    boolean isUnremovable() {
        return unremovable;
    }

    protected void setUnremovable(boolean unremovable) {
        this.unremovable = unremovable;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isRigid() {
        return rigid;
    }

    public void setRigid(boolean rigid) {
        this.rigid = rigid;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
        System.out.println("Entity " + getClass().getName() + " destroyed !");
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
    
    public Vec2 getPosition() {
        return position;
    }

    public Vec2 getVelocity() {
        return velocity;
    }
    
    public Rectangle getCollider() {
        int cx = (int) (position.getX() - getWidth() / 2);
        int cy = (int) (position.getY() - getHeight());
        collider.setLocation(cx, cy);
        return collider;
    }

    protected void setColliderSize(int width, int height) {
        collider.setSize(width, height);
    }
    
    protected int getWidth() {
        return collider.width;
    }

    protected int getHeight() {
        return collider.height;
    }
    
    protected void setAnimation(int ... tileIds) {
        animation = tileIds;
    }

    public int getzOrder() {
        return zOrder;
    }

    public void setzOrder(int zOrder) {
        if (this.zOrder != zOrder) {
            this.zOrder = zOrder;
            game.setzOrderChanged(true);
        }
    }
    
    public void start() {
    }
    
    public void fixedUpdate() {
        switch (game.getGameState()) {
            case INITIALIZING: fixedUpdateInitializing(); break;
            case OL_PRESENTS: fixedUpdateOLPresents(); break;
            case TITLE: fixedUpdateTitle(); break;
            case START_GAME: fixedUpdateStartGame(); break;
            case START_NEXT_LIFE: fixedUpdateStartGame(); break;
            case LIVES_PRESENTATION: fixedUpdateLivesPresentation(); break;
            case PLAYING: fixedUpdatePlaying(); break;
            case MARIO_TRANSFORMING: fixedUpdateMarioTransforming(); break;
            case LEVEL_CLEARED: fixedUpdateLevelCleared(); break;
            case TIME_UP: fixedUpdateTimeUp(); break;
            case GAME_OVER: fixedUpdateGameOver(); break;
            case GAME_CLEARED: fixedUpdateGameCleared(); break;        
        }
    }
    
    protected void fixedUpdateInitializing() { 
    }
    
    protected void fixedUpdateOLPresents() { 
    }
    
    protected void fixedUpdateTitle() { 
    }

    protected void fixedUpdateStartGame() { 
    }

    protected void fixedUpdateStartNextLife() { 
    }
    
    protected void fixedUpdateLivesPresentation() { 
    }
    
    protected void fixedUpdatePlaying() { 
    }
    
    protected void fixedUpdateMarioTransforming() { 
    }
    
    protected void fixedUpdateLevelCleared() { 
    }
    
    protected void fixedUpdateTimeUp() { 
    }
    
    protected void fixedUpdateGameOver() { 
    }
    
    protected void fixedUpdateGameCleared() { 
    }
  
    public void update() {
        switch (game.getGameState()) {
            case INITIALIZING: updateInitializing(); break;
            case OL_PRESENTS: updateOLPresents(); break;
            case TITLE: updateTitle(); break;
            case START_GAME: updateStartGame(); break;
            case START_NEXT_LIFE: updateStartNextLife(); break;
            case LIVES_PRESENTATION: updateLivesPresentation(); break;
            case PLAYING: updatePlaying(); break;
            case MARIO_TRANSFORMING: updateMarioTransforming(); break;
            case LEVEL_CLEARED: updateLevelCleared(); break;
            case TIME_UP: updateTimeUp(); break;
            case GAME_OVER: updateGameOver(); break;
            case GAME_CLEARED: updateGameCleared(); break;        
        }
    }

    protected void updateInitializing() { 
    }
    
    protected void updateOLPresents() { 
    }
    
    protected void updateTitle() { 
    }

    protected void updateStartGame() { 
    }

    protected void updateStartNextLife() { 
    }
    
    protected void updateLivesPresentation() { 
    }
    
    protected void updatePlaying() { 
    }
    
    protected void updateMarioTransforming() { 
    }
    
    protected void updateLevelCleared() { 
    }
    
    protected void updateTimeUp() { 
    }
    
    protected void updateGameOver() { 
    }
    
    protected void updateGameCleared() { 
    }
    
    public void lateUpdate() {
        switch (game.getGameState()) {
            case INITIALIZING: lateUpdateInitializing(); break;
            case OL_PRESENTS: lateUpdateOLPresents(); break;
            case TITLE: lateUpdateTitle(); break;
            case START_GAME: lateUpdateStartGame(); break;
            case START_NEXT_LIFE: lateUpdateStartNextLife(); break;
            case LIVES_PRESENTATION: lateUpdateLivesPresentation(); break;
            case PLAYING: lateUpdatePlaying(); break;
            case MARIO_TRANSFORMING: lateUpdateMarioTransforming(); break;
            case LEVEL_CLEARED: lateUpdateLevelCleared(); break;
            case TIME_UP: lateUpdateTimeUp(); break;
            case GAME_OVER: lateUpdateGameOver(); break;
            case GAME_CLEARED: lateUpdateGameCleared(); break;        
        }
    }
    
    protected void lateUpdateInitializing() { 
    }
    
    protected void lateUpdateOLPresents() { 
    }
    
    protected void lateUpdateTitle() { 
    }

    protected void lateUpdateStartGame() { 
    }

    protected void lateUpdateStartNextLife() { 
    }
    
    protected void lateUpdateLivesPresentation() { 
    }
    
    protected void lateUpdatePlaying() { 
    }
    
    protected void lateUpdateMarioTransforming() { 
    }
    
    protected void lateUpdateLevelCleared() { 
    }
    
    protected void lateUpdateTimeUp() { 
    }
    
    protected void lateUpdateGameOver() { 
    }
    
    protected void lateUpdateGameCleared() { 
    }
    
    public void onGameStateChanged(GameState newGameState) {
    }
    
    protected void updateAnimationFrame() {
        animationFrame += (animationTimeScale * Time.getFixedDeltaTime());
    }
    
    public void draw(Graphics2D g) {
        int wx = (int) position.getX();
        int wy = (int) position.getY();
        if (animation == null && tileId != 0) {
            spriteSheet.drawEntityByWorld(g, tileId, wx, wy, spriteHeight, flipSpriteHorizontal, flipSpriteVertical);
        }
        else if (animation != null) {
            int currentFrame = (int) animationFrame % animation.length;
            currentFrame = animation[currentFrame];
            spriteSheet.drawEntityByWorld(g, currentFrame, wx, wy, spriteHeight, flipSpriteHorizontal, flipSpriteVertical);
        }
    }

    public void drawDebug(Graphics2D g) {
        g.setColor(debugColor);
        g.draw(getCollider());
    }
    
    public void onHorizontalEntityCollision(Entity other) {
    }

    public void onVerticalEntityCollision(Entity other) {
    }

    public void onHorizontalTerrainCollision() {
    }

    public void onVerticalTerrainCollision() {
    }
    
    public void onImpactFromGround(ImpactStrength strength) {
    } 

    @Override
    public int compareTo(Entity o) {
        return zOrder - o.zOrder;
    }
        
}
