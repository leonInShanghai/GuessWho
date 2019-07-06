package guess.bobo.cn.guesswho.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.component.SlidingButton;
import guess.bobo.cn.guesswho.modle.OneRoundGame;
import guess.bobo.cn.guesswho.modle.Question;
import guess.bobo.cn.guesswho.service.MediaService;
import guess.bobo.cn.guesswho.service.PrefService;
import guess.bobo.cn.guesswho.service.SoundService;
import guess.bobo.cn.guesswho.utils.DBUtil;
import guess.bobo.cn.guesswho.utils.HighScoreDB;
import guess.bobo.cn.guesswho.utils.LogUtil;
import guess.bobo.cn.guesswho.utils.PixelToDP;

/**
 * Created by Leon on 2018/6/10.
 * Functions: 游戏-答题 界面
 */
public class MainViewActivity extends Activity implements View.OnClickListener{

    private static final int MSG_ID_UPDATE_TIME = 1;
    private static final int MSG_ID_CLOCK_NEXT_QUESTION = 2;
    private static final int MSG_ID_SHOW_TIP = 3;

    private TextView mainViewMin;
    private TextView mianViewSec;
    private ImageView mainviewPersonIcon;
    private TextView mainviewPersonType;
    private TextView mainviewSampleQuestion;
    private TextView mainviewTipMsg;
    private Button mainviewTipButton;
    private TextView mainviewPersonDecription;
    private TextView mainviewPlayerName;
    private TextView mainviewTotalPoints;
    private TextView mainviewAnswer1;
    private SlidingButton mainviewAnswer1Button;
    private TextView mainviewAnswer2;
    private SlidingButton mainviewAnswer2Button;
    private TextView mainviewAnswer3;
    private SlidingButton mainviewAnswer3Button;
    private ImageView mainviewStarList1;
    private ImageView mainviewStarList2;
    private ImageView mainviewStarList3;
    private ImageView mainviewStarList4;
    private ImageView mainviewStarList5;
    private ImageView mainviewStarList6;
    private ImageView mainviewStarList7;
    private ImageView mainviewStarList8;
    private ImageView mainviewStarList9;
    private ImageView mainviewStarList10;
    //mainviewStarList[0] = mainviewStarList1;
    private ImageView[] mainviewStarLists = new ImageView[10];
    private Button mainviewSkip;
    private Button mainviewEnd;

    private OneRoundGame game;
    private String playName;
    //提示消息播放动画的时候用户不能点击答题按钮
    private boolean isNoAnmin = true;


    private List<OneRoundGame> allgames = new ArrayList<OneRoundGame>();



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MSG_ID_UPDATE_TIME:
                    if (!game.isPaused()) {
                        game.increaseTimeUsed();
                        updateTimerUI();
                    }
                    sendEmptyMessageDelayed(MSG_ID_UPDATE_TIME, 1000);
                    break;
                case MSG_ID_CLOCK_NEXT_QUESTION:
                    updateUIAccordingGameContent();
                    break;
                case MSG_ID_SHOW_TIP:
                    mainviewTipMsg.setVisibility(View.VISIBLE);
                    Animation tipAnim = AnimationUtils.loadAnimation(MainViewActivity.this,R.anim.tip_animation);
                    tipAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            mainviewTipMsg.setVisibility(View.VISIBLE);
                            mainviewSampleQuestion.setVisibility(View.GONE);
                            isNoAnmin = false;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mainviewTipMsg.setVisibility(View.GONE);
                            mainviewSampleQuestion.setVisibility(View.VISIBLE);
                            game.addTipCnt();
                            setUITip();
                            isNoAnmin = true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mainviewTipMsg.startAnimation(tipAnim);
                    break;

            }
        }
    };

    private void updateUIAccordingGameContent() {
        //设置游戏者的名字：如果游戏者名字为空，则前面已经处理
        mainviewPlayerName.setText("Player"+":"+playName);

        //设置当前问题的提示信息
        setUITip();


        //设置游戏当前的分数
        mainviewTotalPoints.setText("Points:"+String.valueOf(game.getScore()));

        //根据玩家答对题目数量设置点亮星星的个数
        for (int i = 0;i < mainviewStarLists.length;i++){
            ImageView starImage = mainviewStarLists[i];

            switch (game.getLevel()){
                case 0:
                    if (i < game.getCorrectAnsCntForCurrentLevel()){//答对的题亮星
                        starImage.setImageResource(R.drawable.small_star1);
                    }else {//回答错误的暗星
                        starImage.setImageResource(R.drawable.small_star2);
                    }
                    break;
                case 1:
                    if (i < game.getCorrectAnsCntForCurrentLevel() - game.LEVEL_1_CNT){//答对的题亮星
                        starImage.setImageResource(R.drawable.small_star1);
                    }else {//回答错误的暗星
                        starImage.setImageResource(R.drawable.small_star2);
                    }
                    break;
                case 2:
                    if (i < game.getCorrectAnsCntForCurrentLevel() - game.LEVEL_2_CNT){//答对的题亮星
                        starImage.setImageResource(R.drawable.small_star1);
                    }else {//回答错误的暗星
                        starImage.setImageResource(R.drawable.small_star2);
                    }
                    break;
            }

        }

        //设置当前题目，包括题目，提示，答案
        Question question = game.getCurrentQuestion();
        List<String> allAnswers = question.getAllAnswers();
        mainviewSampleQuestion.setText(question.getQuestion());
        mainviewAnswer1.setText(allAnswers.get(0));
        mainviewAnswer2.setText(allAnswers.get(1));
        mainviewAnswer3.setText(allAnswers.get(2));
        mainviewPersonDecription.setText(question.getDescription());

        //根据题目中人物的类型，设置任务图片和对应的图片标题
        int presonType = game.getCurrentQuestion().getPersonType();
        mainviewPersonIcon.setImageResource(personTypePic.get(presonType));
        mainviewPersonType.setText(personTypeName.get(presonType));
    }

    //设置当前问题的提示信息
    private void setUITip() {
        mainviewTipMsg.setText(game.getCurrentTip());
        setTipButtonCount();
    }

    private void updateTimerUI() {
        mainViewMin.setText(String.valueOf(game.getTimeUsedInMin()));
        mianViewSec.setText(String.valueOf(game.getTimeUsedInSec()));
    }

    @SuppressLint("WrongViewCast")
    private void findViews() {
        mainViewMin = (TextView)findViewById( R.id.main_view_min );
        mianViewSec = (TextView)findViewById( R.id.mian_view_sec );
        mainviewPersonIcon = (ImageView)findViewById( R.id.mainview_person_icon );
        mainviewPersonType = (TextView)findViewById( R.id.mainview_person_type );
        mainviewSampleQuestion = (TextView)findViewById( R.id.mainview_sample_question );
        mainviewTipMsg = (TextView)findViewById( R.id.mainview_tip_msg );
        mainviewTipButton = (Button)findViewById( R.id.mainview_tip_button );
        mainviewPersonDecription = (TextView)findViewById( R.id.mainview_person_decription );
        mainviewPlayerName = (TextView)findViewById( R.id.mainview_player_name );
        mainviewTotalPoints = (TextView)findViewById( R.id.mainview_total_points );
        mainviewAnswer1 = (TextView)findViewById( R.id.mainview_answer_1 );
        mainviewAnswer1Button = (SlidingButton)findViewById( R.id.mainview_answer_1_button );
        mainviewAnswer2 = (TextView)findViewById( R.id.mainview_answer_2 );
        mainviewAnswer2Button = (SlidingButton)findViewById( R.id.mainview_answer_2_button );
        mainviewAnswer3 = (TextView)findViewById( R.id.mainview_answer_3 );
        mainviewAnswer3Button = (SlidingButton)findViewById( R.id.mainview_answer_3_button );
        mainviewStarList1 = (ImageView)findViewById( R.id.mainview_star_list1 );
        mainviewStarList2 = (ImageView)findViewById( R.id.mainview_star_list2 );
        mainviewStarList3 = (ImageView)findViewById( R.id.mainview_star_list3 );
        mainviewStarList4 = (ImageView)findViewById( R.id.mainview_star_list4 );
        mainviewStarList5 = (ImageView)findViewById( R.id.mainview_star_list5 );
        mainviewStarList6 = (ImageView)findViewById( R.id.mainview_star_list6 );
        mainviewStarList7 = (ImageView)findViewById( R.id.mainview_star_list7 );
        mainviewStarList8 = (ImageView)findViewById( R.id.mainview_star_list8 );
        mainviewStarList9 = (ImageView)findViewById( R.id.mainview_star_list9 );
        mainviewStarList10 = (ImageView)findViewById( R.id.mainview_star_list10 );
        mainviewSkip = (Button)findViewById( R.id.mainview_skip );
        mainviewEnd = (Button)findViewById( R.id.mainview_end );

        mainviewTipButton.setOnClickListener( this );


//        mainviewAnswer1Button.setOnClickListener( this );
//        mainviewAnswer2Button.setOnClickListener( this );
//        mainviewAnswer3Button.setOnClickListener( this );
        mainviewSkip.setOnClickListener( this );
        mainviewEnd.setOnClickListener( this );

        //主界面下方的星星，保存到数组中
        mainviewStarLists[0] = mainviewStarList1;
        mainviewStarLists[1] = mainviewStarList2;
        mainviewStarLists[2] = mainviewStarList3;
        mainviewStarLists[3] = mainviewStarList4;
        mainviewStarLists[4] = mainviewStarList5;
        mainviewStarLists[5] = mainviewStarList6;
        mainviewStarLists[6] = mainviewStarList7;
        mainviewStarLists[7] = mainviewStarList8;
        mainviewStarLists[8] = mainviewStarList9;
        mainviewStarLists[9] = mainviewStarList10;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        findViews();
        playName = getIntent().getStringExtra("PLAYER_MAME");

        for(int i = 0; i<4;i++) {
            OneRoundGame oneGame = new OneRoundGame(playName);
//            List<Integer> questionIDs = DBUtil.getAllQuestionIdsInOneGame(this,OneRoundGame.LEVEL_1_COUNT + 3,
//                    OneRoundGame.LEVEL_2_COUNT + 3,OneRoundGame.LEVEL_3_COUNT + 3);
            List<Integer> questionIDs = DBUtil.getAllQuestionIdsInOneGame(this, OneRoundGame.LEVEL_1_COUNT,
                    OneRoundGame.LEVEL_2_COUNT,OneRoundGame.LEVEL_3_COUNT);

            for (int questionID : questionIDs) {
                Question question = DBUtil.getQuestion(questionID);
                oneGame.fillQuestions(question);
            }
            allgames.add(oneGame);
        }

        game = allgames.get(0);

        //设置提示数量
       // setTipButtonCount();

//        for (int questionID : questionIDs){  之前不全面
//            Question question = DBUtil.getQuestion(questionID);
//            game.fillQuestions(question);
//            allgames.add(game);
//        }

        startTimeRecoder();//开始计时器
        startGame();

        //这里播放在onResume中调用一次就可以了
      //  MediaService.play(this,R.raw.loop);
    }

    private void startGame() {
        //下一题
        handler.sendEmptyMessage(MSG_ID_CLOCK_NEXT_QUESTION);
    }

    @Override
    protected void onPause() {//Activity “暂停”要做程序的暂停工作
        super.onPause();
        //设置游戏（计时器）暂停
        game.setPaused(true);

        // 关闭游戏的背景音乐
        MediaService.pause(this, R.raw.loop);
    }


    @Override
    protected void onResume() {//Activity “开始或恢复”要做程序的恢复工作
        super.onResume();
        //设置游戏（计时器）暂停后的恢复
        game.setPaused(false);
        // 打开游戏的背景音乐-启动计时器的时候打开这里就不用打开了
        MediaService.play(this, R.raw.loop);


        //根据玩家答对题目数量设置点暗星星的个数
        for (int i = 0;i < mainviewStarLists.length;i++){
            ImageView starImage = mainviewStarLists[i];
                starImage.setImageResource(R.drawable.small_star2);
        }
    }

    /**用户点击提示后使用提示的次数增加*/
    private void setTipButtonCount() {

        if (game.getTipUsedCnt() == 0){
            mainviewTipButton.setText("Tip\n0/3");
        }else if (game.getTipUsedCnt() == 1){
            mainviewTipButton.setText("Tip\n1/3");
        }else if (game.getTipUsedCnt() == 2){
            mainviewTipButton.setText("Tip\n2/3");
        }else if (game.getTipUsedCnt() == 3){
            mainviewTipButton.setText("Tip\n3/3");
        }

    }



    //-----------------------------------------------------------------------------------------------------------------

//            for(int i = 0; i<4;i++)
//            {
//                OneRoundGame oneGame = new OneRoundGame(playerName + "---" + i);
//                List<Integer> questionIDs = DBUtil.getAllQuestionIdsInOneGame(this,
//                        currentGamer.LEVEL_1_CNT + 3, currentGamer.LEVEL_2_CNT + 3,
//                        currentGamer.LEVEL_3_CNT + 3);
//                for (int questionID : questionIDs) {
//                    Question question = DBUtil.getQuestion(questionID);
//                    oneGame.fillQuestions(question);
//                }
//                allgames.add(oneGame);
//            }
//
//            currentGamer = allgames.get(0);
//
//            startGame();
            // Before, Times has been used to update the clock,
            // now, we just use the handler with delayed message.
            // If you want use the timer again, un-comments following line.
            // startTimeRecorder();

            //mainview_answer_1_button.setOnClickListener(this);
            //mainview_answer_2_button.setOnClickListener(this);
            //mainview_answer_3_button.setOnClickListener(this);


//        private void startGame() {
//
//            // 开始计时：
//            handler.sendEmptyMessageDelayed(MSG_ID_CLOCK_UPDATE, 1000);
//            // 下一题
//            handler.sendEmptyMessage(MSG_ID_CLOCK_NEXT_QUESTION);
//        }
//
//        private void startTimeRecorder() {
//            Timer clockTimer = new Timer();
//            clockTimer.scheduleAtFixedRate(clockTask, 1000, 1000);
//        }


        /*
         * 处理UI更新
         */
//        private Handler handler = new Handler() {
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case MSG_ID_CLOCK_UPDATE:
//                        if (!currentGamer.isPaused()) {
//                            updateTimeRecorder();
//                        }
//                        handler.sendEmptyMessageDelayed(MSG_ID_CLOCK_UPDATE, 1000);
//                        break;
//                    case MSG_ID_CLOCK_NEXT_QUESTION:
//                        updateUIAccordingGameContent();
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//        };

//        // 用于界面时钟更新的定时任务
//        private class ClockTimerTask extends TimerTask {
//            private boolean paused = false;
//
//            @Override
//            public void run() {
//                if (!paused) {
//                    currentGamer.increaseTimeUsed();
//                    handler.sendEmptyMessage(MSG_ID_CLOCK_UPDATE);
//                }
//            }
//
//            public void pause() {
//                paused = true;
//            }
//
//            public void resume() {
//                paused = false;
//            }
//        }


        private static final int DIALOG_ID_ANSWER_RIHGT = 1;

        protected Dialog onCreateDialog(int id) {
            Dialog dialog = null;
            switch (id) {
                case DIALOG_ID_ANSWER_RIHGT:
                    // do the work to define the pause Dialog
                    break;
                default:
                    dialog = null;
            }
            return dialog;
        }

    //---------------------------------------------------------------------------------------------------------------

    //计时器
    private void startTimeRecoder() {
        handler.sendEmptyMessageDelayed(MSG_ID_UPDATE_TIME,1000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 用户选择了答案1 isNoAnmin提示信息是否播放完
        if (isNoAnmin && mainviewAnswer1Button.handleParentTouchEvent(this,event)) {
            selectAnswer(0);
        }
        // 用户选择了答案2 isNoAnmin提示信息是否播放完
        if (isNoAnmin && mainviewAnswer2Button.handleParentTouchEvent(this, event)) {
            selectAnswer(1);
        }
        // 用户选择了答案3 isNoAnmin提示信息是否播放完
        if (isNoAnmin && mainviewAnswer3Button.handleParentTouchEvent(this, event)) {
            selectAnswer(2);
        }

        return super.onTouchEvent(event);
    }

    /**
     *点击事件的处理
     */
    @Override
    public void onClick(View v) {

        //只要是用户点击了按钮就播放按钮的声音 不用每次都设置
        if (v instanceof Button){
            MediaService.play(this,R.raw.button);
        }

        if ( v == mainviewTipButton ) {
            // 用户点击了提示按钮
            handler.sendEmptyMessage(MSG_ID_SHOW_TIP);
        }
        //改用动画效果不用这里了
//         else if ( v == mainviewAnswer1Button ) {
//            // 用户选择了答案1 isNoAnmin提示信息是否播放完
//            if (isNoAnmin){ selectAnswer(0); }
//        } else if ( v == mainviewAnswer2Button ) {
//            // 用户选择了答案2 isNoAnmin提示信息是否播放完
//            if (isNoAnmin){selectAnswer(1);}
//        } else if ( v == mainviewAnswer3Button ) {
//            // 用户选择了答案3 isNoAnmin提示信息是否播放完
//            if (isNoAnmin){selectAnswer(2);}
//        } else
       if ( v == mainviewSkip ) {
            // 用户点击了跳过
           finish();

        } else if ( v == mainviewEnd ) {
            // 用户点击了结束
          finish();

        }
    }




    private void selectAnswer(int useSelectedAnswerIndex) {
        if (game.userSelectAnswer(useSelectedAnswerIndex)) {
            MediaService.play(this, R.raw.right);
        } else {
            MediaService.play(this, R.raw.wrong);
        }

        boolean isAllGameFinished = true;

        for(OneRoundGame game : allgames) {
            if (!game.isFinish()) {
                isAllGameFinished = false;
            }else{
                //用户打错题游戏结束
                endTheGameAheadOfTime();
            }
        }

//        //打通关
//        if (isAllGameFinished) {
//
//            //传递参数（Intent），调用结束界面。Finish主界面，
//            Intent intent = new Intent(this, UselessActivity.class);
//            intent.putExtra("PLAYER_MAME",playName);
//            Timer timer = new Timer();
//            TimerTask saveHighScorTask = new TimerTask(){
//
//                @Override
//                public void run() {
//                    String timeUsed = game.getTimeUsedInMin() + ":" + game.getTimeUsedInSec();
//                    HighScoreDB.save(MainViewActivity.this, game.getPlayerName(), timeUsed, game.getScore());
//             }};
//
//            timer.schedule(saveHighScorTask, 0);
//
//            startActivity(intent);
//            finish();
//        }else {

            if(game.isLevelUp()) {
                //传递参数，调用升级界面。返回到主界面。
                Intent intent = new Intent(this, PassLevelActivity.class);
                intent.putExtra("LEVEL", game.getLevel());
                intent.putExtra("PLAYER_NAME", game.getPlayerName());
                this.startActivity(intent);
            }


            //Leon-------------------------
            int count =  game.nextQuestionCount();
            int i = allgames.indexOf(game);
            //第一个game中的问题回答完了
            if (count >= game.getQuestionCount() - 1){
                if (i >= allgames.size() - 1){
                    //这里先不做任何处理原来是判断数组越界后面在game.nextQuestion()有处理
                }else {
                    game = allgames.get(i++);
                }
            }
            //Leon-------------------------

        //下一题时判断数组是否越界问题数组越界游戏结束
        if (game.nextQuestion()){
            //传递参数（Intent），调用结束界面。Finish主界面，
            Intent intent = new Intent(this, UselessActivity.class);
            intent.putExtra("PLAYER_MAME",playName);
            Timer timer = new Timer();
            TimerTask saveHighScorTask = new TimerTask(){

                @Override
                public void run() {
                    String timeUsed = game.getTimeUsedInMin() + ":" + game.getTimeUsedInSec();
                    HighScoreDB.save(MainViewActivity.this, game.getPlayerName(), timeUsed, game.getScore());
                }};

            timer.schedule(saveHighScorTask, 0);
            startActivity(intent);
            finish();
        }


//            int i = allgames.indexOf(game);
//            if (i == (allgames.size() - 1))
//            {
//                i = 0;
//            }else {
//                i++;
//            }
//            game = allgames.get(i);
//
//            while(game.isFinish()) {
//                if (i == (allgames.size() - 1)) {
//                    i = 0;
//                }else {
//                    i++;
//                }
//                game = allgames.get(i);
//            }

            handler.sendEmptyMessage(MSG_ID_CLOCK_NEXT_QUESTION);
//        }
    }

    /**用户打错题游戏结束*/
    private void endTheGameAheadOfTime() {

        //传递参数（Intent），调用结束界面。Finish主界面，
        Intent intent = new Intent(this,AheadOfTimeActivity.class);
        intent.putExtra("PLAYER_MAME",playName);
        Timer timer = new Timer();
        TimerTask saveHighScorTask = new TimerTask(){

            @Override
            public void run() {
                String timeUsed = game.getTimeUsedInMin() + ":" + game.getTimeUsedInSec();
                HighScoreDB.save(MainViewActivity.this, game.getPlayerName(), timeUsed, game.getScore());
            }};

        timer.schedule(saveHighScorTask, 0);

        startActivity(intent);
        finish();
    }



    @Override
    protected void onDestroy() {

        if (handler != null){
            handler.removeMessages(MSG_ID_UPDATE_TIME);
            handler.removeMessages(MSG_ID_CLOCK_NEXT_QUESTION);
            handler.removeMessages(MSG_ID_SHOW_TIP);
//            handler.removeMessages(MSG_ID_TAP_TO_START_ANIM);
//            handler.removeMessages(MSG_ID_START_GAME);
            //Leon增加退出MainViewActivity后设置为零重新开始游戏
            //Question.setQuestionId(0);
            handler = null;
            LogUtil.e("MainViewActivity销毁的时候移除了Handler");
        }

        super.onDestroy();
    }

//    @Override
//    public void onBackPressed() {  //这里返回不要发出声音避免再次打开音乐
//        super.onBackPressed();
//
//        MediaService.play(this,R.raw.button);
//
//        // 2018-6-18 记录：过于频繁的开关不起作用
//        MediaService.pause(MainViewActivity.this, R.raw.loop);
//    }

    //缓存人物类型和对应图片，字符串资源
    private static Map<Integer,Integer> personTypePic = new HashMap<Integer, Integer>();
    static {
        personTypePic.put(1,R.drawable.music);
        personTypePic.put(2,R.drawable.sports);
        personTypePic.put(3,R.drawable.movies);
        personTypePic.put(4,R.drawable.politics);
    }

    private static Map<Integer,Integer> personTypeName = new HashMap<Integer, Integer>();
    static {
        personTypeName.put(1,R.string.persion_type_name_1);
        personTypeName.put(2,R.string.persion_type_name_2);
        personTypeName.put(3,R.string.persion_type_name_3);
        personTypeName.put(4,R.string.persion_type_name_4);
    }


}
