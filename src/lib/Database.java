/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import lib.overworldobjects.InfoBoard;
import pokemultiplayerclient.ScreenOverworld;
import pokemultiplayerclient.TextArray;

/**
 *
 * @author Gebruiker
 */
public class Database {
    String currentMap;
    public InfoBoard ib1, ib2, ib3;
    public TextArray ta1, ta2, ta3;
    
    public TextArray ta;
    public InfoBoard ib;
    
    String playerName;
    public Pokedex pokedex;
    
    Map<String, ArrayList<Encounter>> list_encounters;
    
    public Database(String playerName){
        this.playerName = playerName;
        pokedex = new Pokedex();
        
        list_encounters = new HashMap<String, ArrayList<Encounter>>();
        insertEncounters();
        
        
    }
    
    public void insertEncounters()
    {
        String map;
        
        map = "overworldTest2";
        addEncounter(map, 0.35f, new Pokemon(pokedex.pikachu, null, pokedex.a_thundershock, pokedex.a_growl, pokedex.a0, pokedex.a0, new Stats(5, 12, 9, 11, 10, 15, 18, 18), 0, 0));
        addEncounter(map, 0.25f, new Pokemon(pokedex.pikachu, null, pokedex.a_thundershock, pokedex.a_growl, pokedex.a_quickattack, pokedex.a0, new Stats(6, 13, 11, 13, 12, 16, 20, 20), 0, 0));
        addEncounter(map, 0.45f, new Pokemon(pokedex.eevee, null, pokedex.a_tackle, pokedex.a_tailwhip, pokedex.a0, pokedex.a0, new Stats(5, 11, 11, 10, 12, 11, 21, 21), 0, 0));
        addEncounter(map, 0.25f, new Pokemon(pokedex.eevee, null, pokedex.a_tackle, pokedex.a_tailwhip, pokedex.a_sandattack, pokedex.a0, new Stats(6, 12, 12, 11, 13, 12, 23, 23), 0, 0));
        
        map = "startcaveTest";
        
    }
    
    public void addEncounter(String mapname, float chance, Pokemon pkmn)
    {
        ArrayList<Encounter> enclist = list_encounters.get(mapname);
        
        if( enclist == null )
        {
            enclist = new ArrayList();
        }
        
        enclist.add(new Encounter(pkmn, chance));
        
        list_encounters.put(mapname, enclist);
    }
    
    public Pokemon getWildEncounter(String mapname)
    {
        list_encounters = new HashMap<String, ArrayList<Encounter>>();
        insertEncounters();
        
        Random random = new Random();
        
        float sum = 0;
        
        
        ArrayList<Encounter> enclist = list_encounters.get(mapname);
        float[] chancelist = new float[enclist.size()];
        Pokemon[] pkmnlist = new Pokemon[enclist.size()];
        
        for(int i = 0; i < enclist.size(); i++)
        {
            sum += enclist.get(i).chance;
            chancelist[i] = sum;
            pkmnlist[i] = enclist.get(i).pokemon;
        }
        
        float calc = random.nextFloat();
        //System.out.println("encounter = " + calc);
        calc *= sum;
        //System.out.println("calc *= sum -> " + calc);
        
        for(int i = 0; i < enclist.size(); i++)
        {
            if( i == 0 )
            {
                if( calc <= chancelist[i] )
                {
                    System.out.println("pkmnlist[" + i + "].hp = " + pkmnlist[i].getHp());
                    return pkmnlist[i];
                }
                    
            }
            else
            {
                if( calc > chancelist[i - 1] && calc <= chancelist[i] )
                {
                    System.out.println("pkmnlist[" + i + "].hp = " + pkmnlist[i].getHp());
                    return pkmnlist[i];
                }
            }
        }
        
        return null;
    }
    
    public static int calcNeededExp(int currentLevel, int expCurve)
    {
        int returned = 0;
        int n = currentLevel;
        
        if(expCurve == 1)
        {
            if(n < 50)
            {
                returned = (int) Math.round(((Math.pow(n,3))*(100-n))/50);
            }
            else if(n >= 50 && n < 68)
            {
                returned = (int) Math.round((Math.pow(n,3)*(150-n))/100);
            }
            else if(n >= 68 && n < 98)
            {
                returned = (int) Math.round((Math.pow(n,3)*((1911-(10*n))/3))/500);
            }
            else if(n >= 98 && n < 100)
            {
                returned = (int) Math.round((Math.pow(n,3)*(160-n))/100);
            }
        }
        else if(expCurve == 2)
        {
            returned = (int) Math.round((4*Math.pow(n,3))/5);
        }
        else if(expCurve == 3)
        {
            returned = (int) Math.round(Math.pow(n,3));
        }
        else if(expCurve == 4)
        {
            returned = (int) Math.round(((6f/5f)*Math.pow(n,3)) - (15f*(Math.pow(n,2))) + (100f*n) - 140f);
        }
        else if(expCurve == 5)
        {
            returned = (int) Math.round((5*Math.pow(n,3))/4);
        }
        else if(expCurve == 6)
        {
            if(n < 15)
            {
                returned = (int) Math.round(Math.pow(n,3) * ((((n+1)/3)+24)/50));
            }
            else if(n >= 15 && n < 36)
            {
                returned = (int) Math.round(Math.pow(n,3) * ((n+14)/50));
            }
            else if(n >= 36 && n < 100)
            {
                returned = (int) Math.round(Math.pow(n,3) * (((n/2)+32)/50));
            }
        }
        
        if(n == 100)
        {
            returned = -1;
        }
        
        System.out.println("calcNeededExp(" + currentLevel + ", " + expCurve + ") = " + returned);
        
        return returned;
    }
    
//    public Pokemon getWildEncounter_old(String mapname)
//    {
//        Random random = new Random();
//        
//        float encounter = random.nextFloat();
//        System.out.println("encounte = " + encounter);
//        if( mapname == "overworldTest2" )
//        {
//            if( encounter < 0.5 ) // Pikachu
//            {
//                if( encounter < 0.4 )
//                {
//                    return new Pokemon(pokedex.pikachu, null, pokedex.a_thundershock, pokedex.a_growl, pokedex.a0, pokedex.a0, new Stats(5, 5, 5, 5, 30, 30));
//                }
//                else if( encounter > 0.4)
//                {
//                    return new Pokemon(pokedex.pikachu, null, pokedex.a_thundershock, pokedex.a_growl, pokedex.a_quickattack, pokedex.a0, new Stats(6, 7, 6, 7, 34, 34));
//                }
//            }
//            else if( encounter >= 0.5 ) // eevee
//            {
//                if( encounter < 0.85 )
//                {
//                    return new Pokemon(pokedex.eevee, null, pokedex.a_tackle, pokedex.a_tailwhip, pokedex.a0, pokedex.a0, new Stats(5, 5, 5, 5, 30, 30));
//                }
//                else if( encounter > 0.85 )
//                {
//                    return new Pokemon(pokedex.eevee, null, pokedex.a_tackle, pokedex.a_tailwhip, pokedex.a_sandattack, pokedex.a0, new Stats(6, 7, 6, 7, 34, 34));
//                }
//            }
//        }
//        
//        
//        return null;
//    }

    
    public InfoBoard getInfoBoard(String mapname, int number){
        ta = new TextArray();
        if (mapname == "overworldTest2")
        {
            if (number == 1)
            {
                ta.addText("Pikachu's House");
                ta.addText(" ");
                
                ta.addText("Fear it!");
                ta.addText("");
                
            }
            else if (number == 2)
            {
                ta.addText(playerName + "'s House");
                ta.addText("Welcome.");
                
                ta.addText("I really love my house!");
                ta.addText("");
            }
        }
        else if (mapname == "house1")
        {
            if (number == 1)
            {
                ta.addText("Bord JONGU");
                ta.addText("Wen er mar aan");
            }
        }
        else if (mapname == "realmaptesting")
        {
            if(number == 1)
            {
                ta.addText(playerName + "'s House");
                ta.addText("");
            }
            else if(number == 2)
            {
                ta.addText("Professor Barkwood's");
                ta.addText("laboratory");
            }
            else if(number == 3)
            {
                ta.addText("Go back!");
                ta.addText("");
            }
            else if(number == 4)
            {
                ta.addText("It's dangerous here!");
                ta.addText("");
            }
            else if(number == 5)
            {
                ta.addText("You have been warned...");
                ta.addText("");
            }
        }
        else {return null;}
        
        ib = new InfoBoard(ta);
        return ib;
    }

}
