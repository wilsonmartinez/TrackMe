package com.wmartinez.devep.trackme.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmartinez.devep.trackme.R;
import com.wmartinez.devep.trackme.TrackActivity;
import com.wmartinez.devep.trackme.pojo.Followed;

import java.util.ArrayList;

/**
 * Created by WilsonMartinez on 12/13/2016.
 */

public class FollowedAdapter extends RecyclerView.Adapter<FollowedAdapter.FollowedViewHolder> {

    private ArrayList<Followed> followeds;
    private Activity activity;
//    String nickname;
//    String email;

    public FollowedAdapter(ArrayList<Followed> followeds, Activity activity) {
        this.followeds = followeds;
        this.activity = activity;
    }

    @Override
    public FollowedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user, parent, false);
        return new FollowedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FollowedViewHolder holder, int position) {
        final Followed followed = followeds.get(position);
        //holder.imgPhoto.setImageDrawable(R.drawable.icon_user);
        holder.tvNickName.setText(String.valueOf(followed.getNickname()));
        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TrackActivity.class);
//                intent.putExtra("user_name", nickname);
//                intent.putExtra("email", email);
                intent.putExtra("user_name", String.valueOf(followed.getNickname()));
                intent.putExtra("email", String.valueOf(followed.getEmail()));
                activity.startActivity(intent);

            }
        });
        holder.imgBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return followeds.size();
    }

    public class FollowedViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        private TextView tvNickName;
        private ImageButton imgBtnSettings;

        public FollowedViewHolder(View itemView) {
            super(itemView);

            imgPhoto = (ImageView) itemView.findViewById(R.id.imgCVUser);
            tvNickName = (TextView) itemView.findViewById(R.id.tvCVUser);
            imgBtnSettings = (ImageButton) itemView.findViewById(R.id.imgBtnSetting);
        }
    }
}
