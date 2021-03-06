package iom.modernland.co.id;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<MemoViewHolder> {

    ArrayList<ListMemo> data;

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View l = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_memo, viewGroup, false);

        MemoViewHolder m = new MemoViewHolder(l);

        return m;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder memoViewHolder, int i) {

        ListMemo m = data.get(i);

        if (m.status.equals("T")){

            memoViewHolder.txtStatus.setText("Approved");

        }

        else if (m.status.equals("C")){

            memoViewHolder.txtStatus.setText("Rejected");

        }

        memoViewHolder.txtNomor.setText(m.nomor);
        memoViewHolder.txtID.setText(m.id_iom);
        memoViewHolder.txtPerihal.setText(m.perihal + "");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
