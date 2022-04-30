package com.asja.finaldesign.service;

import com.asja.finaldesign.common.dto.ODInOutHeatData;
import com.asja.finaldesign.entity.NycOctoberRec;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ASJA
 * @since 2022-04-29
 */
public interface INycOctoberRecService extends IService<NycOctoberRec> {
    List<ODInOutHeatData> getHexagonAllFlow(int dynaId, int inOrOut);
}
