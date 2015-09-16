package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.service.FetchScoresService;
import icepick.Icepick;
import icepick.State;

/**
 * Created by Suren Grigoryan on 9/3/15.
 */
public class DayScoresFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // Indices tied to SCORES_COLUMNS
    static final int COL_ID = 0;
    static final int COL_HOME = 1;
    static final int COL_HOME_GOALS = 2;
    static final int COL_AWAY = 3;
    static final int COL_AWAY_GOALS = 4;
    static final int COL_LEAGUE = 5;
    static final int COL_MATCHDAY = 6;
    static final int COL_MATCH_TIME = 7;

    private static final int SCORES_LOADER = 0;

    private static final String TAG = DayScoresFragment.class.getSimpleName();

    // Specifies columns needed to show stored data
    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.ScoresEntry._ID,
            DatabaseContract.ScoresEntry.COLUMN_HOME,
            DatabaseContract.ScoresEntry.COLUMN_HOME_GOALS,
            DatabaseContract.ScoresEntry.COLUMN_AWAY,
            DatabaseContract.ScoresEntry.COLUMN_AWAY_GOALS,
            DatabaseContract.ScoresEntry.COLUMN_LEAGUE,
            DatabaseContract.ScoresEntry.COLUMN_MATCH_DAY,
            DatabaseContract.ScoresEntry.COLUMN_TIME
    };
    @State
    String[] fragmentDate = new String[1];
    // Stores the position of the expanded cardview
    @State
    int expandedViewPos;
    private DayScoresAdapter mAdapter;

    private void updateScores() {
        getActivity().startService(new Intent(getActivity(), FetchScoresService.class));
    }

    public void setFragmentDate(String date) {
        fragmentDate[0] = date;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        expandedViewPos = mAdapter.getExpandPos();
        Icepick.saveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
        mAdapter.setExpandPos(expandedViewPos);

        getLoaderManager().initLoader(SCORES_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day_scores, container, false);
        RecyclerView scoresRecyclerView = (RecyclerView) rootView.findViewById(R.id.scores_list);
        scoresRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateScores();
        mAdapter = new DayScoresAdapter(getActivity(), null, 0);
        scoresRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), DatabaseContract.ScoresEntry.buildScoreWithDate(),
                SCORES_COLUMNS, null, fragmentDate, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

}
