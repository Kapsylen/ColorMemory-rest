package sesv.dev.domain;

import lombok.Builder;

import java.util.Objects;

@Builder
public record Card (
        Color color
){

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return color == card.color;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(color);
    }
}
