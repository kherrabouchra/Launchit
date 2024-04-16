package com.example.launchit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReqDetailsActivity extends AppCompatActivity {
    Intent intent;
    long reqID;
    long userId;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.req_details);
        intent = getIntent();
        reqID = intent.getLongExtra("REQ_ID", -1);
        userId = intent.getLongExtra("USER_ID", -1);
        databaseHandler = new DatabaseHandler(this);
        requestReadExternalStoragePermissionIfNeeded();
        //databaseHandler.updateRequestStateToRefused(reqID);
        // Fetch data for reqID using DatabaseHandler
        fetchDataForReqID(reqID);

        Button goBack = findViewById(R.id.GoBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic
                Intent intent = new Intent(ReqDetailsActivity.this, DashboardActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }
        });
    }
    private void requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }
    private void fetchDataForReqID(long reqID) {
        RegisterRequest request = databaseHandler.getDetailsByReqID(reqID);
        if (request != null) {
            updateUI(request);
        }
    }
    private boolean isReadExternalStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadExternalStoragePermissionIfNeeded() {
        if (!isReadExternalStoragePermissionGranted()) {
            // Request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

// Modify getFileNameFromUri and getFileSizeFromUri to use these methods

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;

        try {
            // Check if the app has permission to read external storage
            if (!isReadExternalStoragePermissionGranted()) {
                // Log an error indicating that the permission is denied
                Log.e("getFileNameFromUri", "Permission Denied for READ_EXTERNAL_STORAGE");
                // Request the permission.
                requestReadExternalStoragePermissionIfNeeded();
                // Return a placeholder or handle accordingly since we can't access the file without permission.
                return "Permission Denied";
            }

            // Continue with accessing the file
            String[] projection = {OpenableColumns.DISPLAY_NAME};
            try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(displayNameIndex);
                }
            }
        } catch (Exception e) {
            // Log an error if there's an exception
            Log.e("getFileNameFromUri", "Error getting file name from Uri: " + e.getMessage());
        }

        // Add a log to see the final file name
        Log.d("getFileNameFromUri", "Final File Name: " + fileName);
        return fileName;
    }

    private String getFileSizeFromUri(Uri fileUri) {
        String fileSize = "Unknown Size";

        try {
            // Check if the app has permission to read external storage
            if (!isReadExternalStoragePermissionGranted()) {
                // Log an error indicating that the permission is denied
                Log.e("getFileSizeFromUri", "Permission Denied for READ_EXTERNAL_STORAGE");
                // Request the permission.
                requestReadExternalStoragePermissionIfNeeded();
                // Return a placeholder or handle accordingly since we can't access the file without permission.
                return "Permission Denied";
            }

            // Continue with accessing the file
            InputStream inputStream = getContentResolver().openInputStream(fileUri);

            if (inputStream != null) {
                int size = inputStream.available();
                inputStream.close();
                fileSize = formatFileSize(size);
            }
        } catch (Exception e) {
            // Log an error if there's an exception
            Log.e("getFileSizeFromUri", "Error getting file size from Uri: " + e.getMessage());
        }

        return fileSize;
    }


    private String formatFileSize(long size) {
        if (size <= 0) {
            return "0 B";
        }

        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format(Locale.getDefault(), "%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    private void updateUI(RegisterRequest request) {
        // Update UI elements with data from RegisterRequest
        TextView idTextView = findViewById(R.id.id);
        TextView stateTextView = findViewById(R.id.state);
        TextView SubDateTextView = findViewById(R.id.SubDate);
        TextView ExpDateTextView = findViewById(R.id.ExpDate);
        TextView compTextView = findViewById(R.id.comp);
        TextView compRegNumTextView = findViewById(R.id.compRegNum);
        TextView ownershipTextView = findViewById(R.id.ownership);

        if (request != null) {
            LinearLayout cardLayout = findViewById(R.id.fileLayout);

            try {
                String birthCertPathString = request.getBirthCertPath();
                String natCertPathString = request.getNationalityPath();
                String ninCertPathString = request.getNinPath();
                String compCertString = request.getCompanyCertPath();
                String ownershipCertString = request.getOwnershipPath();
                String cpoCertString = request.getCpoPath();
                // Log file paths for debugging
                Log.d("File Paths", "Birth Cert Path: " + birthCertPathString);
                Log.d("File Paths", "Nat Cert Path: " + natCertPathString);
                Log.d("File Paths", "NIN Cert Path: " + ninCertPathString);

                // Create FileInfo cards only for files with non-null and non-empty URIs and add them to the layout
                if (!TextUtils.isEmpty(birthCertPathString)) {
                    addFileInfoCard(cardLayout, "Birth certification:", birthCertPathString);
                }
                if (!TextUtils.isEmpty(natCertPathString)) {
                    addFileInfoCard(cardLayout,"Nationality certification:", natCertPathString);
                }
                if (!TextUtils.isEmpty(ninCertPathString)) {
                    addFileInfoCard(cardLayout, "ID Card:", ninCertPathString);
                }
                if (!TextUtils.isEmpty(compCertString)) {
                    addFileInfoCard(cardLayout,"Company certification:", compCertString);
                }
                if (!TextUtils.isEmpty(ownershipCertString)) {
                    addFileInfoCard(cardLayout, "Ownership certification", ownershipCertString);
                }
                if (!TextUtils.isEmpty(cpoCertString)) {
                    addFileInfoCard(cardLayout, "Comercial ownership certification", cpoCertString);
                }

                // Add similar code for other files...

            } catch (Exception e) {
                // Handle any unexpected exceptions
                Log.e("Update UI", "Error updating UI: " + e.getMessage());
            }
        } else {
            Log.e("Update UI", "RegisterRequest is null");
        }

        Log.d("reqidd", String.valueOf(request.getReqID()));
        idTextView.setText(String.valueOf(request.getReqID()));
        ownershipTextView.setText(request.getFirstName() + " " + request.getLastName());
        compTextView.setText(request.getCompanyName());
        compRegNumTextView.setText(String.valueOf(request.getCompanyId()));

        // Check the state and update text and color accordingly
        if ("accepted".equals(request.getState())) {
            stateTextView.setText("Accepted");
            stateTextView.setTextColor(ContextCompat.getColor(this, R.color.green));
        } else if ("refused".equals(request.getState())) {
            stateTextView.setText("Refused");
            stateTextView.setTextColor(ContextCompat.getColor(this, R.color.red));
        } else {
        }

        if (request.getSubmissionDate() != null) {
            SubDateTextView.setText(formatDate(request.getSubmissionDate()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(request.getSubmissionDate());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date expectedResultDate = calendar.getTime();

            // Update the UI element for the expected result date
            ExpDateTextView.setText(formatDate(expectedResultDate));
        } else {
            // Handle the case where the submission date is null
            ExpDateTextView.setText("N/A");
        }
    }

    private void addFileInfoCard(LinearLayout cardLayout,String name, String filePathString) {
        Log.d("addFileInfoCard", "File path: " + filePathString);
        if (filePathString != null) {
            Uri filePath = Uri.parse(filePathString);
            Log.d("addFileInfoCard", "Adding card for file: " + filePathString);
            View fileInfoCard = createCardView(filePath, name);
            cardLayout.addView(fileInfoCard);
        } else {
            Log.e("addFileInfoCard", "File path is null. Card not added.");
        }
    }


    private void openFile(Uri fileUri) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if not granted
            requestReadExternalStoragePermission();
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setDataAndType(fileUri, getMimeType(fileUri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(getPackageManager()) != null) {
                Log.d("openFile", "Starting activity to open file: " + fileUri.toString());
                startActivity(intent);
            } else {
                Log.e("openFile", "No suitable app found to handle the file: " + fileUri.toString());
                showNoSuitableAppDialog();
            }
        } catch (Exception e) {
            Log.e("Open File", "Error opening file: " + e.getMessage());
            showErrorDialog();
        }
    }



    private void showNoSuitableAppDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No App Installed")
                .setMessage("No app installed to open the file. Please install a suitable app.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("An error occurred while trying to open the file.")
                .setPositiveButton("OK", null)
                .show();
    }



    private String getMimeType(Uri uri) {
        // ContentResolver is used to get the MIME type of a file based on its URI
        ContentResolver resolver = getContentResolver();
        return resolver.getType(uri);
    }

    private String getUserFullName(long userId) {
        // Implement your logic to retrieve user full name based on userId
        // For example, you might have a method in your DatabaseHandler to get user information
        DatabaseHandler db = new DatabaseHandler(this);
        User user = db.getUserById(userId);

        if (user != null) {
            return (user.getFirstName()+" "+user.getLastName());
        } else {
            return "Unknown User";
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }


    public static String getFileName(Uri uri, Context context) {
        String fileName = null;

        if (uri.getScheme().equals("content") && context != null) {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        fileName = cursor.getString(index);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = uri.getLastPathSegment();
        }

        return fileName;
    }

          @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    private View createCardView(Uri fileUri, String name) {

        if (!isReadExternalStoragePermissionGranted()) {
            // Handle the case where permission is not granted
            Log.e("createCardView", "Read External Storage Permission not granted");
            return null;
        }
        View cardView = LayoutInflater.from(this).inflate(R.layout.fileinfo, null);

        TextView fileTextView = cardView.findViewById(R.id.file);
        TextView filesizeTextView = cardView.findViewById(R.id.filesize);

        // Set data for the views
        // fileTextView.setText(getFileNameFromUri(fileUri));
       filesizeTextView.setText( "file size "+getFileSizeFromUri(fileUri));
              fileTextView.setText(name+" "+String.valueOf(fileUri));
        // Set click listener to open the file when the card is clicked
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(fileUri);
            }
        });

        return cardView;
    }
}
