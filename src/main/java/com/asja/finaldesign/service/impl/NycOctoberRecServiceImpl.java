package com.asja.finaldesign.service.impl;

import com.asja.finaldesign.common.dto.ODInOutHeatData;
import com.asja.finaldesign.entity.NycOctoberRec;
import com.asja.finaldesign.mapper.NycOctoberRecMapper;
import com.asja.finaldesign.service.INycOctoberRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ASJA
 * @since 2022-04-29
 */
@Service
public class NycOctoberRecServiceImpl extends ServiceImpl<NycOctoberRecMapper, NycOctoberRec> implements INycOctoberRecService {

    @Override
    public List<ODInOutHeatData> getHexagonAllFlow(int interval, int inOrOut) {
        return getBaseMapper().getHexagonAllFlow(interval,inOrOut);
    }
}
