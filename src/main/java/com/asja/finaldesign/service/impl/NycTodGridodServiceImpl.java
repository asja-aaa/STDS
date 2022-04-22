package com.asja.finaldesign.service.impl;

import com.asja.finaldesign.common.dto.ODInOutHeatData;
import com.asja.finaldesign.entity.NycTodGridod;
import com.asja.finaldesign.mapper.NycTodGridodMapper;
import com.asja.finaldesign.service.INycTodGridodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ASJA
 * @since 2022-03-30
 */
@Service
public class NycTodGridodServiceImpl extends ServiceImpl<NycTodGridodMapper, NycTodGridod> implements INycTodGridodService {

    @Override
    public List<ODInOutHeatData> getODInOutHeatData(int dynaId, int rowId, int colId, int inOrOut) {
        return getBaseMapper().getODInOutHeatData(dynaId,rowId,colId,inOrOut);
    }

    @Override
    public List<ODInOutHeatData> getAllFlow(int dynaId, int inOrOut) {
        return getBaseMapper().getAllFlow(dynaId,inOrOut);
    }
}
