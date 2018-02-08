package br.ol.smb.entity;

import br.ol.smb.infra.Display;
import br.ol.smb.infra.Entity;
import br.ol.smb.infra.Game;
import br.ol.smb.infra.Time;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * FadeEffect class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class FadeEffect extends Entity {

    private final Color[] colors = new Color[256];
    private double intensity;
    private double targetIntensity;
    private double fadeVelocity = 1;
    
    public FadeEffect(Game game) {
        super(game);
        createColors();
        setUnremovable(true);
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    private void createColors() {
        for (int alpha = 0; alpha < 256; alpha++) {
            colors[alpha] = new Color(0, 0, 0, alpha);
        }
    }
    
    @Override
    public void start() {
        coordinate = Coordinate.SCREEN_SPACE;
        setVisible(true);
        setzOrder(1000);
    }

    @Override
    public void fixedUpdate() {
        double delta = targetIntensity - intensity;
        if (delta > 0) {
            intensity += 1 * Time.getFixedDeltaTime();
            if (intensity >= 1) {
                intensity = targetIntensity;
            }
        }
        else if (delta < 0) {
            intensity -= 1 * Time.getFixedDeltaTime();
            if (intensity <= 0) {
                intensity = targetIntensity;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        drawFadeEffect(g);
    }

    private void drawFadeEffect(Graphics2D g) {
        int alpha = (int) (intensity * 255);
        g.setColor(colors[alpha]);
        g.fillRect(0, 0, Display.SCREEN_WIDTH, Display.SCREEN_HEIGHT);
    }

    public void fadeIn() {
        targetIntensity = 1;
    }

    public void fadeOut() {
        targetIntensity = 0;
    }
    
    public boolean isFinished() {
        return intensity == targetIntensity;
    }
    
}
