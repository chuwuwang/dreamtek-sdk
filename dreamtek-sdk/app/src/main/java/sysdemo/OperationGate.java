package sysdemo;

/** One terminal observation wins; callbacks cannot revive timed-out or abandoned operations. */
public final class OperationGate {
    private boolean terminal;
    private boolean closed;

    public synchronized boolean complete() {
        if (terminal || closed) return false;
        terminal = true;
        return true;
    }

    public synchronized void close() { closed = true; }
    public synchronized boolean isPending() { return !terminal && !closed; }
}
