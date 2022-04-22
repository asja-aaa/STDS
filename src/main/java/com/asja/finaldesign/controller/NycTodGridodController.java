package com.asja.finaldesign.controller;


import com.alibaba.fastjson.JSON;
import com.asja.finaldesign.common.CommonResult;
import com.asja.finaldesign.common.dto.GeoJson;
import com.asja.finaldesign.common.dto.ODInOutHeatData;
import com.asja.finaldesign.entity.NycTodGridod;
import com.asja.finaldesign.service.H3Service;
import com.asja.finaldesign.service.INycTodGridodService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.asja.finaldesign.common.dto.GeoJson.odHeatDataCovToGeoJson;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ASJA
 * @since 2022-03-30
 */
@RestController
@RequestMapping("//nyc-tod-gridod")
public class NycTodGridodController {

    @Autowired
    private INycTodGridodService nycTodGridodService;

    @Autowired
    private H3Service h3Service;

    /**
     *
     * @param orderBy 排序字段
     * @param order 1：升序  2：降序
     * @param page 页数
     * @param size 每页数量
     * @param dynaId 时间
     * @return
     */
    @GetMapping("/list")
    public CommonResult getGridodList(
            @RequestParam(value = "orderby",required = false) String orderBy,
            @RequestParam(value = "order",required = false,defaultValue = "1") Integer order,
            @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
            @RequestParam(value = "size",required = false,defaultValue = "100") Integer size,
            @RequestParam(value = "dynaid",required = false,defaultValue = "0") Integer dynaId
            ){
        QueryWrapper<NycTodGridod> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dyna_id",dynaId);
        queryWrapper.orderBy(orderBy!=null,order==1,orderBy);
        queryWrapper.last("limit "+String.valueOf(size*page)+","+ String.valueOf(size*page+size));

        List<NycTodGridod> nycTodGridods = nycTodGridodService.list(queryWrapper);
        return CommonResult.success(nycTodGridods);

    }

    /**
     *
     * @param dynaId 时间id
     * @param rowId 行号
     * @param colId 页号
     * @param inOrOut 1: 出度  0：入度
     * @return
     */
    @GetMapping("/odHeat")
    public String  getInOutFlowHeatData(
            @RequestParam(value = "dynaid",required = false,defaultValue = "1") Integer dynaId,
            @RequestParam(value = "row",required = false,defaultValue = "2") Integer rowId,
            @RequestParam(value = "col",required = false,defaultValue = "2")Integer colId,
            @RequestParam(value = "inOrOut",required = false,defaultValue = "1") Integer inOrOut
    ){
        List<ODInOutHeatData> odInOutHeatData = nycTodGridodService.getODInOutHeatData(dynaId,rowId,colId,inOrOut);
        GeoJson geoJson = odHeatDataCovToGeoJson(odInOutHeatData);
        return JSON.toJSONString(CommonResult.success(geoJson));
    }

    /**
     *
     * @param dynaId 时间id
     * @param inOrOut 1: 出度  0：入度
     * @return
     */
    @GetMapping("/allFlow")
    public String getallFlow(
            @RequestParam(value = "dynaid",required = false,defaultValue = "1") Integer dynaId,
            @RequestParam(value = "inOrOut",required = false,defaultValue = "1") Integer inOrOut
    ){
        List<ODInOutHeatData> allFlow = nycTodGridodService.getAllFlow(dynaId, inOrOut);
        GeoJson geoJson = odHeatDataCovToGeoJson(allFlow);
        return JSON.toJSONString(CommonResult.success(geoJson));
    }

    @GetMapping("/h3")
    public String h3Map(
            @RequestParam(value = "lng",required = false,defaultValue = "-73.9737") Double lng,
            @RequestParam(value = "lat",required = false,defaultValue = "40.7616") Double lat,
            @RequestParam(value = "zoom",required = false,defaultValue = "15") Integer zoom,
            @RequestParam(value = "layer",required = false,defaultValue = "10") Integer layer
    ){
        GeoJson hexagonsGeoJson = h3Service.getHexagonsGeoJson(lng, lat, zoom, layer);
        return JSON.toJSONString(CommonResult.success(hexagonsGeoJson));
    }





}
