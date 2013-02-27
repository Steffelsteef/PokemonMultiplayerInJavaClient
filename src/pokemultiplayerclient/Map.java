/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author Gebruiker
 */
public class Map {
    int mapCode = -1;
    
    ArrayList<Image> images;
    ArrayList<Image> foregrounds;
    ArrayList<Four> startcoords;
    
    Player player;
    int currentMapint = -1;
    
    Random battleRandomator;
    
    
    
    public Map(Player player){
        this.player = player;
        images = new ArrayList();
        foregrounds = new ArrayList();
        startcoords = new ArrayList();
        loadAllImages();
        
        battleRandomator = new Random();
        
    }
    
    
//    public void setMap(int code, int entrance){
//        mapCode = code;
//        player.x = startcoords.get(code).x;
//        player.y = startcoords.get(code).y;
//        
//    }
    
    public void setMapOnLogin(int zone)
    {
        player.pm.objects.mr.toggleMap(zone, false);
    }
    
    public void setMap(int toMapint, String fromLocation){
        this.currentMapint = toMapint;
        if(toMapint == 0) // overworldTest2
        {
            mapCode = 0;
            player.zone = 0;
            switch (fromLocation) {
                case "house":
                    player.x = 960;
                    player.y = 640 - 16;
                    break;
                case "caveExit":
                    player.x = 1792;
                    player.y = 640 - 16;
                    player.setWalking((byte) 0);
                    break;
                
            }
        }
        else if(toMapint == 1) // house1
        {
            mapCode = 1;
            player.zone = 1;
            
            if(fromLocation.equals("entrance"))
            {
                
                player.x = 320; //320
                player.y = 512 - 16; //512
            }
        }
        else if(toMapint == 2) // startcaveTest
        {
            mapCode = 2;
            player.zone = 2;
            
            if(fromLocation.equals("entrance"))
            {
                player.x = 192; //320
                player.y = 128 - 16; //512
                player.setWalking((byte) 0);
            }
        }
        
        else if(toMapint == 3)
        {
            mapCode = 3;
            player.zone = 3;
            if(fromLocation.equals("ownhouse"))
            {
                player.x = 704; //320
                player.y = 576 - 16; //512
                player.setWalking((byte) 0);
            }
        }
        player.so.pmc.sendState(player.x, player.y, player.walking, player.zone, 0);
    }
    
    public String getCurrentMapname()
    {
        return getMapname(currentMapint);
    }
    
    public String getMapname(int code)
    {
        switch(code) {
            case 0: return "overworldTest2";
            case 1: return "house1";
            case 2: return "startcaveTest";
            case 3: return "realmaptesting";
            case 4: return "Unknown Mapname";
                
        }
        
        return "overworldTest2";
    }
            
    
    private void loadAllImages(){
        try {
            images.add(ImageIO.read(getClass().getResource("/img/maps/overworldTest2.png")));
            images.add(ImageIO.read(getClass().getResource("/img/maps/house1.png")));
            images.add(ImageIO.read(getClass().getResource("/img/maps/startcaveTest.png")));
            images.add(ImageIO.read(getClass().getResource("/img/maps/realmaptesting.png")));
            
            foregrounds.add(ImageIO.read(getClass().getResource("/img/maps/overworldTest2f.png")));
            foregrounds.add(ImageIO.read(getClass().getResource("/img/maps/overworldTest2f.png")));
            foregrounds.add(ImageIO.read(getClass().getResource("/img/maps/overworldTest2f.png")));
            foregrounds.add(ImageIO.read(getClass().getResource("/img/maps/realmaptestingf.png")));

            startcoords.add(new Four(0, 0, (short)576, (short)368));
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Image getCurrentMap(int mapCode){
        return images.get(mapCode);
    }
}
class Four{
    int mapCode;
    int entrance;
    short x, y;
    
    
    public Four(int mapCode, int entrance, short x, short y){
        this.mapCode = mapCode;
        this.entrance = entrance;
        this.x = x;
        this.y = y;
    }
}
