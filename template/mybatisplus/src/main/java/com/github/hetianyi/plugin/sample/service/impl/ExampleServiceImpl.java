// #__Template__#
package com.github.hetianyi.plugin.sample.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hetianyi.boot.ready.exception.BizException;
import com.github.hetianyi.boot.ready.model.http.Page;
import com.github.hetianyi.plugin.sample.mapper.ExampleMapper;
import com.github.hetianyi.plugin.sample.model.form.CreateExampleForm;
import com.github.hetianyi.plugin.sample.model.form.QueryExampleForm;
import com.github.hetianyi.plugin.sample.model.form.UpdateExampleForm;
import com.github.hetianyi.plugin.sample.model.po.ExamplePO;
import com.github.hetianyi.plugin.sample.model.vo.ExampleVO;
import com.github.hetianyi.plugin.sample.service.ExampleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jason He
 */
@Slf4j
@Service
public class ExampleServiceImpl extends ServiceImpl<ExampleMapper, ExamplePO> implements ExampleService {

    @Override
    public String insert(CreateExampleForm form) {
        ExamplePO examplePO = form.convertTo(new ExamplePO());
        if (this.baseMapper.insert(examplePO) > 0) {
            log.info("新增示例成功: {}", examplePO.getId());
            return String.valueOf(examplePO.getId());
        }
        return null;
    }

    @Override
    public void update(UpdateExampleForm form) {
        assertExist(form.getId());
        ExamplePO examplePO = form.convertTo(new ExamplePO());
        examplePO.setUpdatedTime(new Date());
        if (this.baseMapper.updateById(examplePO) > 0) {
            log.info("更新成功: {}", examplePO.getId());
            return;
        }
        throw new BizException("更新失败");
    }

    @Override
    public void delete(Long exampleId) {
        assertExist(exampleId.toString());
        if (this.baseMapper.deleteById(exampleId) > 0) {
            log.info("删除成功: {}", exampleId);
            return;
        }
        throw new BizException("删除失败");
    }

    private void assertExist(String id) {
        if (this.baseMapper.selectCount(Wrappers.<ExamplePO>lambdaQuery()
                                                .eq(ExamplePO::getId, Long.valueOf(id))) == 0) {
            throw new BizException("记录不存在: " + id);
        }
    }

    @Override
    public ExampleVO getById(Long exampleId) {
        ExamplePO examplePO = this.baseMapper.selectById(exampleId);
        return new ExampleVO().convertFrom(examplePO);
    }

    @Override
    public Page<ExampleVO> list(QueryExampleForm form) {
        Integer total = this.baseMapper.countExamples(form);
        if (total == 0) {
            return Page.of(form.getPage(), form.getPageSize(), total, ImmutableList.of());
        }
        List<ExampleVO> exampleVOS = this.baseMapper.listExamples(form)
                                                    .stream()
                                                    .map(v -> new ExampleVO().convertFrom(v))
                                                    .collect(Collectors.toList());
        return Page.of(form.getPage(), form.getPageSize(), total, exampleVOS);
    }
}
