package com.zork.class27demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Product;
import com.zork.class27demo.R;
import com.zork.class27demo.activities.HomeActivity;
import com.zork.class27demo.activities.OrderFormActivity;


import java.util.List;

// TODO: Step 1-4: Make a class whose sole purpose is to manage RecyclerViews: a RecyclerView.Adapter
// TODO: Step 3-1: Clean up the RecyclerView.Adapter references to actually use ProductListRecyclerViewAdapter
// (You'll need to change it in the methods below also)
public class ProductListRecyclerViewAdapter extends RecyclerView.Adapter<ProductListRecyclerViewAdapter.ProductListViewHolder> {

    // TODO: Step 2-3: Hand in data items
    List<Product> products;
    // TODO: Step 3-2: Hand in the activity context
    Context callingActivity;

    // TODO: Step 3-2: Hand in the activity context
    public ProductListRecyclerViewAdapter(List<Product> _products, Context _callingActivity) {
        this.products = _products;
        this.callingActivity = _callingActivity;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_product_list, parent, false);
        return new ProductListViewHolder(productFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        Product product = products.get(position);
        TextView productNameTextView = holder.itemView.findViewById(R.id.fragmentProductNameTextView);
        TextView productDateTextView = holder.itemView.findViewById(R.id.fragmentProductDateTextView);
        TextView productCategoryTextView = holder.itemView.findViewById(R.id.fragmentProductCategoryTextView);

        productNameTextView.setText(position + ". " + product.getName());
        productDateTextView.setText(product.getDateCreated().toString());
        productCategoryTextView.setText(product.getProductCategory().toString());

        View productViewHolder = holder.itemView;
        productViewHolder.setOnClickListener(v ->{
            Intent goToOrderFormIntent = new Intent(callingActivity, OrderFormActivity.class);
            goToOrderFormIntent.putExtra(HomeActivity.PRODUCT_NAME_EXTRA_TAG, product.getName());
            callingActivity.startActivity(goToOrderFormIntent);
        });
    }

    @Override
    public int getItemCount() {
//        return 100;

        return products.size();
    }

    public static class ProductListViewHolder extends RecyclerView.ViewHolder{
        public ProductListViewHolder(View fragmentItemView) {
            super(fragmentItemView);
        }
    }
}
