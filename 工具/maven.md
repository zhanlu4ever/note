# Maven

---

## 资料

## 笔记

#### 1. 使用maven命令创建web项目骨架
	mvn archetype:create -DgroupId=com.cbooy -DartifactId=Xxx -DarchetypeArtifactId=maven-archetype-webapp
	
* 使用maven建立好的maven web 项目默认的servlet版本比较低，需要修改成3.1及以上
	

## 出错记录
* Failed to execute goal org.apache.maven.plugins:maven-archetype-plugin:2.4:create (default-cli) on project standalone-pom: Unable to parse configuration of mojo org.apache.maven.plugins:maven-archetype-plugin:2.4:create for parameter #: Cannot create instance of interface org.apache.maven.artifact.repository.ArtifactRepository: org.apache.maven.artifact.repository.ArtifactRepository.<init>() -> [Help 1]
	* 解决：将mvn archetype:create -DgroupId=com.cbooy -DartifactId=Xxx -DarchetypeArtifactId=maven-archetype-webapp修改为mvn archetype:generate -DgroupId=com.cbooy -DartifactId=Xxx -DarchetypeArtifactId=maven-archetype-webapp