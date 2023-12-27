package com.ram.installedapps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAppListAdapter extends BaseAdapter {

    Context context;


    List<AppList> mList;

    MyAppListAdapter(List<AppList> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }


    @Override
    public int getCount() {
        if (mList!=null)
            return mList.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //1. inflate the custom layout
        view = LayoutInflater.from(context).inflate(R.layout.installed_apps_list, viewGroup, false);
        AppList app = mList.get(position);
        //2. bind the views
        TextView tvAppName = view.findViewById(R.id.tv_list_view_appname);
        TextView tvAppPkgName = view.findViewById(R.id.tv_list_view_package);
        ImageView ivAppIcon = view.findViewById(R.id.iv_list_view_icon);

        //3. populate the views
        tvAppName.setText(app.getAppName());
        Log.d("TAG", "getView: "+app.getAppName());
        tvAppPkgName.setText(app.getPackageName());
        ivAppIcon.setImageDrawable(app.getAppIcon());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AppDetails.class);
                intent.putExtra("PackageName", app.getPackageName());
                intent.putExtra("AppName", app.getAppName());
                intent.putExtra("AppCount", getCount());
                context.startActivity(intent);
            }
        });
        return view;
    }
}
