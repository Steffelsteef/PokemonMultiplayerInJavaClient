/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author Gebruiker
 */
public class Player extends Thread{
    ScreenOverworld so;
    String username = "Unknown Object";
    int charType = 0;
    int walking = 0;
    boolean bumping = false;
    PlayerMover pm;
    
    public int zone = 0;
    public int subzone = 0;
    
    
    BufferedImage[][] sprites = new BufferedImage[16][16];
    BufferedImage[][] sprites_battle = new BufferedImage[16][16];
    BufferedImage[][] sprites_grass = new BufferedImage[16][16];
    int x = -512 + 0, y = -512 + -16;
    boolean leftpressed = false, rightpressed = false, uppressed = false, downpressed = false;
    
    
    
    Color color;
    
    public Player(String username, int charType){
        this.username = username;
        this.charType = charType;
        pm = new PlayerMover(this);
        
        getImages();
        
        pm.start();
      
    }
    
    public void setStart(int charType, int x, int y, int walking, int zone, int subzone)
    {
        this.charType = charType;
        this.x = x;
        this.y = y;
        this.walking = walking;
        this.zone = zone;
        this.subzone = subzone;
        
        
    }
    
    
    
    public void setX(int x){
        this.x = (short) x;
    }
    
    public void setY(int y){
        this.y = (short) (y);
    }
    
    public void setWalking(byte w){
        this.walking = w;
    }
    
    int dx, dy;
    byte direction;
    boolean moving = false;
    
    public void setScrOv(ScreenOverworld so)
    {
        this.so = so;
    }
    
    public void getImages(){
        
        
        int sheets = 6;
        BufferedImage[] spritesheets = new BufferedImage[sheets];
        BufferedImage[] spritebattlesheets = new BufferedImage[sheets];
        BufferedImage[] spritegrasssheets = new BufferedImage[sheets];
        
        try {
            spritesheets[0] = ImageIO.read(getClass().getResource("/img/sprsh/Lass.png"));
            spritesheets[1] = ImageIO.read(getClass().getResource("/img/sprsh/Youngster.png"));
            spritesheets[2] = ImageIO.read(getClass().getResource("/img/sprsh/Nerd.png"));
            spritesheets[3] = ImageIO.read(getClass().getResource("/img/sprsh/Rocket1.png"));
            spritesheets[4] = ImageIO.read(getClass().getResource("/img/sprsh/Rocket2.png"));
            spritesheets[5] = ImageIO.read(getClass().getResource("/img/sprsh/Rocket3.png"));
            
            spritebattlesheets[0] = ImageIO.read(getClass().getResource("/img/sprsh/bLass.png"));
            spritebattlesheets[1] = ImageIO.read(getClass().getResource("/img/sprsh/bYoungster.png"));
            spritebattlesheets[2] = ImageIO.read(getClass().getResource("/img/sprsh/bNerd.png"));
            spritebattlesheets[3] = ImageIO.read(getClass().getResource("/img/sprsh/Rocket1.png"));
            spritebattlesheets[4] = ImageIO.read(getClass().getResource("/img/sprsh/Rocket2.png"));
            spritebattlesheets[5] = ImageIO.read(getClass().getResource("/img/sprsh/Rocket3.png"));
            
            spritegrasssheets[0] = ImageIO.read(getClass().getResource("/img/sprsh/gLass.png"));
            spritegrasssheets[1] = ImageIO.read(getClass().getResource("/img/sprsh/gLass.png"));
            spritegrasssheets[2] = ImageIO.read(getClass().getResource("/img/sprsh/gNerd.png"));
            spritegrasssheets[3] = ImageIO.read(getClass().getResource("/img/sprsh/Rocket1.png"));
            spritegrasssheets[4] = ImageIO.read(getClass().getResource("/img/sprsh/Rocket2.png"));
            spritegrasssheets[5] = ImageIO.read(getClass().getResource("/img/sprsh/Rocket3.png"));
        } catch (IOException ex) {
            System.err.println("getImages() failed!");
        }
        
        int w = 0;
        int h = 0;

        sprites = new BufferedImage[sheets][16];
        sprites_battle = new BufferedImage[sheets][16];
        sprites_grass = new BufferedImage[sheets][16];
        
        for(int i = 0; i < sheets; i++){
            if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4){
                w = 64;
                h = 80;
            } 
            else if (i == 5)
            {
                w = 64;
                h = 84;
            }
            int round = 0;
                for (int ih = 0; ih < 4; ih++){
                    sprites_battle[i][round] = spritebattlesheets[i].getSubimage(0, ih * h, w, h);
                    for (int iw = 0; iw < 4; iw++){
                        sprites[i][round] = spritesheets[i].getSubimage(iw * w, ih * h, w, h);
                        sprites_grass[i][round] = spritegrasssheets[i].getSubimage(iw * w, ih * h, w, h);
                        round++;
                    }
                }
                System.out.println("Finished loading spritesheet " + i);
        }
    }

    
    public void bump(int e){
        if (e == KeyEvent.VK_LEFT){
            direction = 4;
            bumping = true;
            dx = dy = 0;
        }
        if (e == KeyEvent.VK_RIGHT){
            direction = 8;
            bumping = true;
            dx = dy = 0;

        }
        if (e == KeyEvent.VK_DOWN){
            direction = 0;
            bumping = true;
            dx = dy = 0;
            System.out.println("This is bumpin");
        }
        if (e == KeyEvent.VK_UP){
            direction = 12;
            bumping = true;
            dx = dy = 0;
        }
    }
    
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_LEFT){
                dx = -1;
                dy = 0;
                direction = 4;
                
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                dx = 1;
                dy = 0;
                direction = 8;
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN){
                dx = 0;
                dy = 1;
                direction = 0;
            }
            if(e.getKeyCode() == KeyEvent.VK_UP){
                dx = 0;
                dy = -1;
                direction = 12;
            }
            if(e.getKeyCode() == KeyEvent.VK_C){
                System.out.println("(" + x +  "," + y + ")");
            }
            if(e.getKeyCode() == KeyEvent.VK_V){
                pm.objects.getInfo();
            }

        }
    
    
}


