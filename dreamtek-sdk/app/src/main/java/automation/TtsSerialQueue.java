package automation;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** Strictly serial English TTS. A failed or timed-out utterance is a hard failure. */
public final class TtsSerialQueue {
    public static final String ENGINE_PACKAGE = "com.iflytek.speechcloud";
    public static final String EXPECTED_ENGINE_VERSION = "1.0.10024";
    private static final float SPEECH_RATE = 0.9f;

    public interface InitListener {
        void onReady();

        void onFailure(String reason);
    }

    private final Map<String, SpeechWait> waits = new ConcurrentHashMap<>();
    private volatile TextToSpeech textToSpeech;
    private volatile boolean ready;
    private volatile String failureReason = "TTS has not finished initializing";

    public TtsSerialQueue(Context context, final InitListener listener) {
        textToSpeech = new TextToSpeech(context.getApplicationContext(), status -> {
            if (status != TextToSpeech.SUCCESS) {
                failureReason = "English TTS initialization failed: " + status;
                listener.onFailure(failureReason);
                return;
            }
            TextToSpeech engine = textToSpeech;
            if (engine == null) {
                failureReason = "English TTS engine was not created";
                listener.onFailure(failureReason);
                return;
            }
            int language = engine.setLanguage(Locale.US);
            if (language == TextToSpeech.LANG_MISSING_DATA
                    || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                failureReason = "English TTS language data is unavailable";
                listener.onFailure(failureReason);
                return;
            }
            if (engine.setSpeechRate(SPEECH_RATE) == TextToSpeech.ERROR) {
                failureReason = "English TTS rejected speech rate " + SPEECH_RATE;
                listener.onFailure(failureReason);
                return;
            }
            engine.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                }

                @Override
                public void onDone(String utteranceId) {
                    finish(utteranceId, null);
                }

                @Override
                public void onError(String utteranceId) {
                    finish(utteranceId, "English TTS playback error");
                }

                @Override
                public void onError(String utteranceId, int errorCode) {
                    finish(utteranceId, "English TTS playback error " + errorCode);
                }

                @Override
                public void onStop(String utteranceId, boolean interrupted) {
                    finish(utteranceId,
                            interrupted ? "English TTS playback was interrupted" : null);
                }
            });
            ready = true;
            failureReason = "";
            listener.onReady();
        }, ENGINE_PACKAGE);
    }

    private void finish(String utteranceId, String error) {
        SpeechWait wait = waits.get(utteranceId);
        if (wait != null) {
            wait.successful = error == null;
            wait.failureReason = error == null ? "" : error;
            wait.latch.countDown();
        }
    }

    public boolean isReady() {
        return ready;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public synchronized boolean speakAndWait(String text, long timeoutMs) {
        if (!ready) {
            return false;
        }
        if (text == null || text.trim().isEmpty()) {
            failureReason = "English TTS prompt is empty";
            return false;
        }
        failureReason = "";
        String utteranceId = "automation-" + UUID.randomUUID();
        SpeechWait wait = new SpeechWait();
        waits.put(utteranceId, wait);
        Bundle parameters = new Bundle();
        int result = textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, parameters, utteranceId);
        if (result != TextToSpeech.SUCCESS) {
            waits.remove(utteranceId);
            failureReason = "English TTS rejected an utterance";
            return false;
        }
        try {
            if (!wait.latch.await(timeoutMs, TimeUnit.MILLISECONDS)) {
                failureReason = "English TTS utterance timed out";
                return false;
            }
            if (!wait.successful) {
                failureReason = wait.failureReason.isEmpty()
                        ? "English TTS utterance failed" : wait.failureReason;
            }
            return wait.successful;
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            failureReason = "English TTS was interrupted";
            return false;
        } finally {
            waits.remove(utteranceId);
        }
    }

    public void shutdown() {
        ready = false;
        TextToSpeech engine = textToSpeech;
        if (engine != null) {
            engine.stop();
            engine.shutdown();
        }
    }

    private static final class SpeechWait {
        final CountDownLatch latch = new CountDownLatch(1);
        volatile boolean successful;
        volatile String failureReason = "";
    }
}
