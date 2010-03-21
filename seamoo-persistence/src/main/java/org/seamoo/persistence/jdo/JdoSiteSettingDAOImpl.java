package org.seamoo.persistence.jdo;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.seamoo.entities.SiteSetting;
import org.seamoo.persistence.SiteSettingDAO;

public class JdoSiteSettingDAOImpl extends
		JdoGenericDAOImpl<SiteSetting, String> implements SiteSettingDAO {

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
