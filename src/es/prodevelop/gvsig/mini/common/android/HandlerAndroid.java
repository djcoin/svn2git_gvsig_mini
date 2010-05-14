package es.prodevelop.gvsig.mini.common.android;

import android.os.Handler;
import es.prodevelop.gvsig.mini.common.IHandler;

public class HandlerAndroid implements IHandler {

	private Handler handler;

	public HandlerAndroid(Handler handler) {
		this.handler = handler;

	}

	@Override
	public Handler getHandler() {
		return handler;
	}

}
