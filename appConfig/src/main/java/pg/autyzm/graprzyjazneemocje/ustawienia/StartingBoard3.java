package pg.autyzm.graprzyjazneemocje.ustawienia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pg.autyzm.graprzyjazneemocje.R;

public class StartingBoard3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.starting_board_3);

        Button buttonBack = (Button) findViewById(R.id.starting_board_3_button_back);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartingBoard3.this, StartingBoard2.class);
                startActivity(intent);
            }
        });

        Button buttonContinue = (Button) findViewById(R.id.starting_board_3_button_continue);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartingBoard3.this, ActivityChoice.class);
                startActivity(intent);
            }
        });

        Button buttonSkip = (Button) findViewById(R.id.starting_board_3_button_skip);

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartingBoard3.this, ActivityChoice.class);
                startActivity(intent);
            }
        });
    }
}
