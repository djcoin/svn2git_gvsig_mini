/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *   Prodevelop, S.L.
 *   Pza. Don Juan de Villarrasa, 14 - 5
 *   46001 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   prode@prodevelop.es
 *   http://www.prodevelop.es
 *
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.search.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.search.MapPreview;
import es.prodevelop.gvsig.mini.search.POICategoryIcon;
import es.prodevelop.gvsig.mini.search.POIItemClickContextListener;
import es.prodevelop.gvsig.mini.utiles.Utilities;

public class POIDetailsActivity extends Activity {

	public final static String CAT = "CAT";
	public final static String SCAT = "SCAT";
	public final static String INFO = "INFO";
	public final static String PHONE = "PHONE";
	public final static String MAIL = "MAIL";
	public final static String URL = "URL";
	public final static String WEB = "WEB";
	public final static String WIKI = "WIKI";
	public final static String IMG = "IMG";
	public final static String ADDR = "ADDR";
	public final static String DESC = "DESC";
	public final static String DIST = "DIST";
	public final static String X = "X";
	public final static String Y = "Y";

	private OsmPOI poi;
	private String dist = "";
	private String selectedText = "";
	private POIItemClickContextListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		poi = new OsmPOI();
		final Intent i = getIntent();

		final String cat = i.getStringExtra(CAT);
		if (cat != null)
			poi.setCategory(cat);

		final String scat = i.getStringExtra(SCAT);
		if (scat != null)
			poi.setSubcategory(scat);

		final String info = i.getStringExtra(INFO);
		if (info != null)
			poi.setInfo(info);

		final String phone = i.getStringExtra(PHONE);
		if (phone != null)
			poi.setPhone(phone);

		final String mail = i.getStringExtra(MAIL);
		if (mail != null)
			poi.setEmail(mail);

		final String url = i.getStringExtra(URL);
		if (url != null)
			poi.setUrl(url);

		final String web = i.getStringExtra(WEB);
		if (web != null)
			poi.setWebsite(web);

		final String wiki = i.getStringExtra(WIKI);
		if (wiki != null)
			poi.setWikipedia(wiki);

		final String addr = i.getStringExtra(ADDR);
		if (addr != null)
			poi.setAddress(addr);

		final String img = i.getStringExtra(IMG);
		if (img != null)
			poi.setImage(img);

		final String desc = i.getStringExtra(DESC);
		if (desc != null)
			poi.setDescription(desc);

		dist = i.getStringExtra(DIST);

		final double x = i.getDoubleExtra(X, 0);
		poi.setX(x);

		final double y = i.getDoubleExtra(Y, 0);
		poi.setY(y);

		LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.poi_details, null);

		TextView descTV = (TextView) layout.findViewById(R.id.desc);
		TextView distTV = (TextView) layout.findViewById(R.id.dist);
		ImageView poiImg = (ImageView) layout.findViewById(R.id.img);
		Button bt = (Button) layout.findViewById(R.id.show_options);
		bt.setVisibility(View.VISIBLE);

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getPOItemClickListener().onPOIClick(-1, poi);
			}
		});

		LinearLayout l = new LinearLayout(this);
		final RelativeLayout.LayoutParams zzParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		zzParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		zzParams.addRule(RelativeLayout.CENTER_VERTICAL);
		// zzParams.setMargins(20, 20, 20, 20);
		MapPreview preview;
		try {
			preview = new MapPreview(this, CompatManager.getInstance()
					.getRegisteredContext(), metrics.widthPixels,
					metrics.heightPixels / 2);
			l.addView(preview);
			((LinearLayout) ((LinearLayout) layout)
					.findViewById(R.id.map_preview)).addView(l, zzParams);

			preview.setMapCenterFromLonLat(poi);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String descPOI = Utilities
				.capitalizeFirstLetters((poi.getDescription() != null) ? poi
						.getDescription() : "?");

		descTV.setText(descPOI);
		applyOptions(descTV);

		distTV.setText(getResources().getString(R.string.distance) + " " + dist);

		Bitmap b = POICategoryIcon.getBitmap32ForCategory(((OsmPOI) poi)
				.getCategory());

		poiImg.setImageBitmap(b);

		// set category
		TextView t = (TextView) layout.findViewById(R.id.t_cat_value);

		if (poi.getCategory().length() > 0) {
			t.setText(Utilities.capitalizeFirstLetters(poi.getCategory()
					.replaceAll("_", " ")));
			t.setVisibility(View.VISIBLE);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_cat);
			t.setVisibility(View.GONE);
		}

		// set subcategory
		t = (TextView) layout.findViewById(R.id.t_scat_value);

		if (poi.getSubcategory().length() > 0) {
			t.setText(Utilities.capitalizeFirstLetters(poi.getSubcategory()
					.replaceAll("_", " ")));
			t.setVisibility(View.VISIBLE);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_scat);
			t.setVisibility(View.GONE);
		}

		// set info
		t = (TextView) layout.findViewById(R.id.t_info_value);

		if (poi.getInfo().length() > 0) {
			t.setText(poi.getInfo());
			t.setVisibility(View.VISIBLE);
			registerForContextMenu(t);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_info);
			t.setVisibility(View.GONE);
		}

		// set phone
		t = (TextView) layout.findViewById(R.id.t_phone_value);

		if (poi.getPhone().length() > 0) {
			t.setText(poi.getPhone());
			applyOptions(t);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_phone);
			t.setVisibility(View.GONE);
		}

		// set mail
		t = (TextView) layout.findViewById(R.id.t_mail_value);

		if (poi.getEmail().length() > 0) {
			t.setText(poi.getEmail());
			applyOptions(t);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_mail);
			t.setVisibility(View.GONE);
		}

		// set url
		t = (TextView) layout.findViewById(R.id.t_url_value);

		if (poi.getUrl().length() > 0) {
			t.setText(poi.getUrl());
			applyOptions(t);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_url);
			t.setVisibility(View.GONE);
		}

		// set web
		t = (TextView) layout.findViewById(R.id.t_web_value);

		if (poi.getWebsite().length() > 0) {
			t.setText(poi.getWebsite());
			applyOptions(t);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_web);
			t.setVisibility(View.GONE);
		}

		// set wiki
		t = (TextView) layout.findViewById(R.id.t_wiki_value);

		if (poi.getWikipedia().length() > 0) {
			t.setText(poi.getWikipedia());
			applyOptions(t);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_wiki);
			t.setVisibility(View.GONE);
		}

		// set img
		t = (TextView) layout.findViewById(R.id.t_img_value);

		if (poi.getImage().length() > 0) {
			t.setText(poi.getImage());
			applyOptions(t);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_img);
			t.setVisibility(View.GONE);
		}

		// set address
		t = (TextView) layout.findViewById(R.id.t_addr_value);

		if (poi.getAddress().length() > 0) {
			t.setText(poi.getAddress());
			applyOptions(t);
		} else {
			t.setVisibility(View.GONE);
			t = (TextView) layout.findViewById(R.id.t_addr);
			t.setVisibility(View.GONE);
		}

		// set X
		t = (TextView) layout.findViewById(R.id.t_xy_value);

		t.setText(String.valueOf(poi.getX()) + "," + String.valueOf(poi.getY()));
		t.setVisibility(View.VISIBLE);
		registerForContextMenu(t);

		setContentView(layout);
	}

	// private Spanned linkifyURL(String url) {
	// try {
	// if (url.toLowerCase().contains("http")
	// || url.toLowerCase().contains("www")) {
	// return Html.fromHtml("<a href=\"" + url + "\"" + ">" + url
	// + "</a>");
	// }
	// return new SpannableString(url);
	// } catch (Exception e) {
	// return new SpannableString(url);
	// }
	// }
	//
	// private Spanned linkifyPhone(String phone) {
	// try {
	// SpannableString ss = new SpannableString(phone);
	//
	// ss.setSpan(new URLSpan("tel:" + phone), 0, phone.length(),
	// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	//
	// return ss;
	//
	// } catch (Exception e) {
	// return new SpannableString(phone);
	// }
	// }

	private void applyOptions(TextView t) {
		t.setVisibility(View.VISIBLE);
		t.setMovementMethod(LinkMovementMethod.getInstance());
		Linkify.addLinks(t, Linkify.ALL);
		registerForContextMenu(t);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		selectedText = ((TextView) v).getText().toString();
		menu.setHeaderTitle(selectedText);
		menu.add(0, v.getId(), 0, getResources().getString(R.string.copy));
		menu.add(0, v.getId(), 1, getResources().getString(R.string.share_text));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getOrder()) {
		case 0:
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(selectedText);
			break;
		case 1:
			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, selectedText);
			Intent i = Intent.createChooser(intent,
					getResources().getString(R.string.share_text));
			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//
			startActivityForResult(i, 2385);
			break;
		}
		return true;
	}

	private POIItemClickContextListener getPOItemClickListener() {
		if (listener == null) {
			listener = new POIItemClickContextListener(this, 0, 0);
		}
		return listener;
	}

}
