package com.chenyu.system.service.imple;

import com.chenyu.system.domain.SysPost;
import com.chenyu.system.service.ISysPostService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chen yu
 * @create 2021-11-10 18:20
 */
@Service
public class SysPostServiceImpl implements ISysPostService {
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return null;
    }

    @Override
    public List<SysPost> selectPostAll() {
        return null;
    }

    @Override
    public SysPost selectPostById(Long postId) {
        return null;
    }

    @Override
    public List<Integer> selectPostListByUserId(Long userId) {
        return null;
    }

    @Override
    public String checkPostNameUnique(SysPost post) {
        return null;
    }

    @Override
    public String checkPostCodeUnique(SysPost post) {
        return null;
    }

    @Override
    public int countUserPostById(Long postId) {
        return 0;
    }

    @Override
    public int deletePostById(Long postId) {
        return 0;
    }

    @Override
    public int deletePostByIds(Long[] postIds) {
        return 0;
    }

    @Override
    public int insertPost(SysPost post) {
        return 0;
    }

    @Override
    public int updatePost(SysPost post) {
        return 0;
    }
}
