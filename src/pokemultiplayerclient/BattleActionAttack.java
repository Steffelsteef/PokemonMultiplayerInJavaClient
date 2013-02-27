/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

/**
 *
 * @author Gebruiker
 */
public class BattleActionAttack {
    private int damage;
    private String attack_name;
    private String pokemon_name;
    private int attack_type;
    private int damage_type;
    private int speed;
    
    public BattleActionAttack(int damage, String attack_name, String pokemon_name, int attack_type, int damage_type, int speed)
    {
        this.damage = damage;
        this.attack_name = attack_name;
        this.pokemon_name = pokemon_name;
        this.attack_type = attack_type;
        this.damage_type = damage_type;
        this.speed = speed;
    }
    
    public int getDamage()
    {
        return damage;
    }
    
    public String getName()
    {
        return attack_name;
    }
    
    public String getPokemon()
    {
        return pokemon_name;
    }
    
    public int getAttackType()
    {
        return attack_type;
    }
    
    public int getDamageType()
    {
        return damage_type;
    }
    
    public int getSpeed()
    {
        return speed;
    }
}
