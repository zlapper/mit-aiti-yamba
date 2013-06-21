package co.danielgarcia.yamba;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;

public class StatusActivity extends Activity implements OnClickListener {
    EditText editText;
    Button button;
    Twitter twitter;
    private static final String TAG = "YAMBA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_activity);

        button = (Button) findViewById(R.id.updateButton);
        button.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.updateEditText);

        //inicializar twitter
        twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://yamba.marakana.com/api");

        //agregar textwatcher
        StatusTextWatcher textWatcher = new StatusTextWatcher();
        editText.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        String text = editText.getText().toString();
        new PostOnTwitter().execute(text);
    }
    
    class PostOnTwitter extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String[] strings) {
            twitter.setStatus(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void arg){
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(StatusActivity.this, getApplicationContext().getResources().getString(R.string.update_confirm), duration).show();
            editText.setText("");
        }
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
}
