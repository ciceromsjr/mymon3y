<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title>Login</title>
    </head>
    <body>
        <f:view>
            <h:form>
                UserID: <h:inputText value="#{login.user}"/>
                <br/>Password: <h:inputText value="#{login.password}"/>
                <br/><h:commandButton value="Login" action="#{login.loginAction}"/>
            </h:form>
        </f:view>
    </body>
</html>