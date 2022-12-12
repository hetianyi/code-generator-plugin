// #__Template__#
package com.github.hetianyi.plugin.sample.service;

import java.util.List;

import com.github.hetianyi.plugin.sample.model.form.CreateExampleForm;
import com.github.hetianyi.plugin.sample.model.form.UpdateExampleForm;
import com.github.hetianyi.plugin.sample.model.po.ExamplePO;

/**
 * 示例服务
 *
 * @author Jason He
 */
public interface ExampleService {
    Long insertExample(CreateExampleForm form);
    void updateExample(UpdateExampleForm form);
    void deleteExample(Long exampleId);
    ExamplePO getExampleById(Long exampleId);
    List<ExamplePO> listExamples();
}
