package gmfood.saltyjeff.github.io.gm_food;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
	public List<Vendor> vendors = new ArrayList<>();
	@NonNull
	@Override
	public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.vendor_view, parent, false);
		SearchResultViewHolder holder = new SearchResultViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
		holder.renderVendor(vendors.get(position));
	}

	@Override
	public int getItemCount() {
		return vendors.size();
	}
}
