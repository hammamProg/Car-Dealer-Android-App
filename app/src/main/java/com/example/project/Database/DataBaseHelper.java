package com.example.project.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.project.Objects.Car;
import com.example.project.Objects.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private Context context;
    // $ Database name and version
    private static final String DATABASE_NAME = "Database_H&H";
    private static final int DATABASE_VERSION = 1;
    // ========================================================
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


    // Table name and column names for car information
    private static final String TABLE_CARS = "cars";
    private static final String COLUMN_CAR_ID = "id";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_CAR_TYPE = "type";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_CAR_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";

    private static final String TABLE_FAVORITE_CARS = "favorite_cars";
    private static final String COLUMN_FAVORITE_ID = "favorite_id";
    private static final String COLUMN_USER_EMAIL = "user_email";  // Foreign key referencing users.email
    private static final String COLUMN_CAR_ID_FK = "car_id";       // Foreign key referencing cars.id

    // SQL query to create the cars table
    private static final String CREATE_TABLE_CARS =
            "CREATE TABLE " + TABLE_CARS + "(" +
                    COLUMN_CAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_BRAND + " TEXT," +
                    COLUMN_MODEL + " TEXT," +
                    COLUMN_CAR_TYPE + " TEXT," +
                    COLUMN_YEAR + " TEXT," +
                    COLUMN_COLOR + " TEXT," +
                    COLUMN_CAR_PRICE + " REAL," +
                    COLUMN_IMAGE + " TEXT" +
                    ")";

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
                    COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0" +
                    ")";

    // SQL query to create the favorite_cars table
    private static final String CREATE_TABLE_FAVORITE_CARS =
            "CREATE TABLE " + TABLE_FAVORITE_CARS + "(" +
                    COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USER_EMAIL + " TEXT," +
                    COLUMN_CAR_ID_FK + " INTEGER," +
                    "FOREIGN KEY(" + COLUMN_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + ")," +
                    "FOREIGN KEY(" + COLUMN_CAR_ID_FK + ") REFERENCES " + TABLE_CARS + "(" + COLUMN_CAR_ID + ")" +
                    ")";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CARS);
        db.execSQL(CREATE_TABLE_FAVORITE_CARS);
        Toast.makeText(context, "DataBase Initialized Success!", Toast.LENGTH_SHORT).show();
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    //    ================================  Car Methods  ===========================================


    // Method to add a new car to the database
    public long addCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_BRAND, car.getBrand());
        values.put(COLUMN_MODEL, car.getModel());
        values.put(COLUMN_CAR_TYPE, car.getType());
        values.put(COLUMN_YEAR, car.getYear());
        values.put(COLUMN_COLOR, car.getColor());
        values.put(COLUMN_CAR_PRICE, car.getPrice());
        values.put(COLUMN_IMAGE, car.getImage());

        long result = db.insert(TABLE_CARS, null, values);
        db.close();
        return result;
    }

    // Method to get all cars from the database
    public List<Car> getAllCars() {
        List<Car> carList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_CAR_ID, COLUMN_BRAND, COLUMN_MODEL, COLUMN_CAR_TYPE, COLUMN_YEAR, COLUMN_COLOR, COLUMN_CAR_PRICE, COLUMN_IMAGE};

        try (Cursor cursor = db.query(TABLE_CARS, columns, null, null, null, null, null)) {
            int carIdIndex = cursor.getColumnIndex(COLUMN_CAR_ID);
            int brandIndex = cursor.getColumnIndex(COLUMN_BRAND);
            int modelIndex = cursor.getColumnIndex(COLUMN_MODEL);
            int typeIndex = cursor.getColumnIndex(COLUMN_CAR_TYPE);
            int yearIndex = cursor.getColumnIndex(COLUMN_YEAR);
            int colorIndex = cursor.getColumnIndex(COLUMN_COLOR);
            int priceIndex = cursor.getColumnIndex(COLUMN_CAR_PRICE);
            int imageIndex = cursor.getColumnIndex(COLUMN_IMAGE);

            while (cursor.moveToNext()) {
                int carId = cursor.getInt(carIdIndex);
                String brand = cursor.getString(brandIndex);
                String model = cursor.getString(modelIndex);
                String type = cursor.getString(typeIndex);
                String year = cursor.getString(yearIndex);
                String color = cursor.getString(colorIndex);
                double price = cursor.getDouble(priceIndex);
                String image = cursor.getString(imageIndex);

                Car car = new Car(carId, brand, model, type, year, color, price, image);
                carList.add(car);
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching all cars", e);
        } finally {
            db.close();
        }

        return carList;
    }

    // Method to check if a car with the given ID exists in the database
    public boolean checkCarExistence(int carId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_CAR_ID};
        String selection = COLUMN_CAR_ID + " = ?";
        String[] selectionArgs = {String.valueOf(carId)};

        Cursor cursor = db.query(TABLE_CARS, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        // If count is greater than 0, the car with the given ID exists
        return count > 0;
    }

    //    ================================  Favorite Methods  ===========================================


    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception based on your application's needs
            return null;
        }
    }

    public boolean checkEmailExistence(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EMAIL};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        // If count is greater than 0, the email already exists
        return count > 0;
    }

    // Method to add a new user to the database
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_PASSWORD, hashPassword(user.getPassword()));
        values.put(COLUMN_COUNTRY, user.getCountry());
        values.put(COLUMN_CITY, user.getCity());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());

        long result = db.insertWithOnConflict(TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return result;
    }

    // Method to check if a user with the given email and password exists in the database
    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EMAIL, COLUMN_IS_LOGGED_IN};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, hashPassword(password)};

        Log.d("email", email);
        Log.d("password", hashPassword(password));

        try (Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)) {
            if (cursor.moveToFirst()) {
                // User with the provided email and password exists
                int isLoggedInColumnIndex = cursor.getColumnIndex(COLUMN_IS_LOGGED_IN);

                if (isLoggedInColumnIndex != -1) {
                    int isLoggedIn = cursor.getInt(isLoggedInColumnIndex);

                    if (isLoggedIn == 0) {
                        // Update the user's login status to 1
                        ContentValues values = new ContentValues();
                        values.put(COLUMN_IS_LOGGED_IN, 1);

                        db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
                        Log.d("db", "updated");

                        // Return true to indicate successful login
                        return true;
                    } else {
                        // User is already logged in
                        Log.d("db", "User is already logged in");

                        // Return true to indicate that the user is already logged in
                        return true;
                    }
                } else {
                    // Handle the case where the column index is not found
                    Log.e("db", "Column index for COLUMN_IS_LOGGED_IN not found");

                    // Return false to indicate login failure
                    return false;
                }
            } else {
                // No user found with the provided email and password
                Log.d("db", "No user found with the provided email and password");

                // Return false to indicate login failure
                return false;
            }
        } finally {
            db.close();
        }
    }


    // Method to check if a user with the given email and password exists in the database
    public boolean getAllUsers(String email, String password) {
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

    // Method to get all users from the database
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_GENDER,
                COLUMN_COUNTRY, COLUMN_CITY, COLUMN_PHONE_NUMBER};

        try (Cursor cursor = db.query(TABLE_USERS, columns, null, null, null, null, null)) {
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int firstNameIndex = cursor.getColumnIndex(COLUMN_FIRST_NAME);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int lastNameIndex = cursor.getColumnIndex(COLUMN_LAST_NAME);
            int genderIndex = cursor.getColumnIndex(COLUMN_GENDER);
            int countryIndex = cursor.getColumnIndex(COLUMN_COUNTRY);
            int cityIndex = cursor.getColumnIndex(COLUMN_CITY);
            int phoneNumberIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                String email = cursor.getString(emailIndex);
                String password = cursor.getString(passwordIndex);
                String firstName = cursor.getString(firstNameIndex);
                String lastName = cursor.getString(lastNameIndex);
                String gender = cursor.getString(genderIndex);
                String country = cursor.getString(countryIndex);
                String city = cursor.getString(cityIndex);
                String phoneNumber = cursor.getString(phoneNumberIndex);

//                TODO
//                List<Car> favoriteCars = null;

                User user = new User(email, firstName, lastName, gender, password, country, city, phoneNumber);
                userList.add(user);
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching all users", e);
        } finally {
            db.close();
        }

        return userList;
    }


}
