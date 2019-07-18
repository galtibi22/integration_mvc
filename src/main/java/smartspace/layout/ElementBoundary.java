package smartspace.layout;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;

import java.util.Date;
import java.util.Map;

public class ElementBoundary {
    private BoundaryKey key;
    private String elementType;
    private String name;
    private Date created;
    private boolean expired;
    private Creator creator;
    private LatLng latlng;
    private Map<String,Object> elementProperties;
    
    public ElementBoundary() {}
    
    public ElementBoundary(ElementEntity entity) {
    	if(entity instanceof ElementEntity) {
		this.key = new BoundaryKey(entity.getKey().split(UserEntity.KEY_DELIM)[0], entity.getCreatorSmartSpace());
	} else {
		throw new RuntimeException("Trying to downcast an object that isn't an instance of ElementEntity to an ElementBoundary");
		}
		this.name = entity.getName();
		this.created = entity.getCreationTimeStamp();
		this.expired = entity.getExpired();
		this.creator = new Creator(entity.getCreatorEmail(), entity.getCreatorSmartSpace());
		this.latlng = new LatLng(entity.getLocation().getX(), entity.getLocation().getY());
		this.elementProperties = entity.getMoreAttributes();
		this.elementType=entity.getType();
	}

    public BoundaryKey getKey() {
        return key;
    }

    public void setKey(BoundaryKey key) {
        this.key = key;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public Map<String, Object> getElementProperties() {
        return elementProperties;
    }

    public void setElementProperties(Map<String, Object> elementProperties) {
        this.elementProperties = elementProperties;
    }

// Check if boundary attributes are null before converting element Boundary to element Entity 
    public ElementEntity convertToEntity() {
        ElementBoundary elementBoundary=this;
        ElementEntity elementEntity = new ElementEntity();
        if(elementBoundary.getName() != null) {
        	elementEntity.setName(elementBoundary.getName());
        } else {
        	elementEntity.setName(null);
        }
        if(elementBoundary.getElementType() != null) {
        	elementEntity.setType(elementBoundary.getElementType());
        } else {
        	elementEntity.setType(null);
        }
// No change should be applied to Entity key
        if (elementBoundary.getKey() != null) {
            elementEntity.setKey(elementBoundary.getKey().getId() + ElementEntity.KEY_DELIM + elementBoundary.getKey().getSmartspace());
            elementEntity.setElementSmartspace(elementBoundary.getKey().getSmartspace());
        } else {
            //elementEntity.setKey(null);
            elementEntity.setElementSmartspace(null);
        }
        if(elementBoundary.getCreated() != null) {
        	elementEntity.setCreationTimeStamp(elementBoundary.getCreated());
        } else {
        	elementEntity.setCreationTimeStamp(null);
        }
        if(elementBoundary.getCreator().getEmail() != null) {
        	elementEntity.setCreatorEmail(elementBoundary.getCreator().getEmail());
        } else {
        	elementEntity.setCreatorEmail(null);
        }
        if(elementBoundary.getCreator().getSmartspace() != null) {
        	elementEntity.setCreatorSmartSpace(elementBoundary.getCreator().getSmartspace());
        } else {
        	elementEntity.setCreatorSmartSpace(null);
        }
        if(elementBoundary.getExpired() != null) {
        	elementEntity.setExpired(elementBoundary.getExpired());
        } else {
        	elementEntity.setExpired(null);
        }
        if(elementBoundary.getElementProperties() != null) {
        	elementEntity.setMoreAttributes(elementBoundary.getElementProperties());
        } else {
        	elementEntity.setMoreAttributes(null);
        }
        if(elementBoundary.getLatlng().getLat() != null && elementBoundary.getLatlng().getLng() != null) {
        	elementEntity.setLocation(new Location(elementBoundary.getLatlng().getLat(),elementBoundary.getLatlng().getLng()));
        } else {
        	elementEntity.setLocation(null);
        }
        return elementEntity;
    }

    @Override
    public String toString() {
        return "ElementBoundary{" + "key=" + key + ", elementType='" + elementType + '\'' + ", name='" + name + '\'' + ", created=" + created + ", expired=" + expired + ", creator=" + creator + ", latlng=" + latlng + ", elementProperties=" + elementProperties + '}';
    }
}
