/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemultiplayerclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import lib.AESencrp;
import lib.Attack;
import lib.Database;
import lib.Pokedex;
import lib.Pokemon;
import lib.Stats;
import ser.ChatLine;
import ser.PokeDataPackage;

/**
 *
 * @author Gebruiker
 */
public class PokeMultiplayerClient extends JComponent {
    private InetAddress ip;
    private String ip_string;
    private PokeMultiplayerClient pmc = this;
    private int port;
    int zone = 0, subzone = 0;
    int my_id;
    
    private DatagramSocket socket;
    Socket socket_other;
    Socket socket_states;
    Socket socket_chat;
    Socket socket_pdp;
    
    String version = "v0.04";
    String version_code = "K@#H@DS*SDHK%";
    
    private byte[] buffer = new byte[1024];
    
    String sendString = null;
    
    private Database db;
    
    ScreenStart screenStart;
    ScreenOfflineLogin screenLogin;
    ScreenOverworld scrOv;
    ScreenBattle screenBattle;
    
    Map<Integer, OtherPlayer> list_others;
    int others_size = 0;
    
    Player player;
    String password;
    String username;
    int chartype;
    
    Map<Integer, Pokemon> pokemon_owned;
    Pokemon[] my_pokemon_party = new Pokemon[6];
    Pokemon[] other_pokemon_party = new Pokemon[6];
    
    private boolean offline = false;
    char choice = 'n';
    
    private boolean sendChatAllowed = false;
    ArrayList<String> list_unsentChatLines;
    
    public static void main(String[] args)
    {
        new PokeMultiplayerClient();
    }
    

    public PokeMultiplayerClient()
    {
        screenStart = new ScreenStart(this);
        
        list_others = new HashMap<Integer, OtherPlayer>();
        pokemon_owned = new HashMap<Integer, Pokemon>();
        
        list_unsentChatLines = new ArrayList<String>();
        
    }
    
    public void openOfflineLogin()
    {
        screenLogin = new ScreenOfflineLogin(this);
    }
    
    public void setOfflineClient(String username, int charType)
    {
        offline = true;
        player = new Player(username, charType);
        player.setStart(charType, 960, 640-16, 0, 0, 0);
        scrOv = new ScreenOverworld(pmc, player);
        scrOv.map.setMapOnLogin(0);
        if(player.charType == 6) scrOv.marge = 4;
        scrOv.offline = true;
        
        System.out.println("Ik doe GEEN offline client.");
        
    }
    
    public void setOnlineClient(String ip, int port, String username, String password, int charType)
    {
        System.out.println("setOnlineClient(" + username + ", " + charType + "," + ip +", " + port + ")");
        
        try {
            socket = new DatagramSocket();
            socket_states = new Socket(ip, port + 1);
            socket_chat = new Socket(ip, port + 3);
            socket_pdp = new Socket(ip, port + 4);
            
            while(socket_states == null) {}
        
    //        try {
    //            this.ip = InetAddress.getByName(ip);
    //            this.ip_string = ip;
    //        } catch (UnknownHostException ex) {
    //            System.err.println("Nope!");
    //            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
    //        }
            this.username = username;
            this.chartype = charType;
            this.password = password;

            try {
                this.ip = InetAddress.getByName(ip);
                this.port = port;
            } catch (UnknownHostException ex) {
                Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
            }



            client_start.run();
        } catch (ConnectException ex) {
            JOptionPane.showMessageDialog(this, "Could not connect to server!", "Hmm... ", JOptionPane.ERROR_MESSAGE);
            System.err.println("Kon niet verbinden met een poort");
            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Something went wrong while connecting to server!", "Hmm... ", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

        
        
    }
    
    private Runnable client_start = new Runnable()
    {
        
        DataOutputStream dos;
        DataInputStream dis;
        @Override
        public void run() {
            try {
                Socket so = socket_states;
                dos = new DataOutputStream(so.getOutputStream());
                dis = new DataInputStream(so.getInputStream());
                
                try {
                    String encr_password = AESencrp.encrypt(password);
                    System.out.println(password + " and " + encr_password);
                    dos.writeUTF(username + ":" + encr_password);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JPanel(), "Sorry, something went wrong with your password.", "Woops!", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                
                
                
                
                String receive_start = dis.readUTF();
                String[] args = receive_start.split(":");
                my_id = Integer.parseInt(args[0]);
                
                if(my_id > 0)
                {
                    player = new Player(username, chartype); 
                    db = new Database(player.username);
                    
                    System.out.println("my_id = " + my_id);
                    System.out.println("setStart(" + Integer.parseInt(args[1]) + "," + Integer.parseInt(args[2]) + "," + Integer.parseInt(args[3]) + "," + Integer.parseInt(args[4]) + "," + Integer.parseInt(args[5]) + "," + Integer.parseInt(args[6]) );
                    player.setStart(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
                    player.username = args[7];
                    
                    new Thread(pdp_receive).start();
                    new Thread(chat_receive).start();
                    new Thread(TCP_receive).start();
                    new Thread(udp_receive).start();
                    screenStart.dispose();
                }
                else if(my_id == -1)
                {
                    JOptionPane.showMessageDialog(new JPanel(), "Wrong password.", "Woops!", JOptionPane.INFORMATION_MESSAGE);
                    screenStart.passw.requestFocusInWindow();
                    screenStart.passw.selectAll();
                }
                else if(my_id == -2)
                {
                    JOptionPane.showMessageDialog(new JPanel(), "Username does not exist.", "Aww!", JOptionPane.INFORMATION_MESSAGE);
                    screenStart.passw.setText("");
                    screenStart.field[2].requestFocusInWindow();
                    screenStart.field[2].selectAll();
                }
                
                
            } catch (IOException ex) {
                Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    };
    
    public void sendChatLine(String string)
    {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket_chat.getOutputStream());
            oos.writeObject(new ChatLine(player.username, list_unsentChatLines.get(0)));
            list_unsentChatLines.remove(0);
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addChatLine(String string)
    {
        scrOv.list_chatLines.add(string);
    }
    
//    private Runnable chat_send = new Runnable()
//    {
//
//        @Override
//        public void run() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//    };
    
    private Runnable pdp_receive = new Runnable() {

        @Override
        public void run() {
            ObjectInputStream ois;
            while(true)
            {
                try {
                    ois = new ObjectInputStream(socket_pdp.getInputStream());
                    PokeDataPackage pdp = (PokeDataPackage) ois.readObject();
                    
                    String username = "";
                    if(pdp.id != -1)
                    {
                        System.out.println("I got a pdp!");
                        screenBattle = new ScreenBattle(scrOv, username, 1, getParty(), pdp, 0);
                        username = list_others.get(pdp.id).username;
                    }
                    else
                    {
                        screenBattle = new ScreenBattle(scrOv, getParty(), pdp, 1);
                    }
                    
                } catch (ClassNotFoundException | IOException ex) {
                    Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };
    
    private Runnable chat_receive = new Runnable() {

        @Override
        public void run() {
            ObjectInputStream ois;
            DataOutputStream dos;
            while(true)
            {
               try {
                    ois = new ObjectInputStream(socket_chat.getInputStream());
                    ChatLine chatLine = null;
                    try {
                        chatLine = (ChatLine) ois.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println("Thanks for the chatLine \"" + chatLine.getLine() + "\".");
                    scrOv.list_chatLines.add(chatLine.getUsername() + ": " + chatLine.getLine());
                } catch (IOException ex) {
                    Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
        }
    };
    
    private Runnable TCP_receive = new Runnable()
    {

        @Override
        public void run() {
            DataInputStream dis = null;
            while(true)
            {
                try {
                    dis = new DataInputStream(socket_states.getInputStream());
                    String receive = dis.readUTF();

                    String[] args = receive.split(":");

                    System.out.println(">>> " + receive);

                    if("ss".equals(args[0]) && my_id != Integer.parseInt(args[1]))
                    {
                        OtherPlayer other = list_others.get(Integer.parseInt(args[1]));
                        other.x = Integer.parseInt(args[2]);
                        other.y = Integer.parseInt(args[3]);
                        other.walking = Integer.parseInt(args[4]);
                        other.zone = Integer.parseInt(args[5]);
                        other.subzone = Integer.parseInt(args[6]);
                        list_others.put(other.id, other);
                    } 
                    else if("sfn".equals(args[0]) && my_id != Integer.parseInt(args[1]))
                    {
                        OtherPlayer other = new OtherPlayer();
                        other.id = Integer.parseInt(args[1]);
                        other.username = args[2];
                        other.charType = Integer.parseInt(args[3]);
                        list_others.put(other.id, other);
                    }
                    else if("sn".equals(args[0]) && my_id != Integer.parseInt(args[1]))
                    {
                        OtherPlayer other = new OtherPlayer();
                        other.id = Integer.parseInt(args[1]);
                        other.username = args[2];
                        other.charType = Integer.parseInt(args[3]);
                        list_others.put(other.id, other);
                        addChatLine(other.username + " has joined the game.");
                    }
                    else if("sw".equals(args[0]))
                    {
                        
                        if(Integer.parseInt( args[1]) == 1 )
                        {
                            System.out.println("Ik heb een welkom ontvangen. player.zone = " + player.zone);
                            scrOv = new ScreenOverworld(pmc, player);
                            System.out.println("Ik heb nu scrOv gemaakt in pmc 357. zone = " + player.zone);
                            scrOv.map.setMapOnLogin(player.zone);
                            System.out.println("setMapOnLogin(" + player.zone + ")");
                            if(player.charType == 6) scrOv.marge = 4;
                        }
                    }
                    else if("syp".equals(args[0]))
                    {
                        System.out.println("Adding a pokemon!");
                        Pokemon newPokemon = new Pokemon(db.pokedex.dex[Integer.parseInt(args[1])], 
                                                        args[2],
                                                        db.pokedex.atk[Integer.parseInt(args[3])],
                                                        db.pokedex.atk[Integer.parseInt(args[4])],
                                                        db.pokedex.atk[Integer.parseInt(args[5])],
                                                        db.pokedex.atk[Integer.parseInt(args[6])],
                                                        new Stats
                                                        (
                                                            Integer.parseInt(args[7]),
                                                            Integer.parseInt(args[8]),
                                                            Integer.parseInt(args[9]),
                                                            Integer.parseInt(args[10]),
                                                            Integer.parseInt(args[11]),
                                                            Integer.parseInt(args[12]),
                                                            Integer.parseInt(args[13]),
                                                            Integer.parseInt(args[14])
                                                        ),
                                                        Integer.parseInt(args[15]),
                                                        Integer.parseInt(args[16]));
                        newPokemon.setExpneeded();
                        newPokemon.setLocation(Integer.parseInt(args[17]));
                        pokemon_owned.put(Integer.parseInt(args[17]), newPokemon);
                        System.out.println("Yay, I added " + args[2]);
                    }
                    else if("sch".equals(args[0]))
                    {
                        addChatLine(args[1] + ": " + args[2]);
                    }
                    else if("sl".equals(args[0])) // Server sends that someone has Left
                    {
                        int id = Integer.parseInt(args[1]);
                        String username = list_others.get(id).username;
                        addChatLine(username + " has left the game.");
                        list_others.remove(id);
                    }
                    else if("sad".equals(args[0])) // Server sends Attack Damage
                    {
                        int attacker = Integer.parseInt(args[1]);
                        int damage = Integer.parseInt(args[2]);
                        String attack_name = args[3];
                        int attack_type = Integer.parseInt(args[4]);
                        int damage_type = Integer.parseInt(args[5]);
                        int attack_speed = Integer.parseInt(args[6]);
                        
                        screenBattle.setAction(attacker, damage, attack_name, attack_type, damage_type, attack_speed);
                    }
                    else if("sexp".equals(args[0])) // Server sends your EXPerience points
                    {
                        int exp = Integer.parseInt(args[1]);
                        screenBattle.setExperience(exp);
                    }
                    else if("sypl".equals(args[0]))
                    {   // 0      1     2     3     4      5    6      7       8       9
                        //sypl : loc : lvl : hp : maxhp : atk : def : spatk : spdef : spd
                        screenBattle.my_party[Integer.parseInt(args[1]) - 1].setHp(Integer.parseInt(args[3]));
                        screenBattle.my_party[Integer.parseInt(args[1]) - 1].setMaxhp(Integer.parseInt(args[4]));
                        screenBattle.my_party[Integer.parseInt(args[1]) - 1].setStrength(Integer.parseInt(args[5]));
                        screenBattle.my_party[Integer.parseInt(args[1]) - 1].setDefense(Integer.parseInt(args[6]));
                        screenBattle.my_party[Integer.parseInt(args[1]) - 1].setSpec_strength(Integer.parseInt(args[7]));
                        screenBattle.my_party[Integer.parseInt(args[1]) - 1].setSpec_defense(Integer.parseInt(args[8]));
                        screenBattle.my_party[Integer.parseInt(args[1]) - 1].setSpeed(Integer.parseInt(args[9]));
                    }
                    else if("sb".equals(args[0]))
                    {
                        if("no".equals(args[1]))
                        {
                            addChatLine("User not found!");
                        }
                    }
                } catch (IOException ex) {
                    //JOptionPane.showMessageDialog(new JPanel(), "Server diconnected!", ":c", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
            
        }
    };
    
    private Runnable udp_receive = new Runnable() {

        @Override
        public void run() {
            byte[] buffer = new byte[2048];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            
            while(true)
            {
                try {
                    socket.receive(dp);
                    String data = new String(dp.getData(), 0, dp.getLength()).trim();
                    String[] args = data.split(":");
                    String command = args[0];
                    //System.out.println("R> " + data);
                    
                    if(command.equals("cc") && my_id != Integer.parseInt(args[1]))
                    {
                        OtherPlayer op = list_others.get(Integer.parseInt(args[1]));
                        op.x = Integer.parseInt(args[2]);
                        op.y = Integer.parseInt(args[3]);
                        op.walking = Integer.parseInt(args[4]);
                        list_others.put(Integer.parseInt(args[1]), op);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };
    
    public void register(String username, String password, int charType, String ip, int port)
    {
        try {
            this.ip = InetAddress.getByName(ip);
            this.port = port;
        } catch (UnknownHostException ex) {
            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String encr_password = "";
        
        try {
            encr_password = AESencrp.encrypt(password);
        } catch (Exception ex) {
            System.err.println("Password niet goed.");
            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            socket_other = new Socket(ip, port + 2);
        } catch (IOException ex) {
            System.err.println("Kon niet verbinden met een poort");
            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        DataOutputStream dos;
        DataInputStream dis;
        try {
            dos = new DataOutputStream(socket_other.getOutputStream());
            dos.writeUTF("cr:" + username + ":" + encr_password + ":" + charType);
            
            dis = new DataInputStream(socket_other.getInputStream());
            String welcome = dis.readUTF();
            
            if( welcome.equals("srf") )
            {
                setOnlineClient(ip, port, username, password, charType);
            } else
            {
                JOptionPane.showMessageDialog(new JPanel(), "Registration failed!", "Oh no!", JOptionPane.ERROR_MESSAGE);
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }

//    public void requestPokedata(int other)
//    {
//        DataOutputStream dos;
//        try {
//            dos = new DataOutputStream(socket_other.getOutputStream());
//            dos.writeUTF("cp:" + + my_id + ":" + other);
//        } catch (IOException ex) {
//            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    public void sendBattleAttack(Pokemon pkmn, Attack atk)
    {
//        DataOutputStream dos;
//        try {
//            dos = new DataOutputStream(socket_states.getOutputStream());
            //int lvl, int str, int spcstr, attackpower, boolean sametype, attackdamagetype, int attacktype
            String data = "cba:" + pkmn.getLevel() + ":" + pkmn.getStrength() + ":" + pkmn.getSpec_strength() + ":" + atk.physical + ":" + atk.damage + ":" + atk.damagetype;
//            dos.writeUTF(data);
            sendTCP(data);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
    
    public Pokemon[] getParty()
    {
        for(int i = 0; i < 6; i++)
        {
            if(pokemon_owned.get(i + 1) != null)
            {
                System.out.println(pokemon_owned.get(1+i));
                my_pokemon_party[i] = pokemon_owned.get(i + 1);
                if(my_pokemon_party[i].nickname.equals("")) my_pokemon_party[i].nickname = my_pokemon_party[i].name;
                System.out.println("my_pokemon_party got a new pet.");
            }
        }
        
        return my_pokemon_party;
    }
    
    public void sendCoords(int x, int y, int w)
    {
        if(offline == false)
        {
            String data = "cc:" + my_id + ":" + x + ":" + y + ":" + w;
            DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), ip, port);
            //System.out.println("Sending to " + ip + " --- " + port);
            try {
                socket.send(dp);
                //System.out.println("Sent " + data);
            } catch (IOException ex) {
                Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void sendState(int x, int y, int w, int z, int sz)
    {
        if(offline == false)
        {
//            try {
                String data = "cs:" + my_id + ":" + x + ":" + y + ":" + w + ":" + z + ":" + sz;
//                DataOutputStream dos = new DataOutputStream(socket_states.getOutputStream());
//                dos.writeUTF(data);
                sendTCP(data);
//            } catch (IOException ex) {
//                Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
//            }
            
        }
    }
    
    public synchronized void sendTCP(String send)
    {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(socket_states.getOutputStream());
            dos.writeUTF(send);
        } catch (IOException ex) {
            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 1 = hp
     * 2 = atk
     * 3 = def
     * 4 = spatk
     * 5 = spdef
     * 6 = spd
     * 7 = maxhp
     * 8 = level
     * 
     * @param me if true, my_pokemon_party
     * @param loc location of pokemon
     * @param type which stat
     * @param setstat new amount
     */
    public synchronized void setStat(boolean me, int loc, int type, int setstat)
    {
        if(me)
        {
            if(type == 1) my_pokemon_party[loc - 1].setHp(setstat);
            if(type == 2) my_pokemon_party[loc - 1].setStrength(setstat);
            if(type == 3) my_pokemon_party[loc - 1].setDefense(setstat);
            if(type == 4) my_pokemon_party[loc - 1].setSpec_strength(setstat);
            if(type == 5) my_pokemon_party[loc - 1].setSpec_defense(setstat);
            if(type == 6) my_pokemon_party[loc - 1].setSpeed(setstat);
            if(type == 7) my_pokemon_party[loc - 1].setMaxhp(setstat);
            if(type == 8) my_pokemon_party[loc - 1].setLevel(setstat);
        }
        else
        {
            if(type == 1) other_pokemon_party[loc - 1].setHp(setstat);
            if(type == 2) other_pokemon_party[loc - 1].setStrength(setstat);
            if(type == 3) other_pokemon_party[loc - 1].setDefense(setstat);
            if(type == 4) other_pokemon_party[loc - 1].setSpec_strength(setstat);
            if(type == 5) other_pokemon_party[loc - 1].setSpec_defense(setstat);
            if(type == 6) other_pokemon_party[loc - 1].setSpeed(setstat);
            if(type == 7) other_pokemon_party[loc - 1].setMaxhp(setstat);
            if(type == 8) other_pokemon_party[loc - 1].setLevel(setstat);
        }
    }

}


    
    
//    public void setInfo(String ip, String port, String username, int charType){
//        try {
//            this.ip = InetAddress.getByName(ip);
//            this.ip_string = ip;
//        } catch (UnknownHostException ex) {
//            System.err.println("Nope!");
//            Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        this.port = Integer.parseInt(port);
//        this.username = username;
//        this.charType = charType;
//        
//        try{
//            setupConnection();
//
//            Thread sendThread = new Thread(send);
//            Thread receiveThread = new Thread(receive);
//
//            sendThread.start();
//            receiveThread.start();
//
//            scrOv = new ScreenOverworld(this, username, charType);
//        }
//        catch(Exception ex)
//        {
//            JOptionPane.showMessageDialog(this, "Could not connect to server.", "Connection failed", JOptionPane.INFORMATION_MESSAGE);
//            int dialogResult = JOptionPane.showConfirmDialog(this, "Would you like to play offline?", "Connection failed", JOptionPane.YES_NO_OPTION);
//            
//            if( dialogResult == JOptionPane.YES_OPTION )
//            {
//                setInfo(username, charType);
//            } 
//            else if( dialogResult == JOptionPane.NO_OPTION )
//            {
//                new ScreenLogin(ip_string, "" + port, username, charType, this);
//            }
//        }
//    }
//    
//    public void setInfo(String username, int charType){
//        this.username = username;
//        this.charType = charType;
//        
//        
//        
//        scrOv = new ScreenOverworld(this, username, charType);
//        scrOv.offline = true;
//    }
//
//    private void setupConnection() throws Exception
//    {
//        this.socket = new DatagramSocket();
//        
//        this.socket_chat = new Socket(ip, port+1);
//        DataOutputStream dos_chat = new DataOutputStream(socket_chat.getOutputStream());
//        dos_chat.writeUTF(username);
//        Runnable receive_chat = new Runnable(){
//            @Override
//            public void run() {
//                Socket so = socket_chat;
//                DataInputStream dis;
//                while(true)
//                {
//                    try {
//                        dis = new DataInputStream(socket_chat.getInputStream());
//                        String received = dis.readUTF();
//                        scrOv.addText(received);
//                        
//
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }    
//        };
//        new Thread(receive_chat).start();
//        //update(this.username, this.charType, 0, 0);
//        
//        
//        this.socket_states = new Socket(ip, port+2);
//        DataOutputStream dos_state = new DataOutputStream(socket_states.getOutputStream());
//        dos_state.writeUTF("cj:" + username + ":" + charType + ":" + 0 + ":" + 0); // out username:charType:zone:subzone
//        
//        Runnable receive_states = new Runnable()
//        {
//
//            @Override
//            public void run() {
//                DataInputStream dis;
//                while(true)
//                {
//                    try{
//                        dis = new DataInputStream(socket_states.getInputStream());
//                        String receiveddata = dis.readUTF();
//                        String[] args = receiveddata.split(":");
//                        String command = args[0];
//                        handleCommand(username, args);
//
//                    } catch (Exception ex){
//                        ex.printStackTrace();
//                    }
//                }
//            }
//            
//        };
//        
//           
//        
//        
//    }
//    
//    public void sendChat(String send)
//    {
//        DataOutputStream dos = null;
//        try {
//            Socket so = socket_chat;
//            dos = new DataOutputStream(so.getOutputStream());
//            
//            dos.writeUTF(send);
//        } catch (IOException ex) {
//            System.err.println("Error: Could not send chat line \"" + send + "\".");
//        
//        }
//    }
//
//    public void update(String username, int charType, int zone, int subzone){ // client join O
//        sendString = "cj:" + username + ":" + charType + ":" + zone + ":" + subzone;
//    }
//    
//    public void update(int exit)
//    {
//        sendString = "cX:" + exit;
//    }
//    
//    public void update(int x, int y, int walking){ // client coords-update O
//        sendString = "cc:" + x + ":" + y + ":" + walking;
//    }
//    
//    public void update(int x, int y, int walking, int zone, int subzone){ // client zone update
//        sendString = "cz:" + x + ":" + y + ":" + walking + ":" + zone + ":" + subzone;
//    }
//    
//    public void update(boolean battle){ // client battle
//        sendString = "cb:" + battle;
//    }
//    
//    public void update(){
//        sendString = null;
//    }
//     
//    private Runnable send = new Runnable() {
//        @Override
//        public void run() {
//            DatagramPacket dp;
//            String sendStringBuffered;
//            while(true){
//                if(sendString != null){
//                    sendStringBuffered = sendString;
//                    dp = new DatagramPacket(sendStringBuffered.getBytes(), sendStringBuffered.length(), ip, port);
//                     try {
//                        socket.send(dp);
//                    } catch (IOException ex) {
//                        Logger.getLogger(NewClient.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                     sendString = null;
//                }
//            }
//        }
//    };
//    
//    private Runnable receive = new Runnable() {
//        @Override
//        public void run() {
//            DatagramPacket dp = new DatagramPacket(buffer, 1024);
//            while(true){
//                try {
//                    socket.receive(dp);
//                    String data = new String(dp.getData(), 0, dp.getLength()).trim();
//                    String[] args = data.split(":");
//                    String command = args[0];
//                    //System.out.println(data);
//                    handleCommand(command, args);
//                    dp = new DatagramPacket(buffer, 1024);
//                } catch (IOException ex) {
//                    Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            
//        }
//        
//    };
//    
////    sendAll("Supd:" + c.id + ":" + x + ":" + y);
//    
//    private void handleCommand(String command, String[] args){
//        if (command.equals("sw")){ // server welcome I | id
//            my_id = Integer.parseInt(args[1]);
//            System.out.println("YAAAAAAAAAAAAAAAAAAAAAAY ID NUMBER " +my_id);
////            for(int i = 0; i < my_id; i++){
////                others.
////            }
//        } else if( command.equals("sD") ){ // server received a disconnect | id : type
//            others.remove(Integer.parseInt(args[1]));
//        } else if( command.equals("sK") ){
//            update(1);
//            JOptionPane.showMessageDialog(scrOv, "You have been kicked! :(", "Disconnected", JOptionPane.INFORMATION_MESSAGE);
//            
//            
//            System.exit(0);
//        } else if (command.equals("sn")){ // server new player I | id : username : charType : zone : subzone
//            OtherPlayer op = new OtherPlayer();
//            int recID = Integer.parseInt(args[1]);
//            
//            op.id = recID;
//            op.username = args[2];
//            op.charType = Integer.parseInt(args[3]);
//            op.zone = Integer.parseInt(args[4]);
//            op.subzone = Integer.parseInt(args[5]);
//            op.walking = 0;
//            
//            others.put(recID, op);
//
//            System.out.println("Added(" + recID + ")" + op.username);
//            others_size++;
//        } else if (command.equals("sc") && !username.equals(args[1])){ // server coords-update I | id : x : y  : walking
//            
//            int received_id = Integer.parseInt(args[1]);
//            
//            if( received_id != my_id)
//            {
//                //System.out.println("received_id = " + received_id);
//
//                OtherPlayer op = others.get( Integer.parseInt(args[1]) );
//                if( op == null )
//                {
//                    op = new OtherPlayer();
//                    op.id = received_id;
//                }
//                //System.out.println("This is op.x: " + op.x);
//
////                System.out.println(">>> Updating index of " + received_id + " with (" + args[2] + ", " + args[3] + ")");
//                op.x = Integer.parseInt(args[2]);
//                op.y = Integer.parseInt(args[3]);
//                op.walking = Integer.parseInt(args[4]);
//
//                others.put(received_id, op);
//
//                //System.out.println("<<< Index updated of " + received_id + " with (" + args[2] + ", " + args[3] + ")");
//            }
//        } else if (command.equals("sz") ){ // server zone update I | id : x : y  : walking : zone : subzone
//            
//            int received_id = Integer.parseInt(args[1]);
//            
//            if( received_id != my_id)
//            {
//                OtherPlayer op = others.get( received_id );
//
//                op.x = Integer.parseInt(args[2]);
//                op.y = Integer.parseInt(args[3]);
//                op.walking = Integer.parseInt(args[4]);
//                op.zone = Integer.parseInt(args[5]);
//                op.subzone = Integer.parseInt(args[6]);
//
//                others.put(received_id, op);
//
//            }
//        } else if( command.equals("sb") ) { // server battle update I | id : battling
//            int received_id = Integer.parseInt(args[1]);
//            
//            if( received_id != my_id )
//            {
//                OtherPlayer op = others.get( received_id );
//                
//                op.battling = Boolean.parseBoolean(args[2]);
//            }
//        }
//    }
//    
//    public static void main(String[] args) {
//        try {
//            PokeMultiplayerClient pmc = new PokeMultiplayerClient();
//        } catch (IOException ex) {
//            System.err.println("Hier gaat ECHT iets fout...");
//        }
//    }
//    
//    
//}
    
    
    
    
    
    
    
/* 
 * ===============================================================================================================================================================
 *  Under this line, the old OLD client is stored.
 * ===============================================================================================================================================================
 */ 
    
    
    
    
    
    
//    String ip = "undefined", username = "Unknown Object";
//    int port;
//    int choice;
//    
//    boolean offline = false;
//    
//    Socket socket;
//    
//    int client_state = 5;
//    
//    ArrayList<DataPackage> list_data_packages = new ArrayList();
//    
//    ScreenLogin screenLogin;
//    ScreenOverworld screenOverworld;
//    
//    public PokeMultiplayerClient(){
//        screenLogin = new ScreenLogin(this);
//        while(ip.equals("undefined")){}
//        
//        try{
//            
//            
//            if(offline == false) {
//                port = Integer.parseInt(this.ip.substring(this.ip.indexOf(":") + 1));
//                String ip = this.ip.substring(0, this.ip.indexOf(":"));
//                socket = new Socket(ip, port);
//                ObjectOutputStream oos = new ObjectOutputStream((socket.getOutputStream()));
//                oos.writeObject(username);
//
//                ObjectInputStream ois = new ObjectInputStream((socket.getInputStream()));
//                String response = (String) ois.readObject();
//
//                JOptionPane.showMessageDialog(null, response, "Message", JOptionPane.INFORMATION_MESSAGE);
//            }
//
//            
//            
//            
//            screenOverworld = new ScreenOverworld(this, username, choice);
//            
//            
//            if(offline == false){
//                new Thread(send).start();
//                new Thread(receive).start();
//            }
//            
//            
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//        
//        
//        
//    }
//    
//    Runnable send = new Runnable(){
//        @Override
//        public void run(){
//            
//                ObjectOutputStream oos;
//                int datatype = 1;
//                
//                try {    
//                    oos = new ObjectOutputStream(socket.getOutputStream());
//                    oos.writeObject(client_state);
//
//                    oos.writeObject(datatype); //type 0, general package
//                    oos.writeObject(screenOverworld.player.x);
//                    oos.writeObject(screenOverworld.player.y);
//                    oos.writeObject(screenOverworld.player.walking);
//                    oos.writeObject(screenOverworld.player.username);
//                    oos.writeObject(screenOverworld.player.zone);
//                    oos.writeObject(screenOverworld.player.subzone);
//                } catch (IOException ex) {
//                    Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//                datatype = 0;
//                while(true){
//                    try {
//                        oos = new ObjectOutputStream(socket.getOutputStream());
//                        oos.writeObject(client_state);
//                        
//                        oos.writeObject(datatype); //type 0, general package
//                        oos.writeObject(screenOverworld.player.x);
//                        oos.writeObject(screenOverworld.player.y);
//                        oos.writeObject(screenOverworld.player.walking);
//                    } catch (IOException ex) {
//                        Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            
//        }
//    };
//    
//    
////    Runnable send = new Runnable(){
////        @Override
////        public void run() {
////            ObjectOutputStream oos;
////            byte zone = 0;
////            byte subzone = 0;
////            String chatline = null;
////            
////            while(true){
////                try {
////                    
////                    oos = new ObjectOutputStream((socket.getOutputStream()));
////                    oos.writeObject(client_state);
////                            
////                    DataPackage dp = new DataPackage();
////                    dp.x = screenOverworld.player.x;
////                    dp.y = screenOverworld.player.y;
////                    dp.username = username;
////                    dp.charType = (byte) choice;
////                    dp.walking = (byte) screenOverworld.player.walking;
////                    dp.zone = zone;
////                    dp.subzone = subzone;
////                    oos = new ObjectOutputStream((socket.getOutputStream()));
////                    oos.writeObject(dp);
////                    
////                    oos = new ObjectOutputStream(socket.getOutputStream());
////                    oos.writeObject(chatline);
////                            
////                            
////                } catch (IOException ex) {
////                    Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
////                }
////                
////                
////            }
////        }
////
////    };
//    
//    Runnable receive = new Runnable(){
//        @Override
//        public void run() {
//            ObjectInputStream ois;
//            
//            while(true){
//                try {
//                    ois = new ObjectInputStream(socket.getInputStream());
//                    int client_state = (int) ois.readObject();
//                    
//                    int client_list_size = (int) ois.readObject();
//                    
//                    for(int i = 0; i < client_list_size; i++){
//                        int datatype = (int) ois.readObject();
//                        DataPackage dp = new DataPackage();
//                        dp.x = (int) ois.readObject();
//                        dp.y = (int) ois.readObject();
//                        dp.walking = (int) ois.readObject();
//
//                        if(datatype == 1){
//                            dp.username = (String) ois.readObject();
//                            dp.zone = (int) ois.readObject();
//                            dp.subzone = (int) ois.readObject();
//                        } else if (datatype == 0){
//                            dp.username = list_data_packages.get(i).username;
//                            dp.zone = list_data_packages.get(i).zone;
//                            dp.subzone = list_data_packages.get(i).subzone;
//                        }
//                        
//                        if(list_data_packages.size() < client_list_size && i == client_list_size - 1){
//                            list_data_packages.add(i,dp);
//                        } else{
//                            list_data_packages.set(i,dp);
//                        }
//                        
//                        
//                        
//                    }
//                    
//                            
//                            
//                            
//                            
//                } catch (Exception ex) {
//                    Logger.getLogger(PokeMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    };
//    
////    Runnable receive = new Runnable(){
////
////        @Override
////        public void run() {
////            ObjectInputStream ois;
////            
////            while(true){
////                try {
//////                    System.out.println("===================================");
//////                    System.out.println("Starting run thread");
//////                    System.out.println("Receiving client_state...");
////                    ois = new ObjectInputStream(socket.getInputStream());
////                    client_state = (Byte) ois.readObject();
//////                    System.out.println("Received client_state (" + client_state + ")");
////                    
//////                    System.out.println("Receiving list_data_packages...");
////                    ois = new ObjectInputStream(socket.getInputStream());
////                    list_data_packages = (ArrayList<DataPackage>) ois.readObject();
//////                    System.out.println("Received list_data_packages");
////                    
////                    String received_chat_list;
//////                    System.out.println("Receiving chat_list...");
////                    ois = new ObjectInputStream(socket.getInputStream());
////                    received_chat_list = (String) ois.readObject();
//////                    System.out.println("Received chat_list");
////                    
////                    if(client_state == 1){ // got kicked
////                        JOptionPane.showMessageDialog(null, "You got kicked :(", "Message", JOptionPane.INFORMATION_MESSAGE);
////                        System.exit(0);
////                    } else if(client_state == 2){ // server shut down
////                        JOptionPane.showMessageDialog(null, "Server shut down.", "Message", JOptionPane.INFORMATION_MESSAGE);
////                        System.exit(0);
////                    }
////                    
//////                    for(int i = 0; i < list_data_packages.size(); i++){
//////                        System.out.println("Ik ontving dat " + i + " op ("+list_data_packages.get(i).x + ", " + list_data_packages.get(i).y + ") is.");
//////                    }
//////                    
//////                    System.out.println("Finished run thread");
//////                    System.out.println("==================================");
////                    
////                    
////                    
////                    
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                }
////            }
////        
////        }
////        
////    };
//
//    public void setInfo(String ip, String username, byte choice){
//        this.ip = ip;
//        this.username = username;
//        this.choice = choice;
//    }
//    
//    public void setInfo(String username, int choice){
//        this.offline = true;
//        this.ip = "n/a";
//        this.username = username;
//        this.choice = choice;
//    }
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        PokeMultiplayerClient pmc = new PokeMultiplayerClient();
//    }
//
//}
