package iom.modernland.co.id;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ListNPVViewHolder extends RecyclerView.ViewHolder {

    TextView txtKodeUnit, txtCluster, txtNama;

    public ListNPVViewHolder(@NonNull final View itemView) {
        super(itemView);

        txtKodeUnit = (TextView) itemView.findViewById(R.id.txtKdUnitNpv);
        txtCluster = (TextView) itemView.findViewById(R.id.txtClusterNpv);
        txtNama = (TextView) itemView.findViewById(R.id.txtNamaNpv);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentListPBJActivity c = (ContentListPBJActivity) itemView.getContext();

                String kode_unit = txtKodeUnit.getText().toString();
                String cluster = txtCluster.getText().toString();
                String nama = txtNama.getText().toString();

                ListDetailNPVFragment lnp = new ListDetailNPVFragment();

                Bundle b = new Bundle();
                b.putString("kode_unitnya",kode_unit);
                b.putString("cluster",cluster);
                b.putString("nama",nama);

                lnp.setArguments(b);

                c.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameNPV, lnp)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
