package blackjack.dto;

import blackjack.domain.BlackjackGame;
import blackjack.domain.participant.Participants;
import blackjack.domain.participant.Participant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InitialDistributionDto {

    private final List<ParticipantCardsDto> participantsInfo = new ArrayList<>();

    private InitialDistributionDto(final Participants participants) {
        participants.getValue()
                .forEach(this::initParticipantInfo);
    }

    public static InitialDistributionDto of(final BlackjackGame game) {
        return new InitialDistributionDto(game.getParticipants());
    }

    private void initParticipantInfo(final Participant participant) {
        ParticipantCardsDto participantCardInfo = ParticipantCardsDto.ofInitial(participant);
        participantsInfo.add(participantCardInfo);
    }

    public List<ParticipantCardsDto> getParticipantsInfo() {
        return Collections.unmodifiableList(participantsInfo);
    }

    public List<String> getAllParticipantNames() {
        return participantsInfo.stream()
                .map(ParticipantCardsDto::getName)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return "InitialDistributionDto{" + "participantsInfo=" + participantsInfo + '}';
    }
}
