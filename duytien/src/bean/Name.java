package com.barry.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan("com.barry.*")
@EnableTransactionManagement
//Load to Environment.
@PropertySources({ @PropertySource("classpath:datasource-cfg.properties") })
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

	@Bean(name = "multipartResolver")
	public MultipartResolver getMultipartResolver() {
		CommonsMultipartResolver resover = new CommonsMultipartResolver();
		// 1MB
		resover.setMaxUploadSize(1 * 1024 * 1024);

		return resover;
	}
	
	@Bean(name="dataSource")
	public DataSource getDataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("ds.database-driver"));
		dataSource.setUrl(env.getProperty("ds.url"));
		dataSource.setUsername(env.getProperty("ds.username"));
		dataSource.setPassword(env.getProperty("ds.password"));
		return dataSource;
	}
	
	@Autowired
	@Bean(name = "transactionManager")
	public DataSourceTransactionManager getTransactionManager(DataSource dataSource){
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource);
		return txManager;
	}
}
------------------------------------------------------------------------------------------------------------------------
  
  package com.barry.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
		appContext.register(AppConfig.class);
		
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
												"SpringDispatcher",
												new DispatcherServlet(appContext));
		dispatcher.addMapping("/");
		dispatcher.setLoadOnStartup(1);
		dispatcher.setInitParameter("contextClass", servletContext.getClass().getName());
	}

}
----------------------------------------------------------------------------------------------------------------------
  
 package com.barry.config;

public class PageConfig {
	public static final int USERS_PER_PAGE = 4;
}
-----------------------------------------------------------------------------------------------------------------------
  
  package com.barry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/resources/css/");
		registry.addResourceHandler("/fonts/**").addResourceLocations("/WEB-INF/resources/fonts/");
		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/resources/js/");
		registry.addResourceHandler("/img/**").addResourceLocations("/WEB-INF/resources/img/");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
}
--------------------------------------------------------------------------------------------------------------------------
  
  package com.barry.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.barry.config.PageConfig;
import com.barry.dao.UserDAO;
import com.barry.form.UploadForm;
import com.barry.model.User;

@Controller
public class ImportController {

	@Autowired
	private UserDAO userDAO;
	
	@RequestMapping("/import")
	public String viewImport(HttpServletRequest request, Model model,
			@RequestParam(value = "page", required = false) Integer page){
		/*HttpSession session = request.getSession();
		if(session.getAttribute("login") == null){
			return "redirect:/login";
		}*/
		if(page == null) page = 1;
		UploadForm uploadForm = new UploadForm();
		model.addAttribute("uploadForm", uploadForm);
		int start = (page-1)*PageConfig.USERS_PER_PAGE;
		List<User> list = userDAO.getListUsers(start, start+PageConfig.USERS_PER_PAGE);
		double userCount = userDAO.getUserCount();
		int pageCount = (int)Math.ceil(userCount/PageConfig.USERS_PER_PAGE);
		model.addAttribute("listUsers", list);
		model.addAttribute("USERS_PER_PAGE", PageConfig.USERS_PER_PAGE);
		
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("page", page.intValue());
		return "import";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder){
		Object target = dataBinder.getTarget();
		if(target==null){
			return;
		}
		if(target.getClass() == UploadForm.class){
			dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		}
	}
	
	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	public String upload(HttpServletRequest request,
						Model model,
						@ModelAttribute("uploadForm") UploadForm uploadForm){
		String uploadRootPath = request.getServletContext().getRealPath("upload");
		File uploadRootDir = new File(uploadRootPath);
		if(!uploadRootDir.exists()){
			uploadRootDir.mkdirs();
		}
		CommonsMultipartFile[] fileDatas = uploadForm.getFileDatas();
		for(CommonsMultipartFile fileData : fileDatas){
			String name = fileData.getOriginalFilename();
			if(name!=null && name.length()>0){
				try{
					File serverFile = new File(uploadRootDir.getAbsolutePath()+File.separator+name);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(fileData.getBytes());
					stream.close();
					importData(serverFile);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "redirect:/import";
	}
	
	public void importData(File file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String currentLine;
			ArrayList<User> list = new ArrayList<>();
			while((currentLine = br.readLine())!=null){
				list.add(stringToUser(currentLine));
			}
			br.close();
			userDAO.insertUsers(list);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private User stringToUser(String stringLine){
		String[] arr = stringLine.split(",");
		return new User(arr[0], arr[1], arr[2], arr[3], arr[4]);
	}
}
------------------------------------------------------------------------------------------------------------------------
  
  package com.barry.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.barry.mapper.UserMapper;
import com.barry.model.User;
import com.barry.util.Utils;

@Repository
@Transactional
public class UserDAO extends JdbcDaoSupport {
	
	public static final int ALL = -1;
	
	@Autowired
	public UserDAO(DataSource dataSource){
		setDataSource(dataSource);
	}
	
	public User getUser(String userId){
		String sql = UserMapper.BASE_SQL + " WHERE u.Id = ?";
		Object[] params = new Object[]{userId};
		UserMapper mapper = new UserMapper();
		try{
		    return this.getJdbcTemplate().queryForObject(sql, params, mapper);
		}
		catch (Exception e) {
			System.out.println(userId+" not found");
			return null;
		}
	}
	
	/*public List<User> getListUsers(int start, int end){
		List<User> list = null;
		UserMapper mapper = new UserMapper();
		String sql = "SELECT TOP "+end+" * FROM [dbo].[USER] u1 WHERE u1.Id != 'User_000'"
				+ " EXCEPT SELECT TOP "+(start-1)+" * FROM [dbo].[USER] u2 WHERE u2.Id != 'User_000'";
		list = this.getJdbcTemplate().query(sql, mapper);
		return list;
	}*/
	
	public List<User> getListUsers(int start, int end){
		List<User> list;
		UserMapper rowMapper = new UserMapper();
		String sql = UserMapper.BASE_SQL + " WHERE u.Id != 'User_000'";
		list = this.getJdbcTemplate().query(sql, rowMapper);
		if(start==ALL&&end==ALL){
			return list;
		}
		else if(0<=start && start<list.size() && start<=end){
			if(end>list.size()){
				end = list.size();
			}
			return list.subList(start, end);
		}
		return null;
	}
	
	public void insertUsers(List<User> list){
		String sql = "INSERT INTO user(Id,Group_Id,FirstName,LastName,Password) VALUES(?,?,?,?,?)";
		for(User user:list){
			Object[] params = new Object[]{user.getUserId(),user.getGroupId(),
					user.getFirstName(),user.getLastName(),Utils.shaEncode(user.getPassword())};
			try{
				this.getJdbcTemplate().update(sql, params);
				System.out.println("Inserted " + user.getUserId());
			}catch (Exception e) {
				System.out.println(user.getUserId()+" existed or error when insert");
			}
		}
	}
	
	public int getUserCount(){
		String sql = "SELECT COUNT(Id) FROM USER WHERE Id != 'User_000'";
		int userCount = this.getJdbcTemplate().queryForObject(sql, Integer.class).intValue();
		return userCount;
	}
	
	public List<User> getListUsers(){
		List<User> list;
		UserMapper rowMapper = new UserMapper();
		String sql = UserMapper.BASE_SQL;
		list = this.getJdbcTemplate().query(sql, rowMapper);
		return list;
	}
}
--------------------------------------------------------------------------------------------------------------------------
  
  package com.barry.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.barry.model.User;

public class UserMapper implements RowMapper<User> {

	public static final String BASE_SQL =
			"SELECT u.Id,u.Group_Id,u.FirstName,u.LastName,u.Password "
				+ "FROM USER u";
	
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		String userId = rs.getString("Id");
		String groupId = rs.getString("Group_Id");
		String firstName = rs.getString("FirstName");
		String lastName = rs.getString("LastName");
		String password = rs.getString("Password");
		return new User(userId, groupId, firstName, lastName, password);
	}
	
}
-----------------------------------------------------------------------------------------------------------------------------
  
  package com.barry.util;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class Utils {
	public static String shaEncode(String input){
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(input.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return DatatypeConverter.printHexBinary(digest);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}

-------------------------------------------------------------------------------------------------------------------------------
  
  # DataSource

ds.database-driver=com.mysql.jdbc.Driver
ds.url=jdbc:mysql://localhost:3306/mockproject
ds.username=root
ds.password=root
--------------------------------------------------------------------------------------------------------------------------------

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Import Data</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrap-filestyle.js"></script>
<style type="text/css">
	#title h4{
		padding: 5px;
		background-color: lavender;
		border: 1px solid darkcyan;
	}
	
	#importData{
		margin-top:20px;
		padding:30px;
		border:1px solid darkcyan;
	}
	
	#scrollContainer{
		padding:20px;
		background-color: lightblue;
	}
	
	#inputBox{
		max-height: 200px;
		
	}
	
	#inputBox > div.row{
		margin:10px 0;
	}
	
	#formAction{
		margin-top:5px;
		padding:20px;
	}
	
	#viewData{
		margin-top:20px;
		padding:30px;
		border:1px solid darkcyan;
	}
	
	button{
		width: 100%;
	}
	.tooltip > .tooltip-inner{
		max-width: 100%;
	}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		$("#addRow").click(function(){
			$("#inputBox").append("<div class='row'></div>");
			$("#inputBox > div:last").append("<div class='col-md-9'></div>");
			$("#inputBox > div:last > div:first")
					.append("<input name='fileDatas' type='file' required='true'/>");
			$("#inputBox > div:last").append("<div class='col-md-3'></div>");
			$("#inputBox > div:last > div:last")
			.append("<button class='btn btn-danger removeBtn' type='button'>Remove</button>");
			$("#inputBox > div:last > div:last > .removeBtn:first").click(function(){
				$(this).parent().parent().remove();
				if($("#inputBox").height()<180 && $("#inputBox").hasClass("pre-scrollable")) $("#inputBox").removeClass("pre-scrollable");
			})
			$(":file").filestyle();
			if($("#inputBox").height()>=180 && !$("#inputBox").hasClass("pre-scrollable")) $("#inputBox").addClass("pre-scrollable");
		});
		$("[data-toggle='tooltip']").tooltip();
	});
</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				<div id="title" class="row">
					<h4 class="text-center">IMPORT DATA</h4>
					<div class="col-md-4 col-md-offset-8">
						<div class="col-md-8">
							User: <strong>Admin</strong>
						</div>
						<div class="col-md-4">
							<a href="logout">Logout</a>
						</div>
					</div>
				</div>
				<div id="importData" class="row">
					<h5>Select file(s) to import:</h5>
					<form:form cssClass="form" action="upload" method="POST" enctype="multipart/form-data" modelAttribute="uploadForm">
						<div id="scrollContainer">
						<div id="inputBox">
							<div class="row">
								<div class="col-md-9">
									<form:input path="fileDatas" class="filestyle" type="file" required="true"/>
								</div>
							</div>
						</div>
						</div>
						<div id="formAction">
							<div class="col-md-3"><button class="btn btn-success" id="addRow" type="button">Add</button></div>
							<div class="col-md-3"><button class="btn btn-primary" type="submit">Import</button></div>
						</div>
					</form:form>
				</div>
				<div id="viewData" class="row">
					<h5>List of users which is imported to DB:</h5>
					<table class="table table-bordered">
						<thead>
							<tr class="info">
								<th>No</th>
								<th>User_Id</th>
								<th>Group_Id</th>
								<th>First_Name</th>
								<th>Last_Name</th>
								<th>Password</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="user" items="${listUsers}" varStatus="status">
							<tr>
								<td>${status.index+1+(page-1)*USERS_PER_PAGE}</td>
								<td>${user.userId}</td>
								<td>${user.groupId}</td>
								<td>${user.firstName}</td>
								<td>${user.lastName}</td>
								<c:set var="subPass" value="${fn:substring(user.password,0,10)}"></c:set>
								<td><span class="test" data-toggle="tooltip" title="${user.password}">${subPass}...</span></td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
					<nav aria-label="Page navigation" class="text-center">
						<ul class="pagination">
							<c:if test="${page>1}">
								<li>
									<a href="import?page=1" aria-label="First">
						        	<span aria-hidden="true">&lt;&lt;</span>
						      		</a>
						    	</li>
						    	<li>
						    		<a href="import?page=${page-1}" aria-label="Previous">
						        	<span aria-hidden="true">&lt;</span>
						      		</a>
						    	</li>
					    	</c:if>
					    	<c:if test="${page<=1}">
								<li class="active">
						        	<span aria-hidden="true">&lt;&lt;</span>
						    	</li>
						    	<li class="disabled">
						        	<span aria-hidden="true">&lt;</span>
						    	</li>
					    	</c:if>
					    	<c:if test="${page-2>=1}">
					    		<c:set var="begin" value="${page-2}"></c:set>
					    	</c:if>
					    	<c:if test="${page-2<1}">
					    		<c:set var="begin" value="1"></c:set>
					    	</c:if>
					    	<c:if test="${begin+4<=pageCount}">
					    		<c:set var="end" value="${begin+4}"></c:set>
					    	</c:if>
					    	<c:if test="${begin+4>pageCount}">
					    		<c:set var="end" value="${pageCount}"></c:set>
					    		<c:if test="${end-4>=1}">
					    			<c:set var="begin" value="${end-4}"></c:set>
					    		</c:if>
					    	</c:if>
					    	<c:forEach var="i" begin="${begin}" end="${end}">
					    		<c:if test="${i==page}">
					    			<li class="active"><a href="import?page=${i}">${i}</a></li>
					    		</c:if>
					    		<c:if test="${i!=page}">
					    			<li><a href="import?page=${i}">${i}</a></li>
					    		</c:if>
					    	</c:forEach>
						    
						    <c:if test="${page<pageCount}">
							<li>
								<a href="import?page=${page+1}" aria-label="Next">
					        	<span aria-hidden="true">&gt;</span>
					      		</a>
					    	</li>
					    	<li>
					    		<a href="import?page=${pageCount}" aria-label="Last">
					        	<span aria-hidden="true">&gt;&gt;</span>
					      		</a>
					    	</li>
					    	</c:if>
					    	<c:if test="${page==pageCount}">
							<li class="disabled">
					        	<span aria-hidden="true">&gt;</span>
					    	</li>
					    	<li class="active">
					        	<span aria-hidden="true">&gt;&gt;</span>
					    	</li>
					    	</c:if>
					  	</ul>
					</nav>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
--------------------------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns="http://java.sun.com/xml/ns/javaee"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
    http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
    id="WebApp_ID" version="3.0">
    
    <display-name>HelloSpringMVC</display-name>
    
</web-app>
-----------------------------------------------------------------------------------------------------------------------------------

<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/javax.servlet/jstl -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>jstl</artifactId>
			<version>1.2</version>
		</dependency>

		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>javax.servlet.jsp</groupId>
			<artifactId>jsp-api</artifactId>
			<version>2.2</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>4.3.4.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-web</artifactId>
			<version>4.3.4.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>4.3.4.RELEASE</version>
		</dependency>
		<!-- http://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jdbc</artifactId>
			<version>4.3.4.RELEASE</version>
		</dependency>

		<!-- http://mvnrepository.com/artifact/org.springframework/spring-tx -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-tx</artifactId>
			<version>4.3.4.RELEASE</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc -->
		<dependency>
			<groupId>com.microsoft.sqlserver</groupId>
			<artifactId>mssql-jdbc</artifactId>
			<version>6.1.0.jre8</version>
		</dependency>

		<!-- Apache Commons FileUpload -->
		<!-- http://mvnrepository.com/artifact/commons-fileupload/commons-fileupload -->
		<dependency>
			<groupId>commons-fileupload</groupId>
			<artifactId>commons-fileupload</artifactId>
			<version>1.3.1</version>
		</dependency>

		<!-- Apache Commons IO -->
		<!-- http://mvnrepository.com/artifact/commons-io/commons-io -->
		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>2.4</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.9</version>
		</dependency>
-------------------------------------------------------------------------------------------------------------------------

https://us2.proxysite.com/process.php?d=joV9tBmIEllcicLRUhncnMMJIXJP2zog9pT4yD%2BEsvXrE2SuJXovCm5Z5Res3vqfFFbw1iEqmLwBrXiGtcSE6210LTvmWZe%2BQdOHAyoD&b=2

----------------------------------------------------------------------------------------------------------------------------
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
    	sessionBuilder.addAnnotatedClass(HocVien.class);
    	sessionBuilder.addAnnotatedClass(HocVienDao.class);
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

---------------------------------------------------------------------------------------------------------------------------------
	
	package huynhquang.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import huynhquang.entities.HocVien;

@Transactional
@Repository
public class HocVienDao {
	@Autowired
	private SessionFactory sessionFactory;

	public HocVienDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<HocVien> getAllUser() {
		List<HocVien> list = new ArrayList<HocVien>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HocVien.class);
		list = criteria.list();
		return list;

	}

	public void deleteUser(HocVien hocvien) {
		sessionFactory.getCurrentSession().delete(hocvien);
		
	}

	public void insertHocvien(HocVien hocvien) {
		sessionFactory.getCurrentSession().saveOrUpdate(hocvien);
	}

	public HocVien getEdit(String id) {
		return (HocVien) sessionFactory.getCurrentSession().get(HocVien.class, id);
		
	}

}

-----------------------------------------------------------------------------
	
	package huynhquang.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import huynhquang.dao.HocVienDao;
import huynhquang.entities.HocVien;

@Controller
public class MainController {
	@Autowired
	private HocVienDao  hocviendao;
	
	
	@RequestMapping(value="/")
	public String Home(){
		return "redirect:/index";
	}
	

	
	@RequestMapping(value="/index", method= RequestMethod.GET)
	public String Test(Model model){
		List<HocVien> list = hocviendao.getAllUser();
		model.addAttribute("list", list);
		return"index";
	}
	
	
	@RequestMapping(value="/del", method=RequestMethod.GET)
	public String DelUser(@RequestParam(name="id_hv", required=false)String userId, HocVien hocvien, ModelMap model) {
	//	HocVien hocvien = new HocVien();
		hocvien.setId_hv(userId);
		hocviendao.deleteUser(hocvien);
		return "redirect:/index";
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveOrUpdateUser(@ModelAttribute(name="userForm") HocVien hocvien,
			BindingResult result, Model model) {
		hocviendao.insertHocvien(hocvien);
		return"redirect:/index";
		//...
	}
	
	
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String EditHocVien(@RequestParam(name="id_hv", required=false)String userId, HocVien hocvien, ModelMap model) {
	//	HocVien hocvien new HocVien();
		hocvien.setId_hv(userId);
		HocVien hocviens= hocviendao.getEdit(userId);
		System.out.println("in hoc vien: "+hocviens);
		
		return "redirect:/index";
	}
	
	

}

