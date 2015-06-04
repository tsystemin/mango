package in.co.tsystem.mango;

/**
 * Created by diganta.paladhi on 05/04/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            Thread.dumpStack();
        }

        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
