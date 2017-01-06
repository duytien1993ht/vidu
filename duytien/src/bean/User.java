<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>o7.planning</groupId>
  <artifactId>ExampleHiber</artifactId>
  <packaging>war</packaging>
  <version>0.0.1-SNAPSHOT</version>
  <name>ExampleHiber Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
           <groupId>javax.servlet</groupId>
           <artifactId>javax.servlet-api</artifactId>
           <version>3.1.0</version>
           <scope>provided</scope>
       </dependency>

       <!-- Jstl for jsp page -->
       <!-- http://mvnrepository.com/artifact/javax.servlet/jstl -->
       <dependency>
           <groupId>javax.servlet</groupId>
           <artifactId>jstl</artifactId>
           <version>1.2</version>
       </dependency>


       <!-- JSP API -->
       <!-- http://mvnrepository.com/artifact/javax.servlet.jsp/jsp-api -->
       <dependency>
           <groupId>javax.servlet.jsp</groupId>
           <artifactId>jsp-api</artifactId>
           <version>2.2</version>
           <scope>provided</scope>
       </dependency>

       <!-- Spring dependencies -->
       <!-- http://mvnrepository.com/artifact/org.springframework/spring-core -->
       
       <dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-orm</artifactId>
			<version>4.3.4.RELEASE</version>
		</dependency>
		
		<dependency>
			<groupId>net.sf.opencsv</groupId>
			<artifactId>opencsv</artifactId>
			<version>2.3</version>
		</dependency>
       <!-- http://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-webmvc</artifactId>
           <version>4.1.4.RELEASE</version>
       </dependency>

       <!-- http://mvnrepository.com/artifact/org.springframework/spring-orm -->
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-orm</artifactId>
           <version>4.1.4.RELEASE</version>
       </dependency>

       <!-- Hibernate -->
       <!-- http://mvnrepository.com/artifact/org.hibernate/hibernate-core -->
       <dependency>
           <groupId>org.hibernate</groupId>
           <artifactId>hibernate-core</artifactId>
           <version>4.3.8.Final</version>
       </dependency>

       <!-- http://mvnrepository.com/artifact/org.hibernate/hibernate-entitymanager -->
       <dependency>
           <groupId>org.hibernate</groupId>
           <artifactId>hibernate-entitymanager</artifactId>
           <version>4.3.8.Final</version>
       </dependency>


       <!-- http://mvnrepository.com/artifact/org.hibernate/hibernate-c3p0 -->
       <dependency>
           <groupId>org.hibernate</groupId>
           <artifactId>hibernate-c3p0</artifactId>
           <version>4.3.8.Final</version>
       </dependency>


       <!-- MySQL JDBC driver -->
       <!-- http://mvnrepository.com/artifact/mysql/mysql-connector-java -->
       <dependency>
           <groupId>mysql</groupId>
           <artifactId>mysql-connector-java</artifactId>
           <version>5.1.34</version>
       </dependency>

       <!-- Oracle JDBC driver -->
       <dependency>
           <groupId>com.oracle</groupId>
           <artifactId>ojdbc6</artifactId>
           <version>11.2.0.3</version>
       </dependency>

       <!-- SQLServer JDBC driver (JTDS) -->
       <!-- http://mvnrepository.com/artifact/net.sourceforge.jtds/jtds -->
       <dependency>
           <groupId>net.sourceforge.jtds</groupId>
           <artifactId>jtds</artifactId>
           <version>1.3.1</version>
       </dependency>
   
  </dependencies>
  <build>
    <finalName>ExampleHiber</finalName>
  </build>
</project>
------------------------------------------------------------------------------------------------------------------------

<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns="http://java.sun.com/xml/ns/javaee"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
    http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
    id="WebApp_ID" version="3.0">
  <display-name>Archetype Created Web Application</display-name>
</web-app>
-------------------------------------------------------------------------------------------------------------------------

package duy.tien.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import duy.tien.dao.EmployeeDAO;
import duy.tien.entity.Employee;

@Configuration
@ComponentScan("duy.tien.*")
@EnableTransactionManagement
// Load to Environment.
@PropertySource("classpath:datasource-cfg.properties")
public class AppConfig {
	@Autowired
	private Environment env;


	@Bean(name = "viewResolver")
	public InternalResourceViewResolver getViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	/*@Bean(name = "multipartResolver")
	public MultipartResolver getMultipartResolver() {
		CommonsMultipartResolver resover = new CommonsMultipartResolver();
		// 1MB
		resover.setMaxUploadSize(1 * 1024 * 1024);

		return resover;
	}*/

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		// Xem: datasouce-cfg.properties
		dataSource.setDriverClassName(env.getProperty("ds.database-driver"));
		dataSource.setUrl(env.getProperty("ds.url"));
		dataSource.setUsername(env.getProperty("ds.username"));
		dataSource.setPassword(env.getProperty("ds.password"));

		System.out.println("## getDataSource: " + dataSource);

		return dataSource;
	}

	private Properties getHibernateProperties() {
    	Properties properties = new Properties();
    	properties.put("hibernate.show_sql", "true");
    	properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
    	return properties;
    }
	
	@Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
    	LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
    	sessionBuilder.addProperties(getHibernateProperties());
    	sessionBuilder.addAnnotatedClass(Employee.class);
    	sessionBuilder.addAnnotatedClass(EmployeeDAO.class);
    	return sessionBuilder.buildSessionFactory();
    }
	
	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(
			SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(
				sessionFactory);

		return transactionManager;
	}
	
}
--------------------------------------------------------------------------------------------------------------------
            
  package duy.tien.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringWebAppInitializer implements WebApplicationInitializer {
	 
	  public void onStartup(ServletContext servletContext) throws ServletException {
	      AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
	      appContext.register(AppConfig.class);
	 
	      ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher",
	              new DispatcherServlet(appContext));
	      dispatcher.setLoadOnStartup(1);
	      dispatcher.addMapping("/");
	        dispatcher.setInitParameter("contextClass", appContext.getClass().getName());
	        servletContext.addListener(new ContextLoaderListener(appContext));
	       // UTF8 Charactor Filter.
	       FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
	       fr.setInitParameter("encoding", "UTF-8");
	       fr.setInitParameter("forceEncoding", "true");
	       fr.addMappingForUrlPatterns(null, true, "/*");
	  }
}
----------------------------------------------------------------------------------------------------------------------------
  
  package duy.tien.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	// Cáº¥u hÃ¬nh Ä‘á»ƒ sá»­ dá»¥ng cÃ¡c file nguá»“n tÄ©nh (html, image, ..)
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/resources/css/").setCachePeriod(31556926);
		registry.addResourceHandler("/img/**").addResourceLocations("/WEB-INF/resources/img/").setCachePeriod(31556926);
		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/resources/js/").setCachePeriod(31556926);
		registry.addResourceHandler("/fonts/**").addResourceLocations("/WEB-INF/resources/fonts/").setCachePeriod(31556926);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}
----------------------------------------------------------------------------------------------------------------------------------
            
 package duy.tien.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import duy.tien.dao.EmployeeDAO;
import duy.tien.entity.Employee;

@Controller
public class MainController {
	
	@Autowired
	private EmployeeDAO employeedao;
	
	
	@RequestMapping(value="/")
	public String Home(Model model){
		return "redirect:/home";
	}
	
	
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public String deleteUser(@RequestParam(value="idhv", required=false)String idhv, Employee employee, ModelMap model) {
		employee.setId(idhv);
		employeedao.deleteUser(employee);
		return "redirect:/home";
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String editUser(@RequestParam(value="idhv", required=false)String idhv,@RequestParam(value="page",required=false) Integer page, Employee employee, ModelMap model){
		employee.setId(idhv);
		model.addAttribute("employee", employeedao.getEdit(idhv));
		long numPage = employeedao.countEmployee()/10;
		model.addAttribute("numPage", numPage);
		if(page ==null ) {
			List<Employee> list = employeedao.getAllUser(1);
			int pageNum = 1;
			model.addAttribute("list", list);
			model.addAttribute("pageNum", pageNum);
			return "list";
		}
		else {
			List<Employee> list = employeedao.getAllUser(page);
			int pageNum = page;
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("list", list);
			return "list";
		}
	}
	
	@RequestMapping(value="/home", method= RequestMethod.GET)
	public String Test(@RequestParam(value="page",required=false) Integer page, ModelMap model,HttpServletRequest req){
		long numPage = employeedao.countEmployee()/10;
		model.addAttribute("numPage", numPage);
		if(page ==null ) {
			List<Employee> list = employeedao.getAllUser(1);
			int pageNum = 1;
			model.addAttribute("list", list);
			model.addAttribute("pageNum", pageNum);
			return "list";
		}
		else {
			List<Employee> list = employeedao.getAllUser(page);
			int pageNum = page;
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("list", list);
			return "list";
		}
	}
	
	@RequestMapping(value="/commit", method=RequestMethod.POST)
	public String insert(@ModelAttribute(value="user_login")Employee employee, Model model,@RequestParam(value="idhv", required=false)String userId){
		employeedao.insertHocvien(employee);
			return "redirect:/home";
	}
	
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public String search(@RequestParam(value="id",required=false)String id,Model model){
		System.out.println(id);
		List<Employee> list = employeedao.getSerach(id);
		model.addAttribute("list", list);
		return "list";
	}
//	@RequestMapping(value="/home", method= RequestMethod.GET)
//	 public String getAllUser(Model model){
//		List<Employee> list = employeedao.getAllUser();
//		model.addAttribute("list", list);
//		return "list";
//	}
	
}
---------------------------------------------------------------------------------------------------------------------------
            
            package duy.tien.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import duy.tien.entity.Employee;


@Transactional
@Repository
public class EmployeeDAO {
	@Autowired
	private SessionFactory sessionFactory;
    
	@Autowired
	public EmployeeDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<Employee> getAllUser() {
		List<Employee> list = new ArrayList<Employee>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getAllUser(int page) {
		List<Employee> list = new ArrayList<Employee>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		if (page == 1) {
			criteria.setFirstResult(0);
			criteria.setMaxResults(10);
			list = criteria.list();
		} else {
			criteria.setFirstResult((page - 1) * 10);
			criteria.setMaxResults(10);
			list = criteria.list();
		}

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Employee> getSerach(String id){
		List<Employee> list = new ArrayList<Employee>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		list = criteria.add(Restrictions.like("id", id+"%")).list();
		System.out.println("Hien thi list: "+list);
		return list;
	}
	
	public void deleteUser(Employee employee) {
		sessionFactory.getCurrentSession().delete(employee);
		
	}
	public void insertHocvien(Employee employee) {
		sessionFactory.getCurrentSession().saveOrUpdate(employee);
	}

	public Employee getHocVien(String id) {
		return (Employee) sessionFactory.getCurrentSession().get(Employee.class, id);
		
	}
	
	public Employee getEdit(String id) {
		return (Employee) sessionFactory.getCurrentSession().get(Employee.class, id);
		
	}

	public Employee findUserById(String id) {
		Employee user = (Employee) sessionFactory.getCurrentSession().load(Employee.class, id);
		return user;


	}

	public long countEmployee() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.setProjection(Projections.rowCount());
		return  (Long) criteria.uniqueResult();
	}
}
--------------------------------------------------------------------------------------------------------------------------------
            package duy.tien.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee implements Serializable{

	private static final long serialVersionUID = -844141939410447932L;
	private String id;
	private String first_name;
	private String last_name;
	private String email;
	private String phone;
	private String dob;

	@Id
	@Column(name="id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="first_name")
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	@Column(name="last_name")
	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	@Column(name="email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name="dob")
	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Employee() {
		super();
	}

	public Employee(String id, String first_name, String last_name, String email, String phone, String dob) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone = phone;
		this.dob = dob;
	}

}
--------------------------------------------------------------------------------------------------------------------------
            
            <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Danh sách</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-3.1.1.min.js"></script>
</head>
<body>
<a href="search?id=12">Search</a>
                <div align="right" class="form-group">
					User:<label>${username}</label>
					<a href="logout"><u>Logout</u></a>
				</div>
 <div style="border: 1px solid #ccc; padding: 20px">
 <div class="container">
  <h2>Update or Insert</h2>
  <form:form class="form-horizontal" action="commit" method="post" modelAttribute="user_login">
    <div class="form-group">
      <label>Id:</label>
      <input type="text" class="form-control" name="id" id="id1" placeholder="Id" value="${employee.id }">
    </div>
    
    <div class="form-group">
      <label>First Name:</label>
      <input type="text" class="form-control" name="first_name" placeholder="Enter First Name" value="${employee.first_name }">
    </div>
    
    <div class="form-group">
      <label>Last_name:</label>
      <input type="text" class="form-control" name="last_name" placeholder="Enter Last Name" value="${employee.last_name }">
    </div>
    
    <div class="form-group">
      <label>Email:</label>
      <input type="text" class="form-control" name="email" placeholder="Enter Email" value="${employee.email }">
    </div>
    
    <div class="form-group">
      <label>Phone:</label>
      <input type="text" class="form-control" name="phone" placeholder="Enter Phone" value="${employee.phone }">
    </div>
    
    <div class="form-group">
      <label>DOB:</label>
      <input type="date" class="form-control" name="dob" placeholder="Enter DOB" value="${employee.dob }">
    </div>
    
    <div align="center"><button type="submit" class="btn btn-default">Commit (Add or Update)</button></div>
 </form:form>
</div>
</div>

   <div style="border: 1px solid #ccc; padding: 20px">
   <div class="container">
  <h2>Basic Table</h2>     
  <table class="table">
    <thead>
      <tr>
       <th>No</th>
	    <th>Id</th>
		<th>First Name</th>
		<th>Last Name</th>
        <th>Email</th>
        <th>Phone</th>
        <th>DOB</th>
        <th>Action</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach var="list" items="${list}" varStatus="status">
      <tr>
        <td>${status.index+1+(pageNum-1)*10}</td>
        <td>${list.id}</td>
		<td>${list.first_name}</td>
		<td>${list.last_name}</td>
		<td>${list.email}</td>
		<td>${list.phone}</td>
		<td>${list.dob}</td>
        <th><a href="edit?idhv=${list.id}&page=${pageNum}"><button type="button" class="btn btn-success">Sửa</button></a>&nbsp;&nbsp;&nbsp;<a href="delete?idhv=${list.id}"><button type="button" class="btn btn-danger">Xóa</button></a></th>
      </tr>
      </c:forEach>
    </tbody>
  </table>
</div>

<div class="row" style="text-align: center;">
					<ul class="pagination">
						<li id="firstPage"><a
							href="${pageContext.request.contextPath}/home?page=1"><<</a></li>
						<c:if test="${pageNum>1 }">
							<li><a id="prev" class="btn btn-default"
								href="${pageContext.request.contextPath}/home?page=${pageNum-1}"><</a>
							</li>
						</c:if>
						<c:if test="${pageNum==1 }">
							<li><a class="btn btn-default" href="" disabled="true"><</a>
							</li>
						</c:if>
						<c:forEach var="i" begin="1" end="${numPage+1}">
							<li id="${i }"><a
								href="${pageContext.request.contextPath}/home?page=${i }">${i}</a></li>
						</c:forEach>

						<li id="next"><a
							href="${pageContext.request.contextPath}/home?page=${pageNum+1}">></a></li>
						<li id="lastPage"><a
							href="${pageContext.request.contextPath}/home?page=${numPage+1}">>></a></li>

					</ul>
					<c:if test='${role == "G_001"}'>
						<div class="col-md-2 pull-right">
							<button id="myBtn" type="button" class="btn btn-primary">New
								Account</button>
						</div>
					</c:if>
				</div>
</div>
</body>
</html>
----------------------------------------------------------------------------------------------------------------------------

# DataSource

ds.database-driver=com.mysql.jdbc.Driver
ds.url=jdbc:mysql://localhost:3306/102120265
ds.username=root
ds.password=root
