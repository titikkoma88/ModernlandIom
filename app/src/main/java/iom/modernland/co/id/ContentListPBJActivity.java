package iom.modernland.co.id;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContentListPBJActivity extends AppCompatActivity {

    FloatingActionMenu materialDesignFamL;
    FloatingActionButton fabBackL, fabLogoutL, fabExitL, fabMainL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list_pbj);

        getSupportFragmentManager().beginTransaction().replace(R.id.framePermohonanBJ, new ListPBJFragment())
                .addToBackStack(null)
                .commit();

        materialDesignFamL = (FloatingActionMenu) findViewById(R.id.menuFamLP);
        fabLogoutL = (FloatingActionButton) findViewById(R.id.menuFabLogoutLP);
        fabMainL = (FloatingActionButton) findViewById(R.id.menuFabMainLP);
        fabExitL = (FloatingActionButton) findViewById(R.id.menuFabExitLP);

        fabMainL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        HomePermohonanActivity.class);

                startActivity(i);
            }
        });

        fabLogoutL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ContentListPBJActivity.this);

                ab.create();
                ab.setTitle("Confirmation");
                ab.setIcon(R.drawable.ic_check_black_24dp);
                ab.setMessage("Are you sure to logout?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // buka postman
                        OkHttpClient postman = new OkHttpClient();

                        // body
                        SharedPreferences spl = (ContentListPBJActivity.this)
                                .getSharedPreferences("DATALOGIN", 0);

                        String username      = spl.getString("username", "");

                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("username", username)
                                .build();

                        // request (POST + tujuan)
                        Request request = new Request.Builder()
                                .post(body)
                                .url(Setting.IP + "proses_logout.php")
                                .build();

                        // progress dialog
                        final ProgressDialog pdl = new ProgressDialog(
                                ContentListPBJActivity.this
                        );
                        pdl.setMessage("Please Wait");
                        pdl.setTitle("Loading ...");
                        pdl.setIcon(R.drawable.ic_check_black_24dp);
                        pdl.show();

                        // send
                        postman.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Please try again",
                                                Toast.LENGTH_LONG).show();
                                        pdl.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String hasil = response.body().string();
                                try {
                                    JSONObject j = new JSONObject(hasil);
                                    boolean st = j.getBoolean("status");

                                    if(st == false)
                                    {
                                        final String p = j.getString("pesan");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),
                                                        p, Toast.LENGTH_LONG).show();
                                                pdl.dismiss();
                                            }
                                        });
                                    }
                                    else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),
                                                        "Berhasil Logout",
                                                        Toast.LENGTH_LONG).show();
                                                pdl.dismiss();

                                                getSharedPreferences("DATALOGIN", MODE_PRIVATE)
                                                        .edit().clear().commit();

                                                Intent i = new Intent(ContentListPBJActivity.this, LoginActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                finish();
                                            }
                                        });

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                ab.show();

            }
        });

        fabExitL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ContentListPBJActivity.this);

                ab.create();
                ab.setTitle("Confirmation");
                ab.setIcon(R.drawable.ic_check_black_24dp);
                ab.setMessage("Are you sure to exit?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finishAffinity();

                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                ab.show();

            }
        });
    }
}
