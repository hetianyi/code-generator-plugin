// #__Template__#
package com.github.hetianyi.plugin.sample.repository;

import com.github.hetianyi.plugin.sample.model.po.ExamplePO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Jason He
 */
@Repository
public interface ExampleRepository extends JpaRepository<ExamplePO, Long>, JpaSpecificationExecutor<ExamplePO> {
}
