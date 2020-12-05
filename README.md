iwindplus-platform-dependencies包含两个模块
分别为iwindplus-boot-dependencies,iwindplus-cloud-dependencies
其中iwindplus-boot-dependencies是基于springboot封装的模块
iwindplus-cloud-dependencies是基于springcloud封装的模板,其中申明了spring-cloud-alibaba-dependencies版本

nexus快照版本（开发版）步骤:
修改pom.xml文件中nexus-snapshots-url为snapshots仓库地址
操作命令： mvn deploy

nexus RELEASE版本步骤：
修改pom.xml文件中nexus-releases-url为releases仓库地址
修改pom.xml文件中release-username(git账户),release-password(git密码)
操作命令:
第一步： mvn release:prepare
第二步： mvn release:perform
后悔药：mvn release:rollback
连起来的命令：release:clean release:prepare release:perform
发布生成环境RELEASE版本操作命令：clean package release:clean release:prepare release:perform
执行完成后git源代码仓库会新建一个tag, nexus的release仓库会看到有正式版maven仓库管理包,相当于封装了一个jar包给其他工程引用
再新建工程就可以maven中引用,而不需要下载源码编译再引用

修改工程版本
versions:set -DnewVersion=1.0.0-SNAPSHOT
