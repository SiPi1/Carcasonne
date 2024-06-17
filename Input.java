import java.util.Scanner;
public class Input {
    static Scanner in = new Scanner(System.in);
    Board b;
    Scoreboard s;
    public Input() {
        this.b = null;
        this.s = null;
    }

    public Input(Board b, Scoreboard s) {
        this.s = s;
        this.b = b;
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
                + "DARKBORDERS or LIGHBORDERS: Set the color of the piece borders.\n");
                break;
            case "SAVE":
                System.out.println(s.save() + "|" + b.save());
                break;
            case "RULES":
                System.out.println("Look them up bozo I cant be bothered to write them here...");
            case "DARKBORDERS":
                Tile.BORDER = "\u001B[30m";
                System.out.print("Done: ");
            case "LIGHTBORDERS":
                Tile.BORDER = "";
                System.out.print("Done: ");
            default:
                System.out.print("Please input a number: ");
        }
    }
}
