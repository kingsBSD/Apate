package uk.ac.kcl.ddh.apate;

import android.app.Application;;import org.greenrobot.greendao.database.Database;

import uk.ac.kcl.ddh.apate.data.DaoMaster;
import uk.ac.kcl.ddh.apate.data.DaoSession;

/**
 * Created by augeas on 01/06/17.
 */

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "apate-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
