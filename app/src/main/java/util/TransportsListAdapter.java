package util;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import upv.welcomeincoming.app.R;

public class TransportsListAdapter extends ArrayAdapter<Transporte> {
    private Context _context;
    private List<Transporte> Transportes;

    public TransportsListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        _context = context;
    }

    public TransportsListAdapter(Context context, int resource, List<Transporte> items) {
        super(context, resource, items);
        Transportes = items;
    }

    @Override
    public int getCount() {
        return Transportes.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_lista_transportes, null);

        }

        Transporte p = getItem(position);

        if (p != null) {
            TextView title = (TextView) v.findViewById(R.id.tvNombre);
            TextView desc = (TextView) v.findViewById(R.id.tvDesc);
            TextView tlf = (TextView) v.findViewById(R.id.tvTlf);
            if (title != null) {
                title.setText(p.getNombre());
                desc.setText(p.getDescripcion());
                tlf.setText(p.getTelefono()[1]);
                tlf.setAutoLinkMask(Linkify.PHONE_NUMBERS);
            }
        }

        return v;

    }

}