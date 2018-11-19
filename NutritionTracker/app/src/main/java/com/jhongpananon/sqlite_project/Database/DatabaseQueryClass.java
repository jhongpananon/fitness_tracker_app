package com.jhongpananon.sqlite_project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.jhongpananon.sqlite_project.Features.CreateAddress.Address;
import com.jhongpananon.sqlite_project.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseQueryClass {

    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertAddress(Address address){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_EXERCISE_NAME, address.getName());
        contentValues.put(Config.COLUMN_EXERCISE_NUM_REPS, address.getRegistrationNumber());
        contentValues.put(Config.COLUMN_EXERCISE_DATE, address.getDate());

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_EXERCISE, null, contentValues);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Address> getAllAddress(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_EXERCISE, null, null, null, null, null, null, null);

            /**
                 // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

                 String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_EXERCISE_ID, Config.COLUMN_EXERCISE_NAME, Config.COLUMN_EXERCISE_NUM_REPS, Config.COLUMN_ADDRESS_EMAIL, Config.COLUMN_EXERCISE_DATE, Config.TABLE_EXERCISE);
                 cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Address> addressList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_EXERCISE_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_EXERCISE_NAME));
                        long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_EXERCISE_NUM_REPS));
                        long date = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_EXERCISE_DATE));

                        addressList.add(new Address(id, name, registrationNumber, date));
                    }   while (cursor.moveToNext());

                    return addressList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Address getAddressByRegNum(long registrationNum){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Address address = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_EXERCISE, null,
                    Config.COLUMN_EXERCISE_NUM_REPS + " = ? ", new String[]{String.valueOf(registrationNum)},
                    null, null, null);

            /**
                 // If you want to execute raw query then uncomment below 2 lines. And comment out above sqLiteDatabase.query() method.

                 String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_EXERCISE, Config.COLUMN_EXERCISE_NUM_REPS, String.valueOf(registrationNum));
                 cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_EXERCISE_ID));
                String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_EXERCISE_NAME));
                long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_EXERCISE_NUM_REPS));
                long date = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_EXERCISE_DATE));
                address = new Address(id, name, registrationNumber, date);
            }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return address;
    }

    public long updateAddressInfo(Address address){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_EXERCISE_NAME, address.getName());
        contentValues.put(Config.COLUMN_EXERCISE_NUM_REPS, address.getRegistrationNumber());
        contentValues.put(Config.COLUMN_EXERCISE_DATE, address.getDate());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_EXERCISE, contentValues,
                    Config.COLUMN_EXERCISE_ID + " = ? ",
                    new String[] {String.valueOf(address.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteAddressByRegNum(long registrationNum) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_EXERCISE,
                                    Config.COLUMN_EXERCISE_NUM_REPS + " = ? ",
                                    new String[]{ String.valueOf(registrationNum)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllAddresss(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_EXERCISE, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_EXERCISE);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

}