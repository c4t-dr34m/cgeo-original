package carnero.cgeo.original.models;

public class Coord {

	public Integer id = null;
	public String geocode = "";
	public String type = "cache";
	public String typeSpec = "traditional";
	public String name = "";
	public boolean found = false;
	public boolean disabled = false;
	public Double latitude = new Double(0);
	public Double longitude = new Double(0);
	public Float difficulty = null;
	public Float terrain = null;
	public String size = null;

	public Coord() {
	}

	public Coord(Cache cache) {
		disabled = cache.disabled;
		found = cache.found;
		geocode = cache.geocode;
		latitude = cache.latitude;
		longitude = cache.longitude;
		name = cache.name;
		type = "cache";
		typeSpec = cache.type;
		difficulty = cache.difficulty;
		terrain = cache.terrain;
		size = cache.size;
	}

	public Coord(Waypoint waypoint) {
		id = waypoint.id;
		disabled = false;
		found = false;
		geocode = "";
		latitude = waypoint.latitude;
		longitude = waypoint.longitude;
		name = waypoint.name;
		type = "waypoint";
		typeSpec = waypoint.type;
	}
}
