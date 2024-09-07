package sesv.dev.application;

import jakarta.validation.constraints.NotNull;

public record CardsRequest(
       @NotNull
       CardApi card1,
       @NotNull
       CardApi card2
) {
}
