package com.kimbrelk.android.hamlogger.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import com.kimbrelk.android.hamlogger.utils.Listable;
import java.util.ArrayList;
import java.util.List;

public final class ClickToSelectEditText<T extends Listable> extends AppCompatEditText {
	
	private List<T> items;
	private String[] listableItems;
	
	private OnItemSelectedListener<T> onItemSelectedListener;
	
	public ClickToSelectEditText(Context context) {
		super(context);
	}
	
	public ClickToSelectEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ClickToSelectEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected final void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setFocusable(false);
		setClickable(true);
		setLongClickable(false);
	}
	
	public final void setItems(List<T> items) {
		this.items = items;
		this.listableItems = new String[items.size()];
		for(int a=0; a<listableItems.length; a++) {
			listableItems[a] = items.get(a).getLabel();
		}
		configureOnClickListener();
	}
	
	public final void setItems(@ArrayRes int stringArrayRes) {
		String[] stringArray = getResources().getStringArray(stringArrayRes);
		setItems(stringArray);
	}
	
	public final void setItems(String[] stringArray) {
		List<T> items = new ArrayList<T>();
		for(int a=0; a<stringArray.length; a++) {
			items.add((T) new ListableString(stringArray[a]));
		}
		setItems(items);
	}
	
	public final void setItems(T[] array) {
		List<T> items = new ArrayList<T>();
		for(int a=0; a<array.length; a++) {
			items.add(array[a]);
		}
		setItems(items);
	}
	
	public List<T> getItems() {
		return items;
	}
	
	private void configureOnClickListener() {
		setOnClickListener(
			view -> {
				PopupMenu popupMenu = new PopupMenu(getContext(), ClickToSelectEditText.this,
						Gravity.TOP | Gravity.START | Gravity.LEFT);
				for(int pos=0; pos<listableItems.length; pos++) {
					String itemTitle = listableItems[pos];
					popupMenu.getMenu().add(0, pos, pos, itemTitle);
				}
				popupMenu.setOnMenuItemClickListener(
					new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							if (onItemSelectedListener != null) {
								onItemSelectedListener.onItemSelected(items.get(item.getItemId()), item.getItemId());
							}
							return true;
						}
					}
				);
				popupMenu.show();
			}
		);
	}
	
	public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
		this.onItemSelectedListener = onItemSelectedListener;
	}
	
	public interface OnItemSelectedListener<T> {
		void onItemSelected(T item, int selectedIndex);
	}
	
	public class ListableString implements Listable {
		
		private String item;
		
		public ListableString(String item) {
			this.item = item;
		}
		
		@NonNull
		@Override
		public String getLabel() {
			return item;
		}
	}
}