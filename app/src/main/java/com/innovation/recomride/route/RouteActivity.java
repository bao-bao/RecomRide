package com.innovation.recomride.route;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.innovation.recomride.R;
import com.innovation.recomride.route.RouteSearchPoiDialog.OnListItemClick;
import com.innovation.recomride.util.AMapUtil;
import com.innovation.recomride.util.ToastUtil;

import static com.innovation.recomride.util.ChString.address;

/**
 * AMapV1地图中简单介绍route搜索
 */
public class RouteActivity extends Activity implements OnMarkerClickListener,
        OnMapClickListener, OnInfoWindowClickListener, InfoWindowAdapter,
        OnPoiSearchListener, OnRouteSearchListener, OnClickListener {
    private AMap aMap;
    private MapView mapView;

    private EditText startTextView;
    private EditText endTextView;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private String strStart;
    private String strEnd;
    private LatLonPoint startPoint = null;
    private LatLonPoint endPoint = null;
    private PoiSearch.Query startSearchQuery;
    private PoiSearch.Query endSearchQuery;

    private boolean isClickStart = false;
    private boolean isClickTarget = false;
    private Marker startMk, targetMk;
    private RouteSearch routeSearch;

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
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        startTextView = (EditText) findViewById(R.id.autotextview_roadsearch_start);
        endTextView = (EditText) findViewById(R.id.autotextview_roadsearch_goals);
        ImageButton startImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_startoption);
        startImageButton.setOnClickListener(this);
        ImageButton endImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_endoption);
        endImageButton.setOnClickListener(this);
        ImageButton routeSearchImagebtn = (ImageButton) findViewById(R.id.imagebtn_roadsearch_search);
        routeSearchImagebtn.setOnClickListener(this);
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
        strStart = startTextView.getText().toString().trim();
        strEnd = endTextView.getText().toString().trim();
        if (strStart == null || strStart.length() == 0) {
            ToastUtil.show(RouteActivity.this, "请选择起点");
            return;
        }
        if (strEnd == null || strEnd.length() == 0) {
            ToastUtil.show(RouteActivity.this, "请选择终点");
            return;
        }
        if (strStart.equals(strEnd)) {
            ToastUtil.show(RouteActivity.this, "起点与终点距离很近，您可以步行前往");
            return;
        }

        startSearchResult();// 开始搜终点
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        isClickStart = false;
        isClickTarget = false;
        if (marker.equals(startMk)) {
            startTextView.setText("地图上的起点");
            startPoint = AMapUtil.convertToLatLonPoint(startMk.getPosition());
            startMk.hideInfoWindow();
            startMk.remove();
        } else if (marker.equals(targetMk)) {
            endTextView.setText("地图上的终点");
            endPoint = AMapUtil.convertToLatLonPoint(targetMk.getPosition());
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

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 查询路径规划起点
     */
    public void startSearchResult() {
        strStart = startTextView.getText().toString().trim();
        if (startPoint != null && strStart.equals("地图上的起点")) {
            endSearchResult();
        } else {
            showProgressDialog();
            startSearchQuery = new PoiSearch.Query(strStart, "", "010"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
            startSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
            startSearchQuery.setPageSize(20);// 设置每页返回多少条数据
            PoiSearch poiSearch = new PoiSearch(RouteActivity.this,
                    startSearchQuery);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();// 异步poi查询
        }
    }

    /**
     * 查询路径规划终点
     */
    public void endSearchResult() {
        strEnd = endTextView.getText().toString().trim();
        if (endPoint != null && strEnd.equals("地图上的终点")) {
            searchRouteResult(startPoint, endPoint);
        } else {
            showProgressDialog();
            endSearchQuery = new PoiSearch.Query(strEnd, "", "010"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
            endSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
            endSearchQuery.setPageSize(20);// 设置每页返回多少条数据

            PoiSearch poiSearch = new PoiSearch(RouteActivity.this,
                    endSearchQuery);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn(); // 异步poi查询
        }
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        WalkRouteQuery query = new WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
            routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {

    }

    /**
     * POI搜索结果回调
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {// 返回成功
            if (result != null && result.getQuery() != null
                    && result.getPois() != null && result.getPois().size() > 0) {// 搜索poi的结果
                if (result.getQuery().equals(startSearchQuery)) {
                    List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
                    RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
                            RouteActivity.this, poiItems);
                    dialog.setTitle("您要找的起点是:");
                    dialog.show();
                    dialog.setOnListClickListener(new OnListItemClick() {
                        @Override
                        public void onListItemClick(
                                RouteSearchPoiDialog dialog,
                                PoiItem startpoiItem) {
                            startPoint = startpoiItem.getLatLonPoint();
                            strStart = startpoiItem.getTitle();
                            startTextView.setText(strStart);
                            endSearchResult();// 开始搜终点
                        }

                    });
                } else if (result.getQuery().equals(endSearchQuery)) {
                    List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
                    RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
                            RouteActivity.this, poiItems);
                    dialog.setTitle("您要找的终点是:");
                    dialog.show();
                    dialog.setOnListClickListener(new OnListItemClick() {
                        @Override
                        public void onListItemClick(
                                RouteSearchPoiDialog dialog, PoiItem endpoiItem) {
                            endPoint = endpoiItem.getLatLonPoint();
                            strEnd = endpoiItem.getTitle();
                            endTextView.setText(strEnd);
                            searchRouteResult(startPoint, endPoint);// 进行路径规划搜索
                        }

                    });
                }
            } else {
                ToastUtil.show(RouteActivity.this, R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(RouteActivity.this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(RouteActivity.this, R.string.error_key);
        } else {
            ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
                    + rCode);
        }
    }

    /**
     * 公交路线查询回调
     */
    @Override
    public void onBusRouteSearched(BusRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null
                    && result.getPaths().size() > 0) {
                BusPath busPath = result.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                BusRouteOverlay routeOverlay = new BusRouteOverlay(this, aMap,
                        busPath,

                        result.getStartPos(),
                        result.getTargetPos()


                );
                routeOverlay.removeFromMap();
                routeOverlay.addToMap();
                routeOverlay.zoomToSpan();
            } else {
                ToastUtil.show(RouteActivity.this, R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(RouteActivity.this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(RouteActivity.this, R.string.error_key);
        } else {
            ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
                    + rCode);
        }
    }

    /**
     * 驾车结果回调
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null
                    && result.getPaths().size() > 0) {
                DrivePath drivePath = result.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                        this, aMap, drivePath, result.getStartPos(),
                        result.getTargetPos());
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
            } else {
                ToastUtil.show(RouteActivity.this, R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(RouteActivity.this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(RouteActivity.this, R.string.error_key);
        } else {
            ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
                    + rCode);
        }
    }

    /**
     * 步行路线结果回调
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null
                    && result.getPaths().size() > 0) {
                WalkPath walkPath = result.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
                        aMap, walkPath, result.getStartPos(),
                        result.getTargetPos());
                walkRouteOverlay.removeFromMap();
                walkRouteOverlay.addToMap();
                walkRouteOverlay.zoomToSpan();
            } else {
                ToastUtil.show(RouteActivity.this, R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(RouteActivity.this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(RouteActivity.this, R.string.error_key);
        } else {
            ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
                    + rCode);
        }
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
            default:
                break;
        }
    }
}

