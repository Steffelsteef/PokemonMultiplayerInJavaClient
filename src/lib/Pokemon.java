/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

/**
 *
 * @author Gebruiker
 */
public class Pokemon {
    public int number;
    public String name;
    public int type1, type2;
    
    public String nickname;
    public Attack at1, at2, at3, at4;
    
    private int level, strength, defense, spec_strength, spec_defense, speed, maxhp, hp;
    
    public int exp, expCurve;
    private int expneeded;
    
    int location;
    
    public void setExpneeded()
    {
        expneeded = Database.calcNeededExp(level, expCurve);
    }
    
    public int getExpneeded()
    {
        return expneeded;
    }
    
    public Pokemon(int number, String name, int type1, int type2){
        this.number = number;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        
    }
    
    public Pokemon(Pokemon pkmn, String nickname, Attack at1, Attack at2, Attack at3, Attack at4, Stats stats, int exp, int expCurve){
        this.number = pkmn.number;
        this.name = pkmn.name;
        if( nickname == null ) this.nickname = pkmn.name;
            else this.nickname = nickname;
        this.type1 = pkmn.type1;
        this.type2 = pkmn.type2;
        this.at1 = at1;
        this.at2 = at2;
        this.at3 = at3;
        this.at4 = at4;
        this.level = stats.level;
        this.strength = stats.attack;
        this.defense = stats.defense;
        this.spec_strength = stats.special_atk;
        this.spec_defense = stats.special_def;
        this.speed = stats.speed;
        this.maxhp = stats.maxhp;
        this.hp = stats.hp;
        this.exp = exp;
        this.expCurve = expCurve;
    }
    
    public void setLocation(int location)
    {
        this.location = location;
    }
    
    @Override
    public String toString()
    {
        return "====toString====" + 
                "\n Pokemon nr. " + number + 
                "\n Name " + name + 
                "\n type1 = " + type1 + 
                "\n type2 = " + type2 + 
                "\n at1 = " + at1.name + 
                "\n at2 = " + at2.name + 
                "\n at3 = " + at3.name + 
                "\n at4 = " + at4.name + 
                "\n level = " + level + 
                "\n strength = " + strength + 
                "\n defense = " + defense + 
                "\n spec_strength = " + spec_strength + 
                "\n spec_defense = " + spec_defense + 
                "\n speed = " + speed + 
                "\n maxhp = " + maxhp + 
                "\n hp = " + hp + 
                "\n exp = " + exp + 
                "\n expCurve= " + expCurve + 
               "\n==//toString====";
    }

    public int getLevel() {
        return level;
    }

    public int getStrength() {
        return strength;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpec_strength() {
        return spec_strength;
    }

    public int getSpec_defense() {
        return spec_defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getMaxhp() {
        return maxhp;
    }

    public int getHp() {
        return hp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setSpec_strength(int spec_strength) {
        this.spec_strength = spec_strength;
    }

    public void setSpec_defense(int spec_defense) {
        this.spec_defense = spec_defense;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHp(int hp) {
        System.out.println("this.hp = " + this.hp + " and hp = " + hp);
        this.hp = hp;
    }

    public void setExpneeded(int expneeded) {
        this.expneeded = expneeded;
    }

    public void setMaxhp(int maxhp) {
        this.maxhp = maxhp;
    }
    
    public int getLocation()
    {
        return location;
    }
    
    

    
    
}

