/**
 * Write a description of class Scoreboard here.
 *
 * @author Miles
 * @version 6/5/24
 */
import java.util.*;
public class Scoreboard
{
    private ArrayList<Integer> scores;
    private ArrayList<String> names;
    public Scoreboard (Collection<Player> n) {
        scores = new ArrayList<Integer>();
        names = new ArrayList<String>();
        for (Player p: n) {
            scores.add(0);
            names.add(p.getName());
        }
    }

    public String toString() {
        String print = "The scores are: \n";
        for (int i = 0; i < scores.size(); i++) {
            print += names.get(i) + ", " + scores.get(i) + "\n";
        }
        return print;
    }

    public void score (ArrayList<Integer> newScores) {
        int temp;
        for (int i = 0; i < newScores.size(); i++) {
            temp = scores.get(i);
            scores.set(i, temp + newScores.get(i));
        }
    }
    
    public int getScore (int n) {
        return scores.get(n);
    }
}