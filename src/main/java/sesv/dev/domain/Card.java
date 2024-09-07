package sesv.dev.domain;

import java.util.Objects;

public class Card {

   private Color color;
   private int xPos, yPos;

    public Card(Color color) {
        this.color = color;
    }

    public void setXAndYPosition(int x, int y) {
      this.xPos = x;
      this.yPos = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColour(Color color) {
        this.color = color;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return (xPos != card.xPos || yPos != card.yPos) && color == card.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, xPos, yPos);
    }
}
