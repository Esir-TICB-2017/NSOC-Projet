package webserver.settings;

import database.ReadInDatabase;
import database.WriteInDatabase;
import org.json.JSONArray;
import org.mortbay.util.ajax.JSON;

/**
 * Created by loulou on 15/02/2017.
 */
public class Settings {
	public static JSONArray getSettings() {
		return ReadInDatabase.getSettings();
	}
	public static JSONArray getUserSettings(String userId) {
		return ReadInDatabase.getUserSettings(userId);
	}
	public static JSONArray getDefaultSettings() {
		JSONArray defaultSettings = ReadInDatabase.getDefaultSettings();
		return defaultSettings;
	}
	public static void writeUserDefaultSettings(String userId) {
		WriteInDatabase.resetUserSettings(userId);
	}
 }
