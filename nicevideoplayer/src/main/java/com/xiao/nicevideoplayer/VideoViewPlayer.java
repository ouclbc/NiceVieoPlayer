package com.xiao.nicevideoplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class VideoViewPlayer extends AbstractMediaPlayer {



    private final ResizableVideoView mVideoView;

    private final Object mInitLock = new Object();

    private final VideoViewPlayer.MediaPlayerListenerHolder mInternalListenerAdapter;


    public VideoViewPlayer(ResizableVideoView mVideoView) {

        Object lock = this.mInitLock;
        synchronized(this.mInitLock) {
            this.mVideoView = mVideoView;
        }
        this.mInternalListenerAdapter = new VideoViewPlayer.MediaPlayerListenerHolder(this);;
        attachInternalListeners();
    }

    public VideoViewPlayer(Context context) {
        Object lock = this.mInitLock;
        synchronized(this.mInitLock) {
            this.mVideoView = new ResizableVideoView(context);
        }
        mInternalListenerAdapter = new VideoViewPlayer.MediaPlayerListenerHolder(this);
        attachInternalListeners();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void attachInternalListeners() {
        this.mVideoView.setOnPreparedListener(this.mInternalListenerAdapter);
        this.mVideoView.setOnCompletionListener(this.mInternalListenerAdapter);
        this.mVideoView.setOnErrorListener(this.mInternalListenerAdapter);
        this.mVideoView.setOnInfoListener(this.mInternalListenerAdapter);
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mVideoView.setVideoURI(uri);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        LogUtil.d("VideoViewPlayer->setDataSource");
        mVideoView.setVideoURI(uri,map);
    }

    @Override
    public void setDataSource(FileDescriptor fileDescriptor) throws IOException, IllegalArgumentException, IllegalStateException {

    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        Uri uri = Uri.parse(path);
        this.mVideoView.setVideoURI(uri);
    }

    @Override
    public String getDataSource() {
        return null;
    }

    @Override
    public void prepareAsync() throws IllegalStateException {

    }

    @Override
    public void start() throws IllegalStateException {
        LogUtil.d("videoview start");
        mVideoView.start();
    }

    @Override
    public void stop() throws IllegalStateException {
        mVideoView.stopPlayback();
    }

    @Override
    public void pause() throws IllegalStateException {
        mVideoView.pause();
    }

    @Override
    public void setScreenOnWhilePlaying(boolean b) {
        mVideoView.setKeepScreenOn(b);
    }

    @Override
    public int getVideoWidth() {
        return mVideoView.getWidth();
    }

    @Override
    public int getVideoHeight() {
        return mVideoView.getHeight();
    }

    @Override
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    @Override
    public void seekTo(long time) throws IllegalStateException {
        mVideoView.seekTo((int)time);
    }

    @Override
    public long getCurrentPosition() {
        return (long)mVideoView.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return (long)mVideoView.getDuration();
    }

    @Override
    public void release() {
        mVideoView.stopPlayback();
    }

    @Override
    public void reset() {

    }

    @Override
    public void setVolume(float v, float v1) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public int getAudioSessionId() {
        return mVideoView.getAudioSessionId();
    }

    @Override
    public MediaInfo getMediaInfo() {
        return null;
    }

    @Override
    public void setLogEnabled(boolean b) {

    }

    @Override
    public boolean isPlayable() {
        return false;
    }

    @Override
    public void setAudioStreamType(int i) {

    }

    @Override
    public void setKeepInBackground(boolean b) {

    }

    @Override
    public int getVideoSarNum() {
        return 0;
    }

    @Override
    public int getVideoSarDen() {
        return 0;
    }

    @Override
    public void setWakeMode(Context context, int i) {

    }

    @Override
    public void setLooping(boolean b) {

    }

    @Override
    public boolean isLooping() {
        return false;
    }

    @Override
    public ITrackInfo[] getTrackInfo() {
        return new ITrackInfo[0];
    }

    @Override
    public void setSurface(Surface surface) {

    }
    private class MediaPlayerListenerHolder implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener,MediaPlayer.OnInfoListener{
        public final WeakReference<VideoViewPlayer> mWeakMediaPlayer;

        public MediaPlayerListenerHolder(VideoViewPlayer mp) {
            this.mWeakMediaPlayer = new WeakReference(mp);
        }

        public void onPrepared(MediaPlayer mp) {
            VideoViewPlayer self = (VideoViewPlayer)this.mWeakMediaPlayer.get();
            LogUtil.d("VideoViewPlayer ——> onPrepared="+self);
            if (self != null) {
                VideoViewPlayer.this.notifyOnPrepared();
            }
        }

        public void onCompletion(MediaPlayer mp) {
            VideoViewPlayer self = (VideoViewPlayer)this.mWeakMediaPlayer.get();
            if (self != null) {
                VideoViewPlayer.this.notifyOnCompletion();
            }
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            VideoViewPlayer self = (VideoViewPlayer)this.mWeakMediaPlayer.get();
            return self != null && VideoViewPlayer.this.notifyOnError(what, extra);
        }

        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            VideoViewPlayer self = (VideoViewPlayer)this.mWeakMediaPlayer.get();
            return self != null && VideoViewPlayer.this.notifyOnInfo(what, extra);
        }

    }
}
