package org.seamoo.persistence.daos.jdo;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.seamoo.entities.SiteSetting;
import org.seamoo.persistence.daos.SiteSettingDao;

public class JdoSiteSettingDaoImpl extends
		JdoGenericDaoImpl<SiteSetting, String> implements SiteSettingDao {

	public void assignSetting(String key, String value) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPM();
		try {
			SiteSetting s = null;
			try {
				s = pm.getObjectById(SiteSetting.class, key);
			} catch (JDOObjectNotFoundException ex) {
				// there is no need to shout about this
			}
			if (s == null)
				s = new SiteSetting(key, value);
			else
				s.setValue(value);
			pm.makePersistent(s);
		} finally {
			pm.close();
		}
	}

	public String getSetting(String key) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPM();
		SiteSetting s = null;
		try {
			s = pm.getObjectById(SiteSetting.class, key);
		} catch (JDOObjectNotFoundException ex) {
			// there is no need to shout about this
		}
		if (s == null)
			return null;
		return s.getValue();
	}
}
