/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gebruiker
 */
public class NewClient {
    private final int port;
    private DatagramSocket socket;
    
    private InetAddress ip;
    private byte[] buffer = new byte[1024];
    public NewClient(int port) throws IOException
    {
        this.port = port;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(NewClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        setupConnection();
    }
    
    private void setupConnection()
    {
        try{
        this.socket = new DatagramSocket();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void run()
    {
        String join = "j:Stefan";
        
         DatagramPacket dp = new DatagramPacket(join.getBytes(), join.length(), ip, 2406);
         
        try {
            socket.send(dp);
        } catch (IOException ex) {
            Logger.getLogger(NewClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int x = 0, y = 0;
        while(true){
            try{
                dp = new DatagramPacket(buffer, 1024);
                this.socket.receive(dp);
                String received = new String(dp.getData(), 0, dp.getLength()).trim();
                System.out.println(received);
                
                x++;
                y++;
                String loc = "upd:" + x + ":" + y;
                dp = new DatagramPacket(loc.getBytes(), loc.length(), ip, 2406);
                this.socket.send(dp);
                Thread.sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        NewClient app;
        try {
            app = new NewClient(2406);
            app.run();
        } catch (IOException ex) {
            Logger.getLogger(NewClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
