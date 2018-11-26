package gmfood.saltyjeff.github.io.gm_food;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

class SearchResultViewHolder extends RecyclerView.ViewHolder {
	TextView name;
	TextView price;
	Button orderButton;
	View view;
	ImageView imgView;
	public SearchResultViewHolder(View itemView) {
		super(itemView);
		view = itemView;
		name = itemView.findViewById(R.id.vendorName);
		price = itemView.findViewById(R.id.vendorPrice);
		orderButton = itemView.findViewById(R.id.placeOrder);
		imgView = itemView.findViewById(R.id.vendorImg);
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
		Glide.with(view).load(vendor.img).into(imgView);
	}
	void launchOrderActivity(Vendor v) {
		Intent intent = new Intent(name.getContext(), OrderActivity.class);
		//TODO: REmove hard coding
		v.id = "299dd8fe-3732-4934-bf43-83263bcddd98";
		intent.putExtra("name", v.name);
		intent.putExtra("id", v.id);
		name.getContext().startActivity(intent);
	}
}
