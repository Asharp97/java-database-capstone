package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class AppointmentService {
  // 1. **Add @Service Annotation**:
  // - To indicate that this class is a service layer class for handling business
  // logic.
  // - The `@Service` annotation should be added before the class declaration to
  // mark it as a Spring service component.
  // - Instruction: Add `@Service` above the class definition.
  private final AppointmentRepository appointmentRepository;
  private final DoctorRepository doctorRepository;
  private final PatientRepository patientRepository;
  private final TokenService tokenService;

  // 2. **Constructor Injection for Dependencies**:
  // - The `AppointmentService` class requires several dependencies like
  // `AppointmentRepository`, `Service`, `TokenService`, `PatientRepository`, and
  // `DoctorRepository`.
  // - These dependencies should be injected through the constructor.
  // - Instruction: Ensure constructor injection is used for proper dependency
  // management in Spring.
  public AppointmentService(AppointmentRepository appointmentRepository, TokenService tokenService,
      PatientRepository patientRepository, DoctorRepository doctorRepository) {
    this.appointmentRepository = appointmentRepository;
    this.tokenService = tokenService;
    this.patientRepository = patientRepository;
    this.doctorRepository = doctorRepository;
  }

  // 3. **Add @Transactional Annotation for Methods that Modify Database**:
  // - The methods that modify or update the database should be annotated with
  // `@Transactional` to ensure atomicity and consistency of the operations.
  // - Instruction: Add the `@Transactional` annotation above methods that
  // interact with the database, especially those modifying data.

  // 4. **Book Appointment Method**:
  // - Responsible for saving the new appointment to the database.
  // - If the save operation fails, it returns `0`; otherwise, it returns `1`.
  // - Instruction: Ensure that the method handles any exceptions and returns an
  // appropriate result code.
  public int bookAppointment(Appointment appointment) {
    try {
      appointmentRepository.save(appointment);
      return 1;
    } catch (Exception e) {
      return 0;
    }
  }

  // 5. **Update Appointment Method**:
  // - This method is used to update an existing appointment based on its ID.
  // - It validates whether the patient ID matches, checks if the appointment is
  // available for updating, and ensures that the doctor is available at the
  // specified time.
  // - If the update is successful, it saves the appointment; otherwise, it
  // returns an appropriate error message.
  // - Instruction: Ensure proper validation and error handling is included for
  // appointment updates.
  @Transactional
  public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
    Optional<Appointment> existingAppointment = appointmentRepository.findById(appointment.getId());
    if (existingAppointment.isEmpty()) {
      return ResponseEntity.status(404).body(Map.of("message", "Appointment not found"));
    }

    LocalDateTime newTime = appointment.getAppointmentTime();
    Long doctorId = appointment.getDoctor().getId();

    // Check for time conflict
    List<Appointment> conflicts = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
        doctorId,
        newTime.minusMinutes(59),
        newTime.plusMinutes(59));

    if (!conflicts.isEmpty())
      return ResponseEntity.status(409).body(Map.of("message", "Time conflict"));

    // Check if the doctor is available
    if (!doctorRepository.existsById(doctorId))
      return ResponseEntity.status(404).body(Map.of("message", "Doctor not found"));

    appointmentRepository.save(appointment);
    return ResponseEntity.ok(Map.of("message", "Appointment updated successfully"));

  }

  // 6. **Cancel Appointment Method**:
  // - This method cancels an appointment by deleting it from the database.
  // - It ensures the patient who owns the appointment is trying to cancel it and
  // handles possible errors.
  // - Instruction: Make sure that the method checks for the patient ID match
  // before deleting the appointment.
  @Transactional
  public ResponseEntity<Map<String, String>> cancelAppointment(long id) {
    try {
      if (appointmentRepository.existsById(id)) {
        appointmentRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Appointment canceled successfully"));
      }
      return ResponseEntity.status(404).body(Map.of("message", "Appointment not found"));

    } catch (Exception e) {
      return ResponseEntity.status(404).body(Map.of("message", "Appointment not found"));
    }
  }

  // 7. **Get Appointments Method**:
  // - This method retrieves a list of appointments for a specific doctor on a
  // particular day, optionally filtered by the patient's name.
  // - It uses `@Transactional` to ensure that database operations are consistent
  // and handled in a single transaction.
  // - Instruction: Ensure the correct use of transaction boundaries, especially
  // when querying the database for appointments.
  public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {

    // 1. Get the doctor ID from the token
    String email = tokenService.extractEmail(token);
    Doctor doctor = doctorRepository.findByEmail(email);
    Long doctorId = doctor.getId();

    // 2. Build the date range for the query
    LocalDateTime start = date.atStartOfDay();
    LocalDateTime end = date.atTime(LocalTime.MAX);

    // 3. Fetch appointments
    List<Appointment> appointments = appointmentRepository
        .findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

    // 4. Filter by patient name if provided (ignore if "all" or empty/blank)
    if (pname != null && !pname.isBlank() && !pname.equalsIgnoreCase("all")) {
      appointments = appointments.stream()
          .filter(a -> a.getPatient().getName().equalsIgnoreCase(pname))
          .collect(Collectors.toList());
    }

    // 5. Return as a map
    Map<String, Object> result = new HashMap<>();
    result.put("appointments", appointments);
    return result;

  }

}
// 8. **Change Status Method**:
// - This method updates the status of an appointment by changing its value in
// the database.
// - It should be annotated with `@Transactional` to ensure the operation is
// executed in a single transaction.
// - Instruction: Add `@Transactional` before this method to ensure atomicity
// when updating appointment status.
