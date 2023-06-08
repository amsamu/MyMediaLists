package com.amsamu.mymedialists.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.amsamu.mymedialists.database.tables.Entry;
import com.amsamu.mymedialists.databinding.CarouselItemBinding;

import java.io.File;

public class CarouselAdapter extends ListAdapter<Entry, CarouselAdapter.CarouselViewHolder> {

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


    public CarouselAdapter() {
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
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // parent = RecycleView
        CarouselItemBinding binding = CarouselItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CarouselViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        Entry entry = getItem(position);
        holder.bind(entry);
    }

    public class CarouselViewHolder extends RecyclerView.ViewHolder{

        private CarouselItemBinding binding;

        public CarouselViewHolder(@NonNull CarouselItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Entry entry){
            Log.d("CarouselAdapter","bind: entry.name = " + entry.name);
            if(entry.coverImage != null) {
                binding.carouselImageView.setImageURI(Uri.fromFile(new File(entry.coverImage)));
                binding.carouselImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            binding.getRoot().setOnClickListener(v -> {
                onItemClickListener.onItemClick(entry);
            });

            binding.executePendingBindings();
        }

    }

}
