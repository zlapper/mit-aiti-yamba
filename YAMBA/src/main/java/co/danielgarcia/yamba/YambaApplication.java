package co.danielgarcia.yamba;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import winterwell.jtwitter.Twitter;

/**
 * Created by Daniel on 25/06/13.
 */
public class YambaApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
	SharedPreferences prefs;
	Twitter twitter;

	@Override
	public void onCreate(){
		//acceder a las preferencias
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this); //registrarse como listener para cuando cambien las preferencias

	}

	public Twitter getTwitter() {
		if (twitter == null) {
			String username, password, apiRoot;
			username = prefs.getString("username", "student");
			password = prefs.getString("password", "password");
			apiRoot = prefs.getString("apiRoot", "http://yamba.marakana.com/api");

			// Connect to twitter service
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(apiRoot);
		}
		return twitter;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
		// invalidate twitter object
		twitter = null;
	}


}
