package gmfood.saltyjeff.github.io.gm_food;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

class MenuViewHolder extends RecyclerView.ViewHolder {
	TextView name;
	TextView price;
	View view;
	ImageView imgView;
	public MenuViewHolder(View itemView) {
		super(itemView);
		view = itemView;
		name = itemView.findViewById(R.id.menuName);
		price = itemView.findViewById(R.id.menuPrice);
		imgView = itemView.findViewById(R.id.menuImg);
	}

	public void renderMenu(MenuItem item) {
		name.setText(item.name);
		price.setText("$"+item.price+".00");
		Glide.with(view).load(item.img).into(imgView);
	}
}
