Thodar Railways 
===============

Overview
--------

Thodar Railways is a comprehensive, console-based Java application designed to simulate the core backend operations of the Indian Railway ticket booking system (similar to IRCTC).

The project follows a modular, feature-based Clean Architecture and uses a Singleton in-memory repository pattern for storing application data. With the latest updates, the system now integrates advanced capabilities including journey planning, file management, and automated notifications.

The application supports:

*   User onboarding and role-based access (Admin / Passenger)
    
*   Advanced Journey Planning and Route Management
    
*   Dynamic train routing and automated pair-train generation
    
*   Quota-based ticket booking (GNWL, PQWL, TQWL)
    
*   Real-time waitlist and RAC recalculations
    
*   Integrated financial wallet and robust transaction tracking
    
*   Support helpdesk, automated notifications, and revenue analytics
    
*   File management for data exports and logs
    

📊 Project Information
----------------------

**PropertyValue**Project NameThodar RailwaysLanguageJava (JDK 8+)Architecture StyleFeature-based MVC / Clean ArchitectureData StorageIn-memory repository (Singleton)Entry PointMain.javaPackage Rootcom.sathish.thodarVersion5.0.0

🏛️ High-Level Architecture
---------------------------

The project is structured using a decoupled MVC-inspired Clean Architecture, separating Data, Business Logic, and UI/Presentation.

### 1\. View / Feature Layer (features/)

Handles user interactions, console I/O, and feature-specific workflows.

*   Examples: AuthView, AdminView, PassengerView, RouteView, NotificationView
    

### 2\. Service Layer (service/)

Handles core mathematical calculations and business logic independently of the UI.

*   Examples: TrainService (Waitlist logic, quota distribution, capacity calculation)
    

### 3\. DTO Layer (data/dto/)

Manages structural entities and data transfer securely between layers.

*   **enums:** TicketClass, TransactionType, TicketQuota, etc.
    
*   **request:** Payloads for incoming actions (e.g., BookingRequest, TrainSetupRequest).
    
*   **response:** Formatted outputs (e.g., TicketSummaryResponse, LiveStatusResponse).
    

### 4\. Repository Layer (data/repository/)

Centralized in-memory persistence using the Singleton pattern.

*   Example: ThodarDB
    

Project Structure
-----------------

\## 📂 Project Structure

\`\`\`text

src/

└── com/

└── sathish/

└── thodar/

├── Main.java

│

├── data/

│ ├── dto/

│ │ ├── enums/

│ │ │ ├── Role.java

│ │ │ ├── ScheduleStatus.java

│ │ │ ├── TicketClass.java

│ │ │ ├── TicketQuota.java

│ │ │ ├── TicketStatus.java

│ │ │ ├── TransactionStatus.java

│ │ │ └── TransactionType.java

│ │ │

│ │ ├── request/

│ │ │ ├── admin/

│ │ │ │ ├── ScheduleRequest.java

│ │ │ │ └── TrainSetupRequest.java

│ │ │ ├── auth/

│ │ │ │ ├── LoginRequest.java

│ │ │ │ └── RegisterRequest.java

│ │ │ └── passenger/

│ │ │ └── BookingRequest.java

│ │ │

│ │ └── response/

│ │ ├── admin/

│ │ │ └── PassengerListResponse.java

│ │ ├── auth/

│ │ │ └── AuthResponse.java

│ │ └── passenger/

│ │ ├── LiveStatusResponse.java

│ │ ├── TicketSummaryResponse.java

│ │ └── Transaction.java

│ │

│ └── repository/

│ └── ThodarDB.java

│

├── features/

│ ├── admin/

│ │ └── AdminView.java

│ ├── auth/

│ │ └── AuthView.java

│ ├── filemanagement/

│ │ ├── FileModel.java

│ │ └── FileView.java

│ ├── journeyplanning/

│ │ ├── RouteModel.java

│ │ └── RouteView.java

│ ├── notification/

│ │ ├── NotificationModel.java

│ │ └── NotificationView.java

│ ├── passenger/

│ │ └── PassengerView.java

│ ├── reporting/

│ │ ├── ReportModel.java

│ │ └── ReportView.java

│ └── support/

│ ├── SupportModel.java

│ └── SupportView.java

│

├── service/

│ └── TrainService.java

│

└── util/

├── ConsoleInput.java

└── ParseHelper.java

⚙️ Core Modules & Features
--------------------------

### 1\. Authentication Module

*   **Sign Up:** Passengers create accounts and initialize zero-balance financial wallets.
    
*   **Sign In:** Secure login routing users to Admin or Passenger dashboards.
    

### 2\. Admin Module

*   **Train Setup & Scheduling:** Configure coach compositions (1A, 2A, 3A, SL), map routes, and schedule dates.
    
*   **Passenger Charts:** View detailed physical seating charts segmented by classes.
    

### 3\. Journey Planning Module

*   **Smart Routing:** Define source and destination logic from master lines.
    
*   **Auto-Pairing:** Intelligently auto-generates return trains with reversed routes (Odd/Even numbering mapping).
    

### 4\. Booking & Service Engine (The Core)

*   **Quota Allocation:** \* **GNWL (General):** Allocated to the first 3 stations.
    
    *   **PQWL (Pooled):** Allocated to intermediate stations (20% capacity).
        
    *   **TQWL (Tatkal):** Strict 24-hour window, exactly 12 seats per coach.
        
*   **Waitlist Shift Algorithm:** Auto-upgrades RAC/WL passengers based on priority when confirmed tickets are cancelled.
    
*   **Group Seating Optimization:** Prevents PNR fragmentation by attempting to keep passengers in the same physical coach.
    

### 5\. Passenger Module

*   **Search & Book:** Smart train discovery by Date and Quota. Integrated simulated payment gateway offering Wallet, UPI, or Card options.
    
*   **Cancellations:** Processes immediate cancellations with automated 80% wallet refunds and dynamic waitlist adjustments.
    

### 6\. Notification & File Management

*   **Alerts:** Dispatch booking confirmations, payment success, and waitlist upgrade notifications.
    
*   **File Exports:** Export ticket summaries, passenger charts, and application logs seamlessly.
    

### 7\. Reporting & Support

*   **Analytics:** Generate daily revenue tracking and booking trend reports.
    
*   **Helpdesk:** Ticketing system for passengers to raise issues and admins to resolve them.
    

📖 Data Dictionary
------------------

### Key Entities

*   TrainSetupRequest: Defines train number, name, path sequence, and coach multipliers.
    
*   BookingRequest: Contains PNR, user mapping, schedule mapping, passenger lists, and quota parameters.
    
*   Transaction: Ledger entry detailing TxnID, amounts, types (TransactionType), and statuses (TransactionStatus).
    

### Key Enums

*   TicketClass: SL, AC\_3A, AC\_2A, AC\_1A
    
*   TicketQuota: GENERAL, TATKAL
    
*   TransactionType: CREDIT, DEBIT
    
*   TicketStatus: CNF, RAC, WL, CAN
    

🚀 How to Run the Project
-------------------------

### Requirements

*   Java JDK 8 or higher
    
*   Standard Java IDE (IntelliJ IDEA, Eclipse) or Command Line
    

### Execution Steps

1.  git clone https://github.com/yourusername/thodar-railways.git
    
2.  cd thodar-railways/src
    
3.  javac com/sathish/thodar/Main.java
    
4.  java com.sathish.thodar.Main
    

### Default Credentials

*   **Admin Role:** Email: admin@thodar.com | Password: admin123
    
*   **Passenger Role:** Create a new user via the "Passenger Register" menu option to generate a fresh wallet.
    

🔮 Future Scope
---------------

*   **Database Integration:** Implementation of persistent DB (MySQL/PostgreSQL) via JDBC.
    
*   **REST APIs:** Spring Boot migration for RESTful API exposure and microservices.
    
*   **Concurrency:** Multithreading implementation (e.g., ReentrantLocks) for handling high-volume Tatkal traffic concurrency.
    
*   **Frontend:** Integration with React.js or Angular for a seamless Web UI.