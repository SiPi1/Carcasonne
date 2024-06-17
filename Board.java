import java.util.ArrayList;
public class Board {
    private ArrayList<ArrayList<Tile>> board = new ArrayList<ArrayList<Tile>>();
    private int lastX, lastY;
    private ArrayList<Integer> extraMeeples = new ArrayList<Integer>();
    private ArrayList<Integer> activeSides;

    final int TILE_LINES = 3;
    final int TILE_SPACES = 5;

    final int TOP = 0;
    final int LEFT = 1;
    final int BOTTOM = 2;
    final int RIGHT = 3;

    final int EMPTY = -2;
    final int GRASS = -1;
    final int ROAD = 0;
    final int CITY = 1;

    final int NO_PLAYER = 0;

    public Board() {
        for (int i = 0; i < 3; i++) {
            ArrayList<Tile> row = new ArrayList<Tile>();
            row.add(null); row.add(new Tile()); row.add(null); 
            board.add(row);
        }
        board.get(1).set(1, new Tile(CITY, ROAD, GRASS, ROAD, true, false, -1));
        board.get(1).set(0, new Tile());
        board.get(1).set(2, new Tile());
        lastX = 1;
        lastY = 1;
    }

    public Board(String save) {
        board = new ArrayList<ArrayList<Tile>>();
        lastX = 1;
        lastY = 0;//points to empty tile: Should make all functions involving it not work, but not crash
        String rowSave;
        while (!save.equals("")) {
            rowSave = save.substring(0, save.indexOf('\\'));
            board.add(new ArrayList<Tile>());
            while (!rowSave.equals("")) {
                if (rowSave.indexOf(';') == 0) board.get(board.size() - 1).add(null);
                else board.get(board.size() - 1).add(
                    new Tile(rowSave.substring(0, rowSave.indexOf(';')))
                );
                rowSave = rowSave.substring(rowSave.indexOf(';') + 1);
            }
            save = save.substring(save.indexOf('\\') + 1);
        }
    }

    public ArrayList<Tile> tiles() {
        ArrayList<Tile> allTiles = new ArrayList<Tile>();

        for (ArrayList<Tile> row: board) 
            for (Tile t: row) 

                if (t != null && t.getSide(0) != -2)
                    allTiles.add(t);
        
        return allTiles;
    }

    private boolean exists(int x, int y) {
        return x >= 0
        && x < board.size()
        && y >= 0
        && y < board.get(1).size()
        && board.get(x).get(y) != null 
        && board.get(x).get(y).getSide(TOP) != -2;
    } //change other iterations of 'exists' to .getSide(TOP) != -2

    private boolean isFinished(ArrayList<Tile> element) {
        for (Tile t: element) {
            if (t.getSide(TOP) == -2) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Tile> elementAt(int x, int y, int side) {
        if (!exists(x, y)) return new ArrayList<Tile>();
        if (board.get(x).get(y).getSideType(side) == GRASS) {
            if (board.get(x).get(y).getMonastery() > NO_PLAYER) {
                ArrayList<Tile> element = new ArrayList<Tile>();
                for (int r = x - 1; r < x + 2; r++) {
                    for (int c = y - 1; c < y + 2; c++) {
                        if (board.get(r).get(c) != null) {
                            element.add(board.get(r).get(c));
                        }
                    }
                }
                return element;
            }
            return new ArrayList<Tile>();
        }
        activeSides = new ArrayList<Integer>();
        return elementAt(x, y, side, new ArrayList<Tile>());
    }

    private ArrayList<Tile> elementAt(int x, int y, int side, ArrayList<Tile> remainder) {
        if (!board.get(x).get(y).getCityConnect() || !board.get(x).get(y).getRoadConnect() ) {
            int count = 0;//counts tiles tracked by activeSides
            for (Tile t: remainder) {
                if (t == board.get(x).get(y) && activeSides.get(count) == side) {
                    return remainder;//ONLY if remainder already contains this tile AND this side do we not keep iterating
                }
                if (!t.getCityConnect() || !t.getRoadConnect()) count++;
            }
            activeSides.add(side);
            int x1, y1;
            switch(side) {
                case TOP:    x1 = x - 1;   y1 = y;
                break;

                case LEFT:   x1 = x;       y1 = y - 1;
                break;

                case BOTTOM: x1 = x + 1;   y1 = y;
                break;

                default:     x1 = x;       y1 = y + 1;
            }
            remainder.add(board.get(x).get(y));
            if (board.get(x).get(y).getSideType(side) == board.get(x).get(y).getSideType(side)) {//if this side is the type we seek
                if (board.get(x1).get(y1).getSideType((side + 2) % 4) == board.get(x).get(y).getSideType(side)) {//if the next tile's type matches
                    remainder = elementAt(x1, y1, (side + 2) % 4, remainder);//find the element of the next tile
                }
                else if (!exists(x1, y1)) {
                    remainder.add(board.get(x1).get(y1));
                }//mark unfinished element
            }
        }//CONDITIONS HERE ARE WRONG - continued on empty tile (they were actually nonexistant lmao. I put them in)

        if (remainder.contains(board.get(x).get(y))) return remainder;
        remainder.add(board.get(x).get(y));

        for (int i = 0; i < 4; i++) {
            int x1, y1;
            switch(i) {
                case TOP:  x1 = x - 1;   y1 = y;
                break;

                case LEFT:   x1 = x;       y1 = y - 1;
                break;

                case BOTTOM: x1 = x + 1;   y1 = y;
                break;

                default:     x1 = x;       y1 = y + 1;
            }
            //System.out.println(board.get(x1).get(y1) +"\n"+board.get(x).get(y).getSideType(side) +"\n"+ board.get(x).get(y).getSideType(i) +"\n"+ board.get(x1).get(y1).getSideType((side + 2) % 4) +"\n"+ board.get(x).get(y).getSideType(side));
            if (board.get(x).get(y).getSideType(side) == board.get(x).get(y).getSideType(i)) {//if this side is the type we seek
                if (board.get(x1).get(y1).getSideType((i + 2) % 4) == board.get(x).get(y).getSideType(side)) {//if the next tile's type matches
                    remainder = elementAt(x1, y1, (i + 2) % 4, remainder);//find the element of the next tile
                }
                else if (!exists(x1, y1)) {
                    remainder.add(board.get(x1).get(y1));
                }//mark unfinished element
            }
        }
        return remainder;
    }

    public int placeTile(int position, Tile tile) {
        int x = 0;
        int y = 0;
        int pos = 0;
        boolean[] check = {false, false};//check roads or cities -- fixes meeples on different sides

        //finds the xy of 'position'
        for (x = 0; x < board.size() && pos != position; x++) {
            for (y = 0; y < board.get(0).size() && pos != position; y++) {
                if (board.get(x).get(y) != null && board.get(x).get(y).getSide(TOP) == -2) {
                    pos++;
                }//checks for empty tile that is printed in toString as a placeable tile
            }
        }
        x -= 1;
        y -= 1;
        lastX = x;
        lastY = y;
        
        if (tile.getCityConnect() || tile.getRoadConnect()) {
            for (int i = 0; i < 4; i++) {
                if (tile.getSideColor(i) > 0) 
                    check[tile.getSideType(i)] = true;
            }
        }//find side types with meeples

        for (int i = 0; i < 4; i++) {
            int x1, y1;
            switch(i) {
                case TOP:  x1 = x - 1;   y1 = y;
                break;

                case LEFT:   x1 = x;       y1 = y - 1;
                break;

                case BOTTOM: x1 = x + 1;   y1 = y;
                break;

                default:     x1 = x;       y1 = y + 1;
            }
            if (exists(x1, y1)) {
                if (tile.getSideType(i) != board.get(x1).get(y1).getSideType((i + 2) % 4)) {
                    return i + 1;
                }//side type mismatch

                if ((tile.getSide(i) > -1 && check[tile.getSideType(i)]) || tile.getSide(i) > 1) {//Changed - hopefully is no longer problematic?
                    for (Tile t: elementAt(x1, y1, (i + 2) % 4)) {
                        if (!t.getCityConnect() || !t.getRoadConnect()) {
                            int j;
                            if (activeSides.size() == 0) {
                                System.out.print("!");//prevents error
                                j = 0;
                            }
                            else j = activeSides.remove(0);//for caps - prevents checking unconnected sides (AAAAAAAAAAAA)
                            if (tile.getSideType(i) == t.getSideType(j) && t.getSideColor(j) != 0) {
                                return i + 5;
                            }
                        }
                        else {
                            for (int j = 0; j < 4; j++) {
                                if (tile.getSideType(i) == t.getSideType(j) && t.getSideColor(j) != 0) {
                                    return i + 5;
                                }
                            }
                        }
                    }
                }
            }// end exists
        }// end for each side

        //place the tile
        board.get(x).set(y, tile);

        //expand the board
        if (x == 0) {
            board.add(0, new ArrayList<Tile>(board.get(0).size()));
            for (int i = 0; i < board.get(1).size(); i++) {
                board.get(0).add(null);
            }
            x++;
        }
        if (x == board.size() - 1) {
            board.add(new ArrayList<Tile>(board.get(0).size()));
            for (int i = 0; i < board.get(1).size(); i++) {
                board.get(board.size() - 1).add(null);
            }
        }

        if (y == 0) {
            for (ArrayList<Tile> row: board) {
                row.add(0, null);
            }
            y++;
        }
        if (y == board.get(0).size() - 1) {
            for (int i = 0; i < board.size(); i++) {
                board.get(i).add(null);
            }
        }

        //add empty tiles
        if (board.get(x - 1).get(y) == null) {
            board.get(x - 1).set(y, new Tile());
        }
        if (board.get(x + 1).get(y) == null) {
            board.get(x + 1).set(y, new Tile());
        }

        if (board.get(x).get(y - 1) == null) {
            board.get(x).set(y - 1, new Tile());
        }
        if (board.get(x).get(y + 1) == null) {
            board.get(x).set(y + 1, new Tile());
        }

        lastX = x;
        lastY = y;
        return 0;
    }

    public ArrayList<Integer> score() {
        ArrayList<Integer> scores = new ArrayList<Integer>();
        extraMeeples = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            ArrayList<Tile> toScore = elementAt(lastX, lastY, i);
            ArrayList<ArrayList<Integer>> results;
            if (isFinished(toScore)) {
                results = scoreElement(toScore, board.get(lastX).get(lastY).getSideType(i));
                for (int p = 0; p < results.get(0).size(); p++) {
                    if(scores.size() == p) {
                        scores.add(results.get(0).get(p));
                    }//account for variable player number
                    else {
                        scores.set(p, scores.get(p) + results.get(0).get(p));
                    }//add scores to total 
                    if (board.get(lastX).get(lastY).getSideType(i) == CITY && results.get(0).get(p) != 2) {
                        scores.set(p, scores.get(p) + results.get(0).get(p));
                    }//double scores for cities more than two tiles
                }//end combine scores

                for (int p = 0; p < results.get(1).size(); p++) {
                    if(extraMeeples.size() == p) {
                        extraMeeples.add(results.get(1).get(p));
                    }//account for variable player number
                    else {
                        extraMeeples.set(p, extraMeeples.get(p) + results.get(1).get(p));
                    }//add scores to total 
                }//end combine meeples
            }
        }//end score cities + roads

        for (int r = lastX - 1; r < lastX + 2; r++) {
            for (int c = lastY - 1; c < lastY + 2; c++) {
                if (exists(r, c) && board.get(r).get(c).getMonastery() > NO_PLAYER) {
                    ArrayList<Tile> score;
                    int tiles = 0;
                    if (board.get(r).get(c).getSideType(0) == GRASS) score = elementAt(r, c, 0);
                    else score = elementAt(r, c, 1); //just make sure we hit the monastery
                    for (Tile t: score) {
                        if (t.getSide(TOP) != -2) tiles++;
                    }//how surrounded is it?
                    if (tiles == 10) {
                        while(scores.size() < board.get(r).get(c).getMonastery()) {
                            scores.add(0);
                        }
                        scores.set(board.get(r).get(c).getMonastery() - 1, scores.get(board.get(r).get(c).getMonastery()) + 10);
                        while(extraMeeples.size() < board.get(r).get(c).getMonastery()) {
                            extraMeeples.add(0);
                        }
                        extraMeeples.set(board.get(r).get(c).getMonastery() - 1, extraMeeples.get(board.get(r).get(c).getMonastery()) + 1);
                        board.get(r).get(c).setMonastery(0);
                    }
                }
            }
        }//end score monasteries WTF WAS I THINKING HERE WHERE IS SCOREELEMENT
        return scores;
    }

    public ArrayList<Integer> scoreAll() {
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for (int r = 0; r < board.size(); r++) {
            for (int c = 0; c < board.get(0).size(); c++) {
                if (exists(r, c)) {
                    Tile t = board.get(r).get(c);

                    for (int i = 0; i < 4; i++) {
                        if (t.getSideColor(i) != NO_PLAYER) {
                            ArrayList<Tile> score = elementAt(r, c, i);
                            ArrayList<Integer> totals = scoreElement(score, t.getSideType(i)).get(0);
                            for (int p = 0; p < totals.size(); p++) {
                                if(scores.size() == p) {
                                    scores.add(totals.get(p));
                                }//account for variable player number
                                else {
                                    scores.set(p, scores.get(p) + totals.get(p));
                                }//add scores to total 
                            }//end combine scores
                        }
                    }//end loop thru sides

                    if (t.getMonastery() > NO_PLAYER) {
                        ArrayList<Tile> score;
                        if (t.getSideType(0) == GRASS) score = elementAt(r, c, 0);
                        else score = elementAt(r, c, 1); //just make sure we hit the monastery
                        ArrayList<Integer> totals = scoreElement(score, t.getMonastery()).get(0);
                        for (int p = 0; p < totals.size(); p++) {
                            if(scores.size() == p) {
                                scores.add(totals.get(p));
                            }//account for variable player number
                            else {
                                scores.set(p, scores.get(p) + totals.get(p));
                            }//add scores to total 
                        }//end combine scores
                    }

                }//end check for tile
            }//end loop thru columns
        }//end loop thru rows
        return scores;
    }

    private ArrayList<ArrayList<Integer>> scoreElement(ArrayList<Tile> element, int type) {
        ArrayList<Integer> score = new ArrayList<Integer>();
        ArrayList<Integer> meeples = new ArrayList<Integer>();
        //System.out.println("Scored! " + type);
        for (int i = 0; i < element.size(); i++) {
            if (element.get(i).getSide(TOP) == -2) {
                element.remove(i);
                i--;
            }
        }//we dont care abt end pieces at this point
        int total = element.size();

        for (Tile t: element) {
            if (type == 3 && t.getMonastery() > NO_PLAYER) {
                while (t.getMonastery() > meeples.size()) {
                    meeples.add(0);
                }//account for flexible player number
                meeples.set(t.getMonastery()-1, meeples.get(t.getMonastery()-1) + 1);
                t.setMonastery(NO_PLAYER);
            }
            else if (!t.getCityConnect() || !t.getRoadConnect()) {
                int i = activeSides.remove(0);
                if (t.getSideType(i) == type && t.getSideColor(i) != NO_PLAYER) {
                    while (t.getSideColor(i) > meeples.size()) {
                        meeples.add(0);
                    }//account for flexible player number
                    meeples.set(t.getSideColor(i) - 1, meeples.get(t.getSideColor(i) - 1) + 1);
                    t.setSideColor(i, NO_PLAYER);
                }
            }
            else {
                for (int i = 0; i < 4; i++) {
                    if (t.getSideType(i) == type && t.getSideColor(i) != NO_PLAYER) {
                        while (t.getSideColor(i) > meeples.size()) {
                            meeples.add(0);
                        }//account for flexible player number
                        meeples.set(t.getSideColor(i) - 1, meeples.get(t.getSideColor(i) - 1) + 1);
                        t.setSideColor(i, NO_PLAYER);
                    }
                }//end remove and track meeples
            }

            if (type == CITY && t.getFortified()) {
                total++;
            }
        }

        int max = 0;
        for (Integer i: meeples) {
            if (i > max) max = i;
        }//find owners of element

        for (Integer i: meeples) {
            if (i == max) score.add(total);
            else score.add(0);
        }//count scores for each player

        ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
        ret.add(score);
        ret.add(meeples);
        return ret;
    }

    /**
     * Precondition: score() was just called
     */
    public ArrayList<Integer> meeplesBack() {
        return extraMeeples;
    }

    public String toString() {
        String boardStr = "";
        int placement = 0;
        for (ArrayList<Tile> row: board) { 
            for (int i = 0; i < TILE_LINES; i++) {
                for (Tile tile : row) {
                    if (tile == null) {
                        boardStr += "     ";//x5
                    }
                    else if (tile.getSide(0) == -2 && i == TILE_LINES / 2) {
                        placement++;
                        if (placement < 10) boardStr += "  " + placement + "  ";
                        else if (placement < 100) boardStr += "  " + placement + " ";
                        else boardStr += " " + placement + " ";
                    }
                    else {
                        boardStr += tile.toString(i);
                    }
                }
                boardStr += "\n";
            }//end every line
        }//end every row of tiles
        return boardStr;
    }

    public String save() {
        String save = "";
        for (ArrayList<Tile> row: board) {
            for (Tile t: row) {
                if (t != null) {
                    save += t.save();
                }
                save += ";";
            }
            save += "\\";
        }
        return save;
    }
}