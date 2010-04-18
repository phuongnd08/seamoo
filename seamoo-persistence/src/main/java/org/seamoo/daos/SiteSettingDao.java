package org.seamoo.daos;

import java.util.Map;

import org.seamoo.entities.SiteSetting;

public interface SiteSettingDao extends GenericDao<SiteSetting, String>{
	//Map<String, String> getAllSettings();
	String getSetting(String key);
	void assignSetting(String key, String value);
	//void assignSetting(Map<String, String> settings);
}
