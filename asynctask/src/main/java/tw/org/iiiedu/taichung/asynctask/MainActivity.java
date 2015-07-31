package tw.org.iiiedu.taichung.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

    private MyAsyncTask task;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private int intCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);

    }

    public void start(View v) {
        task = new MyAsyncTask();
        task.execute("test1",
                     "test2",
                     "test2",
                     "test2",
                     "test2",
                     "test2",
                     "test2",
                     "test3");

    }

    public void stop(View v) {
        task.cancel(true);
    }

        class MyAsyncTask extends AsyncTask<String, Integer, Boolean> {
            /**
             * Creates a new asynchronous task. This constructor must be invoked on the UI thread.
             */
            public MyAsyncTask() {
                super();
            }


            /**
             * Runs on the UI thread before {@link #doInBackground}.
             *
             * @see #onPostExecute
             * @see #doInBackground
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.i("henry", "onPreExecute");

            }

            /**
             * Override this method to perform a computation on a background thread. The
             * specified parameters are the parameters passed to {@link #execute}
             * by the caller of this task.
             * <p/>
             * This method can call {@link #publishProgress} to publish updates
             * on the UI thread.
             *
             * @param names The parameters of the task.
             * @return A result, defined by the subclass of this task.
             * @see #onPreExecute()
             * @see #onPostExecute
             * @see #publishProgress
             */
            @Override
            protected Boolean doInBackground(String... names) {

                for (String name:names){
                    intCounter++;
                    Log.i("henry", "doInBackground");

                    publishProgress(intCounter,intCounter*10,intCounter*100);

                    if (isCancelled()) return false;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                return true;
            }

            /**
             * Runs on the UI thread after {@link #publishProgress} is invoked.
             * The specified values are the values passed to {@link #publishProgress}.
             *
             * @param values The values indicating progress.
             * @see #publishProgress
             * @see #doInBackground
             */
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                Log.i("henry", "onProgressUpdate");
                tv1.setText("OK"+values[0]);
                tv2.setText("OK"+values[1]);
                tv3.setText("OK"+values[2]);


            }

            /**
             * <p>Runs on the UI thread after {@link #doInBackground}. The
             * specified result is the value returned by {@link #doInBackground}.</p>
             * <p/>
             * <p>This method won't be invoked if the task was cancelled.</p>
             *
             * @param aVoid The result of the operation computed by {@link #doInBackground}.
             * @see #onPreExecute
             * @see #doInBackground
             * @see #onCancelled(Object)
             */
            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                Log.i("henry", "onPostExecute");
                tv4.setText(aVoid?"Finished":"Cancelled");

            }



            /**
             * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
             * {@link #doInBackground(Object[])} has finished.</p>
             * <p/>
             * <p>The default implementation simply invokes {@link #onCancelled()} and
             * ignores the result. If you write your own implementation, do not call
             * <code>super.onCancelled(result)</code>.</p>
             *
             * @param aVoid The result, if any, computed in
             *              {@link #doInBackground(Object[])}, can be null
             * @see #cancel(boolean)
             * @see #isCancelled()
             */
            @Override
            protected void onCancelled(Boolean aVoid) {
                super.onCancelled(aVoid);
                Log.i("henry", "onCancelled");
                tv4.setText(aVoid?"Finished":"Cancelled");
            }

            /**
             * <p>Applications should preferably override {@link #onCancelled(Object)}.
             * This method is invoked by the default implementation of
             * {@link #onCancelled(Object)}.</p>
             * <p/>
             * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
             * {@link #doInBackground(Object[])} has finished.</p>
             *
             * @see #onCancelled(Object)
             * @see #cancel(boolean)
             * @see #isCancelled()
             */
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Log.i("henry", "onCancelled2");

            }


        }
}
