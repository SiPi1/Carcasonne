
/**
* Write a description of class Carcasonne here.
*
* @author Miles
* @version 6/5/24
* TODO: Add a new Scanner function with utilities: Highlight meeples, highlight element, save, etc. 
*/
import java.util.*;

public class Carcassonne {
    public static void actiiTable() {
        int width = 5;
        for (int i = 0; i < 10000; i += width) {
            for (int j = 0; j < width; j++) {
                if ((char)(i + j) != '?') System.out.print("\t" + (i + j) + " : " + (char)(i + j));
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("WELCOME TO CARCASONNE!");

        // GAME VARIABLES
        Board board = new Board();
        Deck deck = new Deck();
        ArrayList<Player> competitors = getPlayers(in);
        Player winner;
        Tile tile;
        Player p;

        // LOOP VARIABLES
        int temp;
        int addedMeeples;

        // GAME LOOP
        Scoreboard scores = new Scoreboard(competitors);
        while (!deck.empty()) {
            temp = 0;
            tile = deck.draw();
            p = competitors.remove(0);
            competitors.add(p);
            addedMeeples = 0;

            System.out.println("\n\nPlayer " + p.getNumber() + ": " + p.getName() + "'s turn!\n"
                    + board
                    + tile.getKey()
                    + "\nYour tile: \n" + tile);

            while (temp == 0) { //while loop to reset tile placement
                p.setMeeples(addedMeeples);
                for (int i = 0; i < 4; i++) tile.setSideColor(i, 0);

                rotate(in, tile);
                System.out.println(tile);

                addedMeeples = addMeeple(in, tile, p);//TODO: Move after placing??
                System.out.println(tile);

                System.out.println("Where would you like to place your tile?");
                temp = in.nextInt();
                while (temp > 0 && board.placeTile(temp, tile) != 0) {
                    System.out.println(
                            "That placement is invalid, try again: " + invalidPlacement(board.placeTile(temp, tile)));
                    temp = in.nextInt();
                }
            }
            System.out.println(board);

            scores.score(board.score());
            for (Player pl: competitors) {
                if (board.meeplesBack().size() > pl.getNumber() - 1) 
                    pl.setMeeples(board.meeplesBack().get(pl.getNumber() - 1));
            }
            System.out.print("Your turn is over. " + scores);
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
        in.close();
    }

    private static ArrayList<Player> getPlayers(Scanner in) {
        String name = " ";
        ArrayList<Player> players = new ArrayList<Player>();

        System.out.println("Enter your names, [enter] to finish:");
        for (int i = 0; i < 2 || !name.equals(""); i++) {
            System.out.print("Player " + (i + 1) + ": ");
            name = in.nextLine();
            if (name.equals("")) i--;
            else players.add(new Player(name));
        }
        return players;
    }

    private static void rotate(Scanner in, Tile tile) {
        int temp;
        System.out.print("ROTATE TILE -- 0:0 deg, 1:90 deg, 2:180 deg, 3: 270 deg: ");
        temp = in.nextInt();
        while (temp > 3 || temp < 0) {
            System.out.println("Please input between 0-3.");
            temp = in.nextInt();
        }
        tile.rotate(temp);
    }

    private static int addMeeple(Scanner in, Tile tile, Player player) {
        int side;
        System.out.print("You have " + player.getMeeples() + " meeples.\nClaim this tile? Enter a side (1-4) to place a meeple, or 0 not to: ");
        side = in.nextInt() - 1;
        while (!checkMeeple(side, tile).equals("")) {
            System.out.print(checkMeeple(side, tile));
            side = in.nextInt() - 1;
        }
        if (side == -1)
            return 0;

        player.setMeeples(-1);

        if (tile.getSide(side) == -1)
            tile.setMonastery(player.getNumber());
        else
            tile.setSideColor(side, player.getNumber());

        return 1;
    }

    private static String checkMeeple(int side, Tile tile) {

        if (side < -1 || side > 3)
            return "Please input between 0-4.";
        if (side > -1 && tile.getSide(side) == -1) {
            if (tile.getMonastery() == 0)
                return "";
            else
                return "Sorry, we don't do farmers...";
        }
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