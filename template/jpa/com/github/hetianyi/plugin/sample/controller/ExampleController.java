// #__Template__#
package com.github.hetianyi.plugin.sample.controller;

import javax.annotation.Resource;

import com.github.hetianyi.boot.ready.model.http.Result;
import com.github.hetianyi.plugin.sample.model.form.CreateExampleForm;
import com.github.hetianyi.plugin.sample.model.po.ExamplePO;
import com.github.hetianyi.plugin.sample.service.ExampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例接口
 *
 * @author Jason He
 */
@Slf4j
@RestController
@Api(tags = "示例接口")
@RequestMapping("/api/v1/examples")
public class ExampleController {

    @Resource
    private ExampleService demoService;

    @ApiOperation(value = "新增示例")
    @PostMapping
    public Result<String> insertExample(@RequestBody CreateExampleForm form) {
        return Result.success(String.valueOf(demoService.insertExample(form)));
    }

    @GetMapping("/{exampleId}")
    public Result<ExamplePO> getExample(@PathVariable Long exampleId) {
        return Result.success(demoService.getExampleById(exampleId));
    }

    @DeleteMapping("/{exampleId}")
    public Result deleteExample(@PathVariable Long exampleId) {
        demoService.deleteExample(exampleId);
        return Result.success();
    }
}
