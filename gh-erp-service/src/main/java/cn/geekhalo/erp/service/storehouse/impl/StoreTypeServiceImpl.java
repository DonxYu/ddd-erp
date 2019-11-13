package cn.geekhalo.erp.service.storehouse.impl;

import cn.geekhalo.common.constants.ValidStatus;
import cn.geekhalo.common.service.AbstractService;
import cn.geekhalo.erp.domain.storehouse.QStoreType;
import cn.geekhalo.erp.domain.storehouse.StoreType;
import cn.geekhalo.erp.domain.storehouse.vo.StoreTypeVo;
import cn.geekhalo.erp.dto.storehouse.StoreTypeCreateDto;
import cn.geekhalo.erp.repository.storehouse.StoreTypeRepository;
import cn.geekhalo.erp.service.storehouse.IStoreTypeService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class StoreTypeServiceImpl extends AbstractService implements IStoreTypeService {

    protected StoreTypeServiceImpl() {
        super(log);
    }

    @Autowired
    private StoreTypeRepository storeTypeRepository;


    @Override
    public List<StoreTypeVo> findAll() {
        return storeTypeRepository.findAll().stream().map(st -> new StoreTypeVo(st)).collect(Collectors.toList());
    }

    @Override
    public void createStoreType(StoreTypeCreateDto dto) {
        creatorFor(storeTypeRepository)
                .instance(() -> new StoreType(dto.getName(),dto.getCode()))
                .update(st -> st.init())
                .call();
    }

    @Override
    public void invalid(Long id) {
        updaterFor(storeTypeRepository)
                .id(id)
                .update(storeType -> storeType.invalid())
                .call();
    }

    @Override
    public void valid(Long id) {
        updaterFor(storeTypeRepository)
                .id(id)
                .update(storeType -> storeType.valid())
                .call();
    }

    @Override
    public List<StoreTypeVo> findAllStoreTypeList() {
        Iterable<StoreType> list = storeTypeRepository.findAll(QStoreType.storeType.validStatus.eq(ValidStatus.VALID));
        List<StoreTypeVo> vos = Lists.newArrayList();
        list.forEach(st -> vos.add(new StoreTypeVo(st)));
        return vos;
    }
}
