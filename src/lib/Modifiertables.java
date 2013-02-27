/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gebruiker
 */
public class Modifiertables {
    Map<Integer, Float> atkdef_modifier_table;
    Map<Integer, Float> acc_modifier_table;
    
    public Modifiertables()
    {
        atkdef_modifier_table = new HashMap<>();
        acc_modifier_table = new HashMap<>();
        
        atkdef_modifier_table.put(-6, 0.25f);
        atkdef_modifier_table.put(-5, 0.28f);
        atkdef_modifier_table.put(-4, 0.33f);
        atkdef_modifier_table.put(-3, 0.40f);
        atkdef_modifier_table.put(-2, 0.50f);
        atkdef_modifier_table.put(-1, 0.66f);
        atkdef_modifier_table.put(0, 1f);
        atkdef_modifier_table.put(1, 1.5f);
        atkdef_modifier_table.put(2, 2f);
        atkdef_modifier_table.put(3, 2.5f);
        atkdef_modifier_table.put(4, 3f);
        atkdef_modifier_table.put(5, 3.5f);
        atkdef_modifier_table.put(6, 4f);
        
        acc_modifier_table.put(-6, (float) (3.0f/9.0f));
        acc_modifier_table.put(-5, (float) (3.0f/8.0f));
        acc_modifier_table.put(-4, (float) (3.0f/7.0f));
        acc_modifier_table.put(-3, (float) (3.0f/6.0f));
        acc_modifier_table.put(-2, (float) (3.0f/5.0f));
        acc_modifier_table.put(-1, (float) (3.0f/4.0f));
        acc_modifier_table.put(0, (float) (3.0f/3.0f));
        acc_modifier_table.put(1, (float) (4.0f/3.0f));
        acc_modifier_table.put(2, (float) (5.0f/3.0f));
        acc_modifier_table.put(3, (float) (6.0f/3.0f));
        acc_modifier_table.put(4, (float) (7.0f/3.0f));
        acc_modifier_table.put(5, (float) (8.0f/3.0f));
        acc_modifier_table.put(6, (float) (9.0f/3.0f));
    }
    
    /**
     * if(what) attack / defense modifier
     * else accuracy modifier
     * 
     * @param what
     * @param how
     * @return 
     */
    public float getModifier(boolean what, int how)
    {
        float returned = 0f;
        if(what)
        {
            returned = atkdef_modifier_table.get(how);
        }
        else
        {
            returned = acc_modifier_table.get(how);
        }
        
        return returned;
    }
}
