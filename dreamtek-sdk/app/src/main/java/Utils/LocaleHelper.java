package Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * Utility class for managing application locale.
 * Keeps the application UI in English regardless of the device system language.
 */
public class LocaleHelper {

    private static final String TAG = "LocaleHelper";

    /**
     * Attach the base context with the correct locale configuration.
     * Call this in Activity.attachBaseContext() before super.attachBaseContext().
     *
     * @param context The base context.
     * @return Context with locale-aware configuration.
     */
    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, getLocale(context));
        } else {
            return updateResourcesLegacy(context, getLocale(context));
        }
    }

    /**
     * Return the single locale supported by this test client.
     *
     * @param context Application context.
     * @return The resolved Locale.
     */
    public static Locale getLocale(Context context) {
        return Locale.ENGLISH;
    }

    /**
     * Check if the current locale is Chinese.
     *
     * @param context Application context.
     * @return true if the locale is Chinese.
     */
    public static boolean isChinese(Context context) {
        return false;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    /**
     * Apply locale settings when the activity configuration changes.
     * Call this in onConfigurationChanged() of the Application class
     * or when handling configuration changes manually.
     *
     * @param context       Application context.
     * @param newConfig     The new configuration.
     */
    public static void onConfigurationChanged(Context context, Configuration newConfig) {
        // Locale is handled in attachBaseContext; nothing to do here.
    }
}
