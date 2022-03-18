package blackjack.domain.betting2;

import blackjack.domain.participant2.Participant;
import blackjack.strategy.BettingStrategy;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerBettings {

    private final List<PlayerBetting> value;

    private PlayerBettings(List<PlayerBetting> value) {
        this.value = value;
    }

    public static PlayerBettings of(List<Participant> players, BettingStrategy bettingStrategy) {

        List<PlayerBetting> playerBettings = players.stream()
                .map(player -> initPlayerBetting(bettingStrategy, player))
                .collect(Collectors.toUnmodifiableList());

        return new PlayerBettings(playerBettings);
    }

    private static PlayerBetting initPlayerBetting(BettingStrategy bettingStrategy,
                                                   Participant player) {
        String playerName = player.getName();
        int bettingAmount = bettingStrategy.getBettingAmount(playerName);

        return new PlayerBetting(player, bettingAmount);
    }

    public List<PlayerBetting> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PlayerBettings{" + "value=" + value + '}';
    }
}
