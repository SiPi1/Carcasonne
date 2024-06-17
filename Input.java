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
        switch(userIn) {
            case "HELP":
                System.out.println("Help function: input SAVE and copy result to clipboard to save.");//TODO: Add functions: highlight element?
                break;
            case "SAVE":
                System.out.println(s.save() + "|" + b.save());
                break;
            default:
                System.out.print("Please input a number: ");
        }
    }
}
