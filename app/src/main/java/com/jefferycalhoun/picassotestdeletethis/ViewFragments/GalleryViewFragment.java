package com.jefferycalhoun.picassotestdeletethis.ViewFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jefferycalhoun.picassotestdeletethis.Interfaces.OnPhotoSearchRequestListener;
import com.jefferycalhoun.picassotestdeletethis.Interfaces.OnPhotoSelectedListener;
import com.jefferycalhoun.picassotestdeletethis.Models.Photo;
import com.jefferycalhoun.picassotestdeletethis.R;
import com.jefferycalhoun.picassotestdeletethis.RecyclerViewComponents.PhotoAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Jeff on 5/11/17.
 */

public class GalleryViewFragment extends Fragment implements OnPhotoSelectedListener {

    public static final String TAG = "GalleryFragment";

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_list_background)
    LinearLayout emptyListBackground;
    Unbinder unbinder;
    @BindView(R.id.search_frag)
    FrameLayout searchFrag;
    @BindView(R.id.favorites_options_frag)
    FrameLayout favoritesOptionsFrag;
    @BindView(R.id.swipe_refresher)
    SwipeRefreshLayout swipeRefresher;

    private String searchQuery;
    private String urlString;

    private ArrayList<Photo> mPhotos;

    private PhotoAdapter mAdapter;

    private OnPhotoSearchRequestListener mListener;
    private OnPhotoSaveListener mPhotoSaveListener;

    public interface OnPhotoSaveListener {
        void onPhotoSaveInitiated(Photo photo);
    }

    /**
     * Fragment Lifecycle Methods
     */


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_gallery_view, container, false);

        unbinder = ButterKnife.bind(this, view);

        favoritesOptionsFrag.setVisibility(View.INVISIBLE);
        searchFrag.setVisibility(View.VISIBLE);

        mPhotos = new ArrayList<>();

        mAdapter = new PhotoAdapter(getActivity(), mPhotos, this);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(mAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                query.replace(" ", ",").toLowerCase();
//                urlString = getString(R.string.flickr_url, query);
                mListener.onPhotoSearchRequest(searchQuery);
                searchView.setQuery(searchQuery, false);
                searchView.onActionViewCollapsed();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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

        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListener.onPhotoSearchRequest(urlString);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity(), "Loads 50 photos.\nPull to refresh\nTap to save", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mListener = null;
        mPhotoSaveListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnPhotoSearchRequestListener) context;
            mPhotoSaveListener = (OnPhotoSaveListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class Methods
     */


    public void setPhotos(ArrayList<Photo> photos) {

        if(swipeRefresher.isRefreshing()){
            swipeRefresher.setRefreshing(false);
        }

        searchView.setQuery(searchQuery, false);

        if (photos != null) {
            mPhotos = photos;
        } else {
            mPhotos = new ArrayList<>();
        }
        mAdapter.setPhotoDataSet(mPhotos);
        mAdapter.notifyDataSetChanged();

    }

    /**
     * OnPhotoSelectedListener Methods
     */

    @Override
    public void onPhotoSelected(Photo photo) {
        mPhotoSaveListener.onPhotoSaveInitiated(photo);
    }
}
