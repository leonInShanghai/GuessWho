package guess.bobo.cn.guesswho.utils;

/**
 * Created by Leon on 2018/6/16.
 * Functions:
 */
public class Constants {

    /**打印用的TAG 没有也可以 这里就没有使用*/
    //public static final String LOG_TAG = "GuessWho";

    /**字符串是提供程序的授权的 在Manifest中注册*/
    public static final String CONTENT_PROVIDER_AUTHORITY = "guess.bobo.cn.guesswho.questionprovider";

    public static final String DB_FILE_NAME = "question.db";

    public static final String KEY_SOUND_RES_ID_IN_INTENT = "SOUND_RES";
    public static final String KEY_PAUSE_MUSIC = "PAUSE_MUSIC";
    public static final String KEY_PLAYER_NAME_IN_INTENT = "PLAYER_NAME";

    //第一關條件 門檻
    public static final int CardLimitNumber1 = 1;

    //第二關條件 門檻
    public static final int CardLimitNumber2 = 2;

    //全App变量用来记录首页是否刷新text(国际化)
    public static boolean ISUPDATE = false;
}
