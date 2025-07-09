package com.java.wangyiding.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.java.wangyiding.R;
import com.java.wangyiding.data.db.HistoryDatabase;
import com.java.wangyiding.data.db.MyDatabase;
import com.java.wangyiding.data.model.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private boolean showFooter = false;

    private List<News> data;
    private OnItemClickListener listener;

    private HistoryDatabase db;


    public interface OnItemClickListener {
        void onClick(News news);
    }


    public void showFooter(boolean show) {
        this.showFooter = show;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size() + (showFooter ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (showFooter && position == data.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public NewsAdapter(Context context, List<News> data, OnItemClickListener listener) {
        db =  MyDatabase.getInstance(context);
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_footer, parent, false);
            return new NewsViewHolder(view); // 用相同 ViewHolder 承载即可
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            return new NewsViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_FOOTER) return;

        News news = data.get(position);
        String pic=news.getImageJson();
        if (pic != null && pic.length() > 2) {
            String[] pic_paths = pic.substring(1, pic.length() - 1).split(",");

            if (pic_paths.length > 0 && !pic_paths[0].trim().isEmpty()) {
                String url = pic_paths[0].trim().replaceAll("\"", ""); // 去除可能存在的引号
                holder.pic.setVisibility(View.VISIBLE);
                Glide.with(holder._view)
                        .load(url)
                        .placeholder(R.drawable.default_image_placeholder)
                        .into(holder.pic);
                        //.placeholder(R.drawable.placeholder)   // 可选：加载中占位图
                        //.error(R.drawable.no_image)            // 可选：失败占位图

            } else {
                holder.pic.setVisibility(View.GONE); // ✅ 没图时隐藏
            }
        } else {
            holder.pic.setVisibility(View.GONE); // ✅ 空图片字段也隐藏
        }
        holder.title.setText(news.getTitle());
        holder.source.setText(news.getPublisher() + "  " + news.getPublishTime());
        holder.itemView.setOnClickListener(v -> listener.onClick(news));
        new Thread(()->{
            if(!db.newsDao().hasViewed(news.getNewsId()))
                holder.title.setTextColor(Color.parseColor("#1f1e33"));
            else
                holder.title.setTextColor(Color.parseColor("#888888"));
        }).start();

    }



    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, source;
        View _view;
        ImageView pic;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            _view=itemView;
            title = itemView.findViewById(R.id.newsTitle);
            source = itemView.findViewById(R.id.newsSource);
            pic =itemView.findViewById(R.id.newsPic);
        }
    }
}
