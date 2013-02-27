package pokemultiplayerclient;

import java.util.ArrayList;

public class TextArray {
    private String desc;
        
    private ArrayList<String> text = new ArrayList();
    private String output;
    private int current = 0;
    
    public TextArray(String desc){
        this.desc = desc;
    }
    
    public TextArray(){
        this.desc = "No description.";
    }
    
    public void addText(String text){
        this.text.add(text);
    }
    
    public int getAmount(){
        return this.text.size();
    }
    
    public String showText(){
        if (current < text.size()){
            output = text.get(current);
            current++;
            return output;
        } else {
            current = 0;
            return null;
        }
    }
    
}

