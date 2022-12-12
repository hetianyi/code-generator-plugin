// #__Template__#
package com.github.hetianyi.plugin.sample.controller;

import javax.annotation.Resource;

import com.github.hetianyi.boot.ready.model.http.Page;
import com.github.hetianyi.boot.ready.model.http.Result;
import com.github.hetianyi.plugin.sample.model.form.CreateExampleForm;
import com.github.hetianyi.plugin.sample.model.form.QueryExampleForm;
import com.github.hetianyi.plugin.sample.model.form.UpdateExampleForm;
import com.github.hetianyi.plugin.sample.model.vo.ExampleVO;
import com.github.hetianyi.plugin.sample.service.ExampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例增删改查接口
 *
 * @author Jason He
 */
@Slf4j
@RestController
@Api(tags = "示例增删改查接口")
@RequestMapping("/api/v1/examples")
public class ExampleController {

    @Resource
    private ExampleService exampleService;

    /**
     * 新增示例
     */
    @ApiOperation("新增示例")
    @PostMapping
    public Result<String> insertExample(@RequestBody CreateExampleForm form) {
        return Result.success(exampleService.insert(form));
    }

    /**
     * 更新示例信息
     */
    @ApiOperation("更新示例信息")
    @PutMapping("/{exampleId}")
    public Result updateExample(@PathVariable String exampleId, @RequestBody UpdateExampleForm form) {
        form.setId(exampleId);
        exampleService.update(form);
        return Result.success();
    }

    /**
     * 删除示例
     */
    @ApiOperation("删除示例")
    @DeleteMapping("/{exampleId}")
    public Result<String> deleteExample(@PathVariable String exampleId) {
        exampleService.delete(Long.valueOf(exampleId));
        return Result.success();
    }

    /**
     * 获取示例详情
     */
    @ApiOperation("获取示例详情")
    @GetMapping("/{exampleId}")
    public Result<ExampleVO> getExampleById(@PathVariable String exampleId) {
        return Result.success(exampleService.getById(Long.valueOf(exampleId)));
    }

    /**
     * 分页查询示例信息
     */
    @ApiOperation("分页查询示例信息")
    @GetMapping
    public Result<Page<ExampleVO>> listExamples(QueryExampleForm form) {
        return Result.success(exampleService.list(form));
    }
}
