# ClinicFlow

ClinicFlow is a Java desktop application for managing a small clinic's day-to-day operations — patients, doctors, and appointments — through a JavaFX interface backed by a MySQL database. It provides double-booking prevention, patient data validation, and appointment lifecycle tracking (pending, completed, cancelled) in a single, easy-to-run desktop application.

---

## Overview

ClinicFlow gives front-desk and clinical staff a simple, form-driven interface to register patients, manage doctors, and schedule appointments without needing to touch SQL directly. Core business rules, such as preventing a doctor from being double-booked and rejecting invalid patient records, are enforced in the application layer through dedicated custom exceptions, keeping the database and UI consistent.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| UI Framework | JavaFX 21 (FXML views + CSS styling) |
| Database | MySQL (via MariaDB-compatible dump) |
| DB Driver | MySQL Connector/J 8.4.0 |
| Build Tool | Maven (with Maven Wrapper) |
| Testing | JUnit 5 (Jupiter) |
| Packaging | `javafx-maven-plugin` (cross-platform `win`/`mac`/`linux` profiles) |

---

## Project Structure

```
ClinicFlow/
├── database/
│   └── clinicflow.sql                     # MySQL schema (patients, doctors, appointments)
├── src/
│   ├── main/
│   │   ├── java/com/example/clinicflow/
│   │   │   ├── Applications/
│   │   │   │   ├── Main.java              # JavaFX Application entry point (loads MainView.fxml)
│   │   │   │   └── AppLauncher.java       # Plain-Java launcher (for packaged/non-modular runs)
│   │   │   ├── Classes/
│   │   │   │   ├── Person.java            # Abstract base class for Patient/Doctor
│   │   │   │   ├── Patient.java
│   │   │   │   ├── PatientDAO.java
│   │   │   │   ├── Doctor.java
│   │   │   │   ├── DoctorDAO.java
│   │   │   │   ├── Appointment.java
│   │   │   │   ├── AppointmentDAO.java
│   │   │   │   ├── AppointmentStatus.java # Enum: PENDING, COMPLETED, CANCELLED
│   │   │   │   ├── DBConnection.java      # Loads db.properties & manages the JDBC connection
│   │   │   │   ├── DoubleBookingException.java
│   │   │   │   ├── InvalidPatientException.java
│   │   │   │   └── Validator.java
│   │   │   └── Controllers/
│   │   │       ├── MainController.java
│   │   │       ├── DashboardController.java
│   │   │       ├── PatientListController.java
│   │   │       ├── PatientFormController.java
│   │   │       ├── DoctorListController.java
│   │   │       ├── DoctorFormController.java
│   │   │       ├── AppointmentListController.java
│   │   │       ├── AppointmentFormController.java
│   │   │       ├── RescheduleController.java
│   │   │       └── AlertUtils.java
│   │   └── resources/
│   │       ├── db.properties.example      # Template for local DB credentials
│   │       └── com/example/clinicflow/view/
│   │           ├── MainView.fxml
│   │           ├── DashboardView.fxml
│   │           ├── PatientListView.fxml
│   │           ├── PatientFormDialog.fxml
│   │           ├── DoctorListView.fxml
│   │           ├── DoctorFormDialog.fxml
│   │           ├── AppointmentListView.fxml
│   │           ├── AppointmentFormDialog.fxml
│   │           ├── RescheduleDialog.fxml
│   │           └── styles.css
│   └── test/java/com/example/clinicflow/Classes/
│       ├── AppointmentTest.java
│       ├── PersonTest.java
│       └── ValidatorTest.java
├── .mvn/wrapper/                          # Maven Wrapper (mvnw / mvnw.cmd)
├── pom.xml
└── .gitignore
```

| Layer | Responsibility |
|---|---|
| `Applications/` | JavaFX application bootstrap; loads `MainView.fxml` and starts the primary stage. |
| `Classes/` | Domain model, data-access objects (DAOs), enums, validation, and custom exceptions — the core business logic. |
| `Controllers/` | FXML controllers wiring UI events to the DAO/domain layer for each screen and dialog. |
| `resources/view/` | FXML layouts and stylesheet defining the application's screens. |
| `database/` | SQL dump used to provision the MySQL schema. |
| `src/test/` | JUnit 5 unit tests for core domain classes. |

---

## Features

- Register, view, update, and manage patients
- Manage doctors, including specialty and contact details
- Schedule, list, and reschedule appointments with a dedicated dialog
- Double-booking prevention, enforced via `DoubleBookingException` before an appointment is saved
- Patient data validation, enforced via `Validator` and `InvalidPatientException`
- Appointment status tracking: `PENDING`, `COMPLETED`, `CANCELLED`
- Multi-screen JavaFX dashboard with dedicated list and form views for patients, doctors, and appointments
- Unit-tested core domain logic (appointments, person model, validation)

---

## Database Schema

The MySQL database (`clinicflow`) is composed of three related tables:

| Table | Description |
|---|---|
| `patients` | Patient records — ID, name, age, phone, and address. |
| `doctors` | Doctor records — ID, name, phone, and specialty. |
| `appointments` | Appointments linking a patient and a doctor, with date/time, type, and status. |

**Key relationships:**

```
patients   ──1:*──► appointments
doctors    ──1:*──► appointments
```

`appointments.patient_id` and `appointments.doctor_id` are foreign keys referencing `patients.patient_id` and `doctors.doctor_id` respectively, and `appointments.status` is constrained to `PENDING`, `COMPLETED`, or `CANCELLED`.

The full schema is defined in [`database/clinicflow.sql`](./database/clinicflow.sql).

---

## Prerequisites

- Java Development Kit (JDK) 21
- Maven (or use the included Maven Wrapper — no local install required)
- MySQL Server (or a MariaDB-compatible equivalent)

> The project pulls JavaFX and JUnit dependencies automatically via Maven — no separate JavaFX SDK download is required.

---

## Setup & Installation

### 1. Clone the repository

```bash
git clone https://github.com/anuja-jayaweera/ClinicFlow.git
cd ClinicFlow
```

### 2. Set up the database

Create the database and import the provided schema:

```bash
mysql -u your_username -p -e "CREATE DATABASE clinicflow;"
mysql -u your_username -p clinicflow < database/clinicflow.sql
```

### 3. Configure the database connection

Copy the example properties file and fill in your local credentials:

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Edit `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/clinicflow?useSSL=false&serverTimezone=UTC
db.user=root
db.password=your_password_here
```

> **Never commit `db.properties` to version control.** It is already covered by `.gitignore`; keep it that way.

### 4. Build and run the application

Using the Maven Wrapper (recommended, no local Maven install needed):

```bash
# macOS/Linux
./mvnw javafx:run

# Windows
mvnw.cmd javafx:run
```

Or, with a local Maven installation:

```bash
mvn javafx:run
```

---

## Configuration

| Property | Description | Example |
|---|---|---|
| `db.url` | JDBC connection string, including host, port, database name, and options | `jdbc:mysql://localhost:3306/clinicflow?useSSL=false&serverTimezone=UTC` |
| `db.user` | MySQL username | `root` |
| `db.password` | MySQL password | `••••••••` |

`DBConnection.java` loads these values from `db.properties` on the classpath at startup and will throw a runtime error with setup instructions if the file is missing.

---

## Usage

1. Launch the application (`javafx:run` as above). The Main Dashboard opens by default.
2. From the dashboard, navigate to Patients, Doctors, or Appointments.
3. **Patients / Doctors:** add, edit, or view records through the corresponding list and form views.
4. **Appointments:**
   - Create a new appointment by selecting a patient, doctor, date/time, and appointment type.
   - The system checks for scheduling conflicts and blocks the save with a `DoubleBookingException` message if the doctor is already booked at that time.
   - Reschedule an existing appointment via the dedicated Reschedule dialog.
   - Update an appointment's status as it moves through `PENDING` → `COMPLETED` / `CANCELLED`.
5. Invalid patient input (e.g., missing required fields) is rejected with a validation error before it reaches the database.

---

## Testing

Unit tests cover core domain logic and run via Maven:

```bash
./mvnw test
```

Current test coverage includes:

- `AppointmentTest` — appointment behavior and status handling
- `PersonTest` — shared `Person` base-class behavior
- `ValidatorTest` — input validation rules

---

## Known Limitations

- No user authentication or role-based access control — anyone with access to the app has full access to all records.
- No audit logging of who created, edited, or cancelled records.
- Single hard-coded JDBC connection (no connection pooling), which may not scale well for concurrent multi-user use.
- No automated schema migration tooling — schema changes must be applied manually via the SQL dump.

---

## Roadmap

- [ ] User authentication and role-based access (front desk vs. doctor vs. admin)
- [ ] Audit logging for record creation, edits, and cancellations
- [ ] Appointment search/filtering (by patient, doctor, date range, or status)
- [ ] Export patient/appointment reports (PDF/CSV)
- [ ] Connection pooling for improved concurrency
- [ ] Packaged installers via `javafx-maven-plugin` jlink/jpackage targets

---

## Contributing

Contributions are welcome. Please open an issue to discuss proposed changes before submitting a pull request, especially for anything touching the database schema or appointment-scheduling logic.

---

## Author

**Anuja Jayaweera**
