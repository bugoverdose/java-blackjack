package blackjack.dto;

import blackjack.domain.card.Card;
import blackjack.domain.game.Score;
import java.util.Collections;
import java.util.List;

public class CardsDto {

    private final List<Card> cards;
    private final Score score;

    public CardsDto(final List<Card> cards, final Score score) {
        this.cards = Collections.unmodifiableList(cards);
        this.score = score;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Score getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "CardBundleDto{" +
                "cards=" + cards +
                ", score=" + score +
                '}';
    }
}
