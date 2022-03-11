package blackjack.controller;

import static blackjack.view.InputView.requestMoreCardInput;
import static blackjack.view.InputView.requestPlayerNamesInput;
import static blackjack.view.OutputView.printAllCardsAndScore;
import static blackjack.view.OutputView.printDealerExtraCardInfo;
import static blackjack.view.OutputView.printGameResult;
import static blackjack.view.OutputView.printInitialParticipantsCards;
import static blackjack.view.OutputView.printPlayerBustInfo;
import static blackjack.view.OutputView.printPlayerCardsInfo;

import blackjack.domain.card.CardDeck;
import blackjack.domain.game.BlackjackGame;
import blackjack.domain.game.ResultReferee;
import blackjack.domain.participant.Player;
import blackjack.dto.InitialDistributionDto;
import blackjack.dto.ResultStatisticsDto;
import java.util.List;

public class BlackjackController {

    public BlackjackGame initializeGame() {
        List<String> playerNames = requestPlayerNamesInput();
        return new BlackjackGame(new CardDeck(), playerNames);
    }

    public void showInitialDistribution(BlackjackGame game) {
        InitialDistributionDto dto = new InitialDistributionDto(game.getParticipants());
        printInitialParticipantsCards(dto);
    }

    public void distributeAllCards(BlackjackGame game) {
        List<Player> players = game.getPlayers();
        players.forEach(player -> drawAllPlayerCards(player, game));
        drawDealerCard(game);
    }

    private void drawAllPlayerCards(Player player, BlackjackGame game) {
        while (player.canReceive() && requestMoreCardInput(player.getName())) {
            player.receiveCard(game.popCard());
            printPlayerCardsInfo(player);
        }
        showPlayerBust(player);
    }

    private void showPlayerBust(Player player) {
        if (player.canReceive()) {
            printPlayerBustInfo();
        }
    }

    private void drawDealerCard(BlackjackGame game) {
        if (game.giveCardToDealer()) {
            printDealerExtraCardInfo();
        }
    }

    public void showGameResult(BlackjackGame game) {
        ResultReferee referee = new ResultReferee(game.getDealer(), game.getPlayers());
        ResultStatisticsDto dto = new ResultStatisticsDto(referee.initAndGetGameResults());

        printAllCardsAndScore(dto);
        printGameResult(dto);
    }
}
