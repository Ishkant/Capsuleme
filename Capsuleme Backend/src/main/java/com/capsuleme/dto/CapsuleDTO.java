package com.capsuleme.dto;

import java.time.LocalDate;
import java.util.List;

public class CapsuleDTO {

    // -------------------------------------------------------
    // Request object when creating a new capsule
    // -------------------------------------------------------
    public static class CreateRequest {
        private String userName;
        private String userEmail;
        private String title;
        private String letter;
        private LocalDate unlockDate;
        private List<String> goals;
        private int moodScore;
        private int confidenceScore;
        private String biggestFear;
        private String lifeInOneWord;

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public String getUserEmail() { return userEmail; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getLetter() { return letter; }
        public void setLetter(String letter) { this.letter = letter; }

        public LocalDate getUnlockDate() { return unlockDate; }
        public void setUnlockDate(LocalDate unlockDate) { this.unlockDate = unlockDate; }

        public List<String> getGoals() { return goals; }
        public void setGoals(List<String> goals) { this.goals = goals; }

        public int getMoodScore() { return moodScore; }
        public void setMoodScore(int moodScore) { this.moodScore = moodScore; }

        public int getConfidenceScore() { return confidenceScore; }
        public void setConfidenceScore(int confidenceScore) { this.confidenceScore = confidenceScore; }

        public String getBiggestFear() { return biggestFear; }
        public void setBiggestFear(String biggestFear) { this.biggestFear = biggestFear; }

        public String getLifeInOneWord() { return lifeInOneWord; }
        public void setLifeInOneWord(String lifeInOneWord) { this.lifeInOneWord = lifeInOneWord; }
    }

    // -------------------------------------------------------
    // Response object sent back to the frontend
    // -------------------------------------------------------
    public static class CapsuleResponse {
        private Long id;
        private String userName;
        private String userEmail;
        private String title;
        private String letter;
        private LocalDate unlockDate;
        private boolean unlocked;
        private String createdAt;
        private List<GoalDTO.GoalResponse> goals;
        private SnapshotDTO.SnapshotResponse pastSnapshot;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public String getUserEmail() { return userEmail; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getLetter() { return letter; }
        public void setLetter(String letter) { this.letter = letter; }

        public LocalDate getUnlockDate() { return unlockDate; }
        public void setUnlockDate(LocalDate unlockDate) { this.unlockDate = unlockDate; }

        public boolean isUnlocked() { return unlocked; }
        public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

        public List<GoalDTO.GoalResponse> getGoals() { return goals; }
        public void setGoals(List<GoalDTO.GoalResponse> goals) { this.goals = goals; }

        public SnapshotDTO.SnapshotResponse getPastSnapshot() { return pastSnapshot; }
        public void setPastSnapshot(SnapshotDTO.SnapshotResponse pastSnapshot) { this.pastSnapshot = pastSnapshot; }
    }

    // -------------------------------------------------------
    // Request object when unlocking a capsule
    // -------------------------------------------------------
    public static class UnlockRequest {
        private int moodScore;
        private int confidenceScore;
        private String biggestFear;
        private String lifeInOneWord;
        private List<GoalVerdictItem> goalVerdicts;

        public int getMoodScore() { return moodScore; }
        public void setMoodScore(int moodScore) { this.moodScore = moodScore; }

        public int getConfidenceScore() { return confidenceScore; }
        public void setConfidenceScore(int confidenceScore) { this.confidenceScore = confidenceScore; }

        public String getBiggestFear() { return biggestFear; }
        public void setBiggestFear(String biggestFear) { this.biggestFear = biggestFear; }

        public String getLifeInOneWord() { return lifeInOneWord; }
        public void setLifeInOneWord(String lifeInOneWord) { this.lifeInOneWord = lifeInOneWord; }

        public List<GoalVerdictItem> getGoalVerdicts() { return goalVerdicts; }
        public void setGoalVerdicts(List<GoalVerdictItem> goalVerdicts) { this.goalVerdicts = goalVerdicts; }

        public static class GoalVerdictItem {
            private Long goalId;
            private boolean achieved;

            public Long getGoalId() { return goalId; }
            public void setGoalId(Long goalId) { this.goalId = goalId; }

            public boolean isAchieved() { return achieved; }
            public void setAchieved(boolean achieved) { this.achieved = achieved; }
        }
    }
}
