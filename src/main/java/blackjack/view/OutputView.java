package blackjack.view;

import blackjack.domain.card.Card;
import blackjack.domain.game.BlackjackGame;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Player;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OutputView {

    private static final String NEW_LINE = System.lineSeparator();

    private static final String JOIN_DELIMITER = ", ";
    private static final String INITIAL_CARD_DISTRIBUTION_MESSAGE = NEW_LINE + "딜러와 %s에게 2장의 나누었습니다." + NEW_LINE;
    private static final String DEALER_INITIAL_CARD_FORMAT = "딜러: %s" + NEW_LINE;
    private static final String PLAYER_CARDS_FORMAT = "%s카드: %s" + NEW_LINE;
    private static final String PLAYER_BUST_MESSAGE = "21을 초과하여 패배하였습니다!";
    private static final String DEALER_EXTRA_CARD_MESSAGE = "딜러는 16이하라 한장의 카드를 더 받았습니다.";

    // TODO: DTO 로 변경
    public static void printInitialParticipantsCards(BlackjackGame blackjackGame) {
        StringBuilder builder = new StringBuilder();

        builder.append(getParticipantsCardCountInfo(blackjackGame))
                .append(getDealerCardInfo(blackjackGame.getDealer()));

        List<Player> players = blackjackGame.getParticipants();
        for (Player player : players) {
            builder.append(getParticipantCardsInfo(player));
        }

        print(builder.toString());
    }

    public static void printPlayerCardsInfo(Player player) {
        System.out.print(getParticipantCardsInfo(player));
    }

    public static void printPlayerBustInfo() {
        print(PLAYER_BUST_MESSAGE);
    }

    public static void printDealerExtraCardInfo() {
        print(DEALER_EXTRA_CARD_MESSAGE);
    }

    private static String getParticipantsCardCountInfo(BlackjackGame blackjackGame) {
        String playerNames = mapAndJoinString(blackjackGame.getParticipants(), Player::getName);
        return String.format(INITIAL_CARD_DISTRIBUTION_MESSAGE, playerNames);
    }

    private static String getDealerCardInfo(Dealer dealer) {
        Card dealerCard = dealer.getOpenCard();
        return String.format(DEALER_INITIAL_CARD_FORMAT, dealerCard.getName());
    }

    private static String getParticipantCardsInfo(Player player) {
        String playerCards = mapAndJoinString(player.getCardBundle().getCards(), Card::getName);
        return String.format(PLAYER_CARDS_FORMAT, player.getName(), playerCards);
    }

    private static <T> String mapAndJoinString(Collection<T> collection, Function<T, String> function) {
        return collection.stream()
                .map(function)
                .collect(Collectors.joining(JOIN_DELIMITER));
    }

    private static void print(String text) {
        System.out.println(text);
    }
}
