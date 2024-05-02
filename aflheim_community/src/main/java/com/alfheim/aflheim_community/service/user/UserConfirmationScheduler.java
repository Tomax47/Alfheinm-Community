package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.model.user.RecordState;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import com.alfheim.aflheim_community.repository.UserConfirmationRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserConfirmationScheduler {
    private final UserConfirmationRepo userConfirmationRepository;

    public UserConfirmationScheduler(UserConfirmationRepo userConfirmationRepository) {
        this.userConfirmationRepository = userConfirmationRepository;
    }

    @Scheduled(fixedRate = 10000) // 10 minutes
    public void expireOldUserConfirmations() {
        System.out.println("EXPIRING OLD USER CONFIRMATIONS");
        List<UserConfirmation> activeConfirmations = userConfirmationRepository.findByState(RecordState.ACTIVE);
        for (UserConfirmation confirmation : activeConfirmations) {
            if (confirmation.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(60))) {
                confirmation.setState(RecordState.EXPIRED);
                userConfirmationRepository.save(confirmation);
            }
        }
    }
}
