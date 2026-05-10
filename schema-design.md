# Schema Design: Smart Clinic Management System

This document outlines the database architecture for the SmartCare Solutions platform, utilizing a hybrid approach (Polyglot Persistence) to optimize for both consistency and flexibility.

---

## 1. MySQL Relational Database Design

The relational database manages core entities where data integrity and strict relationships are critical.

### Table: `users`

Stores authentication credentials and role assignments.

| Column Name | Data Type | Constraints | Description |
| --- | --- | --- | --- |
| `user_id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier. |
| `username` | VARCHAR(50) | UNIQUE, NOT NULL | Login name. |
| `password` | VARCHAR(255) | NOT NULL | Encrypted hash. |
| `role` | ENUM('ADMIN', 'DOCTOR', 'PATIENT') | NOT NULL | RBAC control. |
| `email` | VARCHAR(100) | UNIQUE, NOT NULL | Contact email. |

### Table: `doctors`

Professional details for medical staff.

| Column Name | Data Type | Constraints | Description |
| --- | --- | --- | --- |
| `doctor_id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique doctor ID. |
| `user_id` | BIGINT | FOREIGN KEY, UNIQUE, NOT NULL | Link to `users` table. |
| `specialization` | VARCHAR(100) | NOT NULL | Expertise area. |
| `license_no` | VARCHAR(50) | UNIQUE, NOT NULL | Medical ID. |

### Table: `patients`

Demographic information for individuals.

| Column Name | Data Type | Constraints | Description |
| --- | --- | --- | --- |
| `patient_id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique patient ID. |
| `user_id` | BIGINT | FOREIGN KEY, UNIQUE, NOT NULL | Link to `users` table. |
| `dob` | DATE | NOT NULL | Date of birth. |
| `blood_type` | VARCHAR(5) | - | Medical info. |

### Table: `appointments`

Scheduling logic between doctors and patients.

| Column Name | Data Type | Constraints | Description |
| --- | --- | --- | --- |
| `appointment_id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Record ID. |
| `patient_id` | BIGINT | FOREIGN KEY, NOT NULL | References `patients`. |
| `doctor_id` | BIGINT | FOREIGN KEY, NOT NULL | References `doctors`. |
| `status` | VARCHAR(20) | NOT NULL | 'SCHEDULED', 'COMPLETED', etc. |
| `appt_datetime` | DATETIME | NOT NULL | The scheduled slot. |

---

## 2. MongoDB Collection Design

We use MongoDB for the **Prescriptions** module due to the semi-structured nature of medical advice.

### Collection: `prescriptions`

#### Example Document (JSON):

```json
{
  "_id": "64f1a2b3c4d5e6f7a8b9c0d1",
  "appointmentId": 105,
  "patientId": 45,
  "doctorId": 12,
  "dateIssued": "2023-10-27T10:30:00Z",
  "diagnosis": "Hypertension",
  "medications": [
    {
      "name": "Lisinopril",
      "dosage": "10mg",
      "frequency": "Once daily",
      "instructions": "Take in the morning."
    }
  ],
  "vitals": {
    "blood_pressure": "140/90",
    "pulse": 72
  },
  "notes": "Patient advised to reduce sodium intake."
}

```

---

## Design Justifications

1. **Separation of Concerns:** By linking `doctors` and `patients` to a central `users` table, we centralize security logic while allowing specialized data to exist in specific tables.
2. **Atomic Writes:** Using MySQL for appointments ensures that two patients cannot book the same slot simultaneously (ACID properties).
3. **Flexible Schemas:** MongoDB allows doctors to add varying types of medical metadata (vitals, scans, instructions) without changing the database structure for every new health metric.