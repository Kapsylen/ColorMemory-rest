package sesv.dev.domain;

import java.util.Objects;

public class Card {

    private Color color;
    private int xPosition, yPosition;

    public Card(Color color, int xPosition, int yPosition) {
        this.color = color;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public Color getColor() {
        return color;
    }

    public int getXPos() {
        return xPosition;
    }

    public int getYPos() {
        return yPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return (xPosition != card.xPosition || yPosition != card.yPosition) && color == card.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, xPosition, yPosition);
    }
}
