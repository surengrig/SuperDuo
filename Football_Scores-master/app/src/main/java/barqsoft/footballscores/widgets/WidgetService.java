package barqsoft.footballscores.widgets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.R;
import barqsoft.footballscores.Utility;
import barqsoft.footballscores.data.DatabaseContract;

/**
 * Created by Suren Grigoryan on 9/10/15.
 */
@SuppressLint("NewApi")
public class WidgetService extends RemoteViewsService {
    private static final String[] FORECAST_COLUMNS = {
            DatabaseContract.ScoresEntry.COLUMN_MATCH_ID,
            DatabaseContract.ScoresEntry.COLUMN_HOME,
            DatabaseContract.ScoresEntry.COLUMN_AWAY,
            DatabaseContract.ScoresEntry.COLUMN_HOME_GOALS,
            DatabaseContract.ScoresEntry.COLUMN_AWAY_GOALS,
            DatabaseContract.ScoresEntry.COLUMN_TIME
    };

    // these indices must match the projection
    private static final int INDEX_MATCH_ID = 0;
    private static final int INDEX_HOME = 1;
    private static final int INDEX_AWAY = 2;
    private static final int INDEX_HOME_GOALS = 3;
    private static final int INDEX_AWAY_GOALS = 4;
    private static final int INDEX_MATCH_TIME = 5;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();

                Uri matchesUri = DatabaseContract.ScoresEntry.buildScoreWithDate();
                data = getContentResolver().query(matchesUri,
                        FORECAST_COLUMNS,
                        null,
                        new String[]{Utility.getDate(0)},
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                String home = data.getString(INDEX_HOME);
                int homeGoals = data.getInt(INDEX_HOME_GOALS);
                String away = data.getString(INDEX_AWAY);
                int awayGoals = data.getInt(INDEX_AWAY_GOALS);
                String time = data.getString(INDEX_MATCH_TIME);
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_provider_list_item);

                views.setContentDescription(R.id.widget_list_item,
                        Utility.getWidgetContentDesc(getApplicationContext(), home, homeGoals, away,
                                awayGoals, time));

                views.setTextViewText(R.id.widget_home_name, home);
                views.setTextViewText(R.id.widget_away_name, away);
                views.setTextViewText(R.id.widget_score_textview,
                        Utility.getScores(getApplicationContext(), homeGoals,
                                awayGoals));
                views.setTextViewText(R.id.widget_time_textview,
                        time);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_provider_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_MATCH_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
