package guess.bobo.cn.guesswho.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

import guess.bobo.cn.guesswho.modle.RankingListModle;

/**
 * Created by Leon on 2018/6/18.
 * Functions: 数据库操作工具类
 */
public class HighScoreDB extends SQLiteOpenHelper {

    private static String tableName = "high_score";
    private static String DB_NAME = "HIGH_SCORE";
    private static String SQL_CREATE = "CREATE TABLE high_score (id INTEGER primary key autoincrement, highScoresName varchar(50),highScoresPoints int,highScoresTimeUsed varchar(20) )";
    private static String SQL_CNT = "select count(*) count from high_score";

    private static final String SELET_TOP_ONE = "select highScoresName, highScoresPoints from " +
            "high_score order by highScoresPoints desc, " +
            "highScoresTimeUsed asc limit 1";
    private static final String SELET_REVERSE_TOP_ONE = "select highScoresName, highScoresPoints from " +
            "high_score order by highScoresPoints asc, " +
            "highScoresTimeUsed desc limit 1";

    //Leon获取数据库中的全部 order by highScoresPoints desc 根据分数高低排序
    private static final String SELET_TOP_ALL = "select highScoresName, highScoresPoints from high_score order by highScoresPoints desc";

    private static String COL_NAME_NAME = "highScoresName";
    private static String COL_NAME_POINTS = "highScoresPoints";
    private static String COL_NAME_TIME = "highScoresTimeUsed";

    private static int COL_INDEX_ID = 0;
    private static int COL_INDEX_NAME = 1;
    private static int COL_INDEX_POINTS = 2;
    private static int COL_INDEX_TIME = 3;

    public HighScoreDB(Context context, String name, SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(Map<String, Object> fields) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (String name : fields.keySet()) {
            cv.put(name, fields.get(name).toString());
        }
        return db.insert(tableName, null, cv);
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String where = "id = ?";
        String[] whereValue = { Integer.toString(id) };
        db.delete(tableName, where, whereValue);
    }

    public long update(int id, Map<String, String> fields) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (String name : fields.keySet()) {
            cv.put(name, fields.get(name));
        }
        String where = "id = ?";
        String[] whereValue = { Integer.toString(id) };
        return db.update(tableName, cv, where, whereValue);
    }

    private int getCnt() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SQL_CNT, null);
        c.moveToFirst();
        int cnt = c.getInt(0);
        return cnt;

    }

    public static void save(Context context, String playerName,
                            String timeUsed, int score) {
        HighScoreDB db = new HighScoreDB(context, DB_NAME, null, 1);

        //这里写三可能只显示3条数据
        if (db.getCnt() >= 3) {

            int points = db.getLowestPoint();
            if (points > score) {

            }else {
                db.deleteLowest(points);
                Map map = new HashMap();
                map.put(COL_NAME_NAME, playerName);
                map.put(COL_NAME_POINTS, score);
                map.put(COL_NAME_TIME, timeUsed);
                db.insert(map);
            }

        } else {
            Map map = new HashMap();
            map.put(COL_NAME_NAME, playerName);
            map.put(COL_NAME_POINTS, score);
            map.put(COL_NAME_TIME, timeUsed);

            db.insert(map);
        }

    }

    private void deleteLowest(int points) {
        String sql = "delete from high_score where highScoresPoints=" + points;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    private int getLowestPoint() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELET_REVERSE_TOP_ONE, null);
        if (c.moveToFirst())
        {
            int playerScore = c.getInt(1);
            return playerScore;
        }else {
            return -1;
        }
    }

    //获取最高分数一条
    public static Map getHighestPoint(Context context)
    {
        HighScoreDB db = new HighScoreDB(context, DB_NAME, null, 1);
        return db.getHighestPoint();
    }

    private Map getHighestPoint() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELET_TOP_ONE, null);
        if (c.moveToFirst())
        {
            String playerName = c.getString(0);
            int playerScore = c.getInt(1);
            Map result = new HashMap();
            result.put("PLAYERNAME", playerName);
            result.put("POINTS", playerScore);
            return result;
        }else {
            return null;
        }
    }

    /**Leon获取数据库中的全部排名*/
    public  RankingListModle getHighestAll() {
        RankingListModle rankingModle = new RankingListModle();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELET_TOP_ALL, null);
        while (cursor.moveToNext()){
            String playerName = cursor.getString(0);
            int playerScore = cursor.getInt(1);

            //获取排名中的所有分数
            rankingModle.getPlayerScores().add(playerScore);

            //获取排名中的所有姓名
            rankingModle.getPlayerNames().add(playerName);
        }
        cursor.close();

        return rankingModle;
//        if (c.moveToFirst())
//        {
//            String playerName = c.getString(0);
//            int playerScore = c.getInt(1);
//            Map result = new HashMap();
//            result.put("PLAYERNAME", playerName);
//            result.put("POINTS", playerScore);
//            return result;
//        }else {
//            return null;
//        }
    }

    //获取最高分数一条
    public static RankingListModle getHighestAll(Context context)
    {
        HighScoreDB db = new HighScoreDB(context, DB_NAME, null, 1);
        return db.getHighestAll();
    }


}
