/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import ser.PokeDataPackage;

/**
 *
 * @author Gebruiker
 */
public class ScreenOverworld extends JFrame {
    JTextField textField;
    Map map;
    Player player;
    
    boolean offline = false;
    
    boolean chat = true;
    
    PokeMultiplayerClient pmc;
    Content content;
            
    Color nameBackground;
    
    MapReader mr;
    
    ArrayList<String> list_chatLines;
    
    TextArray textArray = null;
    String object_line_1;
    String object_line_2;
    
    boolean characterIsBusy = false;
    boolean typing = false;
    boolean battle = false;
    boolean grass = false;
    boolean check_enemy_grass = false;
    
    private boolean transitionDone = true;
    private boolean turnblack = false;
    private boolean turnback = false;
    private boolean firstback = false;
    
    public boolean transitionDone() {
        return transitionDone;
    }
    public boolean turnblack() {
        return turnblack;
    }
    public boolean turnback() {
        return turnback;
    }
    public boolean firstback() {
        return firstback;
    }
    public void setTransitionDone(boolean to) {
        transitionDone = to;
    }
    public void setTurnblack(boolean to) {
        turnblack = to;
    }
    public void setTurnback(boolean to) {
        turnback = to;
    }
    public void setFirstback(boolean to) {
        firstback = to;
    }
    
    
    
    int op = 25;
    int marge = 0;
    
    public ScreenOverworld(PokeMultiplayerClient c, Player player){
        list_chatLines = new ArrayList<String>();
        for(int i = 0; i < 6; i++)
        {
            list_chatLines.add("");
        }
        
        this.pmc = c;
        
        nameBackground = new Color(255, 255, 255, 192);

        System.out.println(" - scrOv.079, " + player.zone);
        
        this.player = player;
        map = new Map(player);
        player.setScrOv(this);
        
        System.out.println(" - scrOv.085, " + player.zone);
        
        mr = new MapReader(this);
        mr.toggleMap(player.zone, false);
        player.pm.objects.init(this, player, mr);
        
        System.out.println(" - scrOv.091, " + player.zone);
        
        setTitle("Client");
        setDefaultCloseOperation(3);

        textField = new JTextField(1);
        textField.setLayout(null);
        textField.setBounds(0, -80, 200, 16);
        textField.setVisible(true);
        textField.setEnabled(true);

        textField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    System.out.println("Pressed enter");
                    chatHandle();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        content = new Content();
        
        
        add(textField);
        add(content);
        
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                //pmc.update(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                //pmc.update(0);
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
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(getClass().getResource("/img/other/pokeball.png"));
        setIconImage(img);
            

        pack();
        int v = 64;
        Insets insets = getInsets();
        setSize(13 * v + insets.left + insets.right, 11 * v + insets.top + insets.bottom);
        System.out.println("Window Dimensions: " + getSize());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        content.requestFocusInWindow();
        
        //pmc.update(player.x, player.y, player.walking);
        
        
    }
    
    public void chatHandle()
    {
        if( typing == false ){
            textField.requestFocusInWindow();
            typing = true;
        }
        else
        {
            typing = false;
            if ( !textField.getText().isEmpty() )
            {
                if( textField.getText().toLowerCase().startsWith("/cheat 0"))
                {
                    pmc.my_pokemon_party[0].setHp(pmc.my_pokemon_party[0].getMaxhp());
                    pmc.addChatLine("All your Pokemon are healed!");
                } 
                else if ( textField.getText().toLowerCase().startsWith("/battle"))
                {
                    String text = textField.getText();
                    String[] args = text.split(" ");
                    if(args.length > 1 && args[1].isEmpty() == false)
                    {
                        for(OtherPlayer op : pmc.list_others.values())
                        {
                            if (op.username.toLowerCase().equals(args[1].toLowerCase()))
                            {
                                pmc.list_unsentChatLines.add("/battle " + op.id);
                                pmc.sendChatLine("/battle " + op.id);
                            }
                            else
                            {
                                addText("User not found!");
                            }
                        }
                    } else
                    {
                        addText("Command not used properly!");
                    }
                }
                else if( textField.getText().startsWith("/")) {
                    addText("Command not used properly!");
                }
                else
                {
                    pmc.list_unsentChatLines.add(textField.getText());
                    pmc.sendChatLine(textField.getText());
                }
                
            }
            textField.setText("");
            content.requestFocusInWindow();

        }
    }

//    public void setPlayer(Player player){
//        this.player = player;
//    }

    class TAdapter extends KeyAdapter {
        boolean shift;
        
        public TAdapter(){
            System.out.println("Test TA");

        }
        
        

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == 16) {
                shift = true;
            }

            if (!typing && op == 25) {
                player.keyPressed(e);
                
                
                if ((key == KeyEvent.VK_C)){
                    player.pm.objects.getInfo();
                }
                
                if ((key == KeyEvent.VK_X)){
                    System.out.println("Player coords: " + player.x + ", " + player.y);
                }

                if(characterIsBusy == false){
                    if ((!player.leftpressed) && (key == 37)) player.leftpressed = true;
                    if ((!player.rightpressed) && (key == 39)) player.rightpressed = true;
                    if ((!player.uppressed) && (key == 38)) player.uppressed = true;
                    if ((!player.downpressed) && (key == 40)) player.downpressed = true;
                }

                if ((key == KeyEvent.VK_Q) && (!shift)) {
                    //System.out.println("Player is at (" + player.x + ", " + player.y + ")");
    //                outputChatLine.username = player.username;
    //                outputChatLine.line = "Dit is een test.";
                    list_chatLines.add("Text!");
                }
                
                if ( key == KeyEvent.VK_BACK_SPACE && offline == false) {
                    chatHandle();
                    
                }
                
                if ((key == KeyEvent.VK_ENTER)){
                    if(characterIsBusy == false)
                    {
                        textArray = player.pm.objects.checkConvo();
                        if(textArray != null)
                        {
                            object_line_1 = textArray.showText();
                            object_line_2 = textArray.showText();
                            if(object_line_1 == null || object_line_2 == null)
                            {
                                object_line_1 = " ";
                                object_line_2 = " ";
                                textArray = null;
                            }
                            characterIsBusy = true;
                            chat = false;

                        }
                    }
                    else
                    {        
                        if(textArray != null)
                        {
                            object_line_1 = textArray.showText();
                            
                            if(object_line_1 == null)
                            {
                                object_line_1 = " ";
                                object_line_2 = " ";
                                textArray = null;
                                characterIsBusy = false;
                                chat = true;
                            }
                            else
                            {
                                object_line_2 = textArray.showText();
                            }
                        }
                    }
                }

            }

        }

        public void keyReleased(KeyEvent e){
            int key = e.getKeyCode();

            if (key == 16) {
                shift = false;
            }

            if (key == 37) player.leftpressed = false;
            if (key == 39) player.rightpressed = false;
            if (key == 38) player.uppressed = false;
            if (key == 40) player.downpressed = false;
        }
    }
    
    public void addText(String text)
    {
        list_chatLines.add(text);
    }
    
    public void transition()
    {
        System.out.println("Transition starting");
        setTurnblack(true);
        setTransitionDone(false);
        player.leftpressed = false;
        player.rightpressed = false;
        player.uppressed = false;
        player.downpressed = false;
    }

    private class Content extends JComponent{
        
        TextPanel textPanel = new TextPanel();
        
        ArrayList<OtherPlayer> others = new ArrayList();
        int otherX, otherY;
        
        
        

        public Content(){
            textPanel.setTextPanel(0, 448, 832, 256, 1);
            addKeyListener(new TAdapter());
        }
        
        

        public void paint(Graphics g){
            Graphics2D g2d = (Graphics2D) g;
//            others = pmc.others;
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.drawImage(map.images.get(player.zone), (-1 * player.x) + 384 , (-1 * player.y) + 256, null);

    //            g2d.setColor(Color.RED);
    //            for(int i = 0; i < player.pm.objects.x.size(); i++){
    //                g2d.fillRect((-1 * player.x) + player.pm.objects.x.get(i), (-1 * player.y) + player.pm.objects.y.get(i), 64, 64);
    //            }


                if(pmc.list_others.size() > 0){
                    for( OtherPlayer op : pmc.list_others.values() )
                    {
                        OtherPlayer other = op;
                        if( player.zone == op.zone )
                        {
                            int op_marge = 0;
                            otherX = other.x - player.x + 384;
                            otherY = other.y - player.y + 256;
                            if( op.charType == 6 )
                            {
                                op_marge = 4;
                            }

                            if( (op.x % 64) == 0 && ((op.y + 16) % 64 == 0)){
                                if( player.pm.objects.checkGrass(op) )
                                {
                                    op.grass = true;
                                }
                                else
                                {
                                    op.grass = false;
                                }
                            }

                            if( op.battling == true )
                            {
                                g2d.drawImage(player.sprites_battle[(other.charType - 1)][other.walking], otherX, otherY - op_marge, null);
                            }
                            else
                            {
                                if( op.grass == true )
                                {
                                    g2d.drawImage(player.sprites_grass[(other.charType - 1)][other.walking], otherX, otherY - op_marge, null);
                                }
                                else
                                {
                                    g2d.drawImage(player.sprites[(other.charType - 1)][other.walking], otherX, otherY - op_marge, null);
                                }
                            }

        //                    }
        //                    else
        //                    {
        //                        g2d.drawImage(player.sprites[(other.charType - 1)][other.walking], otherX, otherY, null);
        //                    }


                            g2d.setFont(textPanel.font_usernameHead);
                            int usernameLength = (int)g2d.getFontMetrics().getStringBounds(other.username, g2d).getWidth() / 2;
                            g2d.setColor(nameBackground);
                            g2d.fillRect(otherX + 16 - usernameLength, otherY - 28, usernameLength * 2 + 32, 20);
                            g2d.setColor(Color.BLACK);
                            g2d.drawString(other.username, otherX + 32 - usernameLength, otherY - 12);
                        }

                    }

    //                for (int i = 0; i < others.size(); i++) {
    //                    try
    //                    {
    //                        OtherPlayer op = (OtherPlayer)this.others.get(i);
    //
    //                        if (!op.username.toLowerCase().equals(pmc.username.toLowerCase())) {
    //                        otherX = (op.x - player.x + 384);
    //                        otherY = (op.y - player.y + 256);
    //
    //                            g2d.drawImage(player.sprites[(op.charType - 1)][op.walking], otherX, otherY, null);
    //                        //g2d.drawImage(player.sprites[(dp.charType - 1)][dp.walking], otherX, otherY, null);
    //
    //                        g2d.setFont(textPanel.font_usernameHead);
    //                        int usernameLength = (int)g2d.getFontMetrics().getStringBounds(op.username, g2d).getWidth() / 2;
    //                        g2d.setColor(nameBackground);
    //                        g2d.fillRect(otherX + 16 - usernameLength, otherY - 28, usernameLength * 2 + 32, 20);
    //                        g2d.setColor(Color.BLACK);
    //                        g2d.drawString(op.username, otherX + 32 - usernameLength, otherY - 12);
    //                        }
    //
    //                    }
    //                    catch (Exception ex){
    //                        ex.printStackTrace();
    //                    }
    //
    //                }
                }
                if( battle == true )
                {
                    g2d.drawImage(player.sprites_battle[(player.charType - 1)][player.walking], 384, 256, null);
                }
                else if (grass == true)
                {
                    g2d.drawImage(player.sprites_grass[(player.charType - 1)][player.walking], 384, 256, null);
                }
                else
                {
                    g2d.drawImage(player.sprites[(player.charType - 1)][player.walking], 384, 256 - marge, null);
                }
                if( offline != true )
                {
                    g2d.setFont(textPanel.font_usernameHead);
                    int usernameLength = (int)g2d.getFontMetrics().getStringBounds(player.username, g2d).getWidth() / 2;
                    g2d.setColor(nameBackground);
                    g2d.fillRect(400 - usernameLength, 228, usernameLength * 2 + 32, 20);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(player.username, 416 - usernameLength, 244);
                }

                g2d.drawImage(map.foregrounds.get(player.zone), (-1 * player.x) + 384 , (-1 * player.y) + 256, null);

                if (textPanel.type != 0) {
                    if( offline != true || (offline == true && chat == false))
                    {
                        g2d.drawImage(textPanel.top, textPanel.topLeftX + 64, textPanel.topLeftY, null);
                        g2d.drawImage(textPanel.bottom, textPanel.topLeftX + 64, textPanel.topLeftY + textPanel.height - 64, null);
                        g2d.drawImage(textPanel.left, textPanel.topLeftX, textPanel.topLeftY + 64, null);
                        g2d.drawImage(textPanel.right, textPanel.topLeftX + textPanel.width - 64, textPanel.topLeftY + 64, null);

                        g2d.drawImage(textPanel.topLeft, textPanel.topLeftX, textPanel.topLeftY, null);
                        g2d.drawImage(textPanel.bottomLeft, textPanel.topLeftX, textPanel.topLeftY + textPanel.height - 64, null);
                        g2d.drawImage(textPanel.topRight, textPanel.topLeftX + textPanel.width - 64, textPanel.topLeftY, null);
                        g2d.drawImage(textPanel.bottomRight, textPanel.topLeftX + textPanel.width - 64, textPanel.topLeftY + textPanel.height - 64, null);

                        g2d.setColor(new Color(248, 248, 248, 153));
                        g2d.fillRect(textPanel.topLeftX + 64, textPanel.topLeftY + 64, textPanel.width - 128, textPanel.height - 128);
                    }


                    if(chat == true){
                    g2d.setFont(textPanel.font_usernameHead);
                    g2d.setColor(Color.BLACK);
                //
                    if (typing == true) {
                        g2d.drawString(") " + textField.getText(), textPanel.topLeftX + 64, textPanel.topLeftY + 56);
                    }
                //      
                //
                    g2d.setFont(textPanel.font_chat);
                        //g2d.drawString("Server: dit is een test. Je bevindt je in zone " + player.zone + ".", textPanel.topLeftX+64, textPanel.topLeftY + 68 + 16);
                    g2d.drawString(list_chatLines.get(list_chatLines.size() - 1), textPanel.topLeftX + 64, textPanel.topLeftY + 68 + 16);
                    g2d.drawString(list_chatLines.get(list_chatLines.size() - 2), textPanel.topLeftX + 64, textPanel.topLeftY + 94 + 16);
                    g2d.drawString(list_chatLines.get(list_chatLines.size() - 3), textPanel.topLeftX + 64, textPanel.topLeftY + 120 + 16);
                    g2d.drawString(list_chatLines.get(list_chatLines.size() - 4), textPanel.topLeftX + 64, textPanel.topLeftY + 146 + 16);
                    g2d.drawString(list_chatLines.get(list_chatLines.size() - 5), textPanel.topLeftX + 64, textPanel.topLeftY + 172 + 16);
                    g2d.drawString(list_chatLines.get(list_chatLines.size() - 6), textPanel.topLeftX + 64, textPanel.topLeftY + 198 + 16);
                    } else{


                        g2d.setFont(textPanel.font_rusaBattle);
                        g2d.setColor(new Color(16, 16, 16));
                        g2d.drawString(object_line_1, textPanel.topLeftX+64, textPanel.topLeftY+ 96);
                        g2d.drawString(object_line_2, textPanel.topLeftX+64, textPanel.topLeftY+ 182);
                    }
                }

                if(ScreenOverworld.this.battle == true){
                    g2d.setColor(new Color(0, 0, 0, 130));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }

            if( turnback() )
            {
                if(firstback())
                {
                    
                    
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ScreenOverworld.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    setFirstback(false);
                    
                }
                g2d.setColor(new Color(0, 0, 0, op));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                op -= 25;

                if( op < 5 )
                {
                    op = 25;
                    setTurnblack(false);
                    setTurnback(false);
                }
                try {
                    Thread.sleep(15);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ScreenOverworld.class.getName()).log(Level.SEVERE, null, ex);
                }
            }    
            if( turnblack() )
            {
                System.out.println("op = " + op);
                if( !turnback() ) {
                    g2d.setColor(new Color(0, 0, 0, op));
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    op += 25;

                    if( op > 250 )
                    {
                        
                        op = 255;
                        g2d.setColor(new Color(0, 0, 0, 1.0f));
                        g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                        setFirstback(true);
                        op = 250;
                        setTurnback(true);
                        setTransitionDone(true);
                    }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ScreenOverworld.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                
                
            }
            
            
            
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(ScreenOverworld.class.getName()).log(Level.SEVERE, null, ex);
            }

            repaint();
        }

    }
}
