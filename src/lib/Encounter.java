/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

/**
 *
 * @author Gebruiker
 */
public class Encounter {
    Pokemon pokemon;
    float chance;
    
    public Encounter(Pokemon pokemon, float chance)
    {
        this.pokemon = pokemon;
        this.chance = chance;
    }
}
