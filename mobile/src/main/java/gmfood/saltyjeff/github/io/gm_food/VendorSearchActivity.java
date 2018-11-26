package gmfood.saltyjeff.github.io.gm_food;

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

import java.util.List;

import gmfood.saltyjeff.github.io.gm_food.apistuff.GMFOOD;
import gmfood.saltyjeff.github.io.gm_food.apistuff.OptionBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorSearchActivity extends AppCompatActivity {
	RecyclerView listView;
	RecyclerView.Adapter listAdapter;
	RecyclerView.LayoutManager listManager;
	public static final String TAG = "VENDOR SEARCH";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vendor_search);
		listManager = new LinearLayoutManager(this);
		((LinearLayoutManager) listManager).setOrientation(LinearLayoutManager.HORIZONTAL);
		listAdapter = new SearchResultAdapter();
		listView = (RecyclerView) findViewById(R.id.vendorList);
		listView.setLayoutManager(listManager);
		listView.setAdapter(listAdapter);
		final EditText searchBar = (EditText) findViewById(R.id.editText);
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
	}
}
