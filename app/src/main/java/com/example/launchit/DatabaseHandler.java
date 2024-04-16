package com.example.launchit;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 13;
    private static final String DATABASE_NAME = "LaunchitDB";
    //users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone";
   //register_request table
    private static final String TABLE_REGISTER_REQUEST = "register_request";
    private static final String COLUMN_REG_REQ_ID = "_reg_req_id";
    private static final String COLUMN_REG_REQ_STATE = "state";
    private static final String COLUMN_BIRTH_CERT_PATH ="birth_cert_path" ;
    private static final String COLUMN_NATIONALITY_PATH = "nationality_path";
    private static final String COLUMN_NIN_PATH = "nin_path";
    private static final String COLUMN_COMPANY_NAME = "name";
    private static final String COLUMN_COMPANY_ADDRESS = "address";
    private static final String COLUMN_COMPANY_OWNERSHIP = "ownership";
    private static final String COLUMN_COMPANY_REGNUMBER = "regnumber";
    private static final String COLUMN_COMPANY_CERT_PATH = "company_cert_path";
    private static final String COLUMN_OWNERSHIP_PATH = "ownership_path";
    private static final String COLUMN_CPO_PATH = "cpo_path";
    private static final String COLUMN_REG_REQ_FIRST_NAME = "firstname";
    private static final String COLUMN_REG_REQ_LAST_NAME = "lastname";
    private static final String COLUMN_REG_REQ_BIRTHDATE = "birthdate";
    private static final String COLUMN_REG_REQ_BIRTHPLACE = "birthplace";
    private static final String COLUMN_REG_REQ_NATIONALITY = "nationality";
    private static final String COLUMN_REG_REQ_NIN = "nin";
    private static  final String  COLUMN_SUB_DATE = "submission_date";


    //notis


    private static final String TABLE_NOTIFICATIONS = "notifications";  // New table

    // Common column names
    private static final String NOTIF_ID = "id";

    // Users table column names
    private static final String NOTIF_USER = "user";
    private static final String NOTIF_MESSAGE = "message";

    private static final String NOTIF_TIME = "time";
    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_PHONE + " TEXT"
                + ")";

        db.execSQL(createTableQuery);
        // Recreate the table with the updated schema
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                + NOTIF_ID + " INTEGER PRIMARY KEY,"
                + NOTIF_USER + " INTEGER,"
                + NOTIF_MESSAGE + " TEXT,"
                + NOTIF_TIME + " DATETIME,"  // Change the data type to DATETIME
                + "FOREIGN KEY(" + NOTIF_USER + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")" + ")";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void addUser(String firstName, String lastName, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE, phone);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public User getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL, COLUMN_PHONE};
        String selection = COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
            user.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)));
            user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
        }

        if (cursor != null) {
            cursor.close();
        }

        return user;
    }
    @SuppressLint("Range")
    public List<RegisterRequest> getRequestsByUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<RegisterRequest> requestList = new ArrayList<>();

        String[] columns = {
                COLUMN_REG_REQ_ID,
                COLUMN_REG_REQ_STATE,
                COLUMN_SUB_DATE
        };

        String selection = COLUMN_COMPANY_OWNERSHIP + "=?";
        String[] selectionArgs = {String.valueOf(userId)};

        try {
            Cursor cursor = db.query(TABLE_REGISTER_REQUEST, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    RegisterRequest request = new RegisterRequest();
                    request.setReqID(cursor.getLong(cursor.getColumnIndex(COLUMN_REG_REQ_ID)));
                    request.setState(cursor.getString(cursor.getColumnIndex(COLUMN_REG_REQ_STATE)));

                    // Assuming COLUMN_SUB_DATE is of type long in the database
                    long subDateMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_SUB_DATE));
                    Date subDate = new Date(subDateMillis);
                    request.setSubmissionDate(subDate);

                    requestList.add(request);
                } while (cursor.moveToNext());

                cursor.close(); // Close the cursor only after processing the data
            } else {
                Log.d("getRequestsByUser", "Cursor is empty for user ID: " + userId);
            }
        } catch (SQLException e) {
            Log.e("getRequestsByUser", "Error querying database", e);
        }

        return requestList;
    }


    @SuppressLint("Range")
    public RegisterRequest getDetailsByReqID(long reqID) {
        SQLiteDatabase db = this.getReadableDatabase();
        RegisterRequest request = null;

        String[] columns = {
                COLUMN_REG_REQ_ID,
                COLUMN_REG_REQ_FIRST_NAME,
                COLUMN_REG_REQ_LAST_NAME,
                COLUMN_REG_REQ_BIRTHDATE,
                COLUMN_REG_REQ_BIRTHPLACE,
                COLUMN_BIRTH_CERT_PATH,
                COLUMN_REG_REQ_NATIONALITY,
                COLUMN_NATIONALITY_PATH,
                COLUMN_REG_REQ_NIN,
                COLUMN_NIN_PATH,
                COLUMN_COMPANY_NAME,
                COLUMN_COMPANY_REGNUMBER,
                COLUMN_REG_REQ_STATE,
                COLUMN_SUB_DATE,
                COLUMN_BIRTH_CERT_PATH,
                COLUMN_NATIONALITY_PATH,
                COLUMN_NIN_PATH,
                COLUMN_COMPANY_CERT_PATH,
                COLUMN_OWNERSHIP_PATH,
                COLUMN_CPO_PATH
        };

        // Define the selection criteria
        String selection = COLUMN_REG_REQ_ID + " = ?";
        String[] selectionArgs = {String.valueOf(reqID)};

        try (Cursor cursor = db.query(
                TABLE_REGISTER_REQUEST,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                long id = reqID;
                String firstname = cursor.getString(cursor.getColumnIndex(COLUMN_REG_REQ_FIRST_NAME));
                String lastname = cursor.getString(cursor.getColumnIndex(COLUMN_REG_REQ_LAST_NAME));
                String birthDate = cursor.getString(cursor.getColumnIndex(COLUMN_REG_REQ_BIRTHDATE));
                String birthPlace = cursor.getString(cursor.getColumnIndex(COLUMN_REG_REQ_BIRTHPLACE));
                String companyName = cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_NAME));
                long companyId = cursor.getLong(cursor.getColumnIndex(COLUMN_COMPANY_REGNUMBER));
                String nationality = cursor.getString(cursor.getColumnIndex(COLUMN_REG_REQ_NATIONALITY));
                String nin = cursor.getString(cursor.getColumnIndex(COLUMN_REG_REQ_NIN));
                String state = cursor.getString(cursor.getColumnIndex(COLUMN_REG_REQ_STATE));
                long submissionDateMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_SUB_DATE));
                Date submissionDate = (submissionDateMillis != 0) ? new Date(submissionDateMillis) : null;

                String birthCertPathString = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTH_CERT_PATH));
                String nationalityPathString = cursor.getString(cursor.getColumnIndex(COLUMN_NATIONALITY_PATH));
                String ninPathString = cursor.getString(cursor.getColumnIndex(COLUMN_NIN_PATH));
                String compPathString = cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_CERT_PATH));
                String ownpathString = cursor.getString(cursor.getColumnIndex(COLUMN_OWNERSHIP_PATH));
                String cpoPathString = cursor.getString(cursor.getColumnIndex(COLUMN_CPO_PATH));

                Uri birthCertPath = (birthCertPathString != null) ? Uri.parse(birthCertPathString) : null;
                Uri nationalityPath = (nationalityPathString != null) ? Uri.parse(nationalityPathString) : null;
                Uri ninPath = (ninPathString != null) ? Uri.parse(ninPathString) : null;
                Uri compPath = (compPathString != null) ? Uri.parse(compPathString) : null;
                Uri ownpath = (ownpathString != null) ? Uri.parse(ownpathString) : null;
                Uri cpopath = (cpoPathString != null) ? Uri.parse(cpoPathString) : null;

                request = new RegisterRequest(id, firstname, lastname, birthDate, birthPlace, birthCertPath, companyName, companyId, compPath, ownpath, cpopath, nationality, nationalityPath, nin, ninPath, state, submissionDate);
            } else {
                Log.e("getDetailsByReqID", "Cursor is empty for reqID: " + reqID);
            }
        } catch (Exception e) {
            Log.e("getDetailsByReqID", "Error querying database: " + e.getMessage());
        } finally {
            // Close the database connection
            db.close();
        }

        return request;
    }

    @SuppressLint("Range")
    public User getUserById(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL, COLUMN_PHONE};
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)));
            user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));

            Log.d("getUserById", "User found with ID: " + userId);
            Log.d("getUserById", "First Name: " + user.getFirstName());
            Log.d("getUserById", "Last Name: " + user.getLastName());
            Log.d("getUserById", "Email: " + user.getEmail());
            Log.d("getUserById", "Phone Number: " + user.getPhoneNumber());
        } else {
            Log.d("getUserById", "User not found with ID: " + userId);
        }

        if (cursor != null) {
            cursor.close();
        }

        return user;
    }


    public long addRequest(long id, String fn, String ln, String bdate, String bplace, Uri bcert,
                           String nat, Uri natcert, String nin, Uri nincert, String compname,
                           String compAdd, String regnum, Uri compPath, Uri OwnPath,
                           String Cpo, Uri CpoPath) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COMPANY_OWNERSHIP, id);
        values.put(COLUMN_REG_REQ_FIRST_NAME, fn);
        values.put(COLUMN_REG_REQ_LAST_NAME, ln);
        values.put(COLUMN_REG_REQ_BIRTHDATE, bdate);
        values.put(COLUMN_REG_REQ_BIRTHPLACE, bplace);

        // Check if bcert is not null before calling toString()
        values.put(COLUMN_BIRTH_CERT_PATH, bcert != null ? bcert.toString() : "");

        values.put(COLUMN_REG_REQ_NATIONALITY, nat);

        // Check if natcert is not null before calling toString()
        values.put(COLUMN_NATIONALITY_PATH, natcert != null ? natcert.toString() : "");

        values.put(COLUMN_REG_REQ_NIN, nin);

        // Check if nincert is not null before calling toString()
        values.put(COLUMN_NIN_PATH, nincert != null ? nincert.toString() : "");

        values.put(COLUMN_SUB_DATE, new Date().getTime());
        values.put(COLUMN_REG_REQ_STATE, "pending");
        values.put(COLUMN_COMPANY_NAME, compname);
        values.put(COLUMN_COMPANY_ADDRESS, compAdd);
        values.put(COLUMN_COMPANY_REGNUMBER, regnum);

        // Check if compPath is not null before calling toString()
        values.put(COLUMN_COMPANY_CERT_PATH, compPath != null ? compPath.toString() : "");
        values.put(COLUMN_OWNERSHIP_PATH, OwnPath != null ? OwnPath.toString() : "");

        // Check if CpoPath is not null before calling toString()
        values.put(COLUMN_CPO_PATH, CpoPath != null ? CpoPath.toString() : "");

        // Insert the row
        long requestId = db.insert(TABLE_REGISTER_REQUEST, null, values);

        // Close the database connection
        db.close();

        // Return the request ID
        return requestId;
    }

    public void updateRequestStateToAccepted(long reqID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set the new state to "accepted"
        values.put(COLUMN_REG_REQ_STATE, "accepted");

        // Define the WHERE clause to update the specific request ID
        String whereClause = COLUMN_REG_REQ_ID + "=?";
        String[] whereArgs = {String.valueOf(reqID)};

        // Update the row in the database
        int rowsAffected = db.update(TABLE_REGISTER_REQUEST, values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Log.d("updateRequest", "Request state updated to accepted for reqID: " + reqID);

            // Construct the message using reqID and submission date
            String message = "Your request (ID: " + reqID + ") has been accepted.";
            insertNotification(reqID, message);
        } else {
            Log.e("updateRequest", "Failed to update request state for reqID: " + reqID);
        }

        // Close the database connection
        db.close();
    }

    public void updateRequestStateToRefused(long reqID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set the new state to "refused"
        values.put(COLUMN_REG_REQ_STATE, "refused");

        // Define the WHERE clause to update the specific request ID
        String whereClause = COLUMN_REG_REQ_ID + "=?";
        String[] whereArgs = {String.valueOf(reqID)};

        // Update the row in the database
        int rowsAffected = db.update(TABLE_REGISTER_REQUEST, values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Log.d("updateRequest", "Request state updated to refused for reqID: " + reqID);

            // Construct the message using reqID and submission date
            String message = "Your request (ID: " + reqID + ") has been refused.";
            insertNotification(reqID, message);
        } else {
            Log.e("updateRequest", "Failed to update request state for reqID: " + reqID);
        }

        // Close the database connection
        db.close();
    }

    private void insertNotification(long reqID, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Get the user ID associated with the request
        String[] columns = {COLUMN_COMPANY_OWNERSHIP};
        String selection = COLUMN_REG_REQ_ID + "=?";
        String[] selectionArgs = {String.valueOf(reqID)};
        Cursor cursor = db.query(TABLE_REGISTER_REQUEST, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") long userId = cursor.getLong(cursor.getColumnIndex(COLUMN_COMPANY_OWNERSHIP));
            Date currentDate = new Date();

            // Format the date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String formattedDate = sdf.format(currentDate);

            // Insert notification for the user
            values.put(NOTIF_USER, userId);
            values.put(NOTIF_MESSAGE, message);
            values.put(NOTIF_TIME, formattedDate);

            db.insert(TABLE_NOTIFICATIONS, null, values);

            Log.d("insertNotification", "Notification inserted for user ID: " + userId);
        } else {
            Log.e("insertNotification", "Failed to insert notification. User ID not found for reqID: " + reqID);
        }

        // Close the cursor
        if (cursor != null) {
            cursor.close();
        }

        // Close the database connection
        db.close();
    }



    @SuppressLint("Range")
    public List<Notification> getNotificationsByUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Notification> notificationList = new ArrayList<>();

        String[] columns = {
                NOTIF_ID,
                NOTIF_USER,
                NOTIF_MESSAGE,
                NOTIF_TIME
        };

        String selection = NOTIF_USER + "=?";
        String[] selectionArgs = {String.valueOf(userId)};

        try {
            Cursor cursor = db.query(TABLE_NOTIFICATIONS, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do { long timestampMillis = cursor.getLong(cursor.getColumnIndex(NOTIF_TIME));
                    Date notificationTime = new Date(timestampMillis);
                    Notification notification = new Notification(
                            cursor.getLong(cursor.getColumnIndex(NOTIF_ID)),
                            cursor.getLong(cursor.getColumnIndex(NOTIF_USER)),
                            cursor.getString(cursor.getColumnIndex(NOTIF_MESSAGE)), notificationTime
                     );

                    notificationList.add(notification);
                } while (cursor.moveToNext());

                cursor.close(); // Close the cursor only after processing the data
            } else {
                Log.d("getNotificationsByUser", "Cursor is empty for user ID: " + userId);
            }
        } catch (SQLException e) {
            Log.e("getNotificationsByUser", "Error querying database", e);
        }

        return notificationList;
    }

}
