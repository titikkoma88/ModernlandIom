package iom.modernland.co.id;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import ru.nikartm.support.BadgePosition;
import ru.nikartm.support.ImageBadgeView;

public class HomeNPVActivity extends AppCompatActivity {

    private ImageBadgeView imageBadgeView;

    private int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_npv);

        imageBadgeView = findViewById(R.id.ibv_icon6);


        SharedPreferences sp = getSharedPreferences("DATALOGIN", 0);

        String username      = sp.getString("username", "");

        OkHttpClient postman = new OkHttpClient();

        Request request = new Request.Builder()
                .get()
                .url(Setting.IP + "counter_npv.php?username=" + username)
                .build();

        final ProgressDialog pd = new ProgressDialog(HomeNPVActivity.this);
        pd.setMessage("Please wait");
        pd.setTitle("Loading ...");
        pd.setIcon(R.drawable.ic_check_black_24dp);
        pd.setCancelable(false);
        pd.show();

        postman.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Please Try Again",
                                Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String hasil = response.body().string();
                try {
                    JSONObject j = new JSONObject(hasil);
                    boolean st = j.getBoolean("status");
                    final int total = j.getInt("total");

                    if(st == false)
                    {
                        final String p = j.getString("pesan");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        p, Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();

                                initIconWithBadges(total);

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    LinearLayout menuUserLogout = (LinearLayout) findViewById(R.id.menuUserLogoutNPV);
    LinearLayout menuUserExit = (LinearLayout) findViewById(R.id.menuUserExitNPV);

        menuUserLogout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder ab = new AlertDialog.Builder(HomeNPVActivity.this);

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
                    SharedPreferences spl = (HomeNPVActivity.this)
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
                            HomeNPVActivity.this
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

                                            Intent i = new Intent(HomeNPVActivity.this, LoginActivity.class);
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

        menuUserExit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder ab = new AlertDialog.Builder(HomeNPVActivity.this);

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

    public void ApprovePembelian(View view) {

        Intent i = new Intent(HomeNPVActivity.this, ContentApproveNPVActivity.class);
        startActivity(i);

    }

    public void listPembelian(View view) {

        Intent i = new Intent(HomeNPVActivity.this, ContentListNpvActivity.class);
        startActivity(i);

        // AlertDialog.Builder alert = new AlertDialog.Builder(HomeNPVActivity.this);
        // alert.setMessage("Sorry.. This Content Under Construction");
        // alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
        //    @Override
        //    public void onClick(DialogInterface dialog, int which) {

        //    }
        // });
        // alert.create().show();

    }

    private void initIconWithBadges(int total) {
        value = total;
        Typeface typeface = Typeface.createFromAsset(getAssets(), "exo_regular.ttf");
        imageBadgeView.setBadgeValue(value)
                .setBadgeOvalAfterFirst(true)
                .setBadgeTextSize(16)
                .setMaxBadgeValue(999)
                .setBadgeTextFont(typeface)
                .setBadgeBackground(getResources().getDrawable(R.drawable.rectangle_rounded))
                .setBadgePosition(BadgePosition.BOTTOM_RIGHT)
                .setBadgeTextStyle(Typeface.NORMAL)
                .setShowCounter(true)
                .setBadgePadding(4);
    }
}
