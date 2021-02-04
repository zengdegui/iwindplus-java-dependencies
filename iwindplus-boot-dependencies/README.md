iwindplus-boot-dependencies 是基于springboot架构的封装
包含以下模块:
  execl模块: 开启注解缓存等
  oss对象存储模块:本地,阿里云,七牛云文件上传,下载等
  mybatis模块: mybatis
  pay模块: 支付宝支付，微信支付
  redis模块: 开启注解缓存等
  shiro权限模块:  权限认证授权等
  sms短信模块:阿里云,七牛云,凌凯短信发送等
  util工具模块:hutool,日期,excel,树形无限极工具类等
  web公用模块:常用的国际化配置(默认根据部署应用服务器系统语言来),跨域配置,全局异常捕获, 请求body解密，响应body加密等
  wechat微信模块:微信公众号,支付,小程序集成等
这些模块都是需要通过注解开启才能使用,与springboot的按需加载不谋而合
模块如何开启使用,请看模块说明文档