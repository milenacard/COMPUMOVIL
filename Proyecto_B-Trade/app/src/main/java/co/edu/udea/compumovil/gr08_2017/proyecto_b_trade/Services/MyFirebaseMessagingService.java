package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Services;

import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;

/**
 * Created by Daniel on 04/06/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);

            Log.d("NOTIFICACIÓN INTERES", "Message Notification Body: " + remoteMessage.getNotification().getBody());


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_b_trade)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(remoteMessage.getNotification().getBody());
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());



    }
}
