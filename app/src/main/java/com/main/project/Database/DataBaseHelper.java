package com.main.project.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.main.project.Objects.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DataBaseHelper extends SQLiteOpenHelper {
    private final Context context;

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
    private static final String COLUMN_REMEMBER_ME = "remember_me";

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

    // Table name and column names for favorite cars
    private static final String TABLE_FAVORITE_CARS = "favorite_cars";
    private static final String COLUMN_FAVORITE_ID = "favorite_id";
    private static final String COLUMN_USER_EMAIL = "user_email";  // Foreign key referencing users.email
    private static final String COLUMN_CAR_ID_FK = "car_id";       // Foreign key referencing cars.id

    // Table name and column names for reservation information
    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String COLUMN_RESERVATION_ID = "reservation_id";
    private static final String COLUMN_USER_EMAIL_RESERVATION = "user_email";  // Foreign key referencing users.email
    private static final String COLUMN_CAR_ID_RESERVATION = "car_id";           // Foreign key referencing cars.id
    private static final String COLUMN_RESERVATION_DATE = "reservation_date";

    // SQL query to create the reservations table
    private static final String CREATE_TABLE_RESERVATIONS =
            "CREATE TABLE " + TABLE_RESERVATIONS + "(" +
                    COLUMN_RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USER_EMAIL_RESERVATION + " TEXT," +
                    COLUMN_CAR_ID_RESERVATION + " INTEGER," +
                    COLUMN_RESERVATION_DATE + " TEXT," +
                    "FOREIGN KEY(" + COLUMN_USER_EMAIL_RESERVATION + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + ")," +
                    "FOREIGN KEY(" + COLUMN_CAR_ID_RESERVATION + ") REFERENCES " + TABLE_CARS + "(" + COLUMN_CAR_ID + ")" +
                    ")";

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
                    COLUMN_REMEMBER_ME + " INTEGER DEFAULT 0" +
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
        db.execSQL(CREATE_TABLE_RESERVATIONS);
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
        String[] columns = {COLUMN_CAR_ID, COLUMN_BRAND, COLUMN_MODEL, COLUMN_CAR_TYPE, COLUMN_YEAR, COLUMN_COLOR, COLUMN_CAR_PRICE, COLUMN_IMAGE};

        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_CARS, columns, null, null, null, null, null)) {
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
        }

        return carList;
    }

    // Method to get a car by its ID
    public Car getCarById(int carId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_CAR_ID, COLUMN_BRAND, COLUMN_MODEL, COLUMN_CAR_TYPE, COLUMN_YEAR, COLUMN_COLOR, COLUMN_CAR_PRICE, COLUMN_IMAGE};
        String selection = COLUMN_CAR_ID + " = ?";
        String[] selectionArgs = {String.valueOf(carId)};

        Car car = null;

        try (Cursor cursor = db.query(TABLE_CARS, columns, selection, selectionArgs, null, null, null)) {
            int carIdIndex = cursor.getColumnIndex(COLUMN_CAR_ID);
            int brandIndex = cursor.getColumnIndex(COLUMN_BRAND);
            int modelIndex = cursor.getColumnIndex(COLUMN_MODEL);
            int typeIndex = cursor.getColumnIndex(COLUMN_CAR_TYPE);
            int yearIndex = cursor.getColumnIndex(COLUMN_YEAR);
            int colorIndex = cursor.getColumnIndex(COLUMN_COLOR);
            int priceIndex = cursor.getColumnIndex(COLUMN_CAR_PRICE);
            int imageIndex = cursor.getColumnIndex(COLUMN_IMAGE);

            if (cursor.moveToFirst()) {
                // Retrieve car details
                int retrievedCarId = cursor.getInt(carIdIndex);
                String brand = cursor.getString(brandIndex);
                String model = cursor.getString(modelIndex);
                String type = cursor.getString(typeIndex);
                String year = cursor.getString(yearIndex);
                String color = cursor.getString(colorIndex);
                double price = cursor.getDouble(priceIndex);
                String image = cursor.getString(imageIndex);

                car = new Car(retrievedCarId, brand, model, type, year, color, price, image);
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching car by ID", e);
        } finally {
            db.close();
        }

        return car;
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

    //Method to check if car is already in favorite list
    public boolean checkIfCarIsInFavorite(String userEmail, int carId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_CAR_ID_FK, COLUMN_USER_EMAIL};
        String selection = COLUMN_CAR_ID_FK + " = ? AND " + COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {String.valueOf(carId), userEmail};

        Cursor cursor = db.query(TABLE_FAVORITE_CARS, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        // If count is greater than 0, the car with the given ID exists
        return count > 0;
    }

    // Method to add a new favorite car record
    public long addFavoriteCar(String userEmail, int carId) {
        if(checkIfCarIsInFavorite(userEmail,carId)) return -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_EMAIL, userEmail);
        values.put(COLUMN_CAR_ID_FK, carId);

        long result = db.insert(TABLE_FAVORITE_CARS, null, values);
        db.close();
        return result;
    }

    // Method to delete a favorite car by car ID and user email
    public int deleteFavoriteCar(String userEmail, int carId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_CAR_ID_FK + " = ?";
        String[] whereArgs = {userEmail, String.valueOf(carId)};

        int result = db.delete(TABLE_FAVORITE_CARS, whereClause, whereArgs);
        db.close();
        return result;
    }

    // Method to get all favorite cars as List<Car> for a specific user email
    public List<Car> getFavoriteCars(String userEmail) {
        List<Car> favoriteCars = new ArrayList<>();

        String query = "SELECT " + TABLE_CARS + ".* FROM " + TABLE_CARS +
                " INNER JOIN " + TABLE_FAVORITE_CARS +
                " ON " + TABLE_CARS + "." + COLUMN_CAR_ID + " = " + TABLE_FAVORITE_CARS + "." + COLUMN_CAR_ID_FK +
                " WHERE " + TABLE_FAVORITE_CARS + "." + COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};
        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.rawQuery(query, selectionArgs)) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int carId = cursor.getInt(cursor.getColumnIndex(COLUMN_CAR_ID));
                @SuppressLint("Range") String brand = cursor.getString(cursor.getColumnIndex(COLUMN_BRAND));
                @SuppressLint("Range") String model = cursor.getString(cursor.getColumnIndex(COLUMN_MODEL));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(COLUMN_CAR_TYPE));
                @SuppressLint("Range") String year = cursor.getString(cursor.getColumnIndex(COLUMN_YEAR));
                @SuppressLint("Range") String color = cursor.getString(cursor.getColumnIndex(COLUMN_COLOR));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_CAR_PRICE));
                @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));

                Car car = new Car(carId, brand, model, type, year, color, price, image);
                favoriteCars.add(car);
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching favorite cars", e);
        }

        return favoriteCars;
    }


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
    public boolean loginUser(String email, String password, boolean rememberMe) {

        String[] columns = {COLUMN_EMAIL, COLUMN_PASSWORD};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, hashPassword(password)};
        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)) {
            Log.d("email", email);
            Log.d("password", Objects.requireNonNull(hashPassword(password)));
            if (cursor.moveToFirst()) {
                // User with the provided email and password exists

                // Update the user's rememberMe status if requested
                if (rememberMe) {
                    ContentValues values = new ContentValues();
                    // Assuming COLUMN_REMEMBER_ME is a new column added to the users table
                    values.put(COLUMN_REMEMBER_ME, 1);
                    db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
                    Log.d("db", "Remember me updated");
                }

                Log.d("db", "Login successful");
                return true;
            } else {
                // No user found with the provided email and password
                Log.d("db", "No user found with the provided email and password");

                // Return false to indicate login failure
                return false;
            }
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

        String[] columns = {COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_GENDER,
                COLUMN_COUNTRY, COLUMN_CITY, COLUMN_PHONE_NUMBER, COLUMN_REMEMBER_ME};
        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_USERS, columns, null, null, null, null, null)) {
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int firstNameIndex = cursor.getColumnIndex(COLUMN_FIRST_NAME);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int lastNameIndex = cursor.getColumnIndex(COLUMN_LAST_NAME);
            int genderIndex = cursor.getColumnIndex(COLUMN_GENDER);
            int countryIndex = cursor.getColumnIndex(COLUMN_COUNTRY);
            int cityIndex = cursor.getColumnIndex(COLUMN_CITY);
            int phoneNumberIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);
            int rememberMeIndex = cursor.getColumnIndex(COLUMN_REMEMBER_ME);

            while (cursor.moveToNext()) {
                String email = cursor.getString(emailIndex);
                String password = cursor.getString(passwordIndex);
                String firstName = cursor.getString(firstNameIndex);
                String lastName = cursor.getString(lastNameIndex);
                String gender = cursor.getString(genderIndex);
                String country = cursor.getString(countryIndex);
                String city = cursor.getString(cityIndex);
                String phoneNumber = cursor.getString(phoneNumberIndex);
                int rememberMe = cursor.getInt(rememberMeIndex);
                boolean rememberMe_bool = rememberMe == 1;
                List<Car> favoriteCars = getFavoriteCars(email);
                User user = new User(email, firstName, lastName, gender, password, country, city, phoneNumber, rememberMe_bool, favoriteCars);

                // Set the rememberMe field in the User object
                user.setRememberMe(rememberMe == 1);

                userList.add(user);
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching all users", e);
        }

        return userList;
    }

    // Method to get a user by email
    public User getUserByEmail(String email) {

        String[] columns = {
                COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_GENDER,
                COLUMN_COUNTRY, COLUMN_CITY, COLUMN_PHONE_NUMBER, COLUMN_REMEMBER_ME
        };
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String retrievedEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String retrievedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
                @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
                @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
                @SuppressLint("Range") String country = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY));
                @SuppressLint("Range") String city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER));
                @SuppressLint("Range") int rememberMeIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_REMEMBER_ME));

                boolean rememberMe_bool = (rememberMeIndex == 1);

                List<Car> favoriteCars = getFavoriteCars(retrievedEmail);

                return new User(retrievedEmail, firstName, lastName, gender, retrievedPassword, country, city, phoneNumber, rememberMe_bool, favoriteCars);
            } else {
                return null; // User not found
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching user by email", e);
            return null;
        }
    }

    //    ==================================================================================================
    //    ================================  Reservation Methods  ===========================================


    // Method to reserve a car
    public long reserveCar(String userEmail, int carId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Get the current date and time in the required format
        String reservationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        values.put(COLUMN_USER_EMAIL_RESERVATION, userEmail);
        values.put(COLUMN_CAR_ID_RESERVATION, carId);
        values.put(COLUMN_RESERVATION_DATE, reservationDate);

        long result = db.insert(TABLE_RESERVATIONS, null, values);
        db.close();
        return result;
    }

    // Method to get all reservations for a user with associated car details
    public List<Car> getUserReservations(String userEmail) {
        List<Car> reservedCars = new ArrayList<>();

        String[] columns = {COLUMN_CAR_ID_RESERVATION, COLUMN_RESERVATION_DATE};
        String selection = COLUMN_USER_EMAIL_RESERVATION + " = ?";
        String[] selectionArgs = {userEmail};
        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_RESERVATIONS, columns, selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int carId = cursor.getInt(cursor.getColumnIndex(COLUMN_CAR_ID_RESERVATION));
                @SuppressLint("Range") String reservationDate = cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_DATE));

                // Retrieve car details for each reservation
                Car car = getCarById(carId);

                if (car != null) {
                    // Set the reservation date for the reserved car
                    car.setReservationDate(reservationDate);

                    // Add the reserved car to the list
                    reservedCars.add(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservedCars;
    }

// Cars price between min and max

    public List<Car> getCarsByPrice(double min, double max) {
        List<Car> carList = new ArrayList<>();
        String[] columns = {COLUMN_CAR_ID, COLUMN_BRAND, COLUMN_MODEL, COLUMN_CAR_TYPE, COLUMN_YEAR, COLUMN_COLOR, COLUMN_CAR_PRICE, COLUMN_IMAGE};

        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_CARS, columns, COLUMN_CAR_PRICE + " BETWEEN ? AND ?", new String[]{String.valueOf(min), String.valueOf(max)}, null, null, null)) {
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
        }

        return carList;
    }

}
