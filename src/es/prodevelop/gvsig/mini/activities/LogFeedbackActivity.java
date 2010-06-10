package es.prodevelop.gvsig.mini.activities;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.app.Initializer;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * Base activity to manage log feedback
 * @author aromeu
 *
 */
public class LogFeedbackActivity extends Activity {
	
	private final static Logger log = Logger.getLogger(LogFeedbackActivity.class.getName());
	
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			try {
				clearLogs();
				Initializer.getInstance().initialize(
						getApplicationContext());				
				CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
			} catch (BaseException e) {
				
			}
			
//			String logFile = exitedCorrectly();
//			if (logFile != null) {
//				this.showSendLogDialog();
//			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "onCreate", e);
			LogFeedbackActivity.showSendLogDialog(this);
		}
	}
	
	public void onDestroy() {
		try {
			super.onDestroy();
			clearLogs();
		} catch (Exception e) {
			log.log(Level.SEVERE, "onCreate", e);
		}		
	}
	
	public void onResume() {
		try {
			super.onResume();
			Initializer.getInstance().initialize(this.getApplicationContext());
		} catch (Exception e) {
			log.log(Level.SEVERE, "onResume", e);
		}
	}
	
	public void showSendLogDialog() {
		try {
			log.log(Level.FINE, "show send log dialog");
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(R.string.warning);
			TextView text = new TextView(this);
			text.setText(R.string.MapLocation_2);

			alert.setView(text);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								sendExceptionEmail(LogFeedbackActivity.this);
							} catch (Exception e) {
								log.log(Level.SEVERE,"",e);
							}
						}
					});

			alert.setNegativeButton(R.string.not_ask,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							clearLogs();
						}
					});

			alert.show();
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}
	
	/**
	 * Clears the directory where logs are stored
	 */
	public void clearLogs() {
		try {
			File f = new File(Environment.getExternalStorageDirectory() + "/"
					+ Utils.APP_DIR + "/" + Utils.LOG_DIR);
			File[] files = f.listFiles();
			final int length = files.length;
			
			boolean deleted = false;
			for (int i = 0; i<length; i++) {
				deleted = files[i].delete();
			}
			
		    deleted = f.delete();			
		} catch (Exception e) {
			log.log(Level.SEVERE, "clearLogs", e);
		}
	}
	
	public static void showSendLogDialog(final Context context) {
		try {
			showSendLogDialog(context, context.getResources().getString(R.string.fatal_error));
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}
	
	public static void showSendLogDialog(final Context context, int id) {
		try {
			showSendLogDialog(context, context.getResources().getString(id));
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}
	
	public static void showSendLogDialog(final Context context, String message) {
		try {
			AlertDialog.Builder alert = new AlertDialog.Builder(context);

			alert.setTitle(R.string.warning);
			TextView text = new TextView(context);
			text.setText(message);

			alert.setView(text);

			alert.setPositiveButton(R.string.send_log,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								sendExceptionEmail(context);								
							} catch (Exception e) {
//								log.log(Level.SEVERE,"",e);
							}
						}
					});

			alert.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
//							Utils.clearLogs();
						}
					});

			alert.show();
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}
	
	public static String exitedCorrectly() {
		try {
			File f = new File(Environment.getExternalStorageDirectory() + "/"
					+ Utils.APP_DIR + "/" + Utils.LOG_DIR);			
			
			File[] files = f.listFiles();
			final int length = files.length;
			File lastFile = null;			
			long lastModified = 0;
			for (int i = 0; i<length; i++) {
				File tempFile = files[i];
				if (lastModified < tempFile.lastModified()) {
					lastModified = tempFile.lastModified();
					lastFile = files[i];
				}
			}
			return lastFile.getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void sendExceptionEmail(final Context ctx) {
		String lastLog = exitedCorrectly();
		if (lastLog == null) return;
		Utils.openEmail(ctx, "", ctx.getResources().getString(R.string.app_name_itemizedoverlay) + "-Exception",
				new String[] { "minijira@prodevelop.es" }, lastLog);		
	}
}
