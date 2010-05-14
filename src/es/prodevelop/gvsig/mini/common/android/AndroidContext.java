package es.prodevelop.gvsig.mini.common.android;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import es.prodevelop.gvsig.mini.common.IBitmap;
import es.prodevelop.gvsig.mini.common.IContext;
import es.prodevelop.gvsig.mini.common.IHandler;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.util.Utils;

public class AndroidContext implements IContext {

	Context context;

	public AndroidContext(Context context) {
		this.context = context;
	}

	@Override
	public InputStream openAssetFile(String fileName) throws IOException {
		return context.getAssets().open(fileName);
	}

	@Override
	public IBitmap decodeByteArray(byte[] data, int offset, int length) {
		return new BitmapAndroid(BitmapFactory.decodeByteArray(data, offset,
				length));
	}

	@Override
	public IBitmap getBitmapFromResource(int ID) {
		return new BitmapAndroid(ResourceLoader.getBitmap(ID));
	}

	@Override
	public String getExternalStorageDirectoryPath() {
		return Environment.getExternalStorageDirectory().getPath();
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

}
