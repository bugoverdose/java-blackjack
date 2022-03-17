package blackjack.domain.participant;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardBundle;
import blackjack.domain.card.CardHandState;
import blackjack.domain.game.DuelResult;
import blackjack.strategy.CardHandStateStrategy;
import blackjack.strategy.CardSupplier;
import blackjack.strategy.HitOrStayChoiceStrategy;
import java.util.List;
import java.util.stream.Collectors;

public class Player extends Participant {

    private static final int INITIAL_PLAYER_OPEN_CARDS_COUNT = 2;
    private static final String BLACK_NAME_INPUT_EXCEPTION_MESSAGE = "플레이어는 이름을 지녀야 합니다.";
    private static final String INVALID_PLAYER_NAME_EXCEPTION_MESSAGE = "플레이어의 이름은 딜러가 될 수 없습니다.";
    private static final CardHandStateStrategy STATE_UPDATE_STRATEGY =
            (cards) -> CardHandState.of(cards, CardBundle::isBlackjackScore);

    private final String name;

    private Player(final String name, final CardBundle cardBundle) {
        super(cardBundle, STATE_UPDATE_STRATEGY);
        this.name = name;
    }

    public static Player of(final String name, final CardBundle cardBundle) {
        validateName(name);
        return new Player(name, cardBundle);
    }

    private static void validateName(final String name) {
        validateNameExistence(name);
        validateNameNotDealer(name);
    }

    private static void validateNameExistence(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException(BLACK_NAME_INPUT_EXCEPTION_MESSAGE);
        }
    }

    private static void validateNameNotDealer(final String name) {
        if (name.equals(Dealer.UNIQUE_NAME)) {
            throw new IllegalArgumentException(INVALID_PLAYER_NAME_EXCEPTION_MESSAGE);
        }
    }

    public void hitOrStay(final HitOrStayChoiceStrategy hitOrStay,
                          final CardSupplier supplier) {
        if (hitOrStay.shouldHit(name)) {
            receiveCard(supplier.getCard());
            return;
        }
        cardHand.stay();
    }

    private void receiveCard(final Card card) {
        cardHand.hit(card, STATE_UPDATE_STRATEGY);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Card> getInitialOpenCards() {
        return cardHand.getCards()
                .stream()
                .limit(INITIAL_PLAYER_OPEN_CARDS_COUNT)
                .collect(Collectors.toUnmodifiableList());
    }

    public DuelResult getDuelResultWith(Dealer dealer) {
        if (dealer.isBlackjackOrBust() || this.isBlackjackOrBust()) {
            return getSpecialDuelResultWith(dealer);
        }
        return DuelResult.from(this.getScore(), dealer.getScore());
    }

    private DuelResult getSpecialDuelResultWith(Dealer dealer) {
        if (this.isOnlyPlayerBlackjackWith(dealer)) {
            return DuelResult.BLACKJACK_WIN;
        }
        if (this.isOnlyDealerBustWith(dealer)) {
            return DuelResult.WIN;
        }
        if (this.isBothBlackjackWith(dealer)) {
            return DuelResult.DRAW;
        }
        return DuelResult.LOSE;
    }

    private boolean isOnlyPlayerBlackjackWith(Dealer dealer) {
        return !dealer.isBlackjack() && this.isBlackjack();
    }

    private boolean isOnlyDealerBustWith(Dealer dealer) {
        return dealer.isBust() && !this.isBust();
    }

    private boolean isBothBlackjackWith(Dealer dealer) {
        return dealer.isBlackjack() && this.isBlackjack();
    }

    @Override
    public String toString() {
        return "Player{" +
                "cardHand=" + cardHand +
                ", name='" + name + '\'' +
                '}';
    }
}
