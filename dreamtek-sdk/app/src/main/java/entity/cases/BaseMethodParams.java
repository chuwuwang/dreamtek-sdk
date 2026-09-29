package entity.cases;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RuihaoS on 2021/5/9.
 */
public class BaseMethodParams implements Parcelable {

    private int keyId;
    private int keyType;

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.keyId);
        dest.writeInt(this.keyType);
    }

    public void readFromParcel(Parcel source) {
        this.keyId = source.readInt();
        this.keyType = source.readInt();
    }

    public BaseMethodParams() {
    }

    protected BaseMethodParams(Parcel in) {
        this.keyId = in.readInt();
        this.keyType = in.readInt();
    }

    public static final Creator<BaseMethodParams> CREATOR = new Creator<BaseMethodParams>() {
        @Override
        public BaseMethodParams createFromParcel(Parcel source) {
            return new BaseMethodParams(source);
        }

        @Override
        public BaseMethodParams[] newArray(int size) {
            return new BaseMethodParams[size];
        }
    };
}
