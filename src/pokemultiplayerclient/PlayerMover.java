/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gebruiker
 */
public class PlayerMover extends Thread {
    Objects objects;
    Player p;
    
    public PlayerMover(Player p){
        this.p = p;
        
        objects = new Objects();
        objects.addWall(576, 304);
    }
    
    public void run(){
        while(true){
            if(p.moving == false &&
                                    ((p.direction == 4 && p.leftpressed == true) || 
                                    (p.direction == 8 && p.rightpressed == true) ||
                                    (p.direction == 0 && p.downpressed == true) ||
                                    (p.direction == 12 && p.uppressed == true)))
            {
                p.moving = true;
                final byte direction = p.direction;
                final int dx = p.dx;
                final int dy = p.dy;
                
                int check = 0;
                if (direction == 0){
                    check = KeyEvent.VK_DOWN;
                }
                else if (direction == 4)
                    check = KeyEvent.VK_LEFT;
                else if (direction == 8)
                    check = KeyEvent.VK_RIGHT;
                else if (direction == 12)
                    check = KeyEvent.VK_UP;
                
                
                if (objects.checkCollision(check) == true) {
                    p.bump(check);
                }
                
                

                new Thread(){
                    public void run(){
                        for(int i = 0; i < 64; i++){
                            int x = p.x;
                            int y = p.y;
                            if(p.bumping == false){
                                x = p.x + dx;
                                y = p.y + dy;
                                p.setX(x);
                                p.setY(y);
                            }

                            if(i == 0){
                                p.setWalking((byte) (1 + direction));
                            } else if(i == 15){
                                p.setWalking((byte) (2 + direction));
                            } else if(i == 31){
                                p.setWalking((byte) (3 + direction));
                            } else if(i == 47){
                                p.setWalking((byte) (0 + direction));
                            } 

                            try {
                                //this.sleep(8);
                                this.sleep(8);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(PlayerMover.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if(x%4 == 0 && y%4 == 0) p.so.pmc.sendCoords(x, y, p.walking);
                        }
                        
                        objects.checkStandOns(p);
                        
                        p.moving = false;
                        p.bumping = false;
                        
                        //p.so.pmc.update(p.x, p.y, p.walking, p.zone, p.subzone);
                    }
                }.start();
            }
        }
    }
    
}
