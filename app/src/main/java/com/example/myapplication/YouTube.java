package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Vector;

public class YouTube extends Fragment {
    public YouTube() {

    }

    RecyclerView recyclerView;
    Vector<YouTubeVideos> youtubeVideos = new Vector<YouTubeVideos>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));

        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/oKUEbsJDvuo\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/jPFrHYXYxak\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Is7glC9Jp7Q\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ioNng23DkIM\" frameborder=\"0\" allowfullscreen></iframe>") );

        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);

        recyclerView.setAdapter(videoAdapter);

        return view;
    }

    public class YouTubeVideos {
        String videoUrl;

        public YouTubeVideos() {
        }

        public YouTubeVideos(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }

    public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

        List<YouTubeVideos> youtubeVideoList;

        public VideoAdapter() {
        }

        public VideoAdapter(List<YouTubeVideos> youtubeVideoList) {
            this.youtubeVideoList = youtubeVideoList;
        }

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.fragment_youtube_sub, parent, false);

            return new VideoViewHolder(view);

        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {

            holder.videoWeb.loadData( youtubeVideoList.get(position).getVideoUrl(), "text/html" , "utf-8" );

        }

        @Override
        public int getItemCount() {
            return youtubeVideoList.size();
        }

        public class VideoViewHolder extends RecyclerView.ViewHolder{

            WebView videoWeb;

            public VideoViewHolder(View itemView) {
                super(itemView);

                videoWeb = (WebView) itemView.findViewById(R.id.videoWebView);

                videoWeb.getSettings().setJavaScriptEnabled(true);
                videoWeb.setWebChromeClient(new WebChromeClient() {

                } );
            }
        }
    }
}

