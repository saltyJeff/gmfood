package gmfood.saltyjeff.github.io.gm_food;

import android.Manifest;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gm.android.vehicle.hardware.RotaryControlHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import gmfood.saltyjeff.github.io.gm_food.apistuff.GMFOOD;
import gmfood.saltyjeff.github.io.gm_food.apistuff.QuoteResponse;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Callback;

public class RecordOrderActivity extends AppCompatActivity {
	public static final String TAG = "RECORD ACTIVITY";
	String vendorId;
	MediaRecorder recorder = new MediaRecorder();
	File outputFile;
	TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_order);
		vendorId = getIntent().getStringExtra("id");
		text = (TextView)findViewById(R.id.recordText);
		text.setText("Preparing to record... Pls give us the permission");
		Dexter.withActivity(this)
				.withPermission(Manifest.permission.RECORD_AUDIO)
				.withListener(new PermissionListener() {
					@Override
					public void onPermissionGranted(PermissionGrantedResponse response) {
						recordAudioToFile();
					}

					@Override
					public void onPermissionDenied(PermissionDeniedResponse response) {

					}

					@Override
					public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

					}
				})
				.check();
	}
	void recordAudioToFile() {
		try {
			File outputDir = getCacheDir(); // context being the Activity pointer
			outputFile = File.createTempFile("recordOrder", ".mp3", outputDir);
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			FileOutputStream fos = new FileOutputStream(outputFile);
			recorder.setOutputFile(fos.getFD());
			recorder.prepare();
			recorder.start();
			text.setText("Recording to "+outputFile.toString()+" - press button to stop recording");
			((Button)findViewById(R.id.stopRecordButton)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					recorder.stop();
					recorder.release();
					uploadRecording();
				}
			});
		}
		catch(IOException e) {
			Log.e("IO ERR", e.toString());
			finish();
		}
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean didHandleRotary = RotaryControlHelper.onKeyEvent(event, new RotaryControlHelper.RotaryListener() {
			@Override
			public boolean onRotaryClockwise(int velocity) {
				// Rotary was turned clockwise
				return true;
			}

			@Override
			public boolean onRotaryCounterClockwise(int velocity) {
				// Rotary was turned counter clockwise
				return true;
			}

			@Override
			public boolean onMenuButtonClick() {
				// Menu button was pressed
				recorder.stop();
				recorder.release();
				uploadRecording();
				return true;
			}
		});
		return didHandleRotary || super.dispatchKeyEvent(event); // didHandleRotary will be what you return in the interface methods above. If true, super will not be called.
	}
	void uploadRecording() {
		//upload file here
		text.setText("Uploading recording, please wait");
		//make request
		RequestBody requestFile =
				RequestBody.create(
						MediaType.parse("audio/mpeg"),
						outputFile
				);

		// MultipartBody.Part is used to send also the actual file name
		MultipartBody.Part body =
				MultipartBody.Part.createFormData("keywords", outputFile.getName(), requestFile);
		GMFOOD.api.makeQuote(vendorId, body).enqueue(quoteCallback);
	}
	Callback<QuoteResponse> quoteCallback = new Callback<QuoteResponse> () {
		@Override
		public void onResponse(retrofit2.Call<QuoteResponse> call, retrofit2.Response<QuoteResponse> response) {
			text.setText("Done Uploading");
			showConfirmation(response.body());
		}

		@Override
		public void onFailure(retrofit2.Call<QuoteResponse> call, Throwable e) {
			text.setText("Err in ordering");
			Log.e(TAG, e.toString());
			finish();
		}
	};
	void showConfirmation(final QuoteResponse res) {
		//get the price
		String priceStr = "$"+res.priceDollars+String.format("%02d", res.priceCents);
		new AlertDialog.Builder(this)
				.setTitle("Confirm order: "+priceStr)
				.setMessage("Do you really want to pay "+priceStr+"?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(RecordOrderActivity.this, "Placing Order", Toast.LENGTH_LONG).show();
						//TODO: make payment
						GMFOOD.api.pay(res.orderId).enqueue(new Callback<String>() {
							@Override
							public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
								Toast.makeText(getApplicationContext(), "Order went through", Toast.LENGTH_LONG).show();
							}

							@Override
							public void onFailure(retrofit2.Call<String> call, Throwable t) {
								Toast.makeText(getApplicationContext(), "Payment not successful", Toast.LENGTH_LONG).show();
							}
						});
						finish();
					}})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						finish();
					}
				}).show();
	}
}
