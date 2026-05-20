// patientRecordRow.js
export function createDoctorAppointmentRow(appointment) {
  const tr = document.createElement("tr");
  const patient = appointment.patient || {};

  tr.innerHTML = `
      <td class="patient-id">${patient.id ?? ""}</td>
      <td>${patient.name ?? ""}</td>
      <td>${patient.phone ?? ""}</td>
      <td>${patient.email ?? ""}</td>
      <td><img src="../assets/images/addPrescriptionIcon/addPrescription.png" alt="addPrescriptionIcon" class="prescription-btn" data-id="${appointment.id}"></img></td>
    `;

  const button = tr.querySelector(".prescription-btn");
  if (button) {
    button.addEventListener("click", () => {
      window.location.href = `/pages/addPrescription.html?mode=view&appointmentId=${appointment.id}`;
    });
  }

  return tr;
}

export function createPatientRecordRow(appointment) {
  const tr = document.createElement("tr");
  const patientId = appointment.patientId ?? appointment.patient?.id ?? "";
  const appointmentDate =
    appointment.appointmentDate ||
    (appointment.appointmentTime
      ? new Date(appointment.appointmentTime).toLocaleDateString()
      : "");

  tr.innerHTML = `
      <td class="patient-id">${appointmentDate}</td>
      <td>${appointment.id ?? ""}</td>
      <td>${patientId}</td>
      <td><img src="../assets/images/addPrescriptionIcon/addPrescription.png" alt="addPrescriptionIcon" class="prescription-btn" data-id="${appointment.id}"></img></td>
    `;

  const button = tr.querySelector(".prescription-btn");
  if (button) {
    button.addEventListener("click", () => {
      window.location.href = `/pages/addPrescription.html?mode=view&appointmentId=${appointment.id}`;
    });
  }

  return tr;
}
