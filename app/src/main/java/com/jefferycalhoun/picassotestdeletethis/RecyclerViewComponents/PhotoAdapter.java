package com.jefferycalhoun.picassotestdeletethis.RecyclerViewComponents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jefferycalhoun.picassotestdeletethis.Interfaces.OnPhotoSelectedListener;
import com.jefferycalhoun.picassotestdeletethis.Models.Photo;
import com.jefferycalhoun.picassotestdeletethis.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Jeff on 5/11/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    /**
     * Probably didnt need to use weak references; just wanted to play it safe
     */

    private WeakReference<Context> mContextReference;
    private WeakReference<OnPhotoSelectedListener> mListener;
    private ArrayList<Photo> mPhotos;

    public PhotoAdapter(Context context, ArrayList<Photo> photos, OnPhotoSelectedListener listener) {
        mContextReference = new WeakReference<>(context);
        mListener = new WeakReference<>(listener);
        mPhotos = photos;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContextReference.get()).inflate(R.layout.item_image, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        holder.bindView(mPhotos.get(position));

    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void setPhotoDataSet(ArrayList<Photo> photos){
        if(photos != null){
            mPhotos = photos;
        }
        else{
            mPhotos = new ArrayList<>();
        }
    }


    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private Photo photo;

        public PhotoHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);
        }

        public void bindView(final Photo photo) {
            this.photo = photo;
            Picasso.with(mContextReference.get())
                    .load(photo.getUrlString())
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_error_loading)
                    .into(imageView);
        }

        @Override
        public void onClick(View view) {
            mListener.get().onPhotoSelected(photo);

        }
    }
}
