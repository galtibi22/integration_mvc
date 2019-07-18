package smartspace.logic;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.dao.ExtendedElementDao;
import smartspace.dao.ExtendedUserDao;
import smartspace.AppProperties;
import smartspace.data.ElementEntity;
import smartspace.data.UserRole;

@Service
public class ElementServiceImp implements ElementService {

	private ExtendedUserDao<String> userDao;
	private ExtendedElementDao<String> elementDao;
	private AppProperties appProperties;
	
	@Autowired
	public ElementServiceImp(ExtendedUserDao<String> userDao, ExtendedElementDao<String> elementDao, AppProperties appProperties){
		this.userDao = userDao;
		this.elementDao = elementDao;
		this.appProperties = appProperties;
	}
	
	@Override
	public List<ElementEntity> getAll(String adminSmartspace, String adminEmail, int size, int page) {
		if (this.userDao.getUserRole(adminSmartspace, adminEmail) != UserRole.ADMIN) {
			throw new RuntimeException("Only admin users can perform this action");
		}
		return this.elementDao
				.readAll(size, page, "key");
	}

	@Override
	@Transactional
	public Collection<ElementEntity> store(String adminSmartspace, String adminEmail, Collection<ElementEntity> elementEntitiesToImport) {
		if (this.userDao.getUserRole(adminSmartspace, adminEmail) != UserRole.ADMIN) {
		throw new RuntimeException("Only admin users can perform this action");
	}else {
		if (elementEntitiesToImport.stream().anyMatch(entity -> entity.getElementSmartspace().equals(appProperties.getName()))) {
			throw new RuntimeException("Not allowed to import data from the local smartspace");
			}
		return elementEntitiesToImport.stream()
		.map(entity -> elementDao.updateOrInsert(entity))
		.collect(Collectors.toList());
		}
	}

}
