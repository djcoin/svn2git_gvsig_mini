package es.prodevelop.gvsig.mini.search;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;

public class CheckboxExpandableListAdapter extends SimpleExpandableListAdapter {

	boolean[] parentChecked;
	boolean[][] childChecked;
	private Context context;

	public CheckboxExpandableListAdapter(Context context,
			List<? extends Map<String, ?>> groupData, int groupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom, int[] childTo) {
		super(context, groupData, groupLayout, groupFrom, groupTo, childData,
				childLayout, childFrom, childTo);
		this.context = context;
		parentChecked = new boolean[groupData.size()];

		int maxLength = 0;
		final int size = childData.size();
		List l;
		for (int i = 0; i < size; i++) {
			l = childData.get(i);
			final int length = l.size();
			if (length > maxLength)
				maxLength = length;
		}
		childChecked = new boolean[size][maxLength];
	}

	public void toggleParent(int position) {
		boolean checked = parentChecked[position];
		parentChecked[position] = !checked;

		final int length = childChecked[position].length;

		for (int i = 0; i < length; i++) {
			childChecked[position][i] = !checked;
		}

		notifyDataSetChanged();
	}

	public void toggleChild(int parentPos, int childPos) {
		this.setCheckedChild(parentPos, childPos,
				!childChecked[parentPos][childPos]);

	}

	public void setCheckedParent(int position, boolean checked) {
		parentChecked[position] = checked;

		notifyDataSetChanged();
	}

	public void setCheckedChild(int parentPos, int childPos, boolean checked) {

		childChecked[parentPos][childPos] = checked;

		final int length = childChecked[parentPos].length;

		boolean allTheSame = true;
		boolean anyIsTrue = false;
		for (int i = 0; i < length; i++) {
			boolean temp = childChecked[parentPos][i];
			if (temp == checked) {
				allTheSame = true;
			} else {
				allTheSame = false;
				break;
			}
		}

		for (int i = 0; i < length; i++) {
			boolean temp = childChecked[parentPos][i];
			if (temp) {
				anyIsTrue = true;
				break;
			}
		}

		if (anyIsTrue)
			parentChecked[parentPos] = true;
		else if (allTheSame)
			parentChecked[parentPos] = checked;

		notifyDataSetChanged();

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return super.getChild(groupPosition, childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return super.getChildId(groupPosition, childPosition);
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		LinearLayout v = (LinearLayout) super.getChildView(groupPosition,
				childPosition, isLastChild, convertView, parent);
		ImageButton button = (ImageButton) v.findViewById(R.id.bt_details_sub);
		button.setFocusable(false);

		CheckBox check = (CheckBox) v.getChildAt(0);

		// TextView t = (TextView) parent.findViewById(R.id.cat);
		// final String cat = t.getText().toString().replaceAll(" ", "_")
		// .toLowerCase();

		final String sub = ((TextView) v.findViewById(R.id.sub)).getText()
				.toString().replaceAll(" ", "_").toLowerCase();

		Metadata.Category cat = ((SearchExpandableActivity) context).provider
				.getPOIMetadata().getCategoryForSubcategory(sub);

		if (cat != null)
			button.setImageDrawable(POICategoryIcon.getDrawable16ForCategory(
					cat.name, context));

		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				CheckboxExpandableListAdapter.this.setCheckedChild(
						groupPosition, childPosition, isChecked);
			}

		});

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, POISearchActivity.class);
				// i.putExtra(SearchActivity.CATEGORY, cat);
				i.putExtra(SearchActivity.SUBCATEGORY, sub);
				context.startActivity(i);
			}
		});

		check.setChecked(childChecked[groupPosition][childPosition]);

		return v;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return super.getChildrenCount(groupPosition);
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return super.getGroup(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return super.getGroupCount();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return super.getGroupId(groupPosition);
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		LinearLayout v = (LinearLayout) super.getGroupView(groupPosition,
				isExpanded, convertView, parent);
		ImageButton button = (ImageButton) v.findViewById(R.id.bt_details_cat);
		button.setFocusable(false);

		CheckBox check = (CheckBox) v.getChildAt(0);
		TextView t = (TextView) v.findViewById(R.id.cat);
		final String cat = t.getText().toString().replaceAll(" ", "_")
				.toLowerCase();

		button.setImageDrawable(POICategoryIcon.getDrawable32ForCategory(cat,
				context));

		if (cat.compareToIgnoreCase(POICategories.STREETS) == 0)
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(context, StreetSearchActivity.class);
					i.putExtra(SearchActivity.CATEGORY, POICategories.STREETS);
					context.startActivity(i);
				}
			});
		else
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(context, POISearchActivity.class);
					i.putExtra(SearchActivity.CATEGORY, cat);
					context.startActivity(i);
				}
			});

		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked == parentChecked[groupPosition]) {
					CheckboxExpandableListAdapter.this.setCheckedParent(
							groupPosition, isChecked);
				} else {
					CheckboxExpandableListAdapter.this
							.toggleParent(groupPosition);
				}

			}

		});

		check.setChecked(parentChecked[groupPosition]);

		return v;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return super.hasStableIds();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return super.isChildSelectable(groupPosition, childPosition);
	}

	@Override
	public View newChildView(boolean isLastChild, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.newChildView(isLastChild, parent);
	}

	@Override
	public View newGroupView(boolean isExpanded, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.newGroupView(isExpanded, parent);
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return super.areAllItemsEnabled();
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return super.getCombinedChildId(groupId, childId);
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return super.getCombinedGroupId(groupId);
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return super.isEmpty();
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		super.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		super.unregisterDataSetObserver(observer);
	}

}
