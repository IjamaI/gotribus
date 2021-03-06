<%@ page language="java"
	import="java.util.*,model.User,model.Activity,model.ActivityClassified,vo.ActivityGoingMax,vo.UserComment,config.GlobleConfig"
	pageEncoding="utf-8"%>
	<%@ taglib uri="/WEB-INF/tld/c-rt.tld" prefix="c"%>
	<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt-rt.tld" %>
	
	<%
	request.setAttribute("path2",GlobleConfig.pathPath1);
	request.setAttribute("path1",GlobleConfig.pathPath);
	//String newPath=GlobleConfig.pathPath.substring(0,)
	String followed=(String)request.getAttribute("followed");
			String joined=(String)request.getAttribute("joined");
			
			String wish=(String)request.getAttribute("wish");
			String tribus=(String)request.getAttribute("tribus");
			
			String owner=(String)request.getAttribute("owner");
			Activity activity=(Activity)request.getAttribute("activityInfo");
			String picPath=activity.getActivityPic_big();
			String[] details=activity.getActivityDetail().split("\\.");
			String detail="";
			if(details.length!=0){
			detail=details[0]+"...";
			}
			int i=0;
			User user=(User)session.getAttribute("user");
			Integer flag=(Integer)request.getAttribute("flagg");
			
			List activityAlbumList=(List)request.getAttribute("activityAlbum");
				String[] pageNumbers=(String[])request.getAttribute("pageNumbers");
			int activityAlbumListSize=activityAlbumList.size();
	%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${activityInfo.activityName }</title>
	<meta name="description" content="" />
	<meta name="keywords" content="" />
    <link rel="stylesheet" type="text/css" media="screen,projection" href="${path1 }activity/font/font.css" />
    <link rel="stylesheet" type="text/css" media="screen,projection" href="${path1 }activity/styleNew.css" />
    <link rel="stylesheet" type="text/css" media="screen,projection" href="${path1 }activity/css/css3.css" />
    <link rel="stylesheet" type="text/css" href="${path1 }activity/css/jquery.lightbox.css" media="screen" />
    <link rel="stylesheet" type="text/css" media="screen,projection" href="${path1 }activity/css/prettyPhoto.css" />
    <!--[if lt IE 10]>
   		<script src="${path1 }activity/js/PIE.js" type="text/javascript"></script>
    <![endif]-->

</head>

<body onload="map()">
	<div id="wrapper"><!--start wrapper-->
    	<div id="header"><!--start header-->
        	<div class="logo"><a name="top" href="${path2}activity/index.action"><img src="${path1 }activity/img/logo.png" alt="" width="59" height="65" /></a></div>
            <div id="header_rgt"><!--start header_rgt-->
            	<div id="menu_bg"><div id="menu_lft"><div id="menu_rgt">
                    <ul>
                    	<li class="current_page_item">
										<a href="${path2}activity/index.action">EVENT</a>
									</li>
									<li>
										<a href="${path2}movie/movieHomePage.action" title="MOVIE">MOVIE</a>
									</li>
									<li>
										<a href="${path2}book/bookHomePage.action" title="BOOK">BOOK</a>
									</li>
									<li>
										<a href="${path2}music/musicHomePage.action" title="MUSIC">MUSIC</a>
									</li>
                        <li>
<a id="bb" onMouseOver="get()"  style="display:block"   href="${path2}user/my.action" title="MY TRIBUS">MY TRIBUS</a>
										<a id="aa"  onMouseOut="bu()" style="font-size:24px; display:none"  href="${path2}user/my.action" title="MY TRIBUS">MyTRIBUS</a>
</li>
                    </ul>
                   <div class="header_search">
                    	<form action="${path2}user/searchAll.action">
                        	<p class="txt_header"><input id="search" name="search" type="text" /></p>
                            <p class="submit_header"><input type="submit" value=" " /></p>
                        </form>
                    </div>
                    <div class="header_icon_area">
                    	<span class="space_btm"><a href="#"><img src="${path1 }activity/img/icon_header1.png" alt="" width="10" height="11" /></a></span>
                        <span><a href="${path2}user/editForm.action"><img src="${path1 }activity/img/icon_header2.png" alt="" width="12" height="13" /></a></span>
                    </div>
                </div></div></div>
            </div><!--//end #header_rgt-->
        </div><!--//end #header-->
        <div id="main_area"><!--start main_area-->
        	<div id="saerch_area"><!--start saerch_area-->
            	<div id="search_bg" class="space_lft"><!--start search_bg-->
                	<form action="${path2}activity/search.action">
                    	<p class="search_text"><input id="txt1" name="searchCondition" type="text" value="Seach for event name and city" onclick="if(this.value=='Seach for event name and city')(this.value='')"  onblur="if(this.value=='')(this.value='Seach for event name and city')" /></p>
                        <p class="search_submit"><input type="submit" value=" " /></p>
                    </form>
                </div><!--//end #search_bg-->
                <div id="social_media"><!--start social_media-->
                	<div id="social_lftcol">
                    	<a href="https://www.facebook.com/TheTribus"><img src="${path1}activity/img/icon_facebook.jpg" alt="" width="24" height="24" /></a>
                        <a href="#"><img src="${path1}activity/img/icon_tweet.jpg" alt="" width="24" height="24" /></a>
                    </div>
                    <div id="social_box"><!--start social_box-->
                    	<div id="message">
                        	<a href="${path2}userMail/box/0.action"><img src="${path1}activity/img/icon_message1.jpg" alt="" width="22" height="22" /><c:if test="${unreadMail > 0}">
								<span>${unreadMail}</span>
							</c:if></a>
                            <a href="${path2}user/about.action"><img src="${path1}activity/img/icon_message2.jpg" alt="" width="22" height="22" /></a>
                            <a href="${path2}user/police.action"><img src="${path1}activity/img/icon_message3.jpg" alt="" width="22" height="22" /></a>
                            <a href="${path2}user/logout.action"><img src="${path1}activity/img/icon_message4.jpg" alt="" width="22" height="22" /></a>
                        </div>
                        <div class="address">
                        	<h3>&nbsp;
									<%if(user==null){%><a href="${path2}user/loginForm.action">login</a>
									<%}else{ %>
									<a href="${path2}user/my.action"><%=user.getUserAlias()%>
										<%} %> </a>
								</h3>
								    <span>${userProf.profCity}</span>
                        </div>
                    </div><!--//end #social_box-->
                </div><!--//end #social_media-->
            </div><!--//end #saerch_area-->
            <div id="common_maincontent"><!--start common_maincontent-->
                <div id="review_lftcol"><!--start review_lftcol-->
                    <div id="product_area"><!--start product_area-->
                    	<h2>${activityInfo.activityName }</h2>
                        <div id="product_twocol"><!--start product_twocol-->
                        	<div id="product_lftcol"><img src="${activityInfo.activityPic_big}" alt="" width="313" height="417" /></div>
                            <div id="product_rgtcol"><!--start product_rgtcol-->
                            	<p>Start Date:<fmt:formatDate value="${activityInfo.activityStartTime}" type="date" dateStyle="medium"/> <br />End Date: <fmt:formatDate value="${activityInfo.activityFinishTime}" type="date" dateStyle="medium"/> <br />PLace: ${activityInfo.activityCity} ${activityInfo.activityStreet} ${activityInfo.activityState}<br />Starter: <a href="${path2}user/friendHome/${user.userId}.action">${user.userAlias}</a><br />
                                Type: ${activityClassified}<br />Fees: ${activityInfo.activityFeesFrom} <c:choose><c:when test="${!empty activityInfo.activityFeesTo}">~ ${activityInfo.activityFeesTo}</c:when></c:choose> USD</p>
                                <div class="king_wish">
                                	<a id="wishList"   href="javascript:WishList('${path2}user/addWishList/city/${activityId}.action')"><%if(wish!=null&&wish.equals("ok")){%>Already Added<%}else{%>+ Track List<%}%></a>
                                	<a href="javascript:void(0)" onClick="showPop()">+ Tribus List</a>
                                </div>
                                <div class="king_social">
                                	<div id='fb-root'></div>
    <script src='http://connect.facebook.net/en_US/all.js'></script>
                                	<a onclick='postToFeed(); return false;'><img src="${path1}activity/img/icon_facebook.jpg" alt="" width="24" height="24" /></a>
                                	   
                                    <a href="#"><img src="${path1}activity/img/icon_tweet.jpg" alt="" width="24" height="24" /></a>
                                    <p id='msg'></p>
                                    <script> 
      FB.init({appId: "167819743335514", status: true, cookie: true});

      function postToFeed() {
var str=location.href;
        // calling the API ...
        var obj = {
          method: 'feed',
          link: str,
          name: "${activityInfo.activityName}",
          picture: '<%=picPath%>',
          description: "<%=detail%>"
        };

        function callback(response) {
         
        }

        FB.ui(obj, callback);
      }
    
    </script> 
                                </div>
                               
                                <div class="king_done">
                                	<%if(owner==null) {%>
                                	<a id="bt1"  class="selectOne" href="javascript:followActivity('${path2}activity/followActivity.action?activityId=${activityId}')"><%if(followed.equals("true")){%><span style="color:grey">Unfollow</span><%}else{%>Follow<%}%></a>
                                    <a id="bt2"  class="selectTwo click_done" href="javascript:joinActivity('${path2}activity/joinActivity.action?activityId=${activityId}')"><%if(joined.equals("true")){%><span style="color:grey">Unjoin</span><%}else{%>Join<%}%></a>
                               <%}else{ %>
                              <a href="${path2}activity/editActivityInit.action?activityId=${activityId}">Edit</a>
                               <%} %>
                                </div>
                                <div class="map">
                                
<div id="map_canvas_old" style="width: 250px; height: 232px; overflow-x: hidden; overflow-y: hidden; "></div>

</div>
								<div class="enlarge"><a href="#?custom=true&width=800&height=400" rel="prettyPhoto">Enlarge</a></div>
                            </div><!--//end #product_rgtcol-->
                        </div><!--//end #product_twocol-->
                    </div><!--//end #product_area-->
                    <div id="brief_main"><!--start brief_main-->
                    	<div class="brief_activity"><!--start brief_activity-->
                        	<h2>Introduction</h2>
                           
                            <div class="comment_box"><!--start comment_box-->
                                <div class="brief_content">
                                	<p>${activityInfo.activityDetail}</p>
                                    <div class="icon_brief"><img src="${path1 }activity/img/icon_plus.jpg" alt="" width="20" height="20" /></div>
                                </div>
                            </div><!--//end .comment_box-->
                        </div><!--//end .brief_activity-->
                        <div class="brief_gallery"><!--start brief_gallery-->
                        	<%if(activityAlbumListSize>0){%><h2><a href="${path2}activity/album.action?activityId=${activityId}">Snapshot</a>   </h2>
                        	<%}else{ %>
                        	<h2>Snapshot</h2>
                        	<%} %>
                        	
                           <c:choose><c:when test="${flagg==1}">  <span class="add_photo"><a href="${path2}activity/addPicIndex.action?activityId=${activityId}">Upload New</a></span></c:when></c:choose>
                           	<div class="brief_img">
                            	<ul id="slider1"><li>
                                 <c:forEach items="${activityAlbum}" var="item">
                           <a href="${path2}activity/showPicList.action?albumId=${item.albumId}"><img src="${item.albumCover_small}" width="103"
								height="103" />  </a>
                               </c:forEach>
                                <div class="clear"></div>
                               </li></ul>
                            </div>
                        </div><!--//end #brief_gallery-->
                       
                    </div><!--//end #brief_main-->
                    <div id="comment_box_brief"><!--start comment_box_brief-->
                     <%if(session.getAttribute("user")!=null){ %>
                        <div class="comment_box"><!--start comment_box-->
                            <div class="arrow_author2"></div>
                            <div class="comment_first_content"><!--start comment_first_content-->
                                <div class="pic_author">
                                    <div class="arrow_author1"></div>
                                     <%if(session.getAttribute("user")!=null){ %>
                                    <img src="<%=user.getUserPic() %>" alt="" width="73" height="73" />
                                    <%}else{ %>
                                    <img src="${path1}activity/img/logo.png" alt="" width="73" height="73" />
                                    <%} %>
                                </div>
                                 <form id="formComment" name="formComment" method="post" action="${path2}activity/addComment.action?activityId=${activityId}">
                                <div class="author_total_rgt">
                                    <div class="author_speech">
                                        <textarea id="commentContent" name="commentContent" rows="10" cols="10" onclick="if(this.value=='Name Says : ...')(this.value='')"  onblur="if(this.value=='')(this.value='Name Says : ...')">Name Says : ...</textarea>
                                    </div>
                                    <div class="author_icon">
                                        <span><img src="${path1 }activity/img/icon_album.jpg" alt="" width="20" height="19" /></span>
                                        <div class="btn_done"><a href="javascript:check()">Done</a></div>
                                    </div>
                                </div>
                                </form>
                            </div><!--//end .comment_first_content-->
                        </div><!--//end .comment_box-->
                        <%} %>
                        <c:forEach items="${userCommentList}" var="item" varStatus="in">
                        
                            <c:choose>
                       <c:when test="${in.index %2!=0}">
                         <div class="comment_box bg_differ"><!--start comment_box-->
                       </c:when>
                       <c:otherwise>
                       <div class="comment_box"><!--start comment_box-->
                       </c:otherwise>
                       </c:choose>
                            <div class="comment_scnd_content"><!--start comment_scnd_content-->
                                <div class="pic_author">
                                    <a href="${path2}user/friendHome/${user.userId}.action"><img src="${item.userPic}" width="72" height="72" /></a>
                                </div>
                                <div class="author_total_rgt">
                                    <div class="author_speech">
                                        <h3><a href="${path2}user/friendHome/${user.userId}.action">${item.userName}</a></h3>
                                        <div class="edit_tools">
                                        	<label  class="text_label">${item.commentContent}</label>
                                            <div style="display: block;" class="edit"></div>
                                            <textarea rows="10" cols="10"></textarea>
                                         </div>
                                        <span><fmt:formatDate value="${item.commentDate}" type="date" dateStyle="medium"/> </span>
                                    </div>
                                </div>
                            </div><!--//end .comment_scnd_content-->
                        </div><!--//end .comment_box-->
                        </c:forEach>
                    </div><!--//end #comment_box_brief-->
                    <div id="comment_pagination">
                         <%if(pageNumbers.length!=0){ %>
                          <a  href="${path2}activity/info.action?activityId=${activityId}&page=<c:choose><c:when test="${current-1 == 0}">1</c:when><c:otherwise>${current-1}</c:otherwise></c:choose>" class="prev"></a>
                          <%} %>
                         <c:forEach items="${pageNumbers}" var="item">  
                         
             <a href="${path2}activity/info.action?activityId=${activityId}&page=${item}">
             ${item}
             </a>
                                    
            </c:forEach>  <%if(pageNumbers.length!=0){ %>
              <a  href="${path2}activity/info.action?activityId=${activityId}&page=<c:choose><c:when test="${current == maxNumber}">${maxNumber}</c:when><c:otherwise>${current+1}</c:otherwise></c:choose>" class="next"></a>
                   <%} %>
                    </div>
                </div><!--//end #review_lftcol-->
                <div class="clear"></div>
            </div><!--//end #common_maincontent-->
           <div id="footer"><p> &copy;2012 goTribus |<span>All rights reserved</span> </p></div>
            <div id="back_to_top"><a href="#top"></a></div>
        </div><!--//end #main_area-->
    </div><!--//end #wrapper-->
    
    <div id="popup_layer" onClick="closePop()"></div>   
    <div id="light_box_wrapper"><!--start light_box_wrapper-->
    	<div id="light_box_bg"><!--start light_box_bg-->
        	<div class="pop_up_cross"> <a href="javascript:void(0)" onClick="closePop()"></a></div>
        	<h2>Add to list</h2>
           <form name="form10" id="form10" action="" method="post">
                <p>
                   <select id="tribusClassify" name="tribusClassify" >	
                    <c:forEach items="${tribusClassify}" var="item">
                        <option value="${item.id}">${item.name}</option>
                      </c:forEach>
                    </select>
                </p>
                <p><textarea rows="10" cols="10" onclick="if(this.value=='Description...')(this.value='')" onblur="if(this.value=='')(this.value='Description...')" >Description...</textarea></p>
                <div class="pop_btn"><a href="javascript:void(0)" onClick="submit()">Add</a></div>
                <div class="pop_btn1"><a href="${path2}tribuslist/add.action">Create </a></div> 
            </form>
        </div><!--//end #light_box_bg-->
    </div><!--//end #light_box_wrapper-->
    <script type="text/javascript" src="${path1 }activity/js/jquery.min.js"></script>
    <script src="${path1 }activity/js/smoothscroll.js" type="text/javascript"></script>
    <script src="${path1 }activity/js/easy-editable-text.js" type="text/javascript"></script>
    <script src="${path1 }activity/js/box.slider.js" type="text/javascript"></script>
    <script src="${path1 }activity/js/jquery.raty.js" type="text/javascript"></script>
	<script type="text/javascript">
	  $(document).ready(function(){
		$('#slider1').bxSlider();
	  });
	</script>
    <script type="text/javascript" src="${path1 }activity/js/jquery.lightbox.js"></script>
	<script type="text/javascript">
        $(document).ready(function() {
				$('.map a').lightBox();
        	});
		
    </script>
    <script type="text/javascript">
		$(function() {
			$('#score').raty({
				  starOn  : 'blue_star1.jpg',
				  starOff : 'blue_star2.jpg'	,							  
				score: function() {
					return $(this).attr('data-rating');
				}
			});
					   
			$(".selectOne").click(function() {
				$(".selectOne").addClass("click_done");
				$(".selectTwo").removeClass("click_done");
			});
			$(".selectTwo").click(function() {
				$(".selectTwo").addClass("click_done");
				$(".selectOne").removeClass("click_done");
			});   
        });
    </script>
    <script type="text/javascript" src="${path1 }activity/js/code.js"></script>
    
    
    
    
    
    <!---------------------------------change----------------------------------->
    
    <script type="text/javascript" src="${path1 }activity/js/jquery.prettyPhoto.js"></script>
    <script type="text/javascript"
		src="http://maps.google.com/maps/api/js?sensor=true">
	</script>
	<script type="text/javascript">
	    
      function map(){
    
     var myLatlng = new google.maps.LatLng(${activityLat},${activityLong});
  var myOptions = {
    zoom: 12,
    center: myLatlng,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  }
  var map = new google.maps.Map(document.getElementById("map_canvas_old"), myOptions);
    
  var marker = new google.maps.Marker({
      position: myLatlng, 
      map: map, 
      title:""
  });  
      }
  function initialize() {
		var latlng = new google.maps.LatLng(${activityLat},${activityLong});
		var myOptions = {
		  zoom: 14,
		  center: latlng,
		  mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		var map = new google.maps.Map(document.getElementById("map_canvas"),
			myOptions);
			var marker = new google.maps.Marker({
    position: latlng,
    map: map,
    title:""
});
	  }
	</script>
    <script type="text/javascript" charset="utf-8">
	$(document).ready(function(){			
		$("a[rel^='prettyPhoto']").prettyPhoto({
			custom_markup: '<div id="map_canvas" style="width:800px; height:400px"></div>',
			changepicturecallback: function(){ initialize(); }
		});
	});
	</script>
    
    <script type="text/javascript" charset="utf-8">
    
       
      
    function WishList(url){
      http_request = false;
    if (window.XMLHttpRequest) {    								// 非IE浏览器
        http_request = new XMLHttpRequest();							//创建XMLHttpRequest对象
    } else if (window.ActiveXObject) {     							// IE浏览器
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");		//创建XMLHttpRequest对象
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");	//创建XMLHttpRequest对象
           } catch (e) {}
        }
    }
    if (!http_request) {
        alert("Cannot create XMLHttpRequest object instance！");
        return false;
    }
    http_request.onreadystatechange = getResultWishList;							//调用返回结果处理函数
    http_request.open('GET', url, true);								//创建与服务器的连接
    http_request.send(null);	
    }
    
    function getResultWishList(){
        if (http_request.readyState == 4) {     							// 判断请求状态
        if (http_request.status == 200) {     							// 请求成功，开始处理返回结果
            document.getElementById("wishList").innerHTML=http_request.responseText;;	//设置提示内容
 
        } else {     													// 请求页面有错误
            alert("The service you requested is unavalible now！");
        }
    }
    }
    
    
 function TribusList(url){
      http_request = false;
    if (window.XMLHttpRequest) {    								// 非IE浏览器
        http_request = new XMLHttpRequest();							//创建XMLHttpRequest对象
    } else if (window.ActiveXObject) {     							// IE浏览器
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");		//创建XMLHttpRequest对象
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");	//创建XMLHttpRequest对象
           } catch (e) {}
        }
    }
    if (!http_request) {
        alert("Cannot create XMLHttpRequest object instance！");
        return false;
    }
    http_request.onreadystatechange = getResultTribusList;							//调用返回结果处理函数
    http_request.open('GET', url, true);								//创建与服务器的连接
    http_request.send(null);	
    }
    
   function  getResultTribusList(){
        if (http_request.readyState == 4) {     							// 判断请求状态
        if (http_request.status == 200) {     							// 请求成功，开始处理返回结果
            document.getElementById("tribusList").innerHTML=http_request.responseText;;	//设置提示内容
 
        } else {     													// 请求页面有错误
            alert("The service you requested is unavalible now！");
        }
    }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  function followActivity(url){
    
     http_request = false;
    if (window.XMLHttpRequest) {    								// 非IE浏览器
        http_request = new XMLHttpRequest();							//创建XMLHttpRequest对象
    } else if (window.ActiveXObject) {     							// IE浏览器
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");		//创建XMLHttpRequest对象
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");	//创建XMLHttpRequest对象
           } catch (e) {}
        }
    }
    if (!http_request) {
        alert("Cannot create XMLHttpRequest object instance！");
        return false;
    }
    http_request.onreadystatechange = getResult;							//调用返回结果处理函数
    http_request.open('GET', url, true);								//创建与服务器的连接
    http_request.send(null);		
    
    }
function getResult() {
    if (http_request.readyState == 4) {     							// 判断请求状态
        if (http_request.status == 200) {     							// 请求成功，开始处理返回结果
            document.getElementById("bt1").innerHTML=http_request.responseText;;	//设置提示内容
             if(http_request.responseText=="unFollow"){
              document.getElementById("bt1").style.color="grey";
             }else{
             document.getElementById("bt1").style.color="#00B0D8";
             }
        } else {     													// 请求页面有错误
            alert("The service you requested is unavalible now！");
        }
    }
}
  function joinActivity(url){
    
     http_request = false;
    if (window.XMLHttpRequest) {    								// 非IE浏览器
        http_request = new XMLHttpRequest();							//创建XMLHttpRequest对象
    } else if (window.ActiveXObject) {     							// IE浏览器
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");		//创建XMLHttpRequest对象
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");	//创建XMLHttpRequest对象
           } catch (e) {}
        }
    }
    if (!http_request) {
        alert("Cannot create XMLHttpRequest object instance！");
        return false;
    }
    http_request.onreadystatechange = getResult1;							//调用返回结果处理函数
    http_request.open('GET', url, true);								//创建与服务器的连接
    http_request.send(null);		
    
    }
    function getResult1() {
    if (http_request.readyState == 4) {     							// 判断请求状态
        if (http_request.status == 200) {     							// 请求成功，开始处理返回结果
            document.getElementById("bt2").innerHTML=http_request.responseText;;	//设置提示内容
 if(http_request.responseText=="unJoin"){
              document.getElementById("bt2").style.color="grey";
              document.getElementById("bt1").style.color="grey";
              document.getElementById("bt1").innerHTML="Unfollow";
             }else{
             document.getElementById("bt2").style.color="#00B0D8";
             }
        } else {     													// 请求页面有错误
            alert("The service you requested is unavalible now！");
        }
    }		  
} 


    function check(){
    	if(document.getElementById("commentContent").value == ""){
    		alert("empty comment");
    	}if(document.getElementById("commentContent").value == "Name Says : ..."){
    		alert("empty comment");
    	}else{
    		document.getElementById("formComment").submit();
    	}
    	
    }


 function submit(){
  resourceId=${activityInfo.activityId};
  typeId=document.getElementById("tribusClassify").value;
   document.form10.action="${path2}/user/addTribusList/city/"+typeId+"/"+rescourceId;
 document.form10.submit();
  closePop();
  }
    
    
    function get(){

document.getElementById("aa").style.display="block";
document.getElementById("bb").style.display="none";

}
function bu(){

document.getElementById("bb").style.display="block";
document.getElementById("aa").style.display="none";
}
    </script>
    
    
    
 </body>
</html>                                                                                                   