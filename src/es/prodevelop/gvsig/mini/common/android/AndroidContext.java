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
import es.prodevelop.gvsig.mini.common.IBitmap;
import es.prodevelop.gvsig.mini.common.IContext;
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
	public IBitmap decodeByteArray(byte[] data, int offset, int length) throws IOException {		
		Bitmap b = BitmapFactory.decodeByteArray(data, offset,
				length);
		if (b == null) throw new IOException("BitmapFactory failed");
		return new BitmapAndroid(b);
	}

	@Override
	public IBitmap getBitmapFromResource(int ID) {
		return new BitmapAndroid(ResourceLoader.getBitmap(ID));
	}

	@Override
	public String getExternalStorageDirectoryPath() {
		if (externalStoragePath == null)
			this.externalStoragePath = Environment.getExternalStorageDirectory().getPath();
		return this.externalStoragePath;
	}

	@Override
	public boolean isSDMounted() {
		return Utils.isSDMounted();
	}

	@Override
	public void sendMessage(IHandler handler, String message, int ID) {
		Handler h = (Handler) handler.getHandler();
		Message.obtain(h, ID, message).sendToTarget();
	}

	@Override
	public void sendMessage(IHandler handler, int ID) {
		((Handler) handler.getHandler()).sendEmptyMessage(ID);
	}

	@Override
	public String getBaseLayerFilePath() {
		String SDDIR = Environment.getExternalStorageDirectory().getPath();
		return new StringBuffer(SDDIR).append(File.separator).append(
				Utils.APP_DIR).append(File.separator).append(Utils.LAYERS_DIR)
				.append(File.separator).toString();
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
		if (b == null) throw new IOException("BitmapFactory failed");
		return new BitmapAndroid(b);
	}

	@Override
	public IBitmap decodeByteArray(String filePath) throws IOException {
		Bitmap b = BitmapFactory.decodeFile(filePath);
		if (b == null) throw new IOException("BitmapFactory failed");
		return new BitmapAndroid(b);
	}
}
