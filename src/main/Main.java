package main;

import br.ol.smb.infra.Display;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Main class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Display view = new Display();
                JFrame frame = new JFrame();
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(view);
                frame.setVisible(true);
                view.requestFocus();
                view.start();
            }
        });        
    }
    
}
