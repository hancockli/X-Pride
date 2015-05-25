/*
 * Copyright (c) 2015 Coder.HanXin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coderhanxin.xpride.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.coderhanxin.xpride.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.coderhanxin.xpride.BuildConfig.DEBUG;

/*
  This class is copied from PaperAirplane-Dev-Team/BlackLight
*/
public class TimeUtils {

    private static final String TAG = TimeUtils.class.getSimpleName();

    private static final long MILLIS_MIN = 1000 * 60;
    private static final long MILLIS_HOUR = MILLIS_MIN * 60;

    private static String JUST_NOW, MIN, HOUR, DAY, MONTH, YEAR,
            YESTERDAY, THE_DAY_BEFORE_YESTERDAY, TODAY;

    private static SimpleDateFormat day_format = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat date_format = new SimpleDateFormat("M-d HH:mm");
    private static SimpleDateFormat year_format = new SimpleDateFormat("yyyy-M-d HH:mm");
    private static SimpleDateFormat orig_format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);

    private static Calendar sCal1 = Calendar.getInstance(),
            sCal2 = Calendar.getInstance();

    private static TimeUtils mInstance;

    private TimeUtils(Context context) {
        Resources res = context.getResources();
        JUST_NOW = res.getString(R.string.time_just_now);
        MIN = res.getString(R.string.time_min);
        HOUR = res.getString(R.string.time_hour);
        DAY = res.getString(R.string.time_day);
        MONTH = res.getString(R.string.time_month);
        YEAR = res.getString(R.string.time_year);
        YESTERDAY = res.getString(R.string.time_yesterday);
        THE_DAY_BEFORE_YESTERDAY = res.getString(R.string.time_the_day_before_yesterday);
        TODAY = res.getString(R.string.time_today);
    }

    public static TimeUtils instance(Context context) {
        if (mInstance == null) {
            mInstance = new TimeUtils(context);
        }

        return mInstance;
    }

    private boolean isSameDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return nowDay == msgDay;
    }

    private boolean isYesterday(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return nowDay == (msgDay + 1);
    }

    private boolean isTheDayBeforeYesterday(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return nowDay == (msgDay + 2);
    }

    private boolean isSameYear(Calendar now, Calendar msg) {
        int nowYear = now.get(Calendar.YEAR);
        int msgYear = msg.get(Calendar.YEAR);

        return nowYear == msgYear;
    }

    public synchronized long parseTimeString(String created_at) {
        try {
            return orig_format.parse(created_at).getTime();
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Failed parsing time: " + created_at);
            }

            return -1;
        }
    }

    public synchronized String buildTimeString(String created_at) {
        return buildTimeString(parseTimeString(created_at));
    }

    public synchronized String buildTimeString(long millis) {
        Calendar cal = sCal1;

        cal.setTimeInMillis(millis);

        long msg = cal.getTimeInMillis();
        long now = System.currentTimeMillis();

        Calendar nowCalendar = sCal2;
        sCal2.setTimeInMillis(now);

        long differ = now - msg;
        long difsec = differ / 1000;

        if (difsec < 60) {
            return JUST_NOW;
        }

        long difmin = difsec / 60;

        if (difmin < 60) {
            return String.valueOf(difmin) + MIN;
        }

        long difhour = difmin / 60;

        if (difhour < 24 && isSameDay(nowCalendar, cal)) {
            return TODAY + " " + day_format.format(cal.getTime());
        }

        long difday = difhour / 24;

        if (difday < 31) {
            if (isYesterday(nowCalendar, cal)) {
                return YESTERDAY + " " + day_format.format(cal.getTime());
            } else if (isTheDayBeforeYesterday(nowCalendar, cal)) {
                return THE_DAY_BEFORE_YESTERDAY + " " + day_format.format(cal.getTime());
            } else {
                return date_format.format(cal.getTime());
            }
        }

        long difmonth = difday / 31;

        if (difmonth < 12 && isSameYear(nowCalendar, cal)) {
            return date_format.format(cal.getTime());
        }

        return year_format.format(cal.getTime());
    }
}
