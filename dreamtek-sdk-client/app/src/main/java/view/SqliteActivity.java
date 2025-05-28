package view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.verifone.activity.R;

public class SqliteActivity extends Activity implements View.OnClickListener {
    private Button addButton;
    private Button removeButton;
    private Button modifyButton;
    private Button searchButton;
    private VfiDBHelper mVfiDBHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlite_activity);

        mVfiDBHelper = new VfiDBHelper(this, "/sdcard/Download/verifone.db");
        addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(this);
        removeButton = (Button) findViewById(R.id.remove);
        removeButton.setOnClickListener(this);
        modifyButton = (Button) findViewById(R.id.modify);
        modifyButton.setOnClickListener(this);
        searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(this);
    }

    static int num = 0;

    @Override
    public void onClick(View v) {
        SQLiteDatabase db = null;
        ContentValues contentValues = null;
        Cursor cursor;
        long len = 0;

        try {
            db = mVfiDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("TAG", "find db error....");
            return;
        }

        switch (v.getId()) {
            case R.id.add:
                contentValues = new ContentValues();
                contentValues.put("cardId", "0000" + num++);
                contentValues.put("amount", 100);
                contentValues.put("date", "2017-07-01");
                len = db.insert(VfiDBHelper.TABLE_NAME, null, contentValues);
                Log.d("TAG", "database len: " + len);
                break;
            case R.id.remove:
                db.delete(VfiDBHelper.TABLE_NAME, null, null);
                num = 0;
                break;
            case R.id.modify:

                contentValues = new ContentValues();
                contentValues.put("amount", 200);
                db.update(VfiDBHelper.TABLE_NAME, contentValues, "cardId = ?", new String[]{"00001"});

                // or
//                db.execSQL("update vfiTable set amount=120 where cardId='00001'");
                break;
            case R.id.search:
                cursor = db.query(VfiDBHelper.TABLE_NAME, null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    String cardId = cursor.getString(cursor.getColumnIndex("cardId"));
                    int amount = cursor.getInt(cursor.getColumnIndex("amount"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    Log.d("TAG", "cardId:" + cardId + "\tamount:" + amount + "\tdate:" + date);
                }
                break;
        }
    }

    class VfiDBHelper extends SQLiteOpenHelper {
        private static final int DB_VERSION = 1;
        public static final String TABLE_NAME = "vfiTable";

        public VfiDBHelper(Context context, String dbName) {
            super(context, dbName, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                    .append(TABLE_NAME)
                    .append('(')
                    .append("cardId").append(" TEXT PRIMARY KEY,")
                    .append("amount").append(" INTEGER,")
                    .append("date").append(" TEXT")
                    .append(')');
            Log.d("TAG", stringBuilder.toString());
            db.execSQL(stringBuilder.toString());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        }
    }
}
