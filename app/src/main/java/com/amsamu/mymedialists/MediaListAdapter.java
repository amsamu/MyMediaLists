package com.amsamu.mymedialists;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.amsamu.mymedialists.data.Title;
import com.amsamu.mymedialists.databinding.DisplayedListItemBinding;

public class MediaListAdapter extends ListAdapter<Title, MediaListAdapter.MediaListViewHolder> {

    public static final DiffUtil.ItemCallback<Title> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Title>() {
                @Override
                public boolean areItemsTheSame(@NonNull Title oldItem, @NonNull Title newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Title oldItem, @NonNull Title newItem) {
                    return oldItem.equals(newItem);
                }
            };


    protected MediaListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MediaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // parent = RecycleView
        DisplayedListItemBinding binding = DisplayedListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MediaListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaListViewHolder holder, int position) {
        Title title = getItem(position);
        holder.bind(title);
    }

    class MediaListViewHolder extends RecyclerView.ViewHolder{

        private DisplayedListItemBinding binding;

        public MediaListViewHolder(@NonNull DisplayedListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Title title){
            Log.d("MediaListAdapter","bind: title.name = " + title.name);
            binding.itemTitle.setText(title.name);
            binding.executePendingBindings();
        }

    }

}
