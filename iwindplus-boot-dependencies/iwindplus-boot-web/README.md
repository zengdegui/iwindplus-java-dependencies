web模块
1.跨域配置
2.请求body解密，响应body加密配置,有时需要入参boby和出参body加密
	使用示例:
	@DecryptBody(value = DecryptBodyMethod.AES)
	@EncryptBody(value = EncryptBodyMethod.AES)
	@PostMapping("test")
	public ResponseEntity<SmsSendDTO> test(@RequestBody SmsSendDTO data) {
		return ResponseEntity.ok(data);
	}
	DecryptBody注解说明解密方式,EncryptBody说明加密方式
	如果使用了body加解密,则前端也需要用统一的加解密方式做处理
3.全局异常处理捕获，项目集成,异常只需throws new BaseException(String errorCode, String message)出去即可,不需要try catch
	统一了出参格式,包含入参的校验,每次只输出一个参数的错误信息. 统一用ResultVO输出
	输出格式为:
	{
	    "data": {
	        "field": "name",
	        "message": "名称必填"
	    },
	    "error": "param_error",
	    "error_description": "参数不合法"
	}
	其中error为错误编码,建议全局唯一,有时错误描述前端不准确,那么前端就能根据这个错误编码定义弹框消息
4.国际化配置(默认根据部署应用服务器系统语言来),并集成入参校验国际化,
	通过以下配置指定国际化配置文件路径,文件名后缀默认properties
	spring:
		messages:
	    	basename: i18n/messages
5.xss过滤器，请求参数的过滤,在微服务中有时需要请求参数传递到下一个服务:
	可通过以下方式获取:
	XssHttpServletRequestWrapper requestWrapper = new XssHttpServletRequestWrapper(this.request);

接口格式建议:
	@PostMapping("getByMobile")
	public ResponseEntity<UserAccountVO> getByMobile(@RequestParam String mobile) {
		UserAccountVO data = this.baseUserService.getByMobile(mobile);
		return ResponseEntity.ok(data);
	}
	
	此种方式出参为:
	{
	    "host": "10.11.92.35",
	    "gmtCreate": "2020-08-11T13:38:18",
	    "creater": "96bee7c166a8a16b67baf3d0bf0b7279",
	    "gmtModified": "2020-08-11T13:38:18",
	    "modifier": null,
	}
	
	如果需要与全局异常输出统一格式则使用如下方式: 返回ResultVO
	@PostMapping("getByMobile2")
	public ResponseEntity<ResultVO> getByMobile2(@RequestParam String mobile) {
		UserAccountVO data = this.baseUserService.getByMobile(mobile);
		ResultVO result = ResultVO.builder().data(data).build();
		return ResponseEntity.ok(result);
	}
	此种方式出参为:
	{
	    "data": {
	        "host": "10.11.92.42",
	        "gmtCreate": "2020-08-26T11:29:35",
	        "creater": "f4319716b1b7431e854ddbe67c25c6e1",
	        "gmtModified": "2020-08-28T15:06:52",
	        "modifier": "a7f7f20bbf881f7976792c016981590b",
	    },
	    "error": "success",
	    "error_description": "执行成功"
	}
	以上两种方式都行不影响前端调用,前端可通过非200状态码区分来捕获
5.BaseController -- Controller基类
