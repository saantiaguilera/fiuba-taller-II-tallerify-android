package com.u.tallerify.view.base.music_player.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding.view.RxView;
import com.u.tallerify.R;
import com.u.tallerify.utils.CurrentPlay;
import com.u.tallerify.utils.FrescoImageController;
import com.u.tallerify.view.abstracts.FixedSimpleListView;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Ideally instead of having this mega class, we should have components like:
 * - VolumeView
 * - PlayControlsView
 * - etc.
 *
 * No time.
 *
 * Created by saguilera on 3/14/17.
 */
public class MusicPlayerExpandedView extends ScrollView {

    private @NonNull SimpleDraweeView expandImage;
    private @NonNull SeekBar expandTrackBar;
    private @NonNull TextView expandTrackTime;
    private @NonNull TextView expandTrackTimeLeft;
    private @NonNull TextView expandTrackName;
    private @NonNull TextView expandTrackArtist;
    private @NonNull ImageView expandPreviousTrack;
    private @NonNull ImageView expandNextTrack;
    private @NonNull ImageView expandPlayPause;
    private @NonNull SeekBar expandVolumeBar;
    private @NonNull ImageView expandRepeat;
    private @NonNull ImageView expandShuffle;
    private @NonNull ImageView expandFavorite;
    private @NonNull RatingBar expandRatingBar;
    private @NonNull FixedSimpleListView expandPlaylistContainer;

    boolean favorited;

    public MusicPlayerExpandedView(final Context context) {
        this(context, null);
    }

    public MusicPlayerExpandedView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicPlayerExpandedView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.view_music_player_expanded, this);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        setFillViewport(true);

        expandImage = (SimpleDraweeView) findViewById(R.id.view_music_player_expanded_image);
        expandNextTrack = (ImageView) findViewById(R.id.view_music_player_expanded_next_track);
        expandPlayPause = (ImageView) findViewById(R.id.view_music_player_expanded_play_pause);
        expandPreviousTrack = (ImageView) findViewById(R.id.view_music_player_expanded_previous_track);
        expandTrackArtist = (TextView) findViewById(R.id.view_music_player_expanded_track_artist);
        expandTrackName = (TextView) findViewById(R.id.view_music_player_expanded_track_name);
        expandTrackBar = (SeekBar) findViewById(R.id.view_music_player_expanded_track_progress);
        expandTrackTime = (TextView) findViewById(R.id.view_music_player_expanded_track_time);
        expandTrackTimeLeft = (TextView) findViewById(R.id.view_music_player_expanded_track_time_left);
        expandVolumeBar = (SeekBar) findViewById(R.id.view_music_player_expanded_volume);
        expandShuffle = (ImageView) findViewById(R.id.view_music_player_expanded_shuffle);
        expandRepeat = (ImageView) findViewById(R.id.view_music_player_expanded_repeat);
        expandFavorite = (ImageView) findViewById(R.id.view_music_player_expanded_favorite);
        expandRatingBar = (RatingBar) findViewById(R.id.view_music_player_expanded_rating_bar);
        expandPlaylistContainer = (FixedSimpleListView) findViewById(R.id.view_music_player_expanded_playlist);

        // We dont let the user seek positions because Media Player has the seekbar broken
        // in android sdk. Check issues in google repository for information.
        // Still, this feature was developed and did work with the exception of the android bugs
        expandTrackBar.setEnabled(false);

        tintDrawable(expandTrackBar.getProgressDrawable());
        tintDrawable(expandVolumeBar.getProgressDrawable());
        tintDrawable(expandTrackBar.getThumb());
        tintDrawable(expandVolumeBar.getThumb());

        expandTrackName.setSelected(true);
    }

    public @NonNull Observable<Void> observePlayStateClicks() {
        return RxView.clicks(expandPlayPause);
    }

    public @NonNull Observable<Void> observeNextSongClicks() {
        return RxView.clicks(expandNextTrack);
    }

    public @NonNull Observable<Integer> observePlaylistSkipClicks() {
        return expandPlaylistContainer.observePositionClicks();
    }

    public @NonNull Observable<Boolean> observeFavoriteClicks() {
        return RxView.clicks(expandFavorite)
            .map(new Func1<Void, Boolean>() {
                @Override
                public Boolean call(final Void aVoid) {
                    favorited = !favorited;
                    return favorited;
                }
            });
    }

    public @NonNull Observable<Integer> observeRatingSeeks() {
        return com.u.tallerify.view.RxView.dispatchSeeks(expandRatingBar);
    }

    public @NonNull Observable<Void> observePreviousSongClicks() {
        return RxView.clicks(expandPreviousTrack);
    }

    public @NonNull Observable<Integer> observeVolumeSeeks() {
        return com.u.tallerify.view.RxView.dispatchSeeks(expandVolumeBar);
    }

    public @NonNull Observable<Integer> observeSongSeeks() {
        return com.u.tallerify.view.RxView.dispatchSeeks(expandTrackBar);
    }

    public @NonNull Observable<Void> observeShuffleClicks() {
        return RxView.clicks(expandShuffle);
    }

    public @NonNull Observable<Void> observeRepeatClicks() {
        return RxView.clicks(expandRepeat);
    }

    public void setImageUrl(@NonNull String url) {
        FrescoImageController.create()
            .load(url)
            .into(expandImage);
    }

    public void setQueue(@NonNull List<String> names, @NonNull List<String> urls) {
        expandPlaylistContainer.setData(names, urls);
    }

    public void setPlaying() {
        expandPlayPause.setImageResource(R.drawable.ic_play_arrow_black_36dp);
    }

    public void setPaused() {
        expandPlayPause.setImageResource(R.drawable.ic_pause_black_36dp);
    }

    public void setArtistName(@NonNull String name) {
        expandTrackArtist.setText(name);
    }

    public void setSongName(@NonNull String name) {
        expandTrackName.setText(name);
    }

    public void setTrackBarMax(int max) {
        expandTrackBar.setMax(max);
    }

    public void setTrackBarProgress(int progress) {
        expandTrackBar.setProgress(progress);
    }

    @SuppressLint("DefaultLocale")
    public void setTime(int time, int duration) {
        expandTrackTime.setText(String.format("%02d", time / 60) +
            ":" +
            String.format("%02d", time % 60));
        expandTrackTimeLeft.setText(
            String.format("%02d", (duration - time) / 60) +
                ":" +
                String.format("%02d", (duration - time) % 60));
    }

    public void setVolume(int volume) {
        expandVolumeBar.setProgress(volume);
    }

    public void setShuffleEnabled(boolean enabled) {
        expandShuffle.getDrawable().setColorFilter(enabled ?
            new PorterDuffColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null),
                PorterDuff.Mode.SRC_IN) :
            null);
    }

    public void setRepeatMode(CurrentPlay.RepeatMode repeatMode) {
        switch (repeatMode) {
            case NONE:
                expandRepeat.setImageResource(R.drawable.ic_repeat_black_36dp);
                expandRepeat.getDrawable().setColorFilter(null);
                break;
            case ALL:
                expandRepeat.setImageResource(R.drawable.ic_repeat_black_36dp);
                tintDrawable(expandRepeat.getDrawable());
        }
    }

    public void setRating(int rating) {
        expandRatingBar.setVisibility(View.VISIBLE);
        expandRatingBar.setRating(rating);
    }

    public void setFavorite(boolean favved) {
        favorited = favved;
        expandFavorite.setVisibility(View.VISIBLE);
        expandFavorite.getDrawable().setColorFilter(favved ?
            new PorterDuffColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null),
                PorterDuff.Mode.SRC_IN) :
            new PorterDuffColorFilter(ResourcesCompat.getColor(getResources(), R.color.white, null),
                PorterDuff.Mode.SRC_IN) );
    }

    private void tintDrawable(@NonNull Drawable drawable) {
        drawable.setColorFilter(new PorterDuffColorFilter(
            ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null),
            PorterDuff.Mode.SRC_IN));
    }

}
