package com.example.launchit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FormActivity extends AppCompatActivity {
    private CardView form1Card, form2Card, form3Card;
    private static final int FILE_PICKER_BIRTH_CERT_REQUEST_CODE = 1;
    private static final int FILE_PICKER_NATIONALITY_REQUEST_CODE = 2; // Change the value as needed
    private static final int FILE_PICKER_NIN_REQUEST_CODE = 3; // Change the value as needed

    private static final int FILE_PICKER_COMPANY_CERT_REQUEST_CODE = 4;
    private static final int FILE_PICKER_OWNERSHIP_REQUEST_CODE = 5;
    private static final int FILE_PICKER_CPO_REQUEST_CODE = 6;

    private Uri companyCertFilePath ;
    private Uri ownershipFilePath ;
    private Uri cpoFilePath;


    private Uri birthCertFilePath ;
    private Uri nationalityFilePath;
    private Uri ninFilePath ;

    private String firstName;
    private String lastName;
    private String birthdateStr;
    private String birthplaceStr;
    private String nationalityStr;
    private String ninStr;
    private String companyName;
    private String companyWilaya;
    private String companyAddress;
    private String registrationNumber;
    private String ownershipInfo;
    private String cpoInfo;
    private int currentFilePickerRequestCode = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        // Request permission at runtime
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        Intent intent = getIntent();
        long userId = intent.getLongExtra("USER_ID", -1);



        // Initialize CardViews
        form1Card = findViewById(R.id.form1);
        form2Card = findViewById(R.id.form2);
        form3Card = findViewById(R.id.form3);

        // Set onClickListener for StartBtn
        Button startBtn = findViewById(R.id.StartBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make the form1 CardView visible when StartBtn is clicked
                form1Card.setVisibility(View.VISIBLE);
            }
        });

        // Set onClickListener for nextBtn1
        Button nextBtn1 = findViewById(R.id.nextBtn1);
        nextBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields(1)){
                    collectDataFromForm1();
                    form2Card.setVisibility(View.VISIBLE);

                    displayCollectedData();

                }

            }
        });

        // Set onClickListener for nextBtn2
        Button nextBtn2 = findViewById(R.id.nextBtn2);
        nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(validateFields(2)){
                 collectDataFromForm2();

                 form3Card.setVisibility(View.VISIBLE);


                 displayCollectedData();
             }


            }
        });
// Set onClickListener for submitBtn
        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate fields before displaying the collected data
              if(validateFields(3)){

                  DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                  long requestId = db.addRequest(userId,firstName, lastName, birthdateStr, birthplaceStr, birthCertFilePath,
                          nationalityStr, nationalityFilePath, ninStr, ninFilePath, companyName, companyAddress,
                          registrationNumber, companyCertFilePath, ownershipFilePath, cpoInfo, cpoFilePath);

                  if (requestId != -1) {
                      Log.d("suceesss", String.valueOf(requestId));
                      Toast.makeText(FormActivity.this, "Successfully sent!", Toast.LENGTH_SHORT).show();

                      Intent intent = new Intent(FormActivity.this, DashboardActivity.class);
                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                      intent.putExtra("USER_ID", userId) ;
                      startActivity(intent);
                      finish();
                  }else Log.d("failure", "not added ");
              }

              }

        });




        // Set onClickListeners for exit buttons
        ImageButton exit1 = findViewById(R.id.exit1);
        ImageButton exit2 = findViewById(R.id.exit2);
        ImageButton exit3 = findViewById(R.id.exit3);
        ImageButton exit4 = findViewById(R.id.exit4);
        exit1.setOnClickListener(exitButtonClickListener);
        exit2.setOnClickListener(exitButtonClickListener);
        exit3.setOnClickListener(exitButtonClickListener);
        exit4.setOnClickListener(exitButtonClickListener);

// Update your button click listeners
        Button birthBtn = findViewById(R.id.birthbtn);
        birthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilePicker("*/*", FILE_PICKER_BIRTH_CERT_REQUEST_CODE);
            }
        });

        Button natBtn = findViewById(R.id.natBtn);
        natBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilePicker("*/*", FILE_PICKER_NATIONALITY_REQUEST_CODE);
            }
        });

        Button ninBtn = findViewById(R.id.ninBtn);
        ninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilePicker("*/*", FILE_PICKER_NIN_REQUEST_CODE);
            }
        });

        Button compBtn = findViewById(R.id.CompBtn);
        compBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilePicker("*/*", FILE_PICKER_COMPANY_CERT_REQUEST_CODE);
            }
        });

        Button ownBtn = findViewById(R.id.ownBtn);
        ownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilePicker("*/*", FILE_PICKER_OWNERSHIP_REQUEST_CODE);
            }
        });

        Button cpoBtn = findViewById(R.id.cpoBtn);
        cpoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilePicker("*/*", FILE_PICKER_CPO_REQUEST_CODE);
            }
        });
        // Set up Spinner for company wilaya
        Spinner compWilayaSpinner = findViewById(R.id.CompWilaya);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.wilayas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        compWilayaSpinner.setAdapter(adapter);




    }


    // Common OnClickListener for exit buttons
    private View.OnClickListener exitButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent newIntent = new Intent(FormActivity.this, DashboardActivity.class);
            Intent originalIntent = getIntent();
            long userId = originalIntent.getLongExtra("USER_ID", -1);

            newIntent.putExtra("USER_ID", userId);
            startActivity(newIntent);
        }
    };

    // Method to collect data from form1
    private void collectDataFromForm1() {
        // Get values from input fields
        EditText firstname = findViewById(R.id.firstname);
        EditText lastname = findViewById(R.id.lastname);
        EditText birthdate = findViewById(R.id.birthdate);
        EditText birthplace = findViewById(R.id.birthplace);
        EditText nationality = findViewById(R.id.nationality);
        EditText nin = findViewById(R.id.NIN);
        Spinner w = findViewById(R.id.Wilaya);


        String wilaya = w.getSelectedItem().toString();
       firstName = firstname.getText().toString().trim();
         lastName = lastname.getText().toString().trim();
          birthplaceStr = birthplace.getText().toString().trim()+ " - " + wilaya;
       nationalityStr = nationality.getText().toString().trim();
          ninStr = nin.getText().toString().trim();

        birthdateStr = birthdate.getText().toString().trim() + " 00:00:00"; // Assuming time is not relevant

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = inputFormat.parse(birthdateStr);
            birthdateStr = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    private void collectDataFromForm2() {
        // Get values from form2 input fields
        EditText compName = findViewById(R.id.compName);
        EditText compAddress = findViewById(R.id.compaddress);
        Spinner compWilayaSpinner = findViewById(R.id.CompWilaya);
        EditText regNum = findViewById(R.id.regnum);
        EditText ownership = findViewById(R.id.ownership);
        EditText cpo = findViewById(R.id.cpo);
        TextView compnameView = findViewById(R.id.compnameView);
        TextView compaddrView = findViewById(R.id.compaddressView);
        TextView regnumView = findViewById(R.id.compRegNumView);
        TextView ownedView = findViewById(R.id.ownershipView);
        TextView fullnameView = findViewById(R.id.fullnameView);
        TextView birthdateView = findViewById(R.id.birthdateView);
        TextView birthplaceView = findViewById(R.id.birthplaceView);
        TextView nationalityView = findViewById(R.id.nationalityView);


        TextView ninView = findViewById(R.id.ninView);
        fullnameView.setText(firstName+" "+ lastName);
        birthdateView.setText(birthdateStr);
        birthplaceView.setText(birthplaceStr);
        nationalityView.setText(nationalityStr);
        ninView.setText(ninStr);
        companyWilaya = compWilayaSpinner.getSelectedItem().toString();
         companyName = compName.getText().toString().trim();
         companyAddress = compAddress.getText().toString().trim() + " - " + companyWilaya;
        registrationNumber = regNum.getText().toString().trim();
       ownershipInfo = ownership.getText().toString().trim();
        cpoInfo = cpo.getText().toString().trim();
        compnameView.setText(companyName);
        compaddrView.setText(companyAddress);
        regnumView.setText(registrationNumber);
        ownedView.setText(ownershipInfo);
    }

    // Validate input fields
    private boolean validateFields(int step) {
        // Get values from input fields
        EditText firstname = findViewById(R.id.firstname);
        EditText lastname = findViewById(R.id.lastname);
        EditText birthdate = findViewById(R.id.birthdate);
        EditText birthplace = findViewById(R.id.birthplace);
        EditText nationality = findViewById(R.id.nationality);
        EditText nin = findViewById(R.id.NIN);

        // Get values from input fields
        String firstName = firstname.getText().toString().trim();
        String lastName = lastname.getText().toString().trim();
        String birthdateStr = birthdate.getText().toString().trim();
        String birthplaceStr = birthplace.getText().toString().trim();
        String nationalityStr = nationality.getText().toString().trim();
        String ninStr = nin.getText().toString().trim();

        // Common validations for all steps
        // Validate first name

        if(step == 1 ) {
            if (firstName.isEmpty()) {
                Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                firstname.requestFocus();
                return false;
            }

            // Validate last name
            if (lastName.isEmpty()) {
                Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show();
                lastname.requestFocus();
                return false;
            }

            // Validate birthdate
            if (birthdateStr.isEmpty()) {
                Toast.makeText(this, "Please enter your birthdate", Toast.LENGTH_SHORT).show();
                birthdate.requestFocus();
                return false;
            }

            // Validate birthplace
            if (birthplaceStr.isEmpty()) {
                Toast.makeText(this, "Please enter your birthplace", Toast.LENGTH_SHORT).show();
                birthplace.requestFocus();
                return false;
            }

            // Validate nationality
            if (nationalityStr.isEmpty()) {
                Toast.makeText(this, "Please enter your nationality", Toast.LENGTH_SHORT).show();
                nationality.requestFocus();
                return false;
            }

            // Validate NIN
            if (ninStr.isEmpty()) {
                Toast.makeText(this, "Please enter your NIN", Toast.LENGTH_SHORT).show();
                nin.requestFocus();
                return false;
            }

            // Additional validations based on the step
            if (birthCertFilePath==null) {
                Toast.makeText(this, "Please add your birth certificate file", Toast.LENGTH_SHORT).show();
                return false;
            } else if (nationalityFilePath==null) {
                Toast.makeText(this, "Please add your nationality file", Toast.LENGTH_SHORT).show();
                return false;
            } else if (ninFilePath==null) {
                Toast.makeText(this, "Please add your NIN file", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (step == 2) {
            // Validate company name
            EditText compName = findViewById(R.id.compName);
            String companyName = compName.getText().toString().trim();
            if (companyName.isEmpty()) {
                Toast.makeText(this, "Please enter the company name", Toast.LENGTH_SHORT).show();
                compName.requestFocus();
                return false;
            }

            // Validate address
            EditText compAddress = findViewById(R.id.compaddress);
            String companyAddress = compAddress.getText().toString().trim();
            if (companyAddress.isEmpty()) {
                Toast.makeText(this, "Please enter the company address", Toast.LENGTH_SHORT).show();
                compAddress.requestFocus();
                return false;
            }

            // Validate registration number
            EditText regNum = findViewById(R.id.regnum);
            String registrationNumber = regNum.getText().toString().trim();
            if (registrationNumber.isEmpty()) {
                Toast.makeText(this, "Please enter the registration number", Toast.LENGTH_SHORT).show();
                regNum.requestFocus();
                return false;
            }

            // Validate company certificate file
            if (companyCertFilePath==null) {
                Toast.makeText(this, "Please add the company certificate file", Toast.LENGTH_SHORT).show();
                return false;
            } else if (ownershipFilePath==null) {
                Toast.makeText(this, "Please add the ownership file", Toast.LENGTH_SHORT).show();
                return false;
            } else if (cpoFilePath==null) {
                Toast.makeText(this, "Please add the Commercial Property Ownership file", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        if (step == 3) {
            // Get the agreement checkbox
            CheckBox agreementCheckbox = findViewById(R.id.checkBox);

            // Check if the agreement checkbox is not checked
            if (!agreementCheckbox.isChecked()) {
                Toast.makeText(this, "Please agree to the terms before submitting", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        // If all validations pass, return true
        return true;
    }


    private final ActivityResultLauncher<String> pickFileLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    handleFilePickerResult(uri, currentFilePickerRequestCode);
                }
            }
    );


    private void launchFilePicker(String mimeType, int requestCode) {
        pickFileLauncher.launch(mimeType);
        currentFilePickerRequestCode = requestCode;
    }




    private void handleFilePickerResult(Uri selectedFileUri, int requestCode) {
        Log.d("FilePickerResult", "Selected File Uri: " + selectedFileUri.toString());

        switch (requestCode) {
            case FILE_PICKER_BIRTH_CERT_REQUEST_CODE:
                birthCertFilePath = selectedFileUri;
                Toast.makeText(this, "Selected birth certification file: " + selectedFileUri.toString(), Toast.LENGTH_SHORT).show();
                break;

            case FILE_PICKER_NATIONALITY_REQUEST_CODE:
                nationalityFilePath = selectedFileUri;
                Toast.makeText(this, "Selected nationality file: " + selectedFileUri.toString(), Toast.LENGTH_SHORT).show();
                break;
            case FILE_PICKER_NIN_REQUEST_CODE:
                ninFilePath = selectedFileUri;
                Toast.makeText(this, "Selected NIN file: " + selectedFileUri.toString(), Toast.LENGTH_SHORT).show();
                break;

            case FILE_PICKER_COMPANY_CERT_REQUEST_CODE:
                companyCertFilePath = selectedFileUri;
                Toast.makeText(this, "Selected company certificate file: " + selectedFileUri.toString(), Toast.LENGTH_SHORT).show();
                break;

            case FILE_PICKER_OWNERSHIP_REQUEST_CODE:
                ownershipFilePath = selectedFileUri;
                Toast.makeText(this, "Selected ownership file: " + selectedFileUri.toString(), Toast.LENGTH_SHORT).show();
                break;

            case FILE_PICKER_CPO_REQUEST_CODE:
                cpoFilePath = selectedFileUri;
                Toast.makeText(this, "Selected CPO file: " + selectedFileUri.toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void displayCollectedData() {
        // Log collected data instead of displaying it in TextViews
        Log.d("CollectedData", "First Name: " + firstName);
        Log.d("CollectedData", "Last Name: " + lastName);
        Log.d("CollectedData", "Birthdate: " + birthdateStr);
        Log.d("CollectedData", "Birthplace: " + birthplaceStr);
        Log.d("CollectedData", "Nationality: " + nationalityStr);
        Log.d("CollectedData", "NIN: " + ninStr);
        Log.d("CollectedData", "birth cert: " + birthCertFilePath);
        Log.d("CollectedData", "Company Name: " + companyName);
        Log.d("CollectedData", "Company Address: " + companyAddress);
        Log.d("CollectedData", "Company Wilaya: " + companyWilaya);
        Log.d("CollectedData", "Registration Number: " + registrationNumber);
        Log.d("CollectedData", "Company Cert File Path: " + companyCertFilePath);
        Log.d("CollectedData", "Ownership Info: " + ownershipInfo);
        Log.d("CollectedData", "Ownership File Path: " + ownershipFilePath);
        Log.d("CollectedData", "CPO Info: " + cpoInfo);
        Log.d("CollectedData", "CPO File Path: " + cpoFilePath);
        // Log more data as needed
    }

    // Helper method to add TextView to a layout
    private void addTextViewToLayout(LinearLayout layout, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(16);
        layout.addView(textView);
    }

}
