/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.util.Random;
import lib.Attack;
import lib.Database;
import lib.Pokedex;
import lib.Pokemon;
import lib.Stats;

/**
 *
 * @author Gebruiker
 */
public class BattleCalculation {
    ScreenBattle sb;
    boolean local = true;
    
    Pokedex pokedex = new Pokedex();
    
    public BattleCalculation(ScreenBattle sb)
    {
        this.sb = sb;
    }
    
    public void attack(Attack a, boolean local)
    {
        this.local = local;
        
        Pokemon[] pokemon = new Pokemon[2];
        pokemon = sb.getPkmn();
        
        if( local == true )
        {
            enemyAttackCalc(pokemon[1]);
        }
    }
    
    public Attack enemyAttackCalc(Pokemon pkmn)
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
        
        if(returnedAttack.PP == 0 && (pkmn.at1.PP + pkmn.at2.PP + pkmn.at3.PP + pkmn.at4.PP != 0)) returnedAttack = enemyAttackCalc(pkmn);
        else if(pkmn.at1.PP + pkmn.at2.PP + pkmn.at3.PP + pkmn.at4.PP == 0) returnedAttack = pokedex.a_struggle;
        return returnedAttack;
    }
}
