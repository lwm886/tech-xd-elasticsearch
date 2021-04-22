package cn.tech;

import cn.tech.dao.NBAPlayerDao;
import cn.tech.model.NBAPlayer;
import cn.tech.service.NBAPlayerService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class TechApplicationTests {

	@Autowired
	private NBAPlayerDao nbaPlayerDao;

	@Autowired
	private NBAPlayerService nbaPlayerService;

	@Test
	void selectAll() {
		List<NBAPlayer> nbaPlayers = nbaPlayerDao.selectAll();
		log.info(nbaPlayers.toString());
	}

	@Test
	void addPlayer() throws IOException {
		NBAPlayer nbaPlayer = new NBAPlayer();
		nbaPlayer.setDisplayName("杨超越");
		nbaPlayer.setId(999);
		nbaPlayerService.addPlayer(nbaPlayer,"999");
	}

	@Test
	void getPlayer() throws IOException {
		Map<String, Object> player = nbaPlayerService.getPlayer("999");
		log.info(JSON.toJSONString(player));
	}

	@Test
	void  updatePlayer() throws IOException {
		NBAPlayer nbaPlayer = new NBAPlayer();
		nbaPlayer.setDisplayName("杨超越1");
		nbaPlayer.setId(999);
		nbaPlayerService.updatePlayer(nbaPlayer,"999");
	}

	@Test
	void  deletePlayer() throws IOException {
		nbaPlayerService.deletePlayer("999");
	}

	@Test
	void  deleteAllPlayer() throws IOException {
		nbaPlayerService.deleteAllPlayer();
	}

	@Test
	void  importAll() throws IOException {
		nbaPlayerService.importAll();
	}

	@Test
	void searchMatch() throws IOException {
		List<NBAPlayer> nbaPlayers = nbaPlayerService.searchMatch("displayNameEn", "steven");
		log.info(nbaPlayers.toString());
	}

	@Test
	void searchTerm() throws IOException {
		List<NBAPlayer> nbaPlayers = nbaPlayerService.searchTerm("age", "23");
		log.info(JSON.toJSONString(nbaPlayers));
	}

	@Test
	void searchMatchPrefix() throws IOException {
		List<NBAPlayer> nbaPlayers = nbaPlayerService.searchMatchPrefix("displayNameEn", "a");
		log.info(JSON.toJSONString(nbaPlayers));
	}
}
