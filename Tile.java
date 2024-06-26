/**
 * Creates a Tile object that can be added to the Carcossonne board.
 *
 * @author Robert Leoni
 */
public class Tile implements Cloneable {
    int[] sides = new int[4];
    private boolean cityConnects;
    private boolean fortified;
    private int monastery;
    private boolean roadConnects;

    public Tile() {
        for (int i = 0; i < 4; i++) {
            sides[i] = -2;
        }
        cityConnects = false;
        fortified = false;
        monastery = -1;
        roadConnects = false;
    }// construsts an empty tile

    public Tile(int t, int l, int d, int r, boolean cc, boolean f, int m) {
        sides[0] = t;
        sides[1] = l;
        sides[2] = d;
        sides[3] = r;

        cityConnects = cc;
        fortified = f;
        monastery = m;

        int roads = 0;
        for (int i : sides) {
            if (i == 0)
                roads++;
        }
        roadConnects = roads < 3;
        // robert be like roadConnects = ((t == 0 && r == 0 && d != 0 && l != 0) || (t
        // != 0 && r == 0 && d == 0 && l != 0) || (t != 0 && r != 0 && d == 0 && l == 0)
        // || (t == 0 && r != 0 && d != 0 && l == 0) || (t == 0 && r != 0 && d == 0 && l
        // != 0) || (t != 0 && r == 0 && d != 0 && l == 0));
    }// constructs a tile with specified values

    public Tile(String save) {
        this();
        for (int i = 0; i < 4 && save.length() > 1; i++) {
            try {
                sides[i] = Integer.parseInt(save.substring(0, 2));
            } catch (NumberFormatException e) {
                System.out.println("ERROR: " + save);
            }
            save = save.substring(2);
        }

        cityConnects = true;
        if (save.length() > 0)
            switch (save.charAt(0)) {
                case 'X':
                    cityConnects = false;
                    break;
                case 'M':
                    monastery = Integer.parseInt(save.substring(1, 2));
                    break;
                case 'F':
                    fortified = true;
                    break;
                case ';':
                    break;// no special conditions
                case '0':
                    break;// empty tile
                default:
                    System.out.println("TILE PARSE ERROR: " + save);
            }

        int roads = 0;
        for (int i : sides) {
            if (i == 0)
                roads++;
        }
        roadConnects = roads < 3;
    }

    public int getSide(int side) {
        if (side < 4)
            return sides[side];
        else
            return 0;// default case - see main ln. 73
    }// returns the full side int, including color and type

    public int getSideType(int side) {
        if (sides[side] < 0) {
            return sides[side];
        }
        return sides[side] % 2;
    }// returns side type only

    public int getSideColor(int side) {
        if (sides[side] < 0) {
            return sides[side];
        }
        return sides[side] / 2;
    }// returns side color only, or 0 for unclaimed

    public int getMonastery() {
        return monastery;
    }

    public boolean getFortified() {
        return fortified;
    }

    public boolean getCityConnect() {
        return cityConnects;
    }

    public boolean getRoadConnect() {
        return roadConnects;
    }

    public void setMonastery(int m) {
        monastery = m;
    }

    public void setSideColor(int side, int color) {
        sides[side] = (color * 2) + (sides[side] % 2);
    }// changes a specified side so that the type remans the same bu the color
     // changes

    public void rotate(int rotates) {
        int hold;
        for (int i = rotates - 1; i > 0; i--) {
            hold = sides[0];
            sides[0] = sides[1];
            sides[1] = sides[2];
            sides[2] = sides[3];
            sides[3] = hold;
        }
    }

    public static String getKey() {
        char c = 9553;
        return " --------------------------- \n" +
                "| =," + c + "                 roads |\n" + // u2016
                "| U,),∩,(           castles |\n" + // u2229
                "| X          no connections |\n" + // u039B
                "| M               monastery |\n" +
                "| F               fortified |\n" +
                "| 1,2,3,4  player # meeples |\n" +
                " --------------------------- ";
    }

    public String toString(int line) {
        String ln = "";
        if (sides[0] == -2) {
            return "     ";
        }
        switch (line) {
            case 0:
                char t;
                String t1, l1;
                if (sides[0] == -1)
                    t = ' ';
                else if (sides[0] % 2 == 0)
                    t = 9553;
                else
                    t = 'U';

                if (sides[0] / 2 < 1)
                    t1 = " ";
                else
                    t1 = IO.color(getSideColor(0)) + getSideColor(0) + IO.RESET;

                if (sides[1] / 2 < 1)
                    l1 = IO.color(0) + "·" + IO.RESET;
                else
                    l1 = IO.color(getSideColor(1)) + getSideColor(1) + IO.RESET;

                ln += l1 + " " + t + "" + t1 + IO.color(0) + "|" + IO.RESET;
                break;

            case 1:
                char m, f, l;
                String m1, r;
                if (sides[3] == -1)
                    r = IO.color(0) + "|" + IO.RESET;
                else if (sides[3] % 2 == 0)
                    r = "=";
                else
                    r = "(";

                if (sides[1] == -1)
                    l = ' ';
                else if (sides[1] % 2 == 0)
                    l = '=';
                else
                    l = ')';

                if (fortified)
                    f = 'F';
                else
                    f = ' ';

                if (monastery > -1)
                    m = 'M';
                else if (!roadConnects || !cityConnects)
                    m = 'X';
                else
                    m = ' ';

                if (monastery > 0)
                    m1 = IO.color(monastery) + monastery + "" + IO.RESET;
                else
                    m1 = " ";

                ln += "" + l + "" + f + "" + m + "" + m1 + "" + r;
                break;

            case 2:
                String d, d1, r1;

                if (sides[2] == -1)
                    d = IO.color(0) + "-" + IO.RESET;
                else if (sides[2] % 2 == 0)
                    d = "" + (char) 9553;
                else
                    d = "∩";

                if (sides[2] / 2 < 1)
                    d1 = IO.color(0) + "-" + IO.RESET;
                else
                    d1 = IO.color(getSideColor(2)) + getSideColor(2) + "" + IO.RESET;

                if (sides[3] / 2 < 1)
                    r1 = IO.color(0) + "-" + IO.RESET;
                else
                    r1 = IO.color(getSideColor(3)) + getSideColor(3) + "" + IO.RESET;
                ln += IO.color(0) + "-" + IO.RESET + d1 + d + IO.color(0) + "-" + IO.RESET + r1;
        }
        return ln;
    }

    public String toString() {
        return "    1   \n  " + toString(0) + "\n2 " + toString(1) + " 4\n  " + toString(2) + "\n    3     \n";
    }

    public String save() {
        if (sides[0] == -2)
            return "0";
        String save = "";
        for (int i : sides) {
            if (i < 10 && i >= 0)
                save += '0';
            save += i;
        }
        if (!cityConnects)
            save += 'X';
        if (monastery > -1)
            save += "M" + monastery;
        if (fortified)
            save += 'F';
        return save;
    }

    public boolean equals(Tile t) {
        
        if ((fortified != t.getFortified())
            || ((monastery > -1) != (t.getMonastery() > -1))
            || (cityConnects != t.getCityConnect()))
                return false;
        for (int r = 0; r < 4; r++) {
            
            for (int i = 0; i < 4; i++) {
                if (getSideType(i) != t.getSideType(i)) {
                    break;
                }
                if (i == 3) return true;
            }
            
            rotate(2);
        }
        return false;
    }
}