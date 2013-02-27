/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

/**
 *
 * @author Gebruiker
 */
public class Stats {
    public int level, attack, defense, special_atk, special_def, speed, maxhp, hp;
    
    public Stats (int level, int attack, int defense, int special_atk, int special_def, int speed, int maxhp, int hp){
        this.level = level;
        this.attack = attack;
        this.defense = defense;
        this.special_atk = special_atk;
        this.special_def = special_def;
        this.speed = speed;
        this.maxhp = hp;
        this.hp = hp;
    }
}