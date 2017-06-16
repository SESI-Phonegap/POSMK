package chris.sesi.com.minegociomk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Chris on 03/01/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {
     String[] spinnerValues;
     Context mcontext;

    public SpinnerAdapter(Context ctx, int txtViewResourceId,String[] spinnerValues) {
        super(ctx, txtViewResourceId, spinnerValues);
        this.spinnerValues = spinnerValues;
        this.mcontext = ctx;
    }
    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt);
    }
    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt);
    }
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.spinner_row_view_cantidad, parent, false);
        TextView cantidad = (TextView) mySpinner.findViewById(R.id.spinner_row_cantidad);
        cantidad.setText(spinnerValues[position]);
        return mySpinner;

    }

}
