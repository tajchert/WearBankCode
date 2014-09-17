package pl.tajchert.wearbank.codes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.wrapp.floatlabelededittext.FloatLabeledEditText;


public class MyActivity extends Activity {
    private FloatLabeledEditText number;
    private FloatLabeledEditText beforeCode;
    private FloatLabeledEditText afterCode;

    private SharedPreferences prefs;

    public static final String KEY_NUMBER = "number_key";
    public static final String KEY_BEFORE = "before_key";
    public static final String KEY_AFTER = "after_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        number = (FloatLabeledEditText) findViewById(R.id.editTextNumber);
        beforeCode = (FloatLabeledEditText) findViewById(R.id.editTextBefore);
        afterCode = (FloatLabeledEditText) findViewById(R.id.editTextAfter);

        prefs = this.getSharedPreferences("pl.tajchert.wearbank.codes", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(prefs != null){
            if(number != null && beforeCode != null && afterCode != null){
                number.setText(prefs.getString(KEY_NUMBER, ""));
                beforeCode.setText(prefs.getString(KEY_BEFORE, ""));
                afterCode.setText(prefs.getString(KEY_AFTER, ""));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(prefs != null && number != null && beforeCode != null && afterCode != null) {
            prefs.edit().putString(KEY_NUMBER, number.getTextString()).apply();
            prefs.edit().putString(KEY_BEFORE, beforeCode.getTextString()).apply();
            prefs.edit().putString(KEY_AFTER, afterCode.getTextString()).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_save){
            //as onPause save just leave Activity
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
