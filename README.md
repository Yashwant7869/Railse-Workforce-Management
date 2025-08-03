# 🏢 Workforce Management API

A comprehensive Spring Boot REST API for managing workforce tasks with advanced features like priority management, comments, and activity history tracking.

## 🌟 Features

### Core Task Management
- ✅ **Create Tasks** - Create new tasks with title, dates, and staff assignment
- ✅ **Task Assignment** - Assign tasks to pre-loaded staff members (Alice, Bob)
- ✅ **Smart Daily View** - Enhanced date-range filtering for task visibility
- ✅ **Task Reassignment** - Transfer tasks between staff members (with proper duplicate handling)

### Advanced Features
- 🎯 **Priority System** - HIGH, MEDIUM, LOW priority levels with filtering
- 💬 **Comments System** - Add and track task-specific comments
- 📊 **Activity History** - Complete audit trail of all task activities
- 🔍 **Status Management** - ACTIVE, COMPLETED, CANCELLED task statuses
- 📋 **List All Tasks** - Comprehensive task listing (excluding cancelled)

### Bug Fixes Implemented
- 🐛 **Fixed Task Reassignment** - No more duplicate tasks during reassignment
- 🐛 **Filtered Cancelled Tasks** - Cancelled tasks no longer clutter daily views

## 🚀 Quick Start

### Prerequisites
- **Java 17+**
- **Gradle** (included via wrapper)

### Installation & Run
```bash
# Clone the repository
git clone https://github.com/Yashwant7869/Railse-Workforce-Management.git

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### Pre-loaded Data
- **Staff Members**: 
  - Alice (ID: 1)
  - Bob (ID: 2)

## 📚 API Documentation

### Task Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/tasks` | Create a new task |
| `GET` | `/tasks/date-range` | Get tasks by date range |
| `PUT` | `/tasks/{id}/reassign` | Reassign task to new staff |
| `GET` | `/tasks/all` | List all active tasks |

### Priority Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `PUT` | `/tasks/{id}/priority` | Update task priority |
| `GET` | `/tasks/priority/{priority}` | Get tasks by priority level |

### Comments & History

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/tasks/{id}/comments` | Add comment to task |
| `GET` | `/tasks/{id}/history` | Get task with full activity history |

## 📝 Example API Usage

### Create a Task
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project documentation",
    "startDate": "2025-08-03",
    "dueDate": "2025-08-10",
    "assignedStaffId": 1,
    "priority": "HIGH"
  }'
```

### Get Tasks by Date Range
```bash
curl "http://localhost:8080/tasks/date-range?start=2025-08-01&end=2025-08-31"
```

### Add Priority to Task
```bash
curl -X PUT "http://localhost:8080/tasks/1/priority?priority=HIGH"
```

### Add Comment
```bash
curl -X POST http://localhost:8080/tasks/1/comments \
  -H "Content-Type: application/json" \
  -d '{
    "comment": "Task is progressing well",
    "authorName": "Alice"
  }'
```

## 🏗️ Project Structure

```
src/main/java/com/railse/workforcemgmt/
├── controller/
│   └── TaskController.java      # REST API endpoints
├── service/
│   └── TaskService.java         # Business logic
├── model/
│   ├── Task.java               # Task entity
│   ├── Staff.java              # Staff entity
│   ├── Priority.java           # Priority enum
│   ├── Status.java             # Status enum
│   ├── TaskComment.java        # Comment entity
│   └── ActivityLog.java        # Activity tracking
└── dto/
    ├── CreateTaskRequest.java   # Request DTOs
    └── TaskDto.java            # Response DTOs
```

## 🔧 Technology Stack

- **Framework**: Spring Boot 3.5.4
- **Language**: Java 17
- **Build Tool**: Gradle
- **Storage**: In-memory (ConcurrentHashMap)
- **Libraries**: Lombok, MapStruct

## 📋 Testing

The project includes comprehensive unit tests. Run them with:

```bash
./gradlew test
```
