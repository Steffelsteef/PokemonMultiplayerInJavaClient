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
public class ScreenRegister extends JFrame{
    
    int textfieldX = 372;
    int textfieldY = 322;
    
    String ip;
    short port;
    int choice = 1;
    JButton connect, button_finish;
    
    JTextField[] field;
    JPasswordField passw;
    
    BufferedImage bg;
    
    Insets insets;
    ArrayList<JButton> button;
    ArrayList<ImageIcon> ii;
    
    Font font;
    boolean failed_username = false;

    PokeMultiplayerClient client;
    
    public ScreenRegister(String ip, String port, String username, int charType, PokeMultiplayerClient pmc)
    {
        ScreenRegister tryAgain = new ScreenRegister(pmc);
        tryAgain.field[0].setText(ip);
        tryAgain.field[1].setText(port);
        tryAgain.field[2].setText(username);
        tryAgain.choice = charType;
    }
    
    public ScreenRegister(PokeMultiplayerClient cl){
        this.client = cl;
        
        try {
            File fontfile = null;
            InputStream in = getClass().getResourceAsStream("/font/rusa.ttf");
            Font fontje;

            fontje = Font.createFont(Font.TRUETYPE_FONT, in);

            this.font = fontje.deriveFont(Font.PLAIN, 14);
        } catch (FontFormatException ex) {
            Logger.getLogger(ScreenRegister.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScreenRegister.class.getName()).log(Level.SEVERE, null, ex);
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
        
        
        field[0].setBounds(textfieldX,textfieldY,200,20);
        field[1].setBounds(textfieldX + 210,textfieldY,50,20);
        field[2].setBounds(textfieldX,textfieldY + 60, 120,20);
        passw.setBounds(textfieldX + 130,textfieldY + 60,130,20);
        
        field[0].setText(ip);
        field[1].setText("" + port);
        
        
        



        
        setContentPane(new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, null);
                g.setFont(font);
                g.drawString("IP:", textfieldX + 2,textfieldY - 2);
                g.drawString("Port:", textfieldX + 212, textfieldY - 2);
                g.drawString("Username:", textfieldX + 2, textfieldY + 58);
                g.drawString("Password:", textfieldX + 132, textfieldY + 58);
                g.setColor(Color.RED);
                if (failed_username == true){
                    g.drawString("Invalid Username.", 22, 122);
                }


                g.setColor(Color.RED);
                g.fillOval(button.get(choice - 1).getX() + 29, button.get(choice - 1).getY() + 80, 6, 6);
                
                repaint();
            }
        });
        
        
        ii = new ArrayList();
        button = new ArrayList();
        
        int trainers = 0;
        int rockets = 0;
        
        try {
            bg = ImageIO.read(getClass().getResource("/img/other/registerbg.png"));
            
            trainers = 3;
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Lass.png"))).getSubimage(0, 0, 64, 80)));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Youngster.png"))).getSubimage(0, 0, 64, 80)));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Nerd.png"))).getSubimage(0, 0, 64, 80)));
            
            rockets = 3;
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Rocket1.png"))).getSubimage(0, 0, 64, 80)));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Rocket2.png"))).getSubimage(0, 0, 64, 80)));
            ii.add(new ImageIconUIResource((ImageIO.read(getClass().getResource("/img/sprsh/Rocket3.png"))).getSubimage(0, 0, 64, 80)));
        } catch (IOException ex) {
            Logger.getLogger(ScreenRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < trainers; i++){
            System.out.println("Creating Tbutton " + i);
            final int j = i;
            button.add(new JButton());
            button.get(i).setIcon(ii.get(i));
            button.get(i).setBounds((64 * i) + 52, 334, 64, 80);
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
        
        final int trainersfinalinteger = trainers;
        
        for (int i = trainers; i < trainers + rockets; i++){
            System.out.println("Creating Rbutton " + i);
            final int j = i;
            button.add(new JButton());
            button.get(i).setIcon(ii.get(i));
            button.get(i).setBounds((64 * (i - trainers)) + 756, 334, 64, 80);
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
        button_finish = new JButton();
        button_finish.setText("Register and play!");
        button_finish.setBounds(textfieldX + 32, textfieldY + 100, 196, 30);
        button_finish.setVisible(true);
        button_finish.setEnabled(true);
        button_finish.addActionListener(new ActionListener(){
            
            
            @Override
            public void actionPerformed(ActionEvent e) {
                client.register(field[2].getText(), String.copyValueOf(passw.getPassword()), choice, field[0].getText(), Integer.parseInt(field[1].getText()));
                dispose();
            }
        });
        //add(connect);
        add(button_finish);

        
        
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
