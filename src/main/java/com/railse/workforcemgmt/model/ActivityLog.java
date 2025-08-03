package com.railse.workforcemgmt.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLog {
    private Long id;
    private String action;
    private String description;
    private String performedBy;
    private LocalDateTime timestamp;
}
