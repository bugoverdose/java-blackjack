package blackjack.controller;

import static blackjack.view.InputView.requestPlayerNamesInput;
import static blackjack.view.OutputView.printBettingResults;
import static blackjack.view.OutputView.printDealerBlackjackInfo;
import static blackjack.view.OutputView.printInitialParticipantsCards;

import blackjack.domain.BlackjackGame;
import blackjack.domain.betting.BettingResults;
import blackjack.domain.betting.PlayerBettings;
import blackjack.domain.card.CardBundle;
import blackjack.domain.card.CardDeck;
import blackjack.domain.card.CardStack;
import blackjack.domain.participant.GameParticipants;
import blackjack.domain.participant.Participant;
import blackjack.dto.InitialDistributionDto;
import blackjack.strategy.CardBundleStrategy;
import blackjack.view.InputView;
import blackjack.view.OutputView;
import java.util.List;

public class BlackjackController {

    public BlackjackGame initializeGame() {
        final CardStack cardDeck = new CardDeck();
        final List<String> playerNames = requestPlayerNamesInput();
        final CardBundleStrategy strategy = (cardStack) -> CardBundle.of(cardStack.pop(), cardStack.pop());

        return new BlackjackGame(cardDeck, playerNames, strategy);
    }

    public PlayerBettings initializeBettings(final BlackjackGame game) {
        List<Participant> players = game.getParticipants().getPlayers();

        return PlayerBettings.of(players, InputView::requestBettingAmountInput);
    }

    public void playGame(final BlackjackGame game) {
        final InitialDistributionDto dto = InitialDistributionDto.of(game);

        if (game.isBlackjackDealer()) {
            printDealerBlackjackInfo(dto);
            return;
        }

        printInitialParticipantsCards(dto);
        distributeAllCards(game);
    }

    private void distributeAllCards(final BlackjackGame game) {
        game.drawAllPlayerCards(
                InputView::requestMoreCardInput,
                OutputView::printPlayerCardDistributionInfo);
        game.drawDealerCards(OutputView::printDealerExtraCardInfo);
    }

    public void showBettingResults(final BlackjackGame game, final PlayerBettings bettings) {
        final GameParticipants participants = game.getParticipants();
        final Participant dealer = participants.getDealer();

        final BettingResults results = new BettingResults(dealer, bettings);
        printBettingResults(results.getValue());
    }
}
