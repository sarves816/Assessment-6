package com.ast.ambulance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DispatchServiceTest {

    @Test
    void criticalGetsAmbulance()
            throws Exception {

        DispatchService service =
                new DispatchService();

        service.addAmbulance(
                new Ambulance(
                        "A1",
                        Ambulance.Type.ICU,
                        "Driver",
                        "123"));

        EmergencyRequest request =
                service.createRequest(
                        "P1",
                        "Cardiac",
                        "Location A",
                        "Hospital A",
                        EmergencyRequest.Priority.CRITICAL,
                        10);

        assertEquals(
                "A1",
                request.getAmbulance().getId());
    }

    @Test
    void waitingWhenUnavailable()
            throws Exception {

        DispatchService service =
                new DispatchService();

        service.addAmbulance(
                new Ambulance(
                        "A1",
                        Ambulance.Type.BASIC,
                        "Driver",
                        "123"));

        service.createRequest(
                "P1",
                "Emergency",
                "A",
                "H",
                EmergencyRequest.Priority.HIGH,
                5);

        EmergencyRequest request =
                service.createRequest(
                        "P2",
                        "Emergency",
                        "B",
                        "H",
                        EmergencyRequest.Priority.CRITICAL,
                        5);

        assertNull(request.getAmbulance());

        assertEquals(
                1,
                service.getWaitingCount());
    }

    @Test
    void invalidRequest() {

        DispatchService service =
                new DispatchService();

        assertThrows(
                InvalidRequestException.class,
                () -> service.createRequest(
                        "",
                        "Emergency",
                        "A",
                        "H",
                        EmergencyRequest.Priority.NORMAL,
                        5));
    }

    @Test
    void ambulanceReleased()
            throws Exception {

        DispatchService service =
                new DispatchService();

        service.addAmbulance(
                new Ambulance(
                        "A1",
                        Ambulance.Type.ADVANCED_LIFE_SUPPORT,
                        "Driver",
                        "123"));

        service.createRequest(
                "P1",
                "Emergency",
                "A",
                "H",
                EmergencyRequest.Priority.HIGH,
                5);

        service.updateStatus(
                "P1",
                EmergencyRequest.Status.COMPLETED);

        assertEquals(
                Ambulance.State.AVAILABLE,
                service.getAmbulances()
                        .get(0)
                        .getState());
    }
}
