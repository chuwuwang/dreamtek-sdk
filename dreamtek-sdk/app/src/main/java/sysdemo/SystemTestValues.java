package sysdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IInterface;
import com.dreamtek.smartpos.system_service.aidl.device.TusnData;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Iterator;

final class SystemTestValues {
    private SystemTestValues() { }

    static Object parse(Context context, String type, String input) throws Exception {
        if ("<null>".equals(input)) {
            if ("int".equals(type) || "long".equals(type) || "boolean".equals(type)) {
                throw new IllegalArgumentException("Primitive " + type + " cannot be null");
            }
            return null;
        }
        switch (type) {
            case "String": return input;
            case "int": return Integer.valueOf(input.trim());
            case "long": return Long.valueOf(input.trim());
            case "boolean":
                if (!"true".equals(input) && !"false".equals(input)) {
                    throw new IllegalArgumentException("Boolean must be true or false");
                }
                return Boolean.valueOf(input);
            case "byte[]":
                String hex = input.replace(" ", "");
                if (!hex.matches("(?:[0-9a-fA-F]{2})*")) {
                    throw new IllegalArgumentException("Bytes require an even number of hexadecimal digits");
                }
                byte[] bytes = new byte[hex.length() / 2];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
                }
                return bytes;
            case "Bundle": return bundle(new JSONObject(input));
            case "Bitmap":
                Bitmap bitmap;
                if (input.equals("wallpaper1") || input.equals("wallpaper2")) {
                    String asset = input.equals("wallpaper1") ? "sysdemo_wallpaper1.png" : "sysdemo_wallpaper2.jpg";
                    try (InputStream stream = context.getAssets().open(asset)) {
                        bitmap = BitmapFactory.decodeStream(stream);
                    }
                } else {
                    BitmapFactory.Options bounds = new BitmapFactory.Options();
                    bounds.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(input, bounds);
                    if (bounds.outWidth < 1 || bounds.outHeight < 1
                            || (long) bounds.outWidth * bounds.outHeight > 4_000_000L) {
                        throw new IllegalArgumentException("Select a valid image of at most 4 million pixels");
                    }
                    bitmap = BitmapFactory.decodeFile(input);
                }
                if (bitmap == null) throw new IllegalArgumentException("Cannot decode wallpaper");
                return bitmap;
            default: throw new IllegalArgumentException("Unsupported parameter type: " + type);
        }
    }

    private static Bundle bundle(JSONObject json) throws Exception {
        Bundle result = new Bundle();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                JSONObject typed = (JSONObject) value;
                String type = typed.getString("type");
                Object raw = typed.get("value");
                switch (type) {
                    case "int": result.putInt(key, Integer.parseInt(raw.toString())); break;
                    case "long": result.putLong(key, Long.parseLong(raw.toString())); break;
                    case "string": result.putString(key, raw == JSONObject.NULL ? null : raw.toString()); break;
                    case "boolean":
                        if (!(raw instanceof Boolean)) throw new IllegalArgumentException(key + ": expected boolean");
                        result.putBoolean(key, (Boolean) raw); break;
                    case "string[]":
                        JSONArray array = (JSONArray) raw;
                        String[] strings = new String[array.length()];
                        for (int i = 0; i < strings.length; i++) strings[i] = array.getString(i);
                        result.putStringArray(key, strings); break;
                    default: throw new IllegalArgumentException("Unsupported Bundle type: " + type);
                }
            } else if (value == JSONObject.NULL) result.putString(key, null);
            else if (value instanceof Boolean) result.putBoolean(key, (Boolean) value);
            else if (value instanceof Integer) result.putInt(key, (Integer) value);
            else if (value instanceof Long) result.putLong(key, (Long) value);
            else if (value instanceof String) result.putString(key, (String) value);
            else throw new IllegalArgumentException("Specify the Bundle type for " + key);
        }
        return result;
    }

    static String describe(Object value) throws Exception {
        if (value == null) return "null";
        if (value instanceof Bundle) {
            JSONObject json = new JSONObject();
            Bundle bundle = (Bundle) value;
            for (String key : bundle.keySet()) {
                Object item = bundle.get(key);
                JSONObject typed = new JSONObject();
                typed.put("type", item == null ? "null" : item.getClass().getSimpleName());
                typed.put("value", item == null ? JSONObject.NULL : describe(item));
                json.put(key, typed);
            }
            return json.toString(2);
        }
        if (value instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) value;
            return "Bitmap " + bitmap.getWidth() + "x" + bitmap.getHeight() + ", " + bitmap.getByteCount() + " bytes";
        }
        if (value instanceof IInterface) return "Binder alive=" + ((IInterface) value).asBinder().isBinderAlive();
        if (value instanceof byte[]) {
            StringBuilder hex = new StringBuilder();
            for (byte b : (byte[]) value) hex.append(String.format(java.util.Locale.US, "%02X", b & 255));
            return hex.toString();
        }
        if (value instanceof TusnData) {
            // Intentionally the system contract's Parcelable, not the device package's namesake.
            JSONObject json = new JSONObject();
            TusnData data = (TusnData) value;
            json.put("terminalType", data.getTerminalType());
            json.put("mac", data.getMac());
            json.put("tusn", data.getTusn());
            return json.toString(2);
        }
        if (value instanceof String[]) return new JSONArray(java.util.Arrays.asList((String[]) value)).toString();
        return String.valueOf(value);
    }
}
