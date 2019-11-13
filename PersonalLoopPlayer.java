package com.mattiagarreffa.rain;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;

class PersonalLoopPlayer {

    private Context context;
    private Handler handler = new Handler();
    private Handler handler1 = new Handler();

    private MediaPlayer mp = new MediaPlayer();
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private int loop = 0;
    private Boolean mpIsReleased = false;
    private Boolean mediaPlayerIsReleased = false;

    PersonalLoopPlayer(Context context) {
        this.context = context;
    }

    void play(final int resourceID, final SeekBar seekbar) {
        mp = MediaPlayer.create(context, resourceID);
        mpIsReleased = false;
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        loop += 1;
        System.out.println("Empieza el loop " + loop);

        mp.start();
        assert audioManager != null;
        float log = (float) (Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - seekbar.getProgress()) / Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
        mp.setVolume(1 - log, 1 - log);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float log1 = (float) (Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - i) / Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
                mp.setVolume(1 - log1, 1 - log1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playTwo(resourceID, seekbar);
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mp.release();
                        mpIsReleased = true;
                    }
                }, 100);
            }
        }, mp.getDuration() - 150);

    }

    private void playTwo(final int resourceID, final SeekBar seekBar) {
        mediaPlayer = MediaPlayer.create(context, resourceID);
        mediaPlayerIsReleased = false;
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        loop += 1;
        System.out.println("Empieza el loop " + loop);

        mediaPlayer.start();
        assert audioManager != null;
        float log = (float) (Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - seekBar.getProgress()) / Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
        mediaPlayer.setVolume(1 - log, 1 - log);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float log1 = (float) (Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - i) / Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
                mediaPlayer.setVolume(1 - log1, 1 - log1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                play(resourceID, seekBar);
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.release();
                        mediaPlayerIsReleased = true;
                    }
                }, 100);
            }
        }, mediaPlayer.getDuration() - 150);

    }

    void stop() {
        if (mp != null) {
            if (!mpIsReleased) {
                if (mp.isPlaying()) {
                    mp.stop();
                    handler.removeCallbacksAndMessages(null);
                    handler1.removeCallbacksAndMessages(null);
                }
            }
        }
        if (mediaPlayer != null) {
            if (!mediaPlayerIsReleased) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    handler.removeCallbacksAndMessages(null);
                    handler1.removeCallbacksAndMessages(null);
                }
            }
        }
    }

}
