package com.capsuleme.service;

import com.capsuleme.dto.*;
import com.capsuleme.model.Capsule;
import com.capsuleme.model.Goal;
import com.capsuleme.model.Snapshot;
import com.capsuleme.repository.CapsuleRepository;
import com.capsuleme.repository.GoalRepository;
import com.capsuleme.repository.SnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CapsuleService {

    @Autowired
    private CapsuleRepository capsuleRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private SnapshotRepository snapshotRepository;

    @Autowired
    private EmailService emailService;

    // -----------------------------------------------------------------------
    // CREATE a new capsule
    // -----------------------------------------------------------------------
    @Transactional
    public CapsuleDTO.CapsuleResponse createCapsule(CapsuleDTO.CreateRequest request) {

        // 1. Save the capsule
        Capsule capsule = new Capsule();
        capsule.setUserName(request.getUserName());
        capsule.setUserEmail(request.getUserEmail());
        capsule.setTitle(request.getTitle());
        capsule.setLetter(request.getLetter());
        capsule.setUnlockDate(request.getUnlockDate());
        capsule = capsuleRepository.save(capsule);

        // 2. Save goals
        if (request.getGoals() != null) {
            for (String goalText : request.getGoals()) {
                if (goalText != null && !goalText.trim().isEmpty()) {
                    Goal goal = new Goal();
                    goal.setGoalText(goalText.trim());
                    goal.setCapsule(capsule);
                    goalRepository.save(goal);
                }
            }
        }

        // 3. Save PAST snapshot
        Snapshot pastSnapshot = new Snapshot();
        pastSnapshot.setType(Snapshot.SnapshotType.PAST);
        pastSnapshot.setMoodScore(request.getMoodScore());
        pastSnapshot.setConfidenceScore(request.getConfidenceScore());
        pastSnapshot.setBiggestFear(request.getBiggestFear());
        pastSnapshot.setLifeInOneWord(request.getLifeInOneWord());
        pastSnapshot.setCapsule(capsule);
        snapshotRepository.save(pastSnapshot);

        // 4. Send confirmation email (safe — won't crash if email fails)
        try {
            emailService.sendSealConfirmationEmail(capsule);
        } catch (Exception e) {
            System.err.println("Email failed but capsule was saved. Error: " + e.getMessage());
        }

        return buildCapsuleResponse(capsule);
    }

    // -----------------------------------------------------------------------
    // GET all capsules by email
    // -----------------------------------------------------------------------
    public List<CapsuleDTO.CapsuleResponse> getCapsulesByEmail(String email) {
        List<Capsule> capsules = capsuleRepository.findByUserEmail(email);
        return capsules.stream()
                .map(this::buildCapsuleResponse)
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------
    // GET single capsule by ID
    // -----------------------------------------------------------------------
    public Optional<CapsuleDTO.CapsuleResponse> getCapsuleById(Long id) {
        return capsuleRepository.findById(id)
                .map(this::buildCapsuleResponse);
    }

    // -----------------------------------------------------------------------
    // UNLOCK a capsule
    // -----------------------------------------------------------------------
    @Transactional
    public GrowthReportDTO unlockCapsule(Long capsuleId, CapsuleDTO.UnlockRequest request) {

        Capsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new RuntimeException("Capsule not found with id: " + capsuleId));

        if (!capsule.isUnlocked()) {
            if (capsule.getUnlockDate().isAfter(LocalDate.now())) {
                throw new RuntimeException("This capsule is not ready yet. Unlock date: " + capsule.getUnlockDate());
            }
        }

        // Save PRESENT snapshot if not already saved
        Optional<Snapshot> existingPresent = snapshotRepository
                .findByCapsuleIdAndType(capsuleId, Snapshot.SnapshotType.PRESENT);

        if (existingPresent.isEmpty()) {
            Snapshot presentSnapshot = new Snapshot();
            presentSnapshot.setType(Snapshot.SnapshotType.PRESENT);
            presentSnapshot.setMoodScore(request.getMoodScore());
            presentSnapshot.setConfidenceScore(request.getConfidenceScore());
            presentSnapshot.setBiggestFear(request.getBiggestFear());
            presentSnapshot.setLifeInOneWord(request.getLifeInOneWord());
            presentSnapshot.setCapsule(capsule);
            snapshotRepository.save(presentSnapshot);
        }

        // Update goal verdicts
        if (request.getGoalVerdicts() != null) {
            for (CapsuleDTO.UnlockRequest.GoalVerdictItem verdict : request.getGoalVerdicts()) {
                Optional<Goal> goalOpt = goalRepository.findById(verdict.getGoalId());
                if (goalOpt.isPresent()) {
                    Goal goal = goalOpt.get();
                    goal.setAchieved(verdict.isAchieved());
                    goalRepository.save(goal);
                }
            }
        }

        // Mark as unlocked
        capsule.setUnlocked(true);
        capsuleRepository.save(capsule);

        return buildGrowthReport(capsule);
    }

    // -----------------------------------------------------------------------
    // GET growth report for an already-unlocked capsule
    // -----------------------------------------------------------------------
    public GrowthReportDTO getGrowthReport(Long capsuleId) {
        Capsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new RuntimeException("Capsule not found with id: " + capsuleId));

        if (!capsule.isUnlocked()) {
            throw new RuntimeException("This capsule has not been unlocked yet.");
        }

        return buildGrowthReport(capsule);
    }

    // -----------------------------------------------------------------------
    // Used by the scheduler
    // -----------------------------------------------------------------------
    public List<Capsule> getCapsulesDueToday() {
        return capsuleRepository.findByUnlockDateLessThanEqualAndUnlockedFalse(LocalDate.now());
    }

    // -----------------------------------------------------------------------
    // Private helper: build growth report
    // -----------------------------------------------------------------------
    private GrowthReportDTO buildGrowthReport(Capsule capsule) {
        GrowthReportDTO report = new GrowthReportDTO();
        report.setUserName(capsule.getUserName());
        report.setCapsuleTitle(capsule.getTitle());
        report.setCreatedAt(capsule.getCreatedAt().toLocalDate().toString());
        report.setUnlockedAt(LocalDate.now().toString());
        report.setLetter(capsule.getLetter());

        Optional<Snapshot> past = snapshotRepository
                .findByCapsuleIdAndType(capsule.getId(), Snapshot.SnapshotType.PAST);
        Optional<Snapshot> present = snapshotRepository
                .findByCapsuleIdAndType(capsule.getId(), Snapshot.SnapshotType.PRESENT);

        if (past.isPresent()) {
            report.setPastSnapshot(buildSnapshotResponse(past.get()));
        }
        if (present.isPresent()) {
            report.setPresentSnapshot(buildSnapshotResponse(present.get()));
        }

        // Goal results
        List<Goal> goals = goalRepository.findByCapsuleId(capsule.getId());
        List<GrowthReportDTO.GoalResult> goalResults = new ArrayList<>();
        int achievedCount = 0;

        for (Goal goal : goals) {
            GrowthReportDTO.GoalResult result = new GrowthReportDTO.GoalResult();
            result.setGoalId(goal.getId());
            result.setGoalText(goal.getGoalText());
            boolean achieved = Boolean.TRUE.equals(goal.getAchieved());
            result.setAchieved(achieved);
            result.setVerdict(achieved ? "Achieved!" : "Not yet");
            if (achieved) {
                achievedCount++;
            }
            goalResults.add(result);
        }

        report.setGoalResults(goalResults);
        report.setTotalGoals(goals.size());
        report.setAchievedGoals(achievedCount);

        // Growth score calculation
        if (past.isPresent() && present.isPresent()) {
            Snapshot p = past.get();
            Snapshot pr = present.get();

            report.setPastTotalScore(p.getMoodScore() + p.getConfidenceScore());
            report.setPresentTotalScore(pr.getMoodScore() + pr.getConfidenceScore());
            report.setMoodChange(pr.getMoodScore() - p.getMoodScore());
            report.setConfidenceChange(pr.getConfidenceScore() - p.getConfidenceScore());

            double scoreImprovement = ((double)(pr.getMoodScore() + pr.getConfidenceScore()) / 20.0) * 40;
            double goalScore = goals.isEmpty() ? 40 : ((double) achievedCount / goals.size()) * 40;
            double completionBonus = 20;
            int growthScore = (int) Math.round(scoreImprovement + goalScore + completionBonus);
            report.setGrowthScore(Math.min(100, growthScore));
        }

        return report;
    }

    // -----------------------------------------------------------------------
    // Private helper: build capsule response
    // -----------------------------------------------------------------------
    private CapsuleDTO.CapsuleResponse buildCapsuleResponse(Capsule capsule) {
        CapsuleDTO.CapsuleResponse response = new CapsuleDTO.CapsuleResponse();
        response.setId(capsule.getId());
        response.setUserName(capsule.getUserName());
        response.setUserEmail(capsule.getUserEmail());
        response.setTitle(capsule.getTitle());
        response.setLetter(capsule.getLetter());
        response.setUnlockDate(capsule.getUnlockDate());
        response.setUnlocked(capsule.isUnlocked());
        response.setCreatedAt(capsule.getCreatedAt().toLocalDate().toString());

        List<Goal> goals = goalRepository.findByCapsuleId(capsule.getId());
        List<GoalDTO.GoalResponse> goalResponses = new ArrayList<>();
        for (Goal g : goals) {
            GoalDTO.GoalResponse gr = new GoalDTO.GoalResponse();
            gr.setId(g.getId());
            gr.setGoalText(g.getGoalText());
            gr.setAchieved(g.getAchieved());
            goalResponses.add(gr);
        }
        response.setGoals(goalResponses);

        Optional<Snapshot> pastSnap = snapshotRepository
                .findByCapsuleIdAndType(capsule.getId(), Snapshot.SnapshotType.PAST);
        if (pastSnap.isPresent()) {
            response.setPastSnapshot(buildSnapshotResponse(pastSnap.get()));
        }

        return response;
    }

    private SnapshotDTO.SnapshotResponse buildSnapshotResponse(Snapshot snapshot) {
        SnapshotDTO.SnapshotResponse response = new SnapshotDTO.SnapshotResponse();
        response.setId(snapshot.getId());
        response.setType(snapshot.getType().name());
        response.setMoodScore(snapshot.getMoodScore());
        response.setConfidenceScore(snapshot.getConfidenceScore());
        response.setBiggestFear(snapshot.getBiggestFear());
        response.setLifeInOneWord(snapshot.getLifeInOneWord());
        return response;
    }
}
