package pg.autyzm.graprzyjazneemocje.gra;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import pg.autyzm.graprzyjazneemocje.R;

//not used
public class LevelFailedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_failed);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public void repeatLevel(View view){

        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}
