package com.zork.class27demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zork.class27demo.R;
import com.zork.class27demo.activities.HomeActivity;
import com.zork.class27demo.activities.OrderFormActivity;
import com.zork.class27demo.models.Product;

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
    // TODO: Step 3-1: change to ProductListViewHolder
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO: Step 1-7: Inflate fragment
        View productFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_product_list, parent, false);
        // TODO: Step 1-9: Attach fragment to ViewHolder
        return new ProductListViewHolder(productFragment);
    }

    @Override
    // TODO: Step 2-5: Bind data items to Fragments inside of ViewHolders
    // TODO: Step 3-1: change to ProductListViewHolder
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        Product product = products.get(position);
        TextView productNameTextView = holder.itemView.findViewById(R.id.fragmentProductNameTextView);
        TextView productDateTextView = holder.itemView.findViewById(R.id.fragmentProductDateTextView);
        TextView productCategoryTextView = holder.itemView.findViewById(R.id.fragmentProductCategoryTextView);

        productNameTextView.setText(position + ". " + product.getName());
        productDateTextView.setText(product.getDateCreated().toString());
        productCategoryTextView.setText(product.getProductCategory().toString());

        // TODO: Step 3-3: Make OnClickHandler so we can interact with the RecyclerView items
        View productViewHolder = holder.itemView;
        productViewHolder.setOnClickListener(v ->{
            Intent goToOrderFormIntent = new Intent(callingActivity, OrderFormActivity.class);
            goToOrderFormIntent.putExtra(HomeActivity.PRODUCT_NAME_EXTRA_TAG, product.getName());
            callingActivity.startActivity(goToOrderFormIntent);
        });
    }

    @Override
    public int getItemCount() {
        // TODO: Step 1-10: For testing purposes, hardcode a large number of items
//        return 100;
        // TODO: Step 2-5: Make the size of the list dynamic
        return products.size();
    }

    // TODO: Step 1-8: Make a ViewHolder class to hold a fragment
    public static class ProductListViewHolder extends RecyclerView.ViewHolder{
        public ProductListViewHolder(View fragmentItemView) {
            super(fragmentItemView);
        }
    }
}
