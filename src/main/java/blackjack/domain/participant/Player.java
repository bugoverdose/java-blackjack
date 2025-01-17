package blackjack.domain.participant;

import static blackjack.domain.participant.validator.PlayerNameValidator.validateNameNotBlank;
import static blackjack.domain.participant.validator.PlayerNameValidator.validateNameNotDealer;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardBundle;
import blackjack.domain.hand.CardHand;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Player extends BlackjackParticipant {

    private static final int OPEN_CARDS_COUNT = 2;
    private static final int MAX_HIT_SCORE = 21;

    private final String name;

    public Player(final String name, final CardHand cardHand) {
        super(cardHand);
        validateNameNotBlank(name);
        validateNameNotDealer(name);
        this.name = name;
    }

    @Override
    public List<Card> openInitialCards() {
        return cardHand.getCardBundle()
                .getCards()
                .stream()
                .limit(OPEN_CARDS_COUNT)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    protected boolean shouldStay() {
        CardBundle cardBundle = cardHand.getCardBundle();
        return cardBundle.hasScoreOf(MAX_HIT_SCORE);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return Objects.equals(name, player.name)
                && Objects.equals(getCardBundle(), player.getCardBundle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getCardBundle());
    }

    @Override
    public String toString() {
        return "Player{" +
                "cardHand=" + cardHand +
                ", name='" + name + '\'' +
                '}';
    }
}
