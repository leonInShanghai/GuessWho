package guess.bobo.cn.guesswho.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.utils.LogUtil;

/**
 * Created by Leon on 2018/6/2.
 * Functions: 音效服务
 */
public class SoundService {

    private Map<Integer,MediaPlayer> soundsCache = new HashMap<Integer,MediaPlayer>();
    private Context context;
    private PrefService pref;


    public SoundService(Context context) {
        this.context = context;
        pref = new PrefService(context);
    }

    public MediaPlayer getSoundFromCache(int soundResId) {
        MediaPlayer mp = soundsCache.get(soundResId);
        if (mp == null) {
            mp = MediaPlayer.create(context, soundResId);

            soundsCache.put(soundResId, mp);
        }
        return mp;
    }


    public void clearCacheMap(){
        soundsCache.clear();
    }


    public void play(int resId) {

        if (resId == R.raw.loop) {
            if (!pref.isGameMusic()) {
                return;
            }
        } else {
            if (!pref.isGameSound()) {
                return;
            }
        }

        MediaPlayer mp = getSoundFromCache(resId);

//        if (mp.isPlaying()) {
//            mp.reset();
//            try {
//                mp.prepare();
//            } catch (IllegalStateException e) {
//                LogUtil.e(e.toString());
//            } catch (IOException e) {
//                LogUtil.e(e.toString());
//            }
//        }

        mp.start();
        if (R.raw.loop == resId) {
            mp.setLooping(true);
        }else {
            mp.setLooping(false);
        }
    }

    public void stop(int resId) {

        MediaPlayer mp = getSoundFromCache(resId);
        if (mp == null) {
            return;
        }

        mp.pause();

        //解决先关闭声音玩两把在打开声音没有背景音乐的问题
        clearCacheMap();
        mp.release();
    }

    public void release() {
        for (Map.Entry<Integer, MediaPlayer> entry : soundsCache.entrySet()) {
            MediaPlayer mediaPlayer = entry.getValue();
            soundsCache.remove(entry.getKey());
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            }
        }
    }


}




