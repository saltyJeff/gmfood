package gmfood.saltyjeff.github.io.gm_food;

import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {
	public List<MenuItem> menuItems = new ArrayList<>();
	@NonNull
	@Override
	public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.menu_view, parent, false);
		MenuViewHolder holder = new MenuViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
		holder.renderMenu(menuItems.get(position));
	}

	@Override
	public int getItemCount() {
		return menuItems.size();
	}
}
