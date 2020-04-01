package com.example.doctogo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static int DATABASE_VERSION = 6;
    //DATABASE TABLES & COLUMNS
    private static final String DATABASE_NAME = "DocToGoDatabase .db";
    /*
        Users Table: defines all users in the system.
        role (integer) column defines the roles the user is:
            1 = Admin
            2 = Patient
            3 = Doctor
            4 = Cashier
        Anything else other than specified is an error and must be addressed.
        Patients has the most info- all users too have these columns, which may be useless
        (ex: Cashier with age), so most of these columns can be null. However, accounts requiring
        data must be enforced programmatically.
    */
    //TABLE 1
    private final static String TABLE1_NAME = "Users";
    private final static String T1COL_1 = "ID";              //INTEGER PRIMARY KEY
    private final static String T1COL_2 = "Username";        //TEXT
    private final static String T1COL_3 = "Password";        //TEXT
    private final static String T1COL_4 = "Role";            //INTEGER
    private final static String T1COL_5 = "Firstname";       //TEXT
    private final static String T1COL_6 = "Lastname";        //TEXT
    private final static String T1COL_7 = "Address";         //TEXT
    private final static String T1COL_8 = "Email";           //TEXT
    private final static String T1COL_9 = "Phone";           //TEXT
    private final static String T1COL_10 = "Qualifications"; //TEXT        //Doctors Only REQUIRED
    private final static String T1COL_11 = "Weight";         //INTEGER
    private final static String T1COL_12 = "Gender";         //TEXT        //if Gender not selected, UNDEFINED
    private final static String T1COL_13 = "Age";            //INTEGER     //Age of 0 MUST become "N/A"
    private final static String T1COL_14 = "FirstLogin";     //INTEGER     //flag, turn to 1 if admin register->have to modify self.

    /*
        Appointment table: Defines a patient (role 2) having a schedule with a doctor (role 3).
        Due to structure, the appointment's foreign keys referring to the actors will both refer to
        the same users table. Thus, when using inner join, use aliases to refer to these actors
        (eg: SELECT p.id as patient, d.id as doctor FROM Appointments
             INNER JOIN Users p ON Appointments.Patient_ID = p.id
             INNER JOIN Users d ON Appointments.Doctor_ID = p.id;)
     */
    //TABLE 2
    private final static String TABLE2_NAME = "Appointments";
    private final static String T2CONST_1 = "FK_patient";
    private final static String T2CONST_2 = "FK_doctor";
    private final static String T2COL_1 = "Appointment_ID";   //INTEGER PRIMARY KEY
    private final static String T2COL_2 = "Patient_ID";       //INTEGER FOREIGN KEY
    private final static String T2COL_3 = "Doctor_ID";        //INTEGER FOREIGN KEY
    private final static String T2COL_4 = "Schedule_Date";    //TEXT YYYY-MM-DD HH:MM:SS.SSS
    private final static String T2COL_5 = "Duration";         //INTEGER (duration in minutes)

    /*
        Payments table: Defines payments made by patiens (role 2).
        Transdate, if not null, means that payment has been made at that date.
        If Transdate is null, payment has not been made.
     */
    //TABLE 3
    private final static String TABLE3_NAME = "Payments";
    private final static String T3CONST_1 = "FK_report";
    private final static String T3CONST_2 = "FK_patient";
    private final static String T3COL_1 = "Payment_ID";   //INTEGER PRIMARY KEY
    private final static String T3COL_2 = "Patient_ID";   //INTEGER FOREIGN KEY
    private final static String T3COL_3 = "Due_Date";     //TEXT YYYY-MM-DD HH:MM:SS.SSS
    private final static String T3COL_4 = "Trans_Date";   //TEXT YYYY-MM-DD HH:MM:SS.SSS
    private final static String T3COL_5 = "Amount";       //REAL
    private final static String T3COL_6 = "Report_ID";    ////INTEGER FOREIGN KEY

    /*
        Comments table: Contains all comments made by patients (role 2) to doctors (role 3).
        The same two FK to same table method applies here (see Appointments table).
        Patient-Doctor_Posts are structured in a comment/reply form, but doesn't have to be.
     */
    //TABLE 4
    private final static String TABLE4_NAME = "Comments";
    private final static String T4CONST_1 = "FK_patient";
    private final static String T4CONST_2 = "FK_doctor";
    private final static String T4COL_1 = "Comment_ID";   //INTEGER PRIMARY KEY
    private final static String T4COL_2 = "Patient_ID";   //INTEGER FOREIGN KEY
    private final static String T4COL_3 = "Doctor_ID";    //INTEGER FOREIGN KEY
    private final static String T4COL_4 = "Patient_Post"; //TEXT
    private final static String T4COL_5 = "Doctor_Post";  //TEXT

    /*
        Reports table: Made after a successful diagnosis, a report is made from the doctor (role 3)
        describing a patient's diagnosis case. Refers to users table for two FK (see Appointments table).
        Also has another FK, referring to a Payment. Patient's medical history can be derived from here.
     */
    //TABLE 5
    private final static String TABLE5_NAME = "Reports";
    private final static String T5CONST_1 = "FK_patient";
    private final static String T5CONST_2 = "FK_doctor";
    //private final static String T5CONST_3 = "FK_payment";
    private final static String T5COL_1 = "Report_ID";     //INTEGER PRIMARY KEY
    private final static String T5COL_2 = "Patient_ID";    //INTEGER FOREIGN KEY
    private final static String T5COL_3 = "Doctor_ID";     //INTEGER FOREIGN KEY
    private final static String T5COL_4 = "Date";          //TEXT YYYY-MM-DD HH:MM:SS.SSS
    private final static String T5COL_5 = "Description";   //TEXT
    private final static String T5COL_6 = "Payment_ID";    //INTEGER FOREIGN KEY


    public DatabaseHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //SQL Create tables
        //users
        String query = "CREATE TABLE " + TABLE1_NAME +
                "("+ T1COL_1 +" INTEGER PRIMARY KEY, " +
                T1COL_2 +" TEXT, " +
                T1COL_3+" TEXT, " +
                T1COL_4+" INTEGER, " +
                T1COL_5+" TEXT, " +
                T1COL_6+" TEXT, " +
                T1COL_7+" TEXT, " +
                T1COL_8+" TEXT, " +
                T1COL_9+" TEXT, " +
                T1COL_10+" TEXT, " +
                T1COL_11+" INTEGER, " +
                T1COL_12+" TEXT, " +
                T1COL_13+" INTEGER, " +
                T1COL_14+" INTEGER)";
        db.execSQL(query);

        //appointments
        query = "CREATE TABLE " + TABLE2_NAME +
                "("+T2COL_1+" INTEGER PRIMARY KEY, " +
                T2COL_2+" INTEGER, " +
                T2COL_3+" INTEGER, " +
                T2COL_4+" TEXT, " +
                T2COL_5+" INTEGER, " +
                "CONSTRAINT "+ T2CONST_1 +" FOREIGN KEY ("+T2COL_2+") REFERENCES "+TABLE1_NAME+"("+T1COL_1+")," +
                "CONSTRAINT "+ T2CONST_2 +" FOREIGN KEY ("+T2COL_3+") REFERENCES "+TABLE1_NAME+"("+T1COL_1+"))";
        db.execSQL(query);

        //payments
        query = "CREATE TABLE " + TABLE3_NAME +
                "("+T3COL_1+" INTEGER PRIMARY KEY, " +
                T3COL_2+" INTEGER, " +
                T3COL_3+" TEXT, " +
                T3COL_4+" TEXT, " +
                T3COL_5+" REAL, " +
                T3COL_6+" INTEGER, " +
                "CONSTRAINT "+ T3CONST_1 +" FOREIGN KEY ("+T3COL_2+") REFERENCES "+TABLE1_NAME+"("+T1COL_1+")," +
                "CONSTRAINT "+ T3CONST_2 +" FOREIGN KEY ("+T3COL_6+") REFERENCES "+TABLE5_NAME+"("+T5COL_1+"))";
        db.execSQL(query);

        //comments
        query = "CREATE TABLE " + TABLE4_NAME +
                "("+T4COL_1+" INTEGER PRIMARY KEY, " +
                T4COL_2+" INTEGER, " +
                T4COL_3+" INTEGER, " +
                T4COL_4+" TEXT, " +
                T4COL_5+" TEXT, " +
                "CONSTRAINT "+ T4CONST_1 +" FOREIGN KEY ("+T4COL_2+") REFERENCES "+TABLE1_NAME+"("+T1COL_1+")," +
                "CONSTRAINT "+ T4CONST_2 +" FOREIGN KEY ("+T4COL_3+") REFERENCES "+TABLE1_NAME+"("+T1COL_1+"))";
        db.execSQL(query);

        //reports
        query = "CREATE TABLE " + TABLE5_NAME +
                "("+T5COL_1+" INTEGER PRIMARY KEY, " +
                T5COL_2+" INTEGER, " +
                T5COL_3+" INTEGER, " +
                T5COL_4+" TEXT, " +
                T5COL_5+" TEXT, " +
                T5COL_6+" INTEGER, " +
                "CONSTRAINT "+ T5CONST_1 +" FOREIGN KEY ("+T5COL_2+") REFERENCES "+TABLE1_NAME+"("+T1COL_1+")," +
                "CONSTRAINT "+ T5CONST_2 +" FOREIGN KEY ("+T5COL_3+") REFERENCES "+TABLE1_NAME+"("+T1COL_1+")" +
                //"," + "CONSTRAINT "+ T5CONST_3 +" FOREIGN KEY ("+T5COL_6+") REFERENCES "+TABLE3_NAME+"("+T3COL_1+")" +
                ")";
        db.execSQL(query);

        //insert a single admin account for access
        ContentValues cv = new ContentValues();
        cv.put(T1COL_2,"root");
        cv.put(T1COL_3,"pass");
        cv.put(T1COL_8,"localhost");
        cv.put(T1COL_4,1);
        db.insert(TABLE1_NAME,null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        //Drop tables if exists
        //ORDER IS IMPORTANT! Watch constraints.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE4_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE5_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        onCreate(db);
    }

    //login user function: find matching username + password.
    public Cursor loginUser(String user, String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+T1COL_1+", "+T1COL_4+" FROM "+TABLE1_NAME+
                " WHERE "+T1COL_2+"='"+user+"' AND "+T1COL_3+"='"+pass+"'";
        return db.rawQuery(query,null);
    }


    public boolean adminRegister(String accName, String accPass, int accRole, String accEmail)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //validation: ROLE MUST BE 1-4!
        if(accRole > 0 && accRole < 5)
        {
            //accRole acceptable, insert into db
            ContentValues cv = new ContentValues();
            cv.put(T1COL_2,accName);
            cv.put(T1COL_3,accPass);
            cv.put(T1COL_4,accRole);
            cv.put(T1COL_8,accEmail);
            cv.put(T1COL_14,1); //First time login, user HAS to modify account.
            Long reply = db.insert(TABLE1_NAME,null,cv);

            //return results
            if(reply > 0)
            {return true;}
        }
        return false;
    }

    public boolean normalRegister(String accName, String accPass, int accRole, String accEmail, String accFirstName, String accLastName, String accAddress, String accPhone, int accWeight, String accGender, int accAge)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //insert into db
        ContentValues cv = new ContentValues();
        cv.put(T1COL_2,accName);
        cv.put(T1COL_3,accPass);
        cv.put(T1COL_4,accRole);
        cv.put(T1COL_5,accFirstName);
        cv.put(T1COL_6,accLastName);
        cv.put(T1COL_7,accAddress);
        cv.put(T1COL_8,accEmail);
        cv.put(T1COL_9,accPhone);
        cv.put(T1COL_11,accWeight);
        cv.put(T1COL_12,accGender);
        cv.put(T1COL_13,accAge);
        cv.put(T1COL_14,0);
        Long reply = db.insert(TABLE1_NAME,null,cv);

        //return results
        if(reply > 0)
        {return true;}
        else
        {return false;}
    }

    //get all accounts (id, name, pass, role) WHERE Role & Username MATCH filter.
    public Cursor viewAllAccounts(String usernameFilter, String roleFilter)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+T1COL_1+", "+T1COL_2+", "+T1COL_5+", "+T1COL_6+", "+T1COL_4+ " FROM "+TABLE1_NAME
                +" WHERE " + T1COL_4 + roleFilter
                +" AND " + T1COL_2 + usernameFilter;
        Log.e("DbAdminViewAll ", query);
        return db.rawQuery(query,null);
    }

    //Get information from TABLE1_NAME to show in patient activities
    public Cursor getInformationUser(int userId)
    {

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE1_NAME + " WHERE " + T1COL_1 + " = " + userId;
            Log.d("DbgetInformationUser ",query);
            return db.rawQuery(query, null);
        }catch (Exception msg){
            Log.e("DbgetInformationUser ",msg.getMessage());
            return null;
        }
    }

    //update an account from admin perspective
    public boolean adminUpdate(int accID, String accName, String accPass, String accEmail, String accFirstName, String accLastName, String accAddress, String accPhone, int accWeight, String accQualifications,  String accGender, int accAge) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //insert into db
            ContentValues cv = new ContentValues();
            cv.put(T1COL_2,accName);
            cv.put(T1COL_3,accPass);
            cv.put(T1COL_5,accFirstName);
            cv.put(T1COL_6,accLastName);
            cv.put(T1COL_7,accAddress);
            cv.put(T1COL_8,accEmail);
            cv.put(T1COL_9,accPhone);
            cv.put(T1COL_11,accWeight);
            cv.put(T1COL_12,accGender);
            cv.put(T1COL_13,accAge);
            int reply = db.update(TABLE1_NAME,cv,T1COL_1+"="+accID,null);

            //return results
            if(reply > 0)
            {return true;}
            else
            {return false;}
        } catch (Exception msg) {
            Log.e("DbAdminupdInfoUser ", msg.getMessage());
            return false;
        }
    }
//    private final static String T1COL_7 = "Address";         //TEXT
//    private final static String T1COL_8 = "Email";           //TEXT
//    private final static String T1COL_9 = "Phone";           //TEXT
//    private final static String T1COL_10 = "Qualifications"; //TEXT        //Doctors Only REQUIRED
//    private final static String T1COL_11 = "Weight";         //INTEGER



    //Update Patient Information
//    public Cursor updateInformationUser(int userId,String address,String email,String phone,int weight){
        public Cursor updateInformationUser(int userId,String address,String email,String phone,int weight){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "UPDATE " + TABLE1_NAME + " SET " + T1COL_7 + "=" + "'" + address + "'," + T1COL_8 + "=" + "'" + email + "',"
                    + T1COL_9 + "=" + "'" + phone + "'," + T1COL_11 + "=" + "'" + weight + "'"+ " WHERE " + T1COL_1 + "=" + userId;
            Log.d("DbupdInfoUser ",query);
            return db.rawQuery(query, null);
        }catch (Exception msg){
            Log.e("DbupdInfoUser ",msg.getMessage());
            return null;
        }
    }

    //Get all rows from the table Report with paymentID-filter
    public Cursor viewReportWithoutPaymentId(int x)
    {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT " + T5COL_1 + ", " + T5COL_2 + ", " + T5COL_3 + ", " + T5COL_4 + ", " + T5COL_5 + " FROM " + TABLE5_NAME
                    + " WHERE " + T5COL_6 + "=" + x;
            Log.e("DbviewReportWithoutP ", query);
            return db.rawQuery(query, null);
    }
    //Get row from the table Report with reportID-filter
    public Cursor viewReport(int x)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + T5COL_2 + ", " + T5COL_3 + ", " + T5COL_4 + ", " + T5COL_5 + ", " + T5COL_6 + " FROM " + TABLE5_NAME
                + " WHERE " + T5COL_1 + "=" + x;
        Log.e("DbviewReport ", query);
        return db.rawQuery(query, null);
    }
    //Get all rows from payment will transDate-fliter
    public Cursor viewPaymentPayedOrNot(String n)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + T3COL_1 + ", " + T3COL_2 + ", " + T3COL_3 + ", " + T3COL_5 +  " FROM " + TABLE3_NAME
                + " WHERE " + T3COL_4 + n;
        Log.e("Dbpaymentpayed ", query);
        return db.rawQuery(query, null);
    }
    //Find payment row with paymentID
    public Cursor viewPayment(int x)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + T3COL_2 + ", " + T3COL_3 + ", " + T3COL_4 + ", " + T3COL_5  + " FROM " + TABLE3_NAME
                + " WHERE " + T3COL_1 + "=" + x;
        Log.e("DbviewPayment ", query);
        return db.rawQuery(query, null);
    }

    //Insert a new row to payment table
    public long paymentInsert(int patiId, String dueDate, int amount_num, int reportID){
        SQLiteDatabase db = this.getWritableDatabase();
        //insert into db
        ContentValues cv = new ContentValues();
        cv.put(T3COL_2,patiId);
        cv.put(T3COL_3,dueDate);
        cv.put(T3COL_5,amount_num);
        cv.put(T3COL_6,reportID);
        Long reply = db.insert(TABLE3_NAME,null,cv);
        //return results
        if(reply > 0)
        {return reply;}
        else
        {return 0;}

    }

    //Update dueDate to Payment table
    public boolean updatePaymentWithdueDate(int payID, String duedate){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(T3COL_3,duedate);
            int reply = db.update(TABLE3_NAME,cv,T3COL_1+"="+payID,null);

            //return results
            if(reply > 0)
            {return true;}
            else
            {return false;}
        } catch (Exception msg) {
            Log.e("updatePaymentWithdue ", msg.getMessage());
            return false;
        }
    }

    //Update TransDate to Payment table
    public boolean updatePaymentWithtransDate(int payID, String transDate){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(T3COL_4,transDate);
            int reply = db.update(TABLE3_NAME,cv,T3COL_1+"="+payID,null);

            //return results
            if(reply > 0)
            {return true;}
            else
            {return false;}
        } catch (Exception msg) {
            Log.e("updatePaymentWithtrans ", msg.getMessage());
            return false;
        }
    }
    //Update amount to Payment table
    public boolean updatePaymentWithAmount(int payID, int amount){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(T3COL_5,amount);
            int reply = db.update(TABLE3_NAME,cv,T3COL_1+"="+payID,null);

            //return results
            if(reply > 0)
            {return true;}
            else
            {return false;}
        } catch (Exception msg) {
            Log.e("updatePaymentWithAmou ", msg.getMessage());
            return false;
        }
    }

    //Update paymentID to Report
    public boolean updateReportWithPaymentID(int payID, int reportID){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(T5COL_6,payID);
            int reply = db.update(TABLE5_NAME,cv,T5COL_1+"="+reportID,null);

            //return results
            if(reply > 0)
            {return true;}
            else
            {return false;}
        } catch (Exception msg) {
            Log.e("updateReportWithP ", msg.getMessage());
            return false;
        }
    }


}
