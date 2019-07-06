package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.service.MediaService;

/**
 * Created by Leon on 2018/7/5.
 * Functions:
 */
public class WeiboActivity extends Activity implements View.OnClickListener {

    private WebView weiboView;
    private ProgressBar progressbar;
    private RelativeLayout tempLayout;
    private Button goBack;
    private Button goForward;
    private Button reloadBtn;

    private void finndView(){
        weiboView = (WebView)findViewById(R.id.weibo_view);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);
        tempLayout = (RelativeLayout)findViewById(R.id.temp_layout);
        goBack = (Button)findViewById(R.id.goBack);
        goForward = (Button)findViewById(R.id.goForward);
        reloadBtn = (Button)findViewById(R.id.reload_btn);

        goBack.setOnClickListener(this);
        goForward.setOnClickListener(this);
        reloadBtn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_view);

        finndView();
        setWeiBoView();
    }

    @Override
    public void onClick(View v) {

        //只要是用户点击了按钮就播放按钮的声音 不用每次都设置
        if (v instanceof Button){
            MediaService.play(this,R.raw.button);
        }

        if (v == goBack){//用户点击了返回
            if (weiboView.canGoBack()){
               // goBack.setEnabled(true);
                weiboView.goBack();
            }else {
               // goBack.setEnabled(false);
                Toast.makeText(WeiboActivity.this,"没有上一页面",Toast.LENGTH_SHORT).show();
            }
        }else if (v == goForward){
            if (weiboView.canGoForward()){
                //goForward.setEnabled(true);
                weiboView.goForward();
            }else {
               // goForward.setEnabled(false);
                Toast.makeText(WeiboActivity.this,"没有下一页面",Toast.LENGTH_SHORT).show();
            }
        }else if (v == reloadBtn){
            weiboView.reload();
        }
    }

    private void setWeiBoView(){

        WebSettings webSettings = weiboView.getSettings();

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        //webSettings.setAllowFileAccess(true);

        //支持缩放，默认为true。
        webSettings .setSupportZoom(false);
        //调整图片至适合webview的大小
        webSettings .setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings .setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings .setDefaultTextEncodingName("utf-8");
        //设置自动加载图片
        webSettings .setLoadsImagesAutomatically(true);

        // 设置WebView的客户端
        weiboView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// 返回false
            }
        });

        weiboView.loadUrl("https://www.cnblogs.com/henanleon/p/9275865.html");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaService.play(this,R.raw.button);
    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient=new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressbar.setVisibility(View.GONE);
            tempLayout.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressbar.setVisibility(View.VISIBLE);
            tempLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    @Override
    protected void onDestroy() {
        if (weiboView != null){
            //释放资源
            weiboView.destroy();
            weiboView=null;
        }
        super.onDestroy();
    }


}
