iwindplus-platform-dependencies包含两个模块
分别为iwindplus-boot-dependencies,iwindplus-cloud-dependencies
其中iwindplus-boot-dependencies是基于springboot封装的模块
iwindplus-cloud-dependencies是基于springcloud封装的模板,其中申明了spring-cloud-alibaba-dependencies版本

1.nexus快照版本（开发版）步骤:
修改pom.xml文件中nexus-snapshots-url为snapshots仓库地址
操作命令： mvn deploy

2.nexus RELEASE版本步骤：
修改pom.xml文件中nexus-releases-url为releases仓库地址
修改pom.xml文件中release-username(git账户),release-password(git密码)
操作命令:
1)： mvn release:clean release:prepare
发布到仓库中:
2)： mvn release:perform
后悔药：mvn release:rollback

maven-release-plugin会自动帮我们签出刚才打的tag，然后打包，分发到远程Maven仓库中，至此，整个版本的升级，打标签，发布等工作全部完成。我们可以在远程Maven仓库中看到正式发布的1.0版本。
这可是自动化的 ，正式的 版本发布！

现在我们想要发布1.1.0，然后将主干升级为1.2.0-SNAPSHOT，同时开启一个1.1.x的分支，用来修复1.1.0中的bug。
首先，在发布1.1.0之前，我们创建1.1.x分支，运行如下命令：
mvn release:branch -DbranchName=1.0.0 -DupdateBranchVersions=true -DupdateWorkingCopyVersions=false

3.修改工程版本
mvn versions:set -DnewVersion=1.0.0-SNAPSHOT
