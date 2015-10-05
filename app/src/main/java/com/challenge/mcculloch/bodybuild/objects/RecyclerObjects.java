package com.challenge.mcculloch.bodybuild.objects;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.challenge.mcculloch.bodybuild.R;


/**
 * Created by wooan_000 on 12/18/2014.
 */
public class RecyclerObjects {
    public static class HomeItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView vhUserImageUrl;
        public TextView vhUsername;
        public TextView vhAge;
        public TextView vhCity;
        public TextView vhState;
        public TextView vhCountry;
        public android.support.v7.widget.AppCompatButton vhNoteButton;
        public CardView vhCecycle_card_view;

        public HomeItemViewHolder(View v) {
            super(v);
            vhUserImageUrl = (ImageView) v.findViewById(R.id.user_image);

            vhUsername = (TextView) v.findViewById(R.id.home_userName);
            vhAge = (TextView) v.findViewById(R.id.home_userAge);
            vhCity = (TextView) v.findViewById(R.id.home_userCity);
            vhState = (TextView) v.findViewById(R.id.home_userState);
            vhCountry = (TextView) v.findViewById(R.id.home_userCountry);
            vhNoteButton = (android.support.v7.widget.AppCompatButton) v.findViewById(R.id.note_button);
            vhCecycle_card_view = (CardView) v.findViewById(R.id.recycle_card_view);
        }
    }

}
