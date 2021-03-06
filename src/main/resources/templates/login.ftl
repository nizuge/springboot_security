<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>login</title>
</head>
<body>

<form class="form-signin" role="form" name="form" action="/login" method="post">
    <div class="form-group">
        <label for="username">账号</label>
        <input type="text" class="form-control" name="username" value="" placeholder="账号"/>
    </div>
    <div class="form-group">
        <label for="password">密码</label>
        <input type="password" class="form-control" name="password" placeholder="密码"/>
    </div>
    <security:form-login login-processing-url="/j_spring_security_check" username-parameter="j_username"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="submit" id="login" value="Login" class="btn btn-primary"/>
    <span>${msg}</span>
</form>
</body>
</html>