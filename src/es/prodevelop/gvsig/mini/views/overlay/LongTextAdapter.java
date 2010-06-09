package es.prodevelop.gvsig.mini.views.overlay;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class LongTextAdapter extends BaseAdapter {

	String text = "hola";
	private boolean editText = false;
	private Context context;

	public LongTextAdapter(Context context, String longText, boolean editText) {
		this.text = longText;
		this.editText = editText;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return text;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		TextView t;
		if (editText) {
			t = new EditText(this.context);
		} else {
			t = new TextView(this.context);
		}
		
		t.setText(text);
		return t;
	}

}
