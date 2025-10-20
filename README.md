# CheckInn – Hotel Reservation System

CheckInn is a **Java-based hotel reservation system** is now a **database-connected GUI system**.

## Project Description

This project manages **rooms, users, and reservations** to make and view reservations.
It's been built to practice **OOPS concepts in Java** and to satisfy all the syllabus requirements, including database connectivity and modern GUI design (yeah using swing, it's very serious).

## Features

### Phase One (Initial Stage)

* [x] Implement core classes: Hotel, Room, Guest, Reservation, Main.
* [x] Add rooms and guests.
* [x] Create reservations with check-in/check-out dates.
* [x] Print details to console (text-only — those simple times...).
* [x] Maintain proper folder structure.
* [x] Use `.gitignore` to keep junk out of GitHub.

### Current Features (Phases Two, Three, Four, and Five Implemented) ✅

* [x] **Minimal GUI:** Full Java Swing interface with modern **FlatMacDarkLaf** (Dark Mode) theming.
* [x] **Architecture:** Applied **MVC Pattern** and **DAO Pattern** for clean separation of concerns.
* [x] **Data Persistence:** Integrated **JDBC** for secure **MySQL** connectivity.
* [x] **Error Handling:** Added **Custom Exceptions** (`RoomNotAvailableException`, etc.) for cleaner errors.

### Roadmap

* [ ] Eat plenty, and have a good night's sleep.

## Project Structure

```
CheckInn/
├── src/com/hotel/
│   ├── dao/             # Data Access Objects (UserDAO, RoomDAO, etc.)
│   ├── ui/              # GUI Classes (LoginFrame, DashboardFrame)
│   ├── model/           # Core Data Models (User, Room, Reservation)
│   ├── exceptions/      # Custom exceptions
│   ├── Main             # As the name suggests
│   ├── DBUtil           # Database connection
│   └── resources/       # Images and Icons
├── lib/                 # External JARs (flatlaf, mysql-connector)
├── docs/                # Docs (proposal, design, notes)
├── build/               # Compiled .class files (ignored by Git)
├── .gitignore           # Ignore the trash (and the secrets!)
└── README.md            # This file
```

-----

## Tech Stack

* **Java** (JDK 17+)
* **MySQL** (The source of all failure)
* **Swing** (The beautiful UI layer)
* **JDBC** (The bridge to the database)
* **FlatLaf** (Because light mode is a crime)
* Git & GitHub

## Getting Started

### 1. Software & Database Prerequisites

Before running the application, ensure you have the following installed and configured:

* **Java Development Kit (JDK) 17+**
* **MySQL Server** (Running locally on the standard port 3306).
* **External Libraries:** The necessary JAR files (MySQL Connector, FlatLaf) must be present in the `lib/` folder and configured in your project's module dependencies.

### 2. Database Setup (Schema Creation)

You must create the database schema required by the application.

1.  Log in to your MySQL server.
2.  **Create the Database:** Execute `CREATE DATABASE CheckInn;`
3.  **Load Schema:** Execute the SQL script provided in the `docs/` folder (e.g., `docs/schema.sql`) to create the necessary tables (`users`, `rooms`, `reservations`, etc.).

### 3. Application Configuration

You **must** configure your local MySQL credentials as environment variables for the application to connect securely.

### Compile

```bash
# This now requires the JARs to be on the classpath!
javac -cp "lib/*:build" -d build src/com/hotel/**/*.java
```

### Run

```bash
# Ensure lib path is correct for your OS and Java version
java -cp "lib/*:build" com.hotel.Main
```

## Current Contributor

* Just me.
  (I’m the dev, the manager, the customer, and the poor soul debugging at 3 am.)

## Notes

* This is for my **Java syllabus**.
* If it breaks… well, that’s a feature for now.

