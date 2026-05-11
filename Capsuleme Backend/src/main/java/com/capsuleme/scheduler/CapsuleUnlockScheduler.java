package com.capsuleme.scheduler;

import com.capsuleme.model.Capsule;
import com.capsuleme.service.CapsuleService;
import com.capsuleme.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CapsuleUnlockScheduler {

    @Autowired
    private CapsuleService capsuleService;

    @Autowired
    private EmailService emailService;

    // Runs every day at 8:00 AM
    // Cron: second minute hour day month weekday
    @Scheduled(cron = "0 0 8 * * *")
    public void sendUnlockEmails() {
        System.out.println("=== Scheduler: Checking for capsules to unlock ===");

        List<Capsule> dueCapsules = capsuleService.getCapsulesDueToday();

        if (dueCapsules.isEmpty()) {
            System.out.println("No capsules due today.");
            return;
        }

        System.out.println("Found " + dueCapsules.size() + " capsule(s) due today.");

        for (Capsule capsule : dueCapsules) {
            try {
                emailService.sendUnlockEmail(capsule);
                System.out.println("  -> Email sent to: " + capsule.getUserEmail()
                        + " for capsule: " + capsule.getTitle());
            } catch (Exception e) {
                System.err.println("  -> Email failed for capsule id=" + capsule.getId()
                        + " | Error: " + e.getMessage());
            }
        }

        System.out.println("=== Scheduler done ===");
    }
}
