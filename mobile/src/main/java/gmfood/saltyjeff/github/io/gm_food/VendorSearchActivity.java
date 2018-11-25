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
		EditText searchBar = (EditText) findViewById(R.id.editText);
		searchBar.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					((SearchResultAdapter)listAdapter).vendors.add(new Vendor());
					listAdapter.notifyDataSetChanged();
					return true;
				}
				return false;
			}
		});
	}
}
