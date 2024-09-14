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

    /**
     * Only used for test purposes from the CLI
     * @return
     */
    public List<String> displayAllColourCards() {
        return Stream.of(cards).map(String::valueOf).toList();
    }


    public String startGame(Integer amountOfPairs) {
        createGameBoard(amountOfPairs);
        StringBuilder startGameInstruction = new StringBuilder();
        startGameInstruction.append(DISPLAY_RULES());
        var cards = getGameBoard();
        shuffleCards(cards);
        startGameInstruction.append(getAllAvailableColours());
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

    public String playGame(CardApi card1, CardApi card2) {
        StringBuilder playResponse = new StringBuilder();
        int cardNumber1 = card1.number();
        int cardNumber2 = card2.number();
        Card drawnCard1 = drawACard(cardNumber1);
        Card drawnCard2 = drawACard(cardNumber2);
        if(isCard1AndCard2APair(drawnCard1, drawnCard2)) {
            totalPoints++;
            playResponse.append("Pair. You got 1 point.");
            removeCard(cardNumber1);
            removeCard(cardNumber2);
        } else {
            totalPoints--;
            playResponse.append("No pair. You lost 1 point.");
        }
        playResponse.append(displayScore());
        playResponse.append(getAllAvailableColours());
        return playResponse.toString();
    }

    private String displayScore() {
        return "\nPoints: " + totalPoints;
    }


    private boolean isCard1AndCard2APair(Card card1, Card card2) {
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

    public String getAllAvailableColours() {
        StringBuilder availableColours = new StringBuilder();
        for(int i = 1; i <= this.cards.keySet().size(); i++) {
            if(this.cards.get(i) != null) {
                if(i % 5 == 0) {
                    availableColours.append("\n" +  "Nr:" + i + ", color:" + this.cards.get(i).color() + ",");
                } else {
                    availableColours.append("Nr:" + i + ", color:" +this.cards.get(i).color() + ", ");
                }
            }
        }
        return availableColours.toString();
    }

    public String getAllCards() {
        StringBuilder availableColours = new StringBuilder();
        for(int i = 1; i < this.cards.keySet().size(); i++) {
            if(this.cards.get(i) != null) {
                if(i % 6 == 0) {
                    availableColours.append("\n");
                } else {
                    availableColours.append(i + ", ");
                }
            }
        }
        return availableColours.toString();
    }
}
