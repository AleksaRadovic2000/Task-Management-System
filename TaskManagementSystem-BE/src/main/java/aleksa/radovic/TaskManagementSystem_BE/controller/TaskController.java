package aleksa.radovic.TaskManagementSystem_BE.controller;

import aleksa.radovic.TaskManagementSystem_BE.model.Task;
import aleksa.radovic.TaskManagementSystem_BE.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task newTask = taskService.createTask(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "priority", required = false) String priority,
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "assignedTo", required = false) Long assignedTo
    ) {
        List<Task> tasks = taskService.getAllTasks(status, priority, projectId, assignedTo);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask (@PathVariable Long id) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }
}
