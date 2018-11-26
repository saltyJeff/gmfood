package gmfood.saltyjeff.github.io.gm_food;

import android.Manifest;
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

import java.util.List;

import gmfood.saltyjeff.github.io.gm_food.apistuff.GMFOOD;
import gmfood.saltyjeff.github.io.gm_food.apistuff.OptionBody;
import locationprovider.davidserrano.com.LocationProvider;
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
	EditText searchBar;
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
		final EditText searchBar = (EditText) findViewById(R.id.editText);
		this.searchBar = searchBar;
		searchBar.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					OptionBody opts = new OptionBody();
					opts.lat = 0;
					opts.lon = 0;
					opts.keywords = searchBar.getText().toString();
					//TODO: Perform Info Read
					GMFOOD.api.getOptions(opts).enqueue(new Callback<List<Vendor>>() {
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
					});
					return true;
				}
				return false;
			}
		});
		delayEdit();
	}
	public void delayEdit() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				while(System.currentTimeMillis() < startTime + 3000);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						VendorSearchActivity.this.searchBar.setText("Fast Food");
					}
				});
			}
		}).start();
	}
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
}
