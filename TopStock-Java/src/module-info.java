module topstock {
	exports fi.mariapori.topstock;
	opens fi.mariapori.topstock;
	requires transitive java.desktop;
	requires transitive ormlite.jdbc;
	requires java.sql;
}