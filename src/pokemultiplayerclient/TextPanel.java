/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Gebruiker
 */
public class TextPanel {
    int type = 0;
    int topLeftX = 0, topLeftY = 0, width = 0, height = 0;
    ImageIcon window1Bottom, window1BottomLeft, window1BottomRight, window1Left, window1Right, window1Top, window1TopLeft, window1TopRight;
    Image window1BottomImage, window1BottomLeftImage, window1BottomRightImage, window1LeftImage, window1RightImage, window1TopImage, window1TopLeftImage, window1TopRightImage;
    
    Image bottom, bottomLeft, bottomRight, left, right, top, topLeft, topRight;
 
    Font font_rusa, font_chat, font_usernameHead, font_rusaBattle;
    
    public TextPanel(){
        try {
            File fontfile = null;
            InputStream in = getClass().getResourceAsStream("/font/rusa.ttf");
            Font fontje = Font.createFont(Font.TRUETYPE_FONT, in);
            this.font_chat = fontje.deriveFont(Font.PLAIN, 14);
            this.font_usernameHead = fontje.deriveFont(Font.BOLD, 14);
            this.font_rusa = fontje.deriveFont(Font.PLAIN, 12);
            this.font_rusaBattle = fontje.deriveFont(Font.PLAIN, 38);
        } catch (FontFormatException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
            window1Bottom = new ImageIcon(getClass().getResource("/img/window/windowBottom.png"));
            window1BottomLeft = new ImageIcon(getClass().getResource("/img/window/windowBottomLeft.png"));
            window1BottomRight = new ImageIcon(getClass().getResource("/img/window/windowBottomRight.png"));
            window1Left = new ImageIcon(getClass().getResource("/img/window/windowLeft.png"));
            window1Right = new ImageIcon(getClass().getResource("/img/window/windowRight.png"));
            window1Top = new ImageIcon(getClass().getResource("/img/window/windowTop.png"));
            window1TopLeft = new ImageIcon(getClass().getResource("/img/window/windowTopLeft.png"));
            window1TopRight = new ImageIcon(getClass().getResource("/img/window/windowTopRight.png"));

            window1BottomImage = window1Bottom.getImage();
            window1BottomLeftImage = window1BottomLeft.getImage();
            window1BottomRightImage = window1BottomRight.getImage();
            window1LeftImage = window1Left.getImage();
            window1RightImage = window1Right.getImage();
            window1TopImage = window1Top.getImage();
            window1TopLeftImage = window1TopLeft.getImage();
            window1TopRightImage = window1TopRight.getImage();
        
    }
    
    public void setTextPanel(int x, int y, int width, int height, int type){
        this.topLeftX = x;
        this.topLeftY = y;
        this.width = width;
        this.height = height;
        this.type = type;
        
        if (type == 1){
            top = window1TopImage.getScaledInstance(width-128, 64, java.awt.Image.SCALE_FAST);
            bottom = window1BottomImage.getScaledInstance(width-128, 64, java.awt.Image.SCALE_FAST);
            left = window1LeftImage.getScaledInstance(64, height-128, java.awt.Image.SCALE_FAST);
            right = window1RightImage.getScaledInstance(64, height-128, java.awt.Image.SCALE_FAST);
            
            bottomLeft = window1BottomLeftImage;
            bottomRight = window1BottomRightImage;
            topLeft = window1TopLeftImage;
            topRight = window1TopRightImage;
        }
        
        
    }
    
    public void setTextPanel(){
        this.topLeftX = 0;
        this.topLeftY = 0;
        this.width = 0;
        this.height = 0;
        this.type = 0;
    }
    
    
}
