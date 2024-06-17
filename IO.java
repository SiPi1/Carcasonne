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

    public void setScoreboard(Scoreboard s) {
        this.s = s;
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
                + "DARKBORDERS or LIGHBORDERS: Set the color of the piece borders.\n"
                + "DARKCOLORS or LIGHCOLORS: Set the brightness of meeples and player colors.\n");
                break;
            case "SAVE":
                System.out.println(s.save() + "|" + b.save());
                break;
            case "RULES":
                System.out.println("Look them up bozo I cant be bothered to write them here...");
                break;
            case "DARKBORDERS":
                BORDER = "\u001B[30m";
                System.out.print("Done: ");
                break;
            case "LIGHTBORDERS":
                BORDER = "";
                System.out.print("Done: ");
                break;
            case "DARKCOLORS":
                BRIGHT = ";1";
                System.out.print("Done: ");
                break;
            case "LIGHTCOLORS":
                BRIGHT = "";
                System.out.print("Done: ");
                break;
            case "BOARD":
                System.out.println(b);
                break;
            default:
                System.out.print("Please input a number: ");
        }
    }
}
