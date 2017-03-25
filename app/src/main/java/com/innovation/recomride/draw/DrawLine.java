package com.innovation.recomride.draw;

import android.graphics.Color;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AMXPC on 2017/3/26.
 */

public class DrawLine {

    public static PolylineOptions getSafe() {
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(31.282168 - 0.005658, 121.497095 - 0.00663));
        latLngs.add(new LatLng(31.284892 - 0.005658, 121.497139 - 0.00663));
        latLngs.add(new LatLng(31.287982 - 0.005658, 121.497189 - 0.00663));
        latLngs.add(new LatLng(31.29625 - 0.005658, 121.497453 - 0.00663));
        latLngs.add(new LatLng(31.29699 - 0.005658, 121.500004 - 0.00663));
        latLngs.add(new LatLng(31.299428 - 0.005658, 121.499331 - 0.00663));
        latLngs.add(new LatLng(31.30158 - 0.005658, 121.509104 - 0.00663));
        return new PolylineOptions().addAll(latLngs).width(5).color(Color.argb(255, 1, 200, 200));
    }


    public static PolylineOptions getComfort() {
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(31.282168 - 0.005658, 121.497095 - 0.00663));
        latLngs.add(new LatLng(31.284892 - 0.005658, 121.497139 - 0.00663));
        latLngs.add(new LatLng(31.287982 - 0.005658, 121.497189 - 0.00663));
        latLngs.add(new LatLng(31.287773 - 0.005658, 121.502642 - 0.00663));
        latLngs.add(new LatLng(31.286170 - 0.005658, 121.506824 - 0.00663));
        latLngs.add(new LatLng(31.289953 - 0.005658, 121.508764 - 0.00663));
        latLngs.add(new LatLng(31.2902 - 0.005658, 121.508165 - 0.00663));
        latLngs.add(new LatLng(31.291804 - 0.005658, 121.5091 - 0.00663));
        latLngs.add(new LatLng(31.29165 - 0.005658, 121.51007 - 0.00663));
        latLngs.add(new LatLng(31.291941 - 0.005658, 121.510327 - 0.00663));
        latLngs.add(new LatLng(31.292550 - 0.005658, 121.507821 - 0.00663));
        latLngs.add(new LatLng(31.293075 - 0.005658, 121.507641 - 0.00663));
        latLngs.add(new LatLng(31.293687 - 0.005658, 121.507123 - 0.00663));
        latLngs.add(new LatLng(31.297353 - 0.005658, 121.511871 - 0.00663));
        latLngs.add(new LatLng(31.30158 - 0.005658, 121.509104 - 0.00663));
        return new PolylineOptions().addAll(latLngs).width(5).color(Color.argb(255, 1, 200, 1));
    }

    public static PolylineOptions getQuick() {
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(31.282168 - 0.005658, 121.497095 - 0.00663));
        latLngs.add(new LatLng(31.282049 - 0.005658, 121.503154 - 0.00663));
        latLngs.add(new LatLng(31.287773 - 0.005658, 121.502642 - 0.00663));
        latLngs.add(new LatLng(31.289613 - 0.005658, 121.50278 - 0.00663));
        latLngs.add(new LatLng(31.292724 - 0.005658, 121.505893 - 0.00663));
        latLngs.add(new LatLng(31.293760 - 0.005658, 121.504869 - 0.00663));
        latLngs.add(new LatLng(31.29884 - 0.005658, 121.50508 - 0.00663));
        latLngs.add(new LatLng(31.300506 - 0.005658, 121.504684 - 0.00663));
        latLngs.add(new LatLng(31.30158 - 0.005658, 121.509104 - 0.00663));
        return new PolylineOptions().addAll(latLngs).width(5).color(Color.argb(255, 200, 1, 1));
    }

    public static PolylineOptions getShort() {
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(31.282168 - 0.005658, 121.497095 - 0.00663));
        latLngs.add(new LatLng(31.284892 - 0.005658, 121.497139 - 0.00663));
        latLngs.add(new LatLng(31.287982 - 0.005658, 121.497189 - 0.00663));
        latLngs.add(new LatLng(31.29625 - 0.005658, 121.497453 - 0.00663));
        latLngs.add(new LatLng(31.29699 - 0.005658, 121.500004 - 0.00663));
        latLngs.add(new LatLng(31.299428 - 0.005658, 121.499331 - 0.00663));
        latLngs.add(new LatLng(31.30158 - 0.005658, 121.509104 - 0.00663));
        return new PolylineOptions().addAll(latLngs).width(5).color(Color.argb(255, 1, 1, 200));
    }

    public static PolylineOptions getSatisfied() {
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(31.282168 - 0.005658, 121.497095 - 0.00663));
        latLngs.add(new LatLng(31.282049 - 0.005658, 121.503154 - 0.00663));
        latLngs.add(new LatLng(31.287773 - 0.005658, 121.502642 - 0.00663));
        latLngs.add(new LatLng(31.289613 - 0.005658, 121.50278 - 0.00663));
        latLngs.add(new LatLng(31.292724 - 0.005658, 121.505893 - 0.00663));
        latLngs.add(new LatLng(31.297353 - 0.005658, 121.511871 - 0.00663));
        latLngs.add(new LatLng(31.30158 - 0.005658, 121.509104 - 0.00663));
        return new PolylineOptions().addAll(latLngs).width(5).color(Color.argb(255, 200, 1, 200));
    }
}
