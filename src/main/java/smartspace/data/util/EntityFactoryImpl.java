package smartspace.data.util;

import org.springframework.stereotype.Component;
import smartspace.data.*;
import smartspace.data.validators.CommonValidator;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class EntityFactoryImpl implements EntityFactory {

    private Logger logger=Logger.getLogger(this.getClass().getName());


    @Override
    public UserEntity createNewUser(String userEmail, String userSmartSpace, String username, String avatar, UserRole role, long points) {
        CommonValidator.isNotNull(userEmail,userSmartSpace,username,avatar,role,points);
        CommonValidator.isEmail(userEmail);
        CommonValidator.minSize(1,userSmartSpace,username,avatar);
        UserEntity userEntity= new UserEntity(userSmartSpace,userEmail,username,avatar,role,points);
        logger.info("createNewUser method success. userEntity "+userEntity);
        return userEntity;
    }

    @Override
    public ElementEntity createNewElement(String name, String type,  Location location, Date creationTimeStamp,String creatorEmail, String creatorSmartSpace, boolean expired, Map<String, Object> moreAttributes) {
        CommonValidator.isNotNull(name,type,creationTimeStamp,location,creatorEmail,creationTimeStamp,expired);
        CommonValidator.isEmail(creatorEmail);
        CommonValidator.minSize(1,name,creatorSmartSpace,type);
        ElementEntity elementEntity = new ElementEntity(creatorSmartSpace,location,name,type,creationTimeStamp,expired,creatorSmartSpace,creatorEmail,moreAttributes);
        logger.info("createNewElement method success. elementEntity "+elementEntity);
        return elementEntity;
    }


    @Override
    public ActionEntity createNewAction(String elementId, String elementSmartSpace, String actionType, Date creationTimeStamp, String playerEmail, String playerSmartSpace, Map<String, Object> moreAttributes) {
        CommonValidator.isNotNull(elementId,elementSmartSpace,actionType,creationTimeStamp,playerEmail,playerSmartSpace);
        CommonValidator.isEmail(playerEmail);
        CommonValidator.minSize(1,elementId,elementSmartSpace,actionType,playerSmartSpace);
        ActionEntity actionEntity = new ActionEntity(elementSmartSpace, elementSmartSpace, creationTimeStamp, elementId, playerSmartSpace, playerEmail, actionType, moreAttributes);
        logger.info("createNewAction method success. actionEntity "+actionEntity);
        return actionEntity;
    }




}



