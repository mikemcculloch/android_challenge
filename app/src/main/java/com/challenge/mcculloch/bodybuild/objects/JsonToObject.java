package com.challenge.mcculloch.bodybuild.objects;

import android.util.Log;

import com.challenge.mcculloch.bodybuild.BuildConfig;
import com.challenge.mcculloch.bodybuild.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.List;
import java.util.Vector;

/**
 * Created by wooan on 9/10/2015.
 */
public class JsonToObject {

    public static List<Member> JsonToMemberList(JsonArray result) {
        List<Member> gig = new Vector<>();
        try {
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.setDateFormat("s").create();
            for (int i = 0; i < result.size(); i++) {
                gig.add(gson.fromJson(result.get(i).getAsJsonObject(), Member.class));
            }


        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(Constants.LOG, e.getMessage());
            }
        }

        return gig;
    }
}
