package blackjack.view;

import static blackjack.view.OutputView.NEW_LINE;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputView {

    private static final String NAME_INPUT_DELIMITER = ",";
    private static final String YES = "y";
    private static final String NO = "n";
    private static final String REQUEST_PLAYER_NAMES_INPUT_MESSAGE = "게임에 참여할 사람의 이름을 입력하세요. (쉼표 기준으로 분리)";
    private static final String REQUEST_BETTING_AMOUNT_INPUT_MESSAGE = NEW_LINE + "%s의 배팅 금액은?";
    private static final String REQUEST_MORE_CARD_INPUT_FORMAT =
            "한장의 카드를 더 받겠습니까? (예는 " + YES + ", 아니오는 " + NO + ")";
    private static final String INVALID_MORE_PLAYER_CARD_INPUT_EXCEPTION_MESSAGE = YES + "혹은 " + NO + "만 입력해야 합니다.";
    private static final String BETTING_NOT_POSITIVE_EXCEPTION_MESSAGE = "베팅금액은 양의 정수만 입력해야 합니다.";
    private static final String INVALID_BETTING_INPUT_EXCEPTION_MESSAGE = "숫자를 입력해야 합니다.";

    private static final Scanner scanner = new Scanner(System.in);

    private InputView() {
    }

    public static List<String> requestPlayerNamesInput() {
        print(REQUEST_PLAYER_NAMES_INPUT_MESSAGE);
        final String input = scanner.nextLine();

        return getTrimmedStringListOf(input);
    }

    private static List<String> getTrimmedStringListOf(final String input) {
        return Stream.of(input.split(NAME_INPUT_DELIMITER))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public static int requestBettingAmountInput(final String playerName) {
        print(String.format(REQUEST_BETTING_AMOUNT_INPUT_MESSAGE, playerName));

        final int input = parseInt(scanner.nextLine());
        validatePositive(input);

        return input;
    }

    private static void validatePositive(final int input) {
        if (input <= 0) {
            throw new IllegalArgumentException(BETTING_NOT_POSITIVE_EXCEPTION_MESSAGE);
        }
    }

    private static int parseInt(final String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_BETTING_INPUT_EXCEPTION_MESSAGE);
        }
    }

    public static boolean requestMoreCardInput() {
        print(REQUEST_MORE_CARD_INPUT_FORMAT);
        final String input = scanner.nextLine();

        validateInvalidMoreCardInput(input);
        return input.equals(YES);
    }

    private static void validateInvalidMoreCardInput(final String input) {
        if (!input.equals(YES) && !input.equals(NO)) {
            throw new IllegalArgumentException(INVALID_MORE_PLAYER_CARD_INPUT_EXCEPTION_MESSAGE);
        }
    }

    private static void print(final String text) {
        System.out.println(text);
    }
}
