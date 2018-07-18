package shuyun.errorcatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalApp app = (LocalApp) getApplication();
        app.addActivity(this);

        //start catching
        CatcherHandler.getInstance(getApplicationContext())
                .addActivity(this)
                .setErrorActivity(ErrorPagerActivity.class)
                .setDelayTimeBeforeFinish(1200)
                .build();

        Button btSenderror = (Button) findViewById(R.id.bt_senderror);
        btSenderror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //throw a null point exception without catching
                Log.e("", null);
            }
        });
    }
}
