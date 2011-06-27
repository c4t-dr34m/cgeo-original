package carnero.cgeo.original.models;

public class CachesList {
	public boolean def = false;
	public int id = 0;
	public String title = null;
	public Long updated = null;
	public Double latitude = null;
	public Double longitude = null;
	
	public CachesList(boolean defIn) {
		def = defIn;
	}
	
	public CachesList(boolean defIn, int idIn, String titleIn) {
		def = defIn;
		id = idIn;
		title = titleIn;
	}
}
