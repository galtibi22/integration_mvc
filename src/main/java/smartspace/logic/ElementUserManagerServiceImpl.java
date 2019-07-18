package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartspace.AppProperties;
import smartspace.dao.ExtendedElementDao;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElementUserManagerServiceImpl implements ElementUserManagerService {
    private ExtendedUserDao<String> userDao;
    private ExtendedElementDao<String> elementDao;
    private AppProperties appProperties;

    @Autowired
    public ElementUserManagerServiceImpl(ExtendedUserDao<String> userDao, ExtendedElementDao<String> elementDao, AppProperties appProperties) {
        this.userDao = userDao;
        this.elementDao = elementDao;
        this.appProperties = appProperties;
    }

    @Transactional
    @Override
    public ElementEntity store(String managerSmartspace, String managerEmail, ElementEntity elementEntity) {
        UserRole role=this.userDao.getUserRole(managerSmartspace, managerEmail);
        if (role != UserRole.MANAGER) {
            throw new RuntimeException("Only manager users can perform this action");
        }
        return elementDao.create(elementEntity);
    }
    @Transactional
    @Override
    public void update(String managerSmartspace, String managerEmail, String elementSmartspace, String elementId,ElementEntity elementEntity) {
        UserRole role=this.userDao.getUserRole(managerSmartspace, managerEmail);
        if (role != UserRole.MANAGER) {
            throw new RuntimeException("Only manager users can perform this action");
        }
        //TODO I am not sure its ok. But if I don't set the id how the update will work? and also if he pass the elementSmartspace I think we need to update him
        elementEntity.setKey(elementId + UserEntity.KEY_DELIM + elementSmartspace);
        elementDao.update(elementEntity);
    }

    @Override
    public ElementEntity get(String userSmartspace, String userEmail, String elementSmartspace, String elementId) {
        UserRole role=this.userDao.getUserRole(userSmartspace, userEmail);
        if (role != UserRole.MANAGER && role != UserRole.PLAYER ) {
            throw new RuntimeException("Only manager or player users can perform this action");
        }
        return elementDao.find(elementId,elementSmartspace);
    }

    @Override
    public List<ElementEntity> getAll(String userSmartspace, String userEmail, int page, int size) {
        UserRole role=this.userDao.getUserRole(userSmartspace, userEmail);
        if (role != UserRole.MANAGER && role != UserRole.PLAYER ) {
            throw new RuntimeException("Only manager or player users can perform this action");
        }
        return this.elementDao.readAll(size, page);
    }

    @Override
    public List<ElementEntity> getAllByLocation(String userSmartspace, String userEmail, Double x, Double y, Double distance, int page, int size) {
        UserRole role=this.userDao.getUserRole(userSmartspace, userEmail);
        if (role != UserRole.MANAGER && role != UserRole.PLAYER ) {
            throw new RuntimeException("Only manager or player users can perform this action");
        }
        if (x ==null || y==null || distance==null)
            throw new RuntimeException("You must set x,y and distance to use this search");
        return elementDao.readAll(size,page,x,y,distance);

    }



    @Override
    public List<ElementEntity> getAllByName(String userSmartspace, String userEmail, String name, int page, int size) {
        UserRole role=this.userDao.getUserRole(userSmartspace, userEmail);
        if (role != UserRole.MANAGER && role != UserRole.PLAYER ) {
            throw new RuntimeException("Only manager or player users can perform this action");
        }
        if (name==null)
            throw new RuntimeException("You must set name to use this search");
        return elementDao.readAll(size,name,page);

    }

    @Override
    public List<ElementEntity> getAllByType(String userSmartspace, String userEmail, String type, int page, int size) {
        UserRole role=this.userDao.getUserRole(userSmartspace, userEmail);
        if (role != UserRole.MANAGER && role != UserRole.PLAYER ) {
            throw new RuntimeException("Only manager or player users can perform this action");
        }
        if (type==null)
            throw new RuntimeException("You must set type to use this search");
        return elementDao.readAll(type,size,page);
    }



}
