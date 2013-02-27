/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

/**
 *
 * @author Gebruiker
 */
public class Attack {
    public int PP, accuracy, speed, damage, damagetype, effecttype;
    public boolean physical;
    public String name;
    
    
    public Attack(int PP, int accuracy, int speed, int damage, int damagetype, int effecttype, boolean physical, String name){
        this.PP = PP;
        this.accuracy = accuracy;
        this.speed = speed;
        this.damage = damage;
        this.damagetype = damagetype;
        this.effecttype = effecttype;
        this.physical = physical;
        this.name = name;
    }
}
