package com.amsamu.mymedialists.adapters;

import static com.amsamu.mymedialists.util.SharedMethods.formatDate;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.amsamu.mymedialists.R;
import com.amsamu.mymedialists.database.tables.Entry;
import com.amsamu.mymedialists.databinding.DisplayedListItemBinding;
import com.amsamu.mymedialists.util.EntryStatus;
import com.google.android.material.color.utilities.MaterialDynamicColors;

import java.io.File;

public class MediaListAdapter extends ListAdapter<Entry, MediaListAdapter.MediaListViewHolder> {

    public static final DiffUtil.ItemCallback<Entry> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Entry>() {
                @Override
                public boolean areItemsTheSame(@NonNull Entry oldItem, @NonNull Entry newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Entry oldItem, @NonNull Entry newItem) {
                    return oldItem.equals(newItem);
                }
            };


    public MediaListAdapter() {
        super(DIFF_CALLBACK);
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(Entry entry);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MediaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // parent = RecycleView
        DisplayedListItemBinding binding = DisplayedListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MediaListViewHolder(binding, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull MediaListViewHolder holder, int position) {
        Entry entry = getItem(position);
        holder.bind(entry);
    }

    public class MediaListViewHolder extends RecyclerView.ViewHolder{

        private DisplayedListItemBinding binding;
        private Context context;

        public MediaListViewHolder(@NonNull DisplayedListItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(Entry entry){
            Log.d("MediaListAdapter","bind: entry.name = " + entry.name);
            // Load entry name
            binding.listItemEntryName.setText(entry.name);
            // Load cover image
            if(entry.coverImage != null) {
                binding.listItemImage.setImageURI(null);
                binding.listItemImage.setImageURI(Uri.fromFile(new File(entry.coverImage)));
                binding.listItemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }else{
                binding.listItemImage.setImageURI(null);
                binding.listItemImage.setImageDrawable(context.getDrawable(R.drawable.baseline_description_24));
                binding.listItemImage.setScaleType(ImageView.ScaleType.CENTER);
            }
            // Load release year
            if(entry.releaseYear != null){
                binding.listItemEntryYear.setText("(" + entry.releaseYear + ")");
                binding.listItemEntryYear.setVisibility(View.VISIBLE);
            }
            // Load author
            if(entry.author != null && !entry.author.isEmpty()){
                CharSequence prefix = context.getString(R.string.author_prefix);
                binding.listItemAuthor.setText(prefix + " " + entry.author);
                binding.listItemAuthor.setVisibility(View.VISIBLE);
            }
            // Load status
            loadStatus(entry);

            // Load start date
            if(entry.startDate != null){
                binding.listItemStartDate.setText(formatDate(context, entry.startDate));
                binding.listItemStartDate.setVisibility(View.VISIBLE);
                binding.listItemDatesSeparator.setVisibility(View.VISIBLE);
            }
            // Load finish date
            if(entry.finishDate != null){
                binding.listItemFinishDate.setText(formatDate(context, entry.finishDate));
                binding.listItemFinishDate.setVisibility(View.VISIBLE);
            }


            binding.getRoot().setOnClickListener(v -> {
                onItemClickListener.onItemClick(entry);
            });
            binding.executePendingBindings();
        }

        private void loadStatus(Entry entry){
            binding.listItemStatus.setText(entry.status.getString(context));
            int color = 0;
            switch(entry.status){
                case ONGOING:
                    color = context.getColor(R.color.colorOngoing);
                    break;
                case COMPLETED:
                    color = context.getColor(R.color.colorCompleted);
                    break;
                case PLANNED:
                    color = context.getColor(R.color.colorPlanned);
                    break;
                case DROPPED:
                    color = context.getColor(R.color.colorDropped);
                    break;
                case ON_HOLD:
                    color = context.getColor(R.color.colorOnHold);
                    break;
            }
            binding.listItemStatus.setTextColor(color);
        }

    }

}
