package com.ast.ambulance;

public class Ambulance {

    public enum Type {
        BASIC,
        ADVANCED_LIFE_SUPPORT,
        ICU
    }

    public enum State {
        AVAILABLE,
        DISPATCHED,
        EN_ROUTE,
        PATIENT_PICKED_UP,
        HOSPITAL_ARRIVED
    }

    private final String id;
    private final String driverName;
    private final String driverPhone;
    private final Type type;
    private State state = State.AVAILABLE;

    public Ambulance(String id, Type type, String driverName, String driverPhone) {
        this.id = id;
        this.type = type;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public State getState() {
        return state;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setState(State state) {
        this.state = state;
    }
}
