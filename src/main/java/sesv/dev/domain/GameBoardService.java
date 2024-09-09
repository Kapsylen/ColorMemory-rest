package sesv.dev.domain;

import org.springframework.stereotype.Service;
import sesv.dev.application.CardApi;
import sesv.dev.error.CardIsAlreadyDrawnException;
import sesv.dev.error.InvalidInputException;

import java.util.*;

@Service
public class GameBoardService {

        private Card[][] cards;
        private static int totalPoints = 0;

        public void createGameBoard() {
            this.cards = new Card[4][4];
            var listOfPairColours = new ArrayList<Color>();
            listOfPairColours.addAll(Arrays.asList(Color.values()));
            listOfPairColours.addAll(Arrays.asList(Color.values()));
            Collections.shuffle(listOfPairColours);
            createAndSetColorAndPositionForEachCard(listOfPairColours);
        }

        private void createAndSetColorAndPositionForEachCard(List<Color> eightPairOfColours) {
            int colorsForEachLine = 4;
            for (int rowIndex = 0; rowIndex < cards.length; rowIndex++) {
                for (int columnIndex = 0; columnIndex < colorsForEachLine; columnIndex++) {
                    int colorIndex = rowIndex * colorsForEachLine + columnIndex;
                    cards[rowIndex][columnIndex] = new Card(eightPairOfColours.get(colorIndex), rowIndex, columnIndex);
                }
            }
        }

        public Card drawACard(int positionX, int positionY) {
            positionX = adjustPosXAndYToMatchArrayStartFromIndexZero(positionX);
            positionY = adjustPosXAndYToMatchArrayStartFromIndexZero(positionY);
            if (isValidXAndYPosition(positionX, positionY)) {
                if (cards[positionX][positionY] != null) {
                    Card drawnCard = cards[positionX][positionY];
                    return drawnCard;
                } else {
                    throw new CardIsAlreadyDrawnException();
                }
            }
            throw new InvalidInputException();
        }

        private boolean isValidXAndYPosition(int posX, int posY) {
            return posX >= 0 && posX < cards.length && posY >= 0 && posY < cards[posX].length;
        }

        private int adjustPosXAndYToMatchArrayStartFromIndexZero(int value) {
            int adjustPosXAndYArrayStartFromIndexZero = 1;
            value -= adjustPosXAndYArrayStartFromIndexZero;
            return value;
        }

        public void removeCard(int posX, int posY) {
            if (cards[posX][posY] != null) {
                cards[posX][posY] = null;
            }
        }

        /**
         * Only used for test purposes from the CLI
         * @return
         */
        public String displayAllColourCards() {
            StringBuilder board = new StringBuilder();
            for (int rowIndex = 0; rowIndex < cards.length; rowIndex++) {
                for (int columnIndex = 0; columnIndex < cards[rowIndex].length; columnIndex++) {
                    if (cards[rowIndex][columnIndex] != null) {
                        board.append("[" +cards[rowIndex][columnIndex].getColor() + "] ");
                    } else {
                        board.append("[XXX] ");
                    }
                }
                board.append("\n");
            }
            return board.toString();
        }

        public String displayAllCardsWithPositionDetails(Card card) {
            StringBuilder board = new StringBuilder();
            for (int rowIndex = 0; rowIndex < cards.length; rowIndex++) {
                for (int columnIndex = 0; columnIndex < cards[rowIndex].length; columnIndex++) {
                    if (cards[rowIndex][columnIndex] != null) {
                        if (card != null && rowIndex == card.getXPos() && columnIndex == card.getYPos()) {
                            board.append("[" + card.getColor() + "], ");
                        } else {
                            board.append("[" + (rowIndex+1) + ", " + (columnIndex+1) + "],");
                        }
                    } else {
                        board.append(("[XXX], "));
                    }
                }
                board.append("\n");
            }
            return board.toString();
        }

        public int getCardsLeft() {
            int cardLeft;
            cardLeft = 0;
            for (int rowIndex = 0; rowIndex < cards.length; rowIndex++) {
                for (int j = 0; j < cards[rowIndex].length; j++) {
                    if (cards[rowIndex][j] != null) {
                        cardLeft++;
                    }
                }
            }
            return cardLeft;
        }
    public String startGame() {
        createGameBoard();
        StringBuilder startGameInstruction = new StringBuilder();
        startGameInstruction.append(DISPLAY_RULES());
        startGameInstruction.append(displayAllCardsWithPositionDetails(null));
        return startGameInstruction.toString();
    }

    public String playGame(CardApi card1, CardApi card2) {
        StringBuilder playResponse = new StringBuilder();
        int card1PosX = card1.posX();
        int card1PosY = card1.posY();
        int card2PosX = card2.posX();
        int card2PosY = card2.posY();
        Card drawnCard1 = drawACard(card1PosX, card1PosY);
        Card drawnCard2 = drawACard(card2PosX, card2PosY);
        isCard1AndCard2APair(drawnCard1, drawnCard2);
        return playResponse.toString();
    }


    private void isCard1AndCard2APair(Card card1, Card card2) {
        if (isAPairOfCards(card1, card2)) {
            System.out.println("Pair. You got 1 point.");
            totalPoints++;
            removeCard(card1.getXPos(), card1.getYPos());
            removeCard(card2.getXPos(), card2.getYPos());
        } else {
            System.out.println("No pair. You lost 1 point.");
            totalPoints--;
        }
    }


    public static boolean isAPairOfCards(Card card1, Card card2) {
        return card1.equals(card2);
    }

    private final String DISPLAY_RULES() {
        return (
                """
                Welcome to Colour Memory Game.
                              
                Game Rules:
                Draw two cards from the game board below.
                A pair of same colour gives 1 point and 
                two different cards gives -1 point. Each card 
                position is numbered from left to right 
                from the top left corner of the board and starts 
                at row position 1 and column position 1 and 
                so on. When a correct pair has been drawn from 
                the game board it will be removed and marked as [XXX]. 
                The game is completed when all cards have been drawn 
                from the game board and the total score will be
                present.. 
                """);
    }
}
