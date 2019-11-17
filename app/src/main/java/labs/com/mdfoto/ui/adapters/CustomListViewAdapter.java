package labs.com.mdfoto.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import labs.com.mdfoto.R;
import labs.com.mdfoto.models.Patient;
import labs.com.mdfoto.models.PatientList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emin on 2/18/2016.
 */
public class CustomListViewAdapter extends BaseAdapter implements Filterable{

    private final LayoutInflater inflater;
    Context context;
    private ViewHolder holder;
    private Filter planetFilter;
    ArrayList<PatientList> patientLists;
    ArrayList<PatientList> filterList;


    public CustomListViewAdapter(Context context, ArrayList<PatientList> patientLists) {
       // super(context,0, patientLists);
        this.context = context;
        this.patientLists = patientLists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
            return patientLists.size();
    }

    @Override
    public PatientList getItem(int position) {
        return patientLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return patientLists.get(position).hashCode();
    }

    private class PlanetFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = patientLists;
                results.count = patientLists.size();
            }
            else {
                // We perform filtering operation
                List<PatientList> nPlanetList = new ArrayList<PatientList>();

                for (PatientList p : patientLists) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nPlanetList.add(p);
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                patientLists = (ArrayList<PatientList>) results.values;
                notifyDataSetChanged();
            }

        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.patientId = (TextView) convertView.findViewById(R.id.hasta_id);
            holder.patientAdi= (TextView) convertView.findViewById(R.id.hasta_adi);
            convertView.setTag(holder);

        }
        else{
            //Get viewholder we already created
            holder = (ViewHolder)convertView.getTag();
        }

        PatientList patientList = patientLists.get(position);
        if(patientList != null){

            holder.patientId.setText(String.valueOf(patientList.getId()));
            holder.patientAdi.setText(patientList.getName());

        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }


    //View Holder Pattern for better performance
    private static class ViewHolder {
        TextView patientId;
        TextView patientAdi;

    }
}
