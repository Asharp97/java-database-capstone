package com.project.back_end.controllers;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

  // 1. Set Up the Controller Class:
  // - Annotate the class with `@RestController` to define it as a REST API
  // controller.
  // - Use `@RequestMapping("/appointments")` to set a base path for all
  // appointment-related endpoints.
  // - This centralizes all routes that deal with booking, updating, retrieving,
  // and canceling appointments.

  private final AppointmentService appointmentService;
  private final Service service;

  // 2. Autowire Dependencies:
  // - Inject `AppointmentService` for handling the business logic specific to
  // appointments.
  // - Inject the general `Service` class, which provides shared functionality
  // like token validation and appointment checks.
  @Autowired
  public AppointmentController(AppointmentService appointmentService, Service service) {
    this.appointmentService = appointmentService;
    this.service = service;
  }

  // 3. Define the `getAppointments` Method:
  // - Handles HTTP GET requests to fetch appointments based on date and patient
  // name.
  // - Takes the appointment date, patient name, and token as path variables.
  // - First validates the token for role `"doctor"` using the `Service`.
  // - If the token is valid, returns appointments for the given patient on the
  // specified date.
  // - If the token is invalid or expired, responds with the appropriate message
  // and status code.
  @GetMapping("/{date}/{patientName}/{token}")
  public Map<String, Object> getAppointments(@PathVariable LocalDate date, @PathVariable String patientName,
      @PathVariable String token) {
    if (service.validateToken(token, "doctor"))
      return appointmentService.getAppointment(patientName, date, token);
    return Map.of("message", "Invalid token");
  }

  // 4. Define the `bookAppointment` Method:
  // - Handles HTTP POST requests to create a new appointment.
  // - Accepts a validated `Appointment` object in the request body and a token as
  // a path variable.
  // - Validates the token for the `"patient"` role.
  // - Uses service logic to validate the appointment data (e.g., check for doctor
  // availability and time conflicts).
  // - Returns success if booked, or appropriate error messages if the doctor ID
  // is invalid or the slot is already taken.
  @PostMapping("/{token}")
  public HttpStatus bookAppointment(Appointment appointment, @PathVariable String token) {
    if (service.validateToken(token, "patient"))
      return HttpStatus.UNAUTHORIZED;

    if (service.validateAppointment(appointment) == -1)
      return HttpStatus.BAD_REQUEST;

    if (appointmentService.bookAppointment(appointment) == 0)
      return HttpStatus.INTERNAL_SERVER_ERROR;
    return HttpStatus.OK;
  }

  // 5. Define the `updateAppointment` Method:
  // - Handles HTTP PUT requests to modify an existing appointment.
  // - Accepts a validated `Appointment` object and a token as input.
  // - Validates the token for `"patient"` role.
  // - Delegates the update logic to the `AppointmentService`.
  // - Returns an appropriate success or failure response based on the update
  // result.
  public HttpStatus updateAppointment(Appointment appointment, String token) {
    if (service.validateToken(token, "patient"))
      return HttpStatus.UNAUTHORIZED;
    if (service.validateAppointment(appointment) == -1)
      return HttpStatus.BAD_REQUEST;

    ResponseEntity<Map<String, String>> message = appointmentService.updateAppointment(appointment);
    if (message.getStatusCode() != HttpStatus.OK)
      return HttpStatus.INTERNAL_SERVER_ERROR;

    return HttpStatus.OK;

  }

  // 6. Define the `cancelAppointment` Method:
  // - Handles HTTP DELETE requests to cancel a specific appointment.
  // - Accepts the appointment ID and a token as path variables.
  // - Validates the token for `"patient"` role to ensure the user is authorized
  // to cancel the appointment.
  // - Calls `AppointmentService` to handle the cancellation process and returns
  // the result.
  @DeleteMapping("/{id}/{token}")
  public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable long id, @PathVariable String token) {
    if (service.validateToken(token, "patient"))
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid token"));
    return appointmentService.cancelAppointment(id);
  }
}
