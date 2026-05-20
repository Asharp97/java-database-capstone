// appointmentRecordService.js
import { API_BASE_URL } from "../config/config.js";
const APPOINTMENT_API = `${API_BASE_URL}/appointments`;

//This is for the doctor to get all the patient Appointments
export async function getAllAppointments(date, patientName, token) {
  // Use "all" as placeholder if patientName is not provided or is empty
  const name = patientName && patientName.trim() !== "" ? patientName : "all";
  const url = `${APPOINTMENT_API}/${date}/${name}/${token}`;

  console.log("🔵 Fetching appointments from:", url);
  console.log(
    "📋 Parameters - Date:",
    date,
    "Name:",
    name,
    "Token:",
    token?.substring(0, 20) + "...",
  );

  try {
    const response = await fetch(url);
    console.log("📊 Response status:", response.status);

    if (!response.ok) {
      console.error("❌ API Error:", response.status, response.statusText);
      throw new Error(`Failed to fetch appointments (${response.status})`);
    }

    const data = await response.json();
    console.log("✅ API Response:", data);
    return data;
  } catch (error) {
    console.error("🚨 Error fetching appointments:", error);
    throw error;
  }
}

export async function bookAppointment(appointment, token) {
  try {
    const response = await fetch(`${APPOINTMENT_API}/${token}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(appointment),
    });

    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Something went wrong",
    };
  } catch (error) {
    console.error("Error while booking appointment:", error);
    return {
      success: false,
      message: "Network error. Please try again later.",
    };
  }
}

export async function updateAppointment(appointment, token) {
  try {
    const response = await fetch(`${APPOINTMENT_API}/${token}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(appointment),
    });

    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Something went wrong",
    };
  } catch (error) {
    console.error("Error while booking appointment:", error);
    return {
      success: false,
      message: "Network error. Please try again later.",
    };
  }
}
