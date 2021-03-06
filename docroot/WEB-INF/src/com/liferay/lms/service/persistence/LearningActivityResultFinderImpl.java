package com.liferay.lms.service.persistence;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.liferay.lms.model.LearningActivityResult;
import com.liferay.lms.model.LmsPrefs;
import com.liferay.lms.service.LmsPrefsLocalServiceUtil;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.ORMException;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

public class LearningActivityResultFinderImpl extends BasePersistenceImpl<LearningActivityResult> implements LearningActivityResultFinder{
	
	Log log = LogFactoryUtil.getLog(LearningActivityResultFinderImpl.class);
	 	
	public long countStartedOnlyStudents(long actId, long companyId, long courseGropupCreatedId, List<User> _students, long teamId){
		Session session = null;
		try{
			LmsPrefs prefs = LmsPrefsLocalServiceUtil.fetchLmsPrefs(companyId);			
			long teacherRoleId=RoleLocalServiceUtil.getRole(prefs.getTeacherRole()).getRoleId();
			long editorRoleId=RoleLocalServiceUtil.getRole(prefs.getEditorRole()).getRoleId();
			
			String sql="SELECT count(1) FROM lms_learningactivityresult r " +
					" INNER JOIN users_groups ug ON r.userId = ug.userId AND ug.groupId ="+courseGropupCreatedId;
			if(teamId>0){
				sql+=" INNER JOIN users_teams ut ON r.userId = ut.userId AND ut.teamId = "+teamId;
			}
			sql+=" WHERE actId="+actId;
			
			// Se prepara el metodo para recibir un Listado de estudiantes especificos,, por ejemplo que pertenezcan a alguna organizacion. Sino, se trabaja con todos los estudiantes del curso.
			if(Validator.isNotNull(_students) && _students.size() > 0){
				sql += " AND r.userId in (-1";
				for(User user:_students){
					sql+=","+user.getUserId();
				}
				sql+=") ";
			}
			
			sql+=" AND r.userId not in ( SELECT userId FROM usergrouprole WHERE usergrouprole.groupId = " +courseGropupCreatedId+
				 " AND usergrouprole.roleId in ("+teacherRoleId+","+editorRoleId+"))";	
			
			session = openSession();			
			
			log.debug("sql: " + sql);			
			
			SQLQuery q = session.createSQLQuery(sql);
			return ((List<BigInteger>) q.list()).get(0).longValue();
			
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	        closeSession(session);
	    }
	
	    return 0;
	}

	
	public long countFinishedOnlyStudents(long actId, long companyId, long courseGropupCreatedId, List<User> _students, long teamId){
		Session session = null;
		try{
			LmsPrefs prefs = LmsPrefsLocalServiceUtil.fetchLmsPrefs(companyId);			
			long teacherRoleId=RoleLocalServiceUtil.getRole(prefs.getTeacherRole()).getRoleId();
			long editorRoleId=RoleLocalServiceUtil.getRole(prefs.getEditorRole()).getRoleId();
			
			String sql="SELECT count(1) FROM lms_learningactivityresult r " +
					" INNER JOIN users_groups ug ON r.userId = ug.userId " +
					" AND ug.groupId ="+courseGropupCreatedId;
			if(teamId>0){
				sql+=" INNER JOIN users_teams ut ON r.userId = ut.userId AND ut.teamId = "+teamId;
			}
			sql+=" WHERE actId="+actId+" AND r.endDate IS NOT NULL ";
			
			// Se prepara el metodo para recibir un Listado de estudiantes especificos,, por ejemplo que pertenezcan a alguna organizacion. Sino, se trabaja con todos los estudiantes del curso.
			if(Validator.isNotNull(_students) && _students.size() > 0){
				sql += " AND r.userId in (-1";
				for(User user:_students){
					sql+=","+user.getUserId();
				}
				sql+=") ";
			}
			
			sql+=" AND r.userId not in ( SELECT userId FROM usergrouprole WHERE usergrouprole.groupId = " +courseGropupCreatedId+
				 " AND usergrouprole.roleId in ("+teacherRoleId+","+editorRoleId+"))";	
			
			session = openSession();			
			
			log.debug("sql: " + sql);			
			
			SQLQuery q = session.createSQLQuery(sql);
			
			return ((List<BigInteger>) q.list()).get(0).longValue();
			
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	        closeSession(session);
	    }
	
	    return 0;
	}
		
	
	public long countPassedOnlyStudents(long actId, long companyId, long courseGropupCreatedId, List<User> _students, boolean passed, long teamId){
		Session session = null;
		try{
			LmsPrefs prefs = LmsPrefsLocalServiceUtil.fetchLmsPrefs(companyId);			
			long teacherRoleId=RoleLocalServiceUtil.getRole(prefs.getTeacherRole()).getRoleId();
			long editorRoleId=RoleLocalServiceUtil.getRole(prefs.getEditorRole()).getRoleId();
			
			String sql="SELECT count(1) FROM lms_learningactivityresult r " +
					" INNER JOIN users_groups ug ON r.userId = ug.userId " +
					" AND ug.groupId ="+courseGropupCreatedId;
			if(teamId>0){
				sql+=" INNER JOIN users_teams ut ON r.userId = ut.userId AND ut.teamId = "+teamId;
			}
			sql+=" WHERE actId="+actId +" AND r.endDate IS NOT NULL AND r.passed = "+passed ;
			
			// Se prepara el metodo para recibir un Listado de estudiantes especificos,, por ejemplo que pertenezcan a alguna organizacion. Sino, se trabaja con todos los estudiantes del curso.
			if(Validator.isNotNull(_students) && _students.size() > 0){
				sql += " AND r.userId in (-1";
				for(User user:_students){
					sql+=","+user.getUserId();
				}
				sql+=") ";
			}
			
			
			
			sql+=" AND r.userId not in ( SELECT userId FROM usergrouprole WHERE usergrouprole.groupId = " +courseGropupCreatedId+
				 " AND usergrouprole.roleId in ("+teacherRoleId+","+editorRoleId+"))";	
			
			session = openSession();			
			
			log.debug("sql: " + sql);			
			
			SQLQuery q = session.createSQLQuery(sql);
			return ((List<BigInteger>) q.list()).get(0).longValue();
			
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	        closeSession(session);
	    }
	
	    return 0;
	}
	
	
	
	public Double avgResultOnlyStudents(long actId, long companyId, long courseGropupCreatedId, List<User> _students, long teamId){
		Session session = null;
		double result = 0;
		try{
			LmsPrefs prefs = LmsPrefsLocalServiceUtil.fetchLmsPrefs(companyId);			
			long teacherRoleId=RoleLocalServiceUtil.getRole(prefs.getTeacherRole()).getRoleId();
			long editorRoleId=RoleLocalServiceUtil.getRole(prefs.getEditorRole()).getRoleId();
			
			String sql="SELECT AVG(r.result) " +
					" FROM lms_learningactivityresult r " +
					" INNER JOIN users_groups ug ON r.userId = ug.userId " +
					" AND ug.groupId ="+courseGropupCreatedId;
			if(teamId>0){
				sql+=" INNER JOIN users_teams ut ON r.userId = ut.userId AND ut.teamId = "+teamId;
			}
			sql+=" WHERE actId="+actId +" AND r.endDate IS NOT NULL ";
			
			// Se prepara el metodo para recibir un Listado de estudiantes especificos,, por ejemplo que pertenezcan a alguna organizacion. Sino, se trabaja con todos los estudiantes del curso.
			if(Validator.isNotNull(_students) && _students.size() > 0){
				sql += " AND r.userId in (-1";
				for(User user:_students){
					sql+=","+user.getUserId();
				}
				sql+=") ";
			}
			
			sql+=" AND r.userId not in ( SELECT userId FROM usergrouprole WHERE usergrouprole.groupId = " +courseGropupCreatedId+
				 " AND usergrouprole.roleId in ("+teacherRoleId+","+editorRoleId+"))";	
			
			session = openSession();			
			
			log.debug("sql: " + sql);			
			
			SQLQuery q = session.createSQLQuery(sql);
			if((q.list()).get(0)!=null){
				result =((List<BigDecimal>) q.list()).get(0).doubleValue();
			}		 
			
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	        closeSession(session);
	    }
	
	    return result;
	}
	
	
	private SessionFactory getPortalSessionFactory() {
		String sessionFactory = "liferaySessionFactory";

		SessionFactory sf = (SessionFactory) PortalBeanLocatorUtil
				.getBeanLocator().locate(sessionFactory);

		return sf;
	}

	public void closeSessionLiferay(Session session) {
		getPortalSessionFactory().closeSession(session);
	}

	public Session openSessionLiferay() throws ORMException {
		return getPortalSessionFactory().openSession();
	}
}
