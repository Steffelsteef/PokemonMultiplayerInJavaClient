/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemultiplayerclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gebruiker
 */
public class NewNewClient
{
    private int port;
    private InetAddress ip;
    private DatagramSocket socket;
    
    private int myID;
    
    private boolean connected = false;
    
    public java.util.Map<Integer, NewAcceptedClient> list_others;
    
    public NewNewClient()
    {
        openClient(2406);
        
        String join = "j:Test:0";
        
        DatagramPacket dp = new DatagramPacket(join.getBytes(), join.length(), ip, port);
         
        try {
            socket.send(dp);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        list_others = new HashMap<Integer, NewAcceptedClient>();
        
        
        receive.start();
        while(connected == false){}
        send.start();
    }
    
    
    public void openClient(int port)
    {
        try 
        {
            ip = InetAddress.getLocalHost();
            this.port = port;
            socket = new DatagramSocket();
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
    
    Thread receive = new Thread()
    {
        @Override
        public void run()
        {
            byte[] buffer = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buffer, 1024);

            while(true){
                try{
                    socket.receive(dp);

                    String data = new String(dp.getData(), 0, dp.getLength()).trim();
                    String[] args = data.split(":");
                    String command = args[0];
                    System.out.println("   rec> " + data);
                    
                    if( command.equals("w") ) // id
                    {
                        myID = Integer.parseInt(args[1]);
                        System.out.println("Welcome! You have received ID " + args[1]);           
                        connected = true;
                    }
                    else if( command.equals("sc") ) // id : x : y
                    {
                        int recID = Integer.parseInt(args[1]);
                        if (myID != recID)
                        {
                            //System.err.println("Received ID: " + recID);
                            NewAcceptedClient other = list_others.get(recID);
                            other.x = Integer.parseInt(args[2]);
                            other.y = Integer.parseInt(args[3]);

                            list_others.put(recID, other);
                            // Player recID is at point (args[2], args[3])
                        }   
                    }
                    else if( command.equals("sn") ) // id : username : charType
                    {
                        int recID = Integer.parseInt(args[1]);
                        if (myID != recID)
                        {
                            NewAcceptedClient other = new NewAcceptedClient();
                            other.id = recID;
                            other.username = args[2];
                            other.charType = Integer.parseInt(args[3]);

                            list_others.put(recID, other);
                            System.out.println("Added client with ID " + recID);
                        }
                    }
                    
                }
                catch( Exception ex )
                {
                    System.err.println("Error receiving packet.");
                    ex.printStackTrace();
                    
                    System.exit(0);
                }
            }
        }
    };
    
    Thread send = new Thread()
    {
        @Override
        public void run()
        {
            int x = 0, y = 0;
            String coords = "c:" + x + ":" + y;
            System.out.println("Started.");
            while(true)
            {
                try
                {
                    this.sleep(1000);
                    DatagramPacket dp = new DatagramPacket(coords.getBytes(), coords.length(), ip, port);
                    
                    socket.send(dp);
                    System.out.println("    Se> " + coords);

                    x++;
                    y+=2;
                    coords = "c:" + x + ":" + y;



                }
                catch (Exception ex)
                {
                    Logger.getLogger(NewNewClient.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    };
    
    
    
    
    public static void main(String[] args)
    {
        NewNewClient nnc = new NewNewClient();
    }
    
}
