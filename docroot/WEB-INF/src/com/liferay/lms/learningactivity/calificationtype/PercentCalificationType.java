package com.liferay.lms.learningactivity.calificationtype;


import java.text.DecimalFormat;
import java.util.Locale;

import com.liferay.lms.model.CourseResult;
import com.liferay.lms.model.LearningActivityResult;
import com.liferay.lms.model.ModuleResult;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;


public class PercentCalificationType extends BaseCalificationType {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactoryUtil.getLog(PercentCalificationType.class);
	
	@Override
	public long getTypeId() {
		return 0;
	}
	
	@Override
	public String getName() {
		return "percent_ct";
	}
	
	@Override
	public String getTitle(Locale locale) {
		return "percent_ct.title";
	}
	
	@Override
	public String getDescription(Locale locale) {
		return "percent_ct.description";
	}
	
	@Override
	public String getSuffix() {
		return "/100";
	}

	@Override
	public String translate(Locale locale, double result) {
		DecimalFormat df = new DecimalFormat("##.#");
		return df.format(result);
	}
	
	@Override
	public String translate(Locale locale, CourseResult result) {
		return translate(locale, result.getResult());
	}

	@Override
	public String translate(Locale locale, ModuleResult result) {
		return translate(locale, result.getResult());
	}

	@Override
	public String translate(Locale locale, LearningActivityResult result) {
		return translate(locale, result.getResult());
	}
	
	@Override
	public String translate(Locale locale, long companyId, double result) {
		
		log.debug("** translate -- companyId:"+companyId);		
		return translate(locale, result);
	}
	
	@Override
	public long toBase100(double result) {
		return (long) result;
	}

	@Override
	public long getMinValue() {
		return 0;
	}

	@Override
	public long getMaxValue() {
		return 100;
	}
}
