package es.prodevelop.gvsig.mini.search.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.search.activities.SearchActivityWrapper;
import es.prodevelop.gvsig.mini.search.filter.AutoCompleteFilter;
import es.prodevelop.gvsig.mini.utiles.Utilities;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

	private AutoCompleteFilter mFilter;
	ArrayList list = null;

	SearchActivityWrapper activity;

	public AutoCompleteAdapter(SearchActivityWrapper activity) {
		this.activity = activity;
	}

	@Override
	public int getCount() {
		if (list != null)
			return list.size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if (list != null) {
			return list.get(arg0);
		}

		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (list != null) {
			((Activity) activity).setProgressBarIndeterminateVisibility(false);
			((Activity) activity).setTitle("");
		}
		ViewHolder holder;

		// When convertView is not null, we can reuse it directly, there is
		// no need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.
		if (convertView == null) {
			convertView = ((Activity) activity).getLayoutInflater().inflate(
					R.layout.street_row, null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		String desc = Utilities
				.capitalizeFirstLetters(getItem(arg0).toString());

		// Bind the data efficiently with the holder.
		holder.text.setTextColor(Color.BLACK);
		holder.text.setTextSize(16);
		holder.text.setText(desc);

		return convertView;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new AutoCompleteFilter(this.activity);
		}
		return mFilter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#notifyDataSetChanged()
	 */
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#notifyDataSetInvalidated()
	 */
	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
	}

	class ViewHolder {
		TextView text;
	}

	public ArrayList getList() {
		return list;
	}

	public void setList(ArrayList list) {
		this.list = list;
	}

}
