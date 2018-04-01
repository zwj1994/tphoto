package com.bs.tphoto.dao;

import com.bs.tphoto.entity.YBanner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface YBannerMapper {

    /**
     * 查询轮播图
     * @return
     */
    @Select("select b_id,b_name,b_desc,b_icon,b_url from y_banner where b_state = 0 order by b_level desc")
    List<YBanner> selectAll();
}
