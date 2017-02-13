package net.twonky.demo;

public abstract class State {
    abstract public String getName();

    public String toString() {
        return getName();
    }
}
