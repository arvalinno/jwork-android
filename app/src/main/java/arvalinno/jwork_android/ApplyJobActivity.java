package arvalinno.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplyJobActivity extends AppCompatActivity {

    private int jobseekerId;
    private int jobId;
    private String jobName;
    private String jobCategory;
    private double jobFee;
    private int bonus;
    private String selectedPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);

        TextView tvJobName = findViewById(R.id.job_name);
        TextView tvJobCategory = findViewById(R.id.job_category);
        TextView tvJobFee = findViewById(R.id.job_fee);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        TextView tvTextCode = findViewById(R.id.textCode);
        EditText etReferralCode = findViewById(R.id.referral_code);
        TextView tvTotalFee = findViewById(R.id.total_fee);
        Button btnApply = findViewById(R.id.btnApply);
        Button btnHitung = findViewById(R.id.hitung);

        btnApply.setVisibility(View.INVISIBLE);
        tvTextCode.setVisibility(View.INVISIBLE);
        etReferralCode.setVisibility(View.INVISIBLE);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobId = extras.getInt("job_id");
            jobName = extras.getString("job_name");
            jobCategory = extras.getString("job_category");
            jobFee = extras.getInt("job_fee");
            jobseekerId = extras.getInt("jobseekerId");
        }

        tvJobName.setText(jobName);
        tvJobCategory.setText(jobCategory);
        tvJobFee.setText(Double.toString(jobFee));
        tvTotalFee.setText("0");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = findViewById(checkedId);
                switch (checkedId) {
                    case R.id.ewallet:
                        tvTextCode.setVisibility(View.VISIBLE);
                        etReferralCode.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bank:
                        tvTextCode.setVisibility(View.INVISIBLE);
                        etReferralCode.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = radioGroup.getCheckedRadioButtonId();
                switch (checkedId) {
                    case R.id.ewallet:

                        String refCode = etReferralCode.getText().toString();
                        final Response.Listener<String> bonusResponse = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (refCode.isEmpty()) {
                                    Toast.makeText(ApplyJobActivity.this, "No referral code applied!", Toast.LENGTH_LONG).show();
                                    tvTotalFee.setText(Double.toString(jobFee));
                                } else {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        //Get Discount Price
                                        int extraFee = jsonResponse.getInt("extraFee");
                                        int minTotalFee = jsonResponse.getInt("minTotalFee");
                                        boolean bonusStatus = jsonResponse.getBoolean("active");

                                        if (!bonusStatus) {
                                            Toast.makeText(ApplyJobActivity.this, "This bonus is unavailable!", Toast.LENGTH_LONG).show();
                                        } else if (bonusStatus) {
                                            if (jobFee < extraFee || jobFee < minTotalFee) {
                                                Toast.makeText(ApplyJobActivity.this, "Referral code is invalid!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(ApplyJobActivity.this, "Referral code applied!", Toast.LENGTH_LONG).show();
                                                //Set Total Price
                                                tvTotalFee.setText(Double.toString(jobFee + extraFee));
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(ApplyJobActivity.this, "Referral code not found!", Toast.LENGTH_LONG).show();
                                        tvTotalFee.setText(Double.toString(jobFee));
                                    }
                                }

                            }
                        };
                        Response.ErrorListener errorPromo = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", "Error Occured", error);
                            }
                        };
                        //Volley Request for Promo Request
                        BonusRequest bonusRequest = new BonusRequest(refCode, bonusResponse);
                        RequestQueue queue = Volley.newRequestQueue(ApplyJobActivity.this);
                        queue.add(bonusRequest);
                        break;

                    case R.id.bank:
                        tvTotalFee.setText(Double.toString(jobFee));
                        break;
                }
                btnApply.setVisibility(View.VISIBLE);
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int selectedRadioId = radioGroup.getCheckedRadioButtonId();
                ApplyJobRequest request = null;

                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(ApplyJobActivity.this, "Job Applied!", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            else {
                                Toast.makeText(ApplyJobActivity.this, "Apply job failed!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ApplyJobActivity.this, "Apply job failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                if(selectedRadioId == R.id.bank) {
                    request = new ApplyJobRequest(String.valueOf(jobId), String.valueOf(jobseekerId), responseListener);
                    RequestQueue q = Volley.newRequestQueue(ApplyJobActivity.this);
                    q.add(request);
                }

                else if(selectedRadioId == R.id.ewallet) {
                    request = new ApplyJobRequest(String.valueOf(jobId), String.valueOf(jobseekerId), etReferralCode.getText().toString(), responseListener);
                    RequestQueue q = Volley.newRequestQueue(ApplyJobActivity.this);
                    q.add(request);
                }
            }
        });
    }
}