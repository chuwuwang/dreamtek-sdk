package automation;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;

import com.dreamtek.smartpos.deviceservice.aidl.IMagCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.MagCardListener;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/** Executes and evaluates one legacy Magcard case without exposing card data. */
public final class MagCardAutomationSession {
    private static final long SECOND_SEARCH_DELAY_MS = 5_000L;
    private static final long LONG_SEARCH_ACTION_DELAY_MS = 100_000L;
    private static final long STOP_DELAY_MS = 10_000L;
    private static final long POST_STOP_OBSERVATION_MS = 12_000L;

    private final IMagCardReader reader;
    private final String caseName;
    private final long startedAt = SystemClock.elapsedRealtime();
    private final CountDownLatch finished = new CountDownLatch(1);
    private final CountDownLatch userActionReady = new CountDownLatch(1);
    private final AtomicReference<AutomaticCaseResult> result = new AtomicReference<>();
    private final AtomicBoolean searchActive = new AtomicBoolean();
    private volatile boolean resetTrackMask;

    private MagCardAutomationSession(IMagCardReader reader, String caseName) {
        this.reader = reader;
        this.caseName = caseName;
    }

    public static MagCardAutomationSession start(IMagCardReader reader, String caseName) {
        MagCardAutomationSession session = new MagCardAutomationSession(reader, caseName);
        if (reader == null) {
            session.complete(AutomaticCaseResult.error("getMagCardReader returned null"));
            return session;
        }
        try {
            session.startCase();
        } catch (Throwable error) {
            session.complete(session.interfaceError("starting the case", error));
        }
        return session;
    }

    public AutomaticCaseResult await(long timeoutMs) {
        try {
            if (!finished.await(timeoutMs, TimeUnit.MILLISECONDS)) {
                cancel();
                return AutomaticCaseResult.error("Timed out waiting for a Magcard callback");
            }
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            cancel();
            return AutomaticCaseResult.error("Interrupted while waiting for a Magcard callback");
        }
        AutomaticCaseResult value = result.get();
        return value == null
                ? AutomaticCaseResult.error("Magcard evaluator returned no result") : value;
    }

    /** Waits until the interface reaches the point where a short operator prompt is actionable. */
    public boolean awaitUserActionReady(long timeoutMs) {
        try {
            if (!userActionReady.await(timeoutMs, TimeUnit.MILLISECONDS)) {
                complete(AutomaticCaseResult.error(
                        "Timed out waiting for the Magcard operator-action point"));
            }
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            complete(AutomaticCaseResult.error(
                    "Interrupted while waiting for the Magcard operator-action point"));
        }
        return result.get() == null;
    }

    public void cancel() {
        stopSearchQuietly();
        complete(AutomaticCaseResult.error("Magcard case was cancelled"));
    }

    private void startCase() throws RemoteException {
        switch (caseName) {
            case "H01001":
                startTimeoutCase(0, 60);
                break;
            case "H01002":
                startTimeoutCase(10, 10);
                break;
            case "H01003":
                startTimeoutCase(60, 60);
                break;
            case "H01005":
                startSearch(60, Outcome.NORMAL_ANY_TRACK_OR_SRED, 0, 0);
                markUserActionReady();
                break;
            case "H01013":
                startSearch(60, Outcome.FAST_SWIPE_REJECTED, 0, 0);
                markUserActionReady();
                break;
            case "H01014":
                startTimeoutCase(300, 300);
                break;
            case "H01015":
                startTimeoutCase(-1, 60);
                break;
            case "H01016":
                startTimeoutCase(301, 60);
                break;
            case "H01018":
                startConcurrentSearchCase();
                break;
            case "H01023":
                startSearch(60, Outcome.NO_MAGCARD_RESPONSE, 60, 0);
                markUserActionReady();
                break;
            case "H01025":
                startTimeoutCase(1, 1);
                break;
            case "H01026":
                startSearch(300, Outcome.ANY_TRACK_SUCCESS, 0, 0);
                startWorker("long-search-action", () -> {
                    if (pause(LONG_SEARCH_ACTION_DELAY_MS)) {
                        markUserActionReady();
                    }
                });
                break;
            case "H01027":
                startSearch(60, Outcome.READ_ERROR, 0, 0);
                markUserActionReady();
                break;
            case "H02001":
                reader.stopSearch();
                complete(AutomaticCaseResult.pass("stopSearch returned without RemoteException"));
                break;
            case "H02002":
                startDelayedStopCase();
                break;
            case "H02003":
                startRepeatedStopCase();
                break;
            case "H02005":
                startRepeatedImmediateStopCase();
                break;
            case "H04001":
                startTrackCase(0b00000001, 0b00000001);
                break;
            case "H04002":
                startTrackCase(0b00000010, 0b00000010);
                break;
            case "H04003":
                startTrackCase(0b00000100, 0b00000100);
                break;
            case "H04004":
                startTrackCase(0b00000011, 0b00000011);
                break;
            case "H04005":
                startTrackCase(0b00000101, 0b00000101);
                break;
            case "H04006":
                startTrackCase(0b00000111, 0b00000111);
                break;
            case "H04007":
                startTrackCase(0b00000000, 0b00000111);
                break;
            case "H04008":
                startTrackCase(0b00001000, 0b00000111);
                break;
            case "H01004":
                complete(AutomaticCaseResult.notRun(
                        "listener=null exposes no callback or state for an automatic verdict"));
                break;
            case "H01019":
                complete(AutomaticCaseResult.notRun(
                        "D03028 does not expose its printer callback to the Magcard evaluator"));
                break;
            default:
                complete(AutomaticCaseResult.notRun(
                        "No automatic Magcard contract for " + caseName));
                break;
        }
    }

    private void startTimeoutCase(int apiTimeoutSeconds, int expectedTimeoutSeconds)
            throws RemoteException {
        startSearch(apiTimeoutSeconds, Outcome.TIMEOUT, expectedTimeoutSeconds, 0);
    }

    private void startTrackCase(int requestedMask, int expectedMask) throws RemoteException {
        reader.enableTrack(requestedMask);
        resetTrackMask = true;
        startSearch(60, Outcome.EXACT_TRACKS, 0, expectedMask);
        markUserActionReady();
    }

    private void startSearch(int timeoutSeconds, Outcome outcome, int expectedTimeoutSeconds,
                             int expectedTrackMask) throws RemoteException {
        searchActive.set(true);
        reader.searchCard(timeoutSeconds, new EvaluatingListener(outcome, expectedTimeoutSeconds,
                expectedTrackMask));
    }

    private void startConcurrentSearchCase() throws RemoteException {
        AtomicBoolean secondStarted = new AtomicBoolean();
        AtomicBoolean secondSucceeded = new AtomicBoolean();
        searchActive.set(true);
        reader.searchCard(60, new MagCardListener.Stub() {
            @Override
            public void onError(int error, String message) {
                complete(AutomaticCaseResult.fail(
                        "First search ended with onError code " + error));
            }

            @Override
            public void onSuccess(Bundle track) {
                if (!secondStarted.get()) {
                    complete(AutomaticCaseResult.fail(
                            "First search completed before the second search was issued"));
                    return;
                }
                if (!hasAnyTrack(track)) {
                    complete(AutomaticCaseResult.fail(
                            "First search returned onSuccess without track data"));
                    return;
                }
                startWorker("concurrent-observation", () -> {
                    if (!pause(1_000L)) {
                        return;
                    }
                    if (!secondSucceeded.get()) {
                        complete(AutomaticCaseResult.pass(
                                "First listener received the swipe; second listener did not"));
                    }
                });
            }

            @Override
            public void onTimeout() {
                complete(AutomaticCaseResult.fail("First search timed out"));
            }
        });
        startWorker("second-search", () -> {
            if (!pause(SECOND_SEARCH_DELAY_MS)) {
                return;
            }
            secondStarted.set(true);
            try {
                reader.searchCard(30, new MagCardListener.Stub() {
                    @Override
                    public void onError(int error, String message) {
                        // Rejection of the concurrent request is compatible with the case contract.
                    }

                    @Override
                    public void onSuccess(Bundle track) {
                        secondSucceeded.set(true);
                        complete(AutomaticCaseResult.fail(
                                "Second listener received the swipe and replaced/concurred with the first"));
                    }

                    @Override
                    public void onTimeout() {
                        // The first listener remains the deciding observation.
                    }
                });
                markUserActionReady();
            } catch (RemoteException error) {
                complete(interfaceError("issuing the second searchCard call", error));
            }
        });
    }

    private void startDelayedStopCase() throws RemoteException {
        searchActive.set(true);
        reader.searchCard(60, noSuccessAfterStopListener("H02002"));
        startWorker("delayed-stop", () -> {
            if (!pause(STOP_DELAY_MS)) {
                return;
            }
            try {
                reader.stopSearch();
                searchActive.set(false);
                markUserActionReady();
            } catch (RemoteException error) {
                complete(interfaceError("stopping the active search", error));
                return;
            }
            if (pause(POST_STOP_OBSERVATION_MS)) {
                complete(AutomaticCaseResult.pass(
                        "stopSearch returned and no onSuccess arrived during the observation window"));
            }
        });
    }

    private void startRepeatedStopCase() {
        startWorker("ten-stop-cycles", () -> {
            for (int cycle = 1; cycle <= 10 && result.get() == null; cycle++) {
                try {
                    searchActive.set(true);
                    reader.searchCard(60, noSuccessAfterStopListener("H02003 cycle " + cycle));
                    if (!pause(STOP_DELAY_MS)) {
                        return;
                    }
                    reader.stopSearch();
                    searchActive.set(false);
                    if (!pause(5_000L)) {
                        return;
                    }
                } catch (RemoteException error) {
                    complete(interfaceError("running stop cycle " + cycle, error));
                    return;
                }
            }
            if (result.get() == null) {
                complete(AutomaticCaseResult.pass(
                        "10 searchCard/stopSearch cycles returned without RemoteException"));
            }
        });
    }

    private void startRepeatedImmediateStopCase() throws RemoteException {
        searchActive.set(true);
        reader.searchCard(60, noSuccessAfterStopListener("H02005"));
        reader.stopSearch();
        reader.stopSearch();
        searchActive.set(false);
        markUserActionReady();
        startWorker("double-stop-observation", () -> {
            if (pause(POST_STOP_OBSERVATION_MS)) {
                complete(AutomaticCaseResult.pass(
                        "Two stopSearch calls returned and no onSuccess arrived afterward"));
            }
        });
    }

    private MagCardListener noSuccessAfterStopListener(String label) {
        return new MagCardListener.Stub() {
            @Override
            public void onError(int error, String message) {
                // Some service versions report cancellation through onError.
            }

            @Override
            public void onSuccess(Bundle track) {
                complete(AutomaticCaseResult.fail(
                        label + " unexpectedly received onSuccess"));
            }

            @Override
            public void onTimeout() {
                // Cancellation may race with the listener's terminal notification.
            }
        };
    }

    private final class EvaluatingListener extends MagCardListener.Stub {
        private final Outcome outcome;
        private final int expectedTimeoutSeconds;
        private final int expectedTrackMask;

        EvaluatingListener(Outcome outcome, int expectedTimeoutSeconds, int expectedTrackMask) {
            this.outcome = outcome;
            this.expectedTimeoutSeconds = expectedTimeoutSeconds;
            this.expectedTrackMask = expectedTrackMask;
        }

        @Override
        public void onError(int error, String message) {
            searchActive.set(false);
            if (outcome == Outcome.NORMAL_ANY_TRACK_OR_SRED && error == 101) {
                complete(AutomaticCaseResult.pass("SRED-compatible onError code 101"));
            } else if (outcome == Outcome.FAST_SWIPE_REJECTED) {
                complete(AutomaticCaseResult.pass("Fast swipe ended with onError code " + error));
            } else if (outcome == Outcome.READ_ERROR) {
                complete(AutomaticCaseResult.pass("Damaged card ended with onError code " + error));
            } else {
                complete(AutomaticCaseResult.fail("Unexpected onError code " + error));
            }
        }

        @Override
        public void onSuccess(Bundle track) {
            searchActive.set(false);
            int actualMask = trackMask(track);
            switch (outcome) {
                case NORMAL_ANY_TRACK_OR_SRED:
                    complete(actualMask != 0
                            ? AutomaticCaseResult.pass(trackPresence(actualMask))
                            : AutomaticCaseResult.fail(
                                    "onSuccess returned without populated track data"));
                    break;
                case FAST_SWIPE_REJECTED:
                    complete(actualMask != 0b00000111
                            ? AutomaticCaseResult.pass("Fast swipe returned incomplete data; "
                                    + trackPresence(actualMask))
                            : AutomaticCaseResult.fail(
                                    "Fast swipe returned complete data for all three tracks"));
                    break;
                case ANY_TRACK_SUCCESS:
                    complete(actualMask != 0
                            ? AutomaticCaseResult.pass(trackPresence(actualMask))
                            : AutomaticCaseResult.fail(
                                    "onSuccess returned without populated track data"));
                    break;
                case EXACT_TRACKS:
                    complete(actualMask == expectedTrackMask
                            ? AutomaticCaseResult.pass(trackPresence(actualMask))
                            : AutomaticCaseResult.fail("Expected track mask "
                                    + mask(expectedTrackMask) + "; " + trackPresence(actualMask)));
                    break;
                default:
                    complete(AutomaticCaseResult.fail(
                            "Unexpected onSuccess; " + trackPresence(actualMask)));
                    break;
            }
        }

        @Override
        public void onTimeout() {
            searchActive.set(false);
            if (outcome != Outcome.TIMEOUT && outcome != Outcome.NO_MAGCARD_RESPONSE) {
                complete(AutomaticCaseResult.fail("Unexpected onTimeout"));
                return;
            }
            long elapsedMs = SystemClock.elapsedRealtime() - startedAt;
            long expectedMs = expectedTimeoutSeconds * 1_000L;
            long earlyToleranceMs = expectedTimeoutSeconds <= 1 ? 700L : 3_000L;
            if (elapsedMs + earlyToleranceMs < expectedMs) {
                complete(AutomaticCaseResult.fail("onTimeout arrived too early after "
                        + elapsedMs + " ms; expected approximately " + expectedMs + " ms"));
                return;
            }
            complete(AutomaticCaseResult.pass("onTimeout after " + elapsedMs
                    + " ms; expected approximately " + expectedMs + " ms"));
        }
    }

    private void complete(AutomaticCaseResult value) {
        if (!result.compareAndSet(null, value)) {
            return;
        }
        userActionReady.countDown();
        stopSearchQuietly();
        if (resetTrackMask) {
            try {
                reader.enableTrack(0);
            } catch (RemoteException ignored) {
                // Preserve the primary case result; the next track case sets its own mask.
            }
        }
        finished.countDown();
    }

    private void markUserActionReady() {
        userActionReady.countDown();
    }

    private void stopSearchQuietly() {
        if (reader == null || !searchActive.compareAndSet(true, false)) {
            return;
        }
        try {
            reader.stopSearch();
        } catch (RemoteException ignored) {
            // The primary callback or timeout remains the authoritative result.
        }
    }

    private void startWorker(String suffix, Runnable action) {
        new Thread(() -> {
            try {
                action.run();
            } catch (Throwable error) {
                complete(interfaceError("running " + suffix, error));
            }
        }, "magcard-" + caseName.toLowerCase(Locale.US) + '-' + suffix).start();
    }

    private boolean pause(long delayMs) {
        if (result.get() != null) {
            return false;
        }
        try {
            Thread.sleep(delayMs);
            return result.get() == null;
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            complete(AutomaticCaseResult.error("Magcard worker was interrupted"));
            return false;
        }
    }

    private AutomaticCaseResult interfaceError(String action, Throwable error) {
        return AutomaticCaseResult.error(action + " failed with "
                + error.getClass().getSimpleName());
    }

    private static boolean hasAnyTrack(Bundle track) {
        return trackMask(track) != 0;
    }

    private static int trackMask(Bundle track) {
        if (track == null) {
            return 0;
        }
        int mask = 0;
        if (hasValue(track.getString("TRACK1"))) {
            mask |= 0b00000001;
        }
        if (hasValue(track.getString("TRACK2"))) {
            mask |= 0b00000010;
        }
        if (hasValue(track.getString("TRACK3"))) {
            mask |= 0b00000100;
        }
        return mask;
    }

    private static boolean hasValue(String value) {
        return value != null && !value.trim().isEmpty() && !"null".equalsIgnoreCase(value.trim());
    }

    private static String trackPresence(int mask) {
        return "track presence mask=" + mask(mask) + " (card data omitted)";
    }

    private static String mask(int mask) {
        return String.format(Locale.US, "0b%3s",
                Integer.toBinaryString(mask & 0b00000111)).replace(' ', '0');
    }

    private enum Outcome {
        TIMEOUT,
        NORMAL_ANY_TRACK_OR_SRED,
        FAST_SWIPE_REJECTED,
        ANY_TRACK_SUCCESS,
        READ_ERROR,
        NO_MAGCARD_RESPONSE,
        EXACT_TRACKS
    }
}
