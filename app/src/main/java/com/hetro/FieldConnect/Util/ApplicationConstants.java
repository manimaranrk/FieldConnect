package com.hetro.FieldConnect.Util;


public class ApplicationConstants {

    //http://128.199.143.2/fieldconnect/index.php

    public static String url_server = "http://128.199.143.2/fieldconnect1";

    public static String url_validate_user = url_server+"/user_login.php";
    public static String url_validate_user1 = url_server+"/user_login1.php";
    public static String url_logout = url_server+"/logout.php";
    public static String url_get_tower_userid = url_server+"/get_tower_by_userid.php";
    public static String url_get_tower_userid1 = url_server+"/get_tower_by_userid1.php";
    public static String url_get_tower_by_siteid = url_server+"/get_tower_by_siteid.php";
    public static String url_get_tower_by_siteid1 = url_server+"/get_tower_by_siteid1.php";

    public static String url_check_siteid = url_server+"/check_site_id.php";

    public static String url_update_status= url_server+"/updateStatus.php";
    public static String url_update_status1= url_server+"/updateStatus1.php";

    public static String url_select_sitetype= url_server+"/select_sitetype.php";
    public static String url_select_towertype= url_server+"/select_towertype.php";

    public static String url_client_represent= url_server+"/client_represent.php";
    public static String url_survey_data= url_server+"/survey_data.php";


    public static String url_image_upload= url_server+"/site_image_upload.php";

    public static String url_tenant_details= url_server+"/tenant_details.php";
    public static String url_tenant_details1= url_server+"/tenant_details1.php";

    public static String url_check_complet_status= url_server+"/check_complet_status.php";

//    public static String url_update_user_status = url_server+"/current_login_status.php";
//
//    public static String url_validate_user_details = url_server+"/validate_user_details.php";
//    public static String url_create_password = url_server+"/create_password.php";
//    public static String url_change_password = url_server+"/change_password.php";

    //----------------------------------------------------------------------------------------------

    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";

    public static final String TAG_USER = "USER";
    public static final String TAG_USER_ID = "USER_ID";
    public static final String TAG_USERNAME = "USER_NAME";
    public static final String TAG_USER_CIRCLE = "USER_CIRCLE";
    public static final String TAG_GROUP_INDEX = "GROUP_INDEX";

    //----------------------------------------------------------------------------------------------

    public static final String TAG_TOWER = "TOWER";
    public static final String TAG_TOWER_UNIQ_ID = "UNIQ_IDENTITY";

    public static final String TAG_TOWER_PRE_STATUS_NAME = "PRE_STATUS_NAME";
    public static final String TAG_TOWER_POST_STATUS_NAME = "POST_STATUS_NAME";

    public static final String TAG_TOWER_PRE_STATUS = "PRE_STATUS";
    public static final String TAG_TOWER_POST_STATUS = "POST_STATUS";
    public static final String TAG_TOWER_SCHEDULE_DATETIME = "SCHEDULE_DATETIME";
    public static final String TAG_TOWER_VISIT_DATETIME = "VISIT_DATETIME";
    public static final String TAG_TOWER_SITE_ID = "SITE_ID";
    public static final String TAG_TOWER_TEAM_INDEX = "TEAM_INDEX";
    public static final String TAG_TOWER_REMARKS = "REMARKS";
    public static final String TAG_TOWER_LATITUDE = "LATITUDE";
    public static final String TAG_TOWER_LONGITUDE = "LONGITUDE";
    public static final String TAG_TOWER_APPROVED_BY = "APPROVED_BY";
    public static final String TAG_TOWER_COMPLETE_STATUS = "COMPLETE_STATUS";
    public static final String TAG_TOWER_VISIT_INDEX = "VISIT_INDEX";


    public static final String TAG_TOWER_RECORDNO = "RECORE_NO";
    public static final String TAG_TOWER_CIRCLE = "CIRCLE_NO";
    public static final String TAG_TOWER_SITEID = "SITE_ID";
    public static final String TAG_TOWER_ADDRESS = "SITE_ADDRESS";
    public static final String TAG_TOWER_SITE_NAME = "SITE_NAME";
    public static final String TAG_TOWER__SITE_LATITUDE = "SITE_LATITUDE";
    public static final String TAG_TOWER_SITE_LONGITUDE = "SITE_LONGITUDE";
    public static final String TAG_TOWER_ENGINEER_NAME = "SITE_ENGINEER_NAME";
    public static final String TAG_TOWER_ENGINEER_PHONE = "SITE_ENGINEER_PHONE";
    public static final String TAG_TOWER_ID_OD = "SITE_ID_OD";

    //=================================================
    public static final String TAG_EXISTING_TOWER = "Tower";

    public static final String TAG_EXISTING_TOWER_ID = "Tower_Id";
    public static final String TAG_EXISTING_TOWER_TYPE = "Tower_Type";
    //==========================================================


    public static final String TAG_EXISTING_SITE = "Site";

    public static final String TAG_EXISTING_SITE_ID = "Site_Id";
    public static final String TAG_EXISTING_SITE_TYPE = "Site_Type";
    //==========================================================

    public static final String TAG_CHECK_SITE = "Site";

    public static final String TAG_COMPLETE_STATUS= "complete_status";

//    public static final String TAG_TOWER_INDEX = "TOWER_INDEX";
//    public static final String TAG_SITE_ID = "SITE_ID1";
//    public static final String TAG_SITE_NAME = "SITE_NAME";
//    public static final String TAG_TOWER_TYPE = "TOWER_TYPE";
//    public static final String TAG_SITE_PLACEMENT = "SITE_PLACEMENT";
//
//    public static final String TAG_SITE_TECH_NAME = "SITE_TECH_NAME";
//    public static final String TAG_SITE_TECH_PHONE = "SITE_TECH_PHONE";
//    public static final String TAG_SITE_TECH_EMAIL = "SITE_TECH_EMAIL";
//    public static final String TAG_SITE_ENGINEER_NAME = "SITE_ENGINEER_NAME";
//    public static final String TAG_SITE_ENGINEER_PHONE = "SITE_ENGINEER_PHONE";
//
//    public static final String TAG_SITE_LATITUDE = "SITE_LATITUDE";
//    public static final String TAG_SITE_LONGITUDE = "SITE_LONGITUDE";
//    public static final String TAG_SITE_ADDR1 = "SITE_ADDR1";
//    public static final String TAG_SITE_ADDR2 = "SITE_ADDR2";
//    public static final String TAG_SITE_POST_OFFICE = "SITE_POST_OFFICE";
//
//    public static final String TAG_SITE_ZIP_CODE = "SITE_ZIP_CODE";
//    public static final String TAG_SITE_DISTRICT = "SITE_DISTRICT";
//    public static final String TAG_SITE_STATE = "SITE_STATE";
//    public static final String TAG_SITE_COUNTRY = "SITE_COUNTRY";
//    public static final String TAG_SITE_STATUS_ACTIVE = "SITE_STATUS_ACTIVE";
//
//    public static final String TAG_SITE_TYPE = "SITE_TYPE";
//    public static final String TAG_SITE_LAST_UPDATEDBY = "SITE_LAST_UPDATEDBY";
//    public static final String TAG_SITE_LAST_UPDATED_DATE = "SITE_LAST_UPDATED_DATE";
//    public static final String TAG_SITE_CLUSTER_INDEX = "SITE_CLUSTER_INDEX";
//    public static final String TAG_SITE_INFRASTRUCTURE_INDEX = "SITE_INFRASTRUCTURE_INDEX";





}
