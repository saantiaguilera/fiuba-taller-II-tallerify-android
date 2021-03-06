package com.u.tallerify.model.entity;

import android.support.annotation.Nullable;
import java.util.List;

/**
 * Created by saguilera on 3/19/17.
 */

public interface Playable {

    long id();
    @Nullable List<Song> asPlaylist();
    @Nullable List<String> pictures();
    @Nullable String fullName();

}
