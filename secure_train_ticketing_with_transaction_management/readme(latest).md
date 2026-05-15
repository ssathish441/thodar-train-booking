Thodar Railways 🚂
==================

\===============

Overview
--------

Thodar Railways is a comprehensive, console-based Java application designed to simulate the core backend operations of the Indian Railway ticket booking system (similar to IRCTC).

The project follows a modular, feature-based **Clean Architecture** and uses a Singleton in-memory repository pattern for storing application data. With the latest updates, the system focuses strictly on high-performance core railway operations.

**The application supports:**

*   User onboarding and role-based access (Admin / Passenger)
    
*   60-Days Advance Reservation Period (ARP) bulk scheduling
    
*   Dynamic train routing and automated paired-train generation
    
*   Quota-based ticket booking (GNWL, PQWL, TQWL)
    
*   Real-time waitlist and RAC auto-upgrades upon cancellations
    
*   Integrated financial wallet and robust transaction tracking
    
*   Support helpdesk and daily revenue analytics
    

📊 Project Information
----------------------

**Project Name:** Thodar Railways**Language:** Java (JDK 8+)**Architecture Style:** Feature-based MVC / Clean Architecture**Data Storage:** In-memory repository (Singleton Pattern)**Entry Point:** Main.java**Package Root:** com.sathish.thodar**Version:** 5.0.0

🏛️ High-Level Architecture
---------------------------

The project is structured using a decoupled Clean Architecture, separating Data, Business Logic, and UI/Presentation.

### 1\. View / Feature Layer (features/)

Handles user interactions, console I/O, and feature-specific workflows.

*   _Examples:_ AuthView, AdminView, PassengerView
    

### 2\. Core Business Logic Layer (features/core/)

Acts as the brain of the application, handling complex mathematical calculations independently of the UI.

*   _Examples:_ TrainService (Waitlist logic, quota distribution, capacity calculation)
    

### 3\. DTO Layer (data/dto/)

Manages structural entities and transfers data securely between layers to prevent tight coupling.

*   **enums:** TicketClass, TransactionType, TicketQuota, etc.
    
*   **request:** Payloads for incoming actions (BookingRequest, TrainSetupRequest).
    
*   **response:** Formatted outputs mapped from DB entities (PassengerListResponse, TicketSummaryResponse).
    

### 4\. Repository Layer (data/repository/)

Centralized in-memory persistence using the **Singleton** pattern.

*   _Examples:_ ThodarDB
    

📂 Project Structure
--------------------

src/
└── com/sathish/thodar/
├── Main.java
├── data/
│   ├── dto/
│   │   ├── enums/
│   │   │   ├── Role.java
│   │   │   ├── ScheduleStatus.java
│   │   │   ├── TicketClass.java
│   │   │   ├── TicketQuota.java
│   │   │   ├── TicketStatus.java
│   │   │   └── TransactionType.java
│   │   ├── request/
│   │   │   ├── admin/
│   │   │   │   ├── ScheduleRequest.java
│   │   │   │   └── TrainSetupRequest.java
│   │   │   ├── auth/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   └── RegisterRequest.java
│   │   │   └── passenger/
│   │   │       └── BookingRequest.java
│   │   └── response/
│   │       ├── admin/
│   │       │   └── PassengerListResponse.java
│   │       ├── auth/
│   │       │   └── AuthResponse.java
│   │       └── passenger/
│   │           ├── LiveStatusResponse.java
│   │           ├── TicketSummaryResponse.java
│   │           └── Transaction.java
│   └── repository/
│       └── ThodarDB.java
├── features/
│   ├── admin/
│   │   └── AdminView.java
│   ├── auth/
│   │   └── AuthView.java
│   ├── core/                  <-- Core Business Logic
│   │   └── TrainService.java
│   ├── passenger/
│   │   └── PassengerView.java
│   ├── reporting/
│   │   ├── ReportModel.java
│   │   └── ReportView.java
│   └── support/
│       ├── SupportModel.java
│       └── SupportView.java
└── util/
├── ConsoleInput.java
└── ParseHelper.java

⚙️ Core Modules & Features
--------------------------

### 1\. Authentication Module

*   **Sign Up:** Passengers create accounts and initialize zero-balance financial wallets.
    
*   **Sign In:** Secure login routing users to Admin or Passenger dashboards.
    

### 2\. Admin & Route Management

*   **Train Setup & Scheduling:** Configure coach compositions (1A, 2A, 3A, SL), map routes, and schedule dates dynamically.
    
*   **Auto-Pairing:** Intelligently auto-generates return trains with reversed routes.
    
*   **Passenger Charts:** View detailed physical seating charts segmented by classes.
    

### 3\. Booking & Service Engine (The Core)

*   **Quota Allocation:** \* **GNWL (General):** Allocated to end-to-end passengers.
    
    *   **PQWL (Pooled):** Allocated to intermediate stations (~20% capacity).
        
    *   **TQWL (Tatkal):** Strict 24-hour window, capped at ~30% capacity.
        
*   **Waitlist Shift Algorithm:** Auto-upgrades RAC/WL passengers based on priority when confirmed tickets are cancelled.
    
*   **Group Seating Optimization:** Prevents PNR fragmentation by attempting to keep passengers in the same physical coach.
    

### 4\. Passenger Module

*   **Search & Book:** Smart train discovery by Date and Quota. Integrated simulated payment gateway offering Wallet, UPI, or Card options.
    
*   **Cancellations:** Processes immediate cancellations with automated 80% wallet refunds and dynamic waitlist adjustments.
    

### 5\. Reporting & Support

*   **Analytics:** Generate daily revenue tracking and booking trend reports.
    
*   **Helpdesk:** Ticketing system for passengers to raise issues and admins to resolve them.
    
*   **System Simulation:** Admin tools to inject dummy traffic and simulate bulk cancellations for testing waitlist algorithms.
    

📖 Data Dictionary
------------------

### Key Entities

*   **TrainSetupRequest:** Defines train number, name, path sequence, and coach multipliers.
    
*   **BookingRequest:** Contains PNR, user mapping, schedule mapping, passenger lists, and quota parameters.
    
*   **Transaction:** Ledger entry detailing TxnID, amounts, types (TransactionType), and statuses.
    

### Key Enums

*   **TicketClass:** SL, AC\_3A, AC\_2A, AC\_1A
    
*   **TicketQuota:** GENERAL, TATKAL
    
*   **TransactionType:** CREDIT, DEBIT
    
*   **TicketStatus:** CNF, RAC, WL, CAN
    

🚀 How to Run the Project
-------------------------

### Requirements

*   Java JDK 8 or higher
    
*   Standard Java IDE (IntelliJ IDEA, Eclipse) or Command Line
    

### Execution Steps

1.  git clone https://github.com/ssathish441/thodar-train-booking.git
    
2.  cd thodar-train-booking/src
    
3.  javac com/sathish/thodar/Main.java
    
4.  java com.sathish.thodar.Main
    

### Default Credentials

*   **Admin Role:** Email: admin@thodar.com | Password: Admin@123
    
*   **Passenger Role:** Create a new user via the "Passenger Register" menu option to generate a fresh wallet.
    

🔮 Future Scope
---------------

*   **Database Integration:** Implementation of a persistent SQL/RDBMS layer via JDBC to replace in-memory structures.
    
*   **REST APIs:** Spring Boot migration for RESTful API exposure and microservices architecture.
    
*   **Concurrency:** Multithreading implementation (e.g., ReentrantLocks) for handling high-volume Tatkal traffic concurrency.
    
*   **Frontend:** Integration with React.js or Angular for a seamless Web UI.
