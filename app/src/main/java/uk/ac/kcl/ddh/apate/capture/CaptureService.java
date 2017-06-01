package uk.ac.kcl.ddh.apate.capture;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.Date;

import uk.ac.kcl.ddh.apate.cells.CellTowerFactory;
import uk.ac.kcl.ddh.apate.data.CaptureCache;
import uk.ac.kcl.ddh.apate.data.CellReading;

public class CaptureService extends Service {

    private Context context;
    private SharedPreferences sharedPref;
    private boolean running;
    private boolean cellCaptureActive;

    private Runnable cacheWorker;
    private Handler cacheHandle;

    private CaptureCache cache;
    private CellTowerFactory cellFactory;

    public CaptureService() {
    }

    @Override
    public void onCreate() {

        context = this;
        running = false;
        cellCaptureActive = false;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener);

        cellFactory = new CellTowerFactory(context);
        cache = new CaptureCache(context);

        cacheWorker = new Runnable() {
            @Override
            public void run() {
                cache.flush();
                cacheHandle.postDelayed(this, 120000);
            }
        };

        cacheHandle = new Handler();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        toggleCellCapture(sharedPref);
        cacheHandle.post(cacheWorker);
        running = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    private void toggleCellCapture(SharedPreferences pref) {
        if (pref.getBoolean("pref_key_capture_cells", false)) {
            if (!cellCaptureActive) {
                captureCells();
            }
        } else {
            if (cellCaptureActive) {
                stopCellCapture();
            }
        }
    }

    private void captureCells() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            int phoneFlags = PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_CELL_LOCATION;
            ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).listen(phoneListener, phoneFlags);
            cellCaptureActive = true;
        }
    }

    private void stopCellCapture() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).listen(phoneListener, PhoneStateListener.LISTEN_NONE);
            cellCaptureActive = false;
        }
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (running) {

                switch (key) {
                    case "pref_key_capture_cells":
                        toggleCellCapture(prefs);
                        break;
                }
            }
        }
    };

    private final PhoneStateListener phoneListener = new PhoneStateListener() {

        @Override
        public void onDataActivity(int direction) {

        }

        @Override
        public void onCellLocationChanged (CellLocation location) {
            CellReading cell = cellFactory.getReading(location);
            CaptureCache.pushCell(context, cell, new Date());
        }
    };

}
