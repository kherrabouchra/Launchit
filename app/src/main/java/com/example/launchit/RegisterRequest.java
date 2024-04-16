package com.example.launchit;

import android.net.Uri;

import java.util.Date;

public class RegisterRequest {
    private long reqID;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String birthPlace;
    private String nationality;
    private String nin;
    private String state;

    // Additional attributes
    private String birthCertPath;
    private String nationalityPath;
    private String ninPath;
    private long companyId;
    private String companyName;
    private String companyAddress;
    private String companyOwnership;
    private String companyRegNumber;
    private String companyCertPath;
    private String ownershipPath;
    private String cpoPath;
    private Date submissionDate;

    // Constructors
    public RegisterRequest() {
        // Default constructor
    }


    public RegisterRequest(long reqID,  String birthDate, String birthPlace,
                           String companyName,long companyId,  String nationality,
                           String nin, String state, Date submissionDate) {


    }
    public RegisterRequest(long reqID, String state, Date submissionDate) {
        this.reqID = reqID;
        this.state = state;
        this.submissionDate = submissionDate;
    }

    public RegisterRequest(long id, String firstname, String lastname, String birthDate, String birthPlace, String companyName, long companyId, String nationality, String nin, String state, Date submissionDate) {
        this.reqID = id;
this.firstName=firstname;
this.lastName=lastname;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.companyName = companyName;
        this.companyId= companyId;
        this.nationality = nationality;
        this.nin = nin;
        this.state = state;
        this.submissionDate = submissionDate; }

    public RegisterRequest(long id, String firstname, String lastname, String birthDate, String birthPlace, Uri birthCertPath, String companyName, long companyId,
                           Uri comppath, Uri ownpath, Uri cpopath, String nationality, Uri nationalityPath, String nin, Uri ninPath, String state, Date submissionDate) {

        this.reqID = id;
        this.firstName=firstname;
        this.lastName=lastname;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.companyName = companyName;
        this.companyId= companyId;
        this.nationality = nationality;
        this.nin = nin;
        this.state = state;
        this.submissionDate = submissionDate;
         this.birthCertPath= String.valueOf(birthCertPath);
       this.nationalityPath= String.valueOf(nationalityPath);
       this.ninPath= String.valueOf(ninPath);
         companyCertPath= String.valueOf(comppath);
         ownershipPath= String.valueOf(ownpath);
         cpoPath= String.valueOf(cpopath);

    }

    // Getters
    public long getReqID() {
        return reqID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getNationality() {
        return nationality;
    }

    public String getNin() {
        return nin;
    }

    public String getState() {
        return state;
    }

    // Additional getters
    public String getBirthCertPath() {
        return birthCertPath;
    }

    public String getNationalityPath() {
        return nationalityPath;
    }

    public String getNinPath() {
        return ninPath;
    }

    public long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyOwnership() {
        return companyOwnership;
    }

    public String getCompanyRegNumber() {
        return companyRegNumber;
    }

    public String getCompanyCertPath() {
        return companyCertPath;
    }

    public String getOwnershipPath() {
        return ownershipPath;
    }

    public String getCpoPath() {
        return cpoPath;
    }

    // Setters
    public void setReqID(long regNumber) {
        this.reqID = regNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Additional setters
    public void setBirthCertPath(String birthCertPath) {
        this.birthCertPath = birthCertPath;
    }

    public void setNationalityPath(String nationalityPath) {
        this.nationalityPath = nationalityPath;
    }

    public void setNinPath(String ninPath) {
        this.ninPath = ninPath;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void setCompanyOwnership(String companyOwnership) {
        this.companyOwnership = companyOwnership;
    }

    public void setCompanyRegNumber(String companyRegNumber) {
        this.companyRegNumber = companyRegNumber;
    }

    public void setCompanyCertPath(String companyCertPath) {
        this.companyCertPath = companyCertPath;
    }

    public void setOwnershipPath(String ownershipPath) {
        this.ownershipPath = ownershipPath;
    }

    public void setCpoPath(String cpoPath) {
        this.cpoPath = cpoPath;
    }

    // Additional setters...

    public void setId(long aLong) {
        // setId method implementation
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }


}
