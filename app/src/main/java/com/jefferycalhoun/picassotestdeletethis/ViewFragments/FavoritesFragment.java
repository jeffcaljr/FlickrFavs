package com.jefferycalhoun.picassotestdeletethis.ViewFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jefferycalhoun.picassotestdeletethis.Activities.MainActivity;
import com.jefferycalhoun.picassotestdeletethis.Database.PhotoPersistence;
import com.jefferycalhoun.picassotestdeletethis.Models.Photo;
import com.jefferycalhoun.picassotestdeletethis.Interfaces.OnFavoritePhotosDeletedListener;
import com.jefferycalhoun.picassotestdeletethis.Interfaces.OnPhotoSelectedListener;
import com.jefferycalhoun.picassotestdeletethis.RecyclerViewComponents.PhotoAdapter;
import com.jefferycalhoun.picassotestdeletethis.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Jeff on 5/11/17.
 */

public class FavoritesFragment extends Fragment implements OnPhotoSelectedListener {

    public static final String TAG = "FavoritesFrag";

    @BindView(R.id.search_frag)
    FrameLayout searchFrag;
    @BindView(R.id.clear_favorites_button)
    Button clearFavoritesButton;
    @BindView(R.id.favorites_options_frag)
    FrameLayout favoritesOptionsFrag;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_list_background)
    LinearLayout emptyListBackground;
    Unbinder unbinder;

    private ArrayList<Photo> mFavoritePhotos;

    private PhotoAdapter mAdapter;

    private OnFavoritePhotosDeletedListener mListener;


    /**
     * Activity lifecycle methods
     */


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_gallery_view, container, false);

        unbinder = ButterKnife.bind(this, view);

        searchFrag.setVisibility(View.INVISIBLE);
        favoritesOptionsFrag.setVisibility(View.VISIBLE);

        mFavoritePhotos = PhotoPersistence.sharedInstance(getActivity()).getFavoritePhotos();

        mAdapter = new PhotoAdapter(getActivity(), mFavoritePhotos, this);

        //load photos

        mFavoritePhotos = ((MainActivity) getActivity()).loadPhotos();


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(mAdapter);

        if(mFavoritePhotos == null || mFavoritePhotos.size() == 0){
            emptyListBackground.setVisibility(View.VISIBLE);
        }

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getItemCount() == 0) {
                    emptyListBackground.setVisibility(View.VISIBLE);
                } else {
                    emptyListBackground.setVisibility(View.INVISIBLE);
                }
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity(), "Tap to delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (OnFavoritePhotosDeletedListener) context;
        }
        catch(ClassCastException e){
            e.printStackTrace();
        }

    }


    @OnClick(R.id.clear_favorites_button)
    public void onViewClicked() {
        mListener.onAllFavoritePhotosDeleted();
    }

    /**
     * Class Methods
     */


    public void reloadFavorites(ArrayList<Photo> favoritePhotos){
        if(favoritePhotos == null){
            mFavoritePhotos = new ArrayList<>();
        }
        else{
            mFavoritePhotos = favoritePhotos;
        }
        mAdapter.setPhotoDataSet(mFavoritePhotos);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * OnPhotoSelectedListener Methods
     */
    @Override
    public void onPhotoSelected(Photo photo) {
        mListener.onFavoritePhotoDeleted(photo);
    }
}
