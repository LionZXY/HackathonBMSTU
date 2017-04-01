package ru.skafcats.hackathon2017.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.skafcats.hackathon2017.enums.InfoType;
import ru.skafcats.hackathon2017.enums.RealmEnum;
import ru.skafcats.hackathon2017.models.InfoAboutSecureInfo;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String TAG = "SQLiteHelper";
    public static final int lastVersion = 1;
    public static final String INFO_ABOUT_INFO_TABLE = "infosecureinfo";

    public SQLiteHelper(Context context) {
        super(context, TAG, null, lastVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + INFO_ABOUT_INFO_TABLE + "(\n" +
                "    " + INFO_ABOUT_INFO.ID_COLUMN + " INT,\n" +
                "    " + INFO_ABOUT_INFO.LAST_EDIT_COLUMN + " TIMESTAMP,\n" +
                "    " + INFO_ABOUT_INFO.LOGIN_COLUMN + " TEXT,\n" +
                "    " + INFO_ABOUT_INFO.SERVICE_COLUMN + " TEXT,\n" +
                "    " + INFO_ABOUT_INFO.NAME_COLUMN + " TEXT,\n" +
                "    " + INFO_ABOUT_INFO.TYPE_COLUMN + " INT\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void put(InfoAboutSecureInfo infoAboutSecureInfo) {
        ContentValues values = new ContentValues();

        values.put(INFO_ABOUT_INFO.ID_COLUMN, infoAboutSecureInfo.getId());
        values.put(INFO_ABOUT_INFO.LAST_EDIT_COLUMN, infoAboutSecureInfo.getLastEdit());
        if (infoAboutSecureInfo.getLogin() != null)
            values.put(INFO_ABOUT_INFO.LOGIN_COLUMN, infoAboutSecureInfo.getLogin());
        if (infoAboutSecureInfo.getLink() != null)
            values.put(INFO_ABOUT_INFO.SERVICE_COLUMN, infoAboutSecureInfo.getLink());
        if (infoAboutSecureInfo.getName() != null)
            values.put(INFO_ABOUT_INFO.NAME_COLUMN, infoAboutSecureInfo.getName());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(INFO_ABOUT_INFO_TABLE, null, values);
        database.close();
    }

    public List<InfoAboutSecureInfo> getInfos() {
        String query = "SELECT * FROM " + INFO_ABOUT_INFO_TABLE;
        ArrayList<InfoAboutSecureInfo> toExit = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();

        InfoAboutSecureInfo infoAboutSecureInfo = null;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            try {
                infoAboutSecureInfo = new InfoAboutSecureInfo(cursor.getLong(cursor.getColumnIndex(INFO_ABOUT_INFO.ID_COLUMN)));
                infoAboutSecureInfo.setLastEdit(cursor.getLong(cursor.getColumnIndex(INFO_ABOUT_INFO.LAST_EDIT_COLUMN)));
                infoAboutSecureInfo.setName(cursor.getString(cursor.getColumnIndex(INFO_ABOUT_INFO.NAME_COLUMN)));
                infoAboutSecureInfo.setLogin(cursor.getString(cursor.getColumnIndex(INFO_ABOUT_INFO.LOGIN_COLUMN)));
                infoAboutSecureInfo.setLink(cursor.getString(cursor.getColumnIndex(INFO_ABOUT_INFO.SERVICE_COLUMN)));
                toExit.add(infoAboutSecureInfo);
            } catch (Exception e) {
                Log.e(TAG, "Error while add from database", e);
            }
        }
        cursor.close();

        database.close();
        return toExit;
    }

    public void updateTable(List<InfoAboutSecureInfo> currentInfo) {
        List<InfoAboutSecureInfo> dbList = getInfos();

    }

    public static final class INFO_ABOUT_INFO implements BaseColumns {
        public static final String ID_COLUMN = "id";
        public static final String LAST_EDIT_COLUMN = "timecode";
        public static final String NAME_COLUMN = "name";
        public static final String LOGIN_COLUMN = "login";
        public static final String TYPE_COLUMN = "type";
        public static final String SERVICE_COLUMN = "service_link";
    }
}
