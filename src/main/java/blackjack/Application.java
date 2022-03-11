package blackjack;

import blackjack.controller.BlackjackController;
import blackjack.domain.game.BlackjackGame;

public class Application {

    private static final BlackjackController controller = new BlackjackController();

    public static void main(String[] args) {
        BlackjackGame game = controller.initializeGame();
        controller.showInitialDistribution(game);
        controller.distributeAllCards(game);
        controller.showGameResult(game);
    }
}
