/**
 * A deck of tiles
 * 
 * @author Silas W
 */

 import java.util.ArrayList;
import java.util.Random;

 public class Deck {
     private ArrayList<Tile> deck;
     final int EMPTY = -2;
     final int GRASS = -1;
     final int ROAD = 0;
     final int CITY = 1;
     Random r;
 
     public Deck(long seed) {//https://cf.geekdo-images.com/fICWo6RHTkWWWgc532J1mg__medium/img/A5mjaanVRn7Gj6TC8hZWuvDatSU=/fit-in/5ROADROADx5ROADROAD/filters:no_upscale():strip_icc()/pic115467.jpg
        r = new Random(seed); 
        deck = new ArrayList<Tile>();
         for (int i = 0; i < 9; i++) {
             switch(i) {
                 case 8://Singletons
                     deck.add(new Tile(CITY, CITY, CITY, CITY, true, true, -1));
                     deck.add(new Tile(CITY, CITY, GRASS, CITY, true, true, -1));
                     deck.add(new Tile(CITY, CITY, ROAD, CITY, true, false, -1));
                     deck.add(new Tile(GRASS, CITY, GRASS, CITY, true, false, -1));
                     deck.add(new Tile(ROAD, ROAD, ROAD, ROAD, true, false, -1));
                     
                 case 7://2 copies
                     deck.add(new Tile(GRASS, GRASS, ROAD, GRASS, true, false, 0));
                     deck.add(new Tile(CITY, CITY, ROAD, CITY, true, true, -1));
                     deck.add(new Tile(CITY, CITY, GRASS, GRASS, true, true, -1));
                     deck.add(new Tile(CITY, CITY, ROAD, ROAD, true, true, -1));
                     deck.add(new Tile(GRASS, CITY, GRASS, CITY, true, true, -1));
                     deck.add(new Tile(CITY, CITY, GRASS, GRASS, false, false, -1));
                 
                 case 6://3 copies
                     deck.add(new Tile(CITY, CITY, GRASS, CITY, true, false, -1));
                     deck.add(new Tile(CITY, CITY, GRASS, GRASS, true, false, -1));
                     deck.add(new Tile(CITY, CITY, ROAD, ROAD, true, false, -1));
                     deck.add(new Tile(CITY, GRASS, CITY, GRASS, false, false, -1));
                     deck.add(new Tile(CITY, ROAD, ROAD, GRASS, true, false, -1));
                     deck.add(new Tile(CITY, GRASS, ROAD, ROAD, true, false, -1));
                     deck.add(new Tile(CITY, ROAD, ROAD, ROAD, true, false, -1));
                     deck.add(new Tile(CITY, ROAD, GRASS, ROAD, true, false, -1));
 
                 case 5://4 copies
                     deck.add(new Tile(GRASS, GRASS, GRASS, GRASS, true, false, 0));
 
                 case 4://5 copies
                     deck.add(new Tile(CITY, GRASS, GRASS, GRASS, true, false, -1));
 
                 case 3:
                 case 2:
                 case 1://8 copies
                     deck.add(new Tile(ROAD, GRASS, ROAD, GRASS, true, false, -1));
 
                 case 0://9 copies
                     deck.add(new Tile(GRASS, ROAD, ROAD, GRASS, true, false, -1));
             }
         }
     }

    public Deck(Board b, long seed) {
        this(seed);
        ArrayList<Tile> problems = new ArrayList<Tile>();
        deck.add(new Tile(CITY, ROAD, GRASS, ROAD, true, false, -1));//account for start tile
        for (Tile t: b.tiles()) {
            for (int i = 0; i < deck.size(); i++) {
                if (deck.get(i).equals(t)) {
                    deck.remove(i);
                    r.nextInt(deck.size());//cycle the seed
                    break;
                }
                if (i == deck.size() - 1) 
                    problems.add(t);

            }
        }
        if (deck.size() != 68 - b.tiles().size()) System.out.print("PARSE ERROR: DECK OFF BY " + problems);
    }
     
     public Tile draw() {
        if (empty()) return new Tile();
        return deck.remove(r.nextInt(deck.size()));
     }
     
     public boolean empty() {
         return deck.size() == 0;
     }
     
 }