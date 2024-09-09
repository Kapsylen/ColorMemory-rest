package sesv.dev.domain;

import org.junit.jupiter.api.Test;
import sesv.dev.error.CardIsAlreadyDrawnException;
import sesv.dev.error.InvalidInputException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class GameBoardTest {

    @Test
    void shouldReturnCardFromGameBoard_whenGetCard() {
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard();
        var card = gameBoardService.drawACard(1, 1);
        assertNotNull(card);
    }

    @Test
    void shouldThrowCardIsAlreadyDrawnException_whenNoCardFoundOnGivenPosition() {
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard();
        gameBoardService.removeCard(1, 1);
        assertThrowsExactly(CardIsAlreadyDrawnException.class,
                () -> gameBoardService.drawACard(1, 1)
        );
    }

    @Test
    void shouldThrowInvalidInputException_whenInvalidInputPosition() {
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard();
        assertThrowsExactly(InvalidInputException.class,
                () -> gameBoardService.drawACard(-1, 1)
        );
    }

    @Test
    void whenSameCardIsDrawnTwice_shouldBeConsideredAsNotEqual(){
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard();
        Card card1 = gameBoardService.drawACard(1, 1);
        Card card2 = gameBoardService.drawACard(1, 1);
        assertNotEquals(card1, card2);
    }

    @Test
    void whenDrawARemovedCard_ShouldThrowCardIsAlreadyDrawnException() {
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard();
        gameBoardService.removeCard(1, 1);
        assertThrowsExactly(CardIsAlreadyDrawnException.class,
                () -> gameBoardService.drawACard(1, 1)
        );
    }
}