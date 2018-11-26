package gmfood.saltyjeff.github.io.gm_food;

import android.Manifest;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import gmfood.saltyjeff.github.io.gm_food.apistuff.GMFOOD;
import gmfood.saltyjeff.github.io.gm_food.apistuff.OptionBody;
import locationprovider.davidserrano.com.LocationProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorSearchActivity extends AppCompatActivity {
	RecyclerView listView;
	RecyclerView.Adapter listAdapter;
	RecyclerView.LayoutManager listManager;
	public static final String TAG = "VENDOR SEARCH";
	public double lat;
	public double lon;
	Button searchButt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vendor_search);

		//check location perm
		Dexter.withActivity(this)
				.withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
				.withListener(new PermissionListener() {
					@Override
					public void onPermissionGranted(PermissionGrantedResponse response) {
						pollLocationData();
					}

					@Override
					public void onPermissionDenied(PermissionDeniedResponse response) {

					}

					@Override
					public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

					}
				})
				.check();
		listManager = new LinearLayoutManager(this);
		((LinearLayoutManager) listManager).setOrientation(LinearLayoutManager.HORIZONTAL);
		listAdapter = new SearchResultAdapter();
		listView = (RecyclerView) findViewById(R.id.vendorList);
		listView.setLayoutManager(listManager);
		listView.setAdapter(listAdapter);
		searchButt = (Button)findViewById(R.id.editText);
		searchButt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//record audio file
				recordAudioToFile();
			}
		});
		//delayEdit();
	}
	Callback<List<Vendor>> optionCallback = new Callback<List<Vendor>>() {
		@Override
		public void onResponse(Call<List<Vendor>> call, Response<List<Vendor>> response) {
			((SearchResultAdapter)listAdapter).vendors.clear();
			for(Vendor v : response.body()) {
				((SearchResultAdapter)listAdapter).vendors.add(v);
			}
			listAdapter.notifyDataSetChanged();
		}
		@Override
		public void onFailure(Call<List<Vendor>> call, Throwable t) {
			Log.e(TAG, t.toString());
		}
	};

	public void pollLocationData() {
		LocationProvider.LocationCallback callback = new LocationProvider.LocationCallback() {
			@Override
			public void onNewLocationAvailable(float lat, float lon) {
				VendorSearchActivity.this.lat = lat;
				VendorSearchActivity.this.lon = lon;
				//location update
			}

			@Override
			public void locationServicesNotEnabled() {
				//failed finding a location
			}

			@Override
			public void updateLocationInBackground(float lat, float lon) {
				//if a listener returns after the main locationAvailable callback, it will go here
				VendorSearchActivity.this.lat = lat;
				VendorSearchActivity.this.lon = lon;
			}

			@Override
			public void networkListenerInitialised() {
				//when the library switched from GPS only to GPS & network
			}

			@Override
			public void locationRequestStopped() {

			}
		};

		//initialise an instance with the two required parameters
		LocationProvider locationProvider = new LocationProvider.Builder()
				.setContext(this)
				.setListener(callback)
				.create();

		//start getting location
		locationProvider.requestLocation();
	}
	File outputFile;
	MediaRecorder recorder = new MediaRecorder();
	boolean died = false;
	void stopRecording() {
		if(died) {
			return;
		}
		died = true;
		Log.e(TAG, "Ending recordinig of search term");
		recorder.stop();
		recorder.release();
		uploadAudio();
	}
	void recordAudioToFile() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				while(System.currentTimeMillis() < start + 5000);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						stopRecording();
					}
				});
			}
		}).start();
		try {
			File outputDir = getCacheDir(); // context being the Activity pointer
			outputFile = File.createTempFile("recordOrder", ".mp3", outputDir);
			recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
			recorder.setAudioChannels(1);
			recorder.setAudioSamplingRate(16000);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			FileOutputStream fos = new FileOutputStream(outputFile);
			recorder.setOutputFile(fos.getFD());
			recorder.prepare();
			recorder.start();
			searchButt.setText("Stop Voice");
			searchButt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					stopRecording();
				}
			});
		}
		catch(IOException e) {
			Log.e("IO ERR", e.toString());
			finish();
		}
	}
	void uploadAudio() {
		searchButt.setText("VOICE SEARCH");
		RequestBody requestFile =
				RequestBody.create(
						MediaType.parse("audio/mp3"),
						outputFile
				);

		// MultipartBody.Part is used to send also the actual file name
		MultipartBody.Part body =
				MultipartBody.Part.createFormData("keywords", outputFile.getName(), requestFile);
		GMFOOD.api.getOptions(body).enqueue(optionCallback);
	}
}
