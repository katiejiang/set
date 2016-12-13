import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.text.DecimalFormat;

/**
 * This class holds the primary game logic for Set.
 */
public class Table extends JPanel {

  private LinkedList<Card> stock;
  private Set<Card> cards;
  private Card[][] table = new Card[3][7];
  private ArrayList<Card> selectedCards;
  private double time = 0;
  private String message = "";
  private JLabel status;
  public boolean playing = false;
  public String username = "";
  private HashMap<Double, LinkedList<String>> scores;

  public static final int TABLE_WIDTH = 693;
  public static final int TABLE_HEIGHT = 198;
  public static final int MIN_NUM_COL = 4;
  public static final int NUM_CARDS = 81;

  public Table(JLabel status, HashMap<Double, LinkedList<String>> scores) {

    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    setFocusable(true);
    this.status = status;
    this.scores = scores;

    Timer timer = new Timer(100, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (playing) {
          time += 0.1;
          updateStatus();
        }
      }
    });
    timer.start();

    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (playing && e.getX() >= 0 && e.getX() <= 693 && e.getY() >= 0 && e.getY() <= 198) {
          Card c = table[e.getY() / (Card.HEIGHT + 4)][e.getX() / (Card.WIDTH + 4)];
          if (selectedCards.contains(c)) {
            selectedCards.remove(c);
            repaint();
          } else {
            selectedCards.add(c);
            repaint();

            if (selectedCards.size() == 3) {
              if (hasSet(selectedCards)) {
                removeSet();
              } else {
                selectedCards.clear();
              }
            }
          }
        }
      }
    });

  }

  public void reset() {
    // generate new stock
    stock = newDeck();
    playing = true;
    time = 0;
    message = "";

    // add cards to table
    cards = new HashSet<Card>();
    selectedCards = new ArrayList<Card>();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 7; j++) {
        if (j < MIN_NUM_COL) {
          Card c = stock.pop();
          cards.add(c);
          table[i][j] = c;
        } else {
          table[i][j] = null;
        }
      }
    }

    int col = MIN_NUM_COL;
    while (!hasSet(cards)) {
      addCardsTo(col);
      col++;
    }

    /* UNCOMMENT fOLLOWING LINE IF YOU WANT HELP SOLVING THE GAME */
    // printHint();
    
    repaint();
  }

  public static LinkedList<Card> newDeck() {
    Hue[] allColors = { Hue.RED, Hue.PURPLE, Hue.GREEN };
    Fill[] allFills = { Fill.SHADED, Fill.STRIPED, Fill.OUTLINED };
    Shape[] allShapes = { Shape.OVAL, Shape.DIAMOND, Shape.SQUIGGLE };
    LinkedList<Card> deck = new LinkedList<Card>();

    for (int i = 0; i < NUM_CARDS; i++) {
      Hue c = allColors[i / 27];
      Fill f = allFills[(i % 27) / 9];
      Shape s = allShapes[(i % 9) / 3];
      deck.add(new Card(c, f, s, i % 3 + 1));
    }
    Collections.shuffle(deck);
    return deck;
  }

  public void updateStatus() {
    DecimalFormat df = new DecimalFormat("0.0");
    status.setText("<html>" + message + "<br>Time: " + df.format(time) + "<br>" + "Name: " + username + "</html>");
  }

  public static boolean isSet(Card c1, Card c2, Card c3) {
    if (c1 == null || c2 == null || c3 == null) {
      return false;
    }

    boolean[] diff1 = c1.getDifferences(c2);
    boolean[] diff2 = c1.getDifferences(c3);
    boolean[] diff3 = c2.getDifferences(c3);

    return Arrays.equals(diff1, diff2) && Arrays.equals(diff2, diff3);
  }

  public static boolean hasSet(Collection<Card> cardsList) {
    if (cardsList == null) {
      throw new IllegalArgumentException("Cannot check if null list has a Set");
    }

    Card[] arr = cardsList.toArray(new Card[cardsList.size()]);
    for (int i = 0; i < arr.length; i++) {
      for (int j = i + 1; j < arr.length; j++) {
        for (int k = j + 1; k < arr.length; k++) {
          if (isSet(arr[i], arr[j], arr[k])) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public void printHint() {
    Card[] arr = cards.toArray(new Card[cards.size()]);
    for (int i = 0; i < arr.length; i++) {
      for (int j = i + 1; j < arr.length; j++) {
        for (int k = j + 1; k < arr.length; k++) {
          if (isSet(arr[i], arr[j], arr[k])) {
            System.out.println("Hint: " + arr[i] + arr[j] + arr[k]);
            return;
          }
        }
      }
    }
  }

  /* assumes that a valid set is passed in */
  public void removeSet() {
    Card c1 = selectedCards.get(0);
    Card c2 = selectedCards.get(1);
    Card c3 = selectedCards.get(2);

    int lastColIndex = cards.size() / 3 - 1;
    // remove the set from table and add new cards
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 7; j++) {
        if (c1.equals(table[i][j]) || c2.equals(table[i][j]) || c3.equals(table[i][j])) {
          cards.remove(table[i][j]);
          table[i][j] = null;
          if (!stock.isEmpty()) {
            if (lastColIndex < MIN_NUM_COL) {
              Card newCard = stock.pop();
              table[i][j] = newCard;
              cards.add(newCard);
            }
          }
        }
      }
    }

    lastColIndex = cards.size() / 3 - 1;
    refactorTable(lastColIndex + 1);

    while (!hasSet(cards)) {
      if (stock.isEmpty()) {
        message = "You win!";
        playing = false;
        if (scores.containsKey(time)) {
          scores.get(time).add(username);
        } else {
          LinkedList<String> l = new LinkedList<String>();
          l.add(username);
          scores.put(time, l);
        }
        updateStatus();
        return;
      } else {
        addCardsTo(lastColIndex + 1);
        lastColIndex++;
      }
    }

    /* UNCOMMENT fOLLOWING LINE IF YOU WANT HELP SOLVING THE GAME */
    // printHint();
    
    selectedCards.clear();
  }

  public void refactorTable(int lastColIndex) {
    LinkedList<Card> lastColCards = new LinkedList<Card>();

    for (int i = 0; i < 3; i++) {
      if (table[i][lastColIndex] != null) {
        lastColCards.add(table[i][lastColIndex]);
        table[i][lastColIndex] = null;
      }
    }

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < lastColIndex; j++) {
        if (table[i][j] == null) {
          table[i][j] = lastColCards.pop();
        }
      }
    }
  }

  public void addCardsTo(int col) {
    if (!stock.isEmpty()) {
      for (int i = 0; i < 3; i++) {
        Card c = stock.pop();
        cards.add(c);
        table[i][col] = c;
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 7; j++) {
        Card c = table[i][j];
        if (c != null) {
          if (selectedCards.contains(c)) {
            g.setColor(Color.BLACK);
            g.fillRect(j * (Card.WIDTH + 4), i * (Card.HEIGHT + 4), Card.WIDTH + 4, Card.HEIGHT + 4);
          }
          c.draw(g, 2 + j * (Card.WIDTH + 4), 2 + i * (Card.HEIGHT + 4));
        }
      }
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(TABLE_WIDTH, TABLE_HEIGHT);
  }

}
