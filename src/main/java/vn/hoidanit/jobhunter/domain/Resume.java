package vn.hoidanit.jobhunter.domain;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;

@Entity
@Table(name = "resumes")
@Getter
@Setter
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String url;

    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;

    private Instant createdAt;
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.updatedAt = Instant.now();

    }
}
