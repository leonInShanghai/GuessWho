package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.utils.Constants;
import guess.bobo.cn.guesswho.utils.LogUtil;

//AppCompatActivity
public class SplashActivity extends Activity{

    private static final int MSG_ID_CLOSE_SPLASH = 1;
    private static final int DELAY_TIME_SEC = 1800;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case MSG_ID_CLOSE_SPLASH :
                    closeSplashWindow();
                    //Leon写的新方法-移除消息
                    handler.removeMessages(MSG_ID_CLOSE_SPLASH);
                    break;
            }

           // this.removeMessages(msg.what); 原来是在这里这样写的
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        handler.sendEmptyMessageDelayed(MSG_ID_CLOSE_SPLASH,DELAY_TIME_SEC);

        //从数据库中加载数据
        copyDBFileIfFileDoesNotExit();
    }
    /**从数据库中加载数据*/
    private void copyDBFileIfFileDoesNotExit(){
        Timer timer = new Timer();
        TimerTask copyTask = new TimerTask() {
            @Override
            public void run() {
                //从数据库（assets中 question.db）复制文件
                if (!isFileExist()) {
                    //开始复制文件
                    InputStream iSt = null;
                    FileOutputStream oSm = null;

                    try {
                        iSt = SplashActivity.this.getAssets().open(Constants.DB_FILE_NAME);
                        oSm = SplashActivity.this.openFileOutput(Constants.DB_FILE_NAME, MODE_PRIVATE);
                        byte[] buffer = new byte[1024];
                        int cnt = iSt.read(buffer);
                        while (cnt != -1) {
                            oSm.write(buffer);
                            cnt = iSt.read(buffer);
                        }
                        oSm.flush();
                    } catch (IOException e) {
                        LogUtil.e("SplashActivity-line74");
                        e.printStackTrace();
                    } finally {
                        try {
                            if (iSt != null) {
                                iSt.close();
                            }
                            if (oSm != null) {
                                oSm.close();
                            }
                        } catch (IOException ioe) {
                            LogUtil.e("Error in closing the Stream line85");
                        }
                    }
                }
            }

            /**判断文件是否存在避免重复获取*/
            private boolean isFileExist(){
                String[] files = SplashActivity.this.fileList();

                for (String fileName : files){
                    if (Constants.DB_FILE_NAME.equals(fileName)){
                        return true;
                    }
                }
                return false;
            }
        };

        //执行一个任务只执行一次
        timer.schedule(copyTask,0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //加上判断解决 跳转2次的bug
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //用户点击了屏幕发出没有延时消息间接调用 finish 方法
            handler.sendEmptyMessage(MSG_ID_CLOSE_SPLASH);
            return true;
        }

        return super.onTouchEvent(event);
        //return true;
    }

    //关闭Splash页面
    private void closeSplashWindow(){
        Intent intent = new Intent(this,MainMenuActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onDestroy() {
        //当activity销毁的时候 移除所有的消息
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
