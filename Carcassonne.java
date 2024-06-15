
/**
* Write a description of class Carcasonne here.
*
* @author Miles
* @version 6/5/24
* 
* TODO: placing meeples does funny shapes?? sometimes???
*   placement color checking needs to be better
*   move restart turn to placement (0)
*/
import java.util.*;

public class Carcassonne {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("WELCOME TO CARCASONNE!");

        // GAME VARIABLES
        Board board = new Board();
        Deck deck = new Deck();
        LinkedList<Player> competitors = getPlayers(in);
        Player winner;
        Tile tile;
        Player p;

        // LOOP VARIABLES
        int temp = 0;
        int addedMeeples;

        // GAME LOOP
        Scoreboard scores = new Scoreboard(competitors);
        while (!deck.empty()) {
            tile = deck.draw();
            p = competitors.pop();
            competitors.push(p);
            addedMeeples = 0;

            System.out.println(p.getName() + "'s turn!\n"
                    + board
                    + tile.getKey()
                    + "\nYour tile: \n" + tile);

            while (temp == 0) { //while loop to reset tile placement
                p.setMeeples(addedMeeples);
                rotate(in, tile);

                addedMeeples = addMeeple(in, tile, p);

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

    private static LinkedList<Player> getPlayers(Scanner in) {
        String name = " ";
        LinkedList<Player> players = new LinkedList<Player>();

        System.out.println("Enter your names, [enter] to finish:");
        for (int i = 0; !name.equals(""); i++) {
            System.out.print("Player " + (i + 1) + ": ");
            name = in.nextLine();
            players.add(new Player(name));
        }
        players.pollLast();
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
        System.out.println(tile);
    }

    private static int addMeeple(Scanner in, Tile tile, Player player) {
        int side;
        System.out.print("Claim this tile? Enter a side (1-4) to place a meeple, or 0 not to: ");
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
        if (tile.getSide(side) == -1) {
            if (tile.getMonastery() == 0)
                return "";
            else
                return "Do not place your Meeple on grass.";
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
        }

        return "Unrecognized error: " + errorCode;
    }
}