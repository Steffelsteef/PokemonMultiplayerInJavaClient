/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import ser.PokeDataPackage;

/**
 *
 * @author Gebruiker
 */
public class Objects { // created in player.pm
    Player player;
    ScreenOverworld so;
    MapReader mr;
    
    ArrayList<Integer> x = new ArrayList();
    ArrayList<Integer> y = new ArrayList();
    ArrayList<Integer> type = new ArrayList();
    ArrayList<Boolean> collision = new ArrayList();
    ArrayList<Integer> number = new ArrayList();
    
    
    ArrayList<String> text = new ArrayList();
    ArrayList<TextArray> textArray = new ArrayList();
    
    Random battleRandomator;
    
    public Objects(){
        battleRandomator = new Random();
        
    }
    
    public void init(ScreenOverworld so, Player p, MapReader mr){
        this.so = so;
        this.player = p;
        this.mr = mr;
    }
    
    public void getInfo(){
        System.out.println("(x, y) - type");
        for (int i = 0; i < x.size(); i++){
            System.out.println("(" + x.get(i) +", " + y.get(i) + ") - " + type.get(i));
        }
    }
    
    public void removeObjects(){
        this.x = new ArrayList();
        this.y = new ArrayList();
        this.type = new ArrayList();
        this.collision = new ArrayList();
        this.number = new ArrayList();
        this.text = new ArrayList();
        this.textArray = new ArrayList();
    }
    
    public void getObjects(){
        for(int i = 0; i < x.size(); i++){
            String typeString = "", numberString = "";
            if(type.get(i) == 0){ typeString = "wall"; } 
                else if(type.get(i) == 1){ typeString = "infoboard"; }
            if(number.get(i) != 0) { numberString = ", number: " + number.get(i);
            }
            System.out.println("i (x ,y ); type: " + typeString + numberString);
        }
    }
    
    public void addWall(int x, int y){ // only for walls
        this.x.add(x);
        this.y.add(y);
        this.type.add(0);
        this.collision.add(true);
        this.number.add(null);
        this.text.add(null);
        this.textArray.add(null);
    }
    
    public void addGrass(int x, int y, boolean on){ // only for grass
        if( on == true ){
            this.type.add(4);
        } else {
            this.type.add(5);
        }
        
        this.x.add(x);
        this.y.add(y);
        this.collision.add(false);
        this.number.add(null);
        this.text.add(null);
        this.textArray.add(null);
    }
    
    /*
     * 0 = wall
     * 1 = info board
     * 2 = door
     * 3 = encounter
     */
    
    public void addInfoBoard(int x, int y, int number, String text){
        this.x.add(x);
        this.y.add(y);
        this.type.add(1);
        this.collision.add(true);
        this.number.add(number);
        this.text.add(text);
        this.textArray.add(null);
    }
    
    public void addInfoBoard(int x, int y, int number){
        this.x.add(x);
        this.y.add(y);
        this.type.add(1);
        this.collision.add(true);
        this.number.add(number);
        this.text.add(null);
        this.textArray.add(null);
    }
    
    public void addInfoBoard(int x, int y, int number, TextArray textArray){
        this.x.add(x);
        this.y.add(y);
        this.type.add(1);
        this.collision.add(true);
        this.number.add(number);
        this.text.add(null);
        this.textArray.add(textArray);
    }
    
    public void addEncounter(int x, int y, int battle){
        this.x.add(x);
        this.y.add(y);
        this.type.add(3);
        this.collision.add(false);
        this.number.add(null);
        this.text.add(null);
        this.textArray.add(null);
    }
        
    public void addObject(int x, int y, int type, int number, boolean col){ // (2,door)
        this.x.add(x);
        this.y.add(y);
        this.type.add(type);
        this.collision.add(col);
        this.number.add(number);
        this.text.add(null);
        this.textArray.add(null);
    }
    
    public boolean checkGrass(OtherPlayer op){
        for(int i = 0; i < x.size(); i++){
            if(op.x == this.x.get(i) && op.y == this.y.get(i))
            {
                if(this.type.get(i) == 4)
                {
                    return true;
                    
                }
            }
        }
        return false;
    }
    
    public void checkStandOns(Player p){
        for(int i = 0; i < x.size(); i++){
            if(p.x == this.x.get(i) && p.y == this.y.get(i))
            {
                if(this.type.get(i) == 2)
                {
                    mr.toggleMap(this.number.get(i), true);
                }
                else if(this.type.get(i) == 4)
                {
                    so.grass = true;
                    calculateBattle();
                }
                else if(this.type.get(i) == 5)
                {
                    so.grass = false;
                }
            }
            
        }
    }
    
    public void calculateBattle()
    {
        if( battleRandomator.nextDouble() < 0.23)
        {
            PokeDataPackage pdp = null;
            so.pmc.sendTCP("cb:r");
            so.battle = true;
            so.characterIsBusy = true;
            
            so.player.leftpressed = false;
            so.player.rightpressed = false;
            so.player.downpressed = false;
            so.player.uppressed = false;
        }
    }
    
    public boolean checkCollision(int e){
        int KeyCode = e;
        if(e == KeyEvent.VK_LEFT){
            for(int i=0; i<x.size(); i++){
                if(this.player.x - 64 == this.x.get(i) && this.player.y == this.y.get(i) && this.collision.get(i) == true ){
                    return true;
                } 
            }
        }
        
        if(e == KeyEvent.VK_RIGHT){
            for(int i=0; i<x.size(); i++){
                if(this.player.x + 64 == this.x.get(i) && this.player.y == this.y.get(i) && this.collision.get(i) == true ){
                    return true;
                } 
            }
        }
        
        if(e == KeyEvent.VK_UP){
            for(int i=0; i<x.size(); i++){
                if(this.player.y - 64 == this.y.get(i) && this.player.x == this.x.get(i) && this.collision.get(i) == true ){
                    return true;
                } 
            }
        }
        
        if(e == KeyEvent.VK_DOWN){
            for(int i=0; i<x.size(); i++){
                if(this.player.y + 64 == this.y.get(i) && this.player.x == this.x.get(i) && this.collision.get(i) == true ){
                    return true;
                } 
            }
        }
        
        
        return false;
    }
    
    
    public TextArray checkConvo(){
        if (this.player.direction == 4){ // left
            for(int i = 0; i < x.size(); i++){
                if(this.type.get(i) == 1 && this.player.x - 64 == this.x.get(i) && this.player.y == this.y.get(i)){
                    return this.textArray.get(i);
                }
                
            }
            
        }
        if (this.player.direction == 8){ // right
            for(int i = 0; i < x.size(); i++){
                if(this.type.get(i) == 1 && this.player.x + 64 == this.x.get(i) && this.player.y == this.y.get(i)){
                    return this.textArray.get(i);
                }
                
            }
            
        }
        if (this.player.direction == 12){ // up
            for(int i = 0; i < x.size(); i++){
                if(this.type.get(i) == 1 && this.player.x == this.x.get(i) && this.player.y - 64 == this.y.get(i)){
                    return this.textArray.get(i);
                }
                
            }
            
        }
        if (this.player.direction == 0){ // down
            for(int i = 0; i < x.size(); i++){
                if(this.type.get(i) == 1 && this.player.x == this.x.get(i) && this.player.y + 64 == this.y.get(i)){
                    return this.textArray.get(i);
                }
                
            }
            
        }
        
        return null;
    }
    
//    public boolean checkAction(){
//        if (this.client.player.direction ==  t"){
//            for(int i=0; i<x.size(); i++){
//                if(this.client.player.x - 1 == this.x.get(i)/64 && this.client.player.y == this.y.get(i)/64 && this.type.get(i) == 1){
//                    return true;
//                } 
//            }
//        }
//        return false;
//    }

    
}


// wat te doen: dat je in lib.Database.java aangeeft wat elk object is in welke map, en dat je
// in MapReader aangeeft dat hij bij het lezen van een char de mapnaam doorgeeft en de
// object met zijn info. Vervolgens creëert hij, via deze code, de fysieke objecten.