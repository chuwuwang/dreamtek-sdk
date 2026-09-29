package moudles.newModules.data;

import android.os.Bundle;

public class PanBundleStore {
    private static Bundle bundle;

    public static void setBundle(Bundle b) {
        bundle = b;
    }

    public static Bundle getBundle() {
        return bundle;
    }
}
