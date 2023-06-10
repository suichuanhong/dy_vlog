package cn.schff.dyvlog;

import cn.schff.dyvlog.common.util.IdUtil;
import cn.schff.dyvlog.common.util.MsgSendUtil;
import cn.schff.dyvlog.config.TencentCloudConfig;
import cn.schff.dyvlog.pojo.MessageMO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
class DyVlogApplicationTests {

    @Autowired
    private MsgSendUtil msgSendUtil;

    @Autowired
    private IdUtil idUtil;

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
        msgSendUtil.sendMsg("18896122875", "145120");
    }

    @Test
    void testId() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            System.out.println(idUtil.getId("user"));
            Thread.sleep(10L);
        }
    }


    @Autowired
    private TencentCloudConfig tencentCloudConfig;
    @Test
    public void testYml() {
        System.out.println(tencentCloudConfig);
    }

    @Test
    public void testMongoDBInsert() {
        MessageMO messageMO = new MessageMO();
        messageMO.setId(11L);
        messageMO.setFromUserId("111");
        messageMO.setFromUserNickName("2222");
        messageMO.setFromUserFace("eee");
        messageMO.setToUserId("444");
//        messageMO.setContent("我喜欢赵习帆");
        messageMO.setType(0);
        messageMO.setCreate_time(LocalDateTime.now());
        mongoTemplate.save(messageMO);
    }

    @Test
    public void testMongoGetOne() {
        Query query = new Query();
        query.addCriteria(Criteria.where("fromUserId").is("111"));
        MessageMO one = mongoTemplate.findOne(query, MessageMO.class);
        System.out.println(one);
    }


    @Test
    public void test() {
        System.out.println(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC));
    }

}
