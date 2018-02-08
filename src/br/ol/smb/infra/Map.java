package br.ol.smb.infra;

import br.ol.smb.entity.enemy.Enemy;
import br.ol.smb.entity.item.Item;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Map class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Map {
    
    public static final int TILE_SIZE = 16;
    public Tile EMPTY_TILE;
    private Game game;
    private int cols;
    private int rows;
    private Tile[][] tiles;
    
    private final Set<Integer> rigidTiles = new HashSet<Integer>();
    private final Set<Integer> pushableTiles = new HashSet<Integer>();
    private final Set<Integer> destroyableTiles = new HashSet<Integer>();
    private final Set<Integer> collectableTiles = new HashSet<Integer>();
    private final java.util.Map<Integer, int[]> animations = new HashMap<Integer, int[]>();

    public Map(Game game) {
        this.game = game;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getWidth() {
        return cols * TILE_SIZE;
    }

    public int getHeight() {
        return rows * TILE_SIZE;
    }
    
    public Tile[][] getTiles() {
        return tiles;
    }

    public Set<Integer> getRigidTiles() {
        return rigidTiles;
    }

    public Set<Integer> getPushableTiles() {
        return pushableTiles;
    }

    public Set<Integer> getDestroyableTiles() {
        return destroyableTiles;
    }

    public Set<Integer> getCollectableTiles() {
        return collectableTiles;
    }

    public java.util.Map<Integer, int[]> getAnimations() {
        return animations;
    }

    public void load(String level) {
        try {
            String resource = "/res/map/world_" + level + ".map";
            InputStream is = getClass().getResourceAsStream(resource);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;

            // game
            game.setWorld(level);
            parseTimeLimit(br.readLine());
            parseBackgroundColor(br.readLine());
            parseNextWorld(br.readLine());
            parseLevelClearedCol(br.readLine());
            parseNextLevelEntranceCol(br.readLine());
            parseFlagPosition(br.readLine());
            
            // tiles properties
            rigidTiles.clear();
            pushableTiles.clear();
            collectableTiles.clear();
            destroyableTiles.clear();
            while (!"#animations".equals(line = br.readLine())) {
                parseLineTileProperties(line);
            }

            // animations
            animations.clear();
            while (!"#map".equals(line = br.readLine())) {
                parseLineAnimations(line);
            }

            // map
            cols = Integer.parseInt(br.readLine());
            rows = Integer.parseInt(br.readLine());
            tiles = new Tile[rows][cols];
            for (int row = 0; row < rows; row++) {
                parseLineTiles(row, br.readLine());
            }
            while (!"#items".equals(br.readLine())) {}
            
            // objects
            while (!"#enemies".equals(line = br.readLine())) {
                parseLineItem(line);
            }

            // enemies
            while (!"#checkpoints".equals(line = br.readLine())) {
                parseLineEnemy(line);
            }

            // checkpoints
            while (!"#end".equals(line = br.readLine())) {
                parseLineCheckpoint(line);
            }
            
            br.close();
            System.gc();
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    private void parseTimeLimit(String line) {
        line = line.replace(" ", "");
        if (line.startsWith("timeLimit=")) {
            line = line.replace("timeLimit=", "");
            int timeLimit = (int) Integer.parseInt(line);
            game.setTimeLimit(timeLimit);
        }
    }

    private void parseBackgroundColor(String line) {
        line = line.replace(" ", "");
        if (line.startsWith("background=")) {
            line = line.replace("background=", "");
            int rgb = (int) Integer.parseInt(line);
            game.setBackgroundColor(new Color(rgb));
        }
    }

    private void parseNextWorld(String line) {
        line = line.replace(" ", "");
        if (line.startsWith("nextWorld=")) {
            line = line.replace("nextWorld=", "");
            game.setNextWorld(line);
        }
    }

    private void parseLevelClearedCol(String line) {
        line = line.replace(" ", "");
        if (line.startsWith("levelClearedCol=")) {
            line = line.replace("levelClearedCol=", "");
            int levelClearedCol = (int) Integer.parseInt(line);
            game.setLevelClearedCol(levelClearedCol);
        }
    }

    private void parseNextLevelEntranceCol(String line) {
        line = line.replace(" ", "");
        if (line.startsWith("nextLevelEntranceCol=")) {
            line = line.replace("nextLevelEntranceCol=", "");
            int nextLevelEntranceCol = (int) Integer.parseInt(line);
            game.setNextLevelEntranceCol(nextLevelEntranceCol);
        }
    }

    private void parseFlagPosition(String line) {
        line = line.replace(" ", "");
        if (line.startsWith("flagPosition=")) {
            line = line.replace("flagPosition=", "");
            String[] fields = line.split(",");
            int col = (int) Integer.parseInt(fields[0]);
            int row = (int) Integer.parseInt(fields[1]);
            game.getFlag().setPositionByCell(col, row);
        }
    }
    
    private void parseLineTileProperties(String line) {
        line = line.replace(" ", "");
        String[] propertiesStart = { "rigid", "pushable", "destroyable", "collectable" };
        Set[] propertiesSet = { rigidTiles, pushableTiles, destroyableTiles, collectableTiles };
        for (int p = 0; p < propertiesStart.length; p++) {
            if (line.startsWith(propertiesStart[p])) {
                line = line.split("=")[1];
                String[] tilesId = line.split(",");
                for (String tileId : tilesId) {
                    if (!tileId.isEmpty()) {
                        propertiesSet[p].add(Integer.parseInt(tileId));
                    }
                }
            }
        }
    }

    private void parseLineAnimations(String line) {
        line = line.replace(" ", "");
        if (line.isEmpty()) {
            return;
        }
        String[] animationInfo = line.split("=");
        int tileId = Integer.parseInt(animationInfo[0]);
        String[] animationIds = animationInfo[1].split(",");
        int[] animation = new int[animationIds.length];
        for (int i = 0; i < animation.length; i++) {
            animation[i] = Integer.parseInt(animationIds[i]);
        }
        animations.put(tileId, animation);
    }

    private void parseLineItem(String line) {
        try {
            line = line.replace(" ", "");
            String[] propertiesStart = { "Mushroom", "Star", "Flower", "Coin" };
            for (int p = 0; p < propertiesStart.length; p++) {
                if (line.startsWith(propertiesStart[p])) {
                    String[] data = line.split("=");
                    String fields[] = data[1].split(",");
                    int col = Integer.parseInt(fields[0]);
                    int row = Integer.parseInt(fields[1]);
                    int count = Integer.parseInt(fields[2]);
                    int endTileId = Integer.parseInt(fields[3]);
                    String extraData = fields[4];
                    String namespace = "br.ol.smb.entity.item.";
                    Class itemClass = Class.forName(namespace + data[0]);
                    Tile tile = getTileByCell(col, row);
                    Item[] items = new Item[count];
                    for (int i = 0; i < items.length; i++) {
                        Item tileObject = (Item) itemClass.newInstance();
                        tileObject.set(tile, endTileId, extraData);
                        items[i] = tileObject;
                    }
                    tile.setItems(items);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

    private void parseLineEnemy(String line) {
        try {
            line = line.replace(" ", "");
            if (line.isEmpty()) {
                return;
            }
            
            String[] data = line.split("=");
            String fields[] = data[0].split(",");
            int col = Integer.parseInt(fields[0]);
            int row = Integer.parseInt(fields[1]);
            
            String[] enemiesData = data[1].split(";");
            int enemiesCount = enemiesData.length;
            Enemy[] enemies = new Enemy[enemiesCount];
            String namespace = "br.ol.smb.entity.enemy.";
            Tile tile = getTileByCell(col, row);
            for (int i = 0; i < enemiesData.length; i++) {
                String enemyData = enemiesData[i];                
                String[] enemyFields = enemyData.split(",");
                int wx = Integer.parseInt(enemyFields[1]) * Map.TILE_SIZE;
                int wy = Integer.parseInt(enemyFields[2]) * Map.TILE_SIZE;
                wx += Map.TILE_SIZE / 2;
                wy += Map.TILE_SIZE;
                Class enemyClass = Class.forName(namespace + enemyFields[0]);
                Enemy enemy = (Enemy) enemyClass.newInstance();
                enemy.set(game, wx, wy);
                enemies[i] = enemy;
            }
            tile.setEnemies(enemies);
        } catch (Exception ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

    private void parseLineCheckpoint(String line) {
        try {
            line = line.replace(" ", "");
            if (!line.startsWith("checkpoint=")) {
                return;
            }
            line = line.replace("checkpoint=", "");
            String[] data = line.split(",");
            int col = Integer.parseInt(data[0]);
            int row = Integer.parseInt(data[1]);
            Tile tile = getTileByCell(col, row);
            tile.setCheckpoint(true);
            if (game.getLastCheckpoint() == null) {
                game.setLastCheckpoint(tile);
            }
        } catch (Exception ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    private Tile createTileById(int tileId, int col, int row) {
        Tile tile;
        if (tileId == 0) {
            tile = new Tile(game, tileId, col, row, 0, 0, null);
        }
        else {
            tile = new Tile(game, tileId, col, row, 0, 0, Color.BLACK);
        }
        return tile;
    }
    
    private void parseLineTiles(int row, String line) {
        String[] tilesId = line.split(",");
        for (int col = 0; col < tilesId.length; col++) {
            int tileId = Integer.parseInt(tilesId[col].trim());
            tiles[row][col] = createTileById(tileId, col, row);
        }
    }
    
    public Tile replaceTile(Tile previousTile, int newTileId) {
        int col = previousTile.getCol();
        int row = previousTile.getRow();
        Tile newTile = createTileById(newTileId, col, row);
        tiles[row][col] = newTile;
        return newTile;
    }
    
    public void start() {
        EMPTY_TILE = new Tile(game, -1, 0, 0, 0, 0, null);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tiles[row][col].start();
            }
        }        
    }
    
    public void convertWorldToCell(int wx, int wy, Point resultCellPosition) {
        int col = wx / TILE_SIZE;
        int row = wy / TILE_SIZE;
        col = MathUtil.clamp(col, 0, cols - 1);
        row = MathUtil.clamp(row, 0, rows - 1);
        resultCellPosition.setLocation(col, row);
    }
    
    private static final Point pointTmp = new Point();
    
    public void retrieveTiles(Rectangle area, List<Tile> resultTiles) {
        convertWorldToCell(area.x, area.y, pointTmp);
        int col1 = pointTmp.x;
        int row1 = pointTmp.y;
        convertWorldToCell(area.x + area.width, area.y + area.height, pointTmp);
        int col2 = pointTmp.x + 1;
        int row2 = pointTmp.y + 1;

        resultTiles.clear();
        for (int row = row1; row < row2; row++) {
            for (int col = col1; col < col2; col++) {
                resultTiles.add(tiles[row][col]);
            }
        }
    }
    
    public Tile getTileByWorld(int wx, int wy) {
        int col = wx / TILE_SIZE;
        int row = wy / TILE_SIZE;
        return getTileByCell(col, row);
    }
    
    public Tile getTileByCell(int col, int row) {
        if (col < 0 || row < 0 || col > cols - 1 || row > rows - 1) {
            EMPTY_TILE.setCol(col);
            EMPTY_TILE.setRow(row);
            return EMPTY_TILE;
        }
        return tiles[row][col];
    }

    public void setTileByCell(int col, int row, Tile tile) {
        tiles[row][col] = tile;
    }
    
}
