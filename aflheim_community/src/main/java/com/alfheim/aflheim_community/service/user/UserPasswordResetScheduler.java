package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.model.user.RecordState;
import com.alfheim.aflheim_community.model.user.UserPasswordReset;
import com.alfheim.aflheim_community.repository.UserPasswordResetRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserPasswordResetScheduler {

    private final UserPasswordResetRepo userPasswordResetRepo;

    public UserPasswordResetScheduler(UserPasswordResetRepo userPasswordResetRepo) {
        this.userPasswordResetRepo = userPasswordResetRepo;
    }

    @Scheduled(fixedRate = 300000) // 5 minutes
    public void expireOldUserPasswordResetRecords() {
        System.out.println("EXPIRING OLD USER PASSWORD RESET RECORDS");
        List<UserPasswordReset> activePasswordResetRecords = userPasswordResetRepo.findByState(RecordState.ACTIVE);
        for (UserPasswordReset record : activePasswordResetRecords) {
            if (record.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
                record.setState(RecordState.EXPIRED);
                userPasswordResetRepo.save(record);
            }
        }
    }
}
