package com.challenge.mcculloch.bodybuild.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.challenge.mcculloch.bodybuild.Constants;
import com.challenge.mcculloch.bodybuild.objects.NoteLite;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by wooan on 10/4/2015.
 */
public class NoteLiteRepo extends OrmLiteSqliteOpenHelper {
    private final String TAG = this.getClass().getName();
    private static NoteLiteRepo _helperInstance;
    private Context _context;
    private Dao<NoteLite, Integer> noteLite = null;

    public static NoteLiteRepo getInstance(Context context) {
        if (_helperInstance == null)
            _helperInstance = new NoteLiteRepo(context);

        return _helperInstance;
    }

    public NoteLiteRepo(Context context) {
        super(context, Constants.DATABASE_NAME_MemberNotes, null, Constants.DATABASE_VERSION);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NoteLite.class);
        } catch (Exception e) {
            Log.e(TAG, "could not create table NoteLite", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        try {
            TableUtils.dropTable(connectionSource, NoteLite.class, false);
        } catch (SQLException e) {
            Log.e(TAG, "could not drop table NoteLite onUpgrade", e);
        }
        onCreate(sqLiteDatabase, connectionSource);
    }

    @Override
    public void close() {
        super.close();
        _helperInstance = null;
    }

    public void insertNoteLite(NoteLite noteLite) {
        try {
            OrmLiteSqliteOpenHelper dbHelper = NoteLiteRepo.getInstance(_context);
            Dao<NoteLite, Integer> daoNoteLite = dbHelper.getDao(NoteLite.class);
            daoNoteLite.create(noteLite);
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCount(int memberid) {
        try {
            QueryBuilder<NoteLite, Integer> queryBuilder = getNoteLiteDataDao().queryBuilder();
            queryBuilder.where().eq(Constants.memberNotes_UserId, memberid);
            return queryBuilder.query().size();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public NoteLite getNoteByMember(int memberid) {
        try {
            QueryBuilder<NoteLite, Integer> queryBuilder = getNoteLiteDataDao().queryBuilder();
            queryBuilder.where().eq(Constants.memberNotes_UserId, memberid);
            return queryBuilder.query().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Dao<NoteLite, Integer> getNoteLiteDataDao() {
        try {
            if (noteLite == null) {
                noteLite = getDao(NoteLite.class);
            }
            return noteLite;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void deleteNoteLite(int memberid) {
        try {
            Dao<NoteLite, Integer> dao = getNoteLiteDataDao();
            DeleteBuilder<NoteLite, Integer> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(Constants.memberNotes_UserId, memberid);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNoteList(NoteLite noteLite){
        try {
        UpdateBuilder<NoteLite, Integer> updateBuilder = getNoteLiteDataDao().updateBuilder();
        updateBuilder.where().eq(Constants.memberNotes_UserId, noteLite.getMemberId());
        updateBuilder.updateColumnValue(Constants.memberNotes_UserNote, noteLite.getUserNote());
        updateBuilder.update();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
