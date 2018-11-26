package gmfood.saltyjeff.github.io.gm_food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gm.android.vehicle.hardware.RotaryControlHelper;

import java.util.List;

import gmfood.saltyjeff.github.io.gm_food.apistuff.GMFOOD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
	RecyclerView listView;
	RecyclerView.Adapter listAdapter;
	RecyclerView.LayoutManager listManager;
	public static final String TAG = "ORDER ACTIVITY";
	String vendorId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vendorId = getIntent().getStringExtra("id");
		setContentView(R.layout.activity_order);
		((TextView)findViewById(R.id.menuVendor)).setText(getIntent().getStringExtra("name"));
		listManager = new LinearLayoutManager(this);
		((LinearLayoutManager) listManager).setOrientation(LinearLayoutManager.VERTICAL);
		listAdapter = new MenuAdapter();
		listView = (RecyclerView) findViewById(R.id.menuList);
		listView.setLayoutManager(listManager);
		listView.setAdapter(listAdapter);
		((Button)findViewById(R.id.orderButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				recordOrderTransition();
			}
		});
		fillData();
	}
	void fillData() {
		GMFOOD.api.getMenu(vendorId).enqueue(new Callback<List<MenuItem>>() {
			@Override
			public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
				MenuAdapter adapter = (MenuAdapter) listAdapter;
				adapter.menuItems.clear();
				for(MenuItem i : response.body()) {
					adapter.menuItems.add(i);
				}
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Call<List<MenuItem>> call, Throwable t) {
				Log.e(TAG, t.toString());
			}
		});
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
				recordOrderTransition();
				return true;
			}
		});
		return didHandleRotary || super.dispatchKeyEvent(event); // didHandleRotary will be what you return in the interface methods above. If true, super will not be called.
	}
	void recordOrderTransition() {
		Intent intent = new Intent(this, RecordOrderActivity.class);
		intent.putExtra("id", this.vendorId);
		startActivity(intent);
	}
}
