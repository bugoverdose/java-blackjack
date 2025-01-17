package blackjack.domain.participant;

import static blackjack.fixture.CardRepository.CLOVER10;
import static blackjack.fixture.CardRepository.CLOVER2;
import static blackjack.fixture.CardRepository.CLOVER3;
import static blackjack.fixture.CardRepository.CLOVER4;
import static blackjack.fixture.CardRepository.CLOVER8;
import static blackjack.fixture.CardRepository.CLOVER_KING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardBundle;
import blackjack.domain.hand.CardHand;
import blackjack.domain.hand.OneCard;
import blackjack.fixture.CardSupplierStub;
import blackjack.strategy.CardsViewStrategy;
import blackjack.strategy.HitOrStayStrategy;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private static final HitOrStayStrategy HIT_CHOICE = () -> true;
    private static final HitOrStayStrategy STAY_CHOICE = () -> false;
    private static final CardsViewStrategy VIEW_STRATEGY = (p) -> {
    };

    private CardHand cardHand;
    private Participant player;

    @BeforeEach
    void setUp() {
        cardHand = new OneCard(CLOVER8).hit(CLOVER_KING);
        player = new Player("jeong", cardHand);
    }

    @DisplayName("공백으로 구성된 이름으로 플레이어를 생성하려는 경우 예외가 발생한다.")
    @Test
    void constructor_throwExceptionOnBlankName() {
        assertThatThrownBy(() -> new Player("  ", cardHand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("플레이어는 이름을 지녀야 합니다.");
    }

    @DisplayName("플레이어의 이름을 '딜러'로 생성하려는 경우 예외가 발생한다.")
    @Test
    void constructor_throwExceptionOnDealerName() {
        assertThatThrownBy(() -> new Player("딜러", cardHand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("플레이어의 이름은 딜러가 될 수 없습니다.");
    }

    @DisplayName("플레이어 drawAll 메서드 테스트")
    @Nested
    class DrawAllCardsTest {

        @DisplayName("계속 드로우하도록 하면 21이상이 될 때까지 카드를 추가한다.")
        @Test
        void drawAll_keepDrawingUntilBust() {
            List<Card> cardsToBeAdded = List.of(CLOVER2, CLOVER3, CLOVER4);

            player.drawAll(HIT_CHOICE, VIEW_STRATEGY, new CardSupplierStub(cardsToBeAdded));

            assertThat(extractCards(player)).containsExactly(CLOVER8, CLOVER_KING, CLOVER2, CLOVER3);
        }

        @DisplayName("계속 드로우하도록 해도 21이 되었을 때 카드 뽑기를 중단한다.")
        @Test
        void drawAll_stayOn21() {
            List<Card> cardsToBeAdded = List.of(CLOVER3, CLOVER4);

            player.drawAll(HIT_CHOICE, VIEW_STRATEGY, new CardSupplierStub(cardsToBeAdded));

            assertThat(extractCards(player)).containsExactly(CLOVER8, CLOVER_KING, CLOVER3);
        }

        @DisplayName("stay를 선택하는 경우 21이 아니어도 카드를 뽑지 않는다.")
        @Test
        void drawAll_stopsOnStay() {
            player.drawAll(STAY_CHOICE, VIEW_STRATEGY, () -> CLOVER10);

            assertThat(extractCards(player)).containsExactly(CLOVER8, CLOVER_KING);
        }
    }

    @DisplayName("openInitialCards 메서드는 플레이어가 초기에 받은 두 장 카드만을 반환한다.")
    @Test
    void openInitialCards() {
        player.drawAll(HIT_CHOICE, VIEW_STRATEGY, CardSupplierStub.of(CLOVER2, CLOVER10));

        List<Card> actual = player.openInitialCards();

        assertThat(actual).containsExactly(CLOVER8, CLOVER_KING);
    }

    @DisplayName("이름과 카드 패의 구성이 동일한 플레이어는 서로 동일하다고 간주된다.")
    @Test
    void equals_trueOnSameNameAndCardsComposition() {
        Player player1 = new Player("jeong", generateCardHand());
        Player player2 = new Player("jeong", generateCardHand());

        assertThat(player1).isEqualTo(player2);
    }

    @DisplayName("이름과 카드 패의 구성이 동일한 딜러의 해쉬 값은 서로 동일하다.")
    @Test
    void hashCode_trueOnSameNameAndCardsComposition() {
        Player player1 = new Player("jeong", generateCardHand());
        Player player2 = new Player("jeong", generateCardHand());

        assertThat(player1.hashCode())
                .isEqualTo(player2.hashCode());
    }

    private List<Card> extractCards(Participant participant) {
        CardHand cardHand = participant.getHand();
        CardBundle cardBundle = cardHand.getCardBundle();
        return cardBundle.getCards();
    }

    private CardHand generateCardHand() {
        return new OneCard(CLOVER4).hit(CLOVER_KING);
    }
}
