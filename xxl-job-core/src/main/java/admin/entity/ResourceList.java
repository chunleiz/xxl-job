package admin.entity;

import java.util.List;

public class ResourceList {
	private List<Resource> resources;
	private String remainOrDelete;

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public String getRemainOrDelete() {
		return remainOrDelete;
	}

	public void setRemainOrDelete(String remainOrDelete) {
		this.remainOrDelete = remainOrDelete;
	}
	public static class Resource {
		private String type;
		private String name;
		private List indexs;
		private List ids;

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

		public List getIndexs() {
			return indexs;
		}

		public void setIndexs(List indexs) {
			this.indexs = indexs;
		}

		public List getIds() {
			return ids;
		}

		public void setIds(List ids) {
			this.ids = ids;
		}
	}
}


