package com.challenge.mcculloch.bodybuild.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.challenge.mcculloch.bodybuild.MainActivity;
import com.challenge.mcculloch.bodybuild.R;
import com.challenge.mcculloch.bodybuild.helpers.Utilities;
import com.challenge.mcculloch.bodybuild.objects.Member;
import com.challenge.mcculloch.bodybuild.objects.RecyclerObjects;
import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.Vector;

/**
 * Created by wooan on 9/10/2015.
 */
public class HomeItemRecycler extends RecyclerView.Adapter<RecyclerObjects.HomeItemViewHolder> {

    private List<Member> memberList = new Vector<>();
    private MainActivity _activity;

    public HomeItemRecycler(List<Member> jsonObject, MainActivity activity) {
        this.memberList = jsonObject;
        _activity = activity;
    }

    public void add(Member item, int position) {
        memberList.add(item);
        notifyItemInserted(position);
    }

    public void sendUpdate(int position) {
        notifyItemChanged(position);
    }

    public void clear() {
        memberList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerObjects.HomeItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.row_home, viewGroup, false);
        return new RecyclerObjects.HomeItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerObjects.HomeItemViewHolder homeItemViewHolder, int position) {
        Member memberItem = memberList.get(position);
        final int currentPos = position;
        int memberId = 0;
        if (memberItem != null) {
            memberId = memberItem.getUserId();
            homeItemViewHolder.vhUsername.setText(memberItem.getUserName());
            homeItemViewHolder.vhCity.setText(memberItem.getCity());
            homeItemViewHolder.vhCountry.setText(memberItem.getCountry());
            homeItemViewHolder.vhState.setText(new StringBuilder().append(memberItem.getState()).append(",").toString());

            Ion.with(homeItemViewHolder.vhUserImageUrl)
                    .centerCrop()
                    .load(memberItem.getProfilePicUrl());

            homeItemViewHolder.vhAge.setText("");
            if (memberItem.getBirthday() != null) {
                homeItemViewHolder.vhAge.setText(Utilities.GetUserAge(memberItem.getBirthday()));
            }

            homeItemViewHolder.vhCecycle_card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _activity.openDetailActivity(v, currentPos);
                }
            });

            homeItemViewHolder.vhNoteButton.setVisibility(View.GONE);
            final int finalMemberId = memberId;
            if (_activity.getNoteCount(memberId) != 0)
                homeItemViewHolder.vhNoteButton.setVisibility(View.VISIBLE);

            homeItemViewHolder.vhNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _activity.getNote(finalMemberId);
                    _activity.noteDialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public Member GetItemAt(int position) {
        return memberList.get(position);
    }
}
