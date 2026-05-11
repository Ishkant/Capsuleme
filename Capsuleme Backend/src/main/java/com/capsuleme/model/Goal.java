package com.capsuleme.model;

import jakarta.persistence.*;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String goalText;

    // null = not reviewed yet, true = achieved, false = not achieved
    private Boolean achieved = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", nullable = false)
    private Capsule capsule;

    // ---- Getters ----

    public Long getId() {
        return id;
    }

    public String getGoalText() {
        return goalText;
    }

    public Boolean getAchieved() {
        return achieved;
    }

    public Capsule getCapsule() {
        return capsule;
    }

    // ---- Setters ----

    public void setId(Long id) {
        this.id = id;
    }

    public void setGoalText(String goalText) {
        this.goalText = goalText;
    }

    public void setAchieved(Boolean achieved) {
        this.achieved = achieved;
    }

    public void setCapsule(Capsule capsule) {
        this.capsule = capsule;
    }
}
