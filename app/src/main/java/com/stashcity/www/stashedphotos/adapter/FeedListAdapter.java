package com.stashcity.www.stashedphotos.adapter;

/**
 * Created by sravan on 15/10/16.
 */

import com.stashcity.www.stashedphotos.FeedImageView;
import com.stashcity.www.stashedphotos.ApplicationController;
import com.stashcity.www.stashedphotos.R;
import com.stashcity.www.stashedphotos.model.FeedItem;
import com.stashcity.www.stashedphotos.volley.VolleyNetworkCalls;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class FeedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    private Context context;
    private static final String VIDEO_PATH = "https://inducesmile.com/wp-content/uploads/2016/05/small.mp4";
    ImageLoader imageLoader = ApplicationController.getInstance().getImageLoader();
    VolleyNetworkCalls VNC;

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems,Context context) {
        this.activity = activity;
        this.feedItems = feedItems;
        this.context = context;
    }



    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = ApplicationController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        //TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.feedImage1);
         VideoView feedVideoView = (VideoView)convertView.findViewById(R.id.feedVideo);

        final FeedItem item = feedItems.get(position);

        name.setText(item.getName());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

       /* // Checking for null feed url
        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));

            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }*/

        // user profile pic
        profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        if (item.getImage().endsWith("mp4")){
            feedImageView.setVisibility(View.GONE);
            feedVideoView.setVisibility(View.VISIBLE);
            feedVideoView.setVideoPath(VIDEO_PATH);
            feedVideoView.start();
            Log.e("came here ","to show video");
        }
        // Feed image
        else if (item.getImage() != null) {
            feedImageView.setImageUrl(item.getImage(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

      /*  ImageButton BtnImg = (ImageButton) convertView.findViewById(R.id.btnLike);
        BtnImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("loveBTN",":"+item.getId());
                VNC =new VolleyNetworkCalls();
                VNC.LikePost(item.getId());

            }
        });*/
        AppCompatButton shareBtn = (AppCompatButton) convertView.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, item.getUrl());
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this photo!");
                context.startActivity(Intent.createChooser(intent, "Share"));

            }
        });

        feedVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //feedVideoView.start();
            }
        });


        return convertView;
    }

}
