/**
 * Write a description of class Scoreboard here.
 *
 * @author Miles
 */
import java.util.*;
public class Scoreboard
{
    private ArrayList<Integer> scores;
    private ArrayList<String> names;
    public Scoreboard (ArrayList<Player> n) {
        scores = new ArrayList<Integer>();
        names = new ArrayList<String>();
        for (Player p: n) {
            scores.add(0);
            names.add(p.getName());
        }
    }

    public Scoreboard (String save) {
        scores = new ArrayList<Integer>();
        names = new ArrayList<String>();
        while (!save.equals("")) {
            names.add(save.substring(0, save.indexOf('=')));
            scores.add(Integer.parseInt(  save.substring( save.indexOf('=') + 1, save.indexOf(';') )  ));
            save = save.substring(save.indexOf(';') + 1);
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

    public ArrayList<String> getPlayers() {
        return names;//do we need this?
    }

    public String save() {
        String save = "";
        for (int i = 0; i < names.size(); i++) {
            save += names.get(i) + "=" + scores.get(i) + ";";
        }
        return save;
    }
}