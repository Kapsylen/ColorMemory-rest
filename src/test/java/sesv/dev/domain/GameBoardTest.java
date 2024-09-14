package sesv.dev.domain;

import org.junit.jupiter.api.Test;
import sesv.dev.error.CardIsAlreadyDrawnException;
import sesv.dev.error.InvalidInputException;
import sesv.dev.error.InvalidNumberException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class GameBoardTest {

    @Test
    void shouldReturnCardFromGameBoard_whenGetCard() {
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard(10);
        var card = gameBoardService.drawACard( 1);
        assertNotNull(card);
    }

    @Test
    void shouldThrowCardIsAlreadyDrawnException_whenNoCardFoundOnGivenPosition() {
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard(10);
        gameBoardService.removeCard(1);
        assertThrowsExactly(CardIsAlreadyDrawnException.class,
                () -> gameBoardService.drawACard( 1)
        );
    }

    @Test
    void whenSameCardIsDrawnTwice_shouldBeConsideredAsNotEqual(){
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard(10);
        Card card1 = gameBoardService.drawACard( 1);
        Card card2 = gameBoardService.drawACard( 2);
        assertNotEquals(card1, card2);
    }

    @Test
    void whenDrawARemovedCard_ShouldThrowCardIsAlreadyDrawnException() {
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard(10);
        gameBoardService.removeCard(1);
        assertThrowsExactly(CardIsAlreadyDrawnException.class,
                () -> gameBoardService.drawACard( 1)
        );
    }

    @Test
    void shouldCreateBoardWith10PairOfCards() {
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard(10);
        var gameBoard = gameBoardService.getGameBoard();
        gameBoardService.shuffleCards(gameBoard);
        var board = gameBoardService.getGameBoard();
        assertEquals(20, board.size());
        System.out.println(gameBoardService.getAllCards());
    }

    @Test
    void shouldRemoveAPairOfCards_WhenBoardIsCreatedAndATheParIsFound() {
        GameBoardService gameBoardService = new GameBoardService();
        gameBoardService.createGameBoard(10);

    }

        @Test
        void randomizeCards () {
            Random rand = new Random();
            ColorV2 color = new ColorV2(String.format("#%06x", rand.nextInt(256 * 256 * 256)));
            System.out.println(color.getRgbColor());
        }
}