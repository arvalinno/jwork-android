package arvalinno.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelesaiJobActivity extends AppCompatActivity {

    private static int jobseekerId;
    private int invoiceId;
    private String date;
    private String paymentType;
    private int totalFee;
    private String jobseekerName;
    private String jobName;
    private int jobFee;
    private String invoiceStatus;
    private String refCode;
    private JSONObject bonus;

    //component finalization
    private TextView judul;
    private TextView staticJobseekerName;
    private TextView staticInvoiceDate;
    private TextView staticPayment;
    private TextView staticInvoiceStatus;
    private TextView staticRefCode;
    private TextView staticJobNameSelesai;
    private TextView staticTotalFee;

    private TextView jobseeker_name;
    private TextView invoice_date;
    private TextView payment_type;
    private TextView invoice_status;
    private TextView referral_code;
    private TextView job_name_selesai;
    private TextView fee_selesai;
    private TextView total_fee_selesai;

    private Button btnCancel;
    private Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai_job);

        judul = findViewById(R.id.judul);
        staticJobseekerName = findViewById(R.id.staticJobSeekerName);
        staticInvoiceDate= findViewById(R.id.staticInvoiceDate);
        staticPayment = findViewById(R.id.staticPayment);
        staticInvoiceStatus = findViewById(R.id.staticInvoiceStatus);
        staticRefCode = findViewById(R.id.staticRefCode);
        staticJobNameSelesai = findViewById(R.id.staticJobNameSelesai);
        staticTotalFee = findViewById(R.id.staticTotalFee);

        jobseeker_name = findViewById(R.id.jobseeker_name);
        invoice_date= findViewById(R.id.invoice_date);
        payment_type = findViewById(R.id.payment_type);
        invoice_status = findViewById(R.id.invoice_status);
        referral_code = findViewById(R.id.referral_code);
        job_name_selesai = findViewById(R.id.job_name_selesai);
        fee_selesai = findViewById(R.id.fee_selesai);
        total_fee_selesai = findViewById(R.id.total_fee_selesai);

        btnCancel = findViewById(R.id.btnCancel);
        btnFinish = findViewById(R.id.btnFinish);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            jobseekerId = extras.getInt("jobseekerId");
        }

        //initial component visibility
/*        judul.setVisibility(View.INVISIBLE);
        staticJobseekerName.setVisibility(View.INVISIBLE);
        staticInvoiceDate.setVisibility(View.INVISIBLE);
        staticPayment.setVisibility(View.INVISIBLE);
        staticInvoiceStatus.setVisibility(View.INVISIBLE);
        staticRefCode.setVisibility(View.INVISIBLE);
        staticRefCode.setVisibility(View.INVISIBLE);
        staticJobNameSelesai.setVisibility(View.INVISIBLE);
        staticTotalFee.setVisibility(View.INVISIBLE);

        jobseeker_name.setVisibility(View.INVISIBLE);
        invoice_date.setVisibility(View.INVISIBLE);
        payment_type.setVisibility(View.INVISIBLE);
        invoice_status.setVisibility(View.INVISIBLE);
        referral_code.setVisibility(View.INVISIBLE);
        job_name_selesai.setVisibility(View.INVISIBLE);
        fee_selesai.setVisibility(View.INVISIBLE);
        total_fee_selesai.setVisibility(View.INVISIBLE);

        btnCancel.setVisibility(View.INVISIBLE);
        btnFinish.setVisibility(View.INVISIBLE);
*/
        fetchJob();
        initButtons();
    }

    private void fetchJob(){
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()) {
                    Toast.makeText(SelesaiJobActivity.this, "No job applied!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SelesaiJobActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    judul.setVisibility(View.VISIBLE);
                    staticJobseekerName.setVisibility(View.VISIBLE);
                    staticInvoiceDate.setVisibility(View.VISIBLE);
                    staticPayment.setVisibility(View.VISIBLE);
                    staticInvoiceStatus.setVisibility(View.VISIBLE);
                    staticRefCode.setVisibility(View.VISIBLE);
                    staticRefCode.setVisibility(View.VISIBLE);
                    staticJobNameSelesai.setVisibility(View.VISIBLE);
                    staticTotalFee.setVisibility(View.VISIBLE);

                    jobseeker_name.setVisibility(View.VISIBLE);
                    invoice_date.setVisibility(View.VISIBLE);
                    payment_type.setVisibility(View.VISIBLE);
                    invoice_status.setVisibility(View.VISIBLE);
                    referral_code.setVisibility(View.VISIBLE);
                    job_name_selesai.setVisibility(View.VISIBLE);
                    fee_selesai.setVisibility(View.VISIBLE);
                    total_fee_selesai.setVisibility(View.VISIBLE);

                    btnCancel.setVisibility(View.VISIBLE);
                    btnFinish.setVisibility(View.VISIBLE);
                }

                try {
                    //JSONArray and JSONObject for the invoice shown
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i=0; i<jsonResponse.length(); i++) {
                        JSONObject jsonInvoice = jsonResponse.getJSONObject(i);
                        invoiceStatus = jsonInvoice.getString("invoiceStatus");
                        invoiceId = jsonInvoice.getInt("id");
                        date = jsonInvoice.getString("date");
                        paymentType = jsonInvoice.getString("paymentType");
                        totalFee = jsonInvoice.getInt ("totalFee");
                        refCode = "---";
                        try{
                            bonus = jsonInvoice.getJSONObject("bonus");
                            refCode = bonus.getString("referralCode");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        judul.setText(String.valueOf(invoiceId));
                        invoice_date.setText(date.substring(0,10));
                        payment_type.setText(paymentType);
                        total_fee_selesai.setText(String.valueOf(totalFee));
                        invoice_status.setText(invoiceStatus);
                        referral_code.setText(refCode);

                        JSONObject jsonCustomer = jsonInvoice.getJSONObject("jobseeker");
                        jobseekerName = jsonCustomer.getString("name");
                        jobseeker_name.setText(jobseekerName);

                        JSONArray jsonJobs = jsonInvoice.getJSONArray("jobs");
                        for (int j=0; j<jsonJobs.length(); j++) {
                            JSONObject jsonJobObj = jsonJobs.getJSONObject(j);
                            jobName = jsonJobObj.getString("name");
                            job_name_selesai.setText(jobName);

                            jobFee = jsonJobObj.getInt("fee");
                            fee_selesai.setText(String.valueOf(jobFee));
                        }
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JobFetchRequest fetchRequest = new JobFetchRequest(String.valueOf(jobseekerId), responseListener);
        RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
        queue.add(fetchRequest);
    }

    private void initButtons(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Response.Listener<String> cancelListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(SelesaiJobActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Toast.makeText(SelesaiJobActivity.this, "Your job has been canceled!", Toast.LENGTH_LONG).show();
                JobBatalRequest batalRequest = new JobBatalRequest(String.valueOf(invoiceId), cancelListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(batalRequest);
            }
        });

        //finish button function
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Response.Listener<String> doneListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(SelesaiJobActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Toast.makeText(SelesaiJobActivity.this, "Your job is finished!", Toast.LENGTH_LONG).show();
                JobSelesaiRequest selesaiRequest = new JobSelesaiRequest(String.valueOf(invoiceId), doneListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(selesaiRequest);
            }
        });
    }
}