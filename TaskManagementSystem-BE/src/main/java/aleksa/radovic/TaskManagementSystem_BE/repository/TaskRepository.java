package aleksa.radovic.TaskManagementSystem_BE.repository;

import aleksa.radovic.TaskManagementSystem_BE.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
