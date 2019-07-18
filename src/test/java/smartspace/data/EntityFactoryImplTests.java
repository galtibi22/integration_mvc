package smartspace.data;

import org.junit.Test;

import smartspace.CommonTest;
import java.util.Date;

import static org.junit.Assert.fail;

public class EntityFactoryImplTests extends CommonTest {

	// The test has 3 DB operations
	@Test(timeout=3000)
    public void userEntityPositiveTest(){
        logger.info("userEntityPositiveTest - create userEntity,ActionEntity and ElementEntity with all required fields");
        UserEntity userEntity=entityFactory.createNewUser(
                "gal@gal.com","user","userName","fsa", UserRole.PLAYER,10);
        logger.info("Success to create UserEntity "+userEntity);
        ElementEntity elementEntity=entityFactory.createNewElement("element","type",new Location(1,1),new Date(),userEntity.getUserEmail(),"creatorSmartSpace",false,null);
        logger.info("Success to create elementEntity "+elementEntity);
        ActionEntity actionEntity=entityFactory.createNewAction(elementEntity.getKey(),elementEntity.getElementSmartspace(),"type",new Date(),userEntity.getUserEmail(),userEntity.getUserSmartSpace(),null);
        logger.info("Success to create actionEntity "+actionEntity);

    }

	public void userEntityNegativeTest(){
        logger.info("userEntityNegativeTest - try to create entities without mandatory fields");
        try {
            logger.info("createNewUser - try to create userEntity with email incorrect");
            UserEntity userEntity = entityFactory.createNewUser("galgal.com", "user", "userName", "fsa", UserRole.PLAYER, 10);
            fail("Email validation pass with email incorrect");
        }catch (RuntimeException e){
            logger.info(e.getMessage());
        }
        try {
            logger.info("createNewUser - try to create userEntity with avatar null");
            UserEntity userEntity = entityFactory.createNewUser("gal@gal.com", "userSmartSpace", "userName", null, UserRole.PLAYER, 10);
            fail("null validation pass with null value");
        }catch (RuntimeException e ){
            logger.info(e.getMessage());
        }
        try {
            logger.info("createNewUser - try to create userEntity with userSmartSpace with 2 chars");
            UserEntity userEntity = entityFactory.createNewUser("gal@gal.com", "", "userName", "fsa", UserRole.PLAYER, 10);
            fail("size validation pass with 0 chars values when the min is 1");
        }catch (RuntimeException e){
            logger.info(e.getMessage());
        }


    }


}