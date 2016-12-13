import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Card {
  private Hue color;
  private Fill fill;
  private Shape shape;
  private int number;
  private BufferedImage img;

  public static final int HEIGHT = 62;
  public static final int WIDTH = 95;

  public Card(Hue color, Fill fill, Shape shape, int number) {
    this.color = color;
    this.fill = fill;
    this.shape = shape;
    this.number = number;
    try {
      this.img = ImageIO.read(new File(("img/" + color + fill + shape + number + ".gif").toLowerCase()));
    } catch (IOException e) {
      System.out.println("Internal Error: " + e.getMessage());
    }
  }

  public boolean[] getDifferences(Card c) {
    if (c == null) {
      throw new IllegalArgumentException("null card");
    }
    boolean[] result = new boolean[4];
    result[0] = this.color == c.color;
    result[1] = this.fill == c.fill;
    result[2] = this.shape == c.shape;
    result[3] = this.number == c.number;

    return result;
  }

  public void draw(Graphics g, int x, int y) {
    g.drawImage(img, x, y, WIDTH, HEIGHT, null);
  }

  @Override
  public String toString() {
    return ("{" + number + ", " + color + ", " + fill + ", " + shape + "}").toLowerCase();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Card)) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    return this.toString().equals(obj.toString());
  }
}
