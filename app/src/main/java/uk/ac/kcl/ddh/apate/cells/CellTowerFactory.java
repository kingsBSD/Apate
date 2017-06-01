package uk.ac.kcl.ddh.apate.cells;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import uk.ac.kcl.ddh.apate.data.CellReading;

/**
 * Created by augeas on 01/06/17.
 */

public class CellTowerFactory {

    private TelephonyManager manager;

    public CellTowerFactory(Context context) {
        manager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
    }

    public CellReading getReading(CellLocation location) {

        boolean isNone = true;
        boolean isValid = false;
        String Mcc = "None";
        String Mnc = "None";
        String Lac = "None";
        String Cid = "None";
        int intLac,intId;

        String mccmnc = manager.getNetworkOperator();

        if (mccmnc != null) {
            if (location instanceof GsmCellLocation) {
                try {
                    Mcc = mccmnc.substring(0,3);
                    Mnc = mccmnc.substring(3);
                    intLac = ((GsmCellLocation) location).getLac();
                    intId = ((GsmCellLocation) location).getCid();
                    Lac = Integer.toString(intLac);
                    Cid = Integer.toString(intId);
                    if (intLac >= 0 && intId >= 0) {
                        isValid = true;
                    }
                    isNone = false;
                }
                catch(Exception e) {
                    isNone = true;
                }
            }
        }

        if (isValid && !isNone) {
            CellReading reading = new CellReading();
            reading.setMcc(Mcc);
            reading.setMnc(Mnc);
            reading.setLac(Lac);
            reading.setCid(Cid);
            return reading;
        } else {
            return null;
        }

    }

}
