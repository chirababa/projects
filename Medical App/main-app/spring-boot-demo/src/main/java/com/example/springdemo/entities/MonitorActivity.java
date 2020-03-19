package com.example.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "monitor_activity")
public class MonitorActivity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer idMonitorActivity;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "start_time", nullable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "activity", nullable = false)
    private String activityName;

    @Column(name = "isAnomalous", nullable = false)
    private Boolean isAnomalous;

    @Column(name = "recommendation")
    private String recommendation;

    public Integer getIdMonitorActivity() {
        return idMonitorActivity;
    }

    public void setIdMonitorActivity(Integer idMonitorActivity) {
        this.idMonitorActivity = idMonitorActivity;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Boolean getAnomalous() {
        return isAnomalous;
    }

    public void setAnomalous(Boolean anomalous) {
        isAnomalous = anomalous;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
