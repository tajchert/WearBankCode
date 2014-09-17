package pl.tajchert.wearbank.codes;

import android.app.Notification;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;


public class DataLayerListenerService extends WearableListenerService {

    private static final String TAG = DataLayerListenerService.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();
        for (DataEvent event : events) {
            Uri uri = event.getDataItem().getUri();
            String path = uri.getPath();
            if (Tools.WEAR_PATH.equals(path)) {
                DataMapItem item = DataMapItem.fromDataItem(event.getDataItem());
                String codeText = item.getDataMap().getString(Tools.WEAR_KEY_BANKS_CODE);
                if(codeText != null) {
                    String contentArr []  = codeText.split("<<>>");
                    codeText = contentArr[0];
                    if(codeText != null && !codeText.equals("")){
                        makeNotification(codeText);
                    }
                }
            }
        }
    }

    private void makeNotification(String code){
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(DataLayerListenerService.this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Confirmation code: ")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setLargeIcon(BitmapFactory.decodeResource(DataLayerListenerService.this.getResources(),R.drawable.picture_background))
                        .setContentText("" + code);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(DataLayerListenerService.this);
        notificationManager.notify(Tools.NOTICIATION_ID, notificationBuilder.build());
    }

    @Override
    public void onPeerConnected(Node peer) {}

    @Override
    public void onPeerDisconnected(Node peer){}

}
