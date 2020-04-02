package de.netempire.classes;

public class Fork {

    boolean taken;

    void put() {
        // Fork is placed back on the table. -> status: not taken
        taken = false;
    }

    void get() throws InterruptedException {
        // Fork is taken from the table. -> status: taken
        taken = true;
    }
}