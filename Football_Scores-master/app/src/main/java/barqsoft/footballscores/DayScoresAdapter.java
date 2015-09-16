package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Suren Grigoryan on 9/3/15.
 */
public class DayScoresAdapter extends RecyclerCursorAdapter<DayScoresAdapter.ScoreViewHolder> {


    private static final String TAG = DayScoresAdapter.class.getSimpleName();
    private static final String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
    private int mExpandPos;
    private int mCollapsePos = -1;

    public DayScoresAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    public int getExpandPos() {
        return mExpandPos;
    }

    public void setExpandPos(int mExpandPos) {
        this.mExpandPos = mExpandPos;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(final ViewGroup viewGroup, final Cursor cursor) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.scores_list_item2, viewGroup, false);

        final ScoreViewHolder viewHolder = new ScoreViewHolder(item, mContext);

        viewHolder.setOnClickListener(new ScoreViewHolder.OnClickListener() {
            @Override
            public void onClick(int position) {

                if (position != mExpandPos) {
                    mCollapsePos = mExpandPos;
                    mExpandPos = position;
                    notifyItemChanged(mExpandPos);
                    notifyItemChanged(mCollapsePos);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ScoreViewHolder viewHolder, final Cursor cursor, final int position) {

        viewHolder.homeName.setText(cursor.getString(DayScoresFragment.COL_HOME));
        viewHolder.awayName.setText(cursor.getString(DayScoresFragment.COL_AWAY));
        viewHolder.time.setText(cursor.getString(DayScoresFragment.COL_MATCH_TIME));
        viewHolder.score.setText(Utility.getScores(mContext, cursor.getInt(DayScoresFragment.COL_HOME_GOALS),
                cursor.getInt(DayScoresFragment.COL_AWAY_GOALS)));
        viewHolder.matchId = cursor.getDouble(DayScoresFragment.COL_ID);
        viewHolder.homeCrest.setImageResource(Utility.getTeamCrestByTeamName(
                cursor.getString(DayScoresFragment.COL_HOME)));
        viewHolder.awayCrest.setImageResource(Utility.getTeamCrestByTeamName(
                cursor.getString(DayScoresFragment.COL_AWAY)));

        if (mExpandPos == position) {
            if (viewHolder.container.getChildCount() <= 0) {
                viewHolder.container.addView(viewHolder.detailView, 0,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            }

            viewHolder.detailMatchDay.setText(Utility.getMatchDay(mContext,
                    cursor.getInt(DayScoresFragment.COL_MATCHDAY),
                    cursor.getInt(DayScoresFragment.COL_LEAGUE)));
            viewHolder.detailLeague.setText(Utility.getLeague(cursor.getInt(DayScoresFragment.COL_LEAGUE)));
        } else {
            viewHolder.container.removeAllViews();
        }
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ViewGroup container;
        private final LayoutInflater vi;
        private final View detailView;
        private final Button detailShare;
        public final TextView homeName;
        public final TextView awayName;
        public final TextView score;
        public final TextView time;
        public final TextView detailMatchDay;
        public final TextView detailLeague;
        public final ImageView homeCrest;
        public final ImageView awayCrest;
        public double matchId;
        public final View itemView;

        private OnClickListener mOnClickListener;


        public ScoreViewHolder(View v, final Context context) {
            super(v);

            itemView = v;
            itemView.setOnClickListener(this);


            homeName = (TextView) itemView.findViewById(R.id.home_name);
            awayName = (TextView) itemView.findViewById(R.id.away_name);
            score = (TextView) itemView.findViewById(R.id.score_textview);
            time = (TextView) itemView.findViewById(R.id.time_textview);
            homeCrest = (ImageView) itemView.findViewById(R.id.home_crest);
            awayCrest = (ImageView) itemView.findViewById(R.id.away_crest);

            vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            detailView = vi.inflate(R.layout.detail_fragment, null);

            detailMatchDay = (TextView) detailView.findViewById(R.id.matchday_textview);
            detailLeague = (TextView) detailView.findViewById(R.id.league_textview);

            detailShare = (Button) detailView.findViewById(R.id.share_button);
            detailShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String shareText = homeName.getText() + " "
                            + score.getText() + " " + awayName.getText() + " "
                            + FOOTBALL_SCORES_HASHTAG;
                    context.startActivity(Utility.createShareMatchIntent(shareText));
                }
            });

            container = (ViewGroup) itemView.findViewById(R.id.details_fragment_container);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) mOnClickListener.onClick(getLayoutPosition());
        }

        public void setOnClickListener(OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
        }

        public interface OnClickListener {
            void onClick(int position);
        }
    }

}
