package smartspace.sanity;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import smartspace.CommonTest;
import smartspace.data.*;

import java.util.Date;


public class DataCrudFlow extends CommonTest {

    @Test
    public void user_register_to_final_semester_test(){
        given("New user without key");
        UserEntity userEntity=entityFactory.createNewUser("test@gmail.com",appProperties.getName(),"userName",appProperties.AVATAR, UserRole.PLAYER,100);
//        Assert.assertNull("The user has a key before saved in the db",userEntity.getKey());
        and("The user saved in the db and has a key");
        userEntity= userDao.create(userEntity);
        Assert.assertNotNull(userEntity.getKey());
        Assert.assertNotNull("User with id "+userEntity.getUserName()+" is not exist in db", userDao.readById(userEntity.getKey()));
        when("The user create new element");
        ElementEntity element=entityFactory.createNewElement(
                "Final Test",
                "type",
                new Location(100,100),
                new Date(),
                userEntity.getUserEmail(),
                userEntity.getUserSmartSpace(),false,null);
        and("The element saved in the db with new id");
        element=elementDao.create(element);
        Assert.assertNotNull("Element"+element.getName()+" has a key",element.getKey());
        Assert.assertNotNull("Element"+element.getName()+" is not exist in db",elementDao.readById(element.getKey()));
        then("The user can add action to the element");
        ActionEntity actionEntity=entityFactory.createNewAction(element.getKey(),element.getElementSmartspace(),"register",new Date(),userEntity.getUserEmail(),userEntity.getUserSmartSpace(),null);
        and("the action saved in the db");
        actionEntity=actionDao.create(actionEntity);
        and("All actions was deleted from db");
        int actionsBefore=actionDao.readAll().size();
        actionDao.deleteAll();
        int actionAfter=actionDao.readAll().size();
        Assert.assertNotEquals(actionsBefore,actionAfter);
    }
}
