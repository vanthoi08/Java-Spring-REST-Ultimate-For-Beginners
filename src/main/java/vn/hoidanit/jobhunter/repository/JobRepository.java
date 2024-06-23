package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.hoidanit.jobhunter.domain.Job;
import java.util.List;
import vn.hoidanit.jobhunter.domain.Skill;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    List<Job> findBySkillsIn(List<Skill> skills);
}
