Xử lý File Upload trong Spring MVC
Để upload file từ client lên server ta sử dụng thư viện apache common-io và giao diện MultipartResolver của Spring.
Example lấy từ đây http://diepviends.blogspot.com/2013/12/xu-ly-form-trong-spring-mvc.html
Đầu tiên add các thư viện cần thiết vào
mở file pom.xml thêm các dependency sau: 
?
1
2
3
4
5
6
7
8
9
10
11
<dependency>
            <groupid>commons-io</groupid>
            <artifactid>commons-io</artifactid>
            <version>2.1</version>
        </dependency>
<dependency>
            <groupid>javax</groupid>
            <artifactid>javaee-web-api</artifactid>
            <version>6.0</version>
            <scope>provided</scope>
        </dependency>
Cấu hình bean cho dispatcher servlet
Vào /WEB-INF/spring/appServlet/servlet-context.xml định nghĩa bean xử lý MultipartFile 
?
1
2
<beans:bean class="org.springframework.web.multipart.support.StandardServletMultipartResolver" id="multipartResolver">
</beans:bean>
bây giờ cấu hình cho dispatcher servlet xử lý multipart vào web.xml thêm đoạn như sau: 
?
1
2
3
4
5
6
7
8
9
10
11
12
<servlet>
  <servlet-name>appServlet</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <init-param>
   <param-name>contextConfigLocation</param-name>
   <param-value>/WEB-INF/spring/appServlet/servlet-context.xml</param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
  <multipart-config>
   <max-file-size>5000000</max-file-size>
  </multipart-config>
 </servlet>
Xong việc cấu hình.
xử lý file upload thì có 2 cách:
Cách 1: lưu trực tiếp file vào DB, dữ liệu file được lưu trữ là kiểu binary
Cách 2: lưu file vào một thư mục nào đó trên server và sau đó lưu trữ đường dẫn vào DB
Cách thứ 2 thường được sử dụng đối với ứng dụng có upload image từ client
Trong ví dụ này, ta sẽ demo cả 2 cách xử lý trên (cũng tương tự nhau)
Ví dụ của ta sẽ lưu dữ liệu image của product ( kiểu byte) vào trường image (cách 1)
và upload ảnh vào thư mục /resources/upload trên server sau đó lưu đường dẫn của file ảnh được upload lên server vào trường imagePath.(cách 2)
Cụ thể Product class sẽ như sau: 
?
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
package vn.ds.store.domains;
 
import java.io.Serializable;
 
public class Product implements Serializable {
 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 private String name;
 private String price;
 private byte[] image;
 private String imagePath;
 
 public String getName() {
  return name;
 }
 
 public void setName(String name) {
  this.name = name;
 }
 
 public String getPrice() {
  return price;
 }
 
 public void setPrice(String price) {
  this.price = price;
 }
 
 public byte[] getImage() {
  return image;
 }
 
 public void setImage(byte[] image) {
  this.image = image;
 }
 
 public String getImagePath() {
  return imagePath;
 }
 
 public void setImagePath(String imagePath) {
  this.imagePath = imagePath;
 }
  
}
File createProduct.jsp sẽ được sửa như sau: 
?
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
<form:form action="${pageContext.request.contextPath}/product/save" enctype="multipart/form-data" method="post" modelattribute="product">  
  <table>
   <caption>create</caption>
   <tbody>
<tr>
    <td>Name</td>
    <td><form:input path="name"> <form:errors cssclass="err" path="name"></form:errors></form:input></td>
   </tr>
<tr>
    <td>Price</td>
    <td><form:input path="price"> <form:errors cssclass="err" path="price"></form:errors></form:input></td>
   </tr>
<tr>
    <td>Image</td>
    <td><input name="file" type="file"></td>
   </tr>
<tr>
    <td></td>
    <td><input type="submit" value="Submit"></td>
   </tr>
</tbody></table>
</form:form>
Chú ý thêm thuộc tính cho form tag để xử lý upload: enctype="multipart/form-data"
file listProduct.jsp sẽ là: 
?
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
<c:foreach items="${products}" var="p" varstatus="loop">
   </c:foreach><table>
  <caption>List of products</caption>
  <tbody>
<tr>
   <th>Name</th>
   <th>Price</th>
   <th>Image</th>
   <th>imagePath</th>
  </tr>
<tr class="${loop.index % 2 == 0 ? 'even':'odd' }">
    <td>${p.name}</td>
    <td>${p.price}</td>
    <td><img height="200" src="${pageContext.request.contextPath}/product/image" width="300"></td>
    <td><img height="200" src="${p.imagePath}" width="300"></td>
   </tr>
</tbody></table>
Bây giờ ta sẽ viết phần quan trọng nhất - controller xử lý upload ProductController.java 

package vn.ds.store.controllers;
 
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
 
import javax.servlet.http.HttpServletRequest;
 
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
 
import vn.ds.store.domains.Product;
import vn.ds.store.validators.ProductValidator;
 
@Controller
@RequestMapping("product")
public class ProductController {
 
 private ProductValidator validator = new ProductValidator();
 
 @RequestMapping(value = "create", method = RequestMethod.GET)
 public String doGet(Model model) {
  model.addAttribute("product", new Product());
  return "createProduct";
 }
 
 @RequestMapping(value = "save", method = RequestMethod.POST)
 public String doPost(@ModelAttribute Product product, Model model,
   BindingResult errors,
   @RequestParam(value = "file", required = false) MultipartFile file,
   HttpServletRequest request) {
  validator.validate(product, errors);
  if (errors.hasErrors()) {
   return "createProduct";
  }
  if (file != null) {
   byte[] fileContent = null;
   try {
    InputStream inputStream = file.getInputStream();    
    if (inputStream == null)
     System.out.println("File inputstream is null");
    // cach 1 - luu truc tiep
    fileContent = IOUtils.toByteArray(inputStream);    
    product.setImage(fileContent);    
    // cach 2 - upload vao thu muc
    String path = request.getSession().getServletContext().getRealPath("/") + "resources/upload/";
    FileUtils.forceMkdir(new File(path));
    File upload = new File (path + file.getOriginalFilename());
    file.transferTo(upload);
    String imagePath = request.getContextPath() + "/resources/upload/" + file.getOriginalFilename();
    product.setImagePath(imagePath);
    request.getSession().setAttribute("product", product);
    IOUtils.closeQuietly(inputStream);
   } catch (IOException ex) {
    System.out.println("Error saving uploaded file");
   }
  }
  ArrayList<product> lst = new ArrayList<product>();
  lst.add(product);
  model.addAttribute("products", lst);
  return "listProduct";
 }
 
 @RequestMapping(value = "/image", method = RequestMethod.GET)
 @ResponseBody
 public byte[] downloadImg(HttpServletRequest request) {
  Product product = (Product) request.getSession()
    .getAttribute("product");
  if (product != null)
   return product.getImage();
  else
   return null;
 }
}
</product></product>

Ở đây, ta lưu dữ liệu vào session để lấy ra sau (thông thường là lưu vào database, ở đây đang là demo). Cách 1 ta sẽ lưu dữ liệu vào biến image bằng hàm 
?
1
IOUtils.toByteArray(inputStream); 
Sau đó ta định nghĩa một url mà để gọi đến trong src của thẻ img
?
1
<img src="/product/image">
url này chỉ gửi xuống client dữ liệu của image nên ta sử dụng @ResponseBody của spring hỗ trợ để làm việc đó (search google.com để biêt thêm chi tiết), dưới jsp ta chỉ cần gọi đến url này 
?
1
<img height="200" src="${pageContext.request.contextPath}/product/image" width="300">
Cách 2 ta sẽ coppy ảnh vào thư mục /resoures/upload để lấy đường dẫn vật lý trên server để tạo file ta dùng lệnh này: 
?
1
String path = request.getSession().getServletContext().getRealPath("/") + "resources/upload/";
và để hiển thị file trên html, thẻ img chỉ hiển thị đường dẫn internet, ta sẽ lấy đường dẫn theo lệnh sau: 
?
1
String imagePath = request.getContextPath() + "/resources/upload/" + file.getOriginalFilename();
và cuối cùng lệnh coppy data 
?
1
file.transferTo(upload);
