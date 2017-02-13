package net.twonky.demo;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;

public class StateMachine {

    public static enum Event {
        START, DATA_RECEIVED, EOF_RECEIVED, ERROR_RECEIVED
    }

    private ImmutableMap<Pair<State, Event>, State> transitionTable;

    private State currentState;

    public StateMachine() {
        final State init = new InitState();
        final State read = new ReadState();
        final State done = new DoneState();

        // @formatter:off
        transitionTable = ImmutableMap.of(
                Pair.of(init, Event.START),          read,
                Pair.of(read, Event.DATA_RECEIVED),  read,
                Pair.of(read, Event.EOF_RECEIVED),   done,
                Pair.of(read, Event.ERROR_RECEIVED), done);
        // @formatter:on

        currentState = init;
    }

    public State getCurrentState() {
        return currentState;
    }

    public ImmutableMap<Pair<State, Event>, State> getTransitionTable() {
        return transitionTable;
    }

    public State next(Event action) {
        Pair<State, Event> transitionKey = new ImmutablePair<State, Event>(
                currentState, action);
        if (!transitionTable.containsKey(transitionKey)) {
            throw new IllegalStateException("Illegal state/action combination");
        } else {
            return transitionTable.get(transitionKey);
        }
    }
}
