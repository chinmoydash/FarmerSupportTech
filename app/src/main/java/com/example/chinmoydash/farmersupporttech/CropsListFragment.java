package com.example.chinmoydash.farmersupporttech;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chinmoydash.farmersupporttech.background.CropSyncJobs;


public class CropsListFragment extends Fragment {

    private int[] mImageResIds;
    private String[] mCropNames;
    private String[] mCropDescriptions;
    private OnCropSelected mListener;

    public static CropsListFragment newInstance() {
        return new CropsListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnCropSelected) {
            mListener = (OnCropSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnCropSelected.");
        }

        final Resources resources = context.getResources();

        mCropNames = resources.getStringArray(R.array.names);
        mCropDescriptions = resources.getStringArray(R.array.descriptions);
        final TypedArray typedArray = resources.obtainTypedArray(R.array.images);
        final int imageCount = mCropNames.length;
        mImageResIds = new int[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageResIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CropSyncJobs.initialize(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crop_list, container, false);
        final Activity activity = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerView.setAdapter(new CropListAdapter(activity));
        return view;
    }

    public interface OnCropSelected {
        void onCropSelected(int imageResId, String name,
                            String description);
    }

    class CropListAdapter extends RecyclerView.Adapter<ViewHolder> {

        private LayoutInflater mLayoutInflater;

        public CropListAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return new ViewHolder(mLayoutInflater
                    .inflate(R.layout.recycler_item_crop, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            final int imageResId = mImageResIds[position];
            final String name = mCropNames[position];
            final String description = mCropDescriptions[position];
            viewHolder.setData(imageResId, name);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCropSelected(imageResId, name, description);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCropNames.length;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Views
        private ImageView mImageView;
        private TextView mNameTextView;

        private ViewHolder(View itemView) {
            super(itemView);

            // Get references to image and name.
            mImageView = (ImageView) itemView.findViewById(R.id.crop_image);
            mNameTextView = (TextView) itemView.findViewById(R.id.crop_name);
        }

        private void setData(int imageResId, String name) {
            mImageView.setImageResource(imageResId);
            mNameTextView.setText(name);
        }
    }


}
