package uk.ac.kcl.ddh.apate.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.ConcurrentLinkedQueue;

import uk.ac.kcl.ddh.apate.App;

/**
 * Created by augeas on 01/06/17.
 */

public class CaptureCache {

    private final static String CACHED_CELL = "cachedcell";
    private final static String CELL_MCC = "cell_mcc";
    private final static String CELL_MNC = "cell_mnc";
    private final static String CELL_LAC = "cell_lac";
    private final static String CELL_CID = "cell_cid";
    private final static String CELL_TIMESTAMP = "cell_timestamp";

    private final String[] filters = {CACHED_CELL};

    private Context context;

    private ConcurrentLinkedQueue<CellReading> cellQueue;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context ctx, Intent intent) {

            String action = intent.getAction();

            switch (action) {
                case CACHED_CELL:
                    CellReading cell = new CellReading();
                    cell.setMcc(intent.getStringExtra(CELL_MCC));
                    cell.setMnc(intent.getStringExtra(CELL_MNC));
                    cell.setLac(intent.getStringExtra(CELL_LAC));
                    cell.setCid(intent.getStringExtra(CELL_CID));
                    cell.setTimestamp(intent.getStringExtra(CELL_TIMESTAMP));
                    cellQueue.offer(cell);
                    break;
            }

        }
    };

    public static void pushCell(Context context, CellReading reading, java.util.Date timestamp) {
        Intent intent = new Intent(CACHED_CELL);
        intent.putExtra(CELL_MCC, reading.getMcc());
        intent.putExtra(CELL_MNC, reading.getMnc());
        intent.putExtra(CELL_LAC, reading.getLac());
        intent.putExtra(CELL_CID, reading.getCid());
        intent.putExtra(CELL_TIMESTAMP, Long.toString(timestamp.getTime()));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public CaptureCache(Context context) {

        this.context = context;

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);

        cellQueue = new ConcurrentLinkedQueue<CellReading>();

        for (String filter: filters) {
            manager.registerReceiver(receiver, new IntentFilter(filter));
        }
    }

    public void flush() {
        DaoSession daoSession = ((App) context).getDaoSession();

        if (cellQueue.size() > 0) {
            CellReadingDao cellDao = daoSession.getCellReadingDao();
            while (cellQueue.size() != 0) {
                cellDao.insert(cellQueue.poll());
            }
        }

    }

}
