package com.automat.manager.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalValue {
    public static final String PREF_SETTING = "setting";
    public static String KEY_IP = "";
    public static final String PREF_NAME = "prefs";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_REMEMBER = "remember";
    public static final String KEY_AUTH = "api";
    public static final String KEY_ROLE = "role";
    public static final String NAME_USER = "nameUser";
    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";
    public static final String NO_PERMISSION = "You don't have permission";
    public static final String TEXT_STATUS_QR_ITEM_1 = "Обычный";
    public static final String TEXT_STATUS_QR_ITEM_2 = "Узнать подробнее";
    public static final String GET_NEW_ORDER = "NewOrder";
    public static final String LIST_OF_BALLOONS = "ListOfBalloons";
    public static final String COUNT_BALLOON = "CountBalloon";

    //FB
    public static final List<String> topics = Arrays.asList("COLLECTOR_ORDER", "test", "DRIVER_ORDER");
    //

    // Driver1
    public static final String OBJECT_GET_NEW_ORDER = "getNewOrder";
    public static final String OBJECT_DRIVER1 = "ObjectDriver1";
    public static final String DRIVER_ID_ORDER = "IdOrder";
    public static final String DRIVER_COUNT_ORDER_BALLOON = "orderCount";
    public static final String DRIVER_SIZE_ARR_ORDER = "driverSizeArrOrder";

    // Driver2
    public static final String DRIVER_ID_RETURN_ORDER = "IdReturnOrder";
    public static final String DRIVER_COUNT_RETURN_BALLOON = "countReturnBalloon";
    public static final String DRIVER_SIZE_ARR_RETURN_ORDER = "driverSizeArrReturnOrder";
}
