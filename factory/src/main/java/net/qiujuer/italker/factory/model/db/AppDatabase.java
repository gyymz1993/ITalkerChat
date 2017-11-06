package net.qiujuer.italker.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 数据库的基本信息
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 2;
}
