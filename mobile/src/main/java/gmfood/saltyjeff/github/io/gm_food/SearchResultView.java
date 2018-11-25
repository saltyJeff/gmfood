package gmfood.saltyjeff.github.io.gm_food;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class SearchResultViewHolder extends RecyclerView.ViewHolder {
	TextView name;
	TextView price;
	Button orderButton;
	public SearchResultViewHolder(View itemView) {
		super(itemView);
		name = itemView.findViewById(R.id.vendorName);
		price = itemView.findViewById(R.id.vendorPrice);
		orderButton = itemView.findViewById(R.id.placeOrder);
	}

	public void renderVendor(final Vendor vendor) {
		name.setText(vendor.name+"\t");
		String priceStr = "";
		for (int i = 0; i < vendor.price; i++) {
			priceStr += "$";
		}
		price.setText(priceStr);
		orderButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				launchOrderActivity(vendor);
			}
		});
	}
	void launchOrderActivity(Vendor v) {
		Intent intent = new Intent(name.getContext(), OrderActivity.class);
		intent.putExtra("name", v.name);
		intent.putExtra("id", v.id);
		name.getContext().startActivity(intent);
	}
}
