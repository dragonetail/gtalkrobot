package com.gtrobot.dao.word.impl;

import java.util.List;

import com.gtrobot.dao.impl.BaseDaoHibernate;
import com.gtrobot.dao.word.UserFailedWordInfoDao;
import com.gtrobot.model.word.UserFailedWordInfo;
import com.gtrobot.model.word.UserFailedWordInfoKey;

public class UserFailedWordInfoDaoHibernate extends BaseDaoHibernate implements
		UserFailedWordInfoDao {

	/**
	 * @see com.gtrobot.dao.word.UserFailedWordInfoDao#getUserFailedWordInfos(com.gtrobot.model.word.UserFailedWordInfo)
	 */
	public List getUserFailedWordInfos() {
		return getHibernateTemplate().find("from UserFailedWordInfo");
	}

	/**
	 * @see com.gtrobot.dao.word.UserFailedWordInfoDao#getUserFailedWordInfo(Long
	 *      id)
	 */
	public UserFailedWordInfo getUserFailedWordInfo(
			final UserFailedWordInfoKey userFailedWordInfoKey) {
		UserFailedWordInfo userFailedWordInfo = (UserFailedWordInfo) getHibernateTemplate()
				.get(UserFailedWordInfo.class, userFailedWordInfoKey);
		if (userFailedWordInfo == null) {
			log.warn("uh oh, UserFailedWordInfo with id '"
					+ userFailedWordInfoKey + "' not found...");
			return null;
		}

		return userFailedWordInfo;
	}

	/**
	 * @see com.gtrobot.dao.word.UserFailedWordInfoDao#saveUserFailedWordInfo(UserFailedWordInfo
	 *      UserFailedWordInfo)
	 */
	public void saveUserFailedWordInfo(
			final UserFailedWordInfo userFailedWordInfo) {
		getHibernateTemplate().saveOrUpdate(userFailedWordInfo);
	}

	/**
	 * @see com.gtrobot.dao.word.UserFailedWordInfoDao#removeUserFailedWordInfo(Long
	 *      id)
	 */
	public void removeUserFailedWordInfo(
			final UserFailedWordInfo userFailedWordInfo) {
		getHibernateTemplate().delete(userFailedWordInfo);
	}
}
