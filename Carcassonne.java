
/**
* Write a description of class Carcasonne here.
*
* @author Miles
*/
import java.util.*;

public class Carcassonne {
    public static void ansiTable() {
        int width = 10;
        for (int i = 0; i < 110; i += width) {
            for (int j = 0; j < width; j++) {
                String color = "\u001B[" + (i+j) + "m";
                if (i+j < 10) color += " A";
                else if (i+j < 100) color += " ";
                System.out.print(color + (i+j) + "\u001B[0m");
            }
            System.out.println();
        }
    }
    public static void asciiTable() {
        int width = 5;
        for (int i = 0; i < 10000; i += width) {
            for (int j = 0; j < width; j++) {
                System.out.print("\t" + (i + j) + " : " + (char)(i + j));
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {

        IO in = new IO();
        Board board;
        Deck deck;
        Scoreboard scores;
        ArrayList<Player> competitors;
        Player winner;
        String userIn;
        System.out.print("\u001B[2J\u001B[1;0H");
        System.out.println("WELCOME TO CARCASONNE! \nPlay from save? ");
        userIn = in.nextLine();
        if (!userIn.equals("")) {
            int seed = Integer.parseInt(userIn.substring(0, userIn.indexOf('|')));
            userIn = userIn.substring(userIn.indexOf('|') + 1);
            board = new Board(userIn.substring(userIn.indexOf('|') + 1));
            deck = new Deck(board, seed);
            scores = new Scoreboard(userIn.substring(0, userIn.indexOf('|')), seed);
            competitors = new ArrayList<Player>();
            for (String name: scores.getPlayers()) {
                competitors.add(new Player(name));
            }

            ArrayList<Tile> allTiles = board.tiles();
            // assign meeples to players
            for (Tile t: allTiles) {
                for (int i = 0; i < 4; i++) {
                    if (t.getSideColor(i) > 0)
                        competitors.get(t.getSideColor(i) - 1).setMeeples(-1);
                }
                if (t.getMonastery() > 0)
                    competitors.get(t.getMonastery() - 1).setMeeples(-1);
                
            }

            // resolve turn order
            for (int i = 0; i < (allTiles.size() - 1) % competitors.size(); i++) {
                competitors.add(competitors.remove(0));
            }
        }
        else {
            int seed = (int)System.currentTimeMillis();
            board = new Board();
            deck = new Deck(seed);
            competitors = getPlayers(in);
            scores = new Scoreboard(competitors, seed);
        }
        in = new IO(board, scores);

        // LOOP VARIABLES
        int temp;
        int addedMeeples;
        Tile tile;
        Player p;

        // GAME LOOP
        while (!deck.empty()) {
            temp = 0;
            tile = deck.draw();
            p = competitors.remove(0);
            competitors.add(p);
            addedMeeples = 0;

            System.out.println(IO.color(p.getNumber()) + "\n\nPlayer " + p.getNumber() + ": " + p.getName() + "'s turn!\n" + IO.RESET
                    + board
                    + scores);

            while (temp == 0) { //while loop to reset tile placement
                p.setMeeples(addedMeeples);
                for (int i = 0; i < 4; i++) tile.setSideColor(i, 0);

                rotate(in, tile);

                if (p.getMeeples() > 0) addedMeeples = addMeeple(in, tile, p);

                System.out.print(tile + "Place your tile: ");
                temp = in.nextInt();
                while (temp > 0 && board.placeTile(temp, tile) != 0) {
                    System.out.print("\u001B[0J" + //clear previous line
                            "That placement is invalid, try again: " + 
                            invalidPlacement(board.placeTile(temp, tile)));
                    temp = in.nextInt();
                }
                System.out.println("\u001B[6A\u001B[0J");
            }
            System.out.println(board);

            scores.score(board.score());
            for (Player pl: competitors) {
                if (board.meeplesBack().size() > pl.getNumber() - 1) 
                    pl.setMeeples(board.meeplesBack().get(pl.getNumber() - 1));
            }
            //CLEAR SCREEN            
            System.out.print("\u001B[" + (5 + competitors.size()) + ";0H");
            System.out.print("\u001B[0J");
        }

        //GAME END
        scores.score(board.scoreAll());
        winner = competitors.get(0);
        for (Player player: competitors) {
            if (scores.getScore(player.getNumber()) > scores.getScore(winner.getNumber())
                || scores.getScore(player.getNumber()) == scores.getScore(winner.getNumber())

                    && player.getMeeples() > winner.getMeeples()
                || player.getMeeples() == winner.getMeeples()

                    && player.getNumber() > winner.getNumber()) 
                winner = player;
        }
        System.out.println(board + "\n" + scores + "Congratulations " + winner.getName() + ", you won!");
    }

    private static ArrayList<Player> getPlayers(IO in) {
        String name = " ";
        ArrayList<Player> players = new ArrayList<Player>();

        System.out.println("Enter your names, [enter] to finish:");
        for (int i = 0; i < 2 || !name.equals(""); i++) {
            System.out.print(IO.color(i + 1) + "Player " + (i + 1) + ": ");
            name = in.nextLine();
            if (name.equals("")) i--;
            else if (name.indexOf('|') != -1 || name.indexOf('=') != -1 || name.indexOf(';') != -1) {
                System.out.println("\u001B[1A\u001B[0J" + "Names can't contain =, |, or ;");
                i--;
            }
            else players.add(new Player(name));
        }
        return players;
    }

    private static void rotate(IO in, Tile tile) {
        int temp;
        System.out.print(tile + "Which side should be on top? Enter 0-4: ");
        temp = in.nextInt();
        while (temp > 4 || temp < 0) {
            System.out.print("\u001B[0J" + "Please input between 0-4: ");
            temp = in.nextInt();
        }
        tile.rotate(temp);
        System.out.print("\u001B[5A\u001B[0J");
        
    }

    private static int addMeeple(IO in, Tile tile, Player player) {
        int side;
        System.out.print(tile + "You have " + player.getMeeples() + " meeples.\nClaim this tile? Enter a side (1-4) to place a meeple, or 0 not to: ");
        side = in.nextInt() - 1;
        while (!checkMeeple(side, tile).equals("")) {
            System.out.print("\u001B[0J" + checkMeeple(side, tile));
            side = in.nextInt() - 1;
        }
        if (side == -1) {
            System.out.print("\u001B[6A\u001B[0J");
            return 0;
        }
        player.setMeeples(-1);

        if (tile.getSide(side) == -1)
            tile.setMonastery(player.getNumber());
        else
            tile.setSideColor(side, player.getNumber());

        System.out.print("\u001B[6A\u001B[0J");
        return 1;
    }

    private static String checkMeeple(int side, Tile tile) {

        if (side < -1 || side > 3)
            return "Please input between 0-4: ";
        if (side > -1 
            && tile.getSide(side) == -1
            && tile.getMonastery() != 0)
                return "Sorry, we don't do farmers... ";
        
        return "";
    }

    private static String invalidPlacement(int errorCode) {
        if (errorCode == -1)
            return "Placement out of range.";
        String error = "";
        if (errorCode / 4 == 0)
            error += "Side type mismatch on side ";
        else
            error += "Meeple connecting to already claimed city on side ";
        switch (errorCode - 1 % 4) {
            case 0:
                return error + "top.";
            case 1:
                return error + "left.";
            case 2:
                return error + "bottom.";
            case 3:
                return error + "right.";
            default:
                return error + (errorCode - 1 % 4);
        }

        //return "Unrecognized error: " + errorCode;
    }
}