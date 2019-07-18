package smartspace.logic;

import smartspace.data.ElementEntity;

import java.util.List;

public interface ElementUserManagerService {

    public ElementEntity store(String managerSmartspace,String managerEmail,ElementEntity elementEntity);

    public void update (String managerSmartspace,String managerEmail,String elementSmartspace,String elementId,ElementEntity elementEntity);

    public ElementEntity get(String userSmartspace,String userEmail,String elementSmartspace,String elementId);

    public List<ElementEntity> getAll(String userSmartspace, String userEmail, int page, int size);

    public List<ElementEntity> getAllByLocation(String userSmartspace,String userEmail,Double x,Double y,Double distance,int page,int size);

    public List<ElementEntity> getAllByName(String userSmartspace,String userEmail,String name,int page,int size);

    public List<ElementEntity> getAllByType(String userSmartspace,String userEmail,String type,int page,int size);


}
