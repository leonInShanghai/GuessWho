package guess.bobo.cn.guesswho.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.utils.Constants;



/**
 * Created by Leon on 2018/6/3.
 * Functions:
 */
public class MediaService extends Service {

    SoundService soundService;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (soundService == null) {
            soundService = new SoundService(this);
        }
        boolean pause;
        int resId;
        if (intent == null){
            pause = true;
            resId = R.raw.loop;
        }else {
            pause = intent.getBooleanExtra(Constants.KEY_PAUSE_MUSIC, false);
            resId = intent.getIntExtra(Constants.KEY_SOUND_RES_ID_IN_INTENT, 0);
        }

        if (pause) {
            soundService.stop(resId);
        } else {
            if (resId != 0) {
                soundService.play(resId);
            }
        }
    }

    public static void play(Context context, int resId) {
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra(Constants.KEY_SOUND_RES_ID_IN_INTENT, resId);
        intent.putExtra(Constants.KEY_PAUSE_MUSIC, false);
        context.startService(intent);
    }

    public static void pause(Context context, int resId) {
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra(Constants.KEY_SOUND_RES_ID_IN_INTENT, resId);
        intent.putExtra(Constants.KEY_PAUSE_MUSIC, true);
        context.startService(intent);
    }

    @Override
    public void onDestroy() {
        if (soundService != null) {
            soundService.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }





//    private static SoundService sounds;
//   // private SoundService sounds;
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
////        if (sounds == null){
////            sounds = new SoundService(this);
////        }
////        int resId = intent.getIntExtra("RESID",-1);
////        if (resId != -1){// != -1 表示传递了资源
////            sounds.play(resId);
////        }
//
//        super.onStart(intent, startId);
//        if (sounds == null) {
//            sounds = new SoundService(this);
//        }
//
//        boolean pause = intent.getBooleanExtra(Constants.KEY_PAUSE_MUSIC, false);
//        int resId = intent.getIntExtra(Constants.KEY_SOUND_RES_ID_IN_INTENT, 0);
//        if (pause) {
//            sounds.stop(resId);//这里的resId暂时没有用到
//        } else {
//            if (resId != 0) {
//                sounds.play(resId);
//            }
//        }
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    public static void play(Context context, int resId) {
//        Intent intent = new Intent(context, MediaService.class);
//        intent.putExtra(Constants.KEY_SOUND_RES_ID_IN_INTENT, resId);
//        intent.putExtra(Constants.KEY_PAUSE_MUSIC, false);
//        context.startService(intent);
//
////       if (sounds == null){
////            sounds = new SoundService(context);
////        }
////        if (resId != -1){// != -1 表示传递了资源
////            sounds.play(resId);
////        }
//
//    }
//
//    public static void pause(Context context, int resId) {
//        Intent intent = new Intent(context, MediaService.class);
//        intent.putExtra(Constants.KEY_SOUND_RES_ID_IN_INTENT, resId);
//        intent.putExtra(Constants.KEY_PAUSE_MUSIC, true);
//        context.startService(intent);
//    }
//
////    public static void play(Context context,int resId){
////        Intent intent = new Intent(context, MediaService.class);
////        intent.putExtra("RESID", resId);
////        context.startService(intent);
////    }
//
//
//    @Override
//    public void onDestroy() {
//        if (sounds != null) {
//            sounds.release();
//        }
//        super.onDestroy();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }

}
