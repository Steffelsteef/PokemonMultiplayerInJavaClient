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
import javax.swing.JTextField;
import sun.swing.ImageIconUIResource;

/**
 *
 * @author Gebruiker
 */
public class ScreenOfflineLogin extends JFrame{
    
    String ip;
    short port;
    int choice = 1;
    JButton connect, play_offline;
    
    JTextField[] field;
    
    BufferedImage bg;
    
    Insets insets;
    ArrayList<JButton> button;
    ArrayList<ImageIcon> ii;
    
    Font font;
    boolean failed_username = false;

    PokeMultiplayerClient client;
    
    public ScreenOfflineLogin(String ip, String port, String username, int charType, PokeMultiplayerClient pmc)
    {
        ScreenOfflineLogin tryAgain = new ScreenOfflineLogin(pmc);
        tryAgain.field[0].setText(ip);
        tryAgain.field[1].setText(port);
        tryAgain.field[2].setText(username);
        tryAgain.choice = charType;
    }
    
    public ScreenOfflineLogin(PokeMultiplayerClient cl){
        this.client = cl;
        
        try {
            File fontfile = null;
            InputStream in = getClass().getResourceAsStream("/font/rusa.ttf");
            Font fontje;

            fontje = Font.createFont(Font.TRUETYPE_FONT, in);

            this.font = fontje.deriveFont(Font.PLAIN, 14);
        } catch (FontFormatException ex) {
            Logger.getLogger(ScreenOfflineLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScreenOfflineLogin.class.getName()).log(Level.SEVERE, null, ex);
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
        
        
        field[0].setBounds(20,22,200,20);
        field[1].setBounds(230,22,50,20);
        field[2].setBounds(20,82,150,20);
        
        field[0].setText(ip);
        field[1].setText("" + port);
        
        
        



        
        setContentPane(new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, null);
                g.setFont(font);
                g.drawString("IP:", 22,20);
                g.drawString("Port:", 232, 20);
                g.drawString("Username:", 22, 80);
                g.setColor(Color.RED);
                if (failed_username == true){
                    g.drawString("Invalid Username.", 22, 122);
                }

                if(choice == 1){
                    g.setColor(Color.RED);
                    g.fillOval((64 * 0) + 256 + 60, 88, 6, 6);
                } else if(choice == 2){
                    g.setColor(Color.RED);
                    g.fillOval((64 * 1) + 256 + 60, 88, 6, 6);
                } else if(choice == 3){
                    g.setColor(Color.RED);
                    g.fillOval((64 * 2) + 256 + 60, 88, 6, 6);
                }
                repaint();
            }
        });
        
        
        ii = new ArrayList();
        button = new ArrayList();
        try {
            bg = ImageIO.read(getClass().getResource("/img/other/loginbg.png"));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Lass.png"))).getSubimage(0, 0, 64, 80)));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Youngster.png"))).getSubimage(0, 0, 64, 80)));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Nerd.png"))).getSubimage(0, 0, 64, 80)));
        } catch (IOException ex) {
            Logger.getLogger(ScreenOfflineLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < 3; i++){
            final int j = i;
            button.add(new JButton());
            button.get(i).setIcon(ii.get(i));
            button.get(i).setBounds((64 * i) + 256 + 32, 104, 64, 80);
            button.get(i).setVisible(true);
            button.get(i).setEnabled(true);
            button.get(i).setBorderPainted(false); 
            button.get(i).setContentAreaFilled(false); 
            button.get(i).setFocusPainted(false); 
            button.get(i).setOpaque(false); 
            
            button.get(i).addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    choice = (byte) (j + 1);
                }
            });
            
            add(button.get(i));
        }
        
//        connect = new JButton();
//        connect.setText("Join");
//        connect.setBounds(64, 160, 128, 30);
//        connect.setVisible(true);
//        connect.setEnabled(true);
//        connect.addActionListener(new ActionListener(){
//         
//            
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(field[2].getText().contains(" ") || field[2].getText().isEmpty()){
//                    failed_username = true;
//                    System.out.println("Faal!");
//                }else {
//                    
//                    String sendIP = field[0].getText() + ":" + field[1].getText();
//                    String sendU = field[2].getText();
//                    
//                    try{
//                        int tryport = Integer.parseInt(sendIP.substring(sendIP.indexOf(":") + 1));
//                        String tryip = sendIP.substring(0, sendIP.indexOf(":"));
//                    } catch(Exception ex){
//                        ex.printStackTrace();
//                    }
//                    
//                    System.out.println(sendIP + ", " + sendU);
//                    client.setOnlineClient(field[0].getText(), Integer.parseInt(field[1].getText()), sendU, choice);
//                    dispose();
//                }
//            }
//        });
        play_offline = new JButton();
        play_offline.setText("Play offline");
        play_offline.setBounds(64, 200, 128, 30);
        play_offline.setVisible(true);
        play_offline.setEnabled(true);
        play_offline.addActionListener(new ActionListener(){
            
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String sendU = field[2].getText();

                if(field[2].getText().contains(" ") || field[2].getText().isEmpty()){
                    sendU = "Default";
                }
                    
                    client.setOfflineClient(sendU, choice);
                    dispose();
            }
        });
        //add(connect);
        add(play_offline);

        
        
        add(field[0]);
        add(field[1]);
        add(field[2]);
        
        
        setLayout(null);
        pack();
        insets = getInsets();
        setSize(510 + insets.left + insets.right, 240 + insets.top + insets.bottom);
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
