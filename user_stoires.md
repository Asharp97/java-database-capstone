# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

---

## Admin User Stories
### User Story 1: Admin Authentication

**Title:**
*As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely.*

**Acceptance Criteria:**

1. The login page must validate the admin's credentials against the MySQL database.
2. The system must establish a secure session upon successful authentication.
3. An error message must be displayed if the username or password is incorrect.

**Priority:** High
**Story Points:** 3
**Notes:**

* Password should be encrypted using BCrypt before being stored or compared in the database.

---

### User Story 2: Secure Logout

**Title:**
*As an admin, I want to log out of the portal, so that I can protect system access when I am finished working.*

**Acceptance Criteria:**

1. A visible logout button must be available on the AdminDashboard.
2. Clicking logout must invalidate the current session.
3. The user must be redirected back to the login page after logging out.

**Priority:** High
**Story Points:** 1
**Notes:**

* Ensure the browser's "back" button does not allow access to the dashboard after logout.

---

### User Story 3: Add New Doctor

**Title:**
*As an admin, I want to add new doctors to the portal, so that they can begin managing patient appointments.*

**Acceptance Criteria:**

1. The system must provide a form to capture doctor details (name, specialization, email).
2. Data must be saved as a new entry in the MySQL "doctors" table.
3. The system must prevent duplicate entries based on the doctor's email address.

**Priority:** Medium
**Story Points:** 5
**Notes:**

* The creation of a doctor should also trigger the creation of a corresponding user record in the security table.

---

### User Story 4: Delete Doctor Profile

**Title:**
*As an admin, I want to delete a doctor's profile from the portal, so that I can remove staff who are no longer at the clinic.*

**Acceptance Criteria:**

1. The admin must be able to select a doctor from the list and confirm deletion.
2. The doctor's record must be removed from the MySQL database.
3. The system must handle or reassign any existing appointments linked to that doctor before deletion.

**Priority:** Medium
**Story Points:** 3
**Notes:**

* Consider implementing "Soft Delete" (active/inactive status) instead of permanent removal to preserve historical data.

---

### User Story 5: Generate Usage Statistics

**Title:**
*As an admin, I want to run a stored procedure to see the number of appointments per month, so that I can track clinic usage statistics.*

**Acceptance Criteria:**

1. A stored procedure must exist in the MySQL database to aggregate appointments by month.
2. The admin must be able to trigger this procedure via the MySQL CLI or a dedicated admin view.
3. The output must clearly display the month and the corresponding total appointment count.

**Priority:** Low
**Story Points:** 5
**Notes:**

* This provides the foundation for future "Analytics" dashboards in the UI layer.

---
## Doctor User Stories

### User Story 11: Doctor Authentication

**Title:**
*As a doctor, I want to log into the portal, so that I can manage my appointments and view my schedule.*

**Acceptance Criteria:**

1. The system must authenticate the doctor using their unique credentials stored in the MySQL database.
2. Upon successful login, the user must be redirected to the DoctorDashboard.
3. The session must distinguish the "Doctor" role to grant access to medical-specific features.

**Priority:** High
**Story Points:** 2
**Notes:**

* Authentication is handled by the Controller Layer and verified against the User model.

---

### User Story 12: Appointment Calendar View

**Title:**
*As a doctor, I want to view my appointment calendar, so that I can stay organized and manage my daily schedule.*

**Acceptance Criteria:**

1. The system must display a chronological list or calendar view of all confirmed appointments.
2. Each entry must display the patient's name and the specific time slot.
3. The data must be fetched in real-time from the MySQL Repository.

**Priority:** High
**Story Points:** 5
**Notes:**

* Use the Repository Layer to filter appointments by the logged-in Doctor's ID.

---

### User Story 13: Manage Unavailability

**Title:**
*As a doctor, I want to mark my unavailability, so that patients only see and book available time slots.*

**Acceptance Criteria:**

1. The doctor must be able to select specific dates or times to mark as "Unavailable."
2. The Service Layer must validate that no existing appointments are overwritten by this action.
3. The marked unavailability must immediately reflect in the public-facing booking view for patients.

**Priority:** Medium
**Story Points:** 5
**Notes:**

* This logic resides in the Service Layer to coordinate between the doctor's schedule and the patient's booking options.

---

### User Story 14: Profile Management

**Title:**
*As a doctor, I want to update my profile with my specialization and contact info, so that patients have up-to-date information.*

**Acceptance Criteria:**

1. The doctor must be able to edit their specialization and contact details via the DoctorDashboard.
2. Changes must be persisted in the MySQL database and reflected on the public doctor list.
3. The system must validate that contact information (e.g., phone/email) follows the correct format.

**Priority:** Medium
**Story Points:** 3
**Notes:**

* This updates the core Doctor model bound to the MySQL "doctors" table.

---

### User Story 15: Access Patient Details

**Title:**
*As a doctor, I want to view the patient details for upcoming appointments, so that I can be prepared for the consultation.*

**Acceptance Criteria:**

1. Clicking an appointment in the calendar must reveal the patient's name, age, and basic contact info.
2. The system must ensure the doctor can only view details for patients they are scheduled to see.
3. If applicable, any nested prescription history from MongoDB should be accessible in a read-only format.

**Priority:** High
**Story Points:** 5
**Notes:**

* This involves model binding from both MySQL (patient info) and MongoDB (historical records).

---
## Patient User Stories
### User Story 6: Public Doctor Directory

**Title:**
*As a patient, I want to view a list of doctors without logging in, so that I can explore my options before registering.*

**Acceptance Criteria:**

1. The system must display a publicly accessible page listing all registered doctors.
2. The list must show the doctor's name, specialization, and department.
3. No sensitive data or booking buttons should be accessible until the user is authenticated.

**Priority:** Medium
**Story Points:** 3
**Notes:**

* This uses the Thymeleaf-based web dashboard accessible to anonymous users.

---

### User Story 7: Patient Registration

**Title:**
*As a patient, I want to sign up using my email and password, so that I can book appointments.*

**Acceptance Criteria:**

1. The registration form must collect the user's name, email, and password.
2. The system must validate that the email is in a valid format and not already registered.
3. Upon successful registration, a new patient record must be created in the MySQL database.

**Priority:** High
**Story Points:** 5
**Notes:**

* Password hashing must be implemented immediately upon account creation.

---

### User Story 8: Patient Authentication

**Title:**
*As a patient, I want to log into the portal, so that I can manage my bookings and access my records.*

**Acceptance Criteria:**

1. The login page must authenticate the patient against the MySQL "users" table.
2. Successful login must redirect the patient to the PatientDashboard.
3. A "Forgot Password" link should be visible on the login interface.

**Priority:** High
**Story Points:** 2
**Notes:**

* Ensure the session timeout is appropriately configured for security.

---

### User Story 9: Appointment Booking

**Title:**
*As a patient, I want to book an hour-long appointment, so that I can consult with a specific doctor.*

**Acceptance Criteria:**

1. The patient must be able to select a doctor and a preferred time slot.
2. The system must check the Service Layer to ensure the doctor is available at the chosen time.
3. Each appointment must default to a 60-minute duration in the MySQL "appointments" table.

**Priority:** High
**Story Points:** 8
**Notes:**

* Logic should prevent overlapping appointments for the same doctor.

---

### User Story 10: View Upcoming Appointments

**Title:**
*As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.*

**Acceptance Criteria:**

1. The PatientDashboard must display a list of all scheduled appointments.
2. The list must show the date, time, and doctor’s name for each entry.
3. Appointments must be sorted chronologically, starting with the soonest date.

**Priority:** Medium
**Story Points:** 3
**Notes:**

* This data is fetched via the MySQL Repository and bound to the PatientDashboard model.
---