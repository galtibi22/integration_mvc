package smartspace;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import smartspace.dao.ExtendedActionDao;
import smartspace.dao.ExtendedElementDao;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityGenerator;

import java.util.logging.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public abstract class CommonTest {

    protected EntityFactory entityFactory;
    protected ExtendedActionDao actionDao;
    protected ExtendedUserDao<String> userDao;
    protected ExtendedElementDao<String> elementDao;
    protected AppProperties appProperties;
    protected EntityGenerator entityGenerator;

    @Autowired
    public void setElementDao(ExtendedElementDao<String> elementDao) {
        this.elementDao = elementDao;
    }
    @Autowired
    public void setEntityGenerator(EntityGenerator entityGenerator) {
        this.entityGenerator = entityGenerator;
    }
    @Autowired
    public void setUserDao(ExtendedUserDao<String> userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setActionDao(ExtendedActionDao actionDao) {
        this.actionDao = actionDao;
    }

    @Autowired
    public void setEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }
    @Autowired
    public void setAppProperties( AppProperties appProperties)  {      this.appProperties = appProperties;}

    protected Logger logger=Logger.getLogger(this.getClass().toString());

    public void when(String str){
        logger.info("When "+str);
    }
    public void given(String str){
        logger.info("Given "+str);
    }
    public void then(String str){
        logger.info("Then "+str);
    }

    public void and(String str){
        logger.info("And "+str);
    }

    @Before
    public void setup(){
        logger.info("Start setup method");

    }

    @After
    public void cleanup(){
       logger.info("Start cleanup method");
       try {
           actionDao.deleteAll();
           elementDao.deleteAll();
           userDao.deleteAll();
       }catch (Exception e){
           logger.info("clean up failed with error"+e);
       }
    }

}
