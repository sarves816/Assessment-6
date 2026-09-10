package com.ast.ambulance;

import java.time.LocalDateTime;

public class EmergencyRequest {

    public enum Priority {
        CRITICAL(4),
        HIGH(3),
        MODERATE(2),
        NORMAL(1);

        private final int rank;

        Priority(int rank) {
            this.rank = rank;
        }

        public int rank() {
            return rank;
        }
    }

    public enum Status {
        WAITING,
        ASSIGNED,
        EN_ROUTE,
        PATIENT_PICKED_UP,
        HOSPITAL_ARRIVED,
        COMPLETED
    }

    private final String patientId;
    private final String emergencyType;
    private final String pickupLocation;
    private final String destinationHospital;
    private final Priority priority;
    private final double distanceKm;
    private final LocalDateTime createdAt;

    private Status status = Status.WAITING;
    private Ambulance ambulance;
    private long etaMinutes;

    public EmergencyRequest(
            String patientId,
            String emergencyType,
            String pickupLocation,
            String destinationHospital,
            Priority priority,
            double distanceKm) {

        this.patientId = patientId;
        this.emergencyType = emergencyType;
        this.pickupLocation = pickupLocation;
        this.destinationHospital = destinationHospital;
        this.priority = priority;
        this.distanceKm = distanceKm;
        this.createdAt = LocalDateTime.now();
    }

    public String getPatientId() {
        return patientId;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDestinationHospital() {
        return destinationHospital;
    }

    public Priority getPriority() {
        return priority;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public Status getStatus() {
        return status;
    }

    public Ambulance getAmbulance() {
        return ambulance;
    }

    public long getEtaMinutes() {
        return etaMinutes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void assign(Ambulance ambulance, long eta) {
        this.ambulance = ambulance;
        this.etaMinutes = eta;
        this.status = Status.ASSIGNED;
        ambulance.setState(Ambulance.State.DISPATCHED);
    }

    public void setStatus(Status status) {

        this.status = status;

        if (ambulance != null) {

            switch (status) {

                case EN_ROUTE:
                    ambulance.setState(Ambulance.State.EN_ROUTE);
                    break;

                case PATIENT_PICKED_UP:
                    ambulance.setState(Ambulance.State.PATIENT_PICKED_UP);
                    break;

                case HOSPITAL_ARRIVED:
                    ambulance.setState(Ambulance.State.HOSPITAL_ARRIVED);
                    break;

                case COMPLETED:
                    ambulance.setState(Ambulance.State.AVAILABLE);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public String toString() {

        return patientId +
                " | " + priority +
                " | " + emergencyType +
                " | " + status +
                " | ambulance=" +
                (ambulance == null ? "NONE" : ambulance.getId()) +
                " | ETA=" + etaMinutes + " min";
    }
}
