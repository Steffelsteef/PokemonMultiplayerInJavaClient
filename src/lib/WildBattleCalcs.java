/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.util.Random;
import pokemultiplayerclient.ScreenBattle;

/**
 *
 * @author Gebruiker
 */
public class WildBattleCalcs {
    
    /**
     * The mathematical calculation for battle damage.<br />
     * ((((((((2A/5+2)*B*C)/D)/50)+2)*X)*Y/10)*Z)/255<br />
     *  A = attacker's Level<br />
     *  B = attacker's Attack or Special<br />
     *  C = attack Power<br />
     *  D = defender's Defense or Special<br />
     *  X = same-Type attack bonus (1 or 1.5)<br />
     *  Y = Type modifiers (40, 20, 10, 5, 2.5, or 0)<br />
     *  Z = a random number between 217 and 255 <br />
     * @param att_lvl
     *       The level of the attacker.
     * @param att_str
     *       The attack power of the attacker.
     * @param att_spcstr
     *       The special attack power of the attaacker.
     * @param attackpower
     *       The attack's power.
     * @param def_def
     *       The defense of the defender.
     * @param def_spcdef
     *       The special defense of the defender.
     * @param sametype
     *       Whether or not the attacker has the same damage type of the attack. (boolean)
     * @param def_type1 
     *       Type 1 of the defender.
     * @param def_type2
     *       Type 2 of the defender.
     * @param attackdamagetype
     *       The damage type of the attack.
     * @param attacktype
     *       The type of attack (damage, statchange, etc.)
     */
    public static int calcDamage(int att_lvl, float att_str, float att_spcstr, int attackpower, float def_def, float def_spcdef, boolean sametype, int def_type1, int def_type2, int effecttype, int attacktype, boolean physical, float[][] typechart)
    {
        
        float sametypebonus = 1;
        if(sametype == true) sametypebonus = (float) 1.5;
        
        float typemodifier1 = typechart[attacktype][def_type1];
        float typemodifier2 = 1;
        if(def_type2 != 0)
        {
            typemodifier2 = typechart[attacktype][def_type2];
        }
        
        System.out.println("typechart: " + attacktype + ", " + def_type1);
        System.out.println("typemodifiers: " + typemodifier1 + ", " + typemodifier2);
        float typemodifier = typemodifier1 * typemodifier2;
        
        
        double random = Math.random() * 38;
        int randomsolution = (int) Math.round(random);
        
        System.out.println("typechart[" + attacktype + "][" + def_type1 + "]");
        
        float attack = 0f, defense = 0f;
        if(physical)
        {
            attack = att_str;
            defense = def_def;
        }
        else{
            attack = att_spcstr;
            defense = def_spcdef;
        }
        
        return Math.round((((((((((2*att_lvl)/5+2)*attack*attackpower)/defense)/50)+2)*sametypebonus)*typemodifier)*(randomsolution+217))/255);
    }
    
    public static boolean calcEffect(int attacktype, int damagemodifier)
    {
        if(damagemodifier > 0)
        {
            if(attacktype == 4 || attacktype == 11)
            {
                if(Math.random() < 0.1)
                {
                    return true;
                }
            }
        }
        else
        {
            return false;
        }
        
        return true;
    }
    
    public static void calc(ScreenBattle sb, Pokemon p_my, Attack a_my, int id_my, Pokemon p_enemy)
    {
        boolean my_sametype = false;
        
        int my_damage = 0;
        int t = a_my.effecttype;
        boolean hit = calcHit(a_my.accuracy, sb.getModifierFloat(true, 5));
        System.out.println("=== Modifiers: ===");
        System.out.println("  atk(" + sb.getModifierFloat(false, 0) + ")");
        System.out.println("  def(" + sb.getModifierFloat(false, 1) + ")");
        System.out.println("  acc(" + sb.getModifierFloat(false, 5) + ")");
        if(hit && checkNormalDamage(t) == true)
        {
            if(p_my.type1 == a_my.damagetype || p_my.type2 == a_my.damagetype)
            {
                my_sametype = true;
            }
            my_damage = calcDamage(p_my.getLevel(), (float) p_my.getStrength() * (float) sb.getModifierFloat(true, 0), (float) p_my.getSpec_strength() * (float) sb.getModifierFloat(true, 2), a_my.damage, (float) p_enemy.getDefense() * (float) sb.getModifierFloat(false, 1), (float) p_enemy.getSpec_defense() * (float) sb.getModifierFloat(false, 3), my_sametype, p_enemy.type1, p_enemy.type2, a_my.effecttype, a_my.damagetype, a_my.physical, sb.getTypechart());
        }
        else if(hit && checkNormalDamage(t) == false)
        {
            
            if(t == 101 && sb.getModifier(false, 0) > -6)
            {
                sb.changeModifier(false, 0, -1);
                my_damage = 1;
            }
            else if(t == 102 && sb.getModifier(false, 1) > -6)
            {
                sb.changeModifier(false, 1, -1);
                my_damage = 1;
            }
            else if(t == 103 && sb.getModifier(false, 5) > -6)
            {
                sb.changeModifier(false, 5, -1);
                my_damage = 1;
            }
            else if(t == 104 && sb.getModifier(true, 1) < 6)
            {
                sb.changeModifier(true, 1, 1);
                my_damage = 1;
            }
        }
        else
        {
            my_damage = -1;
        }
        
        
        
        Attack a_enemy = enemyAttackCalc(p_enemy, sb.getPokedex());
        boolean enemy_sametype = false;
        
        int enemy_damage = 0;
        t = a_enemy.effecttype;
        hit = calcHit(a_my.accuracy, sb.getModifierFloat(false, 5));
        if(hit && checkNormalDamage(t) == true)
        {
            if(p_enemy.type1 == a_enemy.damagetype || p_enemy.type2 == a_enemy.damagetype)
            {
                enemy_sametype = true;
            }
            enemy_damage = calcDamage(p_enemy.getLevel(), p_enemy.getStrength() * (float) sb.getModifierFloat(false, 0), p_enemy.getSpec_strength() * (float) sb.getModifierFloat(false, 2), a_enemy.damage, p_my.getDefense() * (float) sb.getModifierFloat(true, 1), p_my.getSpec_defense() * (float) sb.getModifierFloat(true, 3), enemy_sametype, p_my.type1, p_my.type2, a_enemy.effecttype, a_enemy.damagetype, a_enemy.physical, sb.getTypechart());
        }
        else if(hit && checkNormalDamage(t) == false)
        {
            if(t == 101 && sb.getModifier(true, 0) > -6)
            {
                sb.changeModifier(true, 0, -1);
                enemy_damage = 1;
            }
            else if(t == 102 && sb.getModifier(true, 1) > -6)
            {
                sb.changeModifier(true, 1, -1);
                enemy_damage = 1;
            }
            else if(t == 103 && sb.getModifier(true, 5) > -6)
            {
                sb.changeModifier(true, 5, -1);
                enemy_damage = 1;
            }
            else if(t == 104 && sb.getModifier(false, 1) < 6)
            {
                sb.changeModifier(false, 1, 1);
                enemy_damage = 1;
            }
        }
        else
        {
            enemy_damage = -1;
        }
        
        int my_speed = (int) Math.round(a_my.speed * p_my.getSpeed() * sb.getModifierFloat(true, 4));
        int enemy_speed = (int) Math.round(a_enemy.speed * p_enemy.getSpeed() * sb.getModifierFloat(false, 4));
        
        if(my_speed == enemy_speed)
        {
            if(Math.random() > 0.5)
            {
                my_speed += 100;
            } else
            {
                enemy_speed += 100;
            }
        }
        
        sb.setAction(id_my, my_damage, a_my.name, a_my.effecttype, a_my.damagetype, my_speed);
        sb.setAction(-1, enemy_damage, a_enemy.name, a_enemy.effecttype, a_my.damagetype, enemy_speed);
    }
    
    public static Attack enemyAttackCalc(Pokemon pkmn, Pokedex pokedex)
    {
        Attack returnedAttack = pokedex.a0;
        float attacks = 0.25f;
        if( pkmn.at2 != pokedex.a0 ) attacks += 0.25f;
        if( pkmn.at3 != pokedex.a0 ) attacks += 0.25f;
        if( pkmn.at4 != pokedex.a0 ) attacks += 0.25f;
        
        Random random = new Random();
        float choice = attacks * random.nextFloat();
        
        if( choice <= 0.25f ) returnedAttack = pkmn.at1;
        else if( choice > 0.25f && choice <= 0.5f ) returnedAttack = pkmn.at2;
        else if( choice > 0.5f && choice <= 0.75f ) returnedAttack = pkmn.at3;
        else if( choice > 0.75f ) returnedAttack = pkmn.at4;
        
        if(returnedAttack.PP == 0 && (pkmn.at1.PP + pkmn.at2.PP + pkmn.at3.PP + pkmn.at4.PP != 0)) returnedAttack = enemyAttackCalc(pkmn, pokedex);
        else if(pkmn.at1.PP + pkmn.at2.PP + pkmn.at3.PP + pkmn.at4.PP == 0) returnedAttack = pokedex.a_struggle;
        return returnedAttack;
    }
    
    private static boolean checkNormalDamage(int t)
    {
        
        if(t != 101 && t != 102 && t != 103 && t != 104)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private static boolean calcHit(int acc, float modifier)
    {
        double chance = (double) modifier * acc;
        
        
        double random = Math.random() * 100;
        if(random < chance) return true;
        else return false;
    }
    
    
}

/*
 *  ((2A/5+2)*B*C)/D)/50)+2)*X)*Y/10)*Z)/255
 *  A = attacker's Level
 *  B = attacker's Attack or Special
 *  C = attack Power
 *  D = defender's Defense or Special
 *  X = same-Type attack bonus (1 or 1.5)
 *  Y = Type modifiers (40, 20, 10, 5, 2.5, or 0)
 *  Z = a random number between 217 and 255 
 */