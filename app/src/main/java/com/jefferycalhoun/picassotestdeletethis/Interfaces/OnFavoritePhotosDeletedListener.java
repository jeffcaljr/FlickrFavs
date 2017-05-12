package com.jefferycalhoun.picassotestdeletethis.Interfaces;

import com.jefferycalhoun.picassotestdeletethis.Models.Photo;

/**
 * Created by Jeff on 5/11/17.
 */

/***
 * Listeners pulled out of their fragments to make finding them easier
 */

public interface OnFavoritePhotosDeletedListener {
    void onAllFavoritePhotosDeleted();
    void onFavoritePhotoDeleted(Photo photo);
}
