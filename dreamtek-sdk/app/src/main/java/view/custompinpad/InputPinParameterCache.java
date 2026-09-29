package view.custompinpad;

public class InputPinParameterCache {
    private static PinPadInitPinInputCustomViewParamIn pinPadInitPinInputCustomViewParamIn;

    public static void setPinPadInitPinInputCustomViewParamIn(PinPadInitPinInputCustomViewParamIn pinPadInitPinInputCustomViewParamIn) {
        InputPinParameterCache.pinPadInitPinInputCustomViewParamIn = pinPadInitPinInputCustomViewParamIn;
    }

    public static PinPadInitPinInputCustomViewParamIn getPinPadInitPinInputCustomViewParamIn() {
        return pinPadInitPinInputCustomViewParamIn;
    }
}
