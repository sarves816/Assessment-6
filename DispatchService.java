package com.ast.ambulance;

import java.util.*;

public class DispatchService {

    private final List<Ambulance> ambulances =
            new ArrayList<>();

    private final List<EmergencyRequest> history =
            new ArrayList<>();

    private final PriorityQueue<EmergencyRequest> waiting =
            new PriorityQueue<>(
                    Comparator
                            .comparingInt(
                                    (EmergencyRequest r) ->
                                            r.getPriority().rank())
                            .reversed()
                            .thenComparing(
                                    EmergencyRequest::getCreatedAt)
            );

    public void addAmbulance(Ambulance ambulance) {

        if (ambulance == null) {
            throw new IllegalArgumentException(
                    "Ambulance cannot be null");
        }

        ambulances.add(ambulance);
        dispatchWaiting();
    }

    public EmergencyRequest createRequest(
            String patientId,
            String emergencyType,
            String pickup,
            String hospital,
            EmergencyRequest.Priority priority,
            double distance)
            throws InvalidRequestException {

        if (patientId == null || patientId.isBlank()
                || emergencyType == null
                || emergencyType.isBlank()
                || pickup == null
                || pickup.isBlank()
                || hospital == null
                || hospital.isBlank()
                || priority == null
                || distance <= 0) {

            throw new InvalidRequestException(
                    "Invalid emergency request details");
        }

        EmergencyRequest request =
                new EmergencyRequest(
                        patientId,
                        emergencyType,
                        pickup,
                        hospital,
                        priority,
                        distance);

        history.add(request);
        waiting.offer(request);

        dispatchWaiting();

        return request;
    }

    private void dispatchWaiting() {

        while (!waiting.isEmpty()) {

            EmergencyRequest request =
                    waiting.peek();

            Ambulance ambulance =
                    findBest(request);

            if (ambulance == null) {
                break;
            }

            waiting.poll();

            request.assign(
                    ambulance,
                    estimate(request, ambulance));
        }
    }

    private Ambulance findBest(
            EmergencyRequest request) {

        return ambulances.stream()
                .filter(a ->
                        a.getState() ==
                                Ambulance.State.AVAILABLE)
                .sorted(
                        Comparator
                                .comparingInt(
                                        (Ambulance a) ->
                                                typeScore(a, request))
                                .reversed()
                                .thenComparingDouble(
                                        a -> request.getDistanceKm())
                )
                .findFirst()
                .orElse(null);
    }

    private int typeScore(
            Ambulance ambulance,
            EmergencyRequest request) {

        return switch (request.getPriority()) {

            case CRITICAL ->
                    ambulance.getType() ==
                            Ambulance.Type.ICU ? 3 :
                    ambulance.getType() ==
                            Ambulance.Type.ADVANCED_LIFE_SUPPORT
                            ? 2 : 1;

            case HIGH ->
                    ambulance.getType() ==
                            Ambulance.Type.ADVANCED_LIFE_SUPPORT
                            ? 3 :
                    ambulance.getType() ==
                            Ambulance.Type.ICU ? 2 : 1;

            default -> 1;
        };
    }

    private long estimate(
            EmergencyRequest request,
            Ambulance ambulance) {

        double speed;

        if (ambulance.getType() ==
                Ambulance.Type.ICU) {

            speed = 45;

        } else if (ambulance.getType() ==
                Ambulance.Type.ADVANCED_LIFE_SUPPORT) {

            speed = 50;

        } else {

            speed = 55;
        }

        return Math.max(
                1,
                Math.round(
                        request.getDistanceKm()
                                / speed * 60));
    }

    public void updateStatus(
            String patientId,
            EmergencyRequest.Status status)
            throws InvalidRequestException {

        EmergencyRequest request =
                history.stream()
                        .filter(r ->
                                r.getPatientId()
                                        .equals(patientId))
                        .findFirst()
                        .orElseThrow(() ->
                                new InvalidRequestException(
                                        "Patient/request not found"));

        if (request.getAmbulance() == null) {

            throw new InvalidRequestException(
                    "No ambulance assigned");
        }

        request.setStatus(status);

        dispatchWaiting();
    }

    public List<EmergencyRequest> getHistory() {

        return Collections.unmodifiableList(history);
    }

    public List<Ambulance> getAmbulances() {

        return Collections.unmodifiableList(ambulances);
    }

    public int getWaitingCount() {

        return waiting.size();
    }
}
