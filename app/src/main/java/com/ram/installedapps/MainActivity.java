package com.ram.installedapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleCoroutineScope;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    PackageManager packageManager;
    String[] applications;
    List<AppList> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = new ArrayList<>();
        packageManager = getPackageManager();
        int c =1;

//        try {
            getInstalledApplications();
//            Log.d("Ranjith", c + " try block: " );
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.d("Ranjith", c + " onCreate: " + e.getMessage());
//            c++;
//
//
//        }
    }


    private void getInstalledApplications() {
        final PackageManager packageManager = getPackageManager();
//        @SuppressLint("QueryPermissionsNeeded") List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        int c = 1;
        // Get the list of installed packages
        List<PackageInfo> signingInfo = getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
//        Log.d("Ranjith", "getCertificateDetails: " + signingInfo.size());
        for (PackageInfo pkgInfo : signingInfo){
            Drawable iconApp = getPackageManager().getApplicationIcon(pkgInfo.applicationInfo);
            String appLabel = (String) getPackageManager().getApplicationLabel(pkgInfo.applicationInfo);
            mList.add(new AppList(appLabel, pkgInfo.packageName, iconApp));
//            Log.d("Ranjith", c+ ". getInstalledApplications: " + pkgInfo.packageName);
            c++;
        }

//        for (ApplicationInfo appInfo : installedApplications) {
//            Drawable icon = getPackageManager().getApplicationIcon(appInfo);
//            CharSequence label = getPackageManager().getApplicationLabel(appInfo);
//            mList.add(new AppList((String) label, appInfo.packageName, icon));
//        }
        MyAppListAdapter adapter = new MyAppListAdapter(mList, this);
        ListView appListView = findViewById(R.id.app_list_view);
        appListView.setAdapter(adapter);
    }
}