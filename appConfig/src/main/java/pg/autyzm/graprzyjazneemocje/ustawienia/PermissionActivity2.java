package pg.autyzm.graprzyjazneemocje.ustawienia;

import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pg.autyzm.graprzyjazneemocje.R;

public class PermissionActivity2 extends AppCompatActivity {
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    String[] PERMISSIONS_API30 = {
            android.Manifest.permission.CAMERA
    };

    List<String> permissionsToBeGranted = new ArrayList<>();

    ActivityResultLauncher<String[]> requestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
        for(Map.Entry<String, Boolean> permission : isGranted.entrySet()){
            if(!permission.getValue()){
                finish();
                System.exit(0);
            }
        }
        Toast.makeText(this, "All permissions GRANTED", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(PermissionActivity2.this, SplashActivity.class));
        finish();
    });


    ActivityResultLauncher<Intent> requestStoragePermissionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == PermissionActivity2.RESULT_OK) {
            Toast.makeText(PermissionActivity2.this, "Storage permission Granted!", Toast.LENGTH_SHORT).show();
        } else {
            finish();
            System.exit(0);
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_background);



        if(SDK_INT >= Build.VERSION_CODES.R) {
            if (!checkStoragePermission()) {
                requestStoragePermission();
            }
            hasNotPermission(this, PERMISSIONS_API30);
        } else {
            hasNotPermission(this, PERMISSIONS);
        }

        if(!permissionsToBeGranted.isEmpty()){
            requestPermissionsLauncher.launch(permissionsToBeGranted.toArray(new String[permissionsToBeGranted.size()]));
        } else {
            startActivity(new Intent(PermissionActivity2.this, SplashActivity.class));
            finish();
        }

    }





    private boolean checkStoragePermission() {
        boolean result = false;
        if (SDK_INT >= Build.VERSION_CODES.R) {
            result = Environment.isExternalStorageManager();
        }

        return result;
    }


    private void requestStoragePermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                requestStoragePermissionLauncher.launch(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private void hasNotPermission(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToBeGranted.add(permission);
                }
            }
        }
    }




}
