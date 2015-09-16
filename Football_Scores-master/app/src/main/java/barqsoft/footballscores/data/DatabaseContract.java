package barqsoft.footballscores.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class DatabaseContract {
    public static final String SCORES_TABLE = "scores_table";
    //URI data
    public static final String CONTENT_AUTHORITY = "barqsoft.footballscores";
    public static final String PATH = "scores";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class ScoresEntry implements BaseColumns {
        //Table data
        public static final String COLUMN_LEAGUE = "league";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_HOME = "home";
        public static final String COLUMN_AWAY = "away";
        public static final String COLUMN_HOME_GOALS = "home_goals";
        public static final String COLUMN_AWAY_GOALS = "away_goals";
        public static final String COLUMN_MATCH_ID = "match_id";
        public static final String COLUMN_MATCH_DAY = "match_day";

        //Types
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static Uri buildScoreWithLeague() {
            return BASE_CONTENT_URI.buildUpon().appendPath("league").build();
        }

        public static Uri buildScoreWithId() {
            return BASE_CONTENT_URI.buildUpon().appendPath("id").build();
        }

        public static Uri buildScoreWithDate() {
            return BASE_CONTENT_URI.buildUpon().appendPath("date").build();
        }
    }
}
