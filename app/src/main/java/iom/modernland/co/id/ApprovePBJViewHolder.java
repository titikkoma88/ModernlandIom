package iom.modernland.co.id;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ApprovePBJViewHolder extends RecyclerView.ViewHolder {

    TextView txtNoPermo, txtJenisPermo, txtStatusPermo;

    public ApprovePBJViewHolder(@NonNull final View itemView) {
        super(itemView);

        txtNoPermo = (TextView) itemView.findViewById(R.id.txtNoPermoA);
        txtJenisPermo = (TextView) itemView.findViewById(R.id.txtJenisPermoA);
        txtStatusPermo = (TextView) itemView.findViewById(R.id.txtStatusPermoA);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentApprovePBJActivity c = (ContentApprovePBJActivity) itemView.getContext();

                String nomor = txtNoPermo.getText().toString();
                String jenis = txtJenisPermo.getText().toString();

                ApproveDetailPBJFragment ad = new ApproveDetailPBJFragment();

                Bundle b = new Bundle();
                b.putString("jenisnya",jenis);
                b.putString("no_permintaan",nomor);

                ad.setArguments(b);

                c.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameApprovePBJ, ad)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }
}
