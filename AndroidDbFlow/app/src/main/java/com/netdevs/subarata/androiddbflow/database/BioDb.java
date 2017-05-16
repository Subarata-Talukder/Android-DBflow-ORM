package com.netdevs.subarata.androiddbflow.database;

import com.netdevs.subarata.androiddbflow.model.Bio;
import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by student on 19/11/2016.
 */

@Database(name = BioDb.NAME, version = BioDb.VERSION)
public class BioDb {
    public static final String NAME = "BioDb";
    public static final int VERSION = 1;
}
