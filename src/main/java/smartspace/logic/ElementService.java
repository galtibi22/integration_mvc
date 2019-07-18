package smartspace.logic;

import java.util.Collection;
import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementService {
	public List<ElementEntity> getAll(String adminSmartspace, String adminEmail, int size, int page);

	public Collection<ElementEntity> store(String adminSmartspace, String adminEmail, Collection<ElementEntity> elementEntitiesToImport);



}
