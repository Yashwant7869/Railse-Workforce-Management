package com.railse.workforcemgmt.model;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
public class Task {
    private Long id;
    private String title;
    private Status status;
    private Priority priority;  // FEATURE 2: Task Priority
    private LocalDate startDate;
    private LocalDate dueDate;
    private Staff assignedStaff;
    
    // FEATURE 3: Comments & Activity History
    private List<TaskComment> comments = new ArrayList<>();
    private List<ActivityLog> activityHistory = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}