package slowscript.warpinator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.LinkedHashMap;

public class MainService extends Service {

    static String TAG = "SERVICE";
    static int PORT = 42000;

    public static Server server;
    public static LinkedHashMap<String, Remote> remotes = new LinkedHashMap<>();
    public TransfersActivity transfersView;

    public static MainService svc;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        svc = this;

        Log.d(TAG, "Service starting...");
        server = new Server(PORT, this);
        server.Start();

        return START_STICKY;
    }

    void stopServer () {
        server.Stop();
        for (Remote r : remotes.values()) {
            r.disconnect();
        }
        remotes.clear();
    }

    public void addRemote(Remote remote) {
        //Add to remotes list
        remotes.put(remote.uuid, remote);
        //Add to GUI
        MainActivity.updateRemoteList();
        //Connect to it
        remote.connect();
    }

    public void removeRemote(String uuid) {
        Remote r = remotes.get(uuid);
        //Disconnect
        r.disconnect();
        //Remove from GUI
        MainActivity.updateRemoteList();
        //Remove
        remotes.remove(uuid);
    }
}
