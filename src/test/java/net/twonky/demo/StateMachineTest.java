package net.twonky.demo;

import static net.twonky.demo.StateMachine.Event.START;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import net.twonky.demo.StateMachine.Event;


public class StateMachineTest {
    private StateMachine stateMachine;

    @Before
    public void setUp() {
        stateMachine = new StateMachine();
    }

    @Test
    public void initialStateIsInit() {
        assertEquals(InitState.class, stateMachine.getCurrentState().getClass());
    }

    @Test
    public void nextStateIsRead() {
        State next = stateMachine.next(START);
        assertEquals(ReadState.class, next.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void illegalEventThrowsExeception() {
        stateMachine.next(Event.DATA_RECEIVED);
    }

    @Test
    public void generateGraph() throws IOException {

        StateMachine machine = new StateMachine();

        PrintWriter writer = new PrintWriter("target/statemachine.dot", "UTF-8");

        writer.println("digraph g {");
        for (Pair<State, Event> key : machine.getTransitionTable().keySet()) {
            writer.println(key.getLeft() + " -> " + machine.getTransitionTable().get(key)
                    + " [label=\"" + key.getRight() + "\"];");
        }
        writer.println("}");
        writer.close();

        MutableGraph g = Parser.read(new File("target/statemachine.dot"));
        Graphviz.fromGraph(g).renderToFile(new File("target/statemachine.png"));

    }
}
