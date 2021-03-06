package iom.modernland.co.id;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class KordinasiMemoFragment extends Fragment {


    public KordinasiMemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View x = inflater.inflate(R.layout.fragment_kordinasi_memo, container, false);

        final RecyclerView rvKordinasiMemo = (RecyclerView) x.findViewById(R.id.rvKordinasiM);

        SharedPreferences sp = getActivity()
                .getSharedPreferences("DATALOGIN", 0);

        String username      = sp.getString("username", "");

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rvKordinasiMemo.setLayoutManager(lm);

        OkHttpClient postman = new OkHttpClient();

        Request request = new Request.Builder()
                .get()
                .url(Setting.IP + "list_kordinasi.php?username=" + username)
                .build();

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait");
        pd.setTitle("Loading ...");
        pd.setIcon(R.drawable.ic_check_black_24dp);
        pd.setCancelable(false);
        pd.show();

        postman.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
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
                    JSONArray j = new JSONArray(hasil);

                    final KordinasiAdapter adapter = new KordinasiAdapter();
                    adapter.data = new ArrayList<ListMemo>();
                    for (int i = 0;i < j.length();i++)
                    {
                        JSONObject jo = j.getJSONObject(i);
                        ListMemo l = new ListMemo();

                        l.id_iom = jo.getString("id_iom");
                        l.nomor = jo.getString("nomor");
                        l.perihal = jo.getString("perihal");
                        l.status = jo.getString("status");
                        l.status_kor = jo.getString("status_kor");

                        adapter.data.add(l);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            rvKordinasiMemo.setAdapter(adapter);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return x;
    }

}
