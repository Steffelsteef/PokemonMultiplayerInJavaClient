/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import lib.Attack;
import lib.Database;
import lib.Modifiertables;
import lib.Pokedex;
import lib.Pokemon;
import lib.Stats;
import lib.WildBattleCalcs;
import ser.PokeDataPackage;

/**
 *
 * @author Gebruiker
 */
public class ScreenBattle extends JFrame{
    float[][] typechart;
    boolean local = true;
    boolean actionFinished = false;
    synchronized boolean actionFinished()
    {
        return actionFinished;
    };
    public void actionFinish(boolean what)
    {
        actionFinished = what;
    }
    Pokedex pokedex = new Pokedex();
    BattleCalculation bc = new BattleCalculation(this);
    BufferedImage bg;
    BufferedImage[] portrait;
    BufferedImage[] current;
    BufferedImage[] myfronts;
    TextPanel textPanel = new TextPanel();
    JButton button_TL, button_TR, button_BL, button_BR;
    ArrayList<JButton> choosePokemon;
    
    BattleActionAttack bat_me = null;
    BattleActionAttack bat_enemy = null;
    String infoString = "Please wait!";
    
    BufferedImage playerNameplate, enemyNameplate;
    
    Pokemon[] my_party = new Pokemon[6];
    Pokemon[] e_party = new Pokemon[6];
    Pokemon my_current, en_current;
    
    int other_id = 0;
    
    ScreenOverworld scrOv;
    
    Font font, namePlates, nameShadow;
    
    public int screentype = 0;
    
    Insets insets;
    
    int[] trainerModValues = new int[7];
    int[] wildModValues = new int[7];
    
    Modifiertables modifiertables = new Modifiertables();
    
    private boolean wildreceived = false;
    public boolean wildreceived() {
        return wildreceived;
    }
    public void setWildreceived(Pokemon wild) {
        en_current = wild;
        wildreceived = true;
    }
    
    private boolean expreceived = false;
    public boolean expreceived() {
        return expreceived;
    }
    public void setExpreceived(boolean to) {
        expreceived = to;
    }
    int exp = 0;
    
    public ScreenBattle( ScreenOverworld so, Pokemon[] my_pdp, PokeDataPackage e_pdp, int bg_code )
    {
        setTypechart();
        resetModifiers(true);
        resetModifiers(false);
        
        this.scrOv = so;
        so.characterIsBusy = true;
        so.battle = true;
        this.other_id = -1;
        //so.pmc.update(true);
        
        so.player.leftpressed = false;
        so.player.rightpressed = false;
        so.player.downpressed = false;
        so.player.uppressed = false;
        
        local = true;
        
        System.out.println("my_pdp name");
        System.out.println(my_pdp[0].name);
        setupScreen( bg_code, my_pdp, e_pdp );
        
    }
    
    
    public ScreenBattle(ScreenOverworld so, String username_enemy, int bg_code, Pokemon[] my_pdp, PokeDataPackage e_pdp, int type){ // type; 1=wild, 2=trainer, 3=gym, 4=player
        this.scrOv = so;
        so.characterIsBusy = true;
        so.battle = true;
        this.other_id = e_pdp.id;
        //so.pmc.update(true);
        
        so.player.leftpressed = false;
        so.player.rightpressed = false;
        so.player.downpressed = false;
        so.player.uppressed = false;
        
        local = false;
        
        setupScreen(bg_code, my_pdp, e_pdp);
        
        
    }
    
    public void setupScreen(int bg_code, Pokemon[] my_pdp, PokeDataPackage e_pdp)
    {
        portrait = new BufferedImage[12];
        current = new BufferedImage[2];
        myfronts = new BufferedImage[6];
        try {
            playerNameplate = ImageIO.read(getClass().getResource("/img/battle/playerNameplate.png"));
            enemyNameplate = ImageIO.read(getClass().getResource("/img/battle/enemyNameplate.png"));
        } catch (IOException ex) { ex.printStackTrace(); }
        

        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                scrOv.characterIsBusy = false;
                scrOv.battle = false;
                //scrOv.pmc.update(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                scrOv.characterIsBusy = false;
                scrOv.battle = false;
                //scrOv.pmc.update(false);
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
//        if (my_pdp == null){
//            my_pdp = new PokeDataPackage();
//            my_pdp.pokemon[0] = new Pokemon(pokedex.pikachu, "Sparkie", pokedex.a_watergun, pokedex.a_thundershock, pokedex.a0, pokedex.a0, new Stats(29, 32, 40, 65, 120, 120), 0);
//        }
        
        setContentPane(new JPanel() {
            Color greenA = new Color(88,208,128);
            Color greenB = new Color(112,248,168);
            Color yellowA = new Color(175,154,39);
            Color yellowB = new Color(244,221,47);
            Color redA = new Color(166,76,67);
            Color redB = new Color(233,96,77);
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            
                g2d.drawImage(bg, 0, 0, null);
                g2d.setFont(font);
                
                g2d.drawImage(enemyNameplate, 64, 32, null);
                g2d.drawImage(playerNameplate, 480, 277, null);
                
                g2d.drawImage(portrait[my_current.getLocation() - 1], 112, 196, null); // me
                g2d.drawImage(portrait[en_current.getLocation() + 5], 512, 32, null); // enemy
                
                g2d.drawImage(textPanel.topLeft, -12, 368 - 12, null);
                
                g2d.setFont(namePlates);
                
                float e_health = ((float)en_current.getHp() / (float)en_current.getMaxhp());
                float p_health = ((float)my_current.getHp() / (float)my_current.getMaxhp());
                if(e_health > 0.5){
                    g2d.setColor(greenA);
                } else if(e_health > 0.2 && e_health <= 0.5){
                    g2d.setColor(yellowA);
                } else if(e_health <= 0.2){
                    g2d.setColor(redA);
                }
                g2d.fillRect(181, 83, Math.round(144 * e_health), 2);
                if(p_health > 0.5){
                    g2d.setColor(greenA);
                } else if(p_health > 0.2 && p_health <= 0.5){
                    g2d.setColor(yellowA);
                } else if(p_health <= 0.2){
                    g2d.setColor(redA);
                }
                g2d.fillRect(624, 328, Math.round(144 * p_health), 2);
                if(e_health > 0.5){
                    g2d.setColor(greenB);
                } else if(e_health > 0.2 && e_health <= 0.5){
                    g2d.setColor(yellowB);
                } else if(e_health <= 0.2){
                    g2d.setColor(redB);
                }
                g2d.fillRect(181, 85, Math.round(144 * e_health), 7);
                if(p_health > 0.5){
                    g2d.setColor(greenB);
                } else if(p_health > 0.2 && p_health <= 0.5){
                    g2d.setColor(yellowB);
                } else if(p_health <= 0.2){
                    g2d.setColor(redB);
                }
                g2d.fillRect(624, 330, Math.round(144 * p_health), 7);


                
                g2d.setColor(new Color(216, 208, 176));
                g2d.drawString(en_current.nickname, 96+2, 32+38+2);
                g2d.setColor(new Color(64, 64, 64));
                g2d.drawString(en_current.nickname, 96, 32+38);
                g2d.setColor(new Color(216, 208, 176));
                g2d.drawString("Lv " + en_current.getLevel(), 256+2, 32+38+2);
                g2d.setColor(new Color(64, 64, 64));
                g2d.drawString("Lv " + en_current.getLevel(), 256, 32+38);
                
                g2d.setColor(new Color(216, 208, 176));
                g2d.drawString(my_current.nickname, 530+2, 315+2);
                g2d.setColor(new Color(64, 64, 64));
                g2d.drawString(my_current.nickname, 530, 315);
                g2d.setColor(new Color(216, 208, 176));
                g2d.drawString("Lv " + my_current.getLevel(), 690+2, 315+2);
                g2d.setColor(new Color(64, 64, 64));
                g2d.drawString("Lv " + my_current.getLevel(), 690, 315);
                
                g2d.drawString("Exp " + my_current.exp + " / " + my_current.getExpneeded(), 690, 215);
                
                g2d.setFont(font); // 181 83
                g2d.setColor(Color.BLACK);
                if(screentype == 1 || screentype == 2)
                {
                    g2d.drawString("I choose you, " + my_current.nickname + "!", 384, 450);
                    g2d.drawString("A " + my_current.name + ", lvl " + my_current.getLevel() + " and " + my_current.getHp() + "/" + my_current.getMaxhp() + " hp", 384, 480);
                } else if(screentype == 10)
                {
                    g2d.drawString(infoString, 384, 450);
                }
                
                int exp = Math.round(((float)my_current.exp / (float)my_current.getExpneeded()) * 192);
                
                g2d.setColor(new Color(91, 197, 237));
                g2d.fillRect(576, 376, exp, 4);
                
                g2d.setColor(new Color(57, 144, 171));
                g2d.fillRect(576, 380, exp, 2);
                //576 en 376
                
                if(screentype == 2)
                {
                    g2d.setColor(new Color(70,70,70));
                    g2d.fillRect(0, 0, 832, 550);
                    g2d.setFont(namePlates);
                    int x = 32, y = 16, i = 0;
                    while(my_party[i] != null)
                    {
                        g2d.setColor(Color.WHITE);
                        String name = my_party[i].name + " Lv. " + my_party[i].getLevel();
                        int nameLength = (int)g2d.getFontMetrics().getStringBounds(name, g2d).getWidth() / 2;
                        g2d.drawString(name, x + 96 - nameLength, y + 224);
                        g2d.drawImage(myfronts[i], x, y, null);
                        
                        int hp = Math.round(192 * (float) my_party[i].getHp() / (float) my_party[i].getMaxhp());
                        g2d.setColor(new Color(100, 100, 100));
                        g2d.fillRect(x, y + 256, 192, 4);
                        g2d.setColor(new Color(120, 120, 120));
                        g2d.fillRect(x, y + 260, 192, 2);
                        
                        if(hp > 0.5 * 192){
                            g2d.setColor(greenA);
                        } else if(hp > 0.2 * 192 && hp <= 0.5 * 192){
                            g2d.setColor(yellowA);
                        } else if(hp <= 0.2 * 192){
                            g2d.setColor(redA);
                        }
                        g2d.fillRect(x, y + 256, hp, 4);
                        
                        if(hp > 0.5 * 192){
                            g2d.setColor(greenB);
                        } else if(hp > 0.2 * 192 && p_health <= 0.5 * 192){
                            g2d.setColor(yellowB);
                        } else if(hp <= 0.2 * 192){
                            g2d.setColor(redB);
                        }
                        g2d.fillRect(x, y + 260, hp, 2);
                        
                        
                        x += 256;
                        i++;
                        if(i == 3)
                        {
                            y += 256;
                            x -= 736;
                        }
                        if(i == 6)
                        {
                            break;
                        }
                    }
                }
                
                repaint();
            }
        });
        
        System.out.println("===============================");
        System.out.println("My pokemon list:");
        for(int i = 0; i < 6; i++){
            if(my_pdp[i] != null)
            {
                my_party[i] = my_pdp[i];
                System.out.println(i + " - " + my_pdp[i].nickname + " (a " + my_pdp[i].name + ") lvl " + my_pdp[i].getLevel());
                
                try {
                    portrait[i] = ImageIO.read(getClass().getResource("/img/pkmn/" + my_pdp[i].number + "b.png"));
                    myfronts[i] = ImageIO.read(getClass().getResource("/img/pkmn/" + my_pdp[i].number + "f.png"));
                } catch (IOException ex) {
                    Logger.getLogger(ScreenBattle.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        my_current = my_party[0];
        System.out.println("===============================");
        
        if( e_pdp != null )
        {
            System.out.println("Enemy's pokemon list:");
            for(int i = 0; i < e_pdp.size; i++)
            {
                e_party[i] = new Pokemon(pokedex.dex[e_pdp.pokenr[i]], e_pdp.nickname[i], pokedex.atk[e_pdp.atk1[i]], pokedex.atk[e_pdp.atk2[i]], pokedex.atk[e_pdp.atk3[i]], pokedex.atk[e_pdp.atk4[i]], new Stats(e_pdp.lvl[i], 0, 0, 0, 0, 0, e_pdp.maxhp[i], e_pdp.hp[i]), 0, 0);
                if(local)
                {
                    e_party[i].setStrength(e_pdp.strength[i]);
                    e_party[i].setDefense(e_pdp.defense[i]);
                    e_party[i].setSpec_strength(e_pdp.special_strength[i]);
                    e_party[i].setSpec_defense(e_pdp.special_defense[i]);
                    e_party[i].setSpeed(e_pdp.speed[i]);
                }
                e_party[i].setLocation(i+1);
                
                System.out.println(i + " - " + e_party[i].nickname + " (a " + e_party[i].name + ") lvl " + e_party[i].getLevel());
                try {
                    portrait[i + 6] = ImageIO.read(getClass().getResource("/img/pkmn/" + e_party[i].number + "f.png"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
            
            }
            en_current = e_party[0];
        }
        else
        {
            infoString = "Wild Pokémon appeared! (Please wait for server)";
            scrOv.pmc.sendTCP("cb:r");
            
            while(wildreceived() == false) {}
            
            Pokemon wild_pokemon = en_current;
            //Pokemon wild_pokemon = scrOv.mr.db.getWildEncounter(scrOv.map.getCurrentMapname());
            System.out.println("Wild pokemon: " + wild_pokemon.name + ", lvl " + wild_pokemon.getLevel() + ", hp " + wild_pokemon.getHp() + "/" + wild_pokemon.getMaxhp());
            
            try {
                portrait[6] = ImageIO.read(getClass().getResource("/img/pkmn/" + wild_pokemon.number + "f.png"));
            } catch (IOException ex) {
                System.err.println("Error loading wild Pokemon's portrait.");
            }
            System.out.println("===============================");
        }
        
        
        try {
            this.bg = ImageIO.read(getClass().getResource("/img/battle/bg_" + bg_code + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(ScreenBattle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
        current[0] = portrait[0];
        current[1] = portrait[6];
//        try {
//            TL = ImageIO.read(getClass().getResource("/img/window/windowTopLeft.png"));
//        } catch (IOException ex) {
//            Logger.getLogger(Battle.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        try {
            File fontfile = null;
            InputStream in = getClass().getResourceAsStream("/font/rusa.ttf");
            Font fontje;

            fontje = Font.createFont(Font.TRUETYPE_FONT, in);
            this.font = fontje.deriveFont(Font.PLAIN, 14);
            this.namePlates = fontje.deriveFont(Font.BOLD, 26);

            
        } catch (Exception e){
            e.printStackTrace();
        }
        
        if(my_current.nickname.trim().isEmpty())
        {
            my_current.nickname = my_current.name;
        }
        
        if(en_current.nickname.trim().isEmpty())
        {
            en_current.nickname = en_current.name;
        }
        
        button_TL = new JButton();
        button_TL.setText("Fight");
        button_TL.setBounds(0,400,128,32);
        button_TL.addActionListener(buttonlistener1);
        add(button_TL);
        button_TR = new JButton();
        button_TR.setText("Pkmn");
        button_TR.setBounds(128,400,128,32);
        button_TR.addActionListener(buttonlistener2);
        add(button_TR);
        button_BL = new JButton();
        button_BL.setText("Items");
        button_BL.setBounds(0,432,128,32);
        button_BL.addActionListener(buttonlistener3);
        add(button_BL);
        button_BR = new JButton();
        button_BR.setText("Run");
        button_BR.setBounds(128,432,128,32);
        button_BR.addActionListener(buttonlistener4);
        add(button_BR);
        
        setLayout(null);
        pack();
        insets = getInsets();
        setSize(832 + insets.left + insets.right, 550 + insets.top + insets.bottom);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        requestFocusInWindow();
    }
    
    
    ActionListener buttonlistener1 = new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            if(screentype == 0) // *Fight*, Pkmn, Items, Run
            {
                //bc.attack(my_current.at1, local);
//                if(local == true)
//                {
//                    scrOv.pmc.sendTCP("cb:c");
//                    JOptionPane.showMessageDialog(new JPanel(), "Wild battles don't work yet! As a comfort, you have caught this Pokémon now.", "Oh!", JOptionPane.INFORMATION_MESSAGE);
//                    JOptionPane.showMessageDialog(new JPanel(), "Not that it will be of any use, since you can't choose other Pokémon yet..", "D'oh!", JOptionPane.INFORMATION_MESSAGE);
//                    scrOv.pmc.screenBattle.dispose();
//                }
//                else
//                {
                    setScreentype(1);
//                }
                
                
            }
            else if(screentype == 1) // *a1*, a2, a3, a4
            {
                System.out.println("Attack 1 chosen");
                actionChosen(1, 1);
                setScreentype(10);
            }
        }
        
    };
    ActionListener buttonlistener2 = new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            if(screentype == 0) // Fight, *Pkmn*, Items, Run
            { 
                setScreentype(2);
//                int i = 0;
//                while(my_party[i] != null && i < 6)
//                {
//                    if(my_party[i] == my_current)
//                    {
//                        System.out.println(" > " + my_party[i].nickname + ", level " + my_party[i].getLevel());
//                    }
//                    else
//                    {
//                        System.out.println("   " + my_party[i].nickname + ", level " + my_party[i].getLevel());
//                    }
//                    i++;
//                }
//                if(my_party[0] == my_current)
//                {
//                    my_current = my_party[1];
//                }
//                else
//                {
//                    my_current = my_party[0];
//                }
            }
            if(screentype == 1){ // a1, *a2*, a3, a4
                System.out.println("Attack 2 chosen");
                actionChosen(1, 2);
                setScreentype(10);
            }
        }
        
    };
    ActionListener buttonlistener3 = new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            if(screentype == 0){ // Fight, Pkmn, *Items*, Run
                System.out.println("Going to choose items!");
                
            }
            if(screentype == 1){ // a1, *a2*, a3, a4
                System.out.println("Attack 3 chosen");
                actionChosen(1, 3);
                setScreentype(10);
            }
        }
        
    };
    ActionListener buttonlistener4 = new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            if(screentype == 0){ // Fight, *Pkmn*, Items, Run
                scrOv.pmc.sendTCP("cb:c:" + en_current.getHp());
                System.out.println("Disposing.");
                ScreenBattle.this.dispose();
            }
            if(screentype == 1){ // a1, *a2*, a3, a4
                System.out.println("Attack 4 chosen");
                actionChosen(1, 4);
                setScreentype(10);
            }
        }
        
    };
    
    public void setAction(int user, int damage, String attack_name, int attack_type, int damage_type, int speed)
    {
        
        if(user == scrOv.pmc.my_id)
        {
            bat_me = new BattleActionAttack(damage, attack_name, my_current.name, attack_type, damage_type, speed);
        }
        else if(user == other_id)
        {
            bat_enemy = new BattleActionAttack(damage, attack_name, "Enemy " + en_current.name, attack_type, damage_type, speed);
        }
        
        if(bat_me != null && bat_enemy != null)
        {
            new Thread(){

                @Override
                @SuppressWarnings("empty-statement")
                public void run() {
                    BattleActionAttack bat_first = null, bat_last = null;
                    boolean mefirst = true;
                    
                    if(bat_me.getSpeed() > bat_enemy.getSpeed())
                    {
                        bat_first = bat_me;
                        bat_last = bat_enemy;
                        mefirst = true;
                    }
                    else if(bat_me.getSpeed() < bat_enemy.getSpeed())
                    {
                        bat_first = bat_enemy;
                        bat_last = bat_me;
                        mefirst = false;
                    }
                    
                    if(mefirst) doAction(bat_first, true);
                    else doAction(bat_first, false);
                    
                    while(!actionFinished())
                    {
                        
                    }
                    actionFinish(false);
                    
                    int checkDefeat = checkDefeat();
                    if(checkDefeat == 0)
                    {
                        if(mefirst) doAction(bat_last, false);
                        else doAction(bat_last, true);
                        
                        bat_me = null;
                        bat_enemy = null;
                        bat_first = null;
                        bat_last = null;

                        while(!actionFinished()){
                            
                        }
                        actionFinish(false);
                        checkDefeat = checkDefeat();
                        if(checkDefeat == 0)
                        {
                            setScreentype(0);
                        }
                        else if(checkDefeat == 1) {
                            handleDefeat(this);
                        }

                        
                    }
                    else if(checkDefeat == 1) {
                        handleDefeat(this);
                    }
                    
                    
                    
                    
                }
            }.start();
        }
    }
    
    public void setScreentype(int type){
        screentype = type;
        if(type == 0) // main screen
        { 
            infoString = "Please wait!";
            button_TL.setVisible(true);
            button_TR.setVisible(true);
            button_BL.setVisible(true);
            button_BR.setVisible(true);
            button_TL.setText("Fight");
            button_TR.setText("Pkmn");
            button_BL.setText("Items");
            button_BR.setText("Run");
            
            for(int i = 0; i < choosePokemon.size(); i++)
            {
                remove(choosePokemon.get(i));
            }
        } 
        else if(type == 2)
        {
            button_TL.setVisible(false);
            button_TR.setVisible(false);
            button_BL.setVisible(false);
            button_BR.setVisible(false);
            choosePokemon = new ArrayList<>();
            int x = 32, y = 16;
            for (int i = 0; i < 6; i++){
                System.out.println("Creating button " + i);
                final int j = i;
                choosePokemon.add(new JButton());
                ImageIcon ii = new ImageIcon(myfronts[i]);
                choosePokemon.get(i).setIcon(ii);
                choosePokemon.get(i).setBounds(x, y, 192, 192);
                choosePokemon.get(i).setVisible(true);
                choosePokemon.get(i).setEnabled(true);
                choosePokemon.get(i).setBorderPainted(false); 
                choosePokemon.get(i).setContentAreaFilled(false); 
                choosePokemon.get(i).setFocusPainted(false); 
                choosePokemon.get(i).setOpaque(false); 

                choosePokemon.get(i).addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        my_current = my_party[j];
                        setScreentype(0);
                    }
                });

                add(choosePokemon.get(i));
                
                x += 256;
                if(i == 3)
                {
                    y += 256;
                    x -= 736;
                }
                if(i == 6)
                {
                    break;
                }
            }
        }
        else if(type == 1) // attack screen
        {
            button_TL.setText(my_current.at1.name);
            button_TR.setText(my_current.at2.name);
            button_BL.setText(my_current.at3.name);
            button_BR.setText(my_current.at4.name);
            
            if(my_current.at1.PP == 0) button_TL.setEnabled(false);
            if(my_current.at2.PP == 0) button_TR.setEnabled(false);
            if(my_current.at3.PP == 0) button_BL.setEnabled(false);
            if(my_current.at4.PP == 0) button_BR.setEnabled(false);
        }
        else if(type == 10) // please wait
        {
            button_TL.setVisible(false);
            button_TR.setVisible(false);
            button_BL.setVisible(false);
            button_BR.setVisible(false);
        }
    }
    
    private int checkDefeat()
    {
        System.out.println("Checking defeat...");
        if(en_current.getHp() <= 0)
        {
            return 1;
        }
        else if(my_current.getHp() <= 0)
        {
            JOptionPane.showMessageDialog(this, "You have lost! :(", "Oh no!", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return 2;
        }
        
        return 0;
    }
    
    private void handleDefeat(Thread thr)
    {
        infoString = "You have defeated " + en_current.name + "!";
        int id_enemy_loc = 1;
        scrOv.pmc.sendTCP("chd:" + other_id + ":" + id_enemy_loc );
        try {
            thr.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ScreenBattle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(expreceived() == false);
        if(my_current.getLevel() < 100)
        {
            infoString = "Received " + exp + " EXP. points!";
            int myexpnow = my_current.exp;


            if (myexpnow + exp < my_current.getExpneeded())
            {
                my_current.exp += exp;
            }
            else
            {
                my_current.exp = (my_current.exp + exp) - my_current.getExpneeded();
                my_current.setLevel(my_current.getLevel() + 1);

                my_current.setExpneeded();
                try {
                    thr.sleep(1300);
                    infoString = " ";
                    thr.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ScreenBattle.class.getName()).log(Level.SEVERE, null, ex);
                }

                infoString = my_current.name + " is now level " + my_current.getLevel() + "!";
            }

            scrOv.pmc.my_pokemon_party[0].exp = my_current.exp;
            scrOv.pmc.my_pokemon_party[0].setLevel(my_current.getLevel());
            
            scrOv.pmc.my_pokemon_party[0].setExpneeded();

            try {
                thr.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ScreenBattle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
        JOptionPane.showMessageDialog(this, "You have won! :D", "Yay!", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    private void actionChosen(int action, int choice)
    {
        if(!local) {
            System.out.println("sendTCP(" + "cac:" + scrOv.pmc.my_id + ":" + other_id + ":1:" + action + ":" + choice + ")");
            scrOv.pmc.sendTCP("cac:" + scrOv.pmc.my_id + ":" + other_id + ":1:" + action + ":" + choice);
        } else {
            Attack choice_attack = null;
            if(choice == 1) choice_attack = my_current.at1;
            else if(choice == 2) choice_attack = my_current.at2;
            else if(choice == 3) choice_attack = my_current.at3;
            else if(choice == 4) choice_attack = my_current.at4;
            
            WildBattleCalcs.calc(this, my_current, choice_attack, scrOv.pmc.my_id, en_current);
//            Attack choice_attack = null;
//            if(choice == 1) choice_attack = my_current.at1;
//            else if(choice == 2) choice_attack = my_current.at2;
//            else if(choice == 3) choice_attack = my_current.at3;
//            else if(choice == 4) choice_attack = my_current.at4;
//            
//            int att_lvl = my_current.getLevel();
//            int att_str = my_current.getStrength();
//            int att_spcstr = my_current.getSpec_strength();
//            int attackpower = choice_attack.damage;
//            int def_def = en_current.getDefense();
//            int def_spcdef = en_current.getSpec_defense();
//            boolean sametype = false;
//            int def_type1 = en_current.type1;
//            int def_type2 = en_current.type2;
//            int attacktype = choice_attack.damagetype;
//            int speed = my_current.getSpeed() * pokedex.atk[choice].speed;
//            int effect = choice_attack.effecttype;
//            
//            if(my_current.type1 == attacktype || my_current.type2 == attacktype) sametype = true;
//            System.out.println("my sametype: " + sametype);
//            
//            int my_damage = lib.WildBattleCalcs.calcDamage(att_lvl, att_str, att_spcstr, attackpower, def_def, def_spcdef, sametype, def_type1, def_type2, attacktype);
//            setAction(scrOv.pmc.my_id, my_damage, choice_attack.name, effect, speed);
//            
//            Attack enemy_attack = bc.enemyAttackCalc(en_current);
//            
//            att_lvl = en_current.getLevel();
//            att_str = en_current.getStrength();
//            att_spcstr = en_current.getSpec_strength();
//            attackpower = enemy_attack.damage;
//            def_def = my_current.getDefense();
//            def_spcdef = my_current.getSpec_defense();
//            sametype = false;
//            def_type1 = my_current.type1;
//            def_type2 = my_current.type2;
//            attacktype = enemy_attack.damagetype;
//            speed = en_current.getSpeed() * enemy_attack.speed;
//            effect = enemy_attack.effecttype;
//            
//            if(en_current.type1 == attacktype || en_current.type2 == attacktype) sametype = true;
//            System.out.println("enemy sametype: " + sametype);
//            
//            int e_damage = lib.WildBattleCalcs.calcDamage(att_lvl, att_str, att_spcstr, attackpower, def_def, def_spcdef, sametype, def_type1, def_type2, attacktype);
//            
//            setAction(0, e_damage, enemy_attack.name, effect, speed);
        }
    }
    
    public Pokemon[] getPkmn()
    {
        Pokemon[] pkmn = new Pokemon[2];
        pkmn[0] = my_current;
        pkmn[1] = en_current;
        
        return pkmn;
    }
    
    private void doAction(final BattleActionAttack action, final boolean me)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                String username = "", othername = "";
                if(me)
                {
                    username = action.getPokemon();
                    othername = en_current.name;
                } else
                {
                    username = action.getPokemon();
                    othername = my_current.name;
                }
                infoString = username + " used " + action.getName() + "!";
                System.out.println("===\nAttack info:\nName: " + action.getName() + "\nPokemon:" + username + "\nType:" + action.getAttackType() + "\nDamage:" + action.getDamage() + "\n===");
                System.out.println("HP's e(" + en_current.getHp() + ") / m(" + my_current.getHp() + ")");

                int a_type = action.getAttackType();
                if(a_type == 1 || a_type == 4 || a_type == 11 || a_type == 53)
                {
                    if(action.getDamage() != -1)
                    {
                        if(me)
                        {
                            en_current.setHp( en_current.getHp() - action.getDamage() );
                        }
                        else
                        {
                            my_current.setHp ( my_current.getHp() - action.getDamage() );
                            my_party[my_current.getLocation() - 1].setHp( my_current.getHp() );
                        }
                    }

                }

                if(my_current.getHp() < 0) my_current.setHp(0);
                else if(en_current.getHp() < 0) en_current.setHp(0);

                // calculate_attackeffect();
                // voor nu:
                if(a_type == 4 || a_type == 11 || a_type == 53)
                {
                    a_type = 1;
                }

                if(a_type != 1 || action.getDamage() == -1)
                {
                    try {
                        sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ScreenBattle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String modified = "";
                    if(action.getAttackType() == 101) modified = "attack";
                    else if(action.getAttackType() == 102) modified = "defense";
                    else if(action.getAttackType() == 103) modified = "accuracy";
                    if(action.getDamage() == 1)
                    {
                        infoString = "It lowered " + othername + "'s " + modified + "!";
                    }
                    else if(action.getDamage() == 0)
                    {
                        infoString = "It had no effect!";
                    }
                    else if(action.getDamage() == 2)
                    {
                        infoString = "It sharply lowered " + othername + "'s " + modified + "!";
                    }
                    else if(action.getDamage() == -1)
                    {
                        infoString = "But it missed!";
                    }
                    if(action.getAttackType() == 11)
                    {
                        infoString = othername + " is confused!";
                    }

                }
                
                
                
                int attacktype = action.getDamageType(), def_type1, def_type2;
                if(me)
                {
                    def_type1 = en_current.type1;
                    def_type2 = en_current.type2;
                }
                else
                {
                    def_type1 = my_current.type1;
                    def_type2 = my_current.type2;
                }
                
                System.out.println("typechart[" + attacktype + "][" + def_type1 + "]");
                float typemodifier1 = typechart[attacktype][def_type1];
                float typemodifier2 = 1f;
                if(def_type2 != 0) typemodifier2 = typechart[attacktype][def_type2];
                float damagemodifier = typemodifier1 * typemodifier2;

                
                System.out.println("a(" + action.getAttackType() + "), dmod(" + damagemodifier + "), damage(" + action.getDamage() + ")");
                int a = action.getAttackType();
                if(damagemodifier != 1f && action.getDamage() != -1)
                {
                    if(a == 1 || a == 4 || a == 11 || a == 30 || a == 50)
                    {
                        try {
                            sleep(2000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ScreenBattle.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        if(damagemodifier > 0 && damagemodifier < 1)
                        {
                            infoString = "It was not very effective...";
                        }
                        else if(damagemodifier > 1)
                        {
                            infoString = "It was super effective!";
                        }
                        else if(damagemodifier == 0)
                        {
                            infoString = "It was ineffective!";
                        }
                    }
                }
                
                
                try {
                    sleep(1800);
                    infoString = "";
                    sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ScreenBattle.class.getName()).log(Level.SEVERE, null, ex);
                }
                actionFinish(true);
                
                
            }
            
            
        }.start();
        
        
        
    }
    
    public void setExperience(int exp)
    {
        this.exp = exp;
        setExpreceived(true);
    }
    
    public Pokedex getPokedex()
    {
        return pokedex;
    }
    
    /**
     * if(who) trainer<br />
     * else wild
     * 
     * @param who
     * @return 
     */
    
    public int getModifier(boolean who, int what)
    {
        if(who)
        {
            return trainerModValues[what];
        }
        else
        {
            return wildModValues[what];
        }
    }
    
    public float getModifierFloat(boolean who, int what)
    {
        if(who)
        {
            if(what >= 0 && what <= 3)
            {
                return modifiertables.getModifier(true, trainerModValues[what]);
            }
            else
            {
                return modifiertables.getModifier(false, trainerModValues[what]);
            }
        }
        else
        {
            if(what >= 0 && what <= 3)
            {
                return modifiertables.getModifier(true, wildModValues[what]);
            }
            else
            {
                return modifiertables.getModifier(false, wildModValues[what]);
            }
        }
    }
    
    public void changeModifier(boolean who, int what, int howmuch)
    {
        if(who) trainerModValues[what] += howmuch;
        else wildModValues[what] += howmuch;
    }
    
    public void resetModifiers(boolean who)
    {
        if(who)
        {
            trainerModValues[0] = 0;
            trainerModValues[1] = 0;
            trainerModValues[2] = 0;
            trainerModValues[3] = 0;
            trainerModValues[4] = 0;
            trainerModValues[5] = 0;
        }
        else
        {
            wildModValues[0] = 0;
            wildModValues[1] = 0;
            wildModValues[2] = 0;
            wildModValues[3] = 0;
            wildModValues[4] = 0;
            wildModValues[5] = 0;
        }
    }
    
    public float[][] getTypechart()
    {
        return typechart;
    }
    
    public void setTypechart()
    {
        
        typechart = new float[18][18];
        typechart[1][1] = 1.0f;
        typechart[1][2] = 1.0f;
        typechart[1][3] = 1.0f;
        typechart[1][4] = 1.0f;
        typechart[1][5] = 1.0f;
        typechart[1][6] = 1.0f;
        typechart[1][7] = 1.0f;
        typechart[1][8] = 1.0f;
        typechart[1][9] = 1.0f;
        typechart[1][10] = 1.0f;
        typechart[1][11] = 1.0f;
        typechart[1][12] = 1.0f;
        typechart[1][13] = 0.5f;
        typechart[1][14] = 0.0f;
        typechart[1][15] = 1.0f;
        typechart[1][16] = 1.0f;
        typechart[1][17] = 0.5f;
        typechart[2][1] = 1.0f;
        typechart[2][2] = 0.5f;
        typechart[2][3] = 0.5f;
        typechart[2][4] = 1.0f;
        typechart[2][5] = 2.0f;
        typechart[2][6] = 2.0f;
        typechart[2][7] = 1.0f;
        typechart[2][8] = 1.0f;
        typechart[2][9] = 1.0f;
        typechart[2][10] = 1.0f;
        typechart[2][11] = 1.0f;
        typechart[2][12] = 2.0f;
        typechart[2][13] = 0.5f;
        typechart[2][14] = 1.0f;
        typechart[2][15] = 0.5f;
        typechart[2][16] = 1.0f;
        typechart[2][17] = 2.0f;
        typechart[3][1] = 1.0f;
        typechart[3][2] = 2.0f;
        typechart[3][3] = 0.5f;
        typechart[3][4] = 1.0f;
        typechart[3][5] = 0.5f;
        typechart[3][6] = 1.0f;
        typechart[3][7] = 1.0f;
        typechart[3][8] = 1.0f;
        typechart[3][9] = 2.0f;
        typechart[3][10] = 1.0f;
        typechart[3][11] = 1.0f;
        typechart[3][12] = 1.0f;
        typechart[3][13] = 2.0f;
        typechart[3][14] = 1.0f;
        typechart[3][15] = 0.5f;
        typechart[3][16] = 1.0f;
        typechart[3][17] = 1.0f;
        typechart[4][1] = 1.0f;
        typechart[4][2] = 1.0f;
        typechart[4][3] = 2.0f;
        typechart[4][4] = 0.5f;
        typechart[4][5] = 0.5f;
        typechart[4][6] = 1.0f;
        typechart[4][7] = 1.0f;
        typechart[4][8] = 1.0f;
        typechart[4][9] = 0.0f;
        typechart[4][10] = 2.0f;
        typechart[4][11] = 1.0f;
        typechart[4][12] = 1.0f;
        typechart[4][13] = 1.0f;
        typechart[4][14] = 1.0f;
        typechart[4][15] = 0.5f;
        typechart[4][16] = 1.0f;
        typechart[4][17] = 1.0f;
        typechart[5][1] = 1.0f;
        typechart[5][2] = 0.5f;
        typechart[5][3] = 2.0f;
        typechart[5][4] = 1.0f;
        typechart[5][5] = 0.5f;
        typechart[5][6] = 1.0f;
        typechart[5][7] = 1.0f;
        typechart[5][8] = 0.5f;
        typechart[5][9] = 2.0f;
        typechart[5][10] = 0.5f;
        typechart[5][11] = 1.0f;
        typechart[5][12] = 0.5f;
        typechart[5][13] = 2.0f;
        typechart[5][14] = 1.0f;
        typechart[5][15] = 0.5f;
        typechart[5][16] = 1.0f;
        typechart[5][17] = 0.5f;
        typechart[6][1] = 1.0f;
        typechart[6][2] = 0.5f;
        typechart[6][3] = 0.5f;
        typechart[6][4] = 1.0f;
        typechart[6][5] = 2.0f;
        typechart[6][6] = 0.5f;
        typechart[6][7] = 1.0f;
        typechart[6][8] = 1.0f;
        typechart[6][9] = 2.0f;
        typechart[6][10] = 2.0f;
        typechart[6][11] = 1.0f;
        typechart[6][12] = 1.0f;
        typechart[6][13] = 1.0f;
        typechart[6][14] = 1.0f;
        typechart[6][15] = 2.0f;
        typechart[6][16] = 1.0f;
        typechart[6][17] = 0.5f;
        typechart[7][1] = 2.0f;
        typechart[7][2] = 1.0f;
        typechart[7][3] = 1.0f;
        typechart[7][4] = 1.0f;
        typechart[7][5] = 1.0f;
        typechart[7][6] = 2.0f;
        typechart[7][7] = 1.0f;
        typechart[7][8] = 0.5f;
        typechart[7][9] = 1.0f;
        typechart[7][10] = 0.5f;
        typechart[7][11] = 0.5f;
        typechart[7][12] = 0.5f;
        typechart[7][13] = 2.0f;
        typechart[7][14] = 0.0f;
        typechart[7][15] = 1.0f;
        typechart[7][16] = 2.0f;
        typechart[7][17] = 2.0f;
        typechart[8][1] = 1.0f;
        typechart[8][2] = 1.0f;
        typechart[8][3] = 1.0f;
        typechart[8][4] = 1.0f;
        typechart[8][5] = 2.0f;
        typechart[8][6] = 1.0f;
        typechart[8][7] = 1.0f;
        typechart[8][8] = 0.5f;
        typechart[8][9] = 0.5f;
        typechart[8][10] = 1.0f;
        typechart[8][11] = 1.0f;
        typechart[8][12] = 1.0f;
        typechart[8][13] = 0.5f;
        typechart[8][14] = 0.5f;
        typechart[8][15] = 1.0f;
        typechart[8][16] = 1.0f;
        typechart[8][17] = 0.0f;
        typechart[9][1] = 1.0f;
        typechart[9][2] = 2.0f;
        typechart[9][3] = 1.0f;
        typechart[9][4] = 2.0f;
        typechart[9][5] = 0.5f;
        typechart[9][6] = 1.0f;
        typechart[9][7] = 1.0f;
        typechart[9][8] = 2.0f;
        typechart[9][9] = 1.0f;
        typechart[9][10] = 0.0f;
        typechart[9][11] = 1.0f;
        typechart[9][12] = 0.5f;
        typechart[9][13] = 2.0f;
        typechart[9][14] = 1.0f;
        typechart[9][15] = 1.0f;
        typechart[9][16] = 1.0f;
        typechart[9][17] = 2.0f;
        typechart[10][1] = 1.0f;
        typechart[10][2] = 1.0f;
        typechart[10][3] = 1.0f;
        typechart[10][4] = 0.5f;
        typechart[10][5] = 2.0f;
        typechart[10][6] = 1.0f;
        typechart[10][7] = 2.0f;
        typechart[10][8] = 1.0f;
        typechart[10][9] = 1.0f;
        typechart[10][10] = 1.0f;
        typechart[10][11] = 1.0f;
        typechart[10][12] = 2.0f;
        typechart[10][13] = 0.5f;
        typechart[10][14] = 1.0f;
        typechart[10][15] = 1.0f;
        typechart[10][16] = 1.0f;
        typechart[10][17] = 0.5f;
        typechart[11][1] = 1.0f;
        typechart[11][2] = 1.0f;
        typechart[11][3] = 1.0f;
        typechart[11][4] = 1.0f;
        typechart[11][5] = 1.0f;
        typechart[11][6] = 1.0f;
        typechart[11][7] = 2.0f;
        typechart[11][8] = 2.0f;
        typechart[11][9] = 1.0f;
        typechart[11][10] = 1.0f;
        typechart[11][11] = 0.5f;
        typechart[11][12] = 1.0f;
        typechart[11][13] = 1.0f;
        typechart[11][14] = 1.0f;
        typechart[11][15] = 1.0f;
        typechart[11][16] = 0.0f;
        typechart[11][17] = 0.5f;
        typechart[12][1] = 1.0f;
        typechart[12][2] = 0.5f;
        typechart[12][3] = 1.0f;
        typechart[12][4] = 1.0f;
        typechart[12][5] = 2.0f;
        typechart[12][6] = 1.0f;
        typechart[12][7] = 0.5f;
        typechart[12][8] = 0.5f;
        typechart[12][9] = 1.0f;
        typechart[12][10] = 0.5f;
        typechart[12][11] = 2.0f;
        typechart[12][12] = 1.0f;
        typechart[12][13] = 1.0f;
        typechart[12][14] = 0.5f;
        typechart[12][15] = 1.0f;
        typechart[12][16] = 2.0f;
        typechart[12][17] = 0.5f;
        typechart[13][1] = 1.0f;
        typechart[13][2] = 2.0f;
        typechart[13][3] = 1.0f;
        typechart[13][4] = 1.0f;
        typechart[13][5] = 1.0f;
        typechart[13][6] = 2.0f;
        typechart[13][7] = 0.5f;
        typechart[13][8] = 1.0f;
        typechart[13][9] = 0.5f;
        typechart[13][10] = 2.0f;
        typechart[13][11] = 1.0f;
        typechart[13][12] = 2.0f;
        typechart[13][13] = 1.0f;
        typechart[13][14] = 1.0f;
        typechart[13][15] = 1.0f;
        typechart[13][16] = 1.0f;
        typechart[13][17] = 0.5f;
        typechart[14][1] = 0.0f;
        typechart[14][2] = 1.0f;
        typechart[14][3] = 1.0f;
        typechart[14][4] = 1.0f;
        typechart[14][5] = 1.0f;
        typechart[14][6] = 1.0f;
        typechart[14][7] = 1.0f;
        typechart[14][8] = 1.0f;
        typechart[14][9] = 1.0f;
        typechart[14][10] = 1.0f;
        typechart[14][11] = 2.0f;
        typechart[14][12] = 1.0f;
        typechart[14][13] = 1.0f;
        typechart[14][14] = 2.0f;
        typechart[14][15] = 1.0f;
        typechart[14][16] = 0.5f;
        typechart[14][17] = 0.5f;
        typechart[15][1] = 1.0f;
        typechart[15][2] = 1.0f;
        typechart[15][3] = 1.0f;
        typechart[15][4] = 1.0f;
        typechart[15][5] = 1.0f;
        typechart[15][6] = 1.0f;
        typechart[15][7] = 1.0f;
        typechart[15][8] = 1.0f;
        typechart[15][9] = 1.0f;
        typechart[15][10] = 1.0f;
        typechart[15][11] = 1.0f;
        typechart[15][12] = 1.0f;
        typechart[15][13] = 1.0f;
        typechart[15][14] = 1.0f;
        typechart[15][15] = 2.0f;
        typechart[15][16] = 1.0f;
        typechart[15][17] = 0.5f;
        typechart[16][1] = 1.0f;
        typechart[16][2] = 1.0f;
        typechart[16][3] = 1.0f;
        typechart[16][4] = 1.0f;
        typechart[16][5] = 1.0f;
        typechart[16][6] = 1.0f;
        typechart[16][7] = 0.5f;
        typechart[16][8] = 1.0f;
        typechart[16][9] = 1.0f;
        typechart[16][10] = 1.0f;
        typechart[16][11] = 2.0f;
        typechart[16][12] = 1.0f;
        typechart[16][13] = 1.0f;
        typechart[16][14] = 2.0f;
        typechart[16][15] = 1.0f;
        typechart[16][16] = 0.5f;
        typechart[16][17] = 0.5f;
        typechart[17][1] = 1.0f;
        typechart[17][2] = 0.5f;
        typechart[17][3] = 0.5f;
        typechart[17][4] = 0.5f;
        typechart[17][5] = 1.0f;
        typechart[17][6] = 2.0f;
        typechart[17][7] = 1.0f;
        typechart[17][8] = 1.0f;
        typechart[17][9] = 1.0f;
        typechart[17][10] = 1.0f;
        typechart[17][11] = 1.0f;
        typechart[17][12] = 1.0f;
        typechart[17][13] = 2.0f;
        typechart[17][14] = 1.0f;
        typechart[17][15] = 1.0f;
        typechart[17][16] = 1.0f;
        typechart[17][17] = 0.5f;
    }
    
//    public static void main(String[] args, String username, short bg_type, PokeDataPackage my_pdp, PokeDataPackage e_pdp){
//        Battle b = new Battle(username, bg_type, my_pdp, e_pdp);
//        
//    }    
}
