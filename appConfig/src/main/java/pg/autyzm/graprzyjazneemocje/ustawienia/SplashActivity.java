package pg.autyzm.graprzyjazneemocje.ustawienia;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import java.util.Locale;
import java.util.Timer;

import pg.autyzm.graprzyjazneemocje.R;
import pg.autyzm.graprzyjazneemocje.lib.SqliteManager;

public class SplashActivity extends Activity {

    public static final String CURRENT_LANG = "KEY_CURRENT_LANG";

    public SqliteManager sqlm;
    protected Locale myLocale;
    Intent refresh;

    private RelativeLayout mainLayout;

    public Timer timer;
    public int userInteractionTimeout = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sqlm = SqliteManager.getInstance(this);
        setLocale(sqlm.getCurrentLang());
        setContentView(R.layout.starting_board_1);


        mainLayout=(RelativeLayout)findViewById(R.id.starting_board_1);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                refresh.putExtra(CURRENT_LANG, sqlm.getCurrentLang());
                startActivity(refresh);
                finish();
            }
        });

    }

    public void setLocale(final String localeName) {
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        sqlm.updateCurrentLang(localeName);

        //refresh = new Intent(this, ActivityChoice.class);
        refresh = new Intent(this, StartingBoard2.class);

/*        Handler handler = new Handler();
        handler.postDelayed(() -> {
            refresh.putExtra(CURRENT_LANG, localeName);
            startActivity(refresh);
            finish();
        }, 5000);*/



    }

    public void onBackPressed() {
        finish();
        System.exit(0);
    }

}
