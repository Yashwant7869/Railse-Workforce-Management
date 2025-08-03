 package com.railse.workforcemgmt.dto;

import lombok.Data;
import java.time.LocalDate;
import com.railse.workforcemgmt.model.Priority;

@Data
public class CreateTaskRequest {
    private String title;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Long assignedStaffId;
    private Priority priority; // FEATURE 2: Optional priority field
}