package sesv.dev.application;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CardApi(@Min(1) Integer posX, @Max(4) Integer posY) {
}
