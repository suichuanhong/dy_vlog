package cn.schff.dyvlog;

import cn.schff.dyvlog.common.util.IdUtil;
import cn.schff.dyvlog.common.util.MsgSendUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
class DyVlogApplicationTests {

    @Autowired
    private MsgSendUtil msgSendUtil;

    @Autowired
    private IdUtil idUtil;

    @Test
    void contextLoads() {
        msgSendUtil.sendMsg("10086", "111111");
    }

    @Test
    void testId() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            System.out.println(idUtil.getId("user"));
            Thread.sleep(10L);
        }
    }

    @Test
    public void test() {
        System.out.println(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC));
    }

}
