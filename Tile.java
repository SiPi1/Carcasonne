/**
 * Creates a Tile object that can be added to the Carcossonne board.
 *
 * @author Robert Leoni
 * @version 6-5-24
 */
public class Tile implements Cloneable
{
    int[] sides = new int[4];
    private boolean cityConnects;
    private boolean fortified;
    private int monastery;
    private boolean roadConnects;

    public Tile(){
        for (int i = 0; i < 4; i++) {
            sides[i] = -2;
        }
        cityConnects = false;
        fortified = false;
        monastery = -1;
        roadConnects = false;
    }//construsts an empty tile

    public Tile(int t, int l, int d, int r, boolean cc, boolean f, int m){
        sides[0] = t;
        sides[1] = l;
        sides[2] = d;
        sides[3] = r;
        cityConnects = cc;
        fortified = f;
        monastery = m;
        int roads = 0;
        for (int i: sides) {
            if (i == 0) roads++;
        }
        roadConnects = roads < 3;
        //robert be like roadConnects = ((t == 0 && r == 0 && d != 0 && l != 0) || (t != 0 && r == 0 && d == 0 && l != 0) || (t != 0 && r != 0 && d == 0 && l == 0) || (t == 0 && r != 0 && d != 0 && l == 0) || (t == 0 && r != 0 && d == 0 && l != 0) || (t != 0 && r == 0 && d != 0 && l == 0));
    }//constructs a tile with specified values

    public int getSide(int side){
        if (side < 4) return sides[side];
        else return 0;//default case - see main ln. 73
    }//returns the full side int, including color and type

    public int getSideType(int side){
        if (sides[side] < 0) {
            return sides[side];
        }
        return sides[side] % 2;
    }//returns side type only

    public int getSideColor(int side){
        if (sides[side] < 0) {
            return sides[side];
        }
        return sides[side] / 2;
    }//returns side color only, or 0 for unclaimed
    
    public int getMonastery(){
        return monastery;
    }
    
    public boolean getFortified(){
        return fortified;
    }
    
    public boolean getCityConnect(){
        return cityConnects;
    }
    
    public boolean getRoadConnect(){
        return roadConnects;
    }
    
    public void setMonastery(int m){
        monastery = m;
    }
    
    public void setSideColor(int side, int color){
        sides[side] = (color * 2) + (sides[side] % 2);
    }//changes a specified side so that the type remans the same bu the color changes
    
    public void rotate(int rotates){
        int hold;
        for (int i = rotates; i > 0; i--){
            hold = sides[0];
            sides[0] = sides[1];
            sides[1] = sides[2];
            sides[2] = sides[3];
            sides[3] = hold;
        }
    }
    
    public String getKey(){
        return "/-----------------------------\\\n" +
               "| =," + '\u2016' + "                   roads |\n" +
               "| U,)," + '\u2229' + ",(   connected castles |\n" +
               "| V,>," + '\u039B' + ",< unconnected castles |\n" +
               "| M                 monastery |\n" +
               "| F                 fortified |\n" +
               "| 1,2,3,4,5  player # meeples |\n" +
               "\\-----------------------------/";
    }
    
    public String toString(int line){
        String ln = "";
        if (sides[0] == -2) {
            return "     ";
        }
        switch (line) {
            case 0:
            char t, t1, l1;
            if (sides[0] == -1) {
                t = '-';
            }
            else if (sides[0] % 2 == 0) {
                t = '\u2016';
            }
            else {
                if (cityConnects) t = 'U';
                else t = 'V';
            }
            if (sides[0] / 2 < 1){
                t1 = '-';
            }
            else {
                t1 = (char)('0' + (sides[0] / 2));
            }
            if (sides[1] / 2 < 1){
                l1 = '/';
            }
            else {
                l1 = (char)('0' + (sides[1] / 2));
            }
            ln += l1 + "-" + t + "" + t1 + "\\";
            break;
            case 1:
            char r, m, m1, f, l;
            if (sides[3] == -1) {
                r = '|';
            }
            else if (sides[3] % 2 == 0) {
                r = '=';
            }
            else {
                if (cityConnects) r = '(';
                else r = '<';
            }
            if (sides[1] == -1) {
                l = '|';
            }
            else if (sides[1] % 2 == 0) {
                l = '=';
            }
            else {
                if (cityConnects) l = ')';
                else l = '>';
            }
            if (fortified){
                f = 'F';
            }
            else {
                f = ' ';
            }
            if (monastery > -1){
                m = 'M';
            }
            else {
                m = ' ';
            }
            if (monastery > 0){
                m1 = (char)('0' + monastery);
            }
            else {
                m1 = ' ';
            }
            ln += "" + l + "" + f + "" + m + "" + m1 + "" + r;
            break;
            case 2:
            char d, d1, r1;
            if (sides[2] == -1) {
                d = '-';
            }
            else if (sides[2] % 2 == 0) {
                d = '\u2016';
            }
            else {
                if (cityConnects) d = '\u2229';
                else d = '\u039B';
            }
            if (sides[2] / 2 < 1){
                d1 = '-';
            }
            else {
                d1 = (char)('0' + (sides[2] / 2));
            }
            if (sides[3] / 2 < 1){
                r1 = '/';
            }
            else {
                r1 = (char)('0' + (sides[3] / 2));
            }
            ln += "\\" + d1 + d + "-" + r1;
        }
        return ln;
    }
    
    public String toString(){
        return toString(0) + "\n" + toString(1) + "\n" + toString(2) + "\n";
    }
}