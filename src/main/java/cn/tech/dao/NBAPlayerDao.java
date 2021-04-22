package cn.tech.dao;

import cn.tech.model.NBAPlayer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NBAPlayerDao {

    @Select("select * from nba_player")
    List<NBAPlayer> selectAll();
}
