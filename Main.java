package com.ast.ambulance;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        DispatchService service =
                new DispatchService();

        service.addAmbulance(
                new Ambulance(
                        "AMB101",
                        Ambulance.Type.BASIC,
                        "Arun",
                        "9000000001"));

        service.addAmbulance(
                new Ambulance(
                        "AMB102",
                        Ambulance.Type.ADVANCED_LIFE_SUPPORT,
                        "Bala",
                        "9000000002"));

        service.addAmbulance(
                new Ambulance(
                        "AMB103",
                        Ambulance.Type.ICU,
                        "Charan",
                        "9000000003"));

        while (true) {

            System.out.println(
                    "\n===== AMBULANCE DISPATCH SYSTEM =====");

            System.out.println("1. Add Emergency");
            System.out.println("2. Update Status");
            System.out.println("3. View History");
            System.out.println("4. View Ambulances");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");

            String choice = sc.nextLine();

            try {

                if (choice.equals("1")) {

                    System.out.print("Patient ID: ");
                    String patientId = sc.nextLine();

                    System.out.print("Emergency Type: ");
                    String emergencyType = sc.nextLine();

                    System.out.print("Pickup Location: ");
                    String pickup = sc.nextLine();

                    System.out.print("Destination Hospital: ");
                    String hospital = sc.nextLine();

                    System.out.print(
                            "Priority (CRITICAL/HIGH/MODERATE/NORMAL): ");

                    EmergencyRequest.Priority priority =
                            EmergencyRequest.Priority.valueOf(
                                    sc.nextLine().toUpperCase());

                    System.out.print("Distance in km: ");

                    double distance =
                            Double.parseDouble(sc.nextLine());

                    EmergencyRequest request =
                            service.createRequest(
                                    patientId,
                                    emergencyType,
                                    pickup,
                                    hospital,
                                    priority,
                                    distance);

                    System.out.println("\nRequest Created:");
                    System.out.println(request);

                } else if (choice.equals("2")) {

                    System.out.print("Patient ID: ");
                    String patientId = sc.nextLine();

                    System.out.print(
                            "Status (EN_ROUTE/PATIENT_PICKED_UP/" +
                            "HOSPITAL_ARRIVED/COMPLETED): ");

                    EmergencyRequest.Status status =
                            EmergencyRequest.Status.valueOf(
                                    sc.nextLine().toUpperCase());

                    service.updateStatus(
                            patientId,
                            status);

                    System.out.println(
                            "Status updated successfully.");

                } else if (choice.equals("3")) {

                    System.out.println(
                            "\n===== EMERGENCY HISTORY =====");

                    for (EmergencyRequest r :
                            service.getHistory()) {

                        System.out.println(r);
                    }

                    System.out.println(
                            "Waiting requests: " +
                                    service.getWaitingCount());

                } else if (choice.equals("4")) {

                    System.out.println(
                            "\n===== AMBULANCES =====");

                    for (Ambulance a :
                            service.getAmbulances()) {

                        System.out.println(
                                a.getId() +
                                " | " +
                                a.getType() +
                                " | " +
                                a.getState() +
                                " | Driver: " +
                                a.getDriverName() +
                                " | Phone: " +
                                a.getDriverPhone());
                    }

                } else if (choice.equals("5")) {

                    System.out.println("Program terminated.");
                    break;

                } else {

                    System.out.println(
                            "Invalid option.");
                }

            } catch (Exception e) {

                System.out.println(
                        "ERROR: " + e.getMessage());
            }
        }

        sc.close();
    }
}
