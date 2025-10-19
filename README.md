This is the updated `README.md` file, reflecting the project's current state as a secure, database-integrated GUI application. The original format, including the humorous and project-focused tone, has been maintained.

-----

# CheckInn – Hotel Reservation System

CheckInn is a **Java-based hotel reservation system** is now a **database-connected GUI system**.

## Project Description

This project manages **rooms, users, and reservations**.
It's been built to practice **OOPS concepts in Java** and to satisfy all the syllabus requirements, including database connectivity, modern GUI design (yeah using swing, it's very serious), and critical security implementations.

The syllabus demanded a circus, and I'm showing them who's the real clown is...

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
* [x] **Secure Config:** Database credentials handled via **Environment Variables** (no secrets on GitHub\!).
* [x] **Data Persistence:** Integrated **JDBC** for secure **MySQL** connectivity.
* [x] **UX Improvement:** Replaced awkward dropdowns with **JTable** single-selection (radio buttons) for room choosing.
* [x] **Error Handling:** Added **Custom Exceptions** (`RoomNotAvailableException`, etc.) for cleaner errors.

### Roadmap

* [ ] Eat plenty, and have a good night's sleep.

## Project Structure

```
CheckInn/
├── src/com/hotel/
│   ├── dao/             # Data Access Objects (UserDAO, RoomDAO, etc.)
│   ├── frames/          # GUI Classes (LoginFrame, DashboardFrame)
│   ├── model/           # Core Data Models (User, Room, Reservation)
│   ├── Main             # As the name suggests
│   ├── DBUtil           # Database connection
│   └── resources/       # Images and Icons
├── lib/                 # External JARs (flatlaf, jbcrypt, mysql-connector)
├── test/                 # JUnit test classes (still future, but getting closer)
├── docs/                 # Docs (proposal, design, notes)
├── build/                # Compiled .class files (ignored by Git)
├── .gitignore            # Ignore the trash (and the secrets!)
└── README.md             # This file
```

-----

## Tech Stack

* **Java** (JDK 17+)
* **MySQL** (The source of all failure)
* **Swing** (The beautiful UI layer)
* **JDBC** (The bridge to the database)
* **FlatLaf** (Because light mode is a crime)
* Git & GitHub (still not submitting zip files)
* JUnit (still later)

## Getting Started

### Prerequisites

You **must** configure your local MySQL credentials as environment variables for the application to connect securely.

* Set `DB_USER` and `DB_PASSWORD` in your IDE's Run Configuration.

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
* Every phase exists only because the professor wants to see “concepts.”
* If it breaks… well, that’s a feature for now.