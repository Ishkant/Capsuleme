package com.capsuleme.dto;

public class GoalDTO {

    public static class GoalResponse {
        private Long id;
        private String goalText;
        private Boolean achieved;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getGoalText() { return goalText; }
        public void setGoalText(String goalText) { this.goalText = goalText; }

        public Boolean getAchieved() { return achieved; }
        public void setAchieved(Boolean achieved) { this.achieved = achieved; }
    }
}
