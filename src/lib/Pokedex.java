/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;


/**
 * <b>In the Pokedex, all information about Pokemon and attacks are
 * stored.</b><br />
 * <br />
 * Pokemon:<br />
 * 0 - Missingno<br />
 * 20 - Raticate<br />
 * 25 - Pikachu<br />
 * 41 - Zubat<br />
 * 129 - Magikarp<br />
 * 130 - Gyarados<br />
 * 133 - Eevee<br />
 * 143 - Snorlax<br />
 * <br />
 * Attacks:<br />
 * 0 - empty<br />
 * 1 - tackle<br />
 * 4 - growl<br />
 * 5 - tail whip<br />
 * 7 - sand attack<br />
 * 8 - defense curl<br />
 * 15 - quick attack<br />
 * 20 - rest<br />
 * 25 - splash<br />
 * 30 - super fang<br />
 * 50 - hyper beam<br />
 * 53 - leech life<br />
 * 60 - thundershock<br />
 * 72 - water gun<br />
 * 
 * @author Gebruiker
 * 
 * 
 */
public class Pokedex {
    public Pokemon[] dex = new Pokemon[201];
    public Attack[] atk = new Attack[101];
    
    public Pokemon missingno = new Pokemon(0, "Missingno", 1, 0);
    
    public Pokemon charmander = new Pokemon(4, "Charmander", 2, 0);
    public Pokemon squirtle = new Pokemon(7, "Squirtle", 3, 0);
    public Pokemon raticate = new Pokemon(20, "Raticate", 1, 0);
    public Pokemon pikachu = new Pokemon(25, "Pikachu", 4, 0);
    public Pokemon zubat = new Pokemon(41, "Zubat", 8, 10);
    public Pokemon magikarp = new Pokemon(129, "Magikarp", 3, 0);
    public Pokemon gyarados = new Pokemon(130, "Gyarados", 3, 6);
    public Pokemon eevee = new Pokemon(133, "Eevee", 1, 0);
    public Pokemon snorlax = new Pokemon (143, "Snorlax", 1, 0);
    public Pokemon misdreavus = new Pokemon (200, "Misdreavus", 14, 0);
    
    public Attack a0 = new Attack(0, 0, 0, 0, 0, 0, false, " ");
    public Attack a_struggle = new Attack(99999, 100, 50, 10, 1, -1, true, "Struggle"); // -1
    public Attack a_tackle = new Attack(35, 95, 50, 35, 1, 1, true, "Tackle"); // 1
    public Attack a_growl = new Attack(40, 100, 50, 10, 1, 101, false, "Growl"); // 4
    public Attack a_tailwhip = new Attack(30, 100, 50, 10, 1, 102, false, "Tail Whip"); // 5
    public Attack a_sandattack = new Attack(15, 100, 50, 10, 9, 103, false, "Sand Attack"); // 7
    public Attack a_defensecurl = new Attack(40, 1000, 50, 10, 1, 104, false, "Defense Curl"); // 8
    public Attack a_quickattack = new Attack(30, 100, 100, 40, 1, 1, true, "Quick Attack"); // 15
    public Attack a_rest = new Attack(10, 2000, 150, 0, 1, 100, false, "Rest"); // 20
    public Attack a_splash = new Attack(40, 2000, 50, 0, 3, 0, false, "Splash"); // 25
    public Attack a_superfang = new Attack(10, 90, 50, 50, 1, 30, true, "Super Fang"); // 30
    public Attack a_hyperbeam = new Attack(5, 90, 50, 150, 1, 50, false, "Hyper Beam"); // 50
    public Attack a_leechlife = new Attack(15, 100, 50, 20, 12, 53, true, "Leech Life"); // 53
    public Attack a_thundershock = new Attack(30, 100, 50, 40, 4, 4, false, "Thundershock"); // 60
    public Attack a_watergun = new Attack(25, 100, 50, 40, 3, 1, false, "Water Gun"); // 72
    public Attack a_psybeam = new Attack(20, 100, 50, 65, 11, 11, false, "Psybeam"); // 90
    public Attack a_skyuppercut = new Attack(15, 85, 50, 90, 7, 1, true, "Sky Uppercut"); // 93
    public Attack a_rockthrow = new Attack(15, 90, 50, 50, 13, 1, false, "Rock Throw"); // 100
    
    public Pokedex()
    {
        dex[0] = missingno;
        dex[4] = charmander;
        dex[7] = squirtle;
        dex[20] = raticate;
        dex[25] = pikachu;
        dex[41] = zubat;
        dex[129] = magikarp;
        dex[130] = gyarados;
        dex[133] = eevee;
        dex[143] = snorlax;
        dex[200] = misdreavus;
        
        atk[0] = a0;
        atk[1] = a_tackle;
        atk[4] = a_growl;
        atk[5] = a_tailwhip;
        atk[7] = a_sandattack;
        atk[8] = a_defensecurl;
        atk[15] = a_quickattack;
        atk[20] = a_rest;
        atk[25] = a_splash;
        atk[30] = a_superfang;
        atk[50] = a_hyperbeam;
        atk[53] = a_leechlife;
        atk[60] = a_thundershock;
        atk[72] = a_watergun;
        atk[90] = a_psybeam;
        atk[93] = a_skyuppercut;
        atk[100] = a_rockthrow;
    }
    
}
/*
     * Attack Type 
     * 0 = Splash 
     * 1 = Normal
     * 2 = Fire
     * 3 = Water
     * 4 = Electric
     * 5 = Grass
     * 6 = Ice
     * 7 = Fighting
     * 8 = Poison
     * 9 = Ground
     * 10 = Flying
     * 11 = Psychic
     * 12 = Bug
     * 13 = Rock
     * 14 = Ghost
     * 15 = Dragon
     * 16 = Dark
     * 17 = Steel
     * 
     * Effect Type
     * 1 - Normal hit
     * 4 - Chance of paralyzing on hit
     * 11 - Chance of confusing on hit
     * 30 - Kill a % of enemy's health
     * 50 - Recharge after using attack
     * 53 - Suck enemy's health
     * 100 - Rest
     * 101 - Lower attack
     * 102 - Lower defense
     * 103 - Lower accuracy
     * 104 - Up own defense
     * 
     * 
     * 99 = Attack by %
     * 100 = Rest
     * 101 = Strength- 102 = Defense-
     */