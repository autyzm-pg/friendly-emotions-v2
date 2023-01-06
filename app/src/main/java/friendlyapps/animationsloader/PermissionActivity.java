package friendlyapps.animationsloader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    private final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    List<String> permissionsToBeGranted = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_background);

        hasNotPermission(this, PERMISSIONS);

        if (!permissionsToBeGranted.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToBeGranted.toArray(new String[permissionsToBeGranted.size()]), PERMISSION_ALL);
        } else {
            startActivity(new Intent(PermissionActivity.this, MainActivity.class));
            finish();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_ALL){
            if(grantResults.length > 0){
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(PermissionActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
                        permissionsToBeGranted.remove(permission);
                    } else {
                        finish();
                        System.exit(0);
                    }
                }

                if(permissionsToBeGranted.isEmpty()){
                    Toast.makeText(this, "All permissions GRANTED", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PermissionActivity.this, MainActivity.class));
                    finish();
                }
            }
        }
    }

}
