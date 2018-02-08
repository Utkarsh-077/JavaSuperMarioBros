package br.ol.smb.entity;

import br.ol.smb.infra.Camera;
import br.ol.smb.infra.Display;
import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Map;
import br.ol.smb.infra.Time;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Point class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Point extends Entity {
    
    private final String pointStr;
    private final double startY;
    
    public Point(Game game, int point, double wx, double wy) {
        super(game);
        coordinate = Coordinate.SCREEN_SPACE;
        this.pointStr = "" + point;
        tileId = 1655;
        Camera camera = game.getEntity(Camera.class);
        double sx = wx - camera.getPosition().getX() + Display.SCREEN_WIDTH / 2;
        position.set(sx, wy);
        startY = wy;
    }

    @Override
    public void start() {
        setzOrder(5);
        setVisible(true);
    }
    
    @Override
    public void fixedUpdate() {
        double vy = -20 * Time.getFixedDeltaTime();
        position.translate(0, vy);
        if (position.getY() < startY - 24) {
            setDestroyed(true);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        int digitCounts = pointStr.length();
        int startX = digitCounts * 4 / 2;
        for (int digitPosition = 0; digitPosition < digitCounts; digitPosition++) {
            int digit = pointStr.charAt(digitPosition) - 48;
            drawDigit(g, digit, startX, digitPosition);
        }
    }
    
    private void drawDigit(Graphics2D g, int digit, int startX, int digitPosition) {
        int dx1 = (int) (4 * digitPosition + getPosition().getX() - startX);
        int dy1 = (int) (position.getY() - getHeight());
        int dx2 = dx1 + 4;
        int dy2 = dy1 + Map.TILE_SIZE;
        int sx1 = 16 + 4 * digit;
        int sy1 = 464;
        int sx2 = sx1 + 4;
        int sy2 = sy1 + Map.TILE_SIZE;
        Image image = spriteSheet.getImage();
        g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }
    
}
