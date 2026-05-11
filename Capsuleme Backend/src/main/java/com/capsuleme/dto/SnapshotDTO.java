package com.capsuleme.dto;

public class SnapshotDTO {

    public static class SnapshotResponse {
        private Long id;
        private String type;
        private int moodScore;
        private int confidenceScore;
        private String biggestFear;
        private String lifeInOneWord;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public int getMoodScore() { return moodScore; }
        public void setMoodScore(int moodScore) { this.moodScore = moodScore; }

        public int getConfidenceScore() { return confidenceScore; }
        public void setConfidenceScore(int confidenceScore) { this.confidenceScore = confidenceScore; }

        public String getBiggestFear() { return biggestFear; }
        public void setBiggestFear(String biggestFear) { this.biggestFear = biggestFear; }

        public String getLifeInOneWord() { return lifeInOneWord; }
        public void setLifeInOneWord(String lifeInOneWord) { this.lifeInOneWord = lifeInOneWord; }
    }
}
