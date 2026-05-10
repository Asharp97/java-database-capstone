package com.project.back_end.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @NotNull
  private Doctor doctor;

  @ManyToOne
  @NotNull

  private Patient patient;

  // - The @Future annotation ensures that the appointment time is always in the
  // future when the appointment is created.

  @Future
  private LocalDateTime appointmentTime;

  // - 0 means the appointment is scheduled.
  // - 1 means the appointment has been completed.
  // - The @NotNull annotation ensures that the status field is not null.
  @NotNull
  private int status;

  // 6. 'getEndTime' method:
  // - Type: private LocalDateTime
  // - Description:
  // - This method is a transient field (not persisted in the database).
  // - It calculates the end time of the appointment by adding one hour to the
  // start time (appointmentTime).
  // - It is used to get an estimated appointment end time for display purposes.

  private LocalDateTime getEndTime() {
    return appointmentTime.plusHours(1);
  }

  // 7. 'getAppointmentDate' method:
  // - Type: private LocalDate
  // - Description:
  // - This method extracts only the date part from the appointmentTime field.
  // - It returns a LocalDate object representing just the date (without the time)
  // of the scheduled appointment.
  private LocalDate getAppointmentDate() {
    return appointmentTime.toLocalDate();
  }

  // 8. 'getAppointmentTimeOnly' method:
  // - Type: private LocalTime
  // - Description:
  // - This method extracts only the time part from the appointmentTime field.
  // - It returns a LocalTime object representing just the time (without the date)
  // of the scheduled appointment.
  private LocalTime getAppointmentTimeOnly() {
    return appointmentTime.toLocalTime();
  }

  // 9. Constructor(s):
  // - A no-argument constructor is implicitly provided by JPA for entity
  // creation.
  // - A parameterized constructor can be added as needed to initialize fields.
  public Appointment() {
  }

  // 10. Getters and Setters:
  // - Standard getter and setter methods are provided for accessing and modifying
  // the fields: id, doctor, patient, appointmentTime, status, etc.
  public long getId() {
    return id;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public Patient getPatient() {
    return patient;
  }

  public LocalDateTime getAppointmentTime() {
    return appointmentTime;
  }

  public int getStatus() {
    return status;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setAppointmentTime(LocalDateTime appointmentTime) {
    this.appointmentTime = appointmentTime;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}
