package guess.bobo.cn.guesswho.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import guess.bobo.cn.guesswho.modle.Question;

/**
 * Created by Leon on 2018/6/16.
 * Functions: 数据库交互类
 */
public class DBUtil {

    private static SQLiteDatabase db;

    /**查询到所有的QuestionID能知道有多少题 和难度Level*/
    private static final String SQL_GET_ALL_QUESTION_ID = "SELECT q.QuestionID,q.Level " +
            "FROM Question q";

    /**根据id查询到题目描述答案类型（歌星，影星，政治家，体育星）*/
    private static final String SQL_GET_QUESTION_DETALL = "SELECT q.Question,p.Name,p.Description," +
            "p.CategoryID,q.Level FROM Question q,Person p WHERE p.PersonID = q.PersonID AND q.QuestionID =?";

    /**根据id查询提示一次能得到2个结果*/
    private static final String SQL_GET_QUESTION_TIP = "SELECT t.Tip FROM Tip t WHERE " +
            "t.QuestionID = ?";

    /**查询到错误的答案*/
    private static final String SQL_GET_QUESTION_ANSWERS = "SELECT w.WrongAnswer FROM WrongAnswer " +
            "w WHERE w.QuestionID = ?";

    private static final Uri CONTENT_URI = Uri.parse("content://" + Constants.CONTENT_PROVIDER_AUTHORITY
            + "/question");


    /**返回所有问题的QuestionId */
    @TargetApi(Build.VERSION_CODES.O)
    public static List<Integer> getAllQuestionIdsInOneGame(Activity context, int levellCnt, int level2Cnt, int level3Cnt){

        List<Integer> result1 = new ArrayList<Integer>();
        List<Integer> result2 = new ArrayList<Integer>();
        List<Integer> result3 = new ArrayList<Integer>();

        db = SQLiteDatabase.openDatabase("data/data/guess.bobo.cn.guesswho/files/"
                +Constants.DB_FILE_NAME,null,SQLiteDatabase.OPEN_READONLY);

        //managedQuery(CONTENT_URI, null, null, null, null);过时方法用新方法替代 注意：5个参数！
        Cursor cursor = context.getContentResolver().query(CONTENT_URI,null,null,
                null,null);

        //原来方法可以使用
        //Cursor cursor = db.rawQuery(SQL_GET_ALL_QUESTION_ID,null);


        while (cursor.moveToNext()){
            int questionID = cursor.getInt(0);
            String level = cursor.getString(1).substring(0,1);
            if (level.equals("1")){
                result1.add(questionID);
            }else if (level.equals("2")){
                result2.add(questionID);
            }else if (level.equals("3")){
                result3.add(questionID);
            }
        }

        cursor.close();

        //随机出题
        Collections.shuffle(result1);
        Collections.shuffle(result2);
        Collections.shuffle(result3);

        List<Integer> result = new ArrayList<Integer>();
        result.addAll(result1.subList(0,levellCnt));
        result.addAll(result2.subList(0,level2Cnt));
        result.addAll(result3.subList(0,level3Cnt));
        return result;
    }

    /**返回问题 描述 答案等*/
    public static Question getQuestion(int questionId){
        Question question = new Question();
        question.setQuestionId(questionId);
        Cursor cursor = db.rawQuery(SQL_GET_QUESTION_DETALL,new String[]{
                String.valueOf(questionId)
        });
        while (cursor.moveToNext()){
            int categoryId = cursor.getInt(3);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String questionTitle = cursor.getString(0);
            String level = cursor.getString(4);

            //题目标题
            question.setQuestion(questionTitle+" 难度:"+level);

            //人物简介描述
            question.setDescription(description);

            //人物的分类
            question.setPersonType(categoryId);

            //正确答案
            question.setCorrectAnswer(name);
            //question.setCorrectAnswer(name+"*");
        }

        cursor.close();

        cursor = db.rawQuery(SQL_GET_QUESTION_TIP,new String[]{
           String.valueOf(questionId)
        });
        while (cursor.moveToNext()){
            String tip = cursor.getString(0);

            //问题的提示
            question.getTips().add(tip);
        }

        cursor.close();

        cursor = db.rawQuery(SQL_GET_QUESTION_ANSWERS,new String[]{
           String.valueOf(questionId)
        });
        while (cursor.moveToNext()){
            //错误答案
            String worngAnswer = cursor.getString(0);
            question.getWrongAnswers().add(worngAnswer);
        }

        cursor.close();

        return question;
    }

}
