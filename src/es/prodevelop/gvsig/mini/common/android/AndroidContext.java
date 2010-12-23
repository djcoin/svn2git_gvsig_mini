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

package es.prodevelop.gvsig.mini.common.android;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.IBitmap;
import es.prodevelop.gvsig.mini.common.IContext;
import es.prodevelop.gvsig.mini.common.IEvent;
import es.prodevelop.gvsig.mini.common.IHandler;
import es.prodevelop.gvsig.mini.common.IRect;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.util.Utils;

public class AndroidContext implements IContext {

	Context context;
	private String externalStoragePath;

	public AndroidContext(Context context) {
		this.context = context;
	}

	@Override
	public InputStream openAssetFile(String fileName) throws IOException {
		return context.getAssets().open(fileName);
	}

	@Override
	public IBitmap decodeByteArray(byte[] data, int offset, int length)
			throws IOException {
		Bitmap b = BitmapFactory.decodeByteArray(data, offset, length);
		if (b == null)
			throw new IOException("BitmapFactory failed");
		return new BitmapAndroid(b);
	}

	@Override
	public IBitmap getBitmapFromResource(int ID) {
		BitmapAndroid b = new BitmapAndroid(ResourceLoader.getBitmap(ID));
		if (b != null)
			b.isValid = false;
		return b;
	}

	@Override
	public String getExternalStorageDirectoryPath() {
		if (externalStoragePath == null)
			this.externalStoragePath = Environment
					.getExternalStorageDirectory().getPath();
		return this.externalStoragePath;
	}

	@Override
	public boolean isSDMounted() {
		return Utils.isSDMounted();
	}

	@Override
	public synchronized void sendMessage(IHandler handler, IEvent event, int ID) {
		Handler h = (Handler) handler.getHandler();
		Message.obtain(h, ID, event).sendToTarget();
	}

	@Override
	public synchronized void sendMessage(IHandler handler, int ID) {
		((Handler) handler.getHandler()).sendEmptyMessage(ID);
	}

	@Override
	public String getBaseLayerFilePath() {
		String SDDIR = Environment.getExternalStorageDirectory().getPath();
		return new StringBuffer(SDDIR).append(File.separator).append(
				Utils.APP_DIR).append(File.separator).append(Utils.LAYERS_DIR)
				.append(File.separator).append("layers.txt").toString();
	}

	@Override
	public String getBaseLayerPersistFilePath() {
		return this.getBaseLayerFilePath();
	}

	@Override
	public IRect getRectangle() {
		return new RectangleAndroid();
	}

	@Override
	public void setExternalStorageDirectoryPath(String path) {
		this.externalStoragePath = path;
	}

	@Override
	public IBitmap decodeByteArray(InputStream in) throws IOException {
		Bitmap b = BitmapFactory.decodeStream(in);
		if (b == null)
			throw new IOException("BitmapFactory failed");
		return new BitmapAndroid(b);
	}

	@Override
	public IBitmap decodeByteArray(String filePath) throws IOException {
		Bitmap b = BitmapFactory.decodeFile(filePath);
		if (b == null)
			throw new IOException("BitmapFactory failed");
		return new BitmapAndroid(b);
	}

	@Override
	public int getDefaultLoadingTileID() {
		return R.drawable.maptile_loading;
	}

	@Override
	public int getDefaultOfflineTileID() {
		return R.drawable.maptile_loadingoffline;
	}

	@Override
	public IBitmap createBitmap(int width, int height) {
		return new BitmapAndroid(Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565));
	}
}
