package cn.tech.service;

import cn.tech.model.NBAPlayer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author lw
 * @since 2021/4/22
 **/
public interface NBAPlayerService {
    /**
     * 创建
     * @param nbaPlayer
     * @param id
     * @return
     * @throws IOException
     */
    boolean addPlayer(NBAPlayer nbaPlayer, String id) throws IOException;

    /**
     * 获取
     * @param id
     * @return
     * @throws IOException
     */
    Map<String,Object> getPlayer(String id) throws IOException;

    /**
     * 更新
     * @param nbaPlayer
     * @param id
     * @return
     * @throws IOException
     */
    boolean updatePlayer(NBAPlayer nbaPlayer, String id) throws IOException;

    /**
     * 删除
     * @param id
     * @return
     * @throws IOException
     */
    boolean deletePlayer(String id) throws IOException;

    /**
     * 删除全部
     * @return
     * @throws IOException
     */
    boolean deleteAllPlayer() throws IOException;

    /**
     * 导入全部
     * @return
     * @throws IOException
     */
    boolean importAll() throws IOException;

    /**
     * Match Query
     * @param key
     * @param val
     * @return
     * @throws IOException
     */
    List<NBAPlayer> searchMatch(String key, String val) throws IOException;

    /**
     * term Query
     * @param key
     * @param val
     * @return
     * @throws IOException
     */
    List<NBAPlayer> searchTerm(String key, String val) throws IOException;

    /**
     * match prefix Query
     * @param key
     * @param val
     * @return
     * @throws IOException
     */
    List<NBAPlayer> searchMatchPrefix(String key, String val) throws IOException;
}
