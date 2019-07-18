package smartspace.data;

import smartspace.dao.rdb.MapToJsonConverter;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "ELEMENTS")
public class ElementEntity implements SmartSpaceEntity<String> {
	
	public static final String KEY_DELIM = "_";
	
	//@NotNull
	private String elementSmartspace;
	//@NotNull
	private String elementId;
	//@NotNull
	private Location location;
	//@NotNull
	//@Min(3)
	private String name;
	//@NotNull
	private String type;
	//@NotNull
	//@CreatedDate
	private Date creationTimeStamp;
	private Boolean expired;
	//@NotNull
	private String creatorSmartSpace;
	//@Email
	private String creatorEmail;
	private Map<String,Object> moreAttributes;

	public ElementEntity(){

	}

	public ElementEntity(String elementSmartspace,
						 Location location,
						  String name,
						  String type,
						 Date creationTimeStamp,
						 Boolean expired,
						 String creatorSmartSpace,
						 String creatorEmail,
						 Map<String, Object> moreAttributes) {
		this.elementSmartspace = elementSmartspace;
		this.location = location;
		this.name = name;
		this.type = type;
		this.expired = expired;
		this.creatorSmartSpace = creatorSmartSpace;
		this.creatorEmail = creatorEmail;
		this.moreAttributes = moreAttributes;
		this.creationTimeStamp=creationTimeStamp;
	}
	
	@Column(name="ID")
	@Id
	@Override
	public String getKey() {
		return this.elementId + KEY_DELIM + this.elementSmartspace;
	}

	@Override
	public void setKey(String key) {
		String[] temp = key.split(KEY_DELIM);
		this.elementId = temp[0];
		this.elementSmartspace = temp[1];
	}

	@Transient
	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	@Embedded
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Date creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public String getCreatorSmartSpace() {
		return creatorSmartSpace;
	}

	public void setCreatorSmartSpace(String creatorSmartSpace) {
		this.creatorSmartSpace = creatorSmartSpace;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	@Lob
	@Convert(converter=MapToJsonConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
	public String toString() {
		return "ElementEntity{" + "elementSmartspace='" + elementSmartspace + '\'' + ", elementId='" + elementId + '\'' + ", location=" + location + ", name='" + name + '\'' + ", type='" + type + '\'' + ", creationTimeStamp=" + creationTimeStamp + ", expired=" + expired + ", creatorSmartSpace='" + creatorSmartSpace + '\'' + ", creatorEmail='" + creatorEmail + '\'' + ", moreAttributes=" + moreAttributes + '}';
	}
}

