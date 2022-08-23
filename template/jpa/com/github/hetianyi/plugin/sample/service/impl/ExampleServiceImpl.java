// #MvcTemplate#
package com.github.hetianyi.plugin.sample.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.github.hetianyi.plugin.sample.model.form.CreateExampleForm;
import com.github.hetianyi.plugin.sample.model.form.UpdateExampleForm;
import com.github.hetianyi.plugin.sample.model.po.ExamplePO;
import com.github.hetianyi.plugin.sample.repository.ExampleRepository;
import com.github.hetianyi.plugin.sample.service.ExampleService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jason He
 */
@Slf4j
@Service
public class ExampleServiceImpl implements ExampleService {

    @Resource
    private ExampleRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long insertExample(CreateExampleForm form) {
        ExamplePO userPO = form.convertTo(new ExamplePO());
        userPO.setCreatedTime(new Date());
        userRepository.save(userPO);
        return userPO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateExample(UpdateExampleForm form) {
        userRepository.save(form.convertTo(new ExamplePO()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteExample(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public ExamplePO getExampleById(Long userId) {
        ExamplePO build = ExamplePO.builder().id(userId).build();
        return userRepository.findOne(Example.of(build)).orElse(null);
    }

    @Override
    public List<ExamplePO> listExamples() {
        return userRepository.findAll();
    }
}
