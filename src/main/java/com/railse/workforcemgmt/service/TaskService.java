package com.railse.workforcemgmt.service;

import com.railse.workforcemgmt.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final Map<Long, Staff> staff = new ConcurrentHashMap<>();
    private final AtomicLong taskCounter = new AtomicLong();
    private final AtomicLong staffCounter = new AtomicLong();

    public TaskService() {
        // Pre-populate with some staff for testing
        staff.put(1L, new Staff(1L, "Alice"));
        staff.put(2L, new Staff(2L, "Bob"));
    }

    // *** FEATURE 1: Smart Daily Task View ***
    // Enhanced logic: Shows tasks that started in range + active tasks from before that are still open
    public List<Task> findTasksByDateRange(LocalDate start, LocalDate end) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() != Status.CANCELLED)  // Exclude cancelled tasks
                .filter(task -> {
                    // Case 1: Tasks that started within the date range
                    boolean startedInRange = !task.getStartDate().isBefore(start) && 
                                           !task.getStartDate().isAfter(end);
                    
                    // Case 2: Active tasks that started before the range but are still open
                    boolean activeFromBefore = task.getStartDate().isBefore(start) && 
                                             task.getStatus() == Status.ACTIVE &&
                                             !task.getDueDate().isBefore(start); // Due date overlaps with range
                    
                    return startedInRange || activeFromBefore;
                })
                .collect(Collectors.toList());
    }

    public Task createTask(String title, LocalDate startDate, LocalDate dueDate, Long staffId) {
        return createTask(title, startDate, dueDate, staffId, Priority.MEDIUM); // Default priority
    }
    
    // FEATURE 2: Enhanced createTask with priority
    public Task createTask(String title, LocalDate startDate, LocalDate dueDate, Long staffId, Priority priority) {
        Staff assignedStaff = staff.get(staffId);
        if (assignedStaff == null) {
            throw new IllegalArgumentException("Staff not found with ID: " + staffId);
        }
        Task task = new Task();
        task.setId(taskCounter.incrementAndGet());
        task.setTitle(title);
        task.setStartDate(startDate);
        task.setDueDate(dueDate);
        task.setAssignedStaff(assignedStaff);
        task.setStatus(Status.ACTIVE);
        task.setPriority(priority != null ? priority : Priority.MEDIUM);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        
        // FEATURE 3: Log task creation activity
        addActivityLog(task, "CREATED", "Task created and assigned to " + assignedStaff.getName(), "System");
        
        tasks.put(task.getId(), task);
        return task;
    }

    // *** BUG 1 FIXED ***
    // Now properly cancels the old task before creating a new one.
    public Task reassignTask(Long oldTaskId, Long newStaffId) {
        Task oldTask = tasks.get(oldTaskId);
        if (oldTask == null) {
            throw new IllegalArgumentException("Task not found with ID: " + oldTaskId);
        }
        
        // Fix: Mark the old task as CANCELLED to avoid duplicates
        oldTask.setStatus(Status.CANCELLED);
        addActivityLog(oldTask, "CANCELLED", "Task cancelled for reassignment", "System");
        
        // Create and return the new task with the new staff assignment
        Task newTask = createTask(oldTask.getTitle(), oldTask.getStartDate(), oldTask.getDueDate(), newStaffId, oldTask.getPriority());
        Staff newStaff = staff.get(newStaffId);
        addActivityLog(newTask, "REASSIGNED", "Task reassigned to " + newStaff.getName(), "System");
        
        return newTask;
    }
    
    // FEATURE 2: Priority management methods
    public Task updateTaskPriority(Long taskId, Priority newPriority) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found with ID: " + taskId);
        }
        
        Priority oldPriority = task.getPriority();
        task.setPriority(newPriority);
        task.setUpdatedAt(LocalDateTime.now());
        
        addActivityLog(task, "PRIORITY_CHANGED", 
            "Priority changed from " + oldPriority + " to " + newPriority, "Manager");
        
        return task;
    }
    
    public List<Task> findTasksByPriority(Priority priority) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() != Status.CANCELLED)
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }
    
    // NEW ENDPOINT: Get all tasks (excluding cancelled)
    public List<Task> findAllTasks() {
        return tasks.values().stream()
                .filter(task -> task.getStatus() != Status.CANCELLED)
                .collect(Collectors.toList());
    }
    
    // NEW ENDPOINT: Get truly all tasks (including cancelled for admin view)
    public List<Task> findAllTasksIncludingCancelled() {
        return new ArrayList<>(tasks.values());
    }
    
    // FEATURE 3: Comments and Activity History methods
    private final AtomicLong commentCounter = new AtomicLong();
    private final AtomicLong activityCounter = new AtomicLong();
    
    public Task addComment(Long taskId, String comment, String authorName) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found with ID: " + taskId);
        }
        
        TaskComment taskComment = new TaskComment(
            commentCounter.incrementAndGet(),
            comment,
            authorName,
            LocalDateTime.now()
        );
        
        task.getComments().add(taskComment);
        task.setUpdatedAt(LocalDateTime.now());
        
        addActivityLog(task, "COMMENT_ADDED", "Comment added by " + authorName, authorName);
        
        return task;
    }
    
    private void addActivityLog(Task task, String action, String description, String performedBy) {
        ActivityLog log = new ActivityLog(
            activityCounter.incrementAndGet(),
            action,
            description,
            performedBy,
            LocalDateTime.now()
        );
        task.getActivityHistory().add(log);
    }
    
    public Task getTaskWithHistory(Long taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found with ID: " + taskId);
        }
        // Task already contains comments and activity history
        // Sort them chronologically
        task.getActivityHistory().sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        task.getComments().sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        
        return task;
    }
}