package com.titus.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<PantryItem> items = new ArrayList<>();

    public PantryAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setItems(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PantryItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reuse an existing row when possible.
        View row = convertView;

        if (row == null) {
            row = inflater.inflate(R.layout.item_pantry, parent, false);
        }

        TextView textName = row.findViewById(R.id.textIngredientName);
        TextView textQuantity = row.findViewById(R.id.textIngredientQuantity);

        PantryItem item = getItem(position);

        // Display 500 instead of 500.0, without rounding the quantity.
        String quantity = BigDecimal.valueOf(item.getQuantity())
                .stripTrailingZeros()
                .toPlainString();

        textName.setText(item.getName());
        textQuantity.setText(context.getString(
                R.string.pantry_quantity_display,
                quantity,
                item.getUnit()));

        return row;
    }
}