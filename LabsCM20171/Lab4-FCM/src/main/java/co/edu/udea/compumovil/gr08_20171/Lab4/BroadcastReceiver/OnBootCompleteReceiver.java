package co.edu.udea.compumovil.gr08_20171.Lab4.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import co.edu.udea.compumovil.gr08_20171.Lab4.services.ServiceActualizarEventos;

public class OnBootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Intent intentServicio = new Intent(context, ServiceActualizarEventos.class);
        //context.startService(intentServicio);
    }
}
