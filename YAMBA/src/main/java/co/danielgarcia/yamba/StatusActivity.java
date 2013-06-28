package co.danielgarcia.yamba;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

public class StatusActivity extends Activity implements OnClickListener {
	private static final String TAG = "StatusActivity"; //Instance variable para Log

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_activity);

		//editText y button
		editText = (EditText) findViewById(R.id.updateEditText);
        button = (Button) findViewById(R.id.updateButton);
        button.setOnClickListener(this);

        //agregar textwatcher (contador de letras)
        StatusTextWatcher textWatcher = new StatusTextWatcher();
        editText.addTextChangedListener(textWatcher);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.status, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        String text = editText.getText().toString();
        new PostOnTwitter().execute(text);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.startService :
				Intent startServiceIntent = new Intent(this, UpdateService.class);
				startService(startServiceIntent);
				break;
			case R.id.stopService :
				Intent stopServiceIntent = new Intent(this, UpdateService.class);
				stopService(stopServiceIntent);
				break;
			case R.id.itemPrefs :
				startActivity(new Intent(this, PrefsActivity.class));
				break;
		}
		return true;
	}

    class StatusTextWatcher implements TextWatcher {
        private int availableChars;
        private int maxChars = 140;
        private int yellowLimit = 20;
        private int redLimit = 5;
        private TextView counter = (TextView) findViewById(R.id.updateCounter);
        private LinearLayout counterLayout = (LinearLayout) findViewById(R.id.counterLayout);

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int count, int after) {
//            Log.i(TAG, Integer.toString(start));
            availableChars = maxChars - s.length();
            button = (Button) findViewById(R.id.updateButton);

            // Change counter text
            counter.setText(Integer.toString(availableChars));

            // Change colors
            if (availableChars <= redLimit) {
                counterLayout.setBackgroundColor(Color.RED);
            } else if (availableChars <= yellowLimit) {
                counterLayout.setBackgroundColor(Color.YELLOW);
            } else {
                counterLayout.setBackgroundColor(Color.TRANSPARENT);
            }

            // Enable/disable send button
            if (availableChars < 0) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

	class PostOnTwitter extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... statuses) {
			try {
				YambaApplication yambaApplication = (YambaApplication) getApplication();
				Twitter twitter = yambaApplication.getTwitter();
				Twitter.Status status = twitter.updateStatus(statuses[0]);
				return status.text;
			} catch (TwitterException e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return "Error al enviar";
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			// no usado en este caso
		}

		@Override
		protected void onPostExecute(String result){
			int duration = Toast.LENGTH_LONG;
			Toast.makeText(StatusActivity.this, result, duration).show();
			editText.setText("");
		}
	}


}
