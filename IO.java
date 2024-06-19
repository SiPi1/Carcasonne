import java.util.Scanner;
public class IO {
    static Scanner in = new Scanner(System.in);
    public static final String RESET = "\u001B[0m";
    private static String BORDER = "\u001B[30m";
    private static String BRIGHT = ";1";
    private static int[] playerColor;

    Board b;
    Scoreboard s;
    public IO() {
        this.b = null;
        this.s = null;
    }

    public IO(Board b, Scoreboard s) {
        this.s = s;
        this.b = b;
        playerColor = new int[s.getPlayers().size()];
        for (int i = 0; i < playerColor.length; i++) {
            playerColor[i] = 31 + i;
        }
    }

    public static String color(int player) {
        if (player == 0) return BORDER;
        if (playerColor == null) return "\u001B[" + (30 + player) + BRIGHT + "m";
        return "\u001B[" + playerColor[player-1] + BRIGHT + "m";
    }
    
    public int nextInt() {
        String userIn = in.nextLine();
        try {
            return Integer.parseInt(userIn);
        }
        catch (NumberFormatException e) {
            help(userIn);
            return nextInt();
        }
    }

    public String nextLine() {
        return in.nextLine();
    }

    private void help(String userIn) {
        switch(userIn.toUpperCase()) {
            case "HELP":
                System.out.println("--RULES--\nEverything moves counterclockwise.\n\n" 
                + "--COMMANDS--\nSAVE: Copy the printed text and paste when you next run the program to save.\nRULES: View the full Carcasonne rules.\n"
                + "DB or LB: Set the color of the piece borders.\n"
                + "DC or LC: Set the brightness of meeples and player colors.\n"
                + "KEY: Displays a key for the board.");
                break;
            case "SAVE":
                System.out.println(s.save() + "|" + b.save());
                break;
            case "RULES":
                System.out.println("Look them up bozo I cant be bothered to write them here...");
                break;
            case "DB":
                BORDER = "";
                break;
            case "LB":
                BORDER = "\u001B[30m";
                break;
            case "DC":
                BRIGHT = "";
                break;
            case "LC":
                BRIGHT = ";1";
                break;
            case "KEY":
                System.out.print(Tile.getKey());
                break;
            case "BOARD":
                System.out.println(b);
                break;
            default:
                System.out.print("Please input a number: ");
        }
    }
}
