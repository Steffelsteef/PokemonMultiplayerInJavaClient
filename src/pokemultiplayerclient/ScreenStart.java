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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import sun.swing.ImageIconUIResource;

/**
 *
 * @author Gebruiker
 */
public class ScreenStart extends JFrame{
    String ip;
    short port;
    int choice = 1;
    JButton play_online, register, play_offline;
    
    JTextField[] field;
    JPasswordField passw;
    
    BufferedImage bg;
    
    Insets insets;
    ArrayList<JButton> button;
    ArrayList<ImageIcon> ii;
    
    Font font;
    Font font_bold;
    Font font_big;
    boolean failed_username = false;

    PokeMultiplayerClient client;
    
    public ScreenStart(String ip, String port, String username, int charType, PokeMultiplayerClient pmc)
    {
        ScreenStart tryAgain = new ScreenStart(pmc);
        tryAgain.field[0].setText(ip);
        tryAgain.field[1].setText(port);
        tryAgain.field[2].setText(username);
        tryAgain.choice = charType;
    }
    
    public ScreenStart(PokeMultiplayerClient cl){
        this.client = cl;
        
        try {
            File fontfile = null;
            InputStream in = getClass().getResourceAsStream("/font/rusa.ttf");
            Font fontje;

            fontje = Font.createFont(Font.TRUETYPE_FONT, in);

            this.font = fontje.deriveFont(Font.PLAIN, 14);
            this.font_bold = fontje.deriveFont(Font.BOLD, 14);
            this.font_big = fontje.deriveFont(Font.BOLD, 32);
        } catch (FontFormatException ex) {
            Logger.getLogger(ScreenStart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScreenStart.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            port = 2406;
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);
        }
        
        setTitle("Let's play Pokémon!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        
        field = new JTextField[3];
        for(int i = 0; i < 3; i++){
            field[i] = new JTextField(1);
            field[i].setLayout(null);
            field[i].setVisible(true);
            field[i].setEnabled(true);
        }
        
        passw = new JPasswordField();
        
        
        field[0].setBounds(320,362 - 8,200,20);
        field[1].setBounds(530,362 - 8,50,20);
        field[2].setBounds(320,422 - 8,120,20);
        passw.setBounds(450,422 - 8,130,20);
        
        field[0].setText(ip);
        field[1].setText("" + port);
        
        
        



        
        setContentPane(new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, null);
                g.setFont(font);
                g.drawString("IP:", 322, 360 - 8);
                g.drawString("Port:", 532, 360 - 8);
                g.drawString("Username:", 322, 420 - 8);
                g.drawString("Password:", 452, 420 - 8);
                
                g.setColor(Color.RED);
                if (failed_username == true){
                    g.drawString("Invalid Username.", 522, 462);
                }
                
                g.setFont(font_big);
                g.drawString(client.version, 16, 32);
                
                g.setColor(Color.BLACK);
                g.setFont(font_bold);
                g.drawString("To-do list:", 800, 45);
                
                g.setFont(font_bold);
                g.drawString("v0.01", 775, 80);
                g.drawString(" x database functionality", 785, 80 + 16);
                g.drawString(" x chat working", 785, 80 + 32);
                g.drawString(" x login via password", 785, 80 + 48);
                
                g.drawString("v0.02", 775, 152);
                g.drawString(" x registration", 785, 152 + 16);
                g.drawString(" x register and play", 785, 152 + 32);
                g.drawString(" x challenge other players", 785, 152 + 48);
                
                g.drawString("v0.03", 775, 224);
                g.drawString(" x basic user battle system", 785, 224 + 16);
                g.drawString(" x basic wild battle system", 785, 224 + 32);
                g.drawString(" x more Rocket characters", 785, 224 + 48);
                g.drawString(" x better register screen", 785, 224 + 64);
                
                g.drawString("v0.04", 775, 312);
                g.drawString(" x better battle system everywhere", 785, 312+16);
                g.drawString(" x able to level up", 785, 312+32);
                g.drawString(" x stats grow with leveling", 785, 312+48);
                g.drawString(" x able to catch other Pokémon", 785, 312 + 64);
                
                g.setFont(font);
                g.drawString("v0.1", 775, 400);
                g.drawString(" - choose other Pokémon in battle", 785, 400 + 16);
                g.drawString(" - use items", 785, 400 + 32);
                g.drawString(" - Pokémon centers!", 785, 400 + 48);
                repaint();
            }
        });
        
        
        ii = new ArrayList();
        button = new ArrayList();
        try {
            bg = ImageIO.read(getClass().getResource("/img/other/version_info.png"));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Lass.png"))).getSubimage(0, 0, 64, 80)));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Youngster.png"))).getSubimage(0, 0, 64, 80)));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Nerd.png"))).getSubimage(0, 0, 64, 80)));
        } catch (IOException ex) {
            Logger.getLogger(ScreenStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        register = new JButton();
        register.setText("Register");
        register.setBounds(618, 330 + 24, 128, 30);
        register.setVisible(true);
        register.setEnabled(true);
        register.addActionListener(new ActionListener(){
         
            
            @Override
            public void actionPerformed(ActionEvent e) {
                new ScreenRegister(client);
                dispose();
            }
        });
        play_online = new JButton();
        play_online.setText("Join");
        play_online.setBounds(618, 379 + 24, 128, 30);
        play_online.setVisible(true);
        play_online.setEnabled(true);
        play_online.addActionListener(new ActionListener(){
         
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if(field[2].getText().contains(" ") || field[2].getText().isEmpty()){
                    failed_username = true;
                    System.out.println("Faal!");
                }else {
                    
                    String sendIP = field[0].getText() + ":" + field[1].getText();
                    String sendU = field[2].getText();
                    
                    try{
                        int tryport = Integer.parseInt(sendIP.substring(sendIP.indexOf(":") + 1));
                        String tryip = sendIP.substring(0, sendIP.indexOf(":"));
                    } catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    System.out.println(sendIP + ", " + sendU);
                    client.setOnlineClient(field[0].getText(), Integer.parseInt(field[1].getText()), sendU, String.copyValueOf(passw.getPassword()), choice);
                    //dispose();
                }
            }
        });
        play_offline = new JButton();
        play_offline.setText("Offline");
        play_offline.setBounds(816, 700, 128, 30);
        play_offline.setVisible(true);
        play_offline.setEnabled(true);
        play_offline.addActionListener(new ActionListener(){
            
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String sendU = field[2].getText();

                if(field[2].getText().contains(" ") || field[2].getText().isEmpty()){
                    sendU = "Default";
                }
                    
                    client.openOfflineLogin();
                    dispose();
            }
        });
        add(play_online);
        add(register);
        //add(play_offline);

        
        
        add(field[0]);
        add(field[1]);
        add(field[2]);
        add(passw);
        
        
        setLayout(null);
        pack();
        insets = getInsets();
        setSize(1024 + insets.left + insets.right, 768 + insets.top + insets.bottom);
        setLocationRelativeTo(null);
        field[2].requestFocusInWindow();


        
    }
    
    
//    public void paint(Graphics g){
//        Graphics2D g2d = (Graphics2D) g;
//        
//        g2d.drawImage(bg, insets.left, insets.top, null);
//        
//        repaint();
//    }

}
