package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;


import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.service.MediaService;
import guess.bobo.cn.guesswho.service.PrefService;
import guess.bobo.cn.guesswho.service.SoundService;

/**
 * Created by Leon on 2018/6/3.
 * Functions:
 */
public class OptionsActivity extends Activity implements View.OnClickListener,View.OnTouchListener,GestureDetector.OnGestureListener{


    private ToggleButton optionsMusic;
    private ToggleButton optionsSound;
    private Button optionsMainmenu;
    private PrefService pref;
    //方法二选择语言的变量
    private GestureDetector gestureDetector;
    private TextView languageText;
    //方法一选择语言的变量
    private boolean isLanguageTouched;
    private TextView language_title;
    private TextView options_language;
    private TextView options_music;
    private TextView text_sound;
    private int oldX;
    //这里要用static不然一离开这个activity i就变成0了
   // private static int i;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-06-03 19:58:48 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        optionsMusic = (ToggleButton)findViewById( R.id.options_music );
        optionsSound = (ToggleButton)findViewById( R.id.options_sound );
        optionsMainmenu = (Button)findViewById( R.id.options_mainmenu );
        languageText = (TextView)findViewById(R.id.options_languages);
        language_title = (TextView)findViewById(R.id.language_title);
        options_language = (TextView)findViewById(R.id.options_language);
        options_music = (TextView)findViewById(R.id.text_music);
        text_sound = (TextView)findViewById(R.id.text_sound);
        //创建保存开关状态参数的类-接下来校验
        pref = new PrefService(this);

        //ToggleButton Checked状态的校验
        optionsMusic.setChecked(pref.isGameMusic());
        optionsSound.setChecked(pref.isGameSound());
        optionsMusic.setOnClickListener( this );
        optionsSound.setOnClickListener( this );
        optionsMainmenu.setOnClickListener( this );

        //方法一：
//        languageText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                isLanguageTouched = true;
//
//                //注意这里返回false返回true就代表把这个事件吃掉了
//                return false;
//            }
//        });

        //方法二
        gestureDetector = new GestureDetector(this);
        languageText.setOnTouchListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        findViews();
    }

    /**
     *用户点击事件的处理
     */
    @Override
    public void onClick(View v) {

        //只要是用户点击了按钮就播放按钮的声音 不用每次都设置
        if (v instanceof Button){
            MediaService.play(this,R.raw.button);
        }

        if ( v == optionsMusic ) {
            // 用户点击了游戏音乐
            pref.setGameMusic(((ToggleButton) v).isChecked(),this);

        } else if ( v == optionsSound ) {
            // 用户点击了游戏声音（背景音乐）
            pref.setGameSound(((ToggleButton) v).isChecked(),this);

        } else if ( v == optionsMainmenu ) {
            // 用户点击了菜单（返回菜单）
            finish();
        }
    }
//方法一
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        if (isLanguageTouched){
//            if (event.getAction() == MotionEvent.ACTION_DOWN){
//                oldX = (int) event.getX();
//            }else if (event.getAction() == MotionEvent.ACTION_UP){
//                if (oldX > event.getX()){
//                    handleLanguageOption(true);
//                }
//                if (oldX < event.getX()){
//                    handleLanguageOption(false);
//                }
//                isLanguageTouched = false;
//            }
//        }
//
//        return false;
//    }

    private void handleLanguageOption(boolean isLeft) {

        int i =  pref.getLanguage(this);

        if (isLeft){

            i--;
            //负数纠正
            if (i < 0){
                // i = i * -1;
                i =  2;
            }
            // i = (i - 1) % 3;
        }else {

           i++;
            //大数纠正
            if (i == 3){
                // i = i * -1;
                i = 0;
            }
            // i = (i + 1) % 3;
        }

//        //负数纠正
//        if (i < 0){
//           // i = i * -1;
//            i = i + 3;
//        }
        //languageText.setText(pref.LANGUAGE_OPTION[i]);

        //设置长久存储游戏语言
        pref.setLanguage(i,this);
        MediaService.play(this,R.raw.slide);

        //更新UI上的语言
        updateLanguageUi();
    }

    private void updateLanguageUi() {
        pref.changeLocaleAccordingOptions(this,pref.LANGUAGE_OPTION[ pref.getLanguage(this)]);
        languageText.setText(R.string.options_languagei);
        language_title.setText(R.string.options_title);
        optionsMainmenu.setText(R.string.player_name_menu);
        options_language.setText(R.string.options_language);
        options_music.setText(R.string.options_music);
        text_sound.setText(R.string.options_sound);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaService.play(this,R.raw.button);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 20){
            handleLanguageOption(true);
        }else if (e2.getX() - e1.getX() > 20){
            handleLanguageOption(false);
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //如果用户点击的是选择语言的按按钮就处理
        if (v.getId() == R.id.options_languages) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
        return false;
    }
}
