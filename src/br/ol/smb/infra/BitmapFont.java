package br.ol.smb.infra;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * BitmapFont class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BitmapFont {
    
    private final String[] fontCharacterOrderLines = {
        "0123456789ABCDEF",
        "GHIJKLMNOPQRSTUV",
        "WXYZ@.  -* !    " 
    };
    private static final int CHAR_SIZE = 8;
    private final Map<Character, Point> charactersCoordinate = new HashMap<Character, Point>();
    private BufferedImage font;

    public BitmapFont() {
        try {
            font = ImageIO.read(getClass().getResourceAsStream("/res/graphic/bitmap_font.png"));
            processCharacterCoordinates();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

    private void processCharacterCoordinates() {
        for (int row = 0; row < fontCharacterOrderLines.length; row++) {
            String fontCharacterOrderLine = fontCharacterOrderLines[row];
            for (int col = 0; col < fontCharacterOrderLine.length(); col++) {
                char c = fontCharacterOrderLine.charAt(col);
                Point charPosition = new Point(col, row);
                charactersCoordinate.put(c, charPosition);
            }
        }
    }

    public void drawText(Graphics2D g, String text, int col, int row) {
        for (int textCol = 0; textCol < text.length(); textCol++) {
            char c = text.charAt(textCol);
            Point charPosition = charactersCoordinate.get(c);
            if (charPosition != null) {
                int dx1 = (col + textCol) * CHAR_SIZE;
                int dy1 = row * CHAR_SIZE;
                int dx2 = dx1 + CHAR_SIZE;
                int dy2 = dy1 + CHAR_SIZE;
                int sx1 = charPosition.x * CHAR_SIZE;
                int sy1 = charPosition.y * CHAR_SIZE;
                int sx2 = sx1 + CHAR_SIZE;
                int sy2 = sy1 + CHAR_SIZE;
                g.drawImage(font, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
            }
        }
    }
    
}
