package com.challenge.mcculloch.bodybuild;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.challenge.mcculloch.bodybuild.helpers.NoteLiteRepo;
import com.challenge.mcculloch.bodybuild.helpers.Utilities;
import com.challenge.mcculloch.bodybuild.objects.NoteLite;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.BitmapInfo;

/**
 * Created by wooan on 9/10/2015.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    int userId, adapterPosition;
    private EditText edittext_note;
    private String userName, userAge, userCity, userCountry, userImage, userState, userRealName, userHeight, userWeight, userBodyFat;
    private NoteLiteRepo noteLiteRepo;
    public Future<JsonArray> loading;
    private android.support.v7.widget.AppCompatButton button_cancel, button_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        noteLiteRepo = new NoteLiteRepo(this);

        initExtras();
        imageTransition();
        Toolbar toolbar = (Toolbar) findViewById(R.id.standard_toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle(userName);
            toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent output = new Intent(DetailActivity.this, MainActivity.class);
//                    setResult(RESULT_OK, output);
                    setResult(adapterPosition);
                    onBackPressed();
                }
            });
        }

        initViews();
    }

    private void initExtras() {
        Intent activityThatCalled = getIntent();
        userId = activityThatCalled.getExtras().getInt(Constants.exUserid);
        userName = activityThatCalled.getExtras().getString(Constants.exUsername);
        userAge = activityThatCalled.getExtras().getString(Constants.exUserAge);
        userCity = activityThatCalled.getExtras().getString(Constants.exUserCity);
        userCountry = activityThatCalled.getExtras().getString(Constants.exUserCountry);
        userImage = activityThatCalled.getExtras().getString(Constants.exUserImage);
        userState = activityThatCalled.getExtras().getString(Constants.exUserState);
        userRealName = activityThatCalled.getExtras().getString(Constants.exUserRealName);
        userHeight = activityThatCalled.getExtras().getString(Constants.exUserHeight);
        userWeight = activityThatCalled.getExtras().getString(Constants.exUserWeight);
        userBodyFat = activityThatCalled.getExtras().getString(Constants.exUserBodyFat);
        adapterPosition = activityThatCalled.getExtras().getInt(Constants.exPosition);
    }

    private void initViews() {
//        Transition
        TextView textview_usercountry = (TextView) findViewById(R.id.textview_usercountry);
        textview_usercountry.setText(userCountry);
        ViewCompat.setTransitionName(textview_usercountry, getResources().getString(R.string.transition_country));

        TextView textview_userstate = (TextView) findViewById(R.id.textview_userstate);
        textview_userstate.setText(new StringBuilder().append(userState).append(",").toString());
        ViewCompat.setTransitionName(textview_userstate, getResources().getString(R.string.transition_state));

        TextView textview_usercity = (TextView) findViewById(R.id.textview_usercity);
        textview_usercity.setText(userCity);
        ViewCompat.setTransitionName(textview_usercity, getResources().getString(R.string.transition_city));

        TextView textview_username = (TextView) findViewById(R.id.textview_username);
        textview_username.setText(userName);
        ViewCompat.setTransitionName(textview_username, getResources().getString(R.string.transition_username));

        TextView textview_userage = (TextView) findViewById(R.id.textview_userage);
        textview_userage.setText(Utilities.GetUserAge(userAge));
        ViewCompat.setTransitionName(textview_userage, getResources().getString(R.string.transition_age));

//        NonTransition
        TextView textview_userrealname = (TextView) findViewById(R.id.textview_userrealname);
        textview_userrealname.setText(userRealName);

        TextView textview_userheight = (TextView) findViewById(R.id.textview_userheight);

        int feet = 0;
        int inchs = 0;
        if (!TextUtils.isEmpty(userHeight)) {
            int height = Integer.parseInt(userHeight);
            feet = (int) Math.ceil(height / 12);
            inchs = (height - (feet * 12));
        }

        textview_userheight.setText(new StringBuilder().append(feet).append("'").append(inchs).append('\u0022').toString());

        TextView textview_userweight = (TextView) findViewById(R.id.textview_userweight);
        textview_userweight.setText(new StringBuilder().append(userWeight).append("lbs").toString());

        TextView textview_bodyfat = (TextView) findViewById(R.id.textview_bodyfat);
        textview_bodyfat.setText(new StringBuilder().append(userBodyFat).append("%").toString());

        edittext_note = (EditText) findViewById(R.id.edittext_note);

        String memberNote = "";
        if (noteLiteRepo.getCount(userId) > 0)
            memberNote = noteLiteRepo.getNoteByMember(userId).getUserNote();

        edittext_note.setText(memberNote);


        button_cancel = (android.support.v7.widget.AppCompatButton) findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);
        button_save = (android.support.v7.widget.AppCompatButton) findViewById(R.id.button_save);
        button_save.setOnClickListener(this);
    }

    private void imageTransition() {
        final ImageView imageview_userimage = (ImageView) findViewById(R.id.imageview_userimage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            String bitmapKey = getIntent().getStringExtra(Constants.exBitMapInfo);
            BitmapInfo bi = Ion.getDefault(this)
                    .getBitmapCache()
                    .get(bitmapKey);
            imageview_userimage.setImageBitmap(bi.bitmap);

            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().getEnterTransition().removeListener(this);
                    }

                    // load the full version, crossfading from the thumbnail image
                    Ion.with(imageview_userimage)
                            .crossfade(true)
                            .load(userImage);
                }
            });
        } else {
            Ion.with(imageview_userimage)
                    .centerCrop()
                    .load(userImage);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel:
                clearNote();
                break;
            case R.id.button_save:
                saveNote();
                break;
        }
    }

    private void clearNote() {
        noteLiteRepo.deleteNoteLite(userId);
        edittext_note.setText("");

        Snackbar.make(findViewById(R.id.parent_detail_layout), "Note Cleared", Snackbar.LENGTH_LONG)
                .show();
    }

    private void saveNote() {
        NoteLite noteLite = new NoteLite();
        noteLite.setMemberId(userId);
        noteLite.setUserNote(edittext_note.getText().toString());
        if (!TextUtils.isEmpty(edittext_note.getText().toString())) {
            if (noteLiteRepo.getCount(userId) > 0)
                noteLiteRepo.updateNoteList(noteLite);
            else {
                noteLiteRepo.insertNoteLite(noteLite);
            }
        }

        Snackbar.make(findViewById(R.id.parent_detail_layout), "Note Saved", Snackbar.LENGTH_LONG)
                .show();
    }
}
