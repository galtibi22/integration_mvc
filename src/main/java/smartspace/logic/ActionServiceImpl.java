package smartspace.logic;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.AppProperties;
import smartspace.dao.ExtendedActionDao;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserRole;

@Service
public class ActionServiceImpl implements ActionService {
	private ExtendedActionDao actionDao;
	private ExtendedUserDao<String> userDao;
	private AppProperties appProperties;
	
	@Autowired
	public ActionServiceImpl(ExtendedActionDao actionDao, ExtendedUserDao<String> userDao, AppProperties appProperties) {
		this.actionDao = actionDao;
		this.userDao = userDao;
		this.appProperties = appProperties;
	}
	
	@Override
	public List<ActionEntity> getAll(String adminSmartspace, String adminEmail, int size, int page) {
		if (this.userDao.getUserRole(adminSmartspace, adminEmail) != UserRole.ADMIN) {
			throw new RuntimeException("Only admin users can perform this action");
		}
		return this.actionDao.readAll(size, page, "key");
	}

	@Override
	@Transactional
	public Collection<ActionEntity> store(String adminSmartspace, String adminEmail,
			Collection<ActionEntity> actionEntitiesToImport) {
		if (this.userDao.getUserRole(adminSmartspace, adminEmail) != UserRole.ADMIN) {
			throw new RuntimeException("Only admin users can perform this action");
		} else {
			if(actionEntitiesToImport.stream().anyMatch(entity -> entity.getPlayerSmartspace().equals(appProperties.getName()))) {
				throw new RuntimeException("Not allowed to import data from the local smartspace");
			}
			
			return actionEntitiesToImport.stream()
					.map(entity -> actionDao.create(entity))
					.collect(Collectors.toList());
		}
		
	}

}
