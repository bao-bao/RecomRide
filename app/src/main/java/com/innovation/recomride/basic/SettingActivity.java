package com.innovation.recomride.basic;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.amap.api.maps2d.MapsInitializer;
import com.innovation.recomride.R;
import com.innovation.recomride.route.RouteActivity;
import com.innovation.recomride.util.SettingData;
import com.innovation.recomride.util.ToastUtil;
import com.innovation.recomride.view.FeatureView;

public class SettingActivity extends ListActivity {

    private static class DemoDetails {
        private final int titleId;
        private final int value;

        DemoDetails(int titleId, int value) {
            super();
            this.titleId = titleId;
            this.value = value;
        }
    }

    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
        CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }
            DemoDetails demo = getItem(position);
            if (demo != null) {
                featureView.setTitleId(demo.titleId);
            }
            return featureView;
        }
    }

    private static final DemoDetails[] demos = {
            new DemoDetails(R.string.safe_set, SettingData.SAFE),
            new DemoDetails(R.string.comfort_set, SettingData.COMFORT),
            new DemoDetails(R.string.satisfied_set, SettingData.SATISFIED)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ListAdapter adapter = new CustomArrayAdapter(
                this.getApplicationContext(), demos);
        setListAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DemoDetails demo = (DemoDetails) getListAdapter().getItem(position);
        SettingData.setSetting(demo.value);
        ToastUtil.show(SettingActivity.this, "修改默认个性化至" + getString(demo.titleId));
    }
}
