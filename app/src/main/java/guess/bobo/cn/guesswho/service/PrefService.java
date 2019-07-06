package guess.bobo.cn.guesswho.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.activity.MainMenuActivity;
import guess.bobo.cn.guesswho.utils.Constants;


/**
 * Created by Leon on 2018/6/3.
 * Functions: 长期保存参数的类即使用户推掉后台进程仍然保存
 */
public class PrefService {

    private Context context;
    public static final String GAME_MUSIC = "GAME_MUSIC";
    public static final String GAME_SOUND = "GAME_SOUND";
    public static final  String PREF_NAME = "ITCAST_PREF";
    public static final  String GAME_LANGUAGE = "GAME_LANGUAGE";

    private static boolean music = true;
    private static boolean sound = true;
    private static int language  = 0;

    public static final int[] LANGUAGE_OPTION = new int[]{
            R.string.options_language_chinese,
            R.string.options_language_english,
            R.string.options_language_trandition_chinese
    };

    public PrefService(Context context) {
        SharedPreferences sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        this.context = context;
        music = sPref.getBoolean(GAME_MUSIC, true);
        sound = sPref.getBoolean(GAME_SOUND,true);
        language = sPref.getInt(GAME_LANGUAGE,0);
    }

    /**
     *获取游戏中上次保存的语言
     */
    public int getLanguage(Context context){
        SharedPreferences sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sPref.getInt(GAME_LANGUAGE,0);
    }

    /**
     * 设置长久存储游戏语言
     */
    public void setLanguage(int gameLanguage,Context context){
        SharedPreferences sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed  = sPref.edit();
        ed.putInt(GAME_LANGUAGE,gameLanguage);
        ed.commit();
       // language = gameLanguage;
    }

    /**
     * 获取游戏音乐
     */
    public boolean isGameMusic(){
        SharedPreferences sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sPref.getBoolean(GAME_MUSIC,true);
       // return music;
    }

    /**
     * 设置游戏音乐
     */
    public void setGameMusic(boolean gameMusic,Context context){
        SharedPreferences sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed  = sPref.edit();
        ed.putBoolean(GAME_MUSIC,gameMusic);
        ed.commit();
        music = gameMusic;
    }


    /**
     * 获取游戏音效
     */
    public boolean isGameSound(){
        return sound;
    }

    /**
     * 设置游戏音效
     */
    public void setGameSound(boolean gameSound,Context context){
        SharedPreferences sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(GAME_SOUND,gameSound);
        ed.commit();
        sound = gameSound;
    }


    /**
     * 调用安卓国际化的方法
     * @param i 代表语言
     */
    public static void changeLocaleAccordingOptions(Context context, int i)
    {

//        Locale locale = null;
//        Resources resources = context.getResources();//获得res资源对象
//        Configuration config=resources.getConfiguration();//获得设置对象
//        DisplayMetrics dm=resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率像素等。
//        if (i == R.string.options_language_english) {
//            //使用英文，
//            locale = Locale.ENGLISH;
//        }
//        if (i == R.string.options_language_chinese) {
//            locale = Locale.CHINESE;
//        }
//        if (i == R.string.options_language_trandition_chinese) {
//            //locale = Locale.TRADITIONAL_CHINESE;
//            locale = Locale.KOREA;
//        }
//        resources.updateConfiguration(config,dm);



        Locale locale = null;
        if (i == R.string.options_language_english) {
            //使用英文，
            locale = Locale.ENGLISH;
        }
        if (i == R.string.options_language_chinese) {
            locale = Locale.CHINESE;
        }
        if (i == R.string.options_language_trandition_chinese) {
            //locale = Locale.TRADITIONAL_CHINESE;
            locale = Locale.KOREA;
        }

        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());

        Constants.ISUPDATE = true;
    }

}
