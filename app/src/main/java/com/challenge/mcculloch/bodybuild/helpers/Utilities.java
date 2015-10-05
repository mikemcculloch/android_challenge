package com.challenge.mcculloch.bodybuild.helpers;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wooan on 10/3/2015.
 */
public class Utilities {

    private Context _context;

    // constructor
    public Utilities(Context context) {
        this._context = context;
    }


    public static String GetUserAge(String compareTo) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(compareTo);
            Calendar myBirthDate = Calendar.getInstance();
            myBirthDate.clear();
            myBirthDate.setTime(date);
            Calendar now = Calendar.getInstance();
            Calendar clone = (Calendar) myBirthDate.clone(); // Otherwise changes are been reflected.
            int years = -1;
            while (!clone.after(now)) {
                clone.add(Calendar.YEAR, 1);
                years++;
            }
            return String.valueOf(years);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

}
