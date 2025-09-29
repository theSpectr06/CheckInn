# CheckInn – Hotel Reservation System

CheckInn is a **Java-based hotel reservation system** built as part of a course project.
Right now it’s a **console app** (because apparently we need to prove we can type before clicking buttons).
Later it’ll grow into a GUI and database-connected system because that’s what the syllabus demands.



## Project Description

This project manages **rooms, guests, and reservations**.
It’s being built as part of a **class project to practice OOP in Java** and to tick off the syllabus boxes (inheritance, exceptions, Swing, JDBC, the whole circus).
At first, it’s dead simple; add some rooms, throw in a few guests, book a reservation.
Future phases? A little less dead simple, but still manageable.


## Features

### Phase One (current stage)

* [x] Implement core classes: Hotel, Room, Guest, Reservation, Main.
* [x] Add rooms and guests.
* [x] Create reservations with check-in/check-out dates.
* [x] Print details to console (text-only — welcome to 1995).
* [x] Maintain proper folder structure.
* [x] Use `.gitignore` to keep junk out of GitHub.

### Roadmap

#### Phase Two

* [ ] Add custom exceptions for cleaner error handling.
* [ ] Implement searching for rooms by type/availability.
* [ ] Write basic JUnit tests.
* [ ] Introduce a Singleton Hotel instance (design pattern practice).

#### Phase Three

* [ ] Build a Swing GUI interface.
* [ ] Apply MVC pattern for structure.

#### Phase Four

* [ ] Integrate JDBC for database connectivity.
* [ ] Persist room, guest, and reservation data.

#### Phase Five

* [ ] Apply SOLID principles for maintainable code.
* [ ] Generate basic reports.
* [ ] Write user documentation and test cases.



## Project Structure

```
CheckInn/
├── src/com/hotel/        # Source code (Hotel, Room, Guest, Reservation, Main)
├── test/                 # JUnit test classes (future)
├── docs/                 # Docs (proposal, design, notes)
├── build/                # Compiled .class files (ignored by Git)
├── .gitignore            # Ignore the trash
└── README.md             # This file
```

---

## Tech Stack

* Java (JDK 17+ if you’re fancy, lower might work too)
* Git & GitHub (because submitting zip files is a sin)
* JUnit (later)
* Swing (later)
* JDBC + SQL (later, aka: “why is this query failing?!”)


## Getting Started

### Compile

```bash
javac -d build src/com/hotel/*.java
```

### Run

```bash
java -cp build com.hotel.Main
```


## Current Contributor

* Just me.
  (I’m the dev, the manager, the customer, and the poor soul debugging at odd hours.)


## Notes

* This is for my **Java syllabus**.
* Every phase exists only because the professor wants to see “concepts.”
* If it breaks… well, that’s a feature for now.


## Special Thanks

* Shoutout to **stroyoes (Santhan)**.
