package aleksa.radovic.TaskManagementSystem_BE.service;

import aleksa.radovic.TaskManagementSystem_BE.model.Project;
import aleksa.radovic.TaskManagementSystem_BE.model.Task;
import aleksa.radovic.TaskManagementSystem_BE.model.User;
import aleksa.radovic.TaskManagementSystem_BE.repository.ProjectRepository;
import aleksa.radovic.TaskManagementSystem_BE.repository.TaskRepository;
import aleksa.radovic.TaskManagementSystem_BE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;

    public Task createTask(Task task) {
        if(task.getProject() != null) {
            Project project = projectRepository.findById(task.getProject().getId())
                    .orElseThrow(()-> new RuntimeException("project not found"));
            task.setProject(project);
        }
        if(task.getAssignedTo() != null) {
            User user = userRepository.findById(task.getAssignedTo().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedTo(user);
        }
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(String status, String priority, Long projectId, Long assignedTo) {
        return taskRepository.findAll().stream()
                .filter(task -> status == null || task.getStatus().name().equalsIgnoreCase(status))
                .filter(task -> priority == null || task.getPriority().name().equalsIgnoreCase(priority))
                .filter(task -> projectId == null || (task.getProject() != null && task.getProject().getId().equals(projectId)))
                .filter(task -> assignedTo == null || (task.getAssignedTo() != null && task.getAssignedTo().getId().equals(assignedTo)))
                .collect(Collectors.toList());
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());

        if (taskDetails.getProject() != null) {
            Project project = projectRepository.findById(taskDetails.getProject().getId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            task.setProject(project);
        }

        if (taskDetails.getAssignedTo() != null) {
            User user = userRepository.findById(taskDetails.getAssignedTo().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedTo(user);
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(existingTask);
    }
}
