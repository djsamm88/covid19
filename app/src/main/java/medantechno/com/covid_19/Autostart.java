package medantechno.com.covid_19;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class Autostart extends BroadcastReceiver {
    public void onReceive(Context context, Intent arg1)
    {
        //Toast.makeText(context, "Action: " + arg1.getAction(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(context,ServiceUpload.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);

        }

        ContextCompat.startForegroundService(context, intent);
        Log.i("Autostart", "started");
    }
}
