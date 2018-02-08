package br.ol.smb.infra;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * SpriteSheet class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class SpriteSheet {

    private BufferedImage image;
    private int cols;
    private int rows;

    public BufferedImage getImage() {
        return image;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
    
    public void load(String resource) {
        try {
            InputStream is = getClass().getResourceAsStream("/res/graphic/" + resource);
            image = ImageIO.read(is);
            cols = image.getWidth() / Map.TILE_SIZE;
            rows = image.getHeight() / Map.TILE_SIZE;
        } catch (IOException ex) {
            Logger.getLogger(SpriteSheet.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

    public void drawEntityByWorld(Graphics2D g, int id, int wx, int wy, int spriteHeight, boolean flipHorizontal, boolean flipVertical) {
        id--;
        int dx1 = wx - Map.TILE_SIZE / 2;
        int dy1 = wy - spriteHeight;
        int dx2 = dx1 + Map.TILE_SIZE;
        int dy2 = dy1 + spriteHeight;
        int scol = id % cols ;
        int srow = id / cols; 
        int sx1 = scol * Map.TILE_SIZE;
        int sy1 = (srow + 1) * Map.TILE_SIZE - spriteHeight;
        int sx2 = sx1 + Map.TILE_SIZE;
        int sy2 = sy1 + spriteHeight;
        if (flipHorizontal) {
            int dxTmp = dx1;
            dx1 = dx2;
            dx2 = dxTmp;
        }
        if (flipVertical) {
            int dyTmp = dy1;
            dy1 = dy2;
            dy2 = dyTmp;
        }
        g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    public void drawTileByWorld(Graphics2D g, int id, int wx, int wy) {
        drawTileByWorld(g, id, wx, wy, Map.TILE_SIZE);
    }

    public void drawTileByWorld(Graphics2D g, int id, int wx, int wy, int cellSize) {
        int scaledCols = cols * (Map.TILE_SIZE / cellSize);
        id--;
        int dx1 = wx;
        int dy1 = wy;
        int dx2 = dx1 + cellSize;
        int dy2 = dy1 + cellSize;
        int scol = id % scaledCols;
        int srow = id / scaledCols; 
        int sx1 = scol * cellSize;
        int sy1 = srow * cellSize;
        int sx2 = sx1 + cellSize;
        int sy2 = sy1 + cellSize;
        g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }
    
    public void drawTileByCell(Graphics2D g, int id, int row, int col) {
        drawTileByCell(g, id, row, col, Map.TILE_SIZE);
    }

    public void drawTileByCell(Graphics2D g, int id, int row, int col, int cellSize) {
        int wx = col * cellSize;
        int wy = row * cellSize;
        drawTileByWorld(g, id, wx, wy, cellSize);
    }

}
