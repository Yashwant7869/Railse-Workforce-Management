package com.railse.workforcemgmt.controller;
import com.railse.workforcemgmt.dto.CreateTaskRequest;
import com.railse.workforcemgmt.model.Task;
import com.railse.workforcemgmt.model.Priority;
import com.railse.workforcemgmt.service.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController                    
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        Task createdTask = taskService.createTask(
                request.getTitle(),
                request.getStartDate(),
                request.getDueDate(),
                request.getAssignedStaffId(),
                request.getPriority()  // FEATURE 2: Include priority in task creation
        );
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Task> tasks = taskService.findTasksByDateRange(startDate, endDate);
        return ResponseEntity.ok(tasks);
    }
    
    // NEW ENDPOINT: Get all tasks (excluding cancelled)
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // NEW ENDPOINT: Get all tasks including cancelled (for admin view)
    @GetMapping("/admin/all")
    public ResponseEntity<List<Task>> getAllTasksIncludingCancelled() {
        List<Task> tasks = taskService.findAllTasksIncludingCancelled();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{id}/reassign")
    public ResponseEntity<Task> reassignTask(
            @PathVariable Long id,
            @RequestParam Long newStaffId) {
        Task reassignedTask = taskService.reassignTask(id, newStaffId);
        return ResponseEntity.ok(reassignedTask);
    }
    
    // FEATURE 2: Priority Management Endpoints
    @PutMapping("/{id}/priority")
    public ResponseEntity<Task> updateTaskPriority(
            @PathVariable Long id,
            @RequestParam Priority priority) {
        Task updatedTask = taskService.updateTaskPriority(id, priority);
        return ResponseEntity.ok(updatedTask);
    }
    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(
            @PathVariable Priority priority) {
        List<Task> tasks = taskService.findTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }
    
    // FEATURE 3: Comments and Activity History Endpoints
    @PostMapping("/{id}/comments")
    public ResponseEntity<Task> addComment(
            @PathVariable Long id,
            @RequestParam String comment,
            @RequestParam String authorName) {
        Task updatedTask = taskService.addComment(id, comment, authorName);
        return ResponseEntity.ok(updatedTask);
    }
    
    @GetMapping("/{id}/history")
    public ResponseEntity<Task> getTaskWithHistory(
            @PathVariable Long id) {
        Task taskWithHistory = taskService.getTaskWithHistory(id);
        return ResponseEntity.ok(taskWithHistory);
    }
}