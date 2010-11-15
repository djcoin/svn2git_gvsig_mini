//package es.prodevelop.gvsig.mini.search;
//
//import java.util.ArrayList;
//
//import org.garret.perst.fulltext.FullTextSearchResult;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.util.Log;
//import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
//import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProvider;
//import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
//import es.prodevelop.gvsig.mini.R;
//
//public class SearchAsyncTask extends
//		AsyncTask<ArrayList<String>, String, Integer> {
//
//	private String[] keywords;
//	private Context context;
//	private ProgressDialog pDialog;
//
//	private String keyWordString;
//	static ArrayList list;
//
//	public SearchAsyncTask(Context context, String[] keywords) {
//		this.keywords = keywords;
//		this.context = context;
//		keyWordString = buildKeyWordString(keywords);
//	}
//
//	@Override
//	protected Integer doInBackground(ArrayList<String>... params) {
//		ArrayList<String> categories = params[0];
//		final int size = categories.size();
//
//		String cat;
//		StringBuffer query = new StringBuffer();
//		for (int i = 0; i < size; i++) {
//			cat = categories.get(i);
//			query.append(keyWordString).append(cat).append(" ) ");
//			if (i != size - 1) {
//				query.append(" OR ");
//			}
//		}
//		Log.d("", query.toString());
//		QuadtreeProvider provider = ((SearchActivityWrapper) context)
//				.getProvider();
//
//		SpatialIndexRoot root = ((SpatialIndexRoot) ((PerstOsmPOIProvider) provider)
//				.getHelper().getRoot());
//
//		FullTextSearchResult result = root.getFullTextIndex().search(
//				query.toString(), SpatialIndexRoot.DEFAULT_LANGUAGE,
//				SpatialIndexRoot.DEFAULT_MAX_RESULTS,
//				SpatialIndexRoot.DEFAULT_MAX_TIME);
//
//		final int length = result.hits.length;
//		list = new ArrayList();
//		for (int i = 0; i < length; i++) {
//			list.add(result.hits[i].getDocument());
//		}
//
//		return 0;
//	}
//
//	@Override
//	protected void onCancelled() {
//		super.onCancelled();
//		if (pDialog != null)
//			pDialog.dismiss();
//	}
//
//	@Override
//	protected void onPostExecute(Integer result) {
//		super.onPostExecute(result);
//		if (pDialog != null)
//			pDialog.dismiss();
//		// launch results activity
//		Intent i = new Intent(context, ResultSearchActivity.class);
//		fillCenter(i);
//		i.putExtra(SearchActivity.HIDE_AUTOTEXTVIEW, true);
//		context.startActivity(i);
//	}
//
//	private void fillCenter(Intent i) {
//		i.putExtra("lon", ((SearchActivityWrapper) context).getCenter().getX());
//		i.putExtra("lat", ((SearchActivityWrapper) context).getCenter().getY());
//	}
//
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//		pDialog = ProgressDialog.show(context, context.getResources()
//				.getString(R.string.please_wait), context.getResources()
//				.getString(R.string.perform_search), true);
//	}
//
//	@Override
//	protected void onProgressUpdate(String... values) {
//		// TODO Auto-generated method stub
//		super.onProgressUpdate(values);
//		if (pDialog != null)
//			pDialog.setMessage(values[0]);
//	}
//
//	private String buildKeyWordString(String[] keywords) {
//		if (keywords != null && keywords.length > 0) {
//			final int length = keywords.length;
//			StringBuffer sb = new StringBuffer();
//			sb.append(" ( ");
//			for (int i = 0; i < length; i++) {
//				sb.append(keywords[i]).append(" AND ");
//			}
//			return sb.toString();
//		} else {
//			return "";
//		}
//	}
//
//}
