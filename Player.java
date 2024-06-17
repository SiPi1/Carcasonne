/**
 * This wil create the Player object which saves data related to the player
 * 
 * @author Robert Leoni
 */
public class Player{
    
    private static int playerNum = 1;
    private String name;
    private int color;
    private int meeples;
    
    public Player(String n){
        name = n;
        color = playerNum;
        meeples = 7;
        playerNum++;
    }
    
    public int getNumber(){
        return color;
    }
    
    public String getName(){
        return name;
    }
    
    public int getMeeples(){
        return meeples;
    }
    
    public void setMeeples(int num){
        meeples += num;
    }
}