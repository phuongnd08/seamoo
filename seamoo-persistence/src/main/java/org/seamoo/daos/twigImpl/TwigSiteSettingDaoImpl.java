package org.seamoo.daos.twigImpl;

import org.seamoo.daos.SiteSettingDao;
import org.seamoo.entities.SiteSetting;

public class TwigSiteSettingDaoImpl extends TwigGenericDaoImpl<SiteSetting, String> implements SiteSettingDao {

	public void assignSetting(String key, String value) {
		// TODO Auto-generated method stub
		try {
			SiteSetting s = null;
			s = findByKey(key);
			if (s == null)
				s = new SiteSetting(key, value);
			else
				s.setValue(value);
			newOds().storeOrUpdate(s);
		} finally {

		}
	}

	public String getSetting(String key) {
		// TODO Auto-generated method stub
		SiteSetting s = findByKey(key);
		if (s == null)
			return null;
		return s.getValue();
	}
}
