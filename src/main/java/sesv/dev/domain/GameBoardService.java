package sesv.dev.domain;

import org.springframework.stereotype.Service;
import sesv.dev.application.CardApi;
import sesv.dev.error.CardIsAlreadyDrawnException;

import java.util.*;
import java.util.stream.Stream;

@Service
public class GameBoardService {

    private Map<Integer, Card> cards;
    private static int totalPoints = 0;

    public void createGameBoard(Integer cards) {
       this.cards = new HashMap<>();
        List<Color> pairOfCards = new ArrayList<>();
        pairOfCards.addAll(Stream.of(Color.values()).limit(cards).toList());
        pairOfCards.addAll(Stream.of(Color.values()).limit(cards).toList());

        for(int i = 0; i < pairOfCards.size(); i++) {
            this.cards.put(i + 1, Card.builder()
                    .color(pairOfCards.get(i))
                    .build());
        }
    }

    public Card drawACard(int number) {

        if (cards.containsKey(number)) {
            return cards.get(number);
        }
        throw new CardIsAlreadyDrawnException();
    }

    public void removeCard(int number) {
        cards.remove(number);
    }

    public String startGame(Integer amountOfPairs) {
        createGameBoard(amountOfPairs);
        StringBuilder startGameInstruction = new StringBuilder();
        startGameInstruction.append(displayRules());
        var cards = getGameBoard();
        shuffleCards(cards);
        startGameInstruction.append(getAllCards());
        return startGameInstruction.toString();
    }

    public void shuffleCards(List<Card> cards) {
        var listOfPairOfColors = new ArrayList<Color>();
        cards.forEach(card -> listOfPairOfColors.add(card.color()));
        Collections.shuffle(listOfPairOfColors);
        this.cards = new HashMap<>();
        for(int i = 0; i < listOfPairOfColors.size(); i++) {
            this.cards.put(i + 1, Card.builder()
                    .color(listOfPairOfColors.get(i))
                    .build());
        }

    }

    public String playGame(CardApi firstCard, CardApi secondCard) {
        StringBuilder playResponse = new StringBuilder();
        int firstCardNumber = firstCard.number();
        int secondCardNumber = secondCard.number();
        Card drawnFirstCard = drawACard(firstCardNumber);
        Card drawnSecondCard = drawACard(secondCardNumber);

        if(isCard1AndCard2APair(drawnFirstCard, drawnSecondCard)) {
            resolveCardsAsPair(playResponse, firstCardNumber, secondCardNumber);
        } else {
            resolveCardsAsNotPair(playResponse);
        }

        playResponse.append(displayScore()).append(getAllCards());

        return playResponse.toString();
    }

    private void resolveCardsAsPair(StringBuilder playResponse, int firstCardNumber, int secondCardNumber){
        totalPoints++;
        playResponse.append("Pair. You got 1 point.\n");
        removeCard(firstCardNumber);
        removeCard(secondCardNumber);

        if(cards.size() == 0) {
            playResponse.append(printGameCompletedMessage());
        }
    }

    private void resolveCardsAsNotPair(StringBuilder playResponse){
        totalPoints--;
        playResponse.append("No pair. You lost 1 point.\n");
    }

    private String printGameCompletedMessage() {
        return String.format("\nGame completed. You got a total of: %d  points.", totalPoints);
    }

    private String displayScore() {
        return "\nPoints: " + totalPoints + "\n";
    }


    private boolean isCard1AndCard2APair(Card card1, Card card2) {
        return card1.equals(card2);
    }

    private String displayRules() {
        return (
                """
                Welcome to Colour Memory Game.
                              
                Game Rules:
                Draw two cards from the game board below.
                A pair of same colour gives 1 point and 
                two different cards gives -1 point. Each card 
                position is a number from 1 - 32 . When a correct 
                pair has been drawn from the game board it will be removed. 
                The game is completed when all cards have been drawn 
                from the game board and the total score will be
                present.. 
                """);
    }

    public List<Card> getGameBoard() {
        return this.cards.values().stream().toList();
    }

    /**
     * Only used for test purposes from the CLI
     * @return
     */
    public String getAllAvailableColours() {
        StringBuilder availableColours = new StringBuilder();
        for(var entry : cards.entrySet()) {
            if(entry.getKey() != null) {
                if(entry.getKey() % 5 == 0) {
                    availableColours.append("\nNr:" + entry.getKey() + ", color:" + entry.getValue().color() + ", ");
                } else {
                    availableColours.append("Nr:" + entry.getKey() + ", color:" + entry.getValue().color() + ", ");
                }
            }
        }
        return availableColours.toString();
    }

    public String getAllCards() {
        StringBuilder availableColours = new StringBuilder();
        for(var entry : cards.entrySet()) {
            if(entry.getKey() != null) {
                if(entry.getKey() % 5 == 0) {
                    availableColours.append("\n" +entry.getKey() + ", ");
                } else {
                    availableColours.append(entry.getKey() + ", ");
                }
            }
        }
        return availableColours.toString();
    }
}
