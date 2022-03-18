package blackjack.domain.hand;

import static blackjack.fixture.CardBundleFixture.CARD_BUNDLE_10;
import static blackjack.fixture.CardRepository.CLOVER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StayTest {

    CardHand cardHand;

    @BeforeEach
    void setUp() {
        cardHand = new Stay(CARD_BUNDLE_10());
    }

    @DisplayName("hit 메서드 실행시, 예외가 발생한다.")
    @Test
    void hit_exception() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> cardHand.hit(CLOVER2));
    }

    @DisplayName("stay 메서드 실행시, 예외가 발생한다.")
    @Test
    void stay() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy((cardHand::stay));
    }

    @DisplayName("isFinished 메서드 실행시 true가 반환된다.")
    @Test
    void isFinished() {
        boolean isFinished = cardHand.isFinished();

        assertThat(isFinished).isTrue();
    }

    @DisplayName("isBlackjack 메서드 실행시 false가 반환된다.")
    @Test
    void isBlackjack() {
        boolean isBlackjack = cardHand.isBlackjack();

        assertThat(isBlackjack).isFalse();
    }

    @DisplayName("isBust 메서드 실행시 false가 반환된다.")
    @Test
    void isBust() {
        boolean isBust = cardHand.isBust();

        assertThat(isBust).isFalse();
    }
}
