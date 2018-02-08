package br.ol.smb.infra;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Input class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Input implements KeyListener {
    
    public static boolean[] keyDown = new boolean[256];
    public static boolean[] keyDownConsumed = new boolean[256];

    public static boolean isKeyDown(int keyCode) {
        return keyDown[keyCode];
    }

    public static boolean isKeyPressed(int keyCode) {
        if (!keyDownConsumed[keyCode] && keyDown[keyCode]) {
            keyDownConsumed[keyCode] = true;
            return true;
        }
        return false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyDown[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyDown[e.getKeyCode()] = false;
        keyDownConsumed[e.getKeyCode()] = false;
    }
    
}
