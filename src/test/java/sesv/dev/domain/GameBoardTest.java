package sesv.dev.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameBoardTest {

    @Test
    void shouldRemoveCardFromGameBoard_whenRemoveCard(){
        GameBoardService gameBoard = new GameBoardService();
        var card = gameBoard.removeCard(1, 1);
        assertTrue(card.isPresent());
    }


    @Test
    void shouldReturnCardFromGameBoard_whenGetCard() {
        GameBoardService gameBoard = new GameBoardService();
        var card = gameBoard.drawACard(1, 1);
        assertTrue(card.isPresent());
    }

    @Test
    void shouldReturnNoCard_whenDrawCardFromEmptyPosition() {
        GameBoardService gameBoard = new GameBoardService();
        gameBoard.removeCard(1, 1);
        var card = gameBoard.removeCard(1, 1);
        assertFalse(card.isPresent());
    }

    @Test
    void whenTwoCardsAreDrawnWhichHasSameColorAndDifferentPosition_shouldBeConsideredAsEqual() {
        GameBoardService gameBoard = new GameBoardService();
        var card1 = gameBoard.getCards()[0][0];
        card1.setColour(Color.BLUE);
        var card2 = gameBoard.getCards()[2][0];
        card2.setColour(Color.BLUE);
        assertTrue(card1.equals(card2));
    }

    @Test
    void whenTwoCardsAreDrawnWhichHasDifferentColorAndDifferentPosition_shouldBeConsideredAsNotEqual() {
        GameBoardService gameBoard = new GameBoardService();
        var card1 = gameBoard.getCards()[0][0];
        card1.setColour(Color.BLUE);
        var card2 = gameBoard.getCards()[2][0];
        card2.setColour(Color.RED);
        assertFalse(card1.equals(card2));
    }

    @Test
    void whenSameCardIsDrawnTwice_shouldBeConsideredAsNotEqual() {
        GameBoardService gameBoard = new GameBoardService();
        var card1 = gameBoard.getCards()[0][0];
        card1.setColour(Color.BLUE);
        var card2 = gameBoard.getCards()[0][0];
        card2.setColour(Color.BLUE);
        card1.setXAndYPosition(0, 0);
        card2.setXAndYPosition(0, 0);
        assertFalse(card1.equals(card2));
    }
}