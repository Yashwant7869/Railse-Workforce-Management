package com.railse.workforcemgmt.dto;

import com.railse.workforcemgmt.model.Status;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private Status status;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String assignedStaffName;
    private Long assignedStaffId;
}