package com.example.newsapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.newsapp.models.Article;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<Article> articles;
    private Context context;
    private OnItemClickListener onItemClickListener;

    Adapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view, onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final  MyViewHolder holder=holders;
        Article model =articles.get(position);

        RequestOptions requestOptions=new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.newsImage);
        String date= model.getPublishedAt().substring(0,10);
        String time=model.getPublishedAt().substring(12,19);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.source.setText(model.getSource().getName());
        holder.time.setText("\u2022"+time);
        holder.author.setText(model.getAuthor());
        holder.publishedAt.setText(date);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public static class  MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title,desc,author,publishedAt,source,time;
        ImageView newsImage;
        ProgressBar progressBar;
        OnItemClickListener onItemClickListener;
        MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.description);
            author=itemView.findViewById(R.id.author);
            publishedAt=itemView.findViewById(R.id.publishedAt);
            source=itemView.findViewById(R.id.source);
            time=itemView.findViewById(R.id.time);
            progressBar=itemView.findViewById(R.id.progress_load_photo);
            newsImage=itemView.findViewById(R.id.newsImage);

            this.onItemClickListener=onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v,getAdapterPosition());
        }
    }
}
