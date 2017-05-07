package controller;

import java.io.IOException;
import java.net.URL;

//the old namespace mentioned in the original tutorial is outdated
//import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bean.User;

public class UserTest {
public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
 URL jsonUrl = new URL("https://raw.githubusercontent.com/coolaj86/json-examples/master/java/jackson/user.json");
 User user = null;

 ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

 // IMPORTANT
 // without this option set adding new fields breaks old code
 mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

 user = mapper.readValue(jsonUrl, User.class);
 System.out.println(user.getName().getLast());
 System.out.println(user.getName().getFirst());
 System.out.println(user.getUserImage());
}
}
-------------------------------------------------------------------------------------------------------------------------------
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>o7.planning</groupId>
	<artifactId>ExampSpring</artifactId>
	<packaging>war</packaging>
	<version>0.0.1-SNAPSHOT</version>
	<name>ExampSpring Maven Webapp</name>
	<url>http://maven.apache.org</url>

	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.10 -->
		<dependency>
			<groupId>org.apache.spark</groupId>
			<artifactId>spark-core_2.10</artifactId>
			<version>1.6.1</version>
		</dependency>
		<dependency>
			<groupId>org.apache.spark</groupId>
			<artifactId>spark-mllib_2.10</artifactId>
			<version>1.6.1</version>
		</dependency>

		<dependency>
			<groupId>org.apache.spark</groupId>
			<artifactId>spark-sql_2.10</artifactId>
			<version>1.6.1</version>
		</dependency>

		<dependency>
			<groupId>com.github.fommil.netlib</groupId>
			<artifactId>all</artifactId>
			<version>1.1.2</version>
			<type>pom</type>
		</dependency>
		<dependency>
			<groupId>commons-cli</groupId>
			<artifactId>commons-cli</artifactId>
			<version>1.2</version>
		</dependency>
		<dependency>
			<groupId>de.l3s.boilerpipe</groupId>
			<artifactId>boilerpipe</artifactId>
			<version>1.1.0</version>
		</dependency>
		<dependency>
			<groupId>xerces</groupId>
			<artifactId>xercesImpl</artifactId>
			<version>2.11.0</version>
		</dependency>
		<dependency>
			<groupId>org.cyberneko.pull</groupId>
			<artifactId>nekopull</artifactId>
			<version>0.2.4</version>
		</dependency>
		<dependency>
			<groupId>net.sourceforge.nekohtml</groupId>
			<artifactId>nekohtml</artifactId>
			<version>1.9.22</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/org.apache.lucene/lucene-analyzers -->
		<dependency>
			<groupId>org.apache.lucene</groupId>
			<artifactId>lucene-analyzers</artifactId>
			<version>3.6.1</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-ooxml</artifactId>
			<version>3.7-beta3</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/com.invincea/spark-hash -->
		<dependency>
			<groupId>com.invincea</groupId>
			<artifactId>spark-hash</artifactId>
			<version>0.1.3</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.springframework/spring-core -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>4.2.4.RELEASE</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>4.2.4.RELEASE</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.springframework/spring-orm -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-orm</artifactId>
			<version>4.2.4.RELEASE</version>
		</dependency>

		<!-- Tiles API -->
		<!-- http://mvnrepository.com/artifact/org.apache.tiles/tiles-api -->
		<dependency>
			<groupId>org.apache.tiles</groupId>
			<artifactId>tiles-api</artifactId>
			<version>3.0.7</version>
		</dependency>
		<!-- Tiles Core -->
		<!-- http://mvnrepository.com/artifact/org.apache.tiles/tiles-core -->
		<dependency>
			<groupId>org.apache.tiles</groupId>
			<artifactId>tiles-core</artifactId>
			<version>3.0.7</version>
		</dependency>

		<!-- Tiles Servlet -->
		<!-- http://mvnrepository.com/artifact/org.apache.tiles/tiles-servlet -->
		<dependency>
			<groupId>org.apache.tiles</groupId>
			<artifactId>tiles-servlet</artifactId>
			<version>3.0.7</version>
		</dependency>

		<!-- Tiles JSP -->
		<!-- http://mvnrepository.com/artifact/org.apache.tiles/tiles-jsp -->
		<dependency>
			<groupId>org.apache.tiles</groupId>
			<artifactId>tiles-jsp</artifactId>
			<version>3.0.7</version>
		</dependency>
		<!-- Hibernate -->
		<!-- https://mvnrepository.com/artifact/org.hibernate/hibernate-core -->
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>5.1.3.Final</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.hibernate/hibernate-entitymanager -->
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-entitymanager</artifactId>
			<version>5.1.3.Final</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
			<version>5.1.3.Final</version>
		</dependency>
		<!-- http://mvnrepository.com/artifact/javax.servlet/jstl -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>jstl</artifactId>
			<version>1.2</version>
		</dependency>
		<!-- SQLServer JDBC driver (JTDS) -->
		<!-- http://mvnrepository.com/artifact/net.sourceforge.jtds/jtds -->
		<dependency>
			<groupId>net.sourceforge.jtds</groupId>
			<artifactId>jtds</artifactId>
			<version>1.3.1</version>
		</dependency>
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
		</dependency>
		<!-- http://mvnrepository.com/artifact/javax.servlet.jsp/jsp-api -->
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.2</version>
        </dependency>
        <!-- http://mvnrepository.com/artifact/javax.servlet.jsp.jstl/javax.servlet.jsp.jstl-api -->
 
        <dependency>
            <groupId>javax.servlet.jsp.jstl</groupId>
            <artifactId>javax.servlet.jsp.jstl-api</artifactId>
            <version>1.2.1</version>
        </dependency>
        
        <dependency>
			<groupId>commons-fileupload</groupId>
			<artifactId>commons-fileupload</artifactId>
			<version>1.2.1</version> <!-- makesure correct version here -->
		</dependency>
		<!-- Apache Commons Upload -->
		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>1.3.2</version>
		</dependency>
		
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>2.1</version>
				<configuration>
					<source>1.7</source>
					<target>1.7</target>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-shade-plugin</artifactId>
				<version>2.4.3</version>
				<configuration>
					<transformers>
						<transformer
							implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
							<mainClass>vn.vitk.Vitk</mainClass>
						</transformer>
					</transformers>
				</configuration>
				<executions>
					<execution>
						<phase>package</phase>
						<goals>
							<goal>shade</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
	<!-- set property for source code encoding -->
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	</properties>
</project>


