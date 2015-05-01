package in.co.tsystem.mango;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by achatterjee on 05-04-2015.
 */
public class DBHelper extends Activity {//SQLiteOpenHelper {
    /*public static final String DATABASE_NAME = "imgdb.db";
    public static final String TABLE_NAME = "tbl_img";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, img BLOB NOT NULL, description TEXT NULL)";
    public static final String DELETE_TABLE="DROP TABLE IF EXISTS " + TABLE_NAME;
    //public SQLiteDatabase imagedb;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
// Create the table
        //db.execSQL(DELETE_TABLE);
        db.execSQL(CREATE_TABLE);
    }
    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//Drop older table if existed
        Log.d("DB onUpgrade called","");
        db.execSQL(DELETE_TABLE);
//Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

        super.onOpen(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//Drop older table if existed
        db.execSQL(DELETE_TABLE);
//Create tables again
        onCreate(db);
    }

    public SQLiteDatabase getDb() {
        return (this.getWritableDatabase());
    }

    public int getVersionnew() {
        return (this.getWritableDatabase().getVersion());
    }

    public void insertBitmap(Bitmap bm) {

// Convert the image into byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] buffer=out.toByteArray();
// Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
// Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try
        {
            values = new ContentValues();
            values.put("img", buffer);
            values.put("description", "Image description");
// Insert Row
            long i = db.insert(TABLE_NAME, null, values);
            Log.i("Insert", i + "");
// Insert into database successfully.
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {
            db.endTransaction();
// End the transaction.
            db.close();
// Close database
        }
    }

    public Bitmap getBitmap(int id){
        Bitmap bitmap = null;
// Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
// Start the transaction.
        db.beginTransaction();

        try
        {
            String selectQuery = "SELECT * FROM "+ TABLE_NAME+" WHERE id = " + id;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0)
            {
                while (cursor.moveToNext()) {
// Convert blob data to byte array
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("img"));
// Convert the byte array to Bitmap
                    bitmap= BitmapFactory.decodeByteArray(blob, 0, blob.length);

                }

            }
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {
            db.endTransaction();
// End the transaction.
            db.close();
// Close database
        }
        return bitmap;

    }

    public class checkDbVer extends AsyncTask< Void, Void, Integer > {

        JSONObject jb;
        private Context mContext;
        BufferedReader br;

        public checkDbVer(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Integer db_ver_stored = 0;

            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        openFileInput("CartDbVer")));
                String inputString;
                //StringBuffer stringBuffer = new StringBuffer();
                while ((inputString = inputReader.readLine()) != null) {
                    //stringBuffer.append(inputString + "\n");
                    db_ver_stored = Integer.parseInt(inputString
                    );
                }

            } catch (IOException e) {
                //e.printStackTrace();
                db_ver_stored = 0;
            }

            Log.d("DB_VER_AND", dbHelper.DATABASE_VERSION + "" );
            Log.d("DB_VER_STORED is "+ db_ver_stored , "");
            //if (result != dbHelper.getVersionnew()) {
            if (result != db_ver_stored) {
                // download catalog db
                newCatDbVer = result;

                //write version to file
                try {
                    FileOutputStream fos = openFileOutput("CartDbVer", Context.MODE_PRIVATE);
                    fos.write(result.toString().getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dbHelper.onUpgrade(dbHelper.getDb(),db_ver_stored,result);
                tsk.execute();
            } else {
                // populate grid view from database

                GridView gv = (GridView)findViewById(R.id.gridview1);
                GridViewAdapter ga1 = new GridViewAdapter(mContext);
                gv.setAdapter(ga1);

                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        Toast.makeText(MainActivity.this, "" + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... arg0) {

            String url_new = null, ver = null;
            int version = 0;

            url_new = "http://10.0.0.112/landing/category_db_ver_check.php";
            ServerComm sChannel = new ServerComm();
            JSONObject jObj = sChannel.restRequest(url_new);

            try {
                ver = jObj.getString("db_ver");
                version = Integer.parseInt(ver);
                Log.i("DB_VER", version + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return version;
        }
    }*/
}