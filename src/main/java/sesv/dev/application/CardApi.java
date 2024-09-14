package sesv.dev.application;

import jakarta.validation.constraints.Min;

public record CardApi(@Min(1) Integer number) {
}
