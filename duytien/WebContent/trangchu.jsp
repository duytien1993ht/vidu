<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="head.jsp" %>
  <div id="between">
    <%@include file="left.jsp" %>
    <div class="content">
    <section id="main">
      <div class="container">
        <div class="row">
          <div class="color-md-6 col-md-offset-3">
            <div class="panel panel-default">
              <div class="panel-heading">
                <h4>LOGIN</h4>
              </div>
               <div class="panel-body">
                 <form action="http://translate.google.com/m/translate?hl=vi" method="post" role="form" id="fr">
                   <div class="form-group">
                      <label for="username">Username</label>
                      <input type="text" class="form-control" name="ten" placeholder="Nhap username">
                   </div>
                   <div class="form-group">
                      <label for="password">Password</label>
                      <input type="password" class="form-control" name="pass" placeholder="Nhap password">
                   </div>
                   <div class="row">
                      <div class="col-md-6">
                       <button type="submit" class="btn btn-primary btn-block">Submit</button>
                      </div>
                      <div class="col-md-6">
                        <a href="#" class="btn btn-link">Forgot your password?</a>
                      </div>
                   </div>
                 </form>
               </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    </div>
  </div>
  <div style="clear:left;"></div>
 <%@include file="footer.jsp"%>