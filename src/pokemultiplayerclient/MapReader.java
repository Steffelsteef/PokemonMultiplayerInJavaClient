/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import lib.Database;
import lib.overworldobjects.InfoBoard;

/**
 *
 * @author Gebruiker
 */
public class MapReader {
    ScreenOverworld screenOverworld;
    
    int amount;
    
    int doorCount = 0;
    
    char ch;
    char[][] coords = new char[100][100];
    
    TextArray textArray;
    
    Database db;
    
    public MapReader(ScreenOverworld screenOverworld){
        this.screenOverworld = screenOverworld;
        db = new Database(screenOverworld.player.username);
        
    }
    
    public void toggleMap(int code, boolean transition){
        System.out.println("Toggling map at " + screenOverworld.map.currentMapint +" -> " + code);
        
        if( transition == true )
        {
            screenOverworld.transition();
        }
        
        System.out.println("hier wel");
        while( screenOverworld.transitionDone() == false ) {}
        System.out.println("Transition done.");
        
        
        if(-1 == screenOverworld.map.currentMapint)
        {
            toggleMap(code, "x");
        }
        if(0 == screenOverworld.map.currentMapint)
        {
            
            if( code == 1 ) toggleMap(1, "entrance");
            if( code == 2 ) toggleMap(2, "entrance");  
        }
        else if(1 == screenOverworld.map.currentMapint)
        {
            if( code == 1 ) toggleMap(3, "ownhouse");
        } 
        else if(2 == screenOverworld.map.currentMapint)
        {
            if( code == 1 ) toggleMap(0,  "caveExit");
        }
        else if(3 == screenOverworld.map.currentMapint)
        {
            if( code == 1 ) toggleMap(1,  "entrance");
        }
    }
    
    private void toggleMap(int mapCode, String from){
        StringReader in;
        System.out.println("mapcode/from = " + mapCode + "/" + from);
        String mapname = "overworldTest2";
        if(mapCode == 1) mapname = "house1";
        else if(mapCode == 2) mapname = "startcaveTest";
        else if(mapCode == 3) mapname = "realmaptesting";
        
        screenOverworld.map.setMap(mapCode, from);
        
        int x, y;
        int nx, ny;
        
        InputStream is = getClass().getResourceAsStream("/img/maps/" + mapname + ".txt");
        URL pathname = getClass().getResource("/img/maps/" + mapname + ".txt");
        File file = null;
//        try {
//            file = new File(pathname.toURI());
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(StringReader.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;
        
        screenOverworld.player.pm.objects.removeObjects();
        doorCount = 0;
        
        try {
            //reader = new BufferedReader(new FileReader(file));
            
            reader = new BufferedReader(new InputStreamReader(is));
            String text;
            ny = 0;
            int infoboard_count = 0;
            
            while ((text = reader.readLine()) != null) {
                in = new StringReader(text);
                amount = 0;
                ny++;
                nx = 0;
                while (amount < 41) {
                    amount++;
                    nx++;
                    ch = (char) in.read();
                    
                    x = nx * 64 - 64;
                    y = ny * 64 - 16 - 64;
                    
                    
                    
                    if(ch == 'X'){                                              // X = muur (overig)
                        System.out.print("O");
                        coords[nx][ny] = 'O';
                        screenOverworld.player.pm.objects.addWall(x, y);
                        
                    } else if(ch == 'g'){
                        System.out.print("g");
                        coords[nx][ny] = 'g';
                        screenOverworld.player.pm.objects.addGrass(x, y, true);
                    } else if(ch == 'G'){
                        System.out.print(" ");
                        coords[nx][ny] = ' ';
                        screenOverworld.player.pm.objects.addGrass(x, y, false);
                    }
                    
                    else if(ch == '1'){                                       // 1 = muur (huis1)
                        System.out.print("=");
                        coords[nx][ny] = '=';
                        screenOverworld.player.pm.objects.addWall(x, y);
                        
                    } else if(ch == '!'){                                       // ! = dak van huis 1
                        System.out.print("_");
                        coords[nx][ny] = '!';
                        
                        
                    } else if(ch == 'd'){
                        System.out.print("^");
                        coords[nx][ny] = '^';
                        
                        doorCount++;
                        
                        screenOverworld.player.pm.objects.addObject(x, y, 2, doorCount, false);
                        
                        
                    } else if(ch == '2'){                                       // 2 = muur (huis2)
                        System.out.print("=");
                        coords[nx][ny] = '=';
                        screenOverworld.player.pm.objects.addWall(x, y);
                        
                    } else if(ch == '@'){                                       // @ = dak van huis 2
                        System.out.print("_");
                        coords[nx][ny] = '_';
                        
                    } else if(ch == '3'){
                        System.out.print("3");
                        coords[nx][ny] = '3';
                        screenOverworld.player.pm.objects.addWall(x, y);
                    } else if(ch == '+'){                                       // Q =  bord 1
                        System.out.print("+");
                        coords[nx][ny] = '+';
                        
                        infoboard_count++;
                        
                        InfoBoard ib = db.getInfoBoard(mapname, infoboard_count);
                        
                        screenOverworld.player.pm.objects.addInfoBoard(x, y, 1, ib.getTextArray());
                        
                    } else if(ch == 'W'){                                       // W = bord 2
                        System.out.print("+");
                        coords[nx][ny] = '+';
                        
                        InfoBoard ib2 = db.getInfoBoard(mapname, 2);
                        
                        screenOverworld.player.pm.objects.addInfoBoard(x, y, 2, db.ib2.getTextArray());
                        
                    } else if(ch == 'E'){
                        System.out.print("+");
                        coords[nx][ny] = '+';
                        
                        InfoBoard ib3 = db.getInfoBoard(mapname, 3);
                        
                        screenOverworld.player.pm.objects.addInfoBoard(x, y, 3, db.ib3.getTextArray());
                        textArray = new TextArray("Pikachu's Lair");
                        screenOverworld.player.pm.objects.addInfoBoard(x, y,3, textArray);
                            textArray.addText("Welcome to Pikachu's lair!");
                            textArray.addText("Well done, it is finished.");
                        
                    }// else if(ch == ','){
                     //   System.out.print("p");
                     //   coords[nx][ny] = 'p';
                     //   vaddEncounter(x-xBegin, y-yBegin, 0, new Pokemon(pokedex.raticate, null, pokedex.a1, null, null, null, new Stats(5,10,10,10,20,20)));
                  /*  }*/ else if(ch == 'n'){                                       // n = newline
                        System.out.print("\n");
                        
                    } else {                                                    // niets
                        System.out.print(" ");
                        
                    }
                }
                
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        } catch (IOException e) {
            System.err.println("IOException.");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }  
    }
    
    
}
