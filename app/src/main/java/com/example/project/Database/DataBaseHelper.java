package com.example.project.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.project.Objects.User;

public class DataBaseHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "Database_H&H";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names for user information
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_IS_LOGGED_IN = "is_logged_in";

    // SQL query to create the users table
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_EMAIL + " TEXT PRIMARY KEY," +
                    COLUMN_FIRST_NAME + " TEXT," +
                    COLUMN_LAST_NAME + " TEXT," +
                    COLUMN_GENDER + " TEXT," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_COUNTRY + " TEXT," +
                    COLUMN_CITY + " TEXT," +
                    COLUMN_PHONE_NUMBER + " TEXT," +
                    COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0" +  // 0 for false, 1 for true
                    ")";



    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to add a new user to the database
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_COUNTRY, user.getCountry());
        values.put(COLUMN_CITY, user.getCity());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());

        long result = db.insertWithOnConflict(TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return result;
    }

    // Method to check if a user with the given email and password exists in the database
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EMAIL};
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }
}
