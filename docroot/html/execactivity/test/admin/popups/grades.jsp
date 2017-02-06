<%@page import="com.liferay.lms.service.CourseLocalServiceUtil"%>
<%@page import="com.liferay.lms.learningactivity.calificationtype.CalificationTypeRegistry"%>
<%@page import="com.liferay.lms.learningactivity.calificationtype.CalificationType"%>
<%@page import="com.liferay.lms.service.LearningActivityResultLocalServiceUtil"%>
<%@page import="com.liferay.lms.model.LearningActivityResult"%>
<%@ include file="/init.jsp" %>


<%

LearningActivityResult result = null;

String actId2 = renderRequest.getParameter("actId");
String studentId2 = renderRequest.getParameter("studentId");
if((actId2!=null)&&(studentId2!=null)){
	Long lactId2 = ParamUtil.getLong(renderRequest,"actId");
	Long lstudentId2 = ParamUtil.getLong(renderRequest,"studentId");
	result = LearningActivityResultLocalServiceUtil.getByActIdAndUserId(lactId2, lstudentId2);
}

CalificationType ct = new CalificationTypeRegistry().getCalificationType(CourseLocalServiceUtil.getCourseByGroupCreatedId(themeDisplay.getScopeGroupId()).getCalificationType());

%>
<portlet:actionURL var="setGradesURL" name="setGrades" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">   
		<portlet:param name="ajaxAction" value="setGrades" />      
		<portlet:param name="jspPage" value="/html/execactivity/test/admin/popups/grades.jsp" />           
</portlet:actionURL>

<portlet:actionURL var="updateGradesURL" name="setGrades" windowState="<%= LiferayWindowState.NORMAL.toString() %>">   
</portlet:actionURL>

<aui:form action="<%=updateGradesURL %>" name="fn_grades" method="post" >
	<aui:fieldset>
		<aui:input type="hidden" name="studentId" value='<%=studentId2%>' />
		<aui:input type="hidden" name="actId" value='<%=actId2%>' />
	    <aui:input type="text" name="result" label="offlinetaskactivity.grades" value='<%=result!=null?ct.translate(themeDisplay.getLocale(), themeDisplay.getCompanyId(), result.getResult()):"" %>' >
	    	<aui:validator name="number"></aui:validator>
	    	<aui:validator  name="custom"  errorMessage="<%=LanguageUtil.format(themeDisplay.getLocale(), \"result.must-be-between\", new Object[]{ct.getMinValue(),ct.getMaxValue()})%>"  >
				function (val, fieldNode, ruleValue) {
					var result = false;
					if (val >= <%=ct.getMinValue() %> && val <= <%= ct.getMaxValue() %>) {
						result = true;
					}
					return result;					
				}
			</aui:validator>
	    </aui:input>
	    <liferay-ui:error key="offlinetaskactivity.grades.result-bad-format" message="offlinetaskactivity.grades.result-bad-format" />
		<aui:input type="textarea" cols="40" rows="2" name="comments" label="offlinetaskactivity.comments" value='<%=((result!=null)&&(result.getComments()!=null))?result.getComments():"" %>'/>
	</aui:fieldset>
	
	<aui:button-row>
		<aui:button type="submit" name="saveGrade" value="save"></aui:button>
		<aui:button name="Close" value="cancel" onclick="${renderResponse.getNamespace()}doClosePopupGrades();" type="button" />
	</aui:button-row>
		
	<liferay-ui:success key="offlinetaskactivity.grades.updating" message="offlinetaskactivity.correct.saved" />
	<liferay-ui:error key="offlinetaskactivity.grades.bad-updating" message="offlinetaskactivity.grades.bad-updating" />
</aui:form>