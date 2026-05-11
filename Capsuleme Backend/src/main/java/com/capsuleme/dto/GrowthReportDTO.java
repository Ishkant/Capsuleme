package com.capsuleme.dto;

import java.util.List;

public class GrowthReportDTO {

    private String userName;
    private String capsuleTitle;
    private String createdAt;
    private String unlockedAt;
    private String letter;

    private SnapshotDTO.SnapshotResponse pastSnapshot;
    private SnapshotDTO.SnapshotResponse presentSnapshot;

    private List<GoalResult> goalResults;
    private int totalGoals;
    private int achievedGoals;

    private int pastTotalScore;
    private int presentTotalScore;
    private int moodChange;
    private int confidenceChange;
    private int growthScore;

    // ---- Getters ----

    public String getUserName() { return userName; }
    public String getCapsuleTitle() { return capsuleTitle; }
    public String getCreatedAt() { return createdAt; }
    public String getUnlockedAt() { return unlockedAt; }
    public String getLetter() { return letter; }
    public SnapshotDTO.SnapshotResponse getPastSnapshot() { return pastSnapshot; }
    public SnapshotDTO.SnapshotResponse getPresentSnapshot() { return presentSnapshot; }
    public List<GoalResult> getGoalResults() { return goalResults; }
    public int getTotalGoals() { return totalGoals; }
    public int getAchievedGoals() { return achievedGoals; }
    public int getPastTotalScore() { return pastTotalScore; }
    public int getPresentTotalScore() { return presentTotalScore; }
    public int getMoodChange() { return moodChange; }
    public int getConfidenceChange() { return confidenceChange; }
    public int getGrowthScore() { return growthScore; }

    // ---- Setters ----

    public void setUserName(String userName) { this.userName = userName; }
    public void setCapsuleTitle(String capsuleTitle) { this.capsuleTitle = capsuleTitle; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUnlockedAt(String unlockedAt) { this.unlockedAt = unlockedAt; }
    public void setLetter(String letter) { this.letter = letter; }
    public void setPastSnapshot(SnapshotDTO.SnapshotResponse pastSnapshot) { this.pastSnapshot = pastSnapshot; }
    public void setPresentSnapshot(SnapshotDTO.SnapshotResponse presentSnapshot) { this.presentSnapshot = presentSnapshot; }
    public void setGoalResults(List<GoalResult> goalResults) { this.goalResults = goalResults; }
    public void setTotalGoals(int totalGoals) { this.totalGoals = totalGoals; }
    public void setAchievedGoals(int achievedGoals) { this.achievedGoals = achievedGoals; }
    public void setPastTotalScore(int pastTotalScore) { this.pastTotalScore = pastTotalScore; }
    public void setPresentTotalScore(int presentTotalScore) { this.presentTotalScore = presentTotalScore; }
    public void setMoodChange(int moodChange) { this.moodChange = moodChange; }
    public void setConfidenceChange(int confidenceChange) { this.confidenceChange = confidenceChange; }
    public void setGrowthScore(int growthScore) { this.growthScore = growthScore; }

    // ---- Inner class ----

    public static class GoalResult {
        private Long goalId;
        private String goalText;
        private boolean achieved;
        private String verdict;

        public Long getGoalId() { return goalId; }
        public void setGoalId(Long goalId) { this.goalId = goalId; }

        public String getGoalText() { return goalText; }
        public void setGoalText(String goalText) { this.goalText = goalText; }

        public boolean isAchieved() { return achieved; }
        public void setAchieved(boolean achieved) { this.achieved = achieved; }

        public String getVerdict() { return verdict; }
        public void setVerdict(String verdict) { this.verdict = verdict; }
    }
}
