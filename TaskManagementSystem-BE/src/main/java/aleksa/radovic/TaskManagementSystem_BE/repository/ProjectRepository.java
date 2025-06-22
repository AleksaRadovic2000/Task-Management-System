package aleksa.radovic.TaskManagementSystem_BE.repository;

import aleksa.radovic.TaskManagementSystem_BE.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
}
