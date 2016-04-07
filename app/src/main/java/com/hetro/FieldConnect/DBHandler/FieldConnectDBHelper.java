package com.hetro.FieldConnect.fieldconnect.DBHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FieldConnectDBHelper extends SQLiteOpenHelper {

    // All Static variables
    private static FieldConnectDBHelper dbHelper = null;
    private static SQLiteDatabase database = null;
    // Database Version
    private static final int DATABASE_VERSION = 1;


    public static final String DATABASE_NAME = "FieldConnect.db";


    // User table name
    private final String TABLE_USERS = "FieldConnectUsers";
    private final String TABLE_EMPLOYEE = "FieldConnectEmployee";



    public final static String KEY_ID = "_id";

    private static Context contextValue;

    // Users table with username and passwordHash
    private final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + DBConstants.KEY_USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,"
            + DBConstants.KEY_USERS_NAME + " VARCHAR(150),"
            + DBConstants.KEY_USERS_PASSWORD + " VARCHAR(150),"
            + DBConstants.KEY_USERS_MOBILE + " INTEGER(15),"
            + DBConstants.KEY_USERS_DEVICEID + " VARCHAR(30),"
            + DBConstants.KEY_USERS_STATUS + " VARCHAR(30), "
            + DBConstants.KEY_USERS_CREATE_DATE + " INTEGER,"
            + DBConstants.KEY_USERS_MODIFIED_DATE + " INTEGER,"
            + DBConstants.KEY_USERS_CREATED_BY + " VARCHAR(150), "
            + DBConstants.KEY_USERS_MODIFIED_BY + " VARCHAR(150)" + " )";



//    private final String CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + TABLE_EMPLOYEE + "("
//            + DBConstants.KEY_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ,"
//            + DBConstants.KEY_EMPLOYEE_NAME + " VARCHAR(150),"
//            + DBConstants.KEY_EMPLOYEE_DESIGNATION + " VARCHAR(50),"
//            + DBConstants.KEY_EMPLOYEE_MODIFIED_BY + " VARCHAR(150),"
//            + DBConstants.KEY_EMPLOYEE_MODIFIED_TIME + " INTEGER" + " )";

    private FieldConnectDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
        dbHelper = this;
    }

    //Singleton to Instantiate the SQLiteOpenHelper
    public static FieldConnectDBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new FieldConnectDBHelper(context);
            openConnection();
        }
        contextValue = context;
        return dbHelper;
    }

    // It is used to open database
    private static void openConnection() {
        if (database == null) {
            database = dbHelper.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        Log.e("Inside DB", "DB Creation");

        db.execSQL(CREATE_USERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        onCreate(db);
    }





//    public void downloadVenue(String id,String venueName,String address_1,String address_2,String city,
//                              String state,String pincode,String region_id,String contact_name,
//                              String contact_number,String contact_email,String owner_name,String owner_number,
//                              String clas,String seating_capacity,String website,String past_occupancy,
//                              String lat,String lng,String venue_status){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_VENUE_NAME,venueName);
//        values.put(DBConstants.KEY_SERVER_VENUE_ID,id);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_1,address_1);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_2, address_2);
//        values.put(DBConstants.KEY_VENUE_CITY,city);
//        values.put(DBConstants.KEY_VENUE_STATE,state);
//        values.put(DBConstants.KEY_VENUE_PINCODE,pincode);
//        values.put(DBConstants.KEY_VENUE_REGION_ID,region_id);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NAME,contact_name);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NUMBER,contact_number);
//        values.put(DBConstants.KEY_VENUE_CONTACT_EMAIL,contact_email);
//        values.put(DBConstants.KEY_VENUE_OWNER_NAME,owner_name);
//        values.put(DBConstants.KEY_VENUE_OWNER_NUMBER,owner_number);
//        values.put(DBConstants.KEY_VENUE_CLASS,clas);
//        values.put(DBConstants.KEY_VENUE_SEATING_CAPACITY,seating_capacity);
//        values.put(DBConstants.KEY_VENUE_WEBSITE,website);
//        values.put(DBConstants.KEY_VENUE_SERVERSYNC,"yes");
//        values.put(DBConstants.KEY_VENUE_PAST_OCCUPANCY,past_occupancy);
//        values.put(DBConstants.KEY_VENUE_LAT,lat);
//        values.put(DBConstants.KEY_VENUE_LNG,lng);
//        values.put(DBConstants.KEY_VENUE_STATUS,venue_status);
//
//        database.insert(TABLE_VENUE, null, values);
//    }

//    public void deleteImagePath(String imagePath){
//
//        try {
//
//
//            database.delete(TABLE_VENUE_IMAGE, DBConstants.KEY_VENUE_IMAGE_VENUEPATH + "= '" + imagePath+"'", null);
//
//            getImage();
//
//        }catch (Exception e){
//            Log.e("errr",e.toString());
//        }
//
//    }

//    public void storeImage(String venueName, String imagePath){
//
//        try {
//            ContentValues values = new ContentValues();
//            values.put(DBConstants.KEY_VENUE_IMAGE_VENUEPATH, imagePath);
//            values.put(DBConstants.KEY_VENUE_IMAGE_SERVERSYNC, "no");
//            values.put(DBConstants.KEY_VENUE_IMAGE_VENUENAME, venueName);
//            database.insert(TABLE_VENUE_IMAGE, null, values);
//
//            getImage();
//
//        }catch (Exception e){
//            Log.e("errr",e.toString());
//        }
//
//    }

//    public void getImage(){
//        Cursor cursor=null;
//
//        try {
//        String selectQuery = "SELECT * FROM " + TABLE_VENUE_IMAGE ;
//        cursor=database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        Log.e("count--", cursor.getCount() + "");
//        for (int i = 0; i < cursor.getCount(); i++) {
//
//            String venueImagePath=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_IMAGE_VENUEPATH));
//            String venueeName=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_IMAGE_VENUENAME));
//
//            Log.e("ImagePathDto--",venueImagePath);
//            Log.e("ImageName--",venueeName);
//
//        }
//        cursor.close();
//
//        }
//        catch (Exception e){
//
//            Log.e("errroor",e.toString());
//
//        }
//
//    }



//    public Cursor getImagebyVenue(String venueName){
//        Cursor cursor=null;
//
//        try {
//
//            String selectQuery = "SELECT * FROM " + TABLE_VENUE_IMAGE + " WHERE " +
//                    DBConstants.KEY_VENUE_IMAGE_VENUENAME +
//                    "= '" + venueName + "' AND "+DBConstants.KEY_VENUE_IMAGE_SERVERSYNC +"= 'no'";
//             cursor=database.rawQuery(selectQuery,null);
//            cursor.moveToFirst();
//            Log.e("count--", cursor.getCount() + "");
//            for (int i = 0; i < cursor.getCount(); i++) {
//
//                String venueImagePath=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_IMAGE_VENUEPATH));
//
//            }
//
//
//
//        }
//        catch (Exception e){
//
//            Log.e("eeeeeeee",e.toString());
//
//        }
//
//        return cursor;
//
//    }
//
//    public String getVenueIdbyName(String venueName){
//
//        String venueId=null;
//        Cursor cursor=null;
//
//        try {
//
//            String selectQuery = "SELECT * FROM " + TABLE_VENUE + " WHERE " + DBConstants.KEY_VENUE_NAME +
//                    "= '" + venueName + "'";
//            cursor=database.rawQuery(selectQuery,null);
//            cursor.moveToFirst();
//            Log.e("count--", cursor.getCount() + "");
//            for (int i = 0; i < cursor.getCount(); i++) {
//
//                venueId= cursor.getString(cursor.getColumnIndex(DBConstants.KEY_SERVER_VENUE_ID));
//
//            }
//
//
//
//        }
//        catch (Exception e){
//
//            Log.e("eeeeeeee",e.toString());
//
//        }
//
//       return venueId;
//    }
//
//    public void updateVenueScheduleStatusServerSync(String venueId){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_SCHEDULE_VENUE_ID,venueId);
//
//        values.put(DBConstants.KEY_SCHEDULE_SERVERSYNC, "yes");
//
//        database.update(TABLE_SCHEDULE_VENUE, values, DBConstants.KEY_SCHEDULE_VENUE_ID + "= '" + venueId + "'", null);
//
//
//    }
//
//
//    public void updateVenueScheduleStatus(String venueId){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_SCHEDULE_VENUE_ID,venueId);
//
//        values.put(DBConstants.KEY_SCHEDULE_SERVERSYNC,"no");
//
//        database.update(TABLE_SCHEDULE_VENUE, values, DBConstants.KEY_SCHEDULE_VENUE_ID + "= '" + venueId + "'", null);
//    }
//
//    public void downloadScheduleList(String venueId,String venueStatus,String venueComment,
//                                     String followUp,String employeeId,String histotyTime){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_SCHEDULE_VENUE_ID,venueId);
//        values.put(DBConstants.KEY_SCHEDULE_STATUS,venueStatus);
//        values.put(DBConstants.KEY_SCHEDULE_COMMENT,venueComment);
//        values.put(DBConstants.KEY_SCHEDULE_FOLLOWUP,followUp);
//        values.put(DBConstants.KEY_SCHEDULE_EMPLOYEE_ID,employeeId);
//        values.put(DBConstants.KEY_SCHEDULE_SERVERSYNC,"no");
//        values.put(DBConstants.KEY_SCHEDULE_HISTORYTIME,histotyTime);
//
//        database.insert(TABLE_SCHEDULE_VENUE, null, values);
//    }
//
//    public void updateVenuebyName(String id,String venueName,String address_1,String address_2,String city,String state,
//                              String pincode,String region_id,String contact_name,String contact_number,String contact_email,
//                              String owner_name,String owner_number,String clas,String seating_capacity,String website,
//                              String past_occupancy,String lat,String lng,String venue_status){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_VENUE_NAME,venueName);
//        values.put(DBConstants.KEY_SERVER_VENUE_ID,id);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_1,address_1);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_2, address_2);
//        values.put(DBConstants.KEY_VENUE_CITY,city);
//        values.put(DBConstants.KEY_VENUE_STATE,state);
//        values.put(DBConstants.KEY_VENUE_PINCODE,pincode);
//        values.put(DBConstants.KEY_VENUE_REGION_ID,region_id);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NAME,contact_name);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NUMBER,contact_number);
//        values.put(DBConstants.KEY_VENUE_CONTACT_EMAIL,contact_email);
//        values.put(DBConstants.KEY_VENUE_OWNER_NAME,owner_name);
//        values.put(DBConstants.KEY_VENUE_OWNER_NUMBER,owner_number);
//        values.put(DBConstants.KEY_VENUE_CLASS,clas);
//        values.put(DBConstants.KEY_VENUE_SEATING_CAPACITY,seating_capacity);
//        values.put(DBConstants.KEY_VENUE_WEBSITE,website);
//        values.put(DBConstants.KEY_VENUE_SERVERSYNC,"yes");
//        values.put(DBConstants.KEY_VENUE_PAST_OCCUPANCY,past_occupancy);
//        values.put(DBConstants.KEY_VENUE_LAT,lat);
//        values.put(DBConstants.KEY_VENUE_LNG,lng);
//        values.put(DBConstants.KEY_VENUE_STATUS, venue_status);
//
//        //database.insert(TABLE_VENUE, null, toggle);
//
//        database.update(TABLE_VENUE, values, DBConstants.KEY_VENUE_NAME + "= '" + venueName + "'", null);
//
//    }
//    public int checkVenueisPresentById(String venueId){
//
//        String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_SERVER_VENUE_ID +"= '"+venueId+"' and "+DBConstants.KEY_VENUE_SERVERSYNC +"= 'yes'";
//        //   String selectQuery= "SELECT * FROM "+ TABLE_VENUE;
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        int countCursor=cursor.getColumnCount();
//
//        return countCursor;
//    }
//
//
//    public int checkVenueisPresent(String venueName){
//
//        String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_VENUE_NAME +"= '"+venueName+"' and "+DBConstants.KEY_VENUE_SERVERSYNC +"= 'yes'";
//        //   String selectQuery= "SELECT * FROM "+ TABLE_VENUE;
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        int countCursor=cursor.getColumnCount();
//
//       return countCursor;
//    }
//
//    public int checkUserisPresent(String userName){
//
//        String selectQuery= "SELECT * FROM "+ TABLE_USER + " WHERE "+DBConstants.KEY_USER_NAME +"= '"+userName+"' and "+DBConstants.KEY_USER_SERVERSYNC +"= 'yes'";
//        //   String selectQuery= "SELECT * FROM "+ TABLE_VENUE;
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        int countCursor=cursor.getColumnCount();
//
//        return countCursor;
//
//    }
//
//    public void updateUser(String userName,String userRole){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_USER_NAME,userName);
//        values.put(DBConstants.KEY_USER_ROLE,userRole);
//        values.put(DBConstants.KEY_USER_SERVERSYNC, "yes");
//
//        // database.insert(TABLE_USER, null, toggle);
//
//        database.update(TABLE_USER, values, DBConstants.KEY_USER_NAME + "= '" + userName + "'", null);
//    }
//
//    public void downloadUser(String userName,String userRole){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_USER_NAME,userName);
//        values.put(DBConstants.KEY_USER_ROLE,userRole);
//        values.put(DBConstants.KEY_USER_SERVERSYNC,"yes");
//
//        database.insert(TABLE_USER, null, values);
//    }
//
//    public void createUserRequestServerSync(String id,String userName,String passWord,String deviceImei,String mobileNo,
//                                            String userRole,String accessToken,String userStatus,String employeeId){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_REQUEST_USERNAME,userName);
//        values.put(DBConstants.KEY_REQUEST_USERID,id);
//        values.put(DBConstants.KEY_REQUEST_PASSWORD,passWord);
//        values.put(DBConstants.KEY_REQUEST_DEVICE_IMEI,deviceImei);
//        values.put(DBConstants.KEY_REQUEST_MOBILENO,mobileNo);
//        values.put(DBConstants.KEY_REQUEST_USER_ROLE,userRole);
//        values.put(DBConstants.KEY_REQUEST_ACCESS_TOKEN,accessToken);
//        values.put(DBConstants.KEY_REQUEST_USER_STATUS,userStatus);
//        values.put(DBConstants.KEY_REQUEST_EMPLOYEE_ID, employeeId);
//
//        database.insert(TABLE_USER_REQUEST, null, values);
//
//    }
//
//    public void createVenueServerSync(String venueName,String  venueAddress,String venueCity,String venueState,
//                                      String venuePin,String venueRegion,String contactName,String contactNumber,
//                                      String contactEmail,String ownerName,String ownerNumber,String venueClass,
//                                      String seatingCapacity,String venueWebsite,String pastOccupancy){
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_VENUE_NAME,venueName);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_1,venueAddress);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_2, venueAddress);
//        values.put(DBConstants.KEY_VENUE_CITY,venueCity);
//        values.put(DBConstants.KEY_VENUE_STATE,venueState);
//        values.put(DBConstants.KEY_VENUE_PINCODE,venuePin);
//        values.put(DBConstants.KEY_VENUE_REGION_ID,venueRegion);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NAME,contactName);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NUMBER,contactNumber);
//        values.put(DBConstants.KEY_VENUE_CONTACT_EMAIL,contactEmail);
//        values.put(DBConstants.KEY_VENUE_OWNER_NAME,ownerName);
//        values.put(DBConstants.KEY_VENUE_OWNER_NUMBER,ownerNumber);
//        values.put(DBConstants.KEY_VENUE_CLASS,venueClass);
//        values.put(DBConstants.KEY_VENUE_SEATING_CAPACITY,seatingCapacity);
//        values.put(DBConstants.KEY_VENUE_WEBSITE,venueWebsite);
//        values.put(DBConstants.KEY_VENUE_IMAGE_PATH,"");
//        values.put(DBConstants.KEY_VENUE_SERVERSYNC,"yes");
//        values.put(DBConstants.KEY_VENUE_PAST_OCCUPANCY,pastOccupancy);
//
//        database.insert(TABLE_VENUE, null, values);
//
//        showvenue();
//
//    }
//
//    public void createVenue(String venueName,String  venueAddress,String venueCity,String venueState,
//                            String venuePin,String venueRegion,String contactName,String contactNumber,
//                            String contactEmail,String ownerName,String ownerNumber,String venueClass,
//                            String seatingCapacity,String venueWebsite,String pastOccupancy){
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_VENUE_NAME,venueName);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_1,venueAddress);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_2, venueAddress);
//        values.put(DBConstants.KEY_VENUE_CITY,venueCity);
//        values.put(DBConstants.KEY_VENUE_STATE,venueState);
//        values.put(DBConstants.KEY_VENUE_PINCODE,venuePin);
//        values.put(DBConstants.KEY_VENUE_REGION_ID,venueRegion);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NAME,contactName);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NUMBER,contactNumber);
//        values.put(DBConstants.KEY_VENUE_CONTACT_EMAIL,contactEmail);
//        values.put(DBConstants.KEY_VENUE_OWNER_NAME,ownerName);
//        values.put(DBConstants.KEY_VENUE_OWNER_NUMBER,ownerNumber);
//        values.put(DBConstants.KEY_VENUE_CLASS,venueClass);
//        values.put(DBConstants.KEY_VENUE_SEATING_CAPACITY,seatingCapacity);
//        values.put(DBConstants.KEY_VENUE_WEBSITE,venueWebsite);
//        values.put(DBConstants.KEY_VENUE_IMAGE_PATH,"");
//        values.put(DBConstants.KEY_VENUE_SERVERSYNC,"no");
//        values.put(DBConstants.KEY_VENUE_PAST_OCCUPANCY,pastOccupancy);
//
//        database.insert(TABLE_VENUE, null, values);
//
//        showvenue1();
//
//    }
//
//    public void updateVenueStatusServerSync(String venueNme,String scheduleVenueStatus){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_VENUE_NAME,venueNme);
//
//        values.put(DBConstants.KEY_VENUE_SERVERSYNC,"yes");
//
//        values.put(DBConstants.KEY_VENUE_STATUS, scheduleVenueStatus);
//
//        database.update(TABLE_VENUE, values, DBConstants.KEY_VENUE_NAME + "= '" + venueNme + "'", null);
//    }
//
//    public void updateVenueStatus(String venueNme,String scheduleVenueStatus){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_VENUE_NAME,venueNme);
//
//        values.put(DBConstants.KEY_VENUE_SERVERSYNC,"no");
//
//        values.put(DBConstants.KEY_VENUE_STATUS, scheduleVenueStatus);
//
//        database.update(TABLE_VENUE, values, DBConstants.KEY_VENUE_NAME + "= '" + venueNme + "'", null);
//    }
//
//    public void updateVenue(String venueNme,String venueAdrs,String venueCity,String venueState,String venuePincode,
//                            String regionId,String contactName,String contactNumber,String contactEmail,String ownerName,
//                            String ownerNumber,String venueClass,String seatingcapacity,String venueWebsite,
//                            String pastOccumancy,String imageFilePath,String scheduleVenueStatus){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_VENUE_NAME,venueNme);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_1,venueAdrs);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_2, venueAdrs);
//        values.put(DBConstants.KEY_VENUE_CITY,venueCity);
//        values.put(DBConstants.KEY_VENUE_STATE,venueState);
//        values.put(DBConstants.KEY_VENUE_PINCODE,venuePincode);
//        values.put(DBConstants.KEY_VENUE_REGION_ID,regionId);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NAME,contactName);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NUMBER,contactNumber);
//        values.put(DBConstants.KEY_VENUE_CONTACT_EMAIL,contactEmail);
//        values.put(DBConstants.KEY_VENUE_OWNER_NAME,ownerName);
//        values.put(DBConstants.KEY_VENUE_OWNER_NUMBER,ownerNumber);
//        values.put(DBConstants.KEY_VENUE_CLASS,venueClass);
//        values.put(DBConstants.KEY_VENUE_SEATING_CAPACITY,seatingcapacity);
//        values.put(DBConstants.KEY_VENUE_WEBSITE,venueWebsite);
//        values.put(DBConstants.KEY_VENUE_IMAGE_PATH,imageFilePath);
//        values.put(DBConstants.KEY_VENUE_SERVERSYNC,"no");
//        values.put(DBConstants.KEY_VENUE_PAST_OCCUPANCY,pastOccumancy);
//        values.put(DBConstants.KEY_VENUE_STATUS, scheduleVenueStatus);
//
//        database.update(TABLE_VENUE, values, DBConstants.KEY_VENUE_NAME + "= '" + venueNme + "'", null);
//    }
//
//    public void updateVenue(String venueNme,String venueAdrs,String venueCity,String venueState,String venuePincode,
//                            String regionId,String contactName,String contactNumber,String contactEmail,String ownerName,
//                            String ownerNumber,String venueClass,String seatingcapacity,String venueWebsite,
//                            String pastOccumancy,String scheduleVenueStatus){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_VENUE_NAME,venueNme);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_1,venueAdrs);
//        values.put(DBConstants.KEY_VENUE_ADDRESS_2, venueAdrs);
//        values.put(DBConstants.KEY_VENUE_CITY,venueCity);
//        values.put(DBConstants.KEY_VENUE_STATE,venueState);
//        values.put(DBConstants.KEY_VENUE_PINCODE,venuePincode);
//        values.put(DBConstants.KEY_VENUE_REGION_ID,regionId);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NAME,contactName);
//        values.put(DBConstants.KEY_VENUE_CONTACT_NUMBER,contactNumber);
//        values.put(DBConstants.KEY_VENUE_CONTACT_EMAIL,contactEmail);
//        values.put(DBConstants.KEY_VENUE_OWNER_NAME,ownerName);
//        values.put(DBConstants.KEY_VENUE_OWNER_NUMBER,ownerNumber);
//        values.put(DBConstants.KEY_VENUE_CLASS,venueClass);
//        values.put(DBConstants.KEY_VENUE_SEATING_CAPACITY,seatingcapacity);
//        values.put(DBConstants.KEY_VENUE_WEBSITE,venueWebsite);
//
//        values.put(DBConstants.KEY_VENUE_SERVERSYNC,"no");
//        values.put(DBConstants.KEY_VENUE_PAST_OCCUPANCY,pastOccumancy);
//        values.put(DBConstants.KEY_VENUE_STATUS, scheduleVenueStatus);
//
//        database.update(TABLE_VENUE, values, DBConstants.KEY_VENUE_NAME + "= '" + venueNme + "'", null);
//    }
//
//    public void updateVenueServerSync(String venueNme,String venueAdrs,String venueCity,String venueState,String venuePincode,
//                                      String regionId,String contactName,String contactNumber,String contactEmail,String ownerName,
//                                      String ownerNumber,String venueClass,String seatingcapacity,String venueWebsite,
//                                      String pastOccumancy,String imageFilePath,String scheduleVenueStatus){
//
//        try {
//
//            ContentValues values = new ContentValues();
//            values.put(DBConstants.KEY_VENUE_NAME,venueNme);
//            values.put(DBConstants.KEY_VENUE_ADDRESS_1,venueAdrs);
//            values.put(DBConstants.KEY_VENUE_ADDRESS_2, venueAdrs);
//            values.put(DBConstants.KEY_VENUE_CITY,venueCity);
//            values.put(DBConstants.KEY_VENUE_STATE,venueState);
//            values.put(DBConstants.KEY_VENUE_PINCODE,venuePincode);
//            values.put(DBConstants.KEY_VENUE_REGION_ID,regionId);
//            values.put(DBConstants.KEY_VENUE_CONTACT_NAME,contactName);
//            values.put(DBConstants.KEY_VENUE_CONTACT_NUMBER,contactNumber);
//            values.put(DBConstants.KEY_VENUE_CONTACT_EMAIL,contactEmail);
//            values.put(DBConstants.KEY_VENUE_OWNER_NAME,ownerName);
//            values.put(DBConstants.KEY_VENUE_OWNER_NUMBER,ownerNumber);
//            values.put(DBConstants.KEY_VENUE_CLASS,venueClass);
//            values.put(DBConstants.KEY_VENUE_SEATING_CAPACITY,seatingcapacity);
//            values.put(DBConstants.KEY_VENUE_WEBSITE,venueWebsite);
//            values.put(DBConstants.KEY_VENUE_IMAGE_PATH,imageFilePath);
//            values.put(DBConstants.KEY_VENUE_SERVERSYNC,"yes");
//            values.put(DBConstants.KEY_VENUE_PAST_OCCUPANCY,pastOccumancy);
//            values.put(DBConstants.KEY_VENUE_STATUS, scheduleVenueStatus);
//
//            database.update(TABLE_VENUE, values, DBConstants.KEY_VENUE_NAME + "= '" + venueNme + "'", null);
//        }
//        catch(Exception e){
//            Log.e("exce---",e.toString());
//        }
//
//    }
//
//    public void createUser(String userName,String password,String role,String accessToken,String status,
//                           String employeeId){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_USER_NAME,userName);
//        values.put(DBConstants.KEY_USER_PASSWORD,password);
//        values.put(DBConstants.KEY_USER_ROLE,role);
//        values.put(DBConstants.KEY_USER_ACCESS_TOKEN,accessToken);
//        values.put(DBConstants.KEY_USER_STATUS,status);
//        values.put(DBConstants.KEY_USER_SERVERSYNC,"no");
//        values.put(DBConstants.KEY_USER_EMPLOYEE_ID,employeeId);
//
//        database.insert(TABLE_USER,null,values);
//    }
//
//
//    public void createUserServerSync(String userName,String password,String role,String accessToken,String status,String employeeId){
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_USER_NAME,userName);
//        values.put(DBConstants.KEY_USER_PASSWORD,password);
//        values.put(DBConstants.KEY_USER_ROLE,role);
//        values.put(DBConstants.KEY_USER_ACCESS_TOKEN,accessToken);
//        values.put(DBConstants.KEY_USER_STATUS,status);
//        values.put(DBConstants.KEY_USER_SERVERSYNC,"yes");
//        values.put(DBConstants.KEY_USER_EMPLOYEE_ID, employeeId);
//        database.insert(TABLE_USER, null, values);
//
//
//    }
//
////    public void createRegion(String regionName,String regionId){
////        ContentValues toggle = new ContentValues();
////        toggle.put(DBConstants.KEY_REGION_NAME,regionName);
////        toggle.put(DBConstants.KEY_SERVER_REGION_ID,regionId);
////        toggle.put(DBConstants.KEY_REGION_SERVERSYNC,"no");
////        database.insert(TABLE_REGION, null, toggle);
////
////        showRegion();
////    }
//
//    public int checkRegionisPresent(String regionName){
//        int countCursor=0;
//        try {
//            String selectQuery = "SELECT * FROM " + TABLE_REGION + " WHERE " + DBConstants.KEY_REGION_NAME + "= '" + regionName + "' and " + DBConstants.KEY_REGION_SERVERSYNC + "= 'yes'";
//            //   String selectQuery= "SELECT * FROM "+ TABLE_VENUE;
//
//            Cursor cursor = database.rawQuery(selectQuery, null);
//
//             countCursor = cursor.getColumnCount();
//
//            Log.e("-----------",countCursor+"");
//
//          //  showRegion();
//        }catch (Exception e){
//            Log.e("eeee",e.toString());
//        }
//
//        return countCursor;
//    }

//    public void updateRegionServerSync(String regionName,String regionId){
//        ContentValues toggle = new ContentValues();
//        toggle.put(DBConstants.KEY_REGION_NAME,regionName);
//        toggle.put(DBConstants.KEY_SERVER_REGION_ID,regionId);
//        toggle.put(DBConstants.KEY_REGION_SERVERSYNC,"yes");
//
//        database.update(TABLE_REGION, toggle, DBConstants.KEY_REGION_NAME + "= '" + regionName + "'", null);
//
//       // database.insert(TABLE_REGION, null, toggle);
//
//        showRegion();
//    }

//    public void createRegionServerSync(String regionName,String regionId){
//        ContentValues toggle = new ContentValues();
//        toggle.put(DBConstants.KEY_REGION_NAME,regionName);
//        toggle.put(DBConstants.KEY_SERVER_REGION_ID,regionId);
//        toggle.put(DBConstants.KEY_REGION_SERVERSYNC,"yes");
//        database.insert(TABLE_REGION, null, toggle);
//
//        showRegion();
//    }




//    public void updateVenue(CreateVenueDto createVenueDto){
//
//        ContentValues toggle = new ContentValues();
//        toggle.put(DBConstants.KEY_VENUE_NAME,createVenueDto.getVenueName());
//        toggle.put(DBConstants.KEY_VENUE_ADDRESS_1,createVenueDto.getVenueAddress());
//        toggle.put(DBConstants.KEY_VENUE_ADDRESS_2, createVenueDto.getVenueAddress());
//        toggle.put(DBConstants.KEY_VENUE_CITY,createVenueDto.getVenueCity());
//        toggle.put(DBConstants.KEY_VENUE_STATE,createVenueDto.getVenueState());
//        toggle.put(DBConstants.KEY_VENUE_PINCODE,createVenueDto.getVenuePin());
//        toggle.put(DBConstants.KEY_VENUE_REGION_ID,createVenueDto.getVenueRegion());
//        toggle.put(DBConstants.KEY_VENUE_CONTACT_NAME,createVenueDto.getContactName());
//        toggle.put(DBConstants.KEY_VENUE_CONTACT_NUMBER,createVenueDto.getContactNumber());
//        toggle.put(DBConstants.KEY_VENUE_CONTACT_EMAIL,createVenueDto.getContactEmail());
//        toggle.put(DBConstants.KEY_VENUE_OWNER_NAME,createVenueDto.getOwnerName());
//        toggle.put(DBConstants.KEY_VENUE_OWNER_NUMBER,createVenueDto.getOwnerNumber());
//        toggle.put(DBConstants.KEY_VENUE_CLASS,createVenueDto.getVenueClass());
//        toggle.put(DBConstants.KEY_VENUE_SEATING_CAPACITY,createVenueDto.getSeatingCapacity());
//        toggle.put(DBConstants.KEY_VENUE_WEBSITE,createVenueDto.getVenueWebsite());
//        toggle.put(DBConstants.KEY_VENUE_IMAGE_PATH,createVenueDto.getImagePath());
//        toggle.put(DBConstants.KEY_VENUE_SERVERSYNC,"no");
//        toggle.put(DBConstants.KEY_VENUE_PAST_OCCUPANCY,createVenueDto.getPastOccupancy());
//        toggle.put(DBConstants.KEY_VENUE_STATUS,createVenueDto.getVenueStatus());
//
//
//
//
//        database.update(TABLE_VENUE, toggle, DBConstants.KEY_VENUE_NAME + "= '"+createVenueDto.getVenueName()+"'", null);
//    }



//




//    public String getVenueById(int id){
//        String venue=null;
//
//         String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_SERVER_VENUE_ID +"= '"+id+"'";
//     //   String selectQuery= "SELECT * FROM "+ TABLE_VENUE;
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        Log.e("count--", cursor.getCount() + "");
//        for (int i = 0; i < cursor.getCount(); i++) {
//
//            venue=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_NAME));
//          //  String Sync=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_SERVERSYNC));
//
//            Log.e("venue",venue);
//          //  Log.e("venue",Sync);
//
//
//        }
//
//
//        cursor.close();
//
//
//
//        return venue;
//    }
//
//
//    public String getVenueStatus(int id){
//        String venueStatus=null;
//
//        String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_SERVER_VENUE_ID +"= '"+id+"'";
//        //   String selectQuery= "SELECT * FROM "+ TABLE_VENUE;
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        Log.e("count--", cursor.getCount() + "");
//        for (int i = 0; i < cursor.getCount(); i++) {
//
//            venueStatus=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_STATUS));
//            //  String Sync=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_SERVERSYNC));
//
//            Log.e("venue",venueStatus);
//            //  Log.e("venue",Sync);
//
//
//        }
//
//
//        cursor.close();
//
//
//
//        return venueStatus;
//    }
//
//    public void showvenue1(){
//
//
//        String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_VENUE_SERVERSYNC +"= 'no'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        Log.e("count cursor--", cursor.getCount() + "");
//        Log.e("count--",cursor.getCount()+"");
//        for (int i = 0; i < cursor.getCount(); i++) {
//
//            String venue=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_NAME));
//            String Sync=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_SERVERSYNC));
//
//            Log.e("venue",venue);
//            Log.e("venue",Sync);
//
//
//        }
//
//
//        cursor.close();
//
//
//    }
//
//
//    public Cursor showVenuebyName(String venueName){
//
//
//        String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_VENUE_NAME +"= '"+venueName+"'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        Log.e("count--",cursor.getCount()+"");
////        for (int i = 0; i < cursor.getCount(); i++) {
////
////            String venue=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_NAME));
////
////
////
////
////
////
////        }
////
////
////        cursor.close();
//
//        return cursor;
//    }
//
//    public void showvenue(){
//
//
//        String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_VENUE_SERVERSYNC +"= 'yes'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        Log.e("count--",cursor.getCount()+"");
//        for (int i = 0; i < cursor.getCount(); i++) {
//
//            String venue=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_NAME));
//            String Sync=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_SERVERSYNC));
//
//            Log.e("venue",venue);
//            Log.e("venue",Sync);
//
//
//        }
//
//
//        cursor.close();
//
//
//    }
//
//    public int venueCount(String venueName){
//
//
//        String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_VENUE_SERVERSYNC +"= 'yes' AND "+
//        DBConstants.KEY_VENUE_NAME +"= '"+venueName+"'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        int count = cursor.getCount();
////        cursor.moveToFirst();
////        Log.e("count--",cursor.getCount()+"");
////        for (int i = 0; i < cursor.getCount(); i++) {
////
////            String venue=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_NAME));
////            String Sync=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_SERVERSYNC));
////
////            Log.e("venue",venue);
////            Log.e("venue",Sync);
////
////
////        }
////
////
////        cursor.close();
//
//        return count;
//
//
//    }
//
//    public int venueCount(){
//
//
//        String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_VENUE_SERVERSYNC +"= 'yes'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//        int count = cursor.getCount();
////        cursor.moveToFirst();
////        Log.e("count--",cursor.getCount()+"");
////        for (int i = 0; i < cursor.getCount(); i++) {
////
////            String venue=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_NAME));
////            String Sync=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_VENUE_SERVERSYNC));
////
////            Log.e("venue",venue);
////            Log.e("venue",Sync);
////
////
////        }
////
////
////        cursor.close();
//
//        return count;
//
//
//    }
//
//
////    public void createVenue(String venueName,String venueAddress,String venueCity,String venueState,String venuePin,String seating,String venueImagePath,double latitude,double longitude){
////        ContentValues toggle = new ContentValues();
////        toggle.put(DBConstants.KEY_VENUE_NAME,venueName);
////        toggle.put(DBConstants.KEY_VENUE_ADDRESS_1,venueAddress);
////        toggle.put(DBConstants.KEY_VENUE_CITY,venueCity);
////        toggle.put(DBConstants.KEY_VENUE_STATE,venueState);
////        toggle.put(DBConstants.KEY_VENUE_PINCODE,venuePin);
////        toggle.put(DBConstants.KEY_VENUE_SEATING_CAPACITY,seating);
////        toggle.put(DBConstants.KEY_VENUE_IMAGE_PATH,venueImagePath);
////        toggle.put(DBConstants.KEY_VENUE_LAT,latitude);
////        toggle.put(DBConstants.KEY_VENUE_LNG, longitude);
////
////
////        database.insert(TABLE_VENUE, null, toggle);
////
////        showVenue();
////
////
////    }
//
//    public int staffCount(){
//
//        String selectQuery= "SELECT * FROM "+ TABLE_USER + " WHERE "+DBConstants.KEY_USER_SERVERSYNC +"= 'yes'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        int cursorCount=cursor.getCount();
//
//        return cursorCount;
//    }
//
//    public int staffCount(String userName){
//
//        String selectQuery= "SELECT * FROM "+ TABLE_USER + " WHERE "+DBConstants.KEY_USER_SERVERSYNC +"= 'yes' AND "+
//                DBConstants.KEY_USER_NAME +"= '"+userName+"'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        int cursorCount=cursor.getCount();
//
//        return cursorCount;
//    }
//
//    public int regionCount(){
//
//        String selectQuery= "SELECT * FROM "+ TABLE_REGION + " WHERE "+DBConstants.KEY_REGION_SERVERSYNC +"= 'yes'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        int cursorCount=cursor.getCount();
//
//        return cursorCount;
//    }
//    public int regionCount(String regionName){
//
//        String selectQuery= "SELECT * FROM "+ TABLE_REGION + " WHERE "+DBConstants.KEY_REGION_SERVERSYNC +"= 'yes' AND "+
//                DBConstants.KEY_REGION_NAME +"= '"+regionName+"'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        int cursorCount=cursor.getCount();
//
//        return cursorCount;
//    }
//
//
////    public List<RegionListDto> showRegion(){
////
////        List<RegionListDto> regionListDtos=new ArrayList<RegionListDto>();
////
////        RegionListDto regionListDto;
////
////        String selectQuery= "SELECT * FROM "+ TABLE_REGION;
////        Cursor cursor=database.rawQuery(selectQuery,null);
////
////        Log.e("region count-", cursor.getCount() + "");
////        cursor.moveToFirst();
////        for (int i = 0; i < cursor.getCount(); i++) {
////            regionListDto=new RegionListDto(cursor);
////            regionListDtos.add(regionListDto);
////            Log.e("reg---",regionListDto+"");
////            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
////            cursor.moveToNext();
////        }
////
////
////        cursor.close();
////
////        return regionListDtos;
////    }
//
////    public List<UserListDto> showUser(){
////
////        List<UserListDto> userListDtos=new ArrayList<UserListDto>();
////
////        UserListDto userListDto;
////
////        String selectQuery= "SELECT * FROM "+ TABLE_USER + " WHERE "+DBConstants.KEY_USER_SERVERSYNC +"= 'yes'";
////        Cursor cursor=database.rawQuery(selectQuery,null);
////
////        Log.e("venue count-",cursor.getCount()+"");
////        cursor.moveToFirst();
////        for (int i = 0; i < cursor.getCount(); i++) {
////            userListDto=new UserListDto(cursor);
////            userListDtos.add(userListDto);
////            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
////            cursor.moveToNext();
////        }
////
////
////        cursor.close();
////
////        return userListDtos;
////    }
//
//    public Cursor showRequestByUser(String requestUsername){
//
//        String selectQuery = "SELECT * FROM "+ TABLE_USER_REQUEST +" WHERE "+DBConstants.KEY_REQUEST_USERNAME + "= '"+requestUsername+"'";
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        return cursor;
//    }
//
////    public List<RequestListDto> showAllRequest(){
////
////        List<RequestListDto> requestListDtos=new ArrayList<RequestListDto>();
////        RequestListDto requestListDto;
////        String selectQuery= "SELECT * FROM "+ TABLE_USER_REQUEST;
////        Cursor cursor=database.rawQuery(selectQuery,null);
////
////        Log.e("venue count-",cursor.getCount()+"");
////        cursor.moveToFirst();
////        for (int i = 0; i < cursor.getCount(); i++) {
////            requestListDto=new RequestListDto(cursor);
////            requestListDtos.add(requestListDto);
////            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
////            cursor.moveToNext();
////        }
////
////
////        cursor.close();
////
////
////
////
////        return requestListDtos;
////    }
//
//
////    public List<VenueListDto> showVenue(){
////
////        List<VenueListDto> venueListDtos=new ArrayList<VenueListDto>();
////
////        VenueListDto venueListDto;
////
////        String selectQuery= "SELECT * FROM "+ TABLE_VENUE + " WHERE "+DBConstants.KEY_VENUE_SERVERSYNC +"= 'yes'";
////        Cursor cursor=database.rawQuery(selectQuery,null);
////
////        Log.e("venue count-",cursor.getCount()+"");
////        cursor.moveToFirst();
////        for (int i = 0; i < cursor.getCount(); i++) {
////            venueListDto=new VenueListDto(cursor);
////            venueListDtos.add(venueListDto);
////            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
////            cursor.moveToNext();
////        }
////
////
////        cursor.close();
////
////    return venueListDtos;
////    }
//
//
//    public void createMandapam(String mandapamName,String mandapamAddress,String mandapamClass,String salesPersonId,String lati,String longi){
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_MANDAPAM_NAME,mandapamName);
//        values.put(DBConstants.KEY_MANDAPAM_CLASS,mandapamClass);
//        values.put(DBConstants.KEY_MANDAPAM_ADDRESS,mandapamAddress);
//        values.put(DBConstants.KEY_MANDAPAM_SALESPERSONID,salesPersonId);
//        values.put(DBConstants.KEY_MANDAPAM_SCHEDULEDATE,getDateTime());
//        values.put(DBConstants.KEY_MANDAPAM_LATITUDE, lati);
//        values.put(DBConstants.KEY_MANDAPAM_LONGITUDE, longi);
//
//        database.insert(TABLE_MANDAPAM, null, values);
//
//
//    }
//
//
//    public void updateStatus(String userId,String status){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_MANDAPAM_STATUS,status);
//
//
//        database.update(TABLE_MANDAPAM, values, DBConstants.KEY_MANDAPAM_SALESPERSONID + "='" + userId + "'", null);
//
//    }
//
//    public Cursor getMandapamDetails(String mandapamName){
//
//        String selectQuery = "SELECT * FROM " + TABLE_MANDAPAM + " WHERE " +DBConstants.KEY_MANDAPAM_NAME +
//                "= '"+ mandapamName+"'";
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//
//
//       return cursor;
//    }
//
//    public Cursor selectVenue(String venueName){
//
//        String selectQuery = "SELECT * FROM " + TABLE_VENUE + " WHERE "+DBConstants.KEY_VENUE_NAME +"= '"+venueName+"'";
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        return cursor;
//    }

//    public List<ScheduleVenueListDto> selectMandapamByEmployeeIdDate1(String employeeId){
//
//        List<ScheduleVenueListDto> scheduleVenueListDtos=new ArrayList<ScheduleVenueListDto>();
//        ScheduleVenueListDto scheduleVenueListDto;
//
//        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE_VENUE +" WHERE " +
//                DBConstants.KEY_SCHEDULE_EMPLOYEE_ID + "= '"+employeeId+"' AND "+
//                DBConstants.KEY_SCHEDULE_SERVERSYNC+ "='yes'";
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            scheduleVenueListDto=new ScheduleVenueListDto(cursor);
//            scheduleVenueListDtos.add(scheduleVenueListDto);
//            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
//            cursor.moveToNext();
//        }
//
//
//        cursor.close();
//
//        return scheduleVenueListDtos;
//    }

//    public List<ScheduleVenueListDto> selectMandapamByEmployeeIdDate1(String employeeId,String dateString){
//
//        List<ScheduleVenueListDto> scheduleVenueListDtos=new ArrayList<ScheduleVenueListDto>();
//        ScheduleVenueListDto scheduleVenueListDto;
//
//
//
//        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE_VENUE +" WHERE " +
//                DBConstants.KEY_SCHEDULE_EMPLOYEE_ID + "= '"+employeeId+"' AND "+
//                DBConstants.KEY_SCHEDULE_SERVERSYNC+ "='yes'";
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            scheduleVenueListDto=new ScheduleVenueListDto(cursor);
//            scheduleVenueListDtos.add(scheduleVenueListDto);
//            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
//            cursor.moveToNext();
//        }
//
//
//        cursor.close();
//
//        return scheduleVenueListDtos;
//    }

//    public List<ScheduleVenueListDto> selectMandapamByEmployeeIdDate(String employeeId){
//
//        List<ScheduleVenueListDto> scheduleVenueListDtos=new ArrayList<ScheduleVenueListDto>();
//        ScheduleVenueListDto scheduleVenueListDto;
//
//        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE_VENUE +" WHERE " +
//                DBConstants.KEY_SCHEDULE_EMPLOYEE_ID + "= '"+employeeId+"' AND "+
//
//                DBConstants.KEY_SCHEDULE_SERVERSYNC+ "='yes'";
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            scheduleVenueListDto=new ScheduleVenueListDto(cursor);
//            scheduleVenueListDtos.add(scheduleVenueListDto);
//            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
//            cursor.moveToNext();
//        }
//
//
//        cursor.close();
//
//        return scheduleVenueListDtos;
//    }


//    public List<ScheduleVenueListDto> selectMandapamByEmployeeIdDate(String employeeId,String dateString){
//
//        List<ScheduleVenueListDto> scheduleVenueListDtos=new ArrayList<ScheduleVenueListDto>();
//        ScheduleVenueListDto scheduleVenueListDto;
//
////        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE_VENUE +" WHERE " +
////                DBConstants.KEY_SCHEDULE_EMPLOYEE_ID + "= '"+employeeId+"' AND "+
////                DBConstants.KEY_SCHEDULE_FOLLOWUP + " LIKE '"+dateString+"%' AND "+
////                DBConstants.KEY_SCHEDULE_SERVERSYNC+ "='yes'";
//
//        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE_VENUE +" WHERE " +
//                DBConstants.KEY_SCHEDULE_EMPLOYEE_ID + "= '"+employeeId+"' AND "+
//                DBConstants.KEY_SCHEDULE_FOLLOWUP + " LIKE '"+dateString+"%' AND "+
//                DBConstants.KEY_SCHEDULE_SERVERSYNC+ "='yes'";
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            scheduleVenueListDto=new ScheduleVenueListDto(cursor);
//            scheduleVenueListDtos.add(scheduleVenueListDto);
//            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
//            cursor.moveToNext();
//        }
//
//
//        cursor.close();
//
//        return scheduleVenueListDtos;
//    }



//    public List<ScheduleVenueListDto> selectMandapamByEmployeeId(String employeeId){
//
//        List<ScheduleVenueListDto> scheduleVenueListDtos=new ArrayList<ScheduleVenueListDto>();
//        ScheduleVenueListDto scheduleVenueListDto;
//
//        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE_VENUE +" WHERE " +
//                DBConstants.KEY_SCHEDULE_EMPLOYEE_ID + "= '"+employeeId+"'AND "+
//                DBConstants.KEY_SCHEDULE_SERVERSYNC+ "='no' ORDER BY "+
//                DBConstants.KEY_SCHEDULE_FOLLOWUP;
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            scheduleVenueListDto=new ScheduleVenueListDto(cursor);
//            scheduleVenueListDtos.add(scheduleVenueListDto);
//            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
//            cursor.moveToNext();
//        }
//
//
//        cursor.close();
//
//        return scheduleVenueListDtos;
//    }
//
//    public List<MandapamListDto> selectMandapamBySalespersonId(String salesPersonId){
//
//        List<MandapamListDto> mandapamDtoArrayList=new ArrayList<MandapamListDto>();
//        MandapamListDto mandapamListDto;
//
//        String selectQuery = "SELECT * FROM " + TABLE_MANDAPAM +" WHERE " + DBConstants.KEY_MANDAPAM_SALESPERSONID +
//                "= '"+salesPersonId+"'";
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            mandapamListDto=new MandapamListDto(cursor);
//            mandapamDtoArrayList.add(mandapamListDto);
//            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
//            cursor.moveToNext();
//        }
//
//
//        cursor.close();
//
//       return mandapamDtoArrayList;
//    }

//    private String getDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Date date = new Date();
//        return dateFormat.format(date);
//    }
//
//    public void createUser(String userName,String passWord,String mobileNo,String deviceId){
//
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.KEY_USERS_NAME,userName);
//        values.put(DBConstants.KEY_USERS_PASSWORD,passWord);
//        values.put(DBConstants.KEY_USERS_MOBILE,mobileNo);
//        values.put(DBConstants.KEY_USERS_DEVICEID,deviceId);
//        values.put(DBConstants.KEY_USERS_STATUS,"no");
//
//        database.insert(TABLE_USERS, null, values);
//
//    }
//
//
//    public void updateLoginStatus(int loginStatus,String userName){
//
//        if(loginStatus==1) {
//
//            ContentValues values = new ContentValues();
//            values.put(DBConstants.KEY_USERS_STATUS, "yes");
//
//            database.update(TABLE_USERS, values, DBConstants.KEY_USERS_NAME + "='" + userName + "'", null);
//        }
//        else{
//            ContentValues values = new ContentValues();
//            values.put(DBConstants.KEY_USERS_STATUS, "no");
//
//            database.update(TABLE_USERS, values, DBConstants.KEY_USERS_NAME + "='" + userName + "'", null);
//        }
//    }
//
//    public String userName(){
//        String userName=null;
//
//        String selectQuery = "SELECT * FROM " + TABLE_USERS;
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                userName=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_USERS_NAME));
//
//
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        return userName;
//    }
//
//    public String userId(){
//        String userId=null;
//
//        String selectQuery = "SELECT * FROM " + TABLE_USERS;
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                userId=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_USERS_ID));
//
//
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        return userId;
//    }
//
//
//    public String loginStatus(){
//        String loginStatus=null;
//
//        String selectQuery = "SELECT * FROM " + TABLE_USERS;
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                loginStatus=cursor.getString(cursor.getColumnIndex(DBConstants.KEY_USERS_STATUS));
//
//
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//       return loginStatus;
//    }
//
//    public int countUser(){
//
//        String selectQuery = "SELECT * FROM " + TABLE_USERS;
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        int count=cursor.getCount();
//
//       return count;
//    }
//
//    public int validateUser(String userName,String passWord){
//
//
//        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " +DBConstants.KEY_USERS_NAME +
//                " = '"+userName+"' AND "+DBConstants.KEY_USERS_PASSWORD+" = '"+passWord+"'";
//
//        Cursor cursor=database.rawQuery(selectQuery,null);
//
//        int count=cursor.getCount();
//
//
//
//
////        String selectQuery = "SELECT  * FROM " + TABLE_SITE_INSPECTION+" where "
//////                +SmartDCRConstants.KEY_INEPECTION_UNIQUE_ID +" = '"+uniqId+"' AND "
//////                +SmartDCRConstants.KEY_INSPECTION_PARAMETER +" = '"+parameter+"'";
//////        Cursor cursor = database.rawQuery(selectQuery, null);
//
//
//
//        return count;
//    }





    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//    public void insertSiteInspection(String iD,String uniqId,String fileNumber,String section,String parameter,String min,String max,
//                                     String actual,String measured,String violation,String violationPersentage,String result,
//                                     String severityStatus,String submissionType){
//
//        ContentValues toggle = new ContentValues();
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_ID, iD);
//        toggle.put(SmartDCRConstants.KEY_INEPECTION_UNIQUE_ID, uniqId);
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_FILENUMBER, fileNumber);
//
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_SECTION, section);
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_PARAMETER, parameter);
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_MIN, min);
//
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_MAX, max);
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_ACTUAL, actual);
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_MEASURED, measured);
//
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_VIOLATION, violation);
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_VIOLATION_PERCENTAGE, violationPersentage);
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_RESULT, result);
//
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_SEVERITY_STATUS, severityStatus);
//        toggle.put(SmartDCRConstants.KEY_INSPECTION_SUBMISSIONTYPE, submissionType);
//
//        database.insert(TABLE_SITE_INSPECTION, null, toggle);
//        //    showInspection();
//
//
//    }
//
//
//    public void updateServerSyncInspection(String parameter,String severity,String measured,String inspectionid){
//
//
//        ContentValues toggle = new ContentValues();
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_ID,inspectionid);
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_PARAMETER,parameter);
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_MEASURED,measured);
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_SEVERITY_STATUS,severity);
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_SERVER_SYNC,"yes");
//        database.insert(TABLE_SITE_INSPECTED, null, toggle);
//
//        Log.e("Update", "update");
//
//
//
//    }
//
//    public void updateInspection(String parameter,String severity,String measured,String inspectionid){
//
//
//        ContentValues toggle = new ContentValues();
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_ID,inspectionid);
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_PARAMETER,parameter);
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_MEASURED,measured);
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_SEVERITY_STATUS,severity);
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_SERVER_SYNC,"no");
//        database.insert(TABLE_SITE_INSPECTED, null, toggle);
//
//        Log.e("Update","update");
//
//
//
//    }
//
//
//    public Cursor inspectedSiteData(){
//
//        String selectQuery = "SELECT  * FROM " + TABLE_SITE_INSPECTED+" where "
//                +SmartDCRConstants.KEY_INSPECTED_SERVER_SYNC +" = 'no'";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//
//
//        return cursor;
//    }
//
//
//
//    public void insertSiteData(String uniqId,String drawing,String stage1_status){
//
//        ContentValues toggle = new ContentValues();
//        toggle.put(SmartDCRConstants.KEY_SITE_UNIQ_ID, uniqId);
//        toggle.put(SmartDCRConstants.KEY_SITE_DRAWING, drawing);
//        toggle.put(SmartDCRConstants.KEY_SITE_STATUS, stage1_status);
//        database.insert(TABLE_SITE_DATA, null, toggle);
//        showData();
//    }
//
//    public void updateInspectedData(String inspectionId){
//
//        ContentValues toggle = new ContentValues();
//        toggle.put(SmartDCRConstants.KEY_INSPECTED_SERVER_SYNC, "yes");
//
//        database.update(TABLE_SITE_INSPECTED, toggle, SmartDCRConstants.KEY_INSPECTED_ID + "='" + inspectionId + "'", null);
//    }
//
//    public Cursor getData(String parameter,String uniqId){
//
//        String selectQuery = "SELECT  * FROM " + TABLE_SITE_INSPECTION+" where "
//                +SmartDCRConstants.KEY_INEPECTION_UNIQUE_ID +" = '"+uniqId+"' AND "
//                +SmartDCRConstants.KEY_INSPECTION_PARAMETER +" = '"+parameter+"'";
//        Cursor cursor = database.rawQuery(selectQuery, null);
////        cursor.moveToFirst();
////        int count=cursor.getCount();
////
////        if (cursor.moveToFirst()) {
////            do {
////
////
////
////
////            } while (cursor.moveToNext());
////        }
////        cursor.close();
//
//        return cursor;
//    }
//
//    public List showInspectionById(String uniqId) {
//        List parameterList=new ArrayList();
//
//        //String selectQuery = "SELECT  * FROM  " + TABLE_INSPECTION_GODOWN +" where "+InspectionConstants.KEY_GODOWN_TALUK_ID +" = "+talukId ;
//
//        String selectQuery = "SELECT  * FROM " + TABLE_SITE_INSPECTION+" where "+SmartDCRConstants.KEY_INEPECTION_UNIQUE_ID +" = '"+uniqId+"'";
//        Cursor cursor = database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        int count=cursor.getCount();
//        Log.e("evidence count", count + "");
//        if (cursor.moveToFirst()) {
//            do {
//                Log.e(" ### uniq id=====", cursor.getString(cursor.getColumnIndex(SmartDCRConstants.KEY_INEPECTION_UNIQUE_ID)));
//                Log.e(" ### stage1_status =====", cursor.getString(cursor.getColumnIndex(SmartDCRConstants.KEY_INSPECTION_ID)));
//
//                String parameter=cursor.getString(cursor.getColumnIndex(SmartDCRConstants.KEY_INSPECTION_PARAMETER));
//
//                parameterList.add(parameter);
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        return parameterList;
//    }
//
//    public void showInspection() {
//
//        String selectQuery = "SELECT  * FROM " + TABLE_SITE_INSPECTION;
//        Cursor cursor = database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        int count=cursor.getCount();
//        Log.e("evidence count", count + "");
//        if (cursor.moveToFirst()) {
//            do {
//                Log.e(" ### uniq id=====", cursor.getString(cursor.getColumnIndex(SmartDCRConstants.KEY_INEPECTION_UNIQUE_ID)));
//                Log.e(" ### stage1_status =====", cursor.getString(cursor.getColumnIndex(SmartDCRConstants.KEY_INSPECTION_ID)));
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }
//
//
//    public byte[] getPdfArray(String uniqId){
//
//        byte[] bytePdf=null;
//
//        String selectQuery = "SELECT  * FROM " + TABLE_SITE_DATA+" where "+SmartDCRConstants.KEY_SITE_UNIQ_ID +" = '"+uniqId+"'";
//        Cursor cursor = database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        int count=cursor.getCount();
//        Log.e("evidence count--==++++", count + "");
//        if (cursor.moveToFirst()) {
//            do {
//
//                bytePdf = cursor.getBlob(cursor.getColumnIndex(SmartDCRConstants.KEY_SITE_DRAWING));
//                //okbytePdf =cursor.
////                Log.e(" ### uniq id=====", cursor.getString(cursor.getColumnIndex(SmartDCRConstants.KEY_SITE_UNIQ_ID)));
////                Log.e(" ### stage1_status =====", cursor.getString(cursor.getColumnIndex(SmartDCRConstants.KEY_SITE_STATUS)));
//                //(assuming you have a ResultSet named RS)
//
//               /* Blob blob = cursor.getBlob(cursor.getColumnIndex(SmartDCRConstants.KEY_SITE_DRAWING));
//
//                int blobLength = (int) blob.length();
//                byte[] blobAsBytes = blob.getBytes(1, blobLength);
//
////release the blob and free up memory. (since JDBC 4.0)
//                blob.free();*/
//
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        return bytePdf;
//
//        //return blobAsBytes;
//    }
//
//    public void showData() {
//
//        String selectQuery = "SELECT  * FROM " + TABLE_SITE_DATA;
//        Cursor cursor = database.rawQuery(selectQuery,null);
//        cursor.moveToFirst();
//        int count=cursor.getCount();
//        Log.e("evidence count", count + "");
//        if (cursor.moveToFirst()) {
//            do {
//                Log.e(" ### uniq id=====", cursor.getString(cursor.getColumnIndex(SmartDCRConstants.KEY_SITE_UNIQ_ID)));
//                Log.e(" ### stage1_status =====", cursor.getString(cursor.getColumnIndex(SmartDCRConstants.KEY_SITE_STATUS)));
//
//
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }
//
//    public List<InspectionDataDto> showList() {
//
//        List<InspectionDataDto> inspectionDataDtoArrayList=new ArrayList<InspectionDataDto>();
//        InspectionDataDto inspectionDataDto;
//        String selectQuery = "SELECT  * FROM " + TABLE_SITE_DATA;
//        Cursor cursor = database.rawQuery(selectQuery,null);
//
//        //   int count=cursor.getCount();
//        //   Log.e("evidence count",count+"");
//
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            inspectionDataDto=new InspectionDataDto(cursor);
//            inspectionDataDtoArrayList.add(inspectionDataDto);
//            //    Log.e("inspection Act",inspectionDataDtoArrayList+"");
//            cursor.moveToNext();
//        }
//
//        cursor.close();
//
//        return inspectionDataDtoArrayList;
//    }




    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    //insert login data inside database
//    public void insertData(LoginResponseDto loginResponse) {
//
//        ContentValues toggle = new ContentValues();
//        toggle.put(SmartDCRConstants.KEY_USERS_NAME, loginResponse.getUserDetailDto().getUsername());
//        toggle.put(SmartDCRConstants.KEY_USERS_PASS_HASH, loginResponse.getUserDetailDto().getPassword());
//        database.insertWithOnConflict(TABLE_USERS, SmartDCRConstants.KEY_USERS_NAME, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//        Log.i("P", loginResponse.getUserDetailDto().getPassword());
//
//    }
//
//    public int showData() {
//        SQLiteDatabase database = this.getWritableDatabase();
//        String selectQuery = "SELECT  * FROM " + TABLE_USERS;
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        return cursor.getCount();
//    }
//
//    // Used to retrieve data when no network available in device
//    public String retrieveData(String userName) {
//        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " where " + SmartDCRConstants.KEY_USERS_NAME + " = " + "\"" + userName + "\"";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        LoginResponseDto loginResponse;
//        if (cursor.moveToFirst()) {
//            loginResponse = new LoginResponseDto(cursor);
//            loginResponse.setUserDetailDto(new UserDetailDto(cursor));
//            return loginResponse.getUserDetailDto().getPassword();
//        }
//        cursor.close();
//        return null;
//    }
//
//    //  This function returns true if  Bill ,Product ,Beneficiary  and Allotment tables are empty.
//    public boolean getBillProductBeneficiaryAllotData() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return (db.rawQuery("SELECT * FROM " + TABLE_BILL, null).getCount()) == 0 && (db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null).getCount()) == 0 && (db.rawQuery("SELECT * FROM " + TABLE_BENEFICIARY, null).getCount()) == 0 && (db.rawQuery("SELECT * FROM " + TABLE_ALLOTMENT, null).getCount()) == 0 && (db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION_TYPE, null).getCount()) == 0;
//    }
//
//    //This function inserts details to TABLE_BILL,;
//
//    public void insertBillData(FpsDataDto fpsDataDto) {
//        try {
//
//
//            List<BillDto> billList = new ArrayList<BillDto>(fpsDataDto.getBillDto());
//            for (BillDto bill : billList) {
//                ContentValues toggle = new ContentValues();
//                toggle.put(KEY_ID, bill.getId());
//                toggle.put(SmartDCRConstants.KEY_BILL_FPS_ID, bill.getFpsId());
//                toggle.put(SmartDCRConstants.KEY_BILL_DATE, bill.getBillDate());
//                toggle.put(SmartDCRConstants.KEY_BILL_CREATED_BY, bill.getCreatedby());
//                toggle.put(SmartDCRConstants.KEY_BILL_TIME_STAMP, bill.getBillDate());
//               // toggle.put("transactionId", bill.getTransactionId());
//                DateTime dates = new DateTime(Long.parseLong(bill.getBillDate()));
//                if (dates.getYear() == new DateTime().getYear()) {
//                    Log.e("Date", dates + ":" + dates.getYear() + ":" + dates.getMonthOfYear());
//                    toggle.put(SmartDCRConstants.KEY_BILL_TIME_MONTH, dates.getMonthOfYear());
//                }
//                toggle.put(SmartDCRConstants.KEY_BILL_AMOUNT, bill.getAmount());
//                toggle.put(SmartDCRConstants.KEY_BILL_MODE, bill.getBillDate());
//                toggle.put(SmartDCRConstants.KEY_BILL_CHANNEL, String.valueOf(bill.getChannel()));
//                toggle.put(SmartDCRConstants.KEY_BILL_BENEFICIARY, bill.getBeneficiaryId());
//                toggle.put(SmartDCRConstants.KEY_BILL_CREATED_DATE, bill.getCreatedDate());
//                toggle.put(SmartDCRConstants.KEY_BILL_STATUS, "T");
//                //  database.insert(TABLE_BILL, null, toggle);
//                database.insertWithOnConflict(TABLE_BILL, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//                String selectQuery = "SELECT  * FROM " + TABLE_BILL + " order by " + SmartDCRConstants.KEY_BILL_REF_ID + " DESC limit 1";
//                Cursor cursor = database.rawQuery(selectQuery, null);
//                cursor.moveToFirst();
//                BillDto billNew = new BillDto(cursor);
//                insertBillItems(bill.getBillItemDto(), billNew.getBillReferenceId(), billNew.getBillMonth(), billNew.getBeneficiaryId(),bill.getBillDate());
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    //This function inserts details to TABLE_BILL;
//
//    public boolean insertBill(BillDto bill) {
//        try {
//            ContentValues toggle = new ContentValues();
//            toggle.put(KEY_ID, bill.getId());
//            toggle.put(SmartDCRConstants.KEY_BILL_FPS_ID, bill.getFpsId());
//            toggle.put(SmartDCRConstants.KEY_BILL_DATE, bill.getBillDate());
//            toggle.put(SmartDCRConstants.KEY_BILL_CREATED_BY, bill.getCreatedby());
//            toggle.put(SmartDCRConstants.KEY_BILL_AMOUNT, bill.getAmount());
//            toggle.put(SmartDCRConstants.KEY_BILL_MODE, String.valueOf(bill.getMode()));
//            toggle.put(SmartDCRConstants.KEY_BILL_CHANNEL, String.valueOf(bill.getChannel()));
//            toggle.put(SmartDCRConstants.KEY_BILL_TIME_STAMP, new Date().getTime());
//            toggle.put(SmartDCRConstants.KEY_BILL_CREATED_DATE, bill.getCreatedDate());
//            toggle.put(SmartDCRConstants.KEY_BILL_TIME_MONTH, new DateTime().getMonthOfYear());
//            toggle.put(SmartDCRConstants.KEY_BILL_BENEFICIARY, bill.getBeneficiaryId());
//          //  toggle.put("transactionId", bill.getTransactionId());
//            toggle.put(SmartDCRConstants.KEY_BILL_STATUS, "R");
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_QR_CODE, bill.getQrCode());
//            if (!database.isOpen()) {
//                database = dbHelper.getWritableDatabase();
//            }
//            //   database.insert(TABLE_BILL, null, toggle);
//
//            database.insertWithOnConflict(TABLE_BILL, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//            String selectQuery = "SELECT  * FROM " + TABLE_BILL + " order by " + SmartDCRConstants.KEY_BILL_REF_ID + " DESC limit 1";
//            Cursor cursor = database.rawQuery(selectQuery, null);
//            cursor.moveToFirst();
//            Log.e("Bills",bill.getBillDate());
//            BillDto billNew = new BillDto(cursor);
//            insertBillItems(bill.getBillItemDto(), billNew.getBillReferenceId(), billNew.getBillMonth(), billNew.getBeneficiaryId(),bill.getBillDate());
//            cursor.close();
//            return true;
//        } catch (Exception e) {
//            Util.LoggingQueue(contextValue, "Error", e.toString());
//            Log.e("Error", e.toString(), e);
//            return false;
//        }
//
//    }
//
//    //Get Beneficiary data by QR Code
//    public BillDto lastGenBill() {
//        String selectQuery = "SELECT  * FROM " + TABLE_BILL + " order by refId DESC limit 1";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        if (cursor.getCount() == 0) {
//            return null;
//        } else {
//            BillDto beneficiary = new BillDto(cursor);
//            return beneficiary;
//        }
//    }
//
//   //Update the bill
//    public void billUpdate(BillDto bill){
//        ContentValues toggle = new ContentValues();
//        toggle.put(KEY_ID, bill.getId());
//        toggle.put(SmartDCRConstants.KEY_BILL_FPS_ID, bill.getFpsId());
//        toggle.put(SmartDCRConstants.KEY_BILL_DATE, bill.getBillDate());
//        toggle.put(SmartDCRConstants.KEY_BILL_CREATED_BY, bill.getCreatedby());
//        toggle.put(SmartDCRConstants.KEY_BILL_AMOUNT, bill.getAmount());
//        toggle.put(SmartDCRConstants.KEY_BILL_MODE, String.valueOf(bill.getMode()));
//        toggle.put(SmartDCRConstants.KEY_BILL_CHANNEL, String.valueOf(bill.getChannel()));
//        toggle.put(SmartDCRConstants.KEY_BILL_CREATED_DATE, bill.getCreatedDate());
//        toggle.put(SmartDCRConstants.KEY_BILL_BENEFICIARY, bill.getBeneficiaryId());
//        toggle.put("transactionId", bill.getTransactionId());
//        toggle.put(SmartDCRConstants.KEY_BILL_STATUS, "T");
//        database.update(TABLE_BILL,toggle, "transactionId='"+bill.getTransactionId()+"'" ,null);
//
//    }
//
//    //This function inserts details to TABLE_BILL_ITEM,;
//
//    private void insertBillItems(Set<BillItemDto> billItem, long refId, int month, long beneficiaryId,String billDate) {
//        ContentValues toggle = new ContentValues();
//        List<BillItemDto> billList = new ArrayList<BillItemDto>(billItem);
//        for (BillItemDto billItems : billList) {
//            toggle.put(KEY_ID, billItems.getId());
//            toggle.put(SmartDCRConstants.KEY_BILL_ITEM_PRODUCT_ID, billItems.getProductId());
//            toggle.put(SmartDCRConstants.KEY_BILL_ITEM_QUANTITY, billItems.getQuantity());
//            toggle.put(SmartDCRConstants.KEY_BILL_ITEM_COST, billItems.getCost());
//            toggle.put(SmartDCRConstants.KEY_BILL_ITEM_BILL_REF, refId);
//            toggle.put(SmartDCRConstants.KEY_BILL_TIME_MONTH, month);
//            toggle.put(SmartDCRConstants.KEY_BILL_BENEFICIARY, beneficiaryId);
//            toggle.put(SmartDCRConstants.KEY_BILL_ITEM_DATE, billDate);
//
//
//            //database.insert(TABLE_BILL_ITEM, null, toggle);
//            database.insertWithOnConflict(TABLE_BILL_ITEM, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//
//        }
//    }
//
//    //Bill for background sync
//    public List<BillDto> getAllBillsForSync() {
//        List<BillDto> bills = new ArrayList<BillDto>();
//        String selectQuery = "SELECT  * FROM " + TABLE_BILL + " where " + SmartDCRConstants.KEY_BILL_STATUS + "='R'";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            BillDto bill = new BillDto(cursor);
//            bill.setBillItemDto(getBillItems(bill.getBillReferenceId()));
//            bills.add(bill);
//            cursor.moveToNext();
//        }
//        return bills;
//    }
//
//    //Bill for background sync
//    public void getAllBillItemsToday(String toDate) {
//        List<BillItemDto> bills = new ArrayList<BillItemDto>();
//        String selectQuery = "SELECT  productId,SUM(quantity) as total FROM " + TABLE_BILL_ITEM + " where " + SmartDCRConstants.KEY_BILL_ITEM_DATE + " LIKE '"+toDate+" %' group by productId";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            BillItemDto billItemDto = new BillItemDto();
//            billItemDto.setProductId(cursor.getLong(cursor.getColumnIndex(SmartDCRConstants.KEY_BILL_ITEM_PRODUCT_ID)));
//            billItemDto.setQuantity(cursor.getDouble(cursor.getColumnIndex("total")));
//            bills.add(billItemDto);
//            cursor.moveToNext();
//        }
//        Log.e("Bill Item",bills.toString());
//    }
//
//
//    //Bill from local db
//    public List<BillDto> getAllBills(long id, int month) {
//        List<BillDto> bills = new ArrayList<BillDto>();
//        String selectQuery = "SELECT  * FROM " + TABLE_BILL + " where " + SmartDCRConstants.KEY_BILL_BENEFICIARY + " = " + id + " AND " + SmartDCRConstants.KEY_BILL_TIME_MONTH + " = " + month;
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            BillDto bill = new BillDto(cursor);
//            bill.setBillItemDto(getBillItems(bill.getBillReferenceId()));
//            bills.add(bill);
//            cursor.moveToNext();
//        }
//        return bills;
//    }
//
//    //Bill from local db
//    public List<BillItemDto> getAllBillItems(long id, int month) {
//        List<BillItemDto> billItems = new ArrayList<BillItemDto>();
//        String selectQuery = "SELECT productId,SUM(quantity) as total FROM " + TABLE_BILL_ITEM + " where " + SmartDCRConstants.KEY_BILL_BENEFICIARY + " = " + id + " AND " + SmartDCRConstants.KEY_BILL_TIME_MONTH + " = " + month + " group by " + SmartDCRConstants.KEY_BILL_ITEM_PRODUCT_ID;
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            BillItemDto billItemDto = new BillItemDto();
//            billItemDto.setProductId(cursor.getLong(cursor.getColumnIndex(SmartDCRConstants.KEY_BILL_ITEM_PRODUCT_ID)));
//            billItemDto.setQuantity(cursor.getDouble(cursor.getColumnIndex("total")));
//            billItems.add(billItemDto);
//            cursor.moveToNext();
//        }
//        return billItems;
//    }
//
//    public Set<BillItemDto> getBillItems(long referenceId) {
//        List<BillItemDto> billItems = new ArrayList<BillItemDto>();
//        String selectQuery = "SELECT  * FROM " + TABLE_BILL_ITEM + " where " + SmartDCRConstants.KEY_BILL_ITEM_BILL_REF + "=" + referenceId;
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            billItems.add(new BillItemDto(cursor));
//            cursor.moveToNext();
//        }
//        Set<BillItemDto> billItemSet = new HashSet<>(billItems);
//        return billItemSet;
//    }
//
//    //This function inserts details to TABLE_BILL_ITEM,;
//    private void setBillItems(BillDto fpsDataDto) {
//        ContentValues billItemValues = new ContentValues();
//        List<BillItemDto> billItemList = new ArrayList<BillItemDto>(fpsDataDto.getBillItemDto());
//        for (BillItemDto billItem : billItemList) {
//            billItemValues.put(KEY_ID, billItem.getId());
//            billItemValues.put(SmartDCRConstants.KEY_BILL_ID, billItem.getBillId());
//            billItemValues.put(SmartDCRConstants.KEY_BILL_ITEM_PRODUCT_ID, billItem.getProductId());
//            billItemValues.put(SmartDCRConstants.KEY_BILL_ITEM_QUANTITY, billItem.getQuantity());
//            billItemValues.put(SmartDCRConstants.KEY_BILL_ITEM_COST, billItem.getCost());
//            if (billItem.isTransmittedToPos()) {
//                billItemValues.put(SmartDCRConstants.KEY_BILL_ITEM_IS_TRANSMIT, 0);
//            } else {
//                billItemValues.put(SmartDCRConstants.KEY_BILL_ITEM_IS_TRANSMIT, 1);
//            }
//            //database.insert(TABLE_BILL_ITEM, null, billItemValues);
//            database.insertWithOnConflict(TABLE_BILL_ITEM, KEY_ID, billItemValues, SQLiteDatabase.CONFLICT_REPLACE);
//        }
//
//    }
//
//    //This function inserts details to TABLE_PRODUCTS;
//
//    public void insertProductData(FpsDataDto fpsDataDto) {
//        List<ProductDto> productList = new ArrayList<ProductDto>(fpsDataDto.getProductDto());
//        for (ProductDto products : productList) {
//            ContentValues toggle = new ContentValues();
//            toggle.put(KEY_ID, products.getId());
//            toggle.put(SmartDCRConstants.KEY_PRODUCT_NAME, products.getName());
//            toggle.put(SmartDCRConstants.KEY_LPRODUCT_NAME, products.getLproductName());
//            toggle.put(SmartDCRConstants.KEY_PRODUCT_CODE, products.getCode());
//            toggle.put(SmartDCRConstants.KEY_LPRODUCT_UNIT, products.getLproductUnit());
//            toggle.put(SmartDCRConstants.KEY_PRODUCT_UNIT, products.getProductUnit());
//            toggle.put(SmartDCRConstants.KEY_PRODUCT_PRICE, products.getProductPrice());
//            if (products.isNegativeIndicator())
//                toggle.put(SmartDCRConstants.KEY_NEGATIVE_INDICATOR, 0);
//            else {
//                toggle.put(SmartDCRConstants.KEY_NEGATIVE_INDICATOR, 1);
//            }
//            toggle.put(SmartDCRConstants.KEY_PRODUCT_MODIFIED_DATE, products.getModifiedDate());
//            if (products.getModifiedby() != null)
//                toggle.put(SmartDCRConstants.KEY_MODIFIED_BY, products.getModifiedby());
//            toggle.put(SmartDCRConstants.KEY_CREATED_DATE, products.getCreatedDate());
//            if (products.getCreatedby() != null)
//                toggle.put(SmartDCRConstants.KEY_CREATED_BY, products.getCreatedby());
//            // database.insert(TABLE_PRODUCTS, null, toggle);
//            database.insertWithOnConflict(TABLE_PRODUCTS, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//        }
//
//
//    }
//
//    //This function inserts details to TABLE_BENEFICIARY;
//    public void insertBeneficiaryData(FpsDataDto fpsDataDto) {
//        try {
//            ContentValues toggle = new ContentValues();
//            List<BeneficiaryDto> beneficiaryDto = new ArrayList<BeneficiaryDto>(fpsDataDto.getBeneficiaryDto());//get Beneficiary Details
//            for (BeneficiaryDto beneficiary : beneficiaryDto) {
//                toggle.put(KEY_ID, beneficiary.getId());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_UFC, beneficiary.getUfc());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_FPS_ID, beneficiary.getFpsId());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_TIN, beneficiary.getTin());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_QR_CODE, beneficiary.getQrCode());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MOBILE, beneficiary.getMobileNumber());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_CREATED_DATE, beneficiary.getCreatedDate());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MODIFIED_DATE, beneficiary.getModifiedDate());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MODIFIED_BY, beneficiary.getModifiedBy());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_STATE_ID, beneficiary.getStateId());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_CARD_TYPE_ID, beneficiary.getCardTypeId());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_DISTRICT_ID, beneficiary.getDistrictId());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_TALUK_ID, beneficiary.getTalukId());
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_VILLAGE_ID, beneficiary.getVillageId());
//
//                int active = 0;
//                if (beneficiary.isActive()) {
//                    active = 1;
//                }
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVE, active);
//
//                setBeneficiaryMemberData(beneficiary);
//                //database.insert(TABLE_BENEFICIARY, null, toggle);
//                database.insertWithOnConflict(TABLE_BENEFICIARY, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//
//            }
//        } catch (Exception e) {
//            Log.e("Error", e.toString(), e);
//        }
//
//    }
//
//    //This function inserts details to TABLE_BENEFICIARY_MEMBER;
//    private void setBeneficiaryMemberData(BeneficiaryDto fpsDataDto) {
//
//        ContentValues toggle = new ContentValues();
//        List<BeneficiaryMemberDto> beneficiaryMemberList = new ArrayList<BeneficiaryMemberDto>(fpsDataDto.getBenefMemberDto());
//        for (BeneficiaryMemberDto beneficiaryMember : beneficiaryMemberList) {
//            toggle.put(KEY_ID, beneficiaryMember.getId());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_QR_CODE, fpsDataDto.getQrCode());
//            if (beneficiaryMember.getTin() != null) {
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_UID, beneficiaryMember.getTin());
//
//            } else {
//                Log.e("BM", "" + TABLE_BENEFICIARY_MEMBER + "TIN should not null");
//            }
//
//            if (beneficiaryMember.getUid() != null) {
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_TIN, beneficiaryMember.getUid());
//            } else {
//                Log.e("BM", "" + TABLE_BENEFICIARY_MEMBER + "UID should not null");
//            }
//
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_EID, beneficiaryMember.getEid());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_FIRST_NAME, beneficiaryMember.getFirstname());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_MIDDLE_NAME, beneficiaryMember.getMiddlename());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_LAST_NAME, beneficiaryMember.getLastname());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_FATHER_NAME, beneficiaryMember.getFatherName());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_MOTHER_NAME, beneficiaryMember.getMotherName());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_GENDER, String.valueOf(beneficiaryMember.getGender()));//gender char
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_PERMANENT_ADDRESS, beneficiaryMember.getPermanentAddress());
//
//
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_TEMP_ADDRESS, beneficiaryMember.getTempAddress());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_CREATED_BY, beneficiaryMember.getCreatedBy());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_CREATED_DATE, beneficiaryMember.getCreatedDate());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_MODIFIED_BY, beneficiaryMember.getModifiedby());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_MODIFIED_DATE, beneficiaryMember.getModifiedDate());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_NAME, beneficiaryMember.getName());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_RESIDENT_ID, String.valueOf(beneficiaryMember.getResidentid()));  //Resident
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_REL_NAME, beneficiaryMember.getRelname());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_GENDER_ID, String.valueOf(beneficiaryMember.getGenderid())); //gender id
//
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_DOB, beneficiaryMember.getDob());
//
//
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_DOB_TYPE, String.valueOf(beneficiaryMember.getDobType())); //dob type
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_STATUS_ID, String.valueOf(beneficiaryMember.getMstatusid()));
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_EDU_NAME, beneficiaryMember.getEduname());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_OCCUPATION_NAME, beneficiaryMember.getOccuname());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_FATHER_CODE, beneficiaryMember.getFathercode());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_FATHER_NM, beneficiaryMember.getFathernm());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_MOTHER_CODE, beneficiaryMember.getMothercode());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_MOTHER_NM, beneficiaryMember.getMothernm());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_SPOUSE_CODE, beneficiaryMember.getSpousecode());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_SPOUSE_NM, beneficiaryMember.getSpousenm());
//
//
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_NAT_NAME, beneficiaryMember.getNatname());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_ADDRESS_LINE_1, beneficiaryMember.getAddressline1());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_ADDRESS_LINE_2, beneficiaryMember.getAddressline2());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_ADDRESS_LINE_3, beneficiaryMember.getAddressline3());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_ADDRESS_LINE_4, beneficiaryMember.getAddressline4());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_ADDRESS_LINE_5, beneficiaryMember.getAddressline5());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_PIN_CODE, beneficiaryMember.getPincode());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_DURATION_IN_YEAR, beneficiaryMember.getDurationinyear());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_P_ADDRESS_LINE_1, beneficiaryMember.getPaddressline1());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_P_ADDRESS_LINE_2, beneficiaryMember.getPaddressline2());
//
//
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_P_ADDRESS_LINE_3, beneficiaryMember.getPaddressline3());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_P_ADDRESS_LINE_4, beneficiaryMember.getPaddressline4());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_P_ADDRESS_LINE_5, beneficiaryMember.getPaddressline5());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_P_PIN_CODE, beneficiaryMember.getPpincode());
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_DATE_DATA_ENTERED, beneficiaryMember.getDateDataEntered());
//
//            if (beneficiaryMember.isAliveStatus())
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_ALIVE_STATUS, 0);
//            else {
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_ALIVE_STATUS, 1);
//            }
//
//            if (beneficiaryMember.isAdult())
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_IS_ADULT, 0);
//            else {
//                toggle.put(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_IS_ADULT, 1);
//            }
//            //database.insert(TABLE_BENEFICIARY_MEMBER, null, toggle);
//            Log.i("Gender", String.valueOf(beneficiaryMember.getGender()));
//
//            database.insertWithOnConflict(TABLE_BENEFICIARY_MEMBER, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//
//        }
//
//
//    }
//
//    private List<BeneficiaryMemberDto> getAllBeneficiaryMembers(String qrCode) {
//        List<BeneficiaryMemberDto> beneficiaryMembers = new ArrayList<BeneficiaryMemberDto>();
//        String selectQuery = "SELECT  * FROM " + TABLE_BENEFICIARY_MEMBER + " where " + SmartDCRConstants.KEY_BENEFICIARY_QR_CODE + "='" + qrCode + "' AND " + SmartDCRConstants.KEY_BENEFICIARY_MEMBER_ALIVE_STATUS + "=0";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            beneficiaryMembers.add(new BeneficiaryMemberDto(cursor));
//
//            cursor.moveToNext();
//        }
//        return beneficiaryMembers;
//        //new BeneficiaryDto(cursor);
//    }
//
//    //Get Beneficiary data by QR Code
//    public BeneficiaryDto beneficiaryDto(String qrCode) {
//        String selectQuery = "SELECT  * FROM " + TABLE_BENEFICIARY + " where " + SmartDCRConstants.KEY_BENEFICIARY_QR_CODE + "='" + qrCode + "'";//+" AND " +SmartDCRConstants.KEY_BENEFICIARY_ACTIVE +"=0";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        if (cursor.getCount() == 0) {
//            return null;
//        } else {
//            BeneficiaryDto beneficiary = new BeneficiaryDto(cursor);
//            beneficiary.setFamilyMembers(getAllBeneficiaryMembers(qrCode));
//            return beneficiary;
//        }
//    }
//
//    //Get Product data by Product Id
//    public ProductDto getProductDetails(long _id) {
//        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " where " + KEY_ID + "=" + _id;//+" AND " +SmartDCRConstants.KEY_BENEFICIARY_ACTIVE +"=0";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        if (cursor.getCount() == 0) {
//            return null;
//        } else
//            return new ProductDto(cursor);
//    }
//
//    //Get Product data by Product Id
//    public List<ProductDto> getAllProductDetails() {
//        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
//        List<ProductDto> products = new ArrayList<ProductDto>();
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            products.add(new ProductDto(cursor));
//            cursor.moveToNext();
//        }
//        return products;
//    }
//
//    //Get Allotments by district and card
//    public List<AllotmentDto> getAllAllotments(long districtId, String cardTypeId) {
//        List<AllotmentDto> allotmentProduct = new ArrayList<AllotmentDto>();
//        String selectQuery = "SELECT  * FROM " + TABLE_ALLOTMENT + " where " + SmartDCRConstants.KEY_ALLOTMENT_DISTRICT + "=" + districtId + " AND " + SmartDCRConstants.KEY_ALLOTMENT_CARD_TYPE + "='" + cardTypeId + "'";//+" AND " +SmartDCRConstants.KEY_BENEFICIARY_ACTIVE +"=0";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            allotmentProduct.add(new AllotmentDto(cursor));
//
//            cursor.moveToNext();
//        }
//        return allotmentProduct;
//    }
//
//    //Get AgeGroup by age and productId
//    public AgeGroupDto getAgeGroup(long productId, int age) {
//        AgeGroupDto ageGroupDto = new AgeGroupDto();
//        String selectQuery = "SELECT  * FROM " + TABLE_AGE_GROUP + " where " + SmartDCRConstants.KEY_AGE_PRODUCT_ID + "=" + productId + " AND (" + SmartDCRConstants.KEY_AGE_TO + " >=" + age + " AND " + SmartDCRConstants.KEY_AGE_FROM + " <=" + age + ")";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        ageGroupDto = new AgeGroupDto(cursor);
//
//        return ageGroupDto;
//    }
//
//
//    //Get Allotment Mapping by product and productId
//    public AllotmentMappingDto getAllotmentMap(long productId, long districtId, double quantity) {
//        AllotmentMappingDto allotmentMappingDto = new AllotmentMappingDto();
//        String selectQuery = "SELECT  * FROM " + TABLE_ALLOTMENT_MAPPING + " where " + SmartDCRConstants.KEY_ALLOTMENT_MAP_PRODUCT_ID + "=" + productId + " AND " + SmartDCRConstants.KEY_ALLOTMENT_MAP_DISTRICT + " =" + districtId + " AND (" + SmartDCRConstants.KEY_ALLOTMENT_MAP_END + " >=" + quantity + " AND " + SmartDCRConstants.KEY_ALLOTMENT_MAP_START + " <=" + quantity + ")";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        allotmentMappingDto = new AllotmentMappingDto(cursor);
//        return allotmentMappingDto;
//    }
//
//    //This function inserts details to TABLE_ALLOTMENT_MAPPING;
//    public void insertAllotmentData(FpsDataDto fpsDataDto) {
//
//        ContentValues toggle = new ContentValues();
//        List<AllotmentDto> allotmentDtoList = new ArrayList<AllotmentDto>(fpsDataDto.getAllotmentDto());//get Beneficiary Details
//        if (!allotmentDtoList.isEmpty()) {
//            for (AllotmentDto allotment : allotmentDtoList) {
//                toggle.put(KEY_ID, allotment.getId());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_PRODUCT_ID, allotment.getProductId());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_LIMIT, allotment.getAllotmentLimit());
//                if (allotment.isCalcAllotment())
//                    toggle.put(SmartDCRConstants.KEY_CALC_ALLOTMENT_ALLOTMENT, 0);
//                else {
//                    toggle.put(SmartDCRConstants.KEY_CALC_ALLOTMENT_ALLOTMENT, 1);
//                }
//
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_DISTRICT, allotment.getDistrictId());
//                // toggle.put(SmartDCRConstants.KEY_ALLOTMENT_AGE_GROUP_ID, allotment.get);
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_CREATED_BY, allotment.getCreatedBy());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_MODIFIED_BY, allotment.getModifiedBY());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_CREATE_DATE, allotment.getCreateDate());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_MODIFIED_DATE, allotment.getModifiedDate());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_CARD_TYPE, allotment.getCardtypeId());
//
//                database.insertWithOnConflict(TABLE_ALLOTMENT, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//
//            }
//        }
//    }
//
//    //This function inserts details to TABLE_ALLOTMENT_MAPPING;
//    public void insertAllotmentMapData(FpsDataDto fpsDataDto) {
//
//        ContentValues toggle = new ContentValues();
//        List<AllotmentMappingDto> allotmentMappingDtoList = new ArrayList<AllotmentMappingDto>(fpsDataDto.getAllomappingDto());//get Beneficiary Details
//        if (!allotmentMappingDtoList.isEmpty()) {
//            for (AllotmentMappingDto allotmentMapping : allotmentMappingDtoList) {
//                toggle.put(KEY_ID, allotmentMapping.getId());
//                //  toggle.put(SmartDCRConstants.KEY_ALLOTMENT_MAP_MAP_ID, allotmentMapping.get);
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_MAP_START, allotmentMapping.getStartRange());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_MAP_END, allotmentMapping.getEndRange());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_MAP_ALLOT, allotmentMapping.getAllotedQuantity());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_MAP_DISTRICT, allotmentMapping.getDistrictId());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_MAP_DESCRIPTION, allotmentMapping.getDescription());
//                toggle.put(SmartDCRConstants.KEY_ALLOTMENT_MAP_PRODUCT_ID, allotmentMapping.getProductId());
//                //database.insert(TABLE_ALLOTMENT_MAPPING, null, toggle);
//                database.insertWithOnConflict(TABLE_ALLOTMENT_MAPPING, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//            }
//        }
//
//    }
//
//    //This function inserts details to TABLE_AGE_GROUP.
//    public void insertAgeGroupData(FpsDataDto fpsDataDto) {
//        ContentValues toggle = new ContentValues();
//        List<AgeGroupDto> ageGroupList = new ArrayList<AgeGroupDto>(fpsDataDto.getAgegroupDto());
//        Log.e("Age", ageGroupList.toString());
//        if (!ageGroupList.isEmpty()) {
//            for (AgeGroupDto ageGroup : ageGroupList) {
//                toggle.put(KEY_ID, ageGroup.getId());
//                toggle.put(SmartDCRConstants.KEY_AGE_NAME, ageGroup.getName());
//                toggle.put(SmartDCRConstants.KEY_AGE_PRODUCT_ID, ageGroup.getProductId());
//                toggle.put(SmartDCRConstants.KEY_AGE_FROM, ageGroup.getFromRange());
//                toggle.put(SmartDCRConstants.KEY_AGE_TO, ageGroup.getToRange());
//                toggle.put(SmartDCRConstants.KEY_AGE_QUANTITY, ageGroup.getQuantity());
//                // database.insert(TABLE_AGE_GROUP, null, toggle);
//
//                database.insertWithOnConflict(TABLE_AGE_GROUP, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//            }
//        }
//    }
//
//
//    //This function inserts details to TABLE_CARD_TYPE.
//    public void insertCardTypeData(FpsDataDto fpsDataDto) {
//
//
//        ContentValues toggle = new ContentValues();
//        List<CardTypeDto> cardTypeList = new ArrayList<CardTypeDto>(fpsDataDto.getCardtypeDto());
//        if (!cardTypeList.isEmpty()) {
//            for (CardTypeDto cardType : cardTypeList) {
//                toggle.put(KEY_ID, cardType.getId());
//                if (String.valueOf(cardType.getType()) != null) {
//                    toggle.put(SmartDCRConstants.KEY_CARD_TYPE, String.valueOf(cardType.getType()));
//                }
//                if (String.valueOf(cardType.getDescription()) != null) {
//                    toggle.put(SmartDCRConstants.KEY_CARD_DESCRIPTION, cardType.getDescription());
//                }
//
//                //  database.insert(TABLE_CARD_TYPE, null, toggle);
//
//                database.insertWithOnConflict(TABLE_CARD_TYPE, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//            }
//        } else {
//            Log.e("CT", TABLE_CARD_TYPE + "is empty");
//        }
//    }
//
//    //This function inserts details to TABLE_USERS,
//    public void insertUserDetailData(FpsDataDto fpsDataDto) {
//
//        ContentValues toggle = new ContentValues();
//        List<UserDetailDto> userDetailList = new ArrayList<UserDetailDto>(fpsDataDto.getUserdetailDto());
//        if (!userDetailList.isEmpty()) {
//            for (UserDetailDto userDetail : userDetailList) {
//                toggle.put(KEY_ID, userDetail.getId());
//                toggle.put(SmartDCRConstants.KEY_USERS_NAME, userDetail.getUsername());
//                toggle.put(SmartDCRConstants.KEY_USERS_PASS_HASH, userDetail.getPassword());
//                toggle.put(SmartDCRConstants.KEY_USERS_PROFILE, userDetail.getProfile());
//                toggle.put(SmartDCRConstants.KEY_USERS_CREATE_DATE, userDetail.getCreatedDate());
//                toggle.put(SmartDCRConstants.KEY_USERS_MODIFIED_DATE, userDetail.getModifiedDate());
//                toggle.put(SmartDCRConstants.KEY_USERS_CREATED_BY, userDetail.getCreatedBy());
//                toggle.put(SmartDCRConstants.KEY_USERS_MODIFIED_BY, userDetail.getModifiedBy());
//                toggle.put(SmartDCRConstants.KEY_USERS_FPS_ID, userDetail.getFpsStore().getId());
//
//                // database.insert(TABLE_USERS, null, toggle);
//
//                database.insertWithOnConflict(TABLE_USERS, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//
//            }
//        } else {
//            Log.e("UD", TABLE_USERS + "is empty");
//        }
//
//    }
//
//    // This function inserts details to TABLE_STORE
//    public void insertFbsStoreData(FpsDataDto fpsDataDto) {
//
//        ContentValues toggle = new ContentValues();
//        List<FpsStoreDto> fpsStoreList = new ArrayList<FpsStoreDto>(fpsDataDto.getFpsstoreDto());
//        if (!fpsStoreList.isEmpty()) {
//            for (FpsStoreDto fpsStore : fpsStoreList) {
//                toggle.put(KEY_ID, fpsStore.getId());
//                toggle.put(SmartDCRConstants.KEY_STORE_GO_DOWN_ID, fpsStore.getGodownId());
//                toggle.put(SmartDCRConstants.KEY_STORE_CODE, fpsStore.getCode());
//                if (fpsStore.isActive()) {
//                    toggle.put(SmartDCRConstants.KEY_STORE_ACTIVE, 0);
//                } else {
//                    toggle.put(SmartDCRConstants.KEY_STORE_ACTIVE, 1);
//                }
//                toggle.put(SmartDCRConstants.KEY_STORE_CREATED_BY, fpsStore.getCreatedBy());
//                toggle.put(SmartDCRConstants.KEY_STORE_MODIFIED_BY, fpsStore.getModifiedBY());
//                toggle.put(SmartDCRConstants.KEY_STORE_CREATED_DATE, fpsStore.getCreateDate());
//                toggle.put(SmartDCRConstants.KEY_STORE_MODIFIED_DATE, fpsStore.getModofiedDate());
//                setGoDownDetails(fpsStore.getGodown());
//                // database.insert(TABLE_STORE, null, toggle);
//                database.insertWithOnConflict(TABLE_STORE, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//            }
//        } else {
//            Log.e("FS", TABLE_STORE + "is empty");
//        }
//
//    }
//
//
//    // This function inserts details to TABLE_STOCK
//    public void insertFpsStockData(FpsDataDto fpsDataDto) {
//
//
//        List<FPSStockDto> fpsStockList = new ArrayList<FPSStockDto>(fpsDataDto.getFpsStockDto());
//        if (!fpsStockList.isEmpty()) {
//            for (FPSStockDto fpsStock : fpsStockList) {
//                ContentValues toggle = new ContentValues();
//                toggle.put(KEY_ID, fpsStock.getFpsId());
//                toggle.put(SmartDCRConstants.KEY_STOCK_PRODUCT_ID, fpsStock.getProductId());
//                toggle.put(SmartDCRConstants.KEY_STOCK_QUANTITY, fpsStock.getQuantity());
//                toggle.put(SmartDCRConstants.KEY_STOCK_REORDER_LEVEL, fpsStock.getReorderLevel());
//
//                if(fpsStock.isEmailAction()) {
//                    toggle.put(SmartDCRConstants.KEY_STOCK_EMAIL_ACTION, 0);
//                }
//
//                else {
//                    toggle.put(SmartDCRConstants.KEY_STOCK_EMAIL_ACTION, 1);
//                }
//
//                if(fpsStock.isSmsMSAction()) {
//                    toggle.put(SmartDCRConstants.KEY_STOCK_SMSACTION, 0);
//                }
//
//                else{
//                    toggle.put(SmartDCRConstants.KEY_STOCK_SMSACTION, 1);
//                }
//                Log.e("FSTOCK", TABLE_STOCK +fpsStock.toString());
//
//                 database.insert(TABLE_STOCK, null, toggle);
//            }
//        } else {
//            Log.e("FSTOCK", TABLE_STOCK + "is empty");
//        }
//
//    }
//
//
//
//    // This function inserts details to TABLE_GO_DOWN
//
//    private void setGoDownDetails(GodownDto goDown) {
//
//        if (goDown != null) {
//
//            ContentValues toggle = new ContentValues();
//            if (goDown.getId() != 0) {
//                toggle.put(KEY_ID, goDown.getId());
//            }
//
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_GO_DOWN_CODE, goDown.getGodownCode());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_NAME, goDown.getName());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_DISTRICT, goDown.getDistrict());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_TALUK, goDown.getTaluk());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_CATEGORY_ID, goDown.getCategoryId());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_CONTACT_PERSON_NAME, goDown.getContactPersonName());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_CONTACT_NUMBER, goDown.getContactNumber());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_PIN_CODE, goDown.getPincode());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_ADDRESS, goDown.getAddress());
//
//            if (goDown.isStatus()) {
//                toggle.put(SmartDCRConstants.KEY_GO_DOWN_IS_STATUS, 0);
//            } else {
//                toggle.put(SmartDCRConstants.KEY_GO_DOWN_IS_STATUS, 1);
//            }
//
//
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_CREATED_DATE, goDown.getCreatedDate());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_MODIFIED_DATE, goDown.getModifiedDate());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_CREATED_BY, goDown.getCreatedby());
//            toggle.put(SmartDCRConstants.KEY_GO_DOWN_MODIFIED_BY, goDown.getModifiedby());
//
//            // database.insert(TABLE_GO_DOWN, null, toggle);
//
//            database.insertWithOnConflict(TABLE_GO_DOWN, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//        }
//    }
//
//    // This function returns cursor  Max Date of Product
//
//    private Cursor getMaxModifiedDateProduct() {
//
//        String queryString = "productModifiedDate =(SELECT MAX(productModifiedDate) FROM  " + TABLE_PRODUCTS + ")";
//        return database.query(TABLE_PRODUCTS, null, queryString, null, null, null, null);
//    }
//
//    // This function returns cursor  Max Date of Beneficiary
//
//    private Cursor getMaxModifiedDateBeneficiary() {
//
//        String queryString = "beneficiaryModifiedDate =(SELECT MAX(beneficiaryModifiedDate) FROM  " + TABLE_BENEFICIARY + ")";
//        return database.query(TABLE_BENEFICIARY, null, queryString, null, null, null, null);
//    }
//
//    // This function returns cursor  Max Date of Bill
//
//    private Cursor getMaxModifiedDateBill() {
//        String queryString = "date =(SELECT MAX(date) FROM  " + TABLE_BILL + ")";
//        return database.query(TABLE_BILL, null, queryString, null, null, null, null);
//    }
//
//    // This function returns cursor  Max Date of Allotment
//    private Cursor getMaxModifiedDateAllotment() {
//        String queryString = "aModifiedDate =(SELECT MAX(aModifiedDate) FROM  " + TABLE_ALLOTMENT + ")";
//        return database.query(TABLE_ALLOTMENT, null, queryString, null, null, null, null);
//    }
//
//
//    // This function returns cursor  Max Date of Transaction
//    private Cursor getMaxModifiedDateTransaction() {
//        String queryString = SmartDCRConstants.KEY_TRANSACTION_TYPE_LAST_MODIFIED_TIME + " =" + "(SELECT MAX(tlastModifiedTime) FROM  " + TABLE_TRANSACTION_TYPE + ")";
//        return database.query(TABLE_TRANSACTION_TYPE, null, queryString, null, null, null, null);
//    }
//
//
//    // This function returns Max Date of Beneficiary
//
//    private String getBeneficiaryModifiedDate() {
//        Cursor cursorBeneficiary = getMaxModifiedDateBeneficiary();
//        String beneficiaryDate = null;
//        if (cursorBeneficiary != null) {
//            cursorBeneficiary.moveToFirst();
//            beneficiaryDate = cursorBeneficiary.getString(cursorBeneficiary.getColumnIndex(SmartDCRConstants.KEY_BENEFICIARY_MODIFIED_DATE));
//            cursorBeneficiary.close();
//            return beneficiaryDate;
//
//        } else {
//            Log.e("BM", "Beneficiary Max date cursor is null");
//
//        }
//        return beneficiaryDate;
//    }
//
//    // This function returns Max Date of Bill
//    private String getBillDate() {
//        Cursor cursorBill = getMaxModifiedDateBill();
//        String billDate = null;
//        if (cursorBill != null) {
//            cursorBill.moveToFirst();
//            billDate = cursorBill.getString(cursorBill.getColumnIndex(SmartDCRConstants.KEY_BILL_DATE));
//            cursorBill.close();
//            return billDate;
//        } else {
//            Log.e("BillM", "Bill Max date cursor is null");
//
//        }
//        return billDate;
//    }
//
//    // This function returns Max Date of Allotment
//    private String getAllotmentDate() {
//        Cursor cursorAllotment = getMaxModifiedDateAllotment();
//        String allotDate = null;
//        if (cursorAllotment != null) {
//            cursorAllotment.moveToFirst();
//            allotDate = cursorAllotment.getString(cursorAllotment.getColumnIndex(SmartDCRConstants.KEY_ALLOTMENT_MODIFIED_DATE));
//            cursorAllotment.close();
//            return allotDate;
//        } else {
//            Log.e("AllotM", "Allot Max date cursor is null");
//
//        }
//        return allotDate;
//    }
//
//    // This function returns Max Date of Product
//    private String getProductDate() {
//        Cursor cursorProduct = getMaxModifiedDateProduct();
//        String productDate = null;
//        if (cursorProduct != null) {
//            cursorProduct.moveToFirst();
//            productDate = cursorProduct.getString(cursorProduct.getColumnIndex(SmartDCRConstants.KEY_PRODUCT_MODIFIED_DATE));
//            cursorProduct.close();
//            return productDate;
//        } else {
//            Log.e("ProductM", "Product Max date cursor is null");
//
//        }
//        return productDate;
//    }
//
//    private String getTransactionDate() {
//        Cursor cursorTransaction = getMaxModifiedDateTransaction();
//        String transactionDate = null;
//        if (cursorTransaction != null) {
//            cursorTransaction.moveToFirst();
//            transactionDate = cursorTransaction.getString(cursorTransaction.getColumnIndex(SmartDCRConstants.KEY_TRANSACTION_TYPE_LAST_MODIFIED_TIME));
//            cursorTransaction.close();
//            return transactionDate;
//        } else {
//            Log.e("TMD", "Transaction Max date cursor is null");
//
//        }
//        return transactionDate;
//    }
//
//    // This function returns List for  max modified date of Product,Beneficiary,Bill and Allotment
//    public List<String> getMaxModifiedDate() {
//        List<String> valueList = new ArrayList<String>();
//
//        if (getProductDate() != null || getBeneficiaryModifiedDate() != null || getBillDate() != null || getAllotmentDate() != null || getTransactionDate() != null)
//            valueList.add(getProductDate());
//        valueList.add(getBeneficiaryModifiedDate());
//        valueList.add(getBillDate());
//        valueList.add(getAllotmentDate());
//        valueList.add(getTransactionDate());
//        return valueList;
//    }
//
//    // This function return beneficiary details for beneficiary activation
//    public List<String> retrieveBeneficiaryActivation(String ufc) {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        int fpsId = 0;
//        int createdBy = 0;
//        int modifiedBy = 0;
//        int active = 0;
//        List<String> valueList = new ArrayList<String>();
//
//        if (!valueList.isEmpty()) {
//
//
//            String selectQuery = "SELECT  * FROM " + TABLE_BENEFICIARY + " where " + SmartDCRConstants.KEY_BENEFICIARY_UFC + "=?";
//            Cursor c = db.rawQuery(selectQuery, new String[]{ufc});
//            if (c.moveToFirst()) {
//                fpsId = c.getInt(c.getColumnIndex(SmartDCRConstants.KEY_BENEFICIARY_FPS_ID));
//                createdBy = c.getInt(c.getColumnIndex(SmartDCRConstants.KEY_BENEFICIARY_MEMBER_CREATED_BY));
//                modifiedBy = c.getInt(c.getColumnIndex(SmartDCRConstants.KEY_BENEFICIARY_MODIFIED_BY));
//                active = c.getInt(c.getColumnIndex(SmartDCRConstants.KEY_BENEFICIARY_ACTIVE));
//            }
//            valueList.add(String.valueOf(fpsId));
//            valueList.add(String.valueOf(createdBy));
//            valueList.add(String.valueOf(modifiedBy));
//            valueList.add(String.valueOf(active));
//            Log.i("ID", "" + "FPS_ID" + fpsId);
//            Log.i("BY", "" + "CREATED " + createdBy);
//            Log.i("BY", "" + "MODIFIED " + modifiedBy);
//            Log.i("AC", "" + "ACTIVE" + active);
//            c.close();
//            return valueList;
//        } else {
//            Log.e("BA", "Beneficiary Activation List is empty");
//        }
//        return valueList;
//    }
//
//    //This function inserts Beneficiary activation details into Beneficiary Activation Table
//
//    public void insertBeneficiaryActivation(String ufc, String oldRationCardNo, char cardType, int noOfCylinder, String rmn, int createdBy, int modifiedBy) {
//
//        ContentValues toggle = new ContentValues();
//        toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVATION_UFC, ufc);
//        toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVATION_OLD_RATION_CARD_NO, oldRationCardNo);
//        toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVATION_CARD_TYPE, String.valueOf(cardType));
//        toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVATION_NO_OF_CYLINDER, noOfCylinder);
//        toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVATION_RMN, rmn);
//        /*if (isSubmitted) {
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVATION_IS_SUBMITTED, 0);
//        } else {
//            toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVATION_IS_SUBMITTED, 1);
//        }*/
//        toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVATION_CREATED_BY, createdBy);
//        toggle.put(SmartDCRConstants.KEY_BENEFICIARY_ACTIVATION_MODIFIED_BY, modifiedBy);
//        if (!database.isOpen()) {
//            database = dbHelper.getWritableDatabase();
//        }
//        database.insert(TABLE_BENEFICIARY_ACTIVATION, null, toggle);
//
//        Log.i("Beneficiary", "" + oldRationCardNo);
//    }
//
//
//    //This function loads data to language table
//    public void insertLanguageTable(MessageDto message) {
//        ContentValues toggle = new ContentValues();
//        toggle.put(KEY_ID, message.getId());
//        toggle.put(SmartDCRConstants.KEY_LANGUAGE_CODE, message.getLanguageCode());
//        toggle.put(SmartDCRConstants.KEY_LANGUAGE_ID, message.getLanguageId());
//        toggle.put(SmartDCRConstants.KEY_LANGUAGE_MESSAGE, message.getDescription());
//
//        /*if (!database.isOpen()) {
//            database = dbHelper.getWritableDatabase();
//        }*/
////        database.insert(TABLE_LANGUAGE, null, toggle);
//
//        database.insertWithOnConflict(TABLE_LANGUAGE, SmartDCRConstants.KEY_LANGUAGE_ID, toggle,
//                SQLiteDatabase.CONFLICT_REPLACE);
//
//    }
//
//    //This function retrieve error description from language table
//    public MessageDto retrieveLanguageTable(int errorCode, String language) {
//        String selectQuery = "SELECT  * FROM " + TABLE_LANGUAGE + " where  " + SmartDCRConstants.KEY_LANGUAGE_CODE + " = " + errorCode + " AND " + SmartDCRConstants.KEY_LANGUAGE_ID + " ='" + language + "'";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        MessageDto messageDto = new MessageDto();
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            messageDto = new MessageDto(cursor);
//            break;
//        }
//        cursor.close();
//        return messageDto;
//    }
//
//    // This function loads transaction type details
//    public void insertTransactionTypeDetails(FpsDataDto fpsDataDto) {
//
//        List<TransactionDto> fpsTransactionTypeDtoList = new ArrayList<TransactionDto>(fpsDataDto.getTransactionDto());
//        if (!fpsTransactionTypeDtoList.isEmpty()) {
//            for (TransactionDto transactionDto : fpsTransactionTypeDtoList) {
//                ContentValues toggle = new ContentValues();
//                toggle.put(KEY_ID, transactionDto.getId());
//                toggle.put(SmartDCRConstants.KEY_TRANSACTION_TYPE_TXN_TYPE, transactionDto.getTxnType());
//
//                if (transactionDto.isStatus()) {
//                    toggle.put(SmartDCRConstants.KEY_TRANSACTION_TYPE_STATUS, 0);
//                } else {
//                    toggle.put(SmartDCRConstants.KEY_TRANSACTION_TYPE_STATUS, 1);
//                }
//                toggle.put(SmartDCRConstants.KEY_TRANSACTION_TYPE_DESCRIPTION, transactionDto.getDescription());
//                toggle.put(SmartDCRConstants.KEY_TRANSACTION_TYPE_CREATED_BY, transactionDto.getCreatedBy());
//                toggle.put(SmartDCRConstants.KEY_TRANSACTION_TYPE_CREATED_DATE, transactionDto.getCreatedDate());
//                toggle.put(SmartDCRConstants.KEY_TRANSACTION_TYPE_LAST_MODIFIED_TIME, transactionDto.getLastModifiedTime());
//                database.insertWithOnConflict(TABLE_TRANSACTION_TYPE, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//
//                Log.e("TD", transactionDto.getDescription());
//            }
//        } else {
//            Log.e("TD", TABLE_TRANSACTION_TYPE + "is empty");
//        }
//    }
//    /*public void showDataTransaction() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION_TYPE;
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        TransactionDto transaction = new TransactionDto();
//        cursor.moveToFirst();
//
//        for(int i= 0; i< cursor.getCount();i++)
//        {
//            TransactionDto transactionDto = new TransactionDto(cursor);
//
//             long transactionType = transactionDto.getTxnType();
//
//              cursor.moveToNext();
//             Log.i("TT",""+transactionType);
//        }
//        cursor.close();
//
//    }*/
//
//
//
//
//
//    // This function inserts details to TABLE_STOCK
//    public void insertFpsStockInwardDetails(FpsDataDto fpsDataDto) {
//
//
//        List<GodownStockOutwardDto> fpsStockInwardList = new ArrayList<GodownStockOutwardDto>(fpsDataDto.getFpsStoInwardDto());
//
//        if (!fpsStockInwardList.isEmpty()) {
//            for (GodownStockOutwardDto fpsStockInvard : fpsStockInwardList) {
//                ContentValues toggle = new ContentValues();
//
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_GODOWNID,fpsStockInvard.getGodownId());
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_FPSID, fpsStockInvard.getFpsId());
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_OUTWARD_DATE, fpsStockInvard.getOutwardDate());
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_PRODUCTID, fpsStockInvard.getProductId());
//
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_QUANTITY, fpsStockInvard.getQuantity());
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_UNIT, fpsStockInvard.getUnit());
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_BATCH_NO, fpsStockInvard.getBatchno());
//
//                if(fpsStockInvard.isFpsAckStatus()==false){
//                    toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_IS_FPSACKSTATUS, 0);
//                }
//                else{
//                    toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_IS_FPSACKSTATUS, 1);
//                }
//
//
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_FPSACKDATE, fpsStockInvard.getFpsAckDate());
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_FPSRECEIVEIQUANTITY,fpsStockInvard.getFpsReceiQuantity());
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_CREATEDBY, fpsStockInvard.getCreatedby());
//                toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_DELIEVERY_CHELLANID,fpsStockInvard.getDeliveryChallanId());
//
//                database.insert(TABLE_FPS_STOCK_INVARD, null, toggle);
//                Log.e("ertert","Input data");
//              // database.insertWithOnConflict(TABLE_FPS_STOCK_INVARD, KEY_ID, toggle, SQLiteDatabase.CONFLICT_REPLACE);
//            }
//        } else {
//            Log.e("FPSI", TABLE_STOCK + "is empty");
//        }
//
//    }
//
//    public  List<GodownStockOutwardDto> showFpsStockInvard(long fpsId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String selectQuery = "SELECT  * FROM " + TABLE_FPS_STOCK_INVARD +" where " + SmartDCRConstants.KEY_FPS_STOCK_INWARD_FPSRECEIVEIQUANTITY+" = 0.0 group by "+SmartDCRConstants.KEY_FPS_STOCK_INWARD_DELIEVERY_CHELLANID ;
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        Log.i("Fpsdate",""+""+cursor.getCount());
//        Log.i("query",""+""+selectQuery);
//        List<GodownStockOutwardDto> fpsInwardList = new ArrayList<GodownStockOutwardDto>();
//        GodownStockOutwardDto fpsStockOutwardDto   = new GodownStockOutwardDto();
//        cursor.moveToFirst();
//        for(int i= 0; i< cursor.getCount();i++){
//            fpsStockOutwardDto = new GodownStockOutwardDto(cursor);
//            fpsInwardList.add(fpsStockOutwardDto);
//            cursor.moveToNext();
//            Log.i("Fpsdate",""+fpsStockOutwardDto.getOutwardDate());
//        }
//        cursor.close();
//        Log.i("FPI",""+fpsStockOutwardDto.toString());
//        Log.i("FPSSIZE",""+fpsInwardList.size());
//        return fpsInwardList;
//    }
//
//
//    public  List<GodownStockOutwardDto> showFpsStockInvardDetail(long fpsId,long chellanId)
//    {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        List<GodownStockOutwardDto> fpsInwardList = new ArrayList<GodownStockOutwardDto>();
//        try
//        {
//            String selectQuery = "SELECT  * FROM " + TABLE_FPS_STOCK_INVARD+" where " +SmartDCRConstants.KEY_FPS_STOCK_INWARD_DELIEVERY_CHELLANID +"=" +chellanId;
//            Cursor cursor = database.rawQuery(selectQuery, null);
//            Set<ChellanProductDto> chellanProductDtoSet = new HashSet<ChellanProductDto>();
//            cursor.moveToFirst();
//            String productName= null;
//            Log.e("TABLE_FPS_STOCK_INVARD","TABLE_FPS_STOCK_INVARD"+cursor.getCount());
//            for(int i= 0; i< cursor.getCount();i++)
//            {
//                GodownStockOutwardDto fpsStockOutwardDto  = new GodownStockOutwardDto(cursor);
//                Log.i("ChellanId",""+fpsStockOutwardDto.getDeliveryChallanId());
//                Log.i("FPID",""+fpsStockOutwardDto.toString());
//
//                productName= getProductName(fpsStockOutwardDto.getProductId());
//
//                Log.i("PNAME",""+productName);
//
//                ChellanProductDto chellanProductDto = new ChellanProductDto();
//                chellanProductDto.setName(productName);
//                chellanProductDto.setProductId(fpsStockOutwardDto.getProductId());
//                chellanProductDto.setQuantity(fpsStockOutwardDto.getQuantity());
//
//                chellanProductDtoSet.add(chellanProductDto);
//
//                fpsStockOutwardDto.setProductDto(chellanProductDtoSet);
//
//                fpsInwardList.add(fpsStockOutwardDto);
//                Log.i("GSOD",""+ fpsInwardList.toString());
//                cursor.moveToNext();
//            }
//            cursor.close();
//
//        }
//
//        catch(Exception e)
//        {
//            Log.e("Exception",e.toString());
//
//        }
//
//        return fpsInwardList;
//    }
//
//
//    /*   db.update(TABLE_NAME,
//                contentValues,
//                        NAME + " = ? AND " + LASTNAME + " = ?",
//                        new String[]{"Manas", "Bajaj"});*/
//    public void updateReceivedQuantity(GodownStockOutwardDto godownStockOutwardDto)
//    {
//        ContentValues toggle = new ContentValues();
//        toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_FPSACKDATE, godownStockOutwardDto.getFpsAckDate());
//
//       /* long productId =0;*/
//        double receivedQuantity = 0.0;
//        for(ChellanProductDto chellanProductDto:godownStockOutwardDto.getProductDto())
//        {
//         receivedQuantity = chellanProductDto.getReceiProQuantity();
//         toggle.put(SmartDCRConstants.KEY_FPS_STOCK_INWARD_FPSRECEIVEIQUANTITY, receivedQuantity);
//         database.update(TABLE_FPS_STOCK_INVARD,toggle,SmartDCRConstants.KEY_FPS_STOCK_INWARD_PRODUCTID + " = ? AND " + SmartDCRConstants.KEY_FPS_STOCK_INWARD_DELIEVERY_CHELLANID + " = ?",new String[]{String.valueOf(godownStockOutwardDto.getDeliveryChallanId()),String.valueOf(chellanProductDto.getProductId())});
//         Log.i("update",""+database.update(TABLE_FPS_STOCK_INVARD,toggle,SmartDCRConstants.KEY_FPS_STOCK_INWARD_PRODUCTID + " = ? AND " + SmartDCRConstants.KEY_FPS_STOCK_INWARD_DELIEVERY_CHELLANID + " = ?",new String[]{String.valueOf(chellanProductDto.getProductId()),String.valueOf(godownStockOutwardDto.getDeliveryChallanId())}));
//        }
//
//
//
//
//    }
//
//
//    //Get Product Name by Product Id
//    public String getProductName(long _id) {
//        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " where " + KEY_ID + "=" + _id;//+" AND " +SmartDCRConstants.KEY_BENEFICIARY_ACTIVE +"=0";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        if (cursor.getCount() == 0) {
//            return null;
//        } else
//            return new ProductDto(cursor).getName();
//    }


    // database connection  close
    public synchronized void closeConnection() {
        if (dbHelper != null) {
            dbHelper.close();
            database.close();
            dbHelper = null;
            database = null;
        }
    }


}