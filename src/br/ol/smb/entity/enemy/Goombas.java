package br.ol.smb.entity.enemy;

/**
 * Goombas class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Goombas extends Enemy {
    
    public Goombas() {
    }

    @Override
    public void start() {
        super.start();
        setAnimation(2225, 2226);
        killPoint = 100;
    }

    @Override
    protected void killedByMarioStamp() {
        tileId = 2227; // dead 
        animation = null;
        super.killedByMarioStamp();
    }

    @Override
    public void onImpactFromGround(ImpactStrength strength) {
        killedFromGround();
    }
    
    @Override
    protected void killedFromGround() {
        super.killedFromGround();
    }
    
//    @Override
//    protected void onStateChanged(State newState) {
//        if (newState == State.DYING) {
//            tileId = 2227; // dead 
//            animation = null;
//        }
//    }
    
}
