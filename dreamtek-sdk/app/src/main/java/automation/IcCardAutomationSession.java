package automation;

import android.os.Bundle;
import android.os.RemoteException;

import com.dreamtek.smartpos.deviceservice.aidl.ISmartCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.SmartCardStatusChangedEvent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** Executes and evaluates one selected contact-IC-card case without logging card data. */
public final class IcCardAutomationSession {
    private static final byte[] SELECT_PSE_APDU = new byte[]{
            (byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00, (byte) 0x0E,
            (byte) '1', (byte) 'P', (byte) 'A', (byte) 'Y', (byte) '.',
            (byte) 'S', (byte) 'Y', (byte) 'S', (byte) '.', (byte) 'D',
            (byte) 'D', (byte) 'F', (byte) '0', (byte) '1', (byte) '0'
    };

    private final ISmartCardReader icCardReader;
    private final ISmartCardReader psamCardReader;
    private final String caseName;
    private final CountDownLatch userActionReady = new CountDownLatch(1);
    private final CountDownLatch completed = new CountDownLatch(1);
    private final Object callbackStateLock = new Object();

    private volatile AutomaticCaseResult result;
    private volatile boolean callbackCase;
    private boolean insertedStatusSeen;

    private IcCardAutomationSession(ISmartCardReader icCardReader,
                                    ISmartCardReader psamCardReader,
                                    String caseName) {
        this.icCardReader = icCardReader;
        this.psamCardReader = psamCardReader;
        this.caseName = caseName;
    }

    public static IcCardAutomationSession start(ISmartCardReader icCardReader,
                                                ISmartCardReader psamCardReader,
                                                String caseName) {
        IcCardAutomationSession session =
                new IcCardAutomationSession(icCardReader, psamCardReader, caseName);
        session.prepare();
        return session;
    }

    private void prepare() {
        if (icCardReader == null) {
            userActionReady.countDown();
            complete(AutomaticCaseResult.error("getSmartCardReader(0) returned null"));
            return;
        }
        if ("I10001".equals(caseName)) {
            callbackCase = true;
            startStatusChangeDetection();
            return;
        }
        userActionReady.countDown();
    }

    private void startStatusChangeDetection() {
        try {
            icCardReader.detectCardStatusChanged(30, new SmartCardStatusChangedEvent.Stub() {
                @Override
                public void onChanged(int statusNow) throws RemoteException {
                    synchronized (callbackStateLock) {
                        if (statusNow == 1) {
                            insertedStatusSeen = true;
                            return;
                        }
                        if (statusNow == 0 && insertedStatusSeen) {
                            complete(AutomaticCaseResult.pass(
                                    "Observed contact-card status sequence 1 then 0"));
                        }
                    }
                }

                @Override
                public void onTimeout() throws RemoteException {
                    complete(AutomaticCaseResult.fail(callbackObservation()));
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    complete(AutomaticCaseResult.fail(
                            "Contact-card status callback errorCode=" + errorCode));
                }
            });
            userActionReady.countDown();
        } catch (RemoteException error) {
            userActionReady.countDown();
            complete(AutomaticCaseResult.error(remoteFailure(
                    "detectCardStatusChanged", error)));
        }
    }

    public boolean awaitUserActionReady(long timeoutMs) {
        try {
            return userActionReady.await(timeoutMs, TimeUnit.MILLISECONDS) && result == null;
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            complete(AutomaticCaseResult.error(
                    "Interrupted while preparing the contact-card action"));
            return false;
        }
    }

    /** Runs synchronous cases after the voice prompt reaches the operator-action point. */
    public void executeAfterUserAction() {
        if (callbackCase || completed.getCount() == 0) {
            return;
        }
        try {
            complete(evaluateSynchronousCase());
        } catch (RemoteException error) {
            complete(AutomaticCaseResult.error(remoteFailure(caseName, error)));
        } catch (Throwable error) {
            String detail = error.getMessage() == null ? "no detail" : error.getMessage();
            complete(AutomaticCaseResult.error(
                    error.getClass().getSimpleName() + ": " + detail));
        }
    }

    public AutomaticCaseResult await(long timeoutMs) {
        try {
            if (!completed.await(timeoutMs, TimeUnit.MILLISECONDS)) {
                return AutomaticCaseResult.error(
                        "Timed out waiting for the contact-card result");
            }
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            return AutomaticCaseResult.error(
                    "Interrupted while waiting for the contact-card result");
        }
        AutomaticCaseResult value = result;
        return value == null
                ? AutomaticCaseResult.error("Contact-card evaluator returned no result") : value;
    }

    public void cancel() {
        complete(AutomaticCaseResult.error("Contact-card case was cancelled"));
    }

    private AutomaticCaseResult evaluateSynchronousCase() throws RemoteException {
        switch (caseName) {
            case "I01001":
                return booleanResult("powerUp", true, icCardReader.powerUp());
            case "I02002":
                return booleanResult("powerDown", true, icCardReader.powerDown());
            case "I03001":
                return booleanResult("isCardIn", false, icCardReader.isCardIn());
            case "I03002":
                return booleanResult("isCardIn", true, icCardReader.isCardIn());
            case "I05001":
                return evaluateApdu();
            case "I06001":
                if (psamCardReader == null) {
                    return AutomaticCaseResult.error("getSmartCardReader(1) returned null");
                }
                return booleanResult("isPSAMCardExists", false,
                        psamCardReader.isPSAMCardExists());
            case "I07001":
                return evaluatePoweredDownCardStatus();
            case "I07002":
                return evaluatePoweredUpCardStatus();
            case "I07003":
                return evaluateRemovedCardStatus();
            case "I08001":
                return evaluatePowerUpAtr();
            case "I09001":
                return evaluateConfiguredPowerUpAtr();
            default:
                return AutomaticCaseResult.notRun(
                        "No reviewed contact-card contract for " + caseName);
        }
    }

    private AutomaticCaseResult evaluateApdu() throws RemoteException {
        boolean powered = icCardReader.powerUp();
        if (!powered) {
            return AutomaticCaseResult.fail("powerUp=false; APDU was not sent");
        }
        byte[] response = icCardReader.exchangeApdu(SELECT_PSE_APDU);
        if (response == null || response.length < 2) {
            return AutomaticCaseResult.fail(
                    "powerUp=true; APDU response is null or shorter than two status bytes");
        }
        int sw1 = response[response.length - 2] & 0xFF;
        int sw2 = response[response.length - 1] & 0xFF;
        String observation = "powerUp=true; responseLength=" + response.length
                + "; statusWord=" + hexByte(sw1) + hexByte(sw2)
                + "; response data omitted";
        return sw1 == 0x90 && sw2 == 0x00
                ? AutomaticCaseResult.pass(observation)
                : AutomaticCaseResult.fail(observation);
    }

    private AutomaticCaseResult evaluatePoweredDownCardStatus() throws RemoteException {
        boolean poweredDown = icCardReader.powerDown();
        int status = icCardReader.checkCardStatus();
        String observation = "powerDown=" + poweredDown + "; cardStatus=" + status;
        return poweredDown && status == 1
                ? AutomaticCaseResult.pass(observation)
                : AutomaticCaseResult.fail(observation);
    }

    private AutomaticCaseResult evaluatePoweredUpCardStatus() throws RemoteException {
        boolean powered = icCardReader.powerUp();
        int status = icCardReader.checkCardStatus();
        String observation = "powerUp=" + powered + "; cardStatus=" + status;
        return powered && status == 2
                ? AutomaticCaseResult.pass(observation)
                : AutomaticCaseResult.fail(observation);
    }

    private AutomaticCaseResult evaluateRemovedCardStatus() throws RemoteException {
        boolean powered = icCardReader.powerUp();
        int status = icCardReader.checkCardStatus();
        String observation = "powerUp=" + powered + "; cardStatus=" + status;
        return !powered && status == 0
                ? AutomaticCaseResult.pass(observation)
                : AutomaticCaseResult.fail(observation);
    }

    private AutomaticCaseResult evaluatePowerUpAtr() throws RemoteException {
        boolean powered = icCardReader.powerUp();
        byte[] atr = powered ? icCardReader.getPowerUpATR() : null;
        int atrLength = atr == null ? 0 : atr.length;
        String observation = "powerUp=" + powered + "; atrLength=" + atrLength
                + "; ATR data omitted";
        return powered && atrLength > 0
                ? AutomaticCaseResult.pass(observation)
                : AutomaticCaseResult.fail(observation);
    }

    private AutomaticCaseResult evaluateConfiguredPowerUpAtr() throws RemoteException {
        Bundle config = new Bundle();
        config.putBoolean("ATRCheck", true);
        byte[] atr = icCardReader.powerUpWithConfig(config);
        int atrLength = atr == null ? 0 : atr.length;
        String observation = "ATRCheck=true; atrLength=" + atrLength
                + "; ATR data omitted";
        return atrLength > 0
                ? AutomaticCaseResult.pass(observation)
                : AutomaticCaseResult.fail(observation);
    }

    private static AutomaticCaseResult booleanResult(String api, boolean expected,
                                                     boolean actual) {
        String observation = api + "=" + actual + "; expected=" + expected;
        return actual == expected
                ? AutomaticCaseResult.pass(observation)
                : AutomaticCaseResult.fail(observation);
    }

    private String callbackObservation() {
        synchronized (callbackStateLock) {
            return insertedStatusSeen
                    ? "Status 1 was observed, but status 0 was not observed before timeout"
                    : "Status 1 then 0 was not observed before timeout";
        }
    }

    private synchronized void complete(AutomaticCaseResult value) {
        if (result != null) {
            return;
        }
        result = value;
        completed.countDown();
    }

    private static String remoteFailure(String api, RemoteException error) {
        String detail = error.getMessage() == null ? "no detail" : error.getMessage();
        return api + " RemoteException: " + detail;
    }

    private static String hexByte(int value) {
        final char[] digits = "0123456789ABCDEF".toCharArray();
        return new String(new char[]{digits[(value >>> 4) & 0x0F], digits[value & 0x0F]});
    }
}
