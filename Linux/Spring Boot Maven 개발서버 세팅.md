---
sticker: lucide//server
---
``` xml title="pom.xml 추가"
<dependencies>
	...
	<!-- Tomcat을 제외하여 war 파일을 패키징할 수 있도록 합니다. -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-tomcat</artifactId>
		<scope>provided</scope>
	</dependency>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<mainClass>com.example.YourMainClass</mainClass>
				</configuration>
			</plugin>
		</plugins>
	</build>
</dependencies>
```
```java title="MyApplication.java 수정"
@SpringBootApplication
public class MyApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(YourApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(YourApplication.class, args);
	}
}
```
![[Pasted image 20250805173038.png]]
![[Pasted image 20250805173145.png]]
``` bash title="tomcat 설치"
wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.26/bin/apache-tomcat-10.1.26.tar.gz
tar -xvzf [tomcat file name]
mv apache-tomcat-10.1.26 /usr/local/tomcat
```
``` bash title="~/.profile 파일에 환경 변수 설정"
export CATALINA_HOME=/usr/local/tomcat
export PATH=$PATH:$CATALINA_HOME/bin
source ~/.profile
```
``` bash title="war 압축 풀고 톰캣 경로에 저장. conf/server.xml 파일에 Context 추가"
# server.xml
<Host name="localhost" appBase="webapps/bizpack" unpackWARs="true" autoDeploy="true">
	<Context docBase="" path="/"/>
	<Valve className="org.apache.catalina.valves.rewrite.RewriteValve" />
	<Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" prefix="localhost_access_log" suffix=".txt" pattern="%h %l %u %t &quot;%r&quot; %s %b" />
</Host>
```
``` bash title="conf/context.xml 파일에 캐시 설정 추가"
# context.xml
<Resources cachingAllowed="true" cacheMaxSize="100000"/>
```
``` bash title="java 설치 후 환경 변수 설정"
java -version
sudo apt update
sudo apt install java-17-openjdk-adm64
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-adm64
export PATH=$PATH:$JAVA_HOME/bin
source ~/.profile
echo $JAVA_HOME
```
``` bash title="방화벽 확인. 막혀있으면 특정 포트 오픈"
# 추가 : -A 삭제 : -D
sudo iptables-save
sudo iptables -A INPUT -p tcp --dport [포트번호] -j ACCEPT
sudo iptables -D INPUT -p tcp --dport 33123 -j ACCEPT
```
![[Pasted image 20250805174224.png]]
