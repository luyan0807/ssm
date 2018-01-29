package cn.jxnu.service;


import cn.jxnu.model.Content;

import java.util.List;

/**
 * Created with IDEA
 * Created by ${jie.chen} on 2016/7/14.
 * 后台登录Service
 */
public interface ContentService {
    List<Content> findContentList();

    int insertSelective(Content content) ;
}
