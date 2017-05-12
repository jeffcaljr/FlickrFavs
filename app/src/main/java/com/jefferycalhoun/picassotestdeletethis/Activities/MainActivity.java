package com.jefferycalhoun.picassotestdeletethis.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jefferycalhoun.picassotestdeletethis.Database.PhotoPersistence;
import com.jefferycalhoun.picassotestdeletethis.ViewFragments.FavoritesFragment;
import com.jefferycalhoun.picassotestdeletethis.Interfaces.OnFavoritePhotosDeletedListener;
import com.jefferycalhoun.picassotestdeletethis.ViewFragments.GalleryViewFragment;
import com.jefferycalhoun.picassotestdeletethis.Interfaces.OnPhotoSearchRequestListener;
import com.jefferycalhoun.picassotestdeletethis.Models.Photo;
import com.jefferycalhoun.picassotestdeletethis.R;
import com.jefferycalhoun.picassotestdeletethis.Singletons.VolleyRequestQueueService;
import com.jefferycalhoun.picassotestdeletethis.ViewFragments.LoadingFragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnPhotoSearchRequestListener, OnFavoritePhotosDeletedListener, GalleryViewFragment.OnPhotoSaveListener {

    private static final String CURRENT_PAGE = "CURRENT_PAGE";
    private static final String CURRENT_QUERY = "CURRENT_QUERY";
    private static final String CURRENT_PHOTOS = "CURRENT_PHOTOS";

//    private ArrayList<Photo> currentPhotos;


    private String searchQuery = "dogs";
    private String urlString;

    private FragmentManager mManager;

    private GalleryViewFragment mGalleryViewFragment;
    private FavoritesFragment mFavoritesFragment;

    private BottomNavigationView navigation;

    private AlertDialog confirmSaveDialog;
    private AlertDialog confirmDeleteDialog;
    private LoadingFragment loadingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mManager = getSupportFragmentManager();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //retain the page the user was viewing, and their most recent search query, if the activity is destroyed and recreated
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(CURRENT_PAGE)){
                navigation.setSelectedItemId(savedInstanceState.getInt(CURRENT_PAGE));
            }
            if(savedInstanceState.containsKey(CURRENT_QUERY)){
                searchQuery = savedInstanceState.getString(CURRENT_QUERY);
            }
//            if(savedInstanceState.containsKey(CURRENT_PHOTOS)){
//                currentPhotos = (ArrayList<Photo>) savedInstanceState.getSerializable(CURRENT_PHOTOS);
//            }
        }


        //if the user was not viewing the favorites page last; show the discover page
        //always shows discover page first, unless user was viewing favorites, and the activity was destroyed and recreated
        if(navigation.getSelectedItemId() != R.id.navigation_favorites){
            mGalleryViewFragment = (GalleryViewFragment) mManager.findFragmentByTag(GalleryViewFragment.TAG);

            if (mGalleryViewFragment == null) {
                mGalleryViewFragment = new GalleryViewFragment();

                mManager.beginTransaction()
                        .replace(R.id.container, mGalleryViewFragment, GalleryViewFragment.TAG)
                        .commit();

            }
        }


        loadingFragment = new LoadingFragment(this, "Loading Images");




    }

    @Override
    protected void onStart() {
        super.onStart();

        //when view is loaded, if the user is viewing the gallery,
        if(navigation.getSelectedItemId() != R.id.navigation_favorites){

            urlString = getString(R.string.flickr_url, searchQuery);

            onPhotoSearchRequest(urlString);
//            if (mGalleryViewFragment != null) {

//                if(currentPhotos == null) {
//                    //if there were no photos already loaded into the discover view, load new ones
//                    onPhotoSearchRequest(urlString);
//                }
//                else{
//                    //otherwise, load those previously loaded photos into the view
//                    if ( mGalleryViewFragment.isVisible()) {
//                        mGalleryViewFragment.setPhotos(currentPhotos);
//                    }
//                }
//            }

        }


    }

    @Override
    public void onBackPressed() {

        //if the user was viewing favorites, go back to discover page. Else, perform default back operation
        if(navigation.getSelectedItemId() != R.id.navigation_home){
            navigation.setSelectedItemId(R.id.navigation_home);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_PAGE, navigation.getSelectedItemId());
        outState.putString(CURRENT_QUERY, searchQuery);
//        if(currentPhotos != null){
//            outState.putSerializable(CURRENT_PHOTOS, currentPhotos);
//        }

    }

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                //navigate user to discover page
                //also send discover page any previously loaded photos. Load new photos if there were none previously loaded
                case R.id.navigation_home:

                    mGalleryViewFragment = (GalleryViewFragment) mManager.findFragmentByTag(GalleryViewFragment.TAG);

                    if (mGalleryViewFragment == null) {
                        mGalleryViewFragment = new GalleryViewFragment();

                        mManager.beginTransaction()
                                .replace(R.id.container, mGalleryViewFragment, GalleryViewFragment.TAG)
                                .commit();

                        onPhotoSearchRequest(searchQuery);

//                        if(currentPhotos != null){
//                            if (mGalleryViewFragment != null && mGalleryViewFragment.isVisible()) {
//                                mGalleryViewFragment.setPhotos(currentPhotos);
//                            }
//                        }
//                        else{
//                            onPhotoSearchRequest(searchQuery);
//                        }

                    }

                    return true;

                //navigate user to view their saved photos
                case R.id.navigation_favorites:

                    mFavoritesFragment = (FavoritesFragment) mManager.findFragmentByTag(FavoritesFragment.TAG);

                    if(mFavoritesFragment == null){
                        mFavoritesFragment = new FavoritesFragment();

                        mManager.beginTransaction()
                                .replace(R.id.container, mFavoritesFragment, FavoritesFragment.TAG)
                                .commit();
                    }

                    return true;
            }
            return false;
        }

    };

    /**
     * Class Methods
     */

    /**
     * Loads all photose user has saved as favorites
     * @return list of all user's favorited photos
     */
    public ArrayList<Photo> loadPhotos(){
        ArrayList<Photo> favoritePhotos = PhotoPersistence.sharedInstance(MainActivity.this).getFavoritePhotos();
        return favoritePhotos;
    }


    /**
     * OnPhotoSearchRequestListener Methods
     */

    @Override
    public void onPhotoSearchRequest(String searchQuery) {

        //searches flickr for photos matching query user selected (or last search query, if the user tries to search for nil
        loadingFragment.show();

        if(searchQuery != null && !searchQuery.equals("")){
            this.searchQuery = searchQuery;
        }

        String urlString = getString(R.string.flickr_url, this.searchQuery);

        JsonObjectRequest request = new JsonObjectRequest(Method.GET, urlString, null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject photosJSON = response.getJSONObject("photos");
                            JSONArray photosJSONArray = photosJSON.getJSONArray("photo");

                            ArrayList<Photo> photos = new ArrayList<>();


                            photos = new ArrayList<>();
                            for (int i = 0; i < photosJSONArray.length(); i++) {
                                photos.add(new Photo((photosJSONArray.getJSONObject(i))));
                            }

                            //when photos are loaded, temporarily store them to prevent reloads of the same data

//                            currentPhotos = photos;

                            //pass loaded photos to gallery fragment for display
                            if (mGalleryViewFragment != null && mGalleryViewFragment.isVisible()) {
                                mGalleryViewFragment.setPhotos(photos);
                            }
                            loadingFragment.dismissWithDelay(500);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingFragment.dismissWithDelay(500);
                        }
                    }
                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display error to user if no photos found that match their search query
                //also clear any photos displayed in gallery from last query
                loadingFragment.dismiss();
                Toast.makeText(MainActivity.this, "unable to find photos", Toast.LENGTH_SHORT).show();
                if (mGalleryViewFragment != null && mGalleryViewFragment.isVisible()) {
                    mGalleryViewFragment.setPhotos(null);
                }
            }
        });

        VolleyRequestQueueService.getInstance(this).addToRequestQueue(request);

    }

    /**
     * OnPhotoSaveListener methods
     */
    @Override
    public void onPhotoSaveInitiated(final Photo photo) {

        //when user clicks a photo to save it, confirm their wish, and save the photo locally if they confirm

        confirmSaveDialog = new AlertDialog.Builder(this)
                .setTitle("Save photo to favorites")
                .setMessage("Are you sure you want to save this photo to your favorites?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PhotoPersistence.sharedInstance(MainActivity.this).savePhoto(photo);
                    }
                })
                .create();

        confirmSaveDialog.show();


    }

    /**
     * OnFavoritePhotosDeletedListener methods
     */

    @Override
    public void onAllFavoritePhotosDeleted() {
        //when user clicks button to delete all their saved photos, confirm their wish, and delete all if they confirm

        confirmDeleteDialog = new AlertDialog.Builder(this)
                .setTitle("Delete all photos from favorites")
                .setMessage("Are you sure you wish to delete ALL photo from your favorites?")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PhotoPersistence.sharedInstance(MainActivity.this).deletaAllPhotos();
                        if(mFavoritesFragment != null){
                            mFavoritesFragment.reloadFavorites(null);
                        }

                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })
                .create();

        confirmDeleteDialog.show();



    }

    @Override
    public void onFavoritePhotoDeleted(final Photo photo) {
        //when user clicks a saved photo to delete it, confirm their wish, and delete if they confirm
        confirmDeleteDialog = new AlertDialog.Builder(this)
                .setTitle("Delete photo from favorites")
                .setMessage("Are you sure you wish to delete this photo from your favorites?")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PhotoPersistence.sharedInstance(MainActivity.this).deletePhoto(photo);
                        if(mFavoritesFragment != null){
                            ArrayList<Photo> newFavorites = PhotoPersistence.sharedInstance(MainActivity.this).getFavoritePhotos();
                            mFavoritesFragment.reloadFavorites(newFavorites);
                            dialogInterface.dismiss();
                        }

                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })
                .create();

        confirmDeleteDialog.show();

    }
}
