package co.danielgarcia.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;

/**
 * Created by Daniel on 25/06/13.
 */
public class UpdateService extends Service {
	private static final String TAG = "UpdateService"; //Instance variable para Log

	static final int DELAY = 6000; //6 seconds
	UpdateThread updateThread;
	boolean runFlag = false;

	@Override
	public void onCreate(){
		super.onCreate();
		Log.v(TAG, "El servicio se ha creado");
	}

	@Override
	public void onDestroy(){

		updateThread.interrupt();
		updateThread = null;

		super.onDestroy();
		Log.v(TAG, "El servicio se ha destruido");
		runFlag = false;
	}

	@Override
	public int onStartCommand(Intent intent, int start, int flags){
		if (!runFlag) {
			updateThread = new UpdateThread();
			updateThread.start();
			Log.v(TAG, "El servicio se ha iniciado");
			runFlag = true;
		}
		return super.onStartCommand(intent, start, flags);
	}

	class UpdateThread extends Thread {
		public void run(){
			UpdateService updateService = UpdateService.this;
			while (updateService.runFlag) {
				try {
					Log.v(TAG, "Estamos interrogando a twitter");
					Twitter twitter = ((YambaApplication) getApplication()).getTwitter();
					List<Twitter.Status> lista = twitter.getFriendsTimeline();
					Thread.sleep(updateService.DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//no utilizado
	public IBinder onBind(Intent intent) {
		return null;
	}

}
