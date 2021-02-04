shiro权限模块
一、有session对接流程
   1、启动类开启注解@EnableShiro
   2、在配置文件中配置ShiroProperty中属性
   3、程序中实现ShiroService
   4、登录接口中加入以下代码:
   Subject subject = SecurityUtils.getSubject();
   ShiroTokenDTO usernamePasswordToken = new ShiroTokenDTO(username, password, rememberMe);
   subject.login(usernamePasswordToken);
一、无session（jwt）对接流程
   1、启动类开启注解@EnableShiroJwt
   2、在配置文件中配置ShiroProperty中属性
   3、程序中注入ShiroService
   4、登录接口中用JWTUtil生成access_token
         在需要登录的接口请求头加入access_token

