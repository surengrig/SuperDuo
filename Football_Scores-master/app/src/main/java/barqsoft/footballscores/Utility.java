package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utility {
    public static final int BUNDESLIGA_1 = 394;                 // 1. Bundesliga
    public static final int BUNDESLIGA_2 = 395;                 // 2. Bundesliga
    public static final int LIGUE_1 = 396;                      // Ligue 1
    public static final int LIGUE_2 = 397;                      // Ligue 2
    public static final int PREMIER_LEAGUE = 398;               // Premier League
    public static final int PRIMERA_DIVISION = 399;             // Primera Division
    public static final int SEGUNDA_DIVISION = 400;             // Segunda Division
    public static final int SERIE_A = 401;                      // Serie A
    public static final int PRIMEIRA_LIGA = 402;                // Primeira Liga
    public static final int BUNDESLIGA_3 = 403;                 // 3. Bundesliga
    public static final int EREDIVISIE = 404;                   // Eredivisie
    public static final int CHAMPIONS_LEAGUE = 405;             // Champions League
    private static final int DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    public static String getLeague(int leagueId) {
        switch (leagueId) {
            case BUNDESLIGA_1:
                return "1. Bundesliga";
            case BUNDESLIGA_2:
                return "2. Bundesliga";
            case LIGUE_1:
                return "Ligue 1";
            case LIGUE_2:
                return "Ligue 2";
            case PREMIER_LEAGUE:
                return "Premier League";
            case PRIMERA_DIVISION:
                return "Primera Division";
            case SEGUNDA_DIVISION:
                return "Segunda Division";
            case SERIE_A:
                return "Serie A";
            case PRIMEIRA_LIGA:
                return "Primeira Liga";
            case BUNDESLIGA_3:
                return "3. Bundesliga";
            case EREDIVISIE:
                return "Eredivisie";
            case CHAMPIONS_LEAGUE:
                return "Champions League";
            default:
                return "Not known League Please report id: " + leagueId;
        }
    }

    public static String getMatchDay(Context context, int matchDay, int leagueId) {
        if (leagueId == CHAMPIONS_LEAGUE) {
            if (matchDay <= 6) {
                return context.getString(R.string.format_match_day_group_stages, matchDay);
            } else if (matchDay == 7 || matchDay == 8) {
                return context.getString(R.string.match_day_knockout);
            } else if (matchDay == 9 || matchDay == 10) {
                return context.getString(R.string.match_day_quarter_final);
            } else if (matchDay == 11 || matchDay == 12) {
                return context.getString(R.string.match_day_semifinal);
            } else {
                return context.getString(R.string.match_day_final);
            }
        } else {
            return context.getString(R.string.format_match_day,
                     matchDay);
        }
    }

    public static String getScores(Context context, int homeGoals, int awayGoals) {
        if (homeGoals < 0 || awayGoals < 0) {
            return context.getString(R.string.match_no_score);
        } else
        {
            return context.getString(R.string.format_match_score, homeGoals, awayGoals);
        }
    }

    public static int getTeamCrestByTeamName(String teamName) {
        if (teamName == null) {
            return R.drawable.no_icon;
        }
        switch (teamName) { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC":
                return R.drawable.arsenal;
            case "Manchester United FC":
                return R.drawable.manchester_united;
            case "Swansea City":
                return R.drawable.swansea_city_afc;
            case "Leicester City":
                return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC":
                return R.drawable.everton_fc_logo1;
            case "West Ham United FC":
                return R.drawable.west_ham;
            case "Tottenham Hotspur FC":
                return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion":
                return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC":
                return R.drawable.sunderland;
            case "Stoke City FC":
                return R.drawable.stoke_city;
            default:
                return R.drawable.no_icon;
        }
    }

    public static String getDate(int day) {
        Date date = new Date(System.currentTimeMillis() + (day * DAY_IN_MILLIS));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(date);
    }


    public static String getDayName(Context context, int day) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.
        long dateInMillis = System.currentTimeMillis() + (day * DAY_IN_MILLIS);
        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if (julianDay == currentJulianDay + 1) {
            return context.getString(R.string.tomorrow);
        } else if (julianDay == currentJulianDay - 1) {
            return context.getString(R.string.yesterday);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }


    @SuppressWarnings("deprecation")
    public static Intent createShareMatchIntent(String shareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        return shareIntent;
    }

    public static String getWidgetContentDesc(Context c, String home, int homeGoals, String away, int awayGoals, String time) {
        if (homeGoals < 0 || awayGoals < 0) {
          return c.getString(R.string.format_a11y_widget_match_no_scores, home, away, time);
        }else {
            return c.getString(R.string.format_a11y_widget_match, home, homeGoals, away, awayGoals, time);
        }
    }
}
