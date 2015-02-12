/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package A;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saranath Raju
 */
public class Main_Class {
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Server1().setVisible(true);
                try {
                    new Client1().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(Main_Class.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
}
