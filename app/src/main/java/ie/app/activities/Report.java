package ie.app.activities;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
//import ie.app.R;
import ie.app.api.DonationApi;
import ie.app.models.Donation;
public class Report extends Base implements OnItemClickListener, OnClickListener
{
    ListView listView;
    DonationAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        listView = (ListView) findViewById(R.id.reportList);
        mSwipeRefreshLayout = (SwipeRefreshLayout)
                findViewById(R.id.report_swipe_refresh_layout);
        new GetAllTask(this).execute("/donations");
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 new GetAllTask(Report.this).execute("/donations");
             }
        });

//        DonationAdapter adapter = new DonationAdapter(this, app.dbManager.getAll());
//        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View row, int pos, long id) {
    }

    @Override
    public void onClick(View view) {
    }


    private class GetAllTask extends AsyncTask<String, Void, List<Donation>> {
        protected ProgressDialog dialog;
        protected Context context;
        public GetAllTask(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Retrieving Donations List");
            this.dialog.show();
        }
        @Override
        protected List<Donation> doInBackground(String... params) {
            try {
                return (List<Donation>) DonationApi.getAll((String) params[0]);
            } catch (Exception e) {
                Log.v("ASYNC", "ERROR : " + e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Donation> result) {
            super.onPostExecute(result);
            app.donations = result;
            adapter = new DonationAdapter(context, app.donations);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(Report.this);
            mSwipeRefreshLayout.setRefreshing(false);
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    class DonationAdapter extends ArrayAdapter<Donation> {
        private Context context;
        public List<Donation> donations;
        public DonationAdapter(Context context, List<Donation> donations) {
            super(context, R.layout.row_donate, donations);
            this.context = context;
            this.donations = donations;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.row_donate, parent, false);
            Donation donation = donations.get(position);
            ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            imgDelete.setTag(donation);
            imgDelete.setOnClickListener(Report.this);
            TextView amountView = (TextView)
                    view.findViewById(R.id.row_amount);
            TextView methodView = (TextView)
                    view.findViewById(R.id.row_method);
            TextView upvotesView = (TextView)
                    view.findViewById(R.id.row_upvotes);
            amountView.setText("" + donation.amount);
            methodView.setText(donation.paymenttype);
            upvotesView.setText("" + donation.upvotes);
            view.setTag(donation.id); // setting the 'row' id to the id of the donation
            return view;
        }
        @Override
        public int getCount() {
            return donations.size();
        }
    }
}