package guess.bobo.cn.guesswho.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import guess.bobo.cn.guesswho.utils.Constants;

/**
 * Created by Leon on 2018/6/17.
 * Functions: 与数据库交互类 写入  方法一
 */
public class QuestionProvider extends ContentProvider {

    private DatabaseHelper mOpenHelper;
    private static final int ALL_QUESTION_IDS = 1;
    private static final int GET_QUESTION_DETAIL_BY_QUESTION_IDS = 2;

    private static final String SQL_ALL_QUESTION_ID_WITH_LEVEL = "select QuestionID, Level from question";
    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Constants.CONTENT_PROVIDER_AUTHORITY,
                "question", ALL_QUESTION_IDS);//Content://cn.itcast.game.questionContent/question
        sUriMatcher.addURI(Constants.CONTENT_PROVIDER_AUTHORITY,
                "question/#", GET_QUESTION_DETAIL_BY_QUESTION_IDS);//Content://cn.itcast.game.questionContent/question/17
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String sql = null;
        switch (sUriMatcher.match(uri)) {
            case ALL_QUESTION_IDS:
                sql = SQL_ALL_QUESTION_ID_WITH_LEVEL;
                break;
            case GET_QUESTION_DETAIL_BY_QUESTION_IDS:
                sql = SQL_ALL_QUESTION_ID_WITH_LEVEL;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, selectionArgs);
        return c;

    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        return 0;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        static String path = "/data/data/guess.bobo.cn.guesswho/files/" + Constants.DB_FILE_NAME;

        DatabaseHelper(Context context) {
            super(context, path, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}
