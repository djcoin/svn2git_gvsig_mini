package es.prodevelop.gvsig.mini.context;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import es.prodevelop.gvsig.mini.common.IContext;

public class AndroidContext implements IContext {
	
	Context context;
	
	public AndroidContext(Context context) {
		this.context = context;
	}

	@Override
	public InputStream openAssetFile(String fileName) throws IOException {
		return context.getAssets().open(fileName);
	}

}
