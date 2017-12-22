package com.example.chinmoydash.farmersupporttech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinmoydash.farmersupporttech.firebasedata.News;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NewsFragment extends Fragment {

    View view;
    @BindView(R.id.rvNews)
    ListView mNews;
    FragmentActivity mActivity;

    FirebaseDatabase mDatabase;
    DatabaseReference mNewsReference;
    ChildEventListener mNewsChildListener;

    MyAdapter newsAdapter;
    Vector<String> keys;
    Vector<String> comments;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mDatabase = FirebaseDatabase.getInstance();
        mNewsReference = mDatabase.getReference().child("news");
        newsAdapter = new MyAdapter(mActivity, R.layout.item_news_list);
        mNews.setAdapter(newsAdapter);
        keys = new Vector<String>();
        comments = new Vector<String>();
        mNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent newsDetails = new Intent(mActivity, NewsDetails.class);
                newsDetails.putExtra(getString(R.string.newsKey), keys.get(pos));
                newsDetails.putExtra(getString(R.string.newkey), newsAdapter.getItem(pos).message);
                newsDetails.putExtra(getString(R.string.name_small), newsAdapter.getItem(pos).name);
                startActivity(newsDetails);
                Log.e(getString(R.string.intent_data), keys.get(pos) + "\n" + newsAdapter.getItem(pos).message + "\n" + newsAdapter.getItem(pos).name);
            }
        });
    }

    @OnClick(R.id.fabAddNews)
    void addDialog() {
        new AddNewsFragment().show(mActivity.getSupportFragmentManager(), "add");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mNewsChildListener == null) {
            mNewsChildListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    News item = dataSnapshot.getValue(News.class);
                    newsAdapter.add(item);
                    keys.add(dataSnapshot.getKey());
                    if (dataSnapshot.hasChild(getString(R.string.commentsKey))) {
                        long noOfComments = dataSnapshot.child(getString(R.string.commentsKey)).getChildrenCount();
                        comments.add(String.valueOf(noOfComments) + getString(R.string.comments));
                    } else {
                        comments.add(getString(R.string.nocomments));
                    }
                    Log.e((getString(R.string.message)), String.valueOf(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    int changedKeyIndex = keys.indexOf(dataSnapshot.getKey());
                    News unChangedNews = newsAdapter.getItem(changedKeyIndex);
                    newsAdapter.remove(unChangedNews);
                    newsAdapter.insert(dataSnapshot.getValue(News.class), changedKeyIndex);
                    comments.remove(changedKeyIndex);

                    if (dataSnapshot.hasChild(getString(R.string.commentsKey))) {
                        long noOfComments = dataSnapshot.child(getString(R.string.commentsKey)).getChildrenCount();
                        comments.add(changedKeyIndex, String.valueOf(noOfComments) + getString(R.string.comments));
                    } else {
                        comments.add(changedKeyIndex,  getString(R.string.nocomments));
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String removedKey = dataSnapshot.getKey();
                    News removedNews = newsAdapter.getItem(keys.indexOf(removedKey));
                    newsAdapter.remove(removedNews);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e((getString(R.string.db_error)), databaseError.getMessage());
                    Toast.makeText(mActivity, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
        }
        mNewsReference.addChildEventListener(mNewsChildListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mNewsReference != null) {
            mNewsReference.removeEventListener(mNewsChildListener);
        }
        newsAdapter.clear();
        keys.clear();
        comments.clear();
    }

    class MyAdapter extends ArrayAdapter<News> {

        public MyAdapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_news_list, parent, false);
                holder = new MyViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MyViewHolder) convertView.getTag();
            }

            News itemNews = getItem(position);
            holder.tvnews.setText(itemNews.getMessage());
            holder.tvcomments.setText(comments.get(position));
            return convertView;
        }

        class MyViewHolder {
            @BindView(R.id.tvNews)
            TextView tvnews;
            @BindView(R.id.tvComments)
            TextView tvcomments;

            public MyViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


}