package com.chenyu.system.service;

import com.chenyu.system.api.domain.SysDept;
import com.chenyu.system.domain.vo.TreeSelect;
import org.apache.catalina.LifecycleState;

import java.util.List;

/**
 * 服务层接口
 *
 * @author chen yu
 * @create 2021-12-13 14:46
 */
public interface ISysDeptService {

   public List<SysDept>  selectDeptList(SysDept dept);

   public void checkDeptDataScope(Long deptId);


   public SysDept selectDeptById(Long deptId);


   public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);

   public List<SysDept> buildDeptTree(List<SysDept> depts);


   public List<Integer> selectDeptListByRoleId(Long roleId);


   public String checkDeptNameUnique(SysDept dept);


   public int insertDept(SysDept dept);


   public int selectNormalChildrenDeptById(Long deptId);


   public int updateDept(SysDept dept);


   public boolean hasChildByDeptId(Long deptId);


   public boolean checkDeptExistUser(Long deptId);


   public int deleteDeptById(Long deptId);





}
