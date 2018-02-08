package br.ol.smb.infra;

/**
 * Vec2 class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Vec2 {

    private double x;
    private double y;

    public Vec2() {
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void translate(double tx, double ty) {
        this.x += tx;
        this.y += ty;
    }

    public void scale(double s) {
        this.x *= s;
        this.y *= s;
    }

    public void scale(double sx, double sy) {
        this.x *= sx;
        this.y *= sy;
    }

    public void add(Vec2 v) {
        this.x += v.x;
        this.y += v.y;
    }
    
    public void sub(Vec2 v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }
    
}
