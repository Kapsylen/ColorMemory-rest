package sesv.dev.domain;

import org.springframework.stereotype.Service;
import sesv.dev.application.CardApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GameBoardService {

    private Card[][] cards;
    private static int totalPoints = 0;

    public void createGameBoard() {
        this.cards = new Card[4][4];
        createRow(0, List.of(Color.RED, Color.BLUE, Color.WHITE, Color.GREEN));
        createRow(1, List.of(Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.BLACK));
        createRow(2, List.of(Color.RED, Color.BLUE, Color.WHITE, Color.GREEN));
        createRow(3, List.of(Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.BLACK));
    }

    private void createRow(int rowIndex, List<Color > colors){
        for (int columnIndex = 0; columnIndex < colors.size(); columnIndex++) {
            cards[rowIndex][columnIndex] = new Card(colors.get(columnIndex));
            cards[rowIndex][columnIndex].setXAndYPosition(rowIndex, columnIndex);
        }
    }

    public Optional<Card> drawACard(int posX, int posY) {
        posX -= 1;
        posY -= 1;
        if (posX >= 0 && posX < cards.length && posY >= 0 && posY < cards[posX].length) {
            if (cards[posX][posY] != null) {
                Card drawnCard = cards[posX][posY];
                return Optional.of(drawnCard);
            }
        }
        return Optional.empty();
    }

    public Optional<Card> removeCard(int posX, int posY) {
        posX -= 1;
        posY -= 1;
        if (cards[posX][posY] != null) {
            Card removedCard = cards[posX][posY];
            cards[posX][posY] = null;
            return Optional.of(removedCard);
        } else {
            Optional.empty();
        }
        return Optional.empty();
    }

    public String displayGameBoardVisibleCards() {
        StringBuilder gameBoard = new StringBuilder();
        for (int i = 0; i < cards.length; i++) {
            for (int j = 0; j < cards[i].length; j++) {
                if (cards[i][j] != null) {
                    gameBoard.append("[" +cards[i][j].getColor() + "] ");
                } else {
                    gameBoard.append("[XXX] ");
                }
            }
            gameBoard.append("\n");
        }
        return gameBoard.toString();
    }

    public String displayGameBoardHiddenCards() {
        StringBuilder gameBoard = new StringBuilder();
        for (int i = 0; i < cards.length; i++) {
            for (int j = 0; j < cards[i].length; j++) {
                if (cards[i][j] != null) {
                    gameBoard.append("[" + i + ", " + j + "]");
                }
                else {
                    gameBoard.append("[XXX] ");
                }
            }
          gameBoard.append("\n");
        }
        return gameBoard.toString();
    }

    public void shuffleBoard() {
        var list = Arrays.asList(cards);
        Collections.shuffle(Arrays.asList(list.get(0)));
        Collections.shuffle(Arrays.asList(list.get(1)));
        Collections.shuffle(Arrays.asList(list.get(2)));
        Collections.shuffle(Arrays.asList(list.get(3)));
        cards = list.toArray(new Card[0][0]);
    }


    public Card[][] getCards() {
        return this.cards;
    }

    public int getCardsLeft() {
        int cardLeft;
        cardLeft = 0;
        for (int i = 0; i < cards.length; i++) {
            for (int j = 0; j < cards[i].length; j++) {
                if (cards[i][j] != null) {
                    cardLeft++;
                }
            }
        }
        return cardLeft;
    }

    public String welcomeInstructions() {
        return (
                """
                Welcome to Colour Memory Game.
                       
                Game Rules:
                Draw two cards from the game board below.
                A pair of same colour gives 1 point and 
                two unkind cards gives -1 point. Each card 
                position is numbered from left to right 
                from the top left corner of the board starts 
                at position X = 1 and Y = 1 and so on. When a
                correct pair has been drawn from the game board
                it will be removed from the game board. The 
                game is completed when all cards have been drawn 
                from the game board. 
                \n""");
    }

    public String startGame() {
        createGameBoard();
        shuffleBoard();
        StringBuilder startGameInstruction = new StringBuilder();
        startGameInstruction.append(welcomeInstructions());
        startGameInstruction.append(displayGameBoardVisibleCards());
        return startGameInstruction.toString();
    }

    public String playGame(CardApi card1, CardApi card2) {
        StringBuilder playResponse = new StringBuilder();
        int card1PosX = card1.posX();
        int card1PosY = card1.posY();
        int card2PosX = card2.posX();
        int card2PosY = card2.posY();
        Optional<Card> drawnCard1 = drawACard(card1PosX, card1PosY);
        Optional<Card> drawnCard2 = drawACard(card2PosX, card2PosY);
        if (drawnCard1.isPresent() && drawnCard2.isPresent() && drawnCard1.get().equals(drawnCard2.get())) {
            totalPoints++;
            removeCard(card1PosX, card1PosY);
            removeCard(card2PosX, card2PosY);
            playResponse.append("Correct pair! You earned 1 point.\n");
            if (getCardsLeft() == 0) {
                playResponse.append("Game Over! Total Points: " + totalPoints);
            }
            playResponse.append(displayGameBoardVisibleCards());
        } else {
            totalPoints--;
            playResponse.append("Incorrect pair! You lost 1 point.\n");
            playResponse.append(displayGameBoardVisibleCards());
        }
        return playResponse.toString();
    }
}
