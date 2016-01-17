package com.sundy.icare.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity 控制类
 * <p/>
 * Created by sundy on 15/12/6.
 */
public class ActivityController {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }

}
