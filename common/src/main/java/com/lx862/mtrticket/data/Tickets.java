package com.lx862.mtrticket.data;

public record Tickets(String name, int predicateID, boolean singleRide, int price, int expireDays, int delayMin, int delayMax, boolean opOnly, boolean exitOnly, String filteredZone) {
}
