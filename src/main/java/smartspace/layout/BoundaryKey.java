package smartspace.layout;

import java.util.Comparator;
import java.util.stream.IntStream;

public class BoundaryKey {
	String id;
	String smartspace;

	public BoundaryKey() {
	}

	public BoundaryKey(String id, String smartspace) {
		super();
		this.id = id;
		this.smartspace = smartspace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSmartspace() {
		return smartspace;
	}

	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((smartspace == null) ? 0 : smartspace.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		BoundaryKey other = (BoundaryKey) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (smartspace == null) {
			if (other.smartspace != null) return false;
		} else if (!smartspace.equals(other.smartspace)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoundaryKey{" + "id='" + id + '\'' + ", smartspace='" + smartspace + '\'' + '}';
	}

}
