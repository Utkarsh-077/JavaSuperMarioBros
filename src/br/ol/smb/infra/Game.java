package br.ol.smb.infra;

import br.ol.smb.entity.BrickPushing;
import br.ol.smb.entity.FadeEffect;
import br.ol.smb.entity.Flag;
import br.ol.smb.entity.HUD;
import br.ol.smb.entity.Initializer;
import br.ol.smb.entity.Mario;
import br.ol.smb.entity.OLPresents;
import br.ol.smb.entity.Point;
import br.ol.smb.entity.Screen;
import br.ol.smb.entity.Tilemap;
import br.ol.smb.entity.Title;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Game class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Game {

    public static enum GameState { 
        INITIALIZING, OL_PRESENTS, TITLE, START_GAME, START_NEXT_LIFE, LIVES_PRESENTATION
        , PLAYING, MARIO_TRANSFORMING, LEVEL_CLEARED, TIME_UP, GAME_OVER, GAME_CLEARED }
    
    public static final boolean DRAW_DEBUG = false;
    public static final Vec2 GRAVITY = new Vec2(0, 0.5);
    
    private GameState gameState = GameState.INITIALIZING;
    private Color backgroundColor = Color.BLACK;
    private BitmapFont bitmapFont;
    private SpriteSheet spriteSheet;
    private Map map;
    private Camera camera;
    private Flag flag;
    private boolean zOrderChanged;
    
    private List<Entity> entities = new ArrayList<Entity>();
    private List<Entity> entitiesAdd = new ArrayList<Entity>();
    private List<Entity> entitiesRemove = new ArrayList<Entity>();
    
    private Tile lastCheckpoint;
    private int levelClearedCol;
    private int nextLevelEntranceCol;
    
    // --- HUD ---
    
    private int score;
    private int hiscore;
    private int lives;
    private int coins;
    private double timeLimit;
    private double timeLeft;
    private String world;
    private String nextWorld;
    
    public Game() {
        bitmapFont = new BitmapFont();
        spriteSheet = new SpriteSheet();
        spriteSheet.load("sprite_sheet.png");
        map = new Map(this);
    }

    public Tile getLastCheckpoint() {
        return lastCheckpoint;
    }

    public void setLastCheckpoint(Tile lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    public int getLevelClearedCol() {
        return levelClearedCol;
    }

    public void setLevelClearedCol(int levelClearedCol) {
        this.levelClearedCol = levelClearedCol;
    }

    public int getNextLevelEntranceCol() {
        return nextLevelEntranceCol;
    }

    public void setNextLevelEntranceCol(int nextLevelEntranceCol) {
        this.nextLevelEntranceCol = nextLevelEntranceCol;
    }


    // --- HUD ---

    public String getScoreStr() {
        String scoreStr = "000000" + (int) score;
        scoreStr = scoreStr.substring(scoreStr.length() - 6, scoreStr.length());
        return scoreStr;
    }

    public void addScore(int point) {
        this.score += point;
    }

    public void clearScore() {
        this.score = 0;
    }
    
    public String getHiscoreStr() {
        String hiscoreStr = "000000" + (int) hiscore;
        hiscoreStr = hiscoreStr.substring(hiscoreStr.length() - 6, hiscoreStr.length());
        return hiscoreStr;
    }

    public void updateHiscore() {
        if (score > hiscore) {
            hiscore = score;
        }
    }
    
    public String getLiveStr() {
        String livesStr = "0" + (int) lives;
        livesStr = livesStr.substring(livesStr.length() - 1, livesStr.length());
        return livesStr;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void incLives() {
        lives++;
    }

    public void decLives() {
        lives++;
    }
    
    public String getCoinsStr() {
        String coinsStr = "00" + (int) coins;
        coinsStr = coinsStr.substring(coinsStr.length() - 2, coinsStr.length());
        return coinsStr;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void clearCoins() {
        this.coins = 0;
    }

    public double getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(double timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void decTimeLeft() {
        timeLeft -= 1;
    }

    public double getTimeLeft() {
        return timeLeft;
    }

    public String getTimeLeftStr() {
        String timeLeftStr = "000" + (int) timeLeft;
        timeLeftStr = timeLeftStr.substring(timeLeftStr.length() - 3, timeLeftStr.length());
        return timeLeftStr;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
    
    public void updateTimeLeft() {
        timeLeft -= 2.5 * Time.getFixedDeltaTime();
    }
    
    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getNextWorld() {
        return nextWorld;
    }

    public void setNextWorld(String nextWorld) {
        this.nextWorld = nextWorld;
    }

    // --- ---

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        if (this.gameState != gameState) {
            this.gameState = gameState;
            onGameStateChanged(gameState);
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public Map getMap() {
        return map;
    }

    public Flag getFlag() {
        return flag;
    }

    public boolean iszOrderChanged() {
        return zOrderChanged;
    }

    public void setzOrderChanged(boolean zOrderChanged) {
        this.zOrderChanged = zOrderChanged;
    }
    
    private void addEntityLocal(Entity entity) {
        entities.add(entity);
    }

    public void addEntity(Entity entity) {
        entitiesAdd.add(entity);
        entity.start();
    }
    
    public void start() {
        createAllEntities();
        for (Entity entity : entities) {
            entity.start();
        }
        map.start();
    }
    
    private void createAllEntities() {
        addEntityLocal(new Initializer(this));
        addEntityLocal(new OLPresents(this));
        addEntityLocal(new Title(this));
        addEntityLocal(camera = new Camera(this));
        addEntityLocal(flag = new Flag(this));
        addEntityLocal(new Tilemap(this));
        addEntityLocal(new BrickPushing(this));
        addEntityLocal(new Mario(this));
        addEntityLocal(new Screen(this));
        addEntityLocal(new HUD(this));
        addEntityLocal(new FadeEffect(this));
    }
    
    public void fixedUpdate() {
        for (Entity entity : entities) {
            entity.fixedUpdate();
        }
    }
    
    public void update() {
        for (Entity entity : entities) {
            entity.update();
        }
    }
    
    public void lateUpdate() {
        for (Entity entity : entities) {
            entity.lateUpdate();
            if (entity.isDestroyed()) {
                entitiesRemove.add(entity);
            }
        }
    }

    private void onGameStateChanged(GameState newGameState) {
        for (Entity entity : entities) {
            entity.onGameStateChanged(newGameState);
        }
    }
    
    void addRemovePendingEntities() {
        if (!entitiesAdd.isEmpty()) {
            entities.addAll(entitiesAdd);
            Collections.sort(entities);
            entitiesAdd.clear();
        }
        if (!entitiesRemove.isEmpty()) {
            entities.removeAll(entitiesRemove);
            entitiesRemove.clear();
        }
    }
    
    private void checkZOrderChange() {
        if (zOrderChanged) {
            Collections.sort(entities);
            zOrderChanged = false;
        }
    }
    
    public void draw(Graphics2D g) {
        checkZOrderChange();
        int cx = (int) (camera.getPosition().getX() - camera.getWidth() / 2);
        for (Entity entity : entities) {
            if (!entity.isVisible()) {
                continue;
            }
            if (entity.getCoordinate() == Entity.Coordinate.WORLD_SPACE) {
                g.translate(-cx, 0);
            }
            entity.draw(g);
            if (entity.getCoordinate() == Entity.Coordinate.WORLD_SPACE) {
                g.translate(cx, 0);
            }
        }
    }

    public void drawDebug(Graphics2D g) {
        if (!DRAW_DEBUG) {
            return;
        }
        checkZOrderChange();
        int cx = (int) (camera.getPosition().getX() - camera.getWidth() / 2);
        for (Entity entity : entities) {
            if (!entity.isVisible()) {
                continue;
            }
            if (entity.getCoordinate() == Entity.Coordinate.WORLD_SPACE) {
                g.translate(-cx, 0);
            }
            entity.drawDebug(g);
            if (entity.getCoordinate() == Entity.Coordinate.WORLD_SPACE) {
                g.translate(cx, 0);
            }
        }
    }
    
    public <T> T getEntity(Class<T> c) {
        for (Entity entity : entities) {
            if (entity.getClass().equals(c)) {
                return (T) entity;
            }
        }
        return null;
    }
    
    public void retrieveEntities(Rectangle area, List<Entity> resultEntities) {
        resultEntities.clear();
        for (Entity entity : entities) {
            if (!(entity instanceof Camera)
                    && !(entity instanceof Mario)
                    && area.intersects(entity.getCollider())) {
                resultEntities.add(entity);
            }
        }
    }

    public void clearScene() {
        for (Entity entity : entities) {
            if (!entity.isUnremovable()) {
                entity.setDestroyed(true);
            }
        }
        
    }
    
    public void spawnPoint(int point, double wx, double wy) {
        addEntity(new Point(this, point, wx, wy));
        addScore(point);
    }

    // --- game flow ---
    
    public void startGame() {
        lastCheckpoint = null;
        lives = 3;
        world = "1-1";
        map.load(world);
        timeLeft = timeLimit;
        setGameState(GameState.START_GAME);
        Sound.play("1up");
    }

    public void playWorld() {
        setGameState(GameState.PLAYING);
        Music.stop();
        Music.play(world);
    }

    public void tryNextLife() {
        clearScene();
        lives--;
        if (lives > 0) {
            if (timeLeft <= 0) {
                timeLeft = timeLimit;
            }
            map.load(world);
            setGameState(GameState.START_NEXT_LIFE);
        }
        else {
            Music.stop();
            Sound.play("game_over");
            setGameState(GameState.GAME_OVER);
        }
    }

    public void tryNextLifeByTimeLeft() {
        setGameState(GameState.TIME_UP);
    }

    public void returnToTitle() {
        updateHiscore();
        clearScore();
        clearCoins();
        setGameState(GameState.TITLE);
    }

    public void levelCleared() {
        setGameState(GameState.LEVEL_CLEARED);
    }

    public void nextLevel() {
            Music.stop();
            Sound.play("game_over");
            setGameState(GameState.GAME_OVER);
        
//        world = nextWorld;
//        lastCheckpoint = null;
//        map.load(world);
//        Music.stop();
//        Music.play(world);
//        timeLeft = timeLimit;
//        setGameState(GameState.START_NEXT_LIFE);
    }
    
}
