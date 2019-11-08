package base;

import com.augurit.CommonApplication;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@ExtendWith(SpringExtension.class)
@RunWith(JUnitPlatform.class)
@WebAppConfiguration
@SpringBootTest(classes = {CommonApplication.class, RestTemplate.class})
//@ActiveProfiles("test")
public class BaseTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        OpusLoginUser user = new OpusLoginUser();
        user.setCurrentOrgId("0368948a-1cdf-4bf8-a828-71d796ba89f6");
        user.setCurrentTmn("1");
        OpuOmUser ckry = webApplicationContext.getBean(OpuOmUserMapper.class).getUserByLoginName("ckry");

        com.augurit.agcloud.framework.security.user.OpuOmUser loginUser = new com.augurit.agcloud.framework.security.user.OpuOmUser();
        BeanUtils.copyProperties(ckry, loginUser);

        user.setUser(loginUser);
        SecurityContext.setCurrentLoginUser(user, 6000);
    }

}

