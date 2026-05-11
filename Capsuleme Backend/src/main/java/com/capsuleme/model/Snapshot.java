package com.capsuleme.model;

import jakarta.persistence.*;

@Entity
@Table(name = "snapshots")
public class Snapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnapshotType type;

    @Column(nullable = false)
    private int moodScore;

    @Column(nullable = false)
    private int confidenceScore;

    @Column(length = 500)
    private String biggestFear;

    @Column(length = 100)
    private String lifeInOneWord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", nullable = false)
    private Capsule capsule;

    public enum SnapshotType {
        PAST, PRESENT
    }

    // ---- Getters ----

    public Long getId() {
        return id;
    }

    public SnapshotType getType() {
        return type;
    }

    public int getMoodScore() {
        return moodScore;
    }

    public int getConfidenceScore() {
        return confidenceScore;
    }

    public String getBiggestFear() {
        return biggestFear;
    }

    public String getLifeInOneWord() {
        return lifeInOneWord;
    }

    public Capsule getCapsule() {
        return capsule;
    }

    // ---- Setters ----

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(SnapshotType type) {
        this.type = type;
    }

    public void setMoodScore(int moodScore) {
        this.moodScore = moodScore;
    }

    public void setConfidenceScore(int confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public void setBiggestFear(String biggestFear) {
        this.biggestFear = biggestFear;
    }

    public void setLifeInOneWord(String lifeInOneWord) {
        this.lifeInOneWord = lifeInOneWord;
    }

    public void setCapsule(Capsule capsule) {
        this.capsule = capsule;
    }
}
