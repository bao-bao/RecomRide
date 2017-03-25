package com.innovation.recomride.route;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.innovation.recomride.R;
import com.innovation.recomride.draw.DrawLine;
import com.innovation.recomride.util.AMapUtil;
import com.innovation.recomride.util.SettingData;
import com.innovation.recomride.util.ToastUtil;


/**
 * AMapV1地图中简单介绍route搜索
 */
public class RouteActivity extends Activity implements OnMarkerClickListener,
        OnMapClickListener, OnInfoWindowClickListener, InfoWindowAdapter,
        OnClickListener {
    private AMap aMap;
    private MapView mapView;

    private EditText startTextView;
    private EditText endTextView;

    private boolean isClickStart = false;
    private boolean isClickTarget = false;
    private Marker startMk, targetMk;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.route_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(bundle);// 此方法必须重写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            registerListener();
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.286035, 121.500242), 15));
        }
        RouteSearch routeSearch = new RouteSearch(this);
        startTextView = (EditText) findViewById(R.id.autotextview_roadsearch_start);
        endTextView = (EditText) findViewById(R.id.autotextview_roadsearch_goals);
        ImageButton startImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_startoption);
        startImageButton.setOnClickListener(this);
        ImageButton endImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_endoption);
        endImageButton.setOnClickListener(this);
        ImageButton routeSearchImagebtn = (ImageButton) findViewById(R.id.imagebtn_roadsearch_search);
        routeSearchImagebtn.setOnClickListener(this);
        Button safeButton = (Button) findViewById(R.id.safe);
        safeButton.setOnClickListener(this);
        Button comfortButton = (Button) findViewById(R.id.comfort);
        comfortButton.setOnClickListener(this);
        Button quickButton = (Button) findViewById(R.id.quick);
        quickButton.setOnClickListener(this);
        Button satisfiedButton = (Button) findViewById(R.id.satisfiedButton);
        satisfiedButton.setOnClickListener(this);
        Button shortestButton = (Button) findViewById(R.id.shortestButton);
        shortestButton.setOnClickListener(this);
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 在地图上选取起点
     */
    private void startImagePoint() {
        ToastUtil.show(RouteActivity.this, "在地图上点击您的起点");
        isClickStart = true;
        isClickTarget = false;
        registerListener();
    }

    /**
     * 在地图上选取终点
     */
    private void endImagePoint() {
        ToastUtil.show(RouteActivity.this, "在地图上点击您的终点");
        isClickTarget = true;
        isClickStart = false;
        registerListener();
    }

    /**
     * 点击搜索按钮开始Route搜索
     */
    public void searchRoute() {
        String strStart = startTextView.getText().toString().trim();
        String strEnd = endTextView.getText().toString().trim();
        if (strStart.length() == 0) {
            ToastUtil.show(RouteActivity.this, "请选择起点");
            return;
        }
        if (strEnd.length() == 0) {
            ToastUtil.show(RouteActivity.this, "请选择终点");
            return;
        }
        if (strStart.equals(strEnd)) {
            ToastUtil.show(RouteActivity.this, "起点与终点距离很近，您可以步行前往");
            return;
        }

        switch (SettingData.getSetting()) {
            case SettingData.SAFE:
                aMap.clear();
                aMap.addPolyline(DrawLine.getSafe());
                break;
            case SettingData.SATISFIED:
                aMap.clear();
                aMap.addPolyline(DrawLine.getQuick());
                break;
            case SettingData.COMFORT:
                aMap.clear();
                aMap.addPolyline(DrawLine.getComfort());
                break;
            default:
                aMap.clear();
                aMap.addPolyline(DrawLine.getShort());
                break;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        isClickStart = false;
        isClickTarget = false;
        if (marker.equals(startMk)) {
            startTextView.setText("地图上的起点");
            LatLonPoint startPoint = AMapUtil.convertToLatLonPoint(startMk.getPosition());
            startMk.hideInfoWindow();
            startMk.remove();
        } else if (marker.equals(targetMk)) {
            endTextView.setText("地图上的终点");
            LatLonPoint endPoint = AMapUtil.convertToLatLonPoint(targetMk.getPosition());
            targetMk.hideInfoWindow();
            targetMk.remove();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latng) {
        if (isClickStart) {
            startMk = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 1)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.point)).position(latng)
                    .title("点击选择为起点"));
            startMk.showInfoWindow();
        } else if (isClickTarget) {
            targetMk = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 1)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.point)).position(latng)
                    .title("点击选择为目的地"));
            targetMk.showInfoWindow();
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(RouteActivity.this);
        aMap.setOnMarkerClickListener(RouteActivity.this);
        aMap.setOnInfoWindowClickListener(RouteActivity.this);
        aMap.setInfoWindowAdapter(RouteActivity.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagebtn_roadsearch_startoption:
                startImagePoint();
                break;
            case R.id.imagebtn_roadsearch_endoption:
                endImagePoint();
                break;
            case R.id.imagebtn_roadsearch_search:
                searchRoute();
                break;
            case R.id.safe:
                aMap.clear();
                aMap.addPolyline(DrawLine.getSafe());
                break;
            case R.id.comfort:
                aMap.clear();
                aMap.addPolyline(DrawLine.getComfort());
                break;
            case R.id.quick:
                aMap.clear();
                aMap.addPolyline(DrawLine.getQuick());
                break;
            case R.id.shortestButton:
                aMap.clear();
                aMap.addPolyline(DrawLine.getShort());
                break;
            case R.id.satisfiedButton:
                aMap.clear();
                aMap.addPolyline(DrawLine.getSatisfied());
                break;
            default:
                break;
        }
    }
}

