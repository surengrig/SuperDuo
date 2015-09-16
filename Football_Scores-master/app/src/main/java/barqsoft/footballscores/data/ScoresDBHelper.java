package barqsoft.footballscores.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import barqsoft.footballscores.data.DatabaseContract.ScoresEntry;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 2;

    public ScoresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CreateScoresTable = "CREATE TABLE " + DatabaseContract.SCORES_TABLE + " ("
                + DatabaseContract.ScoresEntry._ID + " INTEGER PRIMARY KEY,"
                + DatabaseContract.ScoresEntry.COLUMN_DATE + " TEXT NOT NULL,"
                + DatabaseContract.ScoresEntry.COLUMN_TIME + " INTEGER NOT NULL,"
                + DatabaseContract.ScoresEntry.COLUMN_HOME + " TEXT NOT NULL,"
                + DatabaseContract.ScoresEntry.COLUMN_AWAY + " TEXT NOT NULL,"
                + DatabaseContract.ScoresEntry.COLUMN_LEAGUE + " INTEGER NOT NULL,"
                + DatabaseContract.ScoresEntry.COLUMN_HOME_GOALS + " TEXT NOT NULL,"
                + DatabaseContract.ScoresEntry.COLUMN_AWAY_GOALS + " TEXT NOT NULL,"
                + ScoresEntry.COLUMN_MATCH_ID + " INTEGER NOT NULL,"
                + DatabaseContract.ScoresEntry.COLUMN_MATCH_DAY + " INTEGER NOT NULL,"
                + " UNIQUE (" + DatabaseContract.ScoresEntry.COLUMN_MATCH_ID + ") ON CONFLICT REPLACE"
                + " );";
        db.execSQL(CreateScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.SCORES_TABLE);
    }
}
