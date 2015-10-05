package com.challenge.mcculloch.bodybuild;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.challenge.mcculloch.bodybuild.adapters.HomeItemRecycler;
import com.challenge.mcculloch.bodybuild.helpers.NoteLiteRepo;
import com.challenge.mcculloch.bodybuild.objects.JsonToObject;
import com.challenge.mcculloch.bodybuild.objects.Member;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.BitmapInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    public RecyclerView home_recycler_view;
    public HomeItemRecycler homeItemRecycler;
    public Future<JsonArray> loading;
    private int skipCount = 5;
    private boolean overRide = false;
    private String sortBy = "";
    private String sortDirection = Constants.sortAsc;

    private int previousTotal = 0;
    private boolean loadingBool = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    public Dialog dialogProgress;

    private Spinner spinner_orderby;
    private android.support.v7.widget.SwitchCompat switch_direction;

    public View noteReadOnly;
    public AlertDialog noteDialog;
    private NoteLiteRepo noteLiteRepo;
    public TextView textview_note_readonly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupTransistion();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteLiteRepo = new NoteLiteRepo(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.standard_toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.app_name));
        }
        inflateNoteDialog();
        initView();
        initDialogView();
        initSpinner();
        initSwitch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void initView() {
        try {
            home_recycler_view = (RecyclerView) findViewById(R.id.home_recycler_view);
            home_recycler_view.setItemAnimator(new DefaultItemAnimator());

            home_recycler_view.setHasFixedSize(false);

            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mLayoutManager.scrollToPosition(0);
            home_recycler_view.setLayoutManager(mLayoutManager);

            home_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    visibleItemCount = home_recycler_view.getChildCount();
                    totalItemCount = homeItemRecycler.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (loadingBool) {
                        if (totalItemCount > previousTotal) {
                            loadingBool = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loadingBool && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        load();
                        loadingBool = true;
                    }
                }
            });

            List<Member> placeHolder = new Vector<>();
            homeItemRecycler = new HomeItemRecycler(placeHolder, this);
            home_recycler_view.setAdapter(homeItemRecycler);
            load();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(Constants.LOG, e.getMessage());
            }
        }
    }

    public void initDialogView() {
        textview_note_readonly = (TextView) noteReadOnly.findViewById(R.id.textview_note_readonly);
    }

    public void openDetailActivity(View view, int position) {
        try {
            Member adapterItem = homeItemRecycler.GetItemAt(position);
            Intent newIntent = new Intent(getApplicationContext(), DetailActivity.class);

            newIntent.putExtra(Constants.exUserid, adapterItem.getUserId());
            newIntent.putExtra(Constants.exUsername, adapterItem.getUserName());
            newIntent.putExtra(Constants.exUserAge, adapterItem.getBirthday());
            newIntent.putExtra(Constants.exUserImage, adapterItem.getProfilePicUrl());
            newIntent.putExtra(Constants.exUserCity, adapterItem.getCity());
            newIntent.putExtra(Constants.exUserState, adapterItem.getState());
            newIntent.putExtra(Constants.exUserCountry, adapterItem.getCountry());
            newIntent.putExtra(Constants.exUserRealName, adapterItem.getRealName());
            newIntent.putExtra(Constants.exUserHeight, String.valueOf(adapterItem.getHeight()));
            newIntent.putExtra(Constants.exUserWeight, String.valueOf(adapterItem.getWeight()));
            newIntent.putExtra(Constants.exUserBodyFat, String.valueOf(adapterItem.getBodyfat()));
            newIntent.putExtra(Constants.exPosition, position);

            BitmapInfo bi = Ion.with((ImageView) view.findViewById(R.id.user_image)).getBitmapInfo();
            newIntent.putExtra(Constants.exBitMapInfo, bi.key);

            newIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Pair p1 = Pair.create(view.findViewById(R.id.home_userName), getResources().getString(R.string.transition_username));
                Pair p2 = Pair.create(view.findViewById(R.id.home_userAge), getResources().getString(R.string.transition_age));
                Pair p3 = Pair.create(view.findViewById(R.id.home_userCity), getResources().getString(R.string.transition_city));
                Pair p4 = Pair.create(view.findViewById(R.id.home_userState), getResources().getString(R.string.transition_state));
                Pair p5 = Pair.create(view.findViewById(R.id.home_userCountry), getResources().getString(R.string.transition_country));
                Pair p6 = Pair.create(view.findViewById(R.id.user_image), getResources().getString(R.string.transition_bitmap));

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, p1, p2, p3, p4, p5, p6);
                ActivityCompat.startActivityForResult(this, newIntent, 0, options.toBundle());
            } else {
                startActivityForResult(newIntent, 0);
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(Constants.LOG, e.getMessage());
            }
        }
    }

    public void initSpinner() {
        spinner_orderby = (Spinner) findViewById(R.id.spinner_orderby);

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add(getText(R.string.spinner_default).toString());
        stringArrayList.add(getText(R.string.spinner_username).toString());
        stringArrayList.add(getText(R.string.spinner_age).toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_orderby.setAdapter(adapter);

        spinner_orderby.post(new Runnable() {
            @Override
            public void run() {
                spinner_orderby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
                        if (pos == 1)
                            sortBy = Constants.sortByName;
                        if (pos == 2)
                            sortBy = Constants.sortByAge;
                        if (pos > 0) {
                            reloadHomeRecycler();
                            switch_direction.setVisibility(View.VISIBLE);
                        } else {
                            switch_direction.setVisibility(View.GONE);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }
        });
    }

    public void initSwitch() {
        switch_direction = (android.support.v7.widget.SwitchCompat) findViewById(R.id.switch_direction);
        switch_direction.setVisibility(View.GONE);
        switch_direction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    sortDirection = Constants.sortDesc;
                else
                    sortDirection = Constants.sortAsc;

                reloadHomeRecycler();
            }
        });
    }

    public void load() {
        if (loading != null && !loading.isDone() && !loading.isCancelled())
            return;

        loadDialog(false, false);

        String url = Constants.rest_member;
        if (!TextUtils.isEmpty(sortBy)) {
            url += "sort=" + sortBy + sortDirection + "&";
        }

        url += "limit=5";

        if (homeItemRecycler != null) {
            if (homeItemRecycler.getItemCount() > 0 || overRide) {
                url += "&skip=" + skipCount;
                skipCount += 5;
                overRide = false;
            }
        }

        loading = Ion
                .with(this)
                .load(url)
                .setHeader("Cache-Control", "No-Cache")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                                 @Override
                                 public void onCompleted(Exception e, JsonArray jsonArray) {
                                     try {
//                                         mSwipeRefreshLayout.setRefreshing(false);
                                         if (jsonArray != null) {
                                             List<Member> resultsList = JsonToObject.JsonToMemberList(jsonArray);

                                             for (Member item : resultsList) {
                                                 if (item.getRealName() != null)
                                                     homeItemRecycler.add(item, resultsList.size() - 1);
                                             }
                                             homeItemRecycler.notifyDataSetChanged();
                                         }
                                         dialogProgress.dismiss();
                                     } catch (Exception ex) {
                                         if (BuildConfig.DEBUG) {
                                             Log.e(Constants.LOG, ex.getMessage());
                                         }
                                     } finally {
                                         if (homeItemRecycler.getItemCount() < 5) {
                                             overRide = true;
                                             load();
                                         }

                                         dialogProgress.dismiss();
                                     }
                                 }
                             }
                );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sortby) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_sortby:
                return true;

            case R.id.menu_name_sortby:
                sortBy = Constants.sortByName;
                reloadHomeRecycler();
                return true;

            case R.id.menu_age_sortby:
                sortBy = Constants.sortByAge;
                reloadHomeRecycler();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadHomeRecycler() {
        homeItemRecycler.clear();
        skipCount = 5;
        totalItemCount = 0;
        previousTotal = 0;
        load();
    }

    public void loadDialog(boolean dimBackground, boolean cancelable) {
        dialogProgress = new Dialog(this);
        dialogProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProgress.setContentView(R.layout.dialog_loading);
        dialogProgress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProgress.setCancelable(cancelable);

        if (!dimBackground) {
            dialogProgress.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        dialogProgress.show();
    }

    public void setupTransistion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            //set the transition
            Transition ts = new android.transition.ChangeBounds();
//            ts.setStartDelay(100);
            //set the duration
            ts.setDuration(500);
            getWindow().setSharedElementEnterTransition(ts);
            getWindow().setSharedElementExitTransition(ts);
            getWindow().setSharedElementsUseOverlay(false);
//            getWindow().setAllowEnterTransitionOverlap(false); --meh
//            getWindow().setAllowReturnTransitionOverlap(false); --meh
            getWindow().setEnterTransition(ts);
            //set an exit transition so it is activated when the current activity exits
            getWindow().setExitTransition(ts);

        }
    }

    public void inflateNoteDialog() {
        try {

            Rect displayRectangle = new Rect();
            Window window = getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);


            LayoutInflater factory = LayoutInflater.from(this);
            noteReadOnly = factory.inflate(
                    R.layout.dialog_note, null);
            noteDialog = new AlertDialog.Builder(this).create();
            noteDialog.setView(noteReadOnly);


            noteReadOnly.findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    noteDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            if (BuildConfig.DEBUG) {
                Log.e(Constants.LOG, ex.getMessage());
            }
        }

    }

    public void getNote(int memberId) {
        textview_note_readonly.setText("");
        if (noteLiteRepo.getCount(memberId) > 0)
            textview_note_readonly.setText(noteLiteRepo.getNoteByMember(memberId).getUserNote());
    }

    public int getNoteCount(int memberId) {
        return noteLiteRepo.getCount(memberId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeItemRecycler.sendUpdate(resultCode);
    }
}
