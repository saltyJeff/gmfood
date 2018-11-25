package gmfood.saltyjeff.github.io.gm_food;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class MenuViewHolder extends RecyclerView.ViewHolder {
	TextView name;
	TextView price;
	public MenuViewHolder(View itemView) {
		super(itemView);
		name = itemView.findViewById(R.id.menuName);
		price = itemView.findViewById(R.id.menuPrice);
	}

	public void renderMenu(MenuItem item) {
		name.setText(item.name);
		price.setText("$"+item.priceDollars+"."+String.format("%02d", item.priceCents));
	}
}
