package com.jaksic.nikola.expandablerecycler;

import android.os.Handler;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {

    private List<Movie> list;
    private RecyclerView mRecyclerView;
    private RecyclerView.SmoothScroller mSmoothScroller;

    public RecAdapter(List<Movie> list) {
        this.list = list;
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecViewHolder holder, int position) {
        Movie movie = list.get(position);

        holder.bind(movie);

        holder.itemView.setOnClickListener(v -> {
            boolean expanded = movie.isExpanded();
            movie.setExpanded(!expanded);
            notifyItemChanged(position);

            if (movie.isExpanded()) {
                new Handler().postDelayed(() -> {
                    mSmoothScroller.setTargetPosition(position);
                    mRecyclerView.getLayoutManager().startSmoothScroll(mSmoothScroller);
                }, 100);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;

        mSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_ANY;
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 150f / displayMetrics.densityDpi;
            }
        };
    }

    public class RecViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView genre;
        private TextView year;
        private View subItem;

        public RecViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_title);
            genre = itemView.findViewById(R.id.sub_item_genre);
            year = itemView.findViewById(R.id.sub_item_year);
            subItem = itemView.findViewById(R.id.sub_item);
        }

        private void bind(Movie movie) {
            boolean expanded = movie.isExpanded();

            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            title.setText(movie.getTitle());
            genre.setText("Genre: " + movie.getGenre());
            year.setText("Year: " + movie.getYear());
        }
    }
}