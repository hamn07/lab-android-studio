package tw.org.iiiedu.taichung.io_db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private TextView tv;
    private MySQLiteHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        helper = new MySQLiteHelper(this,"pichannel",null,1);
        db = helper.getReadableDatabase();
    }

    public void create(View v){
        ContentValues values = new ContentValues();
        values.put("sha1","sadfhjkluyiorteydfghvbnmhkjlfghjsdfgiuyo");
        db.insert("image",null,values);
        read(null);
    }

    public void read(View v){
        Cursor cursor = db.query("image", new String[]{"sha1,count(sha1)"},
                "sha1=?",new String[]{"sadfhjkluyiorteydfghvbnmhkjlfghjsdfgiuyo"},"sha1","count(sha1)>1","sha1 desc");
        StringBuffer sb = new StringBuffer();
        while(cursor.moveToNext()){
            String sha1 = cursor.getString(cursor.getColumnIndex("sha1"));
            String num = cursor.getString(cursor.getColumnIndex("count(sha1)"));
            sb.append("sha1 : " + sha1 + " num : " + num + "\n");
        }
        tv.setText(sb.toString());
    }

    public void update(View v){
        ContentValues values = new ContentValues();
        values.put("sha1","gfhjghfjfghjgfhjghfjghfjghfjghfjghfjghfj");
        db.update("image",values,"sha1 = ?",new String[]{"sadfhjkluyiorteydfghvbnmhkjlfghjsdfgiuyo"});
        read(null);
    }

    public void delete(View v){
        db.delete("image","sha1 = ?",new String[]{"sadfhjkluyiorteydfghvbnmhkjlfghjsdfgiuyo"});
        read(null);
    }
























    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
