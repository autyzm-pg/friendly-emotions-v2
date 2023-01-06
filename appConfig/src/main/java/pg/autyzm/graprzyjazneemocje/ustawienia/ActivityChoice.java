package pg.autyzm.graprzyjazneemocje.ustawienia;

import static pg.autyzm.graprzyjazneemocje.lib.SqliteManager.getInstance;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Locale;

import pg.autyzm.graprzyjazneemocje.R;
import pg.autyzm.graprzyjazneemocje.lib.SqliteManager;
//screen aneabling choice of activities (going to configurations or adding a picture view)
public class ActivityChoice extends AppCompatActivity {


    ImageView countryEn;
    ImageView countryPl;
    protected Locale myLocale;
    String currentLanguage = null;
    public SqliteManager sqlm;
    Button configurations;
    Button addMaterials;
    String root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name_game));


        sqlm = getInstance(this);

        currentLanguage = getIntent().getStringExtra(SplashActivity.CURRENT_LANG);


        countryPl = findViewById(R.id.imageView);
        countryPl.setOnClickListener(v -> setLocale("pl"));
        countryEn = findViewById(R.id.imageView2);
        countryEn.setOnClickListener(v -> setLocale("en"));

        configurations = findViewById(R.id.configurations);
        addMaterials = findViewById(R.id.addMaterials);

        final Intent mainActivity = new Intent(this, MainActivity.class);
        final Intent addMaterialsIntent = new Intent(this, AddMaterial.class);

        configurations.setOnClickListener(view -> startActivity(mainActivity));


        addMaterials.setOnClickListener(view -> startActivity(addMaterialsIntent));





        root = getExternalFilesDirs(null)[0].getAbsolutePath() + "/";


        File createMainDir = new File(root + "FriendlyEmotions" + File.separator);

        if (!createMainDir.exists())
            createMainDir.mkdir();

        sqlm.cleanTable("photos"); //TODO not clean and add, but only update
        sqlm.cleanTable("videos");

        String root = getExternalFilesDirs(null)[0].getAbsolutePath() + "/";

        File createDir = new File(root + "FriendlyEmotions/Photos" + File.separator);
        //TODO jak katalog nie istnieje, to przepisujemy zdjęcia z resourcow do nowotworzonego katalogu
        if (!createDir.exists()) {
            createDir.mkdir();

            Field[] drawables = pg.autyzm.graprzyjazneemocje.R.drawable.class.getFields();
            for (Field f : drawables) {
                try {
                    if (ifConstainsEmotionName(f.getName())) {
                        extractFromDrawable(f, "Photos", ".jpg", Bitmap.CompressFormat.JPEG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        //TODO bezwarunkowe przepisanie zdjęć z katalogu do bazy danych
        if (new File(root + "FriendlyEmotions/Photos").list() != null) {

            for (String fileName : new File(root + "FriendlyEmotions/Photos").list()) {

                try {
                    int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                    String[] photoSeg = fileName.split("_");
                    sqlm.addPhoto(resID, photoSeg[0] + "_" + photoSeg[1], fileName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        sqlm.AddDefaultLevelsIfNeeded();


    }



    public boolean ifConstainsEmotionName(String inputString) {
        Cursor cur = sqlm.giveAllEmotions();
        while (cur.moveToNext()) {
            String emotion = cur.getString(1);
            if (inputString.contains(emotion))
                return true;
        }
        return false;
    }



    private void extractFromDrawable(Field field, String dir, String fileExt, Bitmap.CompressFormat format) throws IOException {
    //TODO zapisuje prawdopodbnie do katalogu ?? zmiana rozdzielczości
        String emotName = field.getName();
        int resID = getResources().getIdentifier(emotName, "drawable", getPackageName());
        String path = root + "FriendlyEmotions/" + dir + File.separator;
        File file = new File(path, emotName + fileExt);
        FileOutputStream outStream = new FileOutputStream(file);

        if (format != null) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), resID);
            bm.compress(format, 100, outStream);
            outStream.flush();
            outStream.close();

        } else {
            resID = getResources().getIdentifier(emotName, "raw", getPackageName());
            InputStream in = getResources().openRawResource(resID);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    outStream.write(buff, 0, read);
                }
            } finally {
                in.close();
                outStream.close();
            }
        }
    }




    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            sqlm.updateCurrentLang(localeName);
            Intent refresh = new Intent(this, ActivityChoice.class);
            refresh.putExtra(SplashActivity.CURRENT_LANG, localeName);
            startActivity(refresh);
            finish();
        } else {
            Toast.makeText(ActivityChoice.this, R.string.selected_language, Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

}
