package com.asja.finaldesign.service;

import com.asja.finaldesign.common.dto.ODInOutHeatData;
import com.asja.finaldesign.entity.NycTodGridod;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ASJA
 * @since 2022-03-30
 */
public interface INycTodGridodService extends IService<NycTodGridod> {
    List<ODInOutHeatData> getODInOutHeatData(int dynaId, int rowId, int colId, int inOrOut);
    List<ODInOutHeatData> getAllFlow(int dynaId,int inOrOut);
}
