package com.asja.finaldesign.service;

import com.asja.finaldesign.common.dto.GeoJson;
import com.asja.finaldesign.common.dto.geo.Feature;
import com.asja.finaldesign.common.dto.geo.Geometry;
import com.asja.finaldesign.common.dto.geo.Properties;
import com.uber.h3core.H3Core;
import com.uber.h3core.util.GeoCoord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author ASJA
 * @Create 2022-04-21 17:05
 */
@Service
@Slf4j
public class H3Service {

    @Autowired
    private H3Core h3Core;

    @Qualifier("threadExecutor")
    @Autowired
    private Executor executor;

    /**
     * 返回覆盖该坐标点的六边形网格索引
     * @param lng
     * @param lat
     * @param zoom  层级 1-15
     * @return
     */
    public long getCenterHexagon(double lng,double lat,int zoom){
        return h3Core.geoToH3(lat,lng,zoom);
    }

    /**
     * 获取一个六边形栅栏
     * @param index
     * @return
     */
    public  List<GeoCoord> getHexagonCoordsByH3Index(long index){
        return h3Core.h3ToGeoBoundary(index);
    }

    /**
     * 获取一个点周围的六边形网格
     * @param lng
     * @param lat
     * @param zoom
     * @param layer
     * @return
     */
    public  GeoJson getHexagonsGeoJson(double lng,double lat,int zoom,int layer){
        long h3Cen = h3Core.geoToH3(lat, lng, zoom);
        List<List<Long>> kRingDistances = h3Core.kRingDistances(h3Cen, layer);
        List<List<GeoCoord>> ans = Collections.synchronizedList(new ArrayList<>());

        // 异步操作
        CompletableFuture[] completableFutures = kRingDistances.stream().map(item -> CompletableFuture.runAsync(() -> {
            List<List<GeoCoord>> lists = item.stream().map(this::getHexagonCoordsByH3Index).collect(Collectors.toList());
            ans.addAll(lists);
        }, executor)).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(completableFutures).join();

        GeoJson geoJson = GeoJson.geoCoordsCovToGeoJson(ans);
        return  geoJson;

    }

    /**
     * 将区域划分为六边形网格
     * @param polygons
     * @param layer
     * @return
     */
    public GeoJson getRegionDivideHexagonsGeoJson(List<String> polygons,int layer){

        List<GeoCoord> list = polygons.stream().map(polygon -> {
            String[] split = polygon.split(";");
            return new GeoCoord(Double.valueOf(split[0]), Double.valueOf(split[1]));
        }).collect(Collectors.toList());
        List<Long> hexagons = h3Core.polyfill(list, null, layer);

        List<List<GeoCoord>> ans = Collections.synchronizedList(new ArrayList<>());
        // 异步操作
        CompletableFuture[] completableFutures = hexagons.stream().map(item -> CompletableFuture.supplyAsync(() ->
                        getHexagonCoordsByH3Index(item)
                , executor).thenAccept(ans::add)).toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(completableFutures).join();

        GeoJson geoJson = GeoJson.geoCoordsCovToGeoJson(ans);
        return  geoJson;
    }
}
