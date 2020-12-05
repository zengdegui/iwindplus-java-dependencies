web公用模块
1.全局异常处理捕获，项目集成,异常只需throws new BaseException(String errorCode, String message)出去即可,不需要try catch
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
2.Feign调用方式的配置,FeignErrorDecoder异常捕获
3.oauth2相关的配置,Oauth2Helper为工具类

难点:spring-cloud-starter-oauth2,请移步百度了解其工作原理