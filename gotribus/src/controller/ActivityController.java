﻿package controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Activity;
import model.ActivityAlbum;
import model.ActivityClassified;
import model.ActivityComment;
import model.ActivityFollow;
import model.ActivityGoing;
import model.ActivityPic;
import model.ActivityPicComment;
import model.TribusClassify;
import model.UserProfile;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import util.GetLatLong;
import vo.ActivityGoingMax;
import vo.ActivityGoingTempResult;
import vo.Ele;
import vo.FriendComment;
import vo.MyActivity;
import vo.SuperActivity;
import vo.UserComment;
import vo.UserCommentSupper;
import vo.UserComments;
import dao.ActivityAlbumDao;
import dao.ActivityClassifiedDao;
import dao.ActivityCommentDao;
import dao.ActivityDao;
import dao.ActivityFollowDao;
import dao.ActivityGoingDao;
import dao.ActivityPicCommentDao;
import dao.ActivityPicDao;
import dao.FollowDao;
import dao.MessageDao;
import dao.TribusListClassifyDao;
import dao.TribusListDao;
import dao.UserPrevilageDao;
import dao.WishListDao;
import dao.UserDao;
import util.DateToString;

@Controller
// 豢咂�ん涴岆controller揭燴濬
@RequestMapping("activity")
// 豢咂�ん涴岆controller爵醱腔勤茼activity腔揭燴濬
public class ActivityController {
	private ActivityDao activityDao = new ActivityDao();
	private ActivityFollowDao activityFollowDao = new ActivityFollowDao();
	private ActivityGoingDao activityGoingDao = new ActivityGoingDao();
	private FollowDao followDao = new FollowDao();
	private ActivityPicDao activityPicDao = new ActivityPicDao();
	private ActivityAlbumDao activityAlbumDao = new ActivityAlbumDao();
	private MessageDao messageDao = new MessageDao();
	private ActivityCommentDao activityCommentDao = new ActivityCommentDao();
	private ActivityClassifiedDao activityClassifiedDao = new ActivityClassifiedDao();
	private ActivityPicCommentDao activityPicCommentDao = new ActivityPicCommentDao();
	private WishListDao wishListDao = new WishListDao();
	private TribusListDao tribusListDao = new TribusListDao();
	private TribusListClassifyDao tribusListClassifyDao = new TribusListClassifyDao();
	private UserDao userDao = new UserDao();
	private Integer userId = null;

	@SuppressWarnings("unchecked")
	@RequestMapping("activityCreatInit")
	// 斐膘魂雄
	public ModelAndView activityCreateInit(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		List<ActivityClassified> ac = activityClassifiedDao
				.getAllActivityClassified();
		// 陓眊枑倳

		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.addObject("activityClassified", ac);// 蔚activityList溫��ん
		view.setViewName("activity/new_create_activity");// 扢离勤茼腔弝芞view/activity/myActivity.jsp
		// view.setViewName("activity/create_activity");//
		// 扢离勤茼腔弝芞view/activity/myActivity.jsp

		return view;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("editActivityInit")
	// 晤憮魂雄場宎珜醱
	public ModelAndView activityEditInit(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		List<ActivityClassified> ac = activityClassifiedDao
				.getAllActivityClassified();
		Integer activityId = Integer.parseInt(request
				.getParameter("activityId"));
		Activity activity = activityDao.getActivityById(activityId);
		String activityClassified = activityClassifiedDao
				.getClassifiedTagById(activity.getClassifiedId());

		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.addObject("activityClassifiedName", activityClassified);
		System.out.println(activityClassified);
		view.addObject("activity", activity);
		view.addObject("activityClassified", ac);// 蔚activityList溫��ん
		view.setViewName("activity/new_edit_activity");// 扢离勤茼腔弝芞view/activity/myActivity.jsp

		return view;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("activityEdit")
	// 晤憮魂雄
	public String activityEdit(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();

		Integer activityId = Integer.parseInt(request
				.getParameter("activityId"));
		Activity activity = activityDao.getActivityById(activityId);

		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String activityName = request.getParameter("hd_activityName");
		Date activityStartTime = null;
		Date activityFinishTime = null;

		String start = request.getParameter("hd_activitystartDate");
		if (start.contains("-")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {

				start = start.substring(0, 10);

				String finish = request.getParameter("hd_activityfinishDate");
				finish = finish.substring(0, 10);
				activityStartTime = dateFormat.parse(start);
				activityFinishTime = dateFormat.parse(finish);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			try {

				start = start.substring(0, 10);

				String finish = request.getParameter("hd_activityfinishDate");
				finish = finish.substring(0, 10);
				activityStartTime = dateFormat.parse(start);
				activityFinishTime = dateFormat.parse(finish);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Integer classifiedId;
		if (request.getParameter("hd_activityClassified") == null) {
			classifiedId = 6;
		} else {
			classifiedId = Integer.parseInt(request
					.getParameter("hd_activityClassified"));
		}

		String activityDetail = request.getParameter("hd_activityDetail");
		// String activityDetail = request.getParameter("hd_activityDetail");
		StringBuffer activityDetailFinal = new StringBuffer("");
		char[] activityDetailTemp = activityDetail.toCharArray();
		for (int i = 0; i < activityDetail.length(); i++) {

			if (i % 93 == 0) {// 在106位置插入换行符
				activityDetailFinal.append("\n");

			}
			activityDetailFinal.append(activityDetailTemp[i]);

		}
		Double activityFeesFrom = Double.parseDouble(request
				.getParameter("hd_activityFeesFrom"));
		String fees = request.getParameter("hd_activityFeesTo");
		Double activityFeesTo = null;
		if (fees != "") {
			System.out.println(fees);
			activityFeesTo = Double.parseDouble(fees);
		}

		String activityCity = request.getParameter("hd_activityCity");
		String activityApt = request.getParameter("hd_activityApt");
		String activityStreet = request.getParameter("hd_activityStreet");
		String activityState = request.getParameter("hd_activityState");
		String activityPic_small = request.getParameter("hd_file_url_small");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPic = request.getParameter("hd_file_url_middle");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPic_big = request.getParameter("hd_file_url_big");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityClassified = request
				.getParameter("hd_activityClassified");
		Date recordDate = new Date();

		activity.setActivityCity(activityCity);
		activity.setActivityApt(activityApt);
		activity.setActivityStreet(activityStreet);
		activity.setActivityDetail(activityDetailFinal.toString());

		activity.setActivityFeesFrom(activityFeesFrom);
		activity.setActivityFeesTo(activityFeesTo);
		activity.setClassifiedId(Integer.parseInt(activityClassified));
		activity.setActivityFinishTime(activityFinishTime);

		activity.setActivityName(activityName);

		activity.setActivityPic(activityPic);
		activity.setActivityPic_big(activityPic_big);
		activity.setActivityPic_small(activityPic_small);

		activity.setActivityState(activityState);
		activity.setActivityStartTime(activityStartTime);

		activity.setClassifiedId(classifiedId);
		activity.setRecordDate(recordDate);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			userId = 1;
		} else {
			userId = user.getUserId();
		}

		activity.setUserId(userId);
		activity.setRecordDate(recordDate);

		if (activityDao.updateActivity(activity)) {// 厥壅趙魂雄勤砓

			return "redirect:info.action?activityId=" + activityId;

		} else {
			return "activity/error";
		}

	}

	@RequestMapping("activityCreat")
	// 斐膘魂雄
	public String creatActivity(HttpServletRequest request,
			HttpServletResponse response) {

		String activityName = request.getParameter("hd_activityName");
		Date activityStartTime = null;
		Date activityFinishTime = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {

			activityStartTime = dateFormat.parse(request
					.getParameter("hd_activitystartDate"));
			activityFinishTime = dateFormat.parse(request
					.getParameter("hd_activityfinishDate"));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer classifiedId;
		if (request.getParameter("hd_activityClassified") == null) {
			classifiedId = 6;
		} else {
			classifiedId = Integer.parseInt(request
					.getParameter("hd_activityClassified"));
		}

		String activityDetail = request.getParameter("hd_activityDetail");
		StringBuffer activityDetailFinal = new StringBuffer("");
		char[] activityDetailTemp = activityDetail.toCharArray();
		for (int i = 0; i < activityDetail.length(); i++) {

			if (i % 93 == 0) {// 在106位置插入换行符
				activityDetailFinal.append("\n");

			}
			activityDetailFinal.append(activityDetailTemp[i]);

		}
		// System.out.println("!!!!!!"+activityDetail);
		Double activityFeesFrom = Double.parseDouble(request
				.getParameter("hd_activityFeesFrom"));
		Double activityFeesTo = null;
		if (request.getParameter("hd_activityFeesTo") != "") {
			activityFeesTo = Double.parseDouble(request
					.getParameter("hd_activityFeesTo"));
		}
		String activityCity = request.getParameter("hd_activityCity");

		String activityState = request.getParameter("hd_activityState");
		String activityStreet = request.getParameter("hd_activityStreet");
		// List<String> activityPic = new ArrayList(Arrays.asList(request
		// .getParameterValues("pic")));// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPic_small = request.getParameter("hd_file_url_small");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPic_big = request.getParameter("hd_file_url_big");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPic = request.getParameter("hd_file_url_middle");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityClassified = request
				.getParameter("hd_activityClassified");
		Date recordDate = new Date();

		Activity activity = new Activity();
		activity.setActivityCity(activityCity);

		activity.setActivityStreet(activityStreet);
		activity.setActivityDetail(activityDetailFinal.toString());

		activity.setActivityFeesFrom(activityFeesFrom);
		activity.setActivityFeesTo(activityFeesTo);
		activity.setClassifiedId(Integer.parseInt(activityClassified));
		activity.setActivityFinishTime(activityFinishTime);
		activity.setActivityState(activityState);
		activity.setActivityName(activityName);

		activity.setActivityPic(activityPic);
		activity.setActivityPic_big(activityPic_big);
		activity.setActivityPic_small(activityPic_small);

		activity.setActivityStartTime(activityStartTime);

		activity.setClassifiedId(classifiedId);
		activity.setRecordDate(recordDate);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			userId = 1;
		} else {
			userId = user.getUserId();
		}

		activity.setUserId(userId);
		activity.setRecordDate(recordDate);

		if (activityDao.addActivity(activity)) {// 厥壅趙魂雄勤砓
			String activityId = activityDao.getMaxId();
			ActivityFollow activityFollow = new ActivityFollow();
			activityFollow.setActivityId(Integer.parseInt(activityId));
			activityFollow.setUserId(userId);
			activityFollowDao.addActivityFollow(activityFollow);
			return "redirect:info.action?activityId=" + activityId;

		} else {
			return "activity/error";
		}

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@RequestMapping("index")
	// 肮傑翋珜
	public ModelAndView indexActivity(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		// HttpSession session =request.getSession();
		// User u=(User)session.getAttribute("user");
		// userId=u.getUserId();
		List<Activity> hottestActivityList = new ArrayList();
		hottestActivityList = activityDao.getHottestActivity();// �堤芢熱肮傑蹈桶(5跺芢熱僅植奻善狟甡棒崝湮)

		// �堤扂壽蛁腔肮傑蹈桶
		// Integer userId = Integer.parseInt(request.getParameter("userId"));//
		// 植request�堤扂腔userId
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");

		if (u == null) {
			// u= userDao.getUserById(3);
			// userId=3;
			// session.setAttribute("user", u);
		} else {
			userId = u.getUserId();
		}
		List<ActivityFollow> activityFollowList = new ArrayList();
		activityFollowList = activityFollowDao.getActivityFollowByCondition(0,
				userId);
		Iterator iterator1 = activityFollowList.iterator();
		List<Activity> myActivityList = new ArrayList<Activity>();// 斐膘扂腔魂雄蹈桶

		while (iterator1.hasNext()) {
			Integer activityId = ((ActivityFollow) iterator1.next())
					.getActivityId();
			myActivityList.add(activityDao.getActivityById(activityId));// 蔚陔腕善腔activity勤砓溫�扂壽蛁腔魂雄蹈桶爵醱;
		}// 腕善扂壽蛁腔肮傑蹈桶

		// 腕善疑衭壽蛁肮傑蹈桶

		List<User> friendList = new ArrayList();
		friendList = followDao.getAllFriends(userId);
		List<Activity> friendActivityList = new ArrayList();
		Iterator iterator2 = friendList.iterator();

		while (iterator2.hasNext()) {
			User user = (User) iterator2.next();
			List<ActivityFollow> activityFriendFollowList = new ArrayList();
			activityFriendFollowList = activityFollowDao
					.getActivityFollowByCondition(0, user.getUserId());// 鳳腕珨跺疑衭腔壽蛁魂雄勤砓蹈桶
			Iterator iteratorTemp = activityFriendFollowList.iterator();
			// List<Activity> activityListTemp = new ArrayList<Activity>();//
			// 斐膘珨跺疑衭魂雄蹈桶
			while (iteratorTemp.hasNext()) {
				Integer activityIdTemp = ((ActivityFollow) iteratorTemp.next())
						.getActivityId();
				Activity activityTemp = activityDao
						.getActivityById(activityIdTemp);// 腕善疑衭腔珨跺魂雄
				friendActivityList.add(activityTemp);// 蔚陔腕善腔activity勤砓溫�疑衭壽蛁腔魂雄蹈桶
			}

		}// 腕善疑衭壽蛁肮傑蹈桶friendsActivity

		// 腕善蚚誧郔輪腔絞堎魂雄
		List<ActivityFollow> latestActivity = new ArrayList();
		latestActivity = activityFollowDao.getActivityFollowByCondition(0, userId);
		//activityGoingDao.getActivityGoingByCondition(0, userId);// 腕善魂雄統迵桶勤砓
		List<Activity> activityList = new ArrayList();// 斐膘珨跺魂雄勤砓蹈桶
		Iterator iteratorActivityGoing = latestActivity.iterator();
		List<Activity> activityAfterFilter = new ArrayList();
		while (iteratorActivityGoing.hasNext()) {
			activityList.add(activityDao
					.getActivityById(((ActivityFollow) iteratorActivityGoing
							.next()).getActivityId()));
		}
		

		Comparator comp = new Activity();// 絳堤齬唗寞寀˙
		Collections.sort(activityList, comp);// 齬唗俇傖植苤善湮
		Date date = new Date();// 鳳腕絞ヶ奀潔be
		int year1=date.getYear();
		int month1=date.getMonth();
		int day1=date.getDate();

		for (int i = 0; i < activityList.size(); i++) {
	//		System.out.println(activityList.get(i).getActivityStartTime());
//			System.out.println(date);
			Date date2=activityList.get(i).getActivityStartTime();
			
			int year2=date2.getYear();
			int month2=date2.getMonth();
			int day2=date2.getDate();
			//System.out.println(year2+"!"+month2+"!"+day2);
			//System.out.println(year1+"!"+month1+"!"+day1);
			if (date.after(date2)) {// �彆絞ヶ奀潔俀衾絞ヶ魂雄奀潔
				// 寀泐徹

			} else {
				activityAfterFilter.add(activityList.get(i));// 蔚雛逋沭璃腔魂雄氝樓輛�賦彆List
			}
			
			
			if(year1==year2&&month1==month2&&day1==day2){
				
				activityAfterFilter.add(activityList.get(i));// 蔚雛逋沭璃腔魂雄氝樓輛�賦彆List
			}
		}// 祫森腕善賸絞ヶ蚚誧帤懂統樓activity腔listㄛ�綴樟哿瓚剿
		Calendar ca = Calendar.getInstance();
		int month = ca.get(Calendar.MONTH);// 鳳�堎爺
		List days = new ArrayList();// 斐膘絞ヶ堎爺帤統樓魂雄賦彆摩
		int sizee=activityAfterFilter.size()-1;
		if (activityAfterFilter.size() == 0
				|| activityAfterFilter.get(sizee).getActivityStartTime().getMonth() != month) {// �彆郔笝賦彆摩峈錨麼氪賦彆摩笢�睡珨跺啋匼飲祥婓絞堎

			view.addObject("latestActivity", null);// 絞堎拸魂雄
		} else {
			Iterator iterator = activityAfterFilter.iterator();// 梢盪む笢腔魂雄
			// 梑堤絞堎帤統樓腔
			Calendar c = Calendar.getInstance();
			int m = c.get(Calendar.MONTH);// �堤絞ヶ堎爺
			while (iterator.hasNext()) {
				Activity a = (Activity) iterator.next();
				Date startTime = a.getActivityStartTime();

				if (startTime.getMonth() == m) {// 梑堤絞堎帤統樓腔ㄛ眳垀眕涴欴岆秪峈褫夔森§森帤統樓魂雄眳蹈桶※爵衄珨虳岆眳綴堎爺腔魂雄
					days.add(a.getActivityStartTime().getDate());
				}

			}

			view.addObject("days", days);// 蔚掛堎帤統樓眳魂雄腔 �ぶㄗdayㄘ郪傖list溫�珜醱
			view.addObject("latestActivity", activityAfterFilter.get(sizee));
		}
		List activityGoingNumbersList = new ArrayList();
		activityGoingNumbersList = activityGoingDao.getActivityGoingAsNumbers();// �堤�跺統樓�杅郔嗣腔魂雄
		List activityGoingMaxList = new ArrayList();// 斐膘 volist
		Iterator iteratorGoing = activityGoingNumbersList.iterator();
			while (iteratorGoing.hasNext()) {
				ActivityGoingMax activityGoingMax = new ActivityGoingMax();
				ActivityGoingTempResult tempResult = (ActivityGoingTempResult) iteratorGoing
						.next();
				Activity a = activityDao.getActivityById(tempResult
						.getActivityId());
				activityGoingMax.setActivityId(a.getActivityId());// vo勤砓郪蚾羲宎
				activityGoingMax.setPicPath(a.getActivityPic());
				activityGoingMax.setNumbers(tempResult.getNumber());
				activityGoingMax.setActivityName(a.getActivityName());
				activityGoingMax.setUserName(userDao.getUserById(
						activityDao.getActivityById(tempResult.getActivityId())
								.getUserId()).getUserAlias());
				/**
				 * Modified by Chunting
				 * Date: 2013-03-18
				 * Change the parameter type of of serActibityStartTime from Date to String
				 * Because in the JSP page, the original date type is 2012-08-07 00:00:00
				 * After modified, we can use js to get substring 2012-08-07    
				 */
				activityGoingMax.setActivityStartTime(DateToString.convertDateToString(activityDao
						.getActivityById(tempResult.getActivityId())
						.getActivityStartTime()));// vo勤砓郪蚾俇傖
				
				activityGoingMaxList.add(activityGoingMax);// vo勤砓氝樓list
			}
//		}
		// 腕善魂雄郔嗣腔3跺傑庈
		List commentListRandom = new ArrayList();// 呴儂ぜ蹦桶
		List<ActivityComment> activityCommentList = new ArrayList();
		activityCommentList = activityCommentDao.getActivityCommentRandom();// 呴撈�堤珨跺ぜ蹦list
		Iterator commentIterator = activityCommentList.iterator();
		while (commentIterator.hasNext()) {
			ActivityComment activityComment = (ActivityComment) commentIterator
					.next();
			List<ActivityComment> ActivityCommentSonList = new ArrayList();
			ActivityCommentSonList = activityCommentDao
					.getActivityCommentByCondition(activityComment
							.getActivityId(), 0);// 勤議珨沭魂雄梑堤垀衄森魂雄腔ぜ蹦

			Iterator aCommentsIterator = ActivityCommentSonList.iterator();

			List<UserComments> comments = new ArrayList();
			while (aCommentsIterator.hasNext()) {
				ActivityComment aComment = (ActivityComment) aCommentsIterator
						.next();
				UserComments userComments = new UserComments();
				userComments.setUserComment(aComment.getCommentContent());
				userComments.setUserId(aComment.getUserId());
				User uu = userDao.getUserById(aComment.getUserId());
				userComments.setUserPic(uu.getUserPic());
				comments.add(userComments);// 郪蚾疑ぜ蹦List
			}
			FriendComment friendComment = new FriendComment();
			Activity a = activityDao.getActivityById(activityComment
					.getActivityId());// 梑堤涴跺ぜ蹦暮翹勤茼腔魂雄
			friendComment.setActivity(a);
			if (comments.size() > 4) {
				comments = comments.subList(0, 4);
			}
			String brief=a.getActivityDetail().split("\\.")[0]+ "...";
			brief=brief.replace('"', ' ');
			friendComment.setBrief(brief);

			String wish = "";
			if (wishListDao.isAddResource("city", a.getActivityId(), userId)) {
				wish = "Already Added";
				friendComment.setWish(wish);
			}else{
				wish = "+ Track List";
				friendComment.setWish(wish);
				
			}

			friendComment.setUserComment(comments);
			commentListRandom.add(friendComment);// 氝樓勤砓,樓輛list
		}

		List activityTagsList = new ArrayList();
		activityTagsList = activityClassifiedDao.getAllActivityClassified();
		if (activityGoingMaxList.size() > 4) {

			activityGoingMaxList = activityGoingMaxList.subList(0, 4);// �彆湮衾4跺
			// 饒繫硐諍�4跺
		}

		List<FriendComment> commentListRandom1 = new ArrayList();
		// 腎翹袨怓狟ㄛ蚰�疑衭統樓徹腔魂雄
		if (u != null) {

			// 植follow桶爵鏽堤followee (User勤砓)
			List followeeUser = followDao.getAllFriends(u.getUserId());
			Iterator followeeUserIterator = followeeUser.iterator();

			while (followeeUserIterator.hasNext()) {
				User fu = (User) followeeUserIterator.next();
				List<ActivityFollow> activityFollowListNew = new ArrayList();
				activityFollowListNew = activityFollowDao
						.getActivityFollowByCondition(0, fu.getUserId());
				Iterator activityFollowListNewIterator = activityFollowListNew
						.iterator();
				while (activityFollowListNewIterator.hasNext()) {
					List<UserComments> comments = new ArrayList();
					ActivityFollow activityFollow = (ActivityFollow) activityFollowListNewIterator
							.next();

					// 鏽堤珨跺user勤砓腔統樓腔珨跺魂雄
					// �activitiyGoingㄛ脤揭勤茼腔魂雄Id
					// 籵徹魂雄ID�魂雄蹈桶爵醱脤堤勤茼腔魂雄
					Activity a = activityDao.getActivityById(activityFollow
							.getActivityId());
					// 脤堤涴跺魂雄腔垀衄ぜ蹦勤砓+ぜ蹦�勤砓
					List aComments = new ArrayList();
					aComments = activityCommentDao
							.getActivityCommentByCondition(a.getActivityId(), 0);
					Iterator aCommentsIterator = aComments.iterator();
					while (aCommentsIterator.hasNext()) {
						ActivityComment aComment = (ActivityComment) aCommentsIterator
								.next();
						UserComments userComments = new UserComments();
						userComments.setUserComment(aComment
								.getCommentContent());
						userComments.setUserId(aComment.getUserId());
						userComments.setUserPic(userDao.getUserById(
								aComment.getUserId()).getUserPic());
						comments.add(userComments);// 郪蚾疑ぜ蹦List

					}
					FriendComment friendComment = new FriendComment();
					friendComment.setActivity(a);
					if (comments.size() > 4) {
						comments = comments.subList(0, 4);
					}
					String brief=a.getActivityDetail().split("\\.")[0]+ "...";
					brief=brief.replace('"', ' ');
					friendComment.setBrief(brief);
					
					String wish = "";
					if (wishListDao.isAddResource("city", a.getActivityId(), userId)) {
						wish = "ok";
						friendComment.setWish("Already Added");
					}else{
						wish="+ Track List";
						friendComment.setWish(wish);
						
					}
					friendComment.setUserComment(comments);
					commentListRandom1.add(friendComment);// 氝樓勤砓,樓輛list
				}

			}
			if (commentListRandom1.size() > 5) {
				commentListRandom1 = commentListRandom1.subList(0, 5);
			}
		}
		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Integer flag = 0;

		User currentUser = (User) session.getAttribute("user");// 腕善絞ヶ蚚誧勤砓
		if (currentUser != null) {
			flag = 1;
		}
		view.addObject("flag", flag);
		List topTribusCity = new ArrayList();
		topTribusCity = activityDao.getTopTribusCity();
		view.addObject("activityTagsList", activityTagsList);// 蔚垀衄梓ワlist溫��ん
		view.addObject("topTribusCity", topTribusCity);// 溫�topTribusCity
		view.addObject("myActivityList", myActivityList);// 溫�扂腔肮傑
		view.addObject("hottestActivityList", hottestActivityList);// 溫�芢熱肮傑
		if (hottestActivityList.size() != 0) {
			view.addObject("hottestActivity", hottestActivityList.get(0));// 溫�芢熱肮傑
		}
		view.addObject("friendActivityList", friendActivityList);// 溫�疑衭肮傑

		view.addObject("activityGoingMaxList", activityGoingMaxList);// 蔚郔嗣統樓�杅魂雄蹈桶溫��ん換隙珜醱
		if (commentListRandom1.size() == 0) {
			int size = 1;
			view.addObject("commentListRandom", commentListRandom);// 蔚呴儂ぜ蹦蹈桶溫��ん換隙珜醱
			view.addObject("size", size);// 蔚梓祩溫��ん換隙珜醱
		} else {
			view.addObject("commentListRandom", commentListRandom1);// 蔚疑衭呴儂ぜ蹦蹈桶溫��ん換隙珜醱
		}
		view.addObject("user", u);// 蔚session爵醱腔user勤砓換善珜醱

		if (u != null) {

			List<TribusClassify> tribuClassify = new ArrayList();
			tribuClassify = tribusListClassifyDao.getTribusListClassByUserId(u
					.getUserId());
			view.addObject("tribusClassify", tribuClassify);
			System.out.println(tribuClassify.size());
		}

		view.setViewName("activity/new_index");// 扢离弝芞華硊峈httpㄩ//www.tribus.com/view/activity/index.jsp
		return view;// 殿隙弝芞

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("info")
	// 肮傑砆牉翋珜
	public ModelAndView activityInfo(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		try {
			Integer activityId = Integer.parseInt(request
					.getParameter("activityId"));// 植request�堤魂雄activityId
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if (user == null) {
				// userId = 123;
			} else {
				userId = user.getUserId();
			}
			Activity activity = activityDao.getActivityById(activityId);// 腕善森魂雄腔勤砓

			List<ActivityGoing> activityGoingList = new ArrayList();
			activityGoingList = activityGoingDao.getActivityGoingByCondition(
					activityId, 0);// 腕善森魂雄垀衄ActivityGoing勤砓
			Iterator iterator = activityGoingList.iterator();
			List<User> userList = new ArrayList();// 斐膘垀衄going森魂雄腔userList
			while (iterator.hasNext()) {
				ActivityGoing activityGoing = (ActivityGoing) iterator.next();
				userList.add(userDao.getUserById(activityGoing.getUserId()));// 參f統樓森魂雄腔user氝樓善list
			}

			List<User> friends = new ArrayList();
			friends = followDao.getAllFriends(userId);// 腕善森userId垀衄followee勤砓

			List<User> friendFollow = new ArrayList<User>(); // 斐膘諾�ん"疑衭統樓魂雄list"

			for (User user1 : friends) {
				if (userList.contains(user1)) {
					friendFollow.add(user1);
				}
			}

			List<ActivityAlbum> activityAlbumList = new ArrayList();
			activityAlbumList = activityAlbumDao.getActivityAlbumByCondition(
					activityId, 0);// 跦擂珨跺魂雄腔id腕善眈聊list
			Integer flag = 0;
			if (activityAlbumList.size() > 10) {// �祥雛10寀硃ょ�
				activityAlbumList = activityAlbumList.subList(0, 10);
				flag = 1;
			}
			GetLatLong getLatLong = new GetLatLong();
			String[] location = getLatLong.getLatlng(activity
					.getActivityStreet()
					+ "," + activity.getActivityCity());

			List followList = new ArrayList();
			followList = activityFollowDao.getActivityFollowByCondition(
					activityId, userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄
			String followed = null;
			String joined = null;
			String owner = null;

			String wish = null;
			String tribus = null;

			if (tribusListDao.isAddResource("city", activityId, userId)) {

				tribus = "ok";
				view.addObject("tribus", tribus);// 蔚tribus 硉溫��ん換隙珜醱
			}
			if (wishListDao.isAddResource("city", activityId, userId)) {
				wish = "ok";
				view.addObject("wish", wish);// 蔚wish硉溫��ん換隙珜醱
			}

			if (userId == activity.getUserId()) {// 瓚剿絞ヶ蚚誧岆瘁岆森魂雄腔楷れ氪
				owner = "true";

			} else {
				if (followList.size() > 0) {
					followed = "true";// 桶尨眒壽蛁
				} else {
					followed = "false";// 桶尨帤壽蛁
				}
				List goingList = new ArrayList();
				goingList = activityGoingDao.getActivityGoingByCondition(
						activityId, userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄

				if (goingList.size() > 0) {
					joined = "true";// 桶尨眒join
				} else {
					joined = "false";// 桶尨帤join
				}
			}
			List commentList = new ArrayList();
			commentList = activityCommentDao.getActivityCommentByCondition(
					activityId, 0);// ぜ蹦dao
			List userCommentList = new ArrayList();
			Iterator iteratorComment = commentList.iterator();
			while (iteratorComment.hasNext()) {
				ActivityComment activityComment = (ActivityComment) iteratorComment
						.next();
				UserComment userComment = new UserComment();// VO勤砓猾蚾羲宎
				userComment.setCommentContent(activityComment
						.getCommentContent());
				userComment.setCommentDate(activityComment.getCommentDate());
				userComment.setUserName(userDao.getUserById(
						activityComment.getUserId()).getUserAlias());
				userComment.setUserPic(userDao.getUserById(
						activityComment.getUserId()).getUserPic());// VO勤砓猾蚾賦旰
				userComment.setUserId(activityComment.getUserId());
				userCommentList.add(userComment);
			}
			String activtiyClassified = activityClassifiedDao
					.getClassifiedTagById(activity.getClassifiedId());

			int page;

			if (request.getParameter("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
			} // 睿 珨跺ぜ蹦list

			view.addObject("current", page);// 溫�絞ヶ珜醱唗杅

			List result = new ArrayList();
			result = getListByPage(userCommentList, 4, page);// 4沭珨珜 腕善菴page珜ㄐ

			int pageNumbers = getPageNumber(userCommentList, 4);// 4沭珨珜 ㄛ腕善珨僕撓珜
			String[] pages = new String[pageNumbers];
			for (Integer i = 0; i < pageNumbers; i++) {
				pages[i] = i + 1 + "";
			}

			view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱

			Integer flagg = 0;
			User establishUser = userDao.getUserById(activity.getUserId());// 腕善桽え奻換氪勤砓

			User currentUser = (User) session.getAttribute("user");// 腕善絞ヶ蚚誧勤砓
			if (currentUser != null) {
				if (currentUser.getUserId() == establishUser.getUserId())// 佽隴絞ヶ蚚誧憩岆森魂雄楷れ蚚誧
				// 垀眕衄芞え奻換腔�癹
				{
					flagg = 1;

				}
			}
			UserProfile userProfile = (UserProfile) session
					.getAttribute("userProf");
			view.addObject("userProf", userProfile);
			User u = userDao.getUserById(activity.getUserId());// s梑堤蚚誧楷れ氪

			view.addObject("flagg", flagg);
			List recommentActivity = new ArrayList();
			recommentActivity = activityDao.getAllActivity();// �芢熱魂雄
			if (recommentActivity.size() >= 10) {
				recommentActivity = recommentActivity.subList(7, 10);// �芢熱魂雄
			}
			// //陓眊枑倳
			try {
				List unreadInboxmails = messageDao
						.getUserInboxMessageAllUnread(userId);
				view.addObject("unreadMail",
						unreadInboxmails != null ? unreadInboxmails.size() : 0);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<TribusClassify> tribuClassify = new ArrayList();
			tribuClassify = tribusListClassifyDao.getTribusListClassByUserId(u
					.getUserId());
			view.addObject("tribusClassify", tribuClassify);
			System.out.println(tribuClassify.size());
			view.addObject("maxNumber", pages.length);
			view.addObject("activityAlbum", activityAlbumList);// 蔚眈聊list溫��んㄛ換隙珜醱
			view.addObject("flag", flag);// �眈聊listsize湮衾10 森硉峈1
			// 毀眳寀峈0蔚む溫��んㄛ換隙珜醱
			view.addObject("activityClassified", activtiyClassified);// 蔚activtiyClassified溫��ん
			// 換隙珜醱
			view.addObject("user", u);
			view.addObject("activityLat", location[2]);// 蔚華硊帠僅溫��んㄛ換隙珜醱
			view.addObject("activityLong", location[3]);// 蔚華硊冪僅溫��んㄛ換隙珜醱
			System.out.println(location[2] + "," + location[3]);
			view.addObject("activityInfo", activity);// 蔚activity溫��ん 換隙珜醱
			view.addObject("followList", friendFollow);// 蔚followList溫��ん 換隙珜醱
			view.addObject("followed", followed);// 蔚followed袨怓溫��ん換隙珜醱
			view.addObject("joined", joined);// 蔚joined袨怓溫��ん換隙珜醱
			view.addObject("owner", owner);// 蔚owner袨怓溫��ん換隙珜醱
			view.addObject("userList", userList);// 蔚垀衄統樓森魂雄腔蚚誧溫��んㄛ換隙珜醱
			view.addObject("userCommentList", result);// 蔚隱晟VO勤砓溫��ん換隙珜醱
			view.addObject("recommentActivity", recommentActivity);// 蔚芢熱魂雄list溫��ん換隙珜醱
			view.addObject("activityId", activityId);// 蔚activityId袨怓溫��ん換隙珜醱

			if (activity.getClassifiedId() == 3) {
				view.setViewName("activity/market");
			} else {
				view.setViewName("activity/activity");
			}

			return view;
		} catch (Exception e) {
			e.printStackTrace();
			view.setViewName("activity/error");
			return view;
		}
	}// 殿隙弝芞

	@RequestMapping("friendsActivity")
	// 疑衭魂雄翋珜
	public ModelAndView friendsActivity(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			// userId = 123;
		} else {
			userId = user.getUserId();
		}
		List<Activity> activityListByFriend = new ArrayList();
		List<User> friends = new ArrayList();
		friends = followDao.getAllFriends(userId);// 腕善森userId垀衄followee勤砓
		Iterator iterator = friends.iterator();
		while (iterator.hasNext()) {
			User u = (User) iterator.next();
			List<ActivityGoing> activityGoingList = new ArrayList();
			activityGoingList = activityGoingDao.getActivityGoingByCondition(0,
					u.getUserId());// 腕善議珨跺疑衭腔垀衄統樓腔魂雄List
			Iterator<ActivityGoing> activityGoingListIterator = activityGoingList
					.iterator();
			while (activityGoingListIterator.hasNext()) {// 梢盪議跺疑衭統樓腔垀衄魂雄goingList
				ActivityGoing activityGoing = activityGoingListIterator.next();// 腕善議珨疑衭統樓腔腔議珨跺魂雄going勤砓
				Activity activity = activityDao.getActivityById(activityGoing
						.getActivityId());// 腕善議珨疑衭統樓腔腔議珨跺activity勤砓
				activityListByFriend.add(activity);// 氝樓善賦彆摩笢
			}
		}

		String followed = null;
		String joined = null;
		String owner = null;

		Integer page;
		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
		}

		List<SuperActivity> superActivityList = new ArrayList();// vo 勤砓
		// 郔笝換隙珜醱腔list
		Iterator activityListByFriendIterator = activityListByFriend.iterator();
		while (activityListByFriendIterator.hasNext()) {

			Activity a = (Activity) activityListByFriendIterator.next();
			List followList = new ArrayList();
			followList = activityFollowDao.getActivityFollowByCondition(a
					.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄

			if (userId == a.getUserId()) {// 瓚剿絞ヶ蚚誧岆瘁岆森魂雄腔楷れ氪
				owner = "true";
			} else {
				if (followList.size() > 0) {
					followed = "true";// 桶尨眒壽蛁
				} else {
					followed = "false";// 桶尨帤壽蛁
				}
				List goingList = new ArrayList();
				goingList = activityGoingDao.getActivityGoingByCondition(a
						.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁統樓森魂雄

				if (goingList.size() > 0) {
					joined = "true";// 桶尨眒join
				} else {
					joined = "false";// 桶尨帤join
				}
			}
			SuperActivity s = new SuperActivity();
			s.setActivity(a);
			s.setFollowed(followed);
			s.setJoined(joined);
			s.setOwner(owner);
			superActivityList.add(s);// 郔笝腔賦彆摩ㄗむ妗遜剒猁酕珨祭煦珜揭燴ㄘ換隙珜醱
		}
		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List recommentActivity = new ArrayList();
		recommentActivity = activityDao.getAllActivity();// �芢熱魂雄
		recommentActivity = recommentActivity.subList(7, 10);// �芢熱魂雄
		List result = new ArrayList();// 郔笝腔賦彆摩
		String[] pages = null;
		if (superActivityList.size() == 0) {// �彆賦彆摩峈諾腔奀緊

		} else {
			result = getListByPage(superActivityList, 4, page);// 4沭珨珜 腕善菴page珜ㄐ

			int pageNumbers = getPageNumber(superActivityList, 4);// 4沭珨珜
			// ㄛ腕善珨僕撓珜
			pages = new String[pageNumbers];
			for (Integer i = 0; i < pageNumbers; i++) {
				pages[i] = i + 1 + "";
			}
		}
		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserProfile userProfile = (UserProfile) session
				.getAttribute("userProf");
		view.addObject("userProf", userProfile);
		List topTribusCity = new ArrayList();
		topTribusCity = activityDao.getTopTribusCity();
		view.addObject("topTribusCity", topTribusCity);// top tribus
		// city溫��ん換隙珜醱
		view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
		view.addObject("activityList", result);// 蔚腕善腔賦彆ㄗ硌隅珜鎢弇离ㄘ溫��ん 換隙珜醱ㄐ
		view.addObject("followed", followed);// 蔚followed袨怓溫��ん換隙珜醱
		view.addObject("joined", joined);// 蔚joined袨怓溫��ん換隙珜醱
		view.addObject("owner", owner);// 蔚owner袨怓溫��ん換隙珜醱
		view.addObject("recommentActivity", recommentActivity);// 蔚芢熱魂雄list溫��ん換隙珜醱

		view.setViewName("activity/friends_activity");
		return view;
	}

	@RequestMapping("activityAlbum/{activityAlbumId}")
	// 湖羲珨跺眈聊
	public ModelAndView activityAlbumPic(
			@PathVariable("activityAlbumId") Integer activityAlbumId) {
		ModelAndView view = new ModelAndView();
		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ActivityPic> activityPicList = new ArrayList();
		activityPicList = activityPicDao.getActivityPicByCondition(0,
				activityAlbumId);// 籵徹議珨眈聊id鳳腕森眈聊狟腔垀衄picList;
		view.setViewName("activity/pic");// 扢离勤茼腔弝芞view/activity/pic.jsp
		view.addObject("activityPic", activityPicList);// 蔚眈えlist溫��んㄛ換隙珜醱
		return view;// 殿隙弝芞
	}

	@RequestMapping("getRecommends")
	public ModelAndView getRecommends(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		String condition = request.getParameter("searchCondition");// 植刲坰遺爵�堤沭璃
		Integer page;

		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
		}
		String followed = null;
		String joined = null;
		String owner = null;
		List<Activity> activityList = new ArrayList();
		activityList = activityDao.getAllActivity();
		List<SuperActivity> superActivityList = new ArrayList();// 郔笝換隙珜醱腔list
		Iterator activityListIterator = activityList.iterator();
		String wish = null;
		String tribus = null;
		HttpSession session1 = request.getSession();
		User user = (User) session1.getAttribute("user");
		while (activityListIterator.hasNext()) {
			Activity a = (Activity) activityListIterator.next();

			if (user == null) {
				// userId = 123;
			} else {
				userId = user.getUserId();
			}
			List followList = new ArrayList();
			followList = activityFollowDao.getActivityFollowByCondition(a
					.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄

			if (userId == a.getUserId()) {// 瓚剿絞ヶ蚚誧岆瘁岆森魂雄腔楷れ氪
				owner = "true";
			} else {
				if (followList.size() > 0) {
					followed = "true";// 桶尨眒壽蛁
				} else {
					followed = "false";// 桶尨帤壽蛁
				}
				List goingList = new ArrayList();
				goingList = activityGoingDao.getActivityGoingByCondition(a
						.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤絞ヶ蚚誧岆瘁統樓森魂雄

				if (goingList.size() > 0) {
					joined = "true";// 桶尨眒join
				} else {
					joined = "false";// 桶尨帤join
				}
			}

			if (tribusListDao.isAddResource("city", a.getActivityId(), userId)) {

				tribus = "ok";
			} else {
				tribus = "+ Tribus List";
			}
			if (wishListDao.isAddResource("city", a.getActivityId(), userId)) {
				wish = "ok";
			} else {
				wish = "+ Track List";
			}
			SuperActivity s = new SuperActivity();
			s.setWish(wish);
			s.setTribus(tribus);
			s.setActivity(a);
			s.setFollowed(followed);
			s.setJoined(joined);
			s.setOwner(owner);
			superActivityList.add(s);
		}
		List result = new ArrayList();// 郔笝腔賦彆摩
		String[] pages = null;
		if (superActivityList.size() == 0) {// �彆賦彆摩峈諾腔奀緊

		} else {
			result = getListByPage(superActivityList, 4, page);// 4沭珨珜 腕善菴page珜ㄐ

			int pageNumbers = getPageNumber(superActivityList, 4);// 4沭珨珜
			// ㄛ腕善珨僕撓珜
			pages = new String[pageNumbers];
			for (Integer i = 0; i < pageNumbers; i++) {
				pages[i] = i + 1 + "";
			}
		}// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserProfile userProfile = (UserProfile) session1
				.getAttribute("userProf");
		if (user != null) {

			List<TribusClassify> tribuClassify = new ArrayList();
			tribuClassify = tribusListClassifyDao
					.getTribusListClassByUserId(user.getUserId());
			view.addObject("tribusClassify", tribuClassify);

		}
		view.addObject("userProf", userProfile);
		view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
		view.addObject("activityList", result);// 蔚腕善腔賦彆ㄗ硌隅珜鎢弇离ㄘ溫��ん 換隙珜醱ㄐ
		view.addObject("condition", condition);// 蔚刲坰沭璃溫��ん換隙珜醱 隱釬煦珜眳蚚
		view.addObject("followed", followed);// 蔚followed袨怓溫��ん換隙珜醱
		view.addObject("joined", joined);// 蔚joined袨怓溫��ん換隙珜醱
		view.addObject("owner", owner);// 蔚owner袨怓溫��ん換隙珜醱
		view.setViewName("activity/new_search_result");// 扢离勤茼腔弝芞view/activity/view.jsp
		return view;
	}

	@RequestMapping("search")
	// 刲坰遺
	public ModelAndView searchActivity(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		String condition = request.getParameter("searchCondition");// 植刲坰遺爵�堤沭璃
		Integer page;

		if (request.getParameter("page") == null) {
			page = 1;

		} else {
			page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
		}
		view.addObject("current", page);// 溫�絞ヶ珜醱唗杅

		String followed = null;

		HttpSession session1 = request.getSession();
		User user = (User) session1.getAttribute("user");
		if (user == null) {
			// userId = 0;
		} else {
			userId = user.getUserId();
		}
		List<Activity> activityList = new ArrayList();
		activityList = activityDao.getActivityByAbstractCondition(condition);
		List<SuperActivity> superActivityList = new ArrayList();// 郔笝換隙珜醱腔list
		Iterator activityListIterator = activityList.iterator();
		while (activityListIterator.hasNext()) {
			Activity a = (Activity) activityListIterator.next();
			String joined = null;
			String owner = null;
			String tribus = null;
			String wish = null;

			List followList = new ArrayList();
			followList = activityFollowDao.getActivityFollowByCondition(a
					.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄

			if (userId == a.getUserId()) {// 瓚剿絞ヶ蚚誧岆瘁岆森魂雄腔楷れ氪
				owner = "true";
			} else {
				if (followList.size() > 0) {
					followed = "true";// 桶尨眒壽蛁
				} else {
					followed = "false";// 桶尨帤壽蛁
				}
				List goingList = new ArrayList();
				goingList = activityGoingDao.getActivityGoingByCondition(a
						.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤絞ヶ蚚誧岆瘁統樓森魂雄

				if (goingList.size() > 0) {
					joined = "true";// 桶尨眒join
				} else {
					joined = "false";// 桶尨帤join
				}
			}
			if (tribusListDao.isAddResource("city", a.getActivityId(), userId)) {

				tribus = "ok";
			} else {
				tribus = "+ Tribus List";
			}
			if (wishListDao.isAddResource("city", a.getActivityId(), userId)) {
				wish = "ok";
			} else {
				wish = "+ Track List";
			}
			SuperActivity s = new SuperActivity();
			s.setWish(wish);
			s.setTribus(tribus);

			s.setActivity(a);
			s.setFollowed(followed);
			s.setJoined(joined);
			s.setOwner(owner);
			superActivityList.add(s);
		}
		List result = new ArrayList();// 郔笝腔賦彆摩
		String[] pages = null;
		if (superActivityList.size() == 0) {// �彆賦彆摩峈諾腔奀緊
			view.addObject("resultSize", 0);
		} else {
			result = getListByPage(superActivityList, 4, page);// 4沭珨珜 腕善菴page珜ㄐ

			int pageNumbers = getPageNumber(superActivityList, 4);// 4沭珨珜
			// ㄛ腕善珨僕撓珜
			pages = new String[pageNumbers];
			for (Integer i = 0; i < pageNumbers; i++) {
				pages[i] = i + 1 + "";
			}
		}
		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserProfile userProfile = (UserProfile) session1
				.getAttribute("userProf");
		view.addObject("userProf", userProfile);
		view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱

		if (pages != null) {
			view.addObject("maxPage", pages.length);
		}

		view.addObject("activityList", result);// 蔚腕善腔賦彆ㄗ硌隅珜鎢弇离ㄘ溫��ん 換隙珜醱ㄐ
		view.addObject("condition", condition);// 蔚刲坰沭璃溫��ん換隙珜醱 隱釬煦珜眳蚚
		view.addObject("followed", followed);// 蔚followed袨怓溫��ん換隙珜醱

		view.setViewName("activity/new_search_result");// 扢离勤茼腔弝芞view/activity/view.jsp
		return view;
	}

	
	
	
	
	
	@RequestMapping("searchByDay")
	// �ぶ刲坰ㄗ渀勤赻撩腔饒跺�盪沭ㄘ
	public ModelAndView searchByDate(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();

		Integer page;

		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
		}
		view.addObject("current", page);// 溫�絞ヶ珜醱唗杅
		String d = request.getParameter("day");
		String m = request.getParameter("month");
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");
		if (u == null) {
			// userId = 0;
		} else {
			userId = u.getUserId();
		}

		List<SuperActivity> superActivityList = new ArrayList();// 郔笝換隙珜醱腔list
		List activityBeforeFilter = new ArrayList();
		List activityAfterFilter = new ArrayList();// 帤懂腔絞堎腔魂雄賦彆摩 祥徹羶衄輛俴煦珜
		if (d != null && m != null) {
			Integer day = Integer.parseInt(d);
			Integer month = Integer.parseInt(m) - 1;// 植錨羲宎數呾
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR) - 1900;//
			if (u != null) {
//				List<ActivityGoing> activityGoingList = activityGoingDao
//						.getActivityGoingByDate(u.getUserId());// 植魂雄統樓氪桶爵�堤
				List<ActivityFollow> activityGoingList = activityFollowDao.getActivityFollowByCondition(0, u.getUserId());
				// 蚚誧統樓腔垀衄魂雄list
				Iterator iterator = activityGoingList.iterator();
				while (iterator.hasNext()) {
					ActivityFollow activityGoing = (ActivityFollow) iterator
							.next();
					Activity activity = activityDao
							.getActivityById(activityGoing.getActivityId());// 梑善勤茼腔魂雄勤砓
					activityBeforeFilter.add(activity);// 珋婓蔚絞ヶ蚚誧垀衄腔魂雄 統樓腔帤統樓腔
					// ,踏岍腔奻捲赽腔飲坻鎔婓爵醱賸
				}
				Iterator iterator2 = activityBeforeFilter.iterator();
				while (iterator2.hasNext()) {
					Activity activity = (Activity) iterator2.next();

					String followed = null;
					String joined = null;
					String owner = null;
					String wish = null;
					String tribus = null;

					if (activity.getActivityStartTime().getDate() == day
							&& activity.getActivityStartTime().getMonth() == month
							&& activity.getActivityStartTime().getYear() == year) {// 梑堤硌隅珨毞腔魂雄

						List followList = new ArrayList();
						followList = activityFollowDao
								.getActivityFollowByCondition(activity
										.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄

						if (userId == activity.getUserId()) {// 瓚剿絞ヶ蚚誧岆瘁岆森魂雄腔楷れ氪
							owner = "true";
						} else {
							if (followList.size() > 0) {
								followed = "true";// 桶尨眒壽蛁
							} else {
								followed = "false";// 桶尨帤壽蛁
							}
							List goingList = new ArrayList();
							goingList = activityGoingDao
									.getActivityGoingByCondition(activity
											.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤絞ヶ蚚誧岆瘁統樓森魂雄

							if (goingList.size() > 0) {
								joined = "true";// 桶尨眒join
							} else {
								joined = "false";// 桶尨帤join
							}
						}
						if (tribusListDao.isAddResource("city", activity
								.getActivityId(), userId)) {

							tribus = "ok";
						} else {
							tribus = "+ Tribus List";
						}
						if (wishListDao.isAddResource("city", activity
								.getActivityId(), userId)) {
							wish = "ok";
						} else {
							wish = "+ Track List";
						}
						SuperActivity s = new SuperActivity();
						s.setWish(wish);
						s.setTribus(tribus);

						s.setActivity(activity);
						s.setFollowed(followed);
						s.setJoined(joined);
						s.setOwner(owner);
						superActivityList.add(s);

					}
				}

			}
		}

		List result = new ArrayList();// 郔笝腔賦彆摩
		String[] pages = null;
		if (superActivityList.size() == 0) {// �彆賦彆摩峈諾腔奀緊
			view.addObject("resultSize", 0);

		} else {
			result = getListByPage(superActivityList, 5, page);// 5沭珨珜 腕善菴page珜ㄐ

			int pageNumbers = getPageNumber(superActivityList, 5);// 5沭珨珜
			// ㄛ腕善珨僕撓珜
			pages = new String[pageNumbers];
			for (Integer i = 0; i < pageNumbers; i++) {
				pages[i] = i + 1 + "";
			}
		}
		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserProfile userProfile = (UserProfile) session
				.getAttribute("userProf");
		if (u != null) {

			List<TribusClassify> tribuClassify = new ArrayList();
			tribuClassify = tribusListClassifyDao.getTribusListClassByUserId(u
					.getUserId());
			view.addObject("tribusClassify", tribuClassify);

		}
		if (pages != null) {
			view.addObject("maxPage", pages.length);
		}
		view.addObject("userProf", userProfile);
		view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
		view.addObject("activityList", result);// 蔚腕善腔賦彆ㄗ硌隅珜鎢弇离ㄘ溫��ん 換隙珜醱ㄐ
		view.addObject("day", d);// 蔚腕善腔賦彆ㄗ硌隅珜鎢弇离ㄘ溫��ん 換隙珜醱ㄐ
		view.addObject("month", m);// 蔚腕善腔賦彆ㄗ硌隅珜鎢弇离ㄘ溫��ん 換隙珜醱ㄐ
		view.setViewName("activity/search_result_date");// 扢离勤茼腔弝芞view/activity/view.jsp
		return view;
	}
	
	
	

	@RequestMapping("byCityTag")
	// 偌桽�藷傑庈梓ワ刲坰
	public ModelAndView searchActivityByCityTag(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		Integer page;
		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
		}

		view.addObject("current", page);// 溫�絞ヶ珜醱唗杅
		String activityCity = request.getParameter("city");// �堤tag梓ワ腔硉

		List<Activity> activityList = new ArrayList();
		activityList = activityDao.getActivityByCondition(null, activityCity,
				null, null);

		
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		List<SuperActivity> superActivityList = new ArrayList();// 郔笝換隙珜醱腔list
		Iterator activityListIterator = activityList.iterator();
		while (activityListIterator.hasNext()) {
			Activity a = (Activity) activityListIterator.next();

			if (user == null) {
				// userId = 13;
			} else {
				userId = user.getUserId();
			}
			List followList = new ArrayList();
			followList = activityFollowDao.getActivityFollowByCondition(a
					.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄
			String owner = null;
			String followed = null;
			String joined = null;
			if (userId == a.getUserId()) {// 瓚剿絞ヶ蚚誧岆瘁岆森魂雄腔楷れ氪
				owner = "true";
			} else {
				if (followList.size() > 0) {
					followed = "true";// 桶尨眒壽蛁
				} else {
					followed = "false";// 桶尨帤壽蛁
				}
				List goingList = new ArrayList();
				goingList = activityGoingDao.getActivityGoingByCondition(a
						.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄

				if (goingList.size() > 0) {
					joined = "true";// 桶尨眒join
				} else {
					joined = "false";// 桶尨帤join
				}
			}
			SuperActivity s = new SuperActivity();
			s.setActivity(a);
			s.setFollowed(followed);
			s.setJoined(joined);
			s.setOwner(owner);
			superActivityList.add(s);
		}

		List result = new ArrayList();// 郔笝腔賦彆摩

		result = getListByPage(superActivityList, 4, page);// 4沭珨珜 腕善菴page珜ㄐ
		if (superActivityList.size() == 0) {// �彆賦彆摩峈諾腔奀緊
			view.addObject("resultSize", 0);

		}
		int pageNumbers = getPageNumber(superActivityList, 4);// 4沭珨珜 ㄛ腕善珨僕撓珜
		String[] pages = new String[pageNumbers];
		for (Integer i = 0; i < pageNumbers; i++) {
			pages[i] = i + 1 + "";
		}

		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserProfile userProfile = (UserProfile) session
				.getAttribute("userProf");
		if (user != null) {

			List<TribusClassify> tribuClassify = new ArrayList();
			tribuClassify = tribusListClassifyDao
					.getTribusListClassByUserId(user.getUserId());
			view.addObject("tribusClassify", tribuClassify);

		}
		view.addObject("userProf", userProfile);
		view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
		if (pages != null) {
			view.addObject("maxPage", pages.length);
		}
		view.addObject("activityList", result);// 蔚腕善腔賦彆ㄗ硌隅珜鎢弇离ㄘ溫��ん 換隙珜醱ㄐ
		view.addObject("city", activityCity);// 蔚森奀腔刲坰沭璃溫隙珜醱ㄛ隱覂煦珜婬蚚
		//view.addObject("followed", followed);// 蔚followed袨怓溫��ん換隙珜醱
		//view.addObject("joined", joined);// 蔚joined袨怓溫��ん換隙珜醱
		//view.addObject("owner", owner);// 蔚owner袨怓溫��ん換隙珜醱
		view.setViewName("activity/new_search_result");// 扢离勤茼腔弝芞view/activity/view.jsp

		return view;
	}

	
	
	
	
	
	
	
	@RequestMapping("followActivity")
	// 壽蛁/�秏壽蛁森魂雄
	public void followUnFollowAction(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			HttpSession session = request.getSession(true);
			Integer activityId = Integer.parseInt(request
					.getParameter("activityId"));// �堤絞ヶ腔魂雄id

			User user = (User) session.getAttribute("user");
			if (user == null) {
				// userId = 123;
			} else {
				userId = user.getUserId();
			}

			List<ActivityFollow> followList = new ArrayList();
			followList = activityFollowDao.getActivityFollowByCondition(
					activityId, userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄

			if (followList.size() > 0) {// 森奀岆壽蛁腔袨怓ㄛ佽隴蚚誧砑硒俴賤壽蛁紱釬

				if (activityFollowDao.delActivityFollow(followList.get(0))) {
					response.getWriter().write("Follow");// 梓祩換隙珜醱ㄛ魂雄曹峈樓壽蛁袨怓
				}

			} else {// 森奀岆帤壽蛁腔袨怓ㄛ佽隴蚚誧砑硒俴樓壽蛁紱釬
				ActivityFollow activityFollow = new ActivityFollow();

				// 羲宎郪蚾魂雄壽蛁氪勤砓
				activityFollow.setActivityId(activityId);
				activityFollow.setUserId(userId);// 魂雄壽蛁氪勤砓郪蚾俇傖
				if (activityFollowDao.addActivityFollow(activityFollow)) {
					response.getWriter().write("unFollow");// 梓祩換隙珜醱ㄛ魂雄曹峈樓壽蛁袨怓

				} else {
					response.getWriter().write("fail to follow!sorry");// 梓祩換隙珜醱(樓壽蛁囮啖ㄘ
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// view.setViewName("activity/error");
		}
	}

	@RequestMapping("joinActivity")
	// 統樓/�秏統樓森魂雄
	public void joinUnjoinAction(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			HttpSession session = request.getSession(true);
			Integer activityId = Integer.parseInt(request
					.getParameter("activityId"));// �堤絞ヶ腔魂雄id

			User user = (User) session.getAttribute("user");
			if (user == null) {
				userId = 123;
			} else {
				userId = user.getUserId();
			}

			List<ActivityGoing> goingList = new ArrayList();
			goingList = activityGoingDao.getActivityGoingByCondition(
					activityId, userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁join森魂雄

			if (goingList.size() > 0) {// 森奀岆join腔袨怓ㄛ佽隴蚚誧砑硒俴賤join紱釬

				if (activityGoingDao.delActivityGoing(goingList.get(0))) {
					response.getWriter().write("Join");// 梓祩換隙珜醱ㄛ魂雄曹峈Join袨怓
				}

			} else {// 森奀岆帤join腔袨怓ㄛ佽隴蚚誧砑硒俴樓join紱釬
				ActivityFollow activityFollow = new ActivityFollow();

				// 羲宎郪蚾魂雄壽蛁氪勤砓
				activityFollow.setActivityId(activityId);
				activityFollow.setUserId(userId);// 魂雄壽蛁氪勤砓郪蚾俇傖

				ActivityGoing activityGoing = new ActivityGoing();

				// 羲宎郪蚾魂雄join氪勤砓
				activityGoing.setActivityId(activityId);
				activityGoing.setUserId(userId);// 魂雄join氪勤砓郪蚾俇傖
				if (activityGoingDao.addActivityGoing(activityGoing)) {
					if (activityFollowDao.getActivityFollowByCondition(
							activityId, userId).size() == 0) {// 佽隴羶衄樓壽蛁ㄛ垀眕森奀氝樓壽蛁
						activityFollowDao.addActivityFollow(activityFollow);
					}
					response.getWriter().write("unJoin");// 梓祩換隙珜醱ㄛ魂雄曹峈going袨怓

				} else {
					response.getWriter().write("fail to join!sorry");// 梓祩換隙珜醱(join囮啖ㄘ
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// view.setViewName("activity/error");
		}
	}

	@RequestMapping("addComment")
	// 蚚誧隱晟
	public ModelAndView addComment(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		ModelAndView view = new ModelAndView();
		try {
			Integer activityId = Integer.parseInt(request
					.getParameter("activityId"));// �堤絞ヶ腔魂雄id
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if (user == null) {
				// userId = 123;
			} else {
				userId = user.getUserId();
			}
			MessageDao m = new MessageDao();
			
			/**
			 * Marked by Chunting
			 * Date: 2013-03-18
			 * The method remind has two or three variables?
			 * m.remind(0, activityId)
			 */
			m.remind(0, activityId, activityId);
			String commentContent = request.getParameter("commentContent");// �堤絞ヶ腔隱晟囀�

			ActivityComment activityComment = new ActivityComment();
			activityComment.setActivityId(activityId);
			activityComment.setCommentContent(commentContent);
			activityComment.setCommentDate(new Date());
			activityComment.setUserId(userId);
			activityCommentDao.addActivityComment(activityComment);
			view.setViewName("redirect:info.action?activityId=" + activityId);// 扢离勤茼腔弝芞view/activity/info.jsp
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			view.setViewName("activity/error");
		}
		return view;
	}

	@RequestMapping("addPicIndex")
	// 奻換魂雄芞え場宎珜醱
	public ModelAndView addPicIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		try {
			Integer activityId = Integer.parseInt(request
					.getParameter("activityId"));
			List<ActivityAlbum> activityAlbumList = activityAlbumDao
					.getActivityAlbumByCondition(activityId, 0);
			String size = "0";
			if (activityAlbumList.size() == 0) {

			} else {
				size = "1";
			}
			Integer flag = 0;
			// String s="asdas";

			Activity activityTemp = activityDao.getActivityById(activityId);
			User user = userDao.getUserById(activityTemp.getUserId());// 腕善桽え奻換氪勤砓
			HttpSession session = request.getSession();

			User currentUser = (User) session.getAttribute("user");// 腕善絞ヶ蚚誧勤砓
			if (currentUser != null) {
				if (currentUser.getUserId() == user.getUserId())// 佽隴絞ヶ蚚誧憩岆森魂雄楷れ蚚誧
				// 垀眕衄芞え奻換腔�癹
				{
					flag = 1;

				}
			}
			UserProfile userProfile = (UserProfile) session
					.getAttribute("userProf");
			view.addObject("userProf", userProfile);
			view.addObject("flag", flag);
			view.addObject("size", size);// �彆羶衄斐膘眈聊 寀溫�森硉 換隙珜醱﹝眕釬潰脤
			view.addObject("activityAlbumList", activityAlbumList);// 蔚腕善腔賦彆溫��ん
			view.addObject("activityId", activityId);// 蔚activityId溫��ん
			// 換隙珜醱ㄐ
			view.setViewName("activity/new_Upload_pics");// 扢离勤茼腔弝芞view/activity/view.jsp
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			view.setViewName("activity/error");
		}
		return view;
	}

	@RequestMapping("addPic")
	// 奻換魂雄芞え
	public ModelAndView addPic(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		Integer activityId = Integer.parseInt(request
				.getParameter("activityId"));
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			// userId = 123;
		} else {
			userId = user.getUserId();
		}
		// ***********************************
		String activityPicPath1_small = request.getParameter("pic_link1_small");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath1_middle = request
				.getParameter("pic_link1_middle");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath1_big = request.getParameter("pic_link1_big");// 植珜醱諾潔�堤pic橈勤繚噤
		if (!activityPicPath1_small.equals("")
				&& !activityPicPath1_middle.equals("")
				&& !activityPicPath1_big.equals("")) {// 瓚剿�彆菴珨跺眈遺祥峈諾腔趕
			String activityAlbumName1;
			String picDescription1;
			activityAlbumName1 = request.getParameter("albumNamee1");// 植珜醱諾潔�堤albumname扽俶
			picDescription1 = request.getParameter("desc1");
			ActivityPic activityPic = new ActivityPic();// 羲宎ぐ蚾activityPic勤砓

			if (activityAlbumName1.equals("")) {// 佽隴岆樓善賸導衄眈聊--載陔導衄眈聊
				Integer albumId = Integer.parseInt(request
						.getParameter("albumId1"));// 植珜醱諾潔�堤眈聊id
				activityPic.setAlbumId(albumId);
				ActivityAlbum activityAlbum = activityAlbumDao
						.getActivityAlbumByCondition(0, albumId).get(0);
				activityAlbum.setPicNum(activityAlbum.getPicNum() + 1);// 載陔眈え杅講ㄩ蔚埻衄腔杅講+陔腔杅講
				activityAlbumDao.updateActivityAlbum(activityAlbum);// 載陔隙���
			} else {// 氝樓陔眈聊
				ActivityAlbum activityAlbum = new ActivityAlbum();
				activityAlbum.setActivityId(activityId);
				activityAlbum.setAlbumCover(activityPicPath1_middle);
				activityAlbum.setAlbumCover_small(activityPicPath1_small);
				activityAlbum.setAlbumCover_big(activityPicPath1_big);
				activityAlbum.setAlbumName(activityAlbumName1);
				activityAlbum.setPicNum(1);
				activityAlbum.setUserId(userId);
				activityAlbumDao.addActivityAlbum(activityAlbum);// 氝樓善眈聊桶
				System.out.println("asdasdasdas"
						+ activityAlbumDao.getMaxAlbumId());
				activityPic.setAlbumId(activityAlbumDao.getMaxAlbumId());// 梑善郔湮albumId婬湔輛pic桶爵

			}
			activityPic.setPicPath(activityPicPath1_middle);
			activityPic.setPicPath_small(activityPicPath1_small);
			activityPic.setPicPath_big(activityPicPath1_big);
			activityPic.setUserId(userId);
			activityPic.setPicDescription(picDescription1);
			activityPicDao.addActivityPic(activityPic);// 氝樓善眈え桶
		}

		// ********************************
		String activityPicPath2_small = request.getParameter("pic_link2_small");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath2_middle = request
				.getParameter("pic_link2_middle");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath2_big = request.getParameter("pic_link2_big");// 植珜醱諾潔�堤pic橈勤繚噤
		if (!activityPicPath2_small.equals("")
				&& !activityPicPath2_middle.equals("")
				&& !activityPicPath2_big.equals("")) {// 瓚剿�彆菴2跺眈遺祥峈諾腔趕
			String activityAlbumName2;
			String picDescription2;
			activityAlbumName2 = request.getParameter("albumNamee2");// 植珜醱諾潔�堤albumname扽俶
			picDescription2 = request.getParameter("desc2");
			ActivityPic activityPic = new ActivityPic();// 羲宎ぐ蚾activityPic勤砓

			if (activityAlbumName2.equals("")) {// 佽隴岆樓善賸導衄眈聊--載陔導衄眈聊
				Integer albumId = Integer.parseInt(request
						.getParameter("albumId2"));// 植珜醱諾潔�堤眈聊id
				activityPic.setAlbumId(albumId);
				ActivityAlbum activityAlbum = activityAlbumDao
						.getActivityAlbumByCondition(0, albumId).get(0);
				activityAlbum.setPicNum(activityAlbum.getPicNum() + 1);// 載陔眈え杅講ㄩ蔚埻衄腔杅講+陔腔杅講
				activityAlbumDao.updateActivityAlbum(activityAlbum);// 載陔隙���
			} else {// 氝樓陔眈聊
				ActivityAlbum activityAlbum = new ActivityAlbum();
				activityAlbum.setActivityId(activityId);
				activityAlbum.setAlbumCover(activityPicPath2_middle);
				activityAlbum.setAlbumCover_small(activityPicPath2_small);
				activityAlbum.setAlbumCover_big(activityPicPath2_big);
				activityAlbum.setAlbumName(activityAlbumName2);
				activityAlbum.setPicNum(1);
				activityAlbum.setUserId(userId);
				activityAlbumDao.addActivityAlbum(activityAlbum);// 氝樓善眈聊桶
				System.out.println("asdasdasdas"
						+ activityAlbumDao.getMaxAlbumId());
				activityPic.setAlbumId(activityAlbumDao.getMaxAlbumId());// 梑善郔湮albumId婬湔輛pic桶爵

			}
			activityPic.setPicPath(activityPicPath2_middle);
			activityPic.setPicPath_small(activityPicPath2_small);
			activityPic.setPicPath_big(activityPicPath2_big);
			activityPic.setUserId(userId);
			activityPic.setPicDescription(picDescription2);
			activityPicDao.addActivityPic(activityPic);// 氝樓善眈え桶
		}

		// ***************************
		String activityPicPath3_small = request.getParameter("pic_link3_small");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath3_middle = request
				.getParameter("pic_link3_middle");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath3_big = request.getParameter("pic_link3_big");// 植珜醱諾潔�堤pic橈勤繚噤
		if (!activityPicPath3_small.equals("")
				&& !activityPicPath3_middle.equals("")
				&& !activityPicPath3_big.equals("")) {// 瓚剿�彆菴3跺眈遺祥峈諾腔趕
			String activityAlbumName3;
			String picDescription3;
			activityAlbumName3 = request.getParameter("albumNamee3");// 植珜醱諾潔�堤albumname扽俶
			picDescription3 = request.getParameter("desc3");
			ActivityPic activityPic = new ActivityPic();// 羲宎ぐ蚾activityPic勤砓

			if (activityAlbumName3.equals("")) {// 佽隴岆樓善賸導衄眈聊--載陔導衄眈聊
				Integer albumId = Integer.parseInt(request
						.getParameter("albumId3"));// 植珜醱諾潔�堤眈聊id
				activityPic.setAlbumId(albumId);
				ActivityAlbum activityAlbum = activityAlbumDao
						.getActivityAlbumByCondition(0, albumId).get(0);
				activityAlbum.setPicNum(activityAlbum.getPicNum() + 1);// 載陔眈え杅講ㄩ蔚埻衄腔杅講+陔腔杅講
				activityAlbumDao.updateActivityAlbum(activityAlbum);// 載陔隙���
			} else {// 氝樓陔眈聊
				ActivityAlbum activityAlbum = new ActivityAlbum();
				activityAlbum.setActivityId(activityId);
				activityAlbum.setAlbumCover(activityPicPath3_middle);
				activityAlbum.setAlbumCover_small(activityPicPath3_small);
				activityAlbum.setAlbumCover_big(activityPicPath3_big);
				activityAlbum.setAlbumName(activityAlbumName3);
				activityAlbum.setPicNum(1);
				activityAlbum.setUserId(userId);
				activityAlbumDao.addActivityAlbum(activityAlbum);// 氝樓善眈聊桶
				System.out.println("asdasdasdas"
						+ activityAlbumDao.getMaxAlbumId());
				activityPic.setAlbumId(activityAlbumDao.getMaxAlbumId());// 梑善郔湮albumId�綴樓1婬湔輛pic桶爵

			}
			activityPic.setPicPath(activityPicPath3_middle);
			activityPic.setPicPath_small(activityPicPath3_small);
			activityPic.setPicPath_big(activityPicPath3_big);
			activityPic.setUserId(userId);
			activityPic.setPicDescription(picDescription3);
			activityPicDao.addActivityPic(activityPic);// 氝樓善眈え桶
		}

		// ************************
		String activityPicPath4_small = request.getParameter("pic_link4_small");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath4_middle = request
				.getParameter("pic_link4_middle");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath4_big = request.getParameter("pic_link4_big");// 植珜醱諾潔�堤pic橈勤繚噤
		if (!activityPicPath4_small.equals("")
				&& !activityPicPath4_middle.equals("")
				&& !activityPicPath4_big.equals("")) {// 瓚剿�彆菴4跺眈遺祥峈諾腔趕
			String activityAlbumName4;
			String picDescription4;
			activityAlbumName4 = request.getParameter("albumNamee4");// 植珜醱諾潔�堤albumname扽俶
			picDescription4 = request.getParameter("desc4");
			ActivityPic activityPic = new ActivityPic();// 羲宎ぐ蚾activityPic勤砓

			if (activityAlbumName4.equals("")) {// 佽隴岆樓善賸導衄眈聊--載陔導衄眈聊
				Integer albumId = Integer.parseInt(request
						.getParameter("albumId4"));// 植珜醱諾潔�堤眈聊id
				activityPic.setAlbumId(albumId);
				ActivityAlbum activityAlbum = activityAlbumDao
						.getActivityAlbumByCondition(0, albumId).get(0);
				activityAlbum.setPicNum(activityAlbum.getPicNum() + 1);// 載陔眈え杅講ㄩ蔚埻衄腔杅講+陔腔杅講
				activityAlbumDao.updateActivityAlbum(activityAlbum);// 載陔隙���
			} else {// 氝樓陔眈聊
				ActivityAlbum activityAlbum = new ActivityAlbum();
				activityAlbum.setActivityId(activityId);
				activityAlbum.setAlbumCover(activityPicPath4_middle);
				activityAlbum.setAlbumCover_small(activityPicPath4_small);
				activityAlbum.setAlbumCover_big(activityPicPath4_big);
				activityAlbum.setAlbumName(activityAlbumName4);
				activityAlbum.setPicNum(1);
				activityAlbum.setUserId(userId);
				activityAlbumDao.addActivityAlbum(activityAlbum);// 氝樓善眈聊桶
				System.out.println("asdasdasdas"
						+ activityAlbumDao.getMaxAlbumId());
				activityPic.setAlbumId(activityAlbumDao.getMaxAlbumId());// 梑善郔湮albumId�綴樓1婬湔輛pic桶爵

			}
			activityPic.setPicPath(activityPicPath4_middle);
			activityPic.setPicPath_small(activityPicPath4_small);
			activityPic.setPicPath_big(activityPicPath4_big);
			activityPic.setUserId(userId);
			activityPic.setPicDescription(picDescription4);
			activityPicDao.addActivityPic(activityPic);// 氝樓善眈え桶
		}

		// **************
		String activityPicPath5_middle = request
				.getParameter("pic_link5_middle");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath5_small = request.getParameter("pic_link5_small");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath5_big = request.getParameter("pic_link5_big");// 植珜醱諾潔�堤pic橈勤繚噤
		if (!activityPicPath5_small.equals("")
				&& !activityPicPath5_middle.equals("")
				&& !activityPicPath5_big.equals("")) {// 瓚剿�彆菴5跺眈遺祥峈諾腔趕
			String activityAlbumName5;
			String picDescription5;
			activityAlbumName5 = request.getParameter("albumNamee5");// 植珜醱諾潔�堤albumname扽俶
			picDescription5 = request.getParameter("desc5");
			ActivityPic activityPic = new ActivityPic();// 羲宎ぐ蚾activityPic勤砓

			if (activityAlbumName5.equals("")) {// 佽隴岆樓善賸導衄眈聊--載陔導衄眈聊
				Integer albumId = Integer.parseInt(request
						.getParameter("albumId5"));// 植珜醱諾潔�堤眈聊id
				activityPic.setAlbumId(albumId);
				ActivityAlbum activityAlbum = activityAlbumDao
						.getActivityAlbumByCondition(0, albumId).get(0);
				activityAlbum.setPicNum(activityAlbum.getPicNum() + 1);// 載陔眈え杅講ㄩ蔚埻衄腔杅講+陔腔杅講
				activityAlbumDao.updateActivityAlbum(activityAlbum);// 載陔隙���
			} else {// 氝樓陔眈聊
				ActivityAlbum activityAlbum = new ActivityAlbum();
				activityAlbum.setActivityId(activityId);
				activityAlbum.setAlbumCover(activityPicPath5_middle);
				activityAlbum.setAlbumCover_small(activityPicPath5_small);
				activityAlbum.setAlbumCover_big(activityPicPath5_big);
				activityAlbum.setAlbumName(activityAlbumName5);
				activityAlbum.setPicNum(1);
				activityAlbum.setUserId(userId);
				activityAlbumDao.addActivityAlbum(activityAlbum);// 氝樓善眈聊桶
				System.out.println("asdasdasdas"
						+ activityAlbumDao.getMaxAlbumId());
				activityPic.setAlbumId(activityAlbumDao.getMaxAlbumId());// 梑善郔湮albumId�綴樓1婬湔輛pic桶爵

			}
			activityPic.setPicPath(activityPicPath5_middle);
			activityPic.setPicPath_small(activityPicPath5_small);
			activityPic.setPicPath_big(activityPicPath5_big);
			activityPic.setUserId(userId);
			activityPic.setPicDescription(picDescription5);
			activityPicDao.addActivityPic(activityPic);// 氝樓善眈え桶
		}

		// **************
		String activityPicPath6_small = request.getParameter("pic_link6_small");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath6_middle = request
				.getParameter("pic_link6_middle");// 植珜醱諾潔�堤pic橈勤繚噤
		String activityPicPath6_big = request.getParameter("pic_link6_big");// 植珜醱諾潔�堤pic橈勤繚噤

		if (!activityPicPath6_small.equals("")
				&& !activityPicPath6_middle.equals("")
				&& !activityPicPath6_big.equals("")) {// 瓚剿�彆菴珨跺眈遺祥峈諾腔趕
			String activityAlbumName6;
			String picDescription6;
			activityAlbumName6 = request.getParameter("albumNamee6");// 植珜醱諾潔�堤albumname扽俶
			picDescription6 = request.getParameter("desc6");
			ActivityPic activityPic = new ActivityPic();// 羲宎ぐ蚾activityPic勤砓

			if (activityAlbumName6.equals("")) {// 佽隴岆樓善賸導衄眈聊--載陔導衄眈聊
				Integer albumId = Integer.parseInt(request
						.getParameter("albumId6"));// 植珜醱諾潔�堤眈聊id
				activityPic.setAlbumId(albumId);
				ActivityAlbum activityAlbum = activityAlbumDao
						.getActivityAlbumByCondition(0, albumId).get(0);
				activityAlbum.setPicNum(activityAlbum.getPicNum() + 1);// 載陔眈え杅講ㄩ蔚埻衄腔杅講+陔腔杅講
				activityAlbumDao.updateActivityAlbum(activityAlbum);// 載陔隙���
			} else {// 氝樓陔眈聊
				ActivityAlbum activityAlbum = new ActivityAlbum();
				activityAlbum.setActivityId(activityId);
				activityAlbum.setAlbumCover(activityPicPath6_middle);
				activityAlbum.setAlbumCover_small(activityPicPath6_small);
				activityAlbum.setAlbumCover_big(activityPicPath6_big);
				activityAlbum.setAlbumName(activityAlbumName6);
				activityAlbum.setPicNum(1);
				activityAlbum.setUserId(userId);
				activityAlbumDao.addActivityAlbum(activityAlbum);// 氝樓善眈聊桶

				activityPic.setAlbumId(activityAlbumDao.getMaxAlbumId());// 梑善郔湮albumId婬湔輛pic桶爵

			}
			activityPic.setPicPath(activityPicPath6_middle);
			activityPic.setPicPath_small(activityPicPath6_small);
			activityPic.setPicPath_big(activityPicPath6_big);
			activityPic.setUserId(userId);
			activityPic.setPicDescription(picDescription6);
			activityPicDao.addActivityPic(activityPic);// 氝樓善眈え桶
		}

		// 換隙珜醱ㄐ
		view.setViewName("redirect:info.action?activityId=" + activityId);// 扢离勤茼腔弝芞view/activity/showPicList.jsp
		return view;
	}

	@RequestMapping("album")
	// 桯尨蚳憮蹈桶
	public ModelAndView showAlbumList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		try {
			Integer activityId = Integer.parseInt(request
					.getParameter("activityId"));
			HttpSession session = request.getSession();

			List<ActivityAlbum> activityAlbumList = new ArrayList();
			activityAlbumList = activityAlbumDao.getActivityAlbumByCondition(
					activityId, 0);// 跦擂珨跺魂雄腔id腕善眈聊list

			List result = new ArrayList();// 郔笝腔賦彆摩
			int page;
			if (request.getParameter("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
			} // 睿 珨跺ぜ蹦list
			result = getListByPage(activityAlbumList, 6, page);// 6沭珨珜 腕善菴page珜ㄐ
			int pageNumbers = getPageNumber(activityAlbumList, 6);// 6沭珨珜
			// ㄛ腕善珨僕撓珜
			String[] pages = new String[pageNumbers];
			for (Integer i = 0; i < pageNumbers; i++) {
				pages[i] = i + 1 + "";
			}

			Integer flag = 0;
			User user = userDao.getUserById(activityDao.getActivityById(
					activityId).getUserId());// 腕善桽え奻換氪勤砓

			User currentUser = (User) session.getAttribute("user");// 腕善絞ヶ蚚誧勤砓
			if (currentUser != null) {
				if (currentUser.getUserId() == user.getUserId())// 佽隴絞ヶ蚚誧憩岆森魂雄楷れ蚚誧
				// 垀眕衄芞え奻換腔�癹
				{
					flag = 1;

				}
			}
			UserProfile userProfile = (UserProfile) session
					.getAttribute("userProf");
			view.addObject("userProf", userProfile);
			view.addObject("flag", flag);
			view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
			view.addObject("activityId", activityId);
			view.addObject("activityAlbumList", result);

			view.setViewName("activity/new_Edit_Album_pics");
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			view.setViewName("activity/error");
		}
		return view;
	}

	@RequestMapping("editAlbum")
	// 奻換魂雄芞え
	public ModelAndView editAlbum(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();

		// ******
		String albumId1 = request.getParameter("albumIdd1");
		if (albumId1 != "") {// �彆衄菴珨跺遺腔趕ㄗ佽隴斛衄啋匼ㄛ秪峈 �森 符夔芃堤跺遺懂ㄘ

			String albumDescription = request.getParameter("desc1");

			List<ActivityAlbum> activityAlbumList = new ArrayList();
			activityAlbumList = activityAlbumDao.getActivityAlbumByCondition(0,
					Integer.parseInt(albumId1));
			ActivityAlbum activityAlbum;
			if (activityAlbumList.size() != 0) {
				activityAlbum = activityAlbumList.get(0);
				activityAlbum.setAlbumDescription(albumDescription);
				activityAlbumDao.updateActivityAlbum(activityAlbum);
			}
		}
		// ********
		String albumId2 = request.getParameter("albumIdd2");

		if (albumId2 != "") {// �彆衄菴2跺遺腔趕ㄗ佽隴斛衄啋匼ㄛ秪峈 �森 符夔芃堤跺遺懂ㄘ

			String albumDescription = request.getParameter("desc2");

			List<ActivityAlbum> activityAlbumList = new ArrayList();
			activityAlbumList = activityAlbumDao.getActivityAlbumByCondition(0,
					Integer.parseInt(albumId2));
			ActivityAlbum activityAlbum;
			if (activityAlbumList.size() != 0) {
				activityAlbum = activityAlbumList.get(0);
				activityAlbum.setAlbumDescription(albumDescription);
				activityAlbumDao.updateActivityAlbum(activityAlbum);
			}
		}
		// ********
		String albumId3 = request.getParameter("albumIdd3");
		if (albumId3 != "") {// �彆衄菴3跺遺腔趕ㄗ佽隴斛衄啋匼ㄛ秪峈 �森 符夔芃堤跺遺懂ㄘ

			String albumDescription = request.getParameter("desc3");

			List<ActivityAlbum> activityAlbumList = new ArrayList();
			activityAlbumList = activityAlbumDao.getActivityAlbumByCondition(0,
					Integer.parseInt(albumId3));
			ActivityAlbum activityAlbum;
			if (activityAlbumList.size() != 0) {
				activityAlbum = activityAlbumList.get(0);
				activityAlbum.setAlbumDescription(albumDescription);
				activityAlbumDao.updateActivityAlbum(activityAlbum);
			}
		}
		// *********
		String albumId4 = request.getParameter("albumIdd4");
		if (albumId4 != "") {// �彆衄菴4跺遺腔趕ㄗ佽隴斛衄啋匼ㄛ秪峈 �森 符夔芃堤跺遺懂ㄘ

			String albumDescription = request.getParameter("desc4");

			List<ActivityAlbum> activityAlbumList = new ArrayList();
			activityAlbumList = activityAlbumDao.getActivityAlbumByCondition(0,
					Integer.parseInt(albumId4));
			ActivityAlbum activityAlbum;
			if (activityAlbumList.size() != 0) {
				activityAlbum = activityAlbumList.get(0);
				activityAlbum.setAlbumDescription(albumDescription);
				activityAlbumDao.updateActivityAlbum(activityAlbum);
			}
		}
		// **********
		String albumId5 = request.getParameter("albumIdd5");
		if (albumId5 != "") {// �彆衄菴5跺遺腔趕ㄗ佽隴斛衄啋匼ㄛ秪峈 �森 符夔芃堤跺遺懂ㄘ

			String albumDescription = request.getParameter("desc5");

			List<ActivityAlbum> activityAlbumList = activityAlbumDao
					.getActivityAlbumByCondition(0, Integer.parseInt(albumId5));
			ActivityAlbum activityAlbum;
			if (activityAlbumList.size() != 0) {
				activityAlbum = activityAlbumList.get(0);
				activityAlbum.setAlbumDescription(albumDescription);
				activityAlbumDao.updateActivityAlbum(activityAlbum);
			}
		}
		// ********
		String albumId6 = request.getParameter("albumIdd6");
		if (albumId6 != "") {// �彆衄菴6跺遺腔趕ㄗ佽隴斛衄啋匼ㄛ秪峈 �森 符夔芃堤跺遺懂ㄘ

			String albumDescription = request.getParameter("desc6");

			List<ActivityAlbum> activityAlbumList = new ArrayList();
			activityAlbumList = activityAlbumDao.getActivityAlbumByCondition(0,
					Integer.parseInt(albumId6));
			ActivityAlbum activityAlbum;
			if (activityAlbumList.size() != 0) {
				activityAlbum = activityAlbumList.get(0);
				activityAlbum.setAlbumDescription(albumDescription);
				activityAlbumDao.updateActivityAlbum(activityAlbum);
			}
		}
		String activityId = request.getParameter("activityId");
		// 換隙珜醱ㄐ
		view.setViewName("redirect:info.action?activityId=" + activityId);// 扢离勤茼腔弝芞
		return view;

	}

	@RequestMapping("showPicList")
	// 珆尨議珨蚳憮垀衄芞え囀�
	public ModelAndView showPicList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		try {
			Integer albumId = Integer.parseInt(request.getParameter("albumId"));
			List<ActivityPic> activityPics = new ArrayList();
			activityPics = activityPicDao.getActivityPicByCondition(0, albumId);// 腕善眈聊id
			List activityAlbumList = new ArrayList();
			activityAlbumList = activityAlbumDao.getActivityAlbumByCondition(0,
					albumId);
			List result = new ArrayList();// 郔笝腔賦彆摩
			Integer activityId = 0;
			if (activityAlbumDao.getActivityAlbumByCondition(0, albumId) == null) {// �彆眈聊脤戙
				// 賦彆峈諾

			} else {
				activityId = activityAlbumDao.getActivityAlbumByCondition(0,
						albumId).get(0).getActivityId();

			}
			int page;
			if (request.getParameter("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
			} // 睿 珨跺ぜ蹦list
			view.addObject("current", page);// 溫�絞ヶ珜醱唗杅

			result = getListByPage(activityPics, 7, page);// 7沭珨珜 腕善菴page珜ㄐ
			int pageNumbers = getPageNumber(activityPics, 7);// 7沭珨珜 ㄛ腕善珨僕撓珜
			String[] pages = new String[pageNumbers];
			for (Integer i = 0; i < pageNumbers; i++) {
				pages[i] = i + 1 + "";
			}
			String tribus = "";
			if (tribusListDao.isAddResource("city", activityId, userId)) {

				tribus = "ok";
				view.addObject("tribus", tribus);// 蔚tribus 硉溫��ん換隙珜醱
			}
			String wish = "";
			if (wishListDao.isAddResource("city", activityId, userId)) {
				wish = "ok";
				view.addObject("wish", wish);// 蔚wish硉溫��ん換隙珜醱
			}
			Integer flag = 0;
			User user = userDao.getUserById(activityPics.get(0).getUserId());// 腕善桽え奻換氪勤砓
			HttpSession session = request.getSession();
			User currentUser = (User) session.getAttribute("user");// 腕善絞ヶ蚚誧勤砓
			if (currentUser != null) {
				if (currentUser.getUserId() == user.getUserId())// 佽隴絞ヶ蚚誧憩岆森魂雄楷れ蚚誧
				// 垀眕衄芞え奻換腔�癹
				{
					flag = 1;

				}
			}
			if (activityPics.size() < 7) {// �彆賦彆摩峈3 珩憩岆苤衾珨俴腔奀緊腔奀緊
				view.addObject("resultSize", 0);

			}
			if (pages != null) {
				view.addObject("maxPage", pages.length);
			}
			if (user != null) {

				List<TribusClassify> tribuClassify = new ArrayList();
				tribuClassify = tribusListClassifyDao
						.getTribusListClassByUserId(user.getUserId());
				view.addObject("tribusClassify", tribuClassify);

			}
			view.addObject("flag", flag);
			view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
			view.addObject("activityAlbum", activityAlbumList.get(0));
			if (activityAlbumList.size() != 0) {
				ActivityAlbum activtiyAlbum = (ActivityAlbum) activityAlbumList
						.get(0);
				String brief = "";
				if (activtiyAlbum.getAlbumDescription() != null) {
					brief = activtiyAlbum.getAlbumDescription().split("\\.")[0]
							+ "...";
				}
				view.addObject("brief", brief);

			}
			view.addObject("albumId", albumId);
			view.addObject("activityPics", result);// 蔚activityPics溫��ん
			view.addObject("activityId", activityId);//
			// 換隙珜醱ㄐ
			view.setViewName("activity/new_showPicList");// 扢离勤茼腔弝芞view/activity/showPicList.jsp
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			view.setViewName("activity/error");
		}
		return view;
	}

	@RequestMapping("addPicComment")
	// 勤桽え輛俴ぜ蹦
	public ModelAndView addPicComment(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		Integer activityPicId = Integer.parseInt(request
				.getParameter("activityPicId"));
		Integer activityId = Integer.parseInt(request
				.getParameter("activityId"));
		String albumName = request.getParameter("albumName");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			// userId = 123;
		} else {
			userId = user.getUserId();
		}
		String commentContent = request.getParameter("commentContent");// �堤絞ヶ腔隱晟囀�
		ActivityPicComment activityPicComment = new ActivityPicComment();
		activityPicComment.setActivityPicId(activityPicId);
		activityPicComment.setCommentContent(commentContent);
		activityPicComment.setUserId(userId);
		activityPicCommentDao.addActivityPicComment(activityPicComment);
		view.setViewName("redirect:showPic.action?activityPicId="
				+ activityPicId + "&albumName=" + albumName + "&activityId="
				+ activityId);// 扢离勤茼腔弝芞
		return view;

	}

	@RequestMapping("showPic")
	// 珆尨魂雄芞え
	public ModelAndView showPic(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		try {
			Integer activityId = Integer.parseInt(request
					.getParameter("activityId"));
			Integer activityPicId = Integer.parseInt(request
					.getParameter("activityPicId"));
			Integer albumId = Integer.parseInt(request.getParameter("albumId"));
			// String type=request.getParameter("type");//籵徹涴跺瓚剿岆艘ヶ珨跺遜岆綴珨跺桽え
			List<ActivityPic> activityPicsAll = new ArrayList();
			activityPicsAll = activityPicDao.getActivityPicByCondition(0,
					albumId);// 腕善眈聊id

			// 梑狟珨跺眈え
			// Map[] arr = new HashMap[list.size()];
			// Object[] objs = list.toArray(arr);
			ActivityPic object[] = new ActivityPic[activityPicsAll.size()];
			// String s[]=new Stirng[2];
			ActivityPic[] activityPicArray = (ActivityPic[]) activityPicsAll
					.toArray(object);
			ActivityPic activityPicTemp = null;
			int activityPicIdNext = -1;
			int length = activityPicArray.length;
			for (int i = 0; i < length; i++) {
				activityPicTemp = activityPicArray[i];
				if (activityPicTemp.getPicId() == activityPicId) {
					if (i == length - 1) {
						activityPicIdNext = activityPicArray[0].getPicId();
					} else {
						activityPicIdNext = activityPicArray[i + 1].getPicId();

					}

				}
			}
			// 梑奻珨跺眈え

			int activityPicIdPrev = -2;
			for (int i = 0; i < length; i++) {
				activityPicTemp = activityPicArray[i];
				if (activityPicTemp.getPicId() == activityPicId) {
					if (i == 0) {
						activityPicIdPrev = activityPicArray[length - 1]
								.getPicId();
					} else {
						activityPicIdPrev = activityPicArray[i - 1].getPicId();

					}

				}
			}
			if (activityPicsAll.size() == 1) {// 絞硐衄珨桲桽え腔奀緊 ヶ綴飲珨欴
				activityPicIdPrev = activityPicId;
				activityPicIdNext = activityPicId;

			}
			view.addObject("activityPicIdPrev", activityPicIdPrev);
			view.addObject("activityPicIdNext", activityPicIdNext);

			String albumName = request.getParameter("albumName");
			List<ActivityPic> activityPics = new ArrayList();
			activityPics = activityPicDao.getActivityPicByCondition(
					activityPicId, 0);// 腕善眈えid
			String activityPicPath = activityPics.get(0).getPicPath_big();
			Map<String, Long> m = getImgInfo(activityPicPath);// 鳳腕芞え腔酗遵喜渡
			for (Map.Entry<String, Long> entry : m.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue());
				view.addObject(entry.getKey(), entry.getValue());
			}

			List commentList = new ArrayList();
			commentList = activityPicCommentDao
					.getActivityPicCommentByCondition(activityPicId, 0);// ぜ蹦dao
			List userCommentList = new ArrayList();
			Iterator iteratorComment = commentList.iterator();
			while (iteratorComment.hasNext()) {
				ActivityPicComment activityPicComment = (ActivityPicComment) iteratorComment
						.next();
				UserComment userComment = new UserComment();// VO勤砓猾蚾羲宎
				userComment.setCommentContent(activityPicComment
						.getCommentContent());
				userComment.setCommentDate(activityPicComment.getCommentDate());
				User user = userDao.getUserById(activityPicComment.getUserId());
				if (user == null) {
					continue;
				}
				userComment.setUserName(user.getUserAlias());
				userComment.setUserPic(user.getUserPic());// VO勤砓猾蚾賦旰
				userComment.setUserId(activityPicComment.getUserId());
				userCommentList.add(userComment);
			}
			if (userCommentList.size() == 0) {// �彆賦彆摩峈諾腔奀緊
				view.addObject("resultSize", 0);

			}
			int page;

			if (request.getParameter("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
			} // 睿 珨跺ぜ蹦list

			view.addObject("current", page);// 溫�絞ヶ珜醱唗杅
			List result = new ArrayList();
			result = getListByPage(userCommentList, 4, page);// 4沭珨珜 腕善菴page珜ㄐ

			int pageNumbers = getPageNumber(userCommentList, 4);// 4沭珨珜 ㄛ腕善珨僕撓珜
			String[] pages = new String[pageNumbers];
			for (Integer i = 0; i < pageNumbers; i++) {
				pages[i] = i + 1 + "";
			}
			// Integer flag=0;//0峈衄訧跡奻換 1峈羶衄訧跡奻換
			User user = userDao.getUserById(activityPics.get(0).getUserId());// 腕善桽え奻換氪勤砓

			String wish = null;
			String tribus = null;

			if (tribusListDao.isAddResource("city", activityId, userId)) {

				tribus = "ok";
				view.addObject("tribus", tribus);// 蔚tribus 硉溫��ん換隙珜醱
			}
			if (wishListDao.isAddResource("city", activityId, userId)) {
				wish = "ok";
				view.addObject("wish", wish);// 蔚wish硉溫��ん換隙珜醱
			}// 陓眊枑倳
			try {
				List unreadInboxmails = messageDao
						.getUserInboxMessageAllUnread(userId);
				view.addObject("unreadMail",
						unreadInboxmails != null ? unreadInboxmails.size() : 0);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (pages != null) {
				view.addObject("maxPage", pages.length);
			}
			HttpSession session = request.getSession();
			UserProfile userProfile = (UserProfile) session
					.getAttribute("userProf");
			User u = (User) session.getAttribute("user");
			if (u != null) {

				List<TribusClassify> tribuClassify = tribuClassify = tribusListClassifyDao
						.getTribusListClassByUserId(u.getUserId());
				view.addObject("tribusClassify", tribuClassify);

			}
			view.addObject("userProf", userProfile);
			view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
			view.addObject("userCommentList", result);
			view.addObject("albumName", albumName);
			view.addObject("albumId", albumId);
			view.addObject("activityPicId", activityPics.get(0).getPicId());
			view.addObject("user", user);
			view.addObject("activityId", activityId);
			view.addObject("activityPics", activityPics.get(0));// 蔚activityPics溫��ん
			// 換隙珜醱ㄐ
			view.setViewName("activity/new_showPic");// 扢离勤茼腔弝芞view/activity/showPic.jsp
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			view.setViewName("activity/error");
		}
		return view;
	}

	@RequestMapping("searchByTag")
	// 偌�藷梓ワ刲坰
	public ModelAndView searchByTag(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		String tagName = request.getParameter("tagName");
		Integer classifiedId = activityClassifiedDao
				.getClassifiedIdByTag(tagName);
		List activityList = new ArrayList();
		activityList = activityDao.getActivityByClassifiedId(classifiedId);
		List activityTagsList = new ArrayList();
		activityTagsList = activityClassifiedDao.getAllActivityClassified();
		List topTribusCity = new ArrayList();
		topTribusCity = activityDao.getTopTribusCity();
		List<User> followList = new ArrayList();// 蔚懂腔followList蹈桶
		List<UserCommentSupper> UserComment = new ArrayList();// userCommetnSupper勤砓牮衄activity勤砓
		Integer page;
		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
		} // 睿 珨跺ぜ蹦list
		view.addObject("current", page);// 溫�絞ヶ珜醱唗杅

		Iterator activityListIterator = activityList.iterator();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			// userId = 123;
		} else {
			userId = user.getUserId();
		}
		while (activityListIterator.hasNext()) {
			UserCommentSupper u = new UserCommentSupper();
			Activity a = (Activity) activityListIterator.next();

			List activityCommentList = new ArrayList();
			activityCommentList = activityCommentDao
					.getActivityCommentByCondition(a.getActivityId(), 0);// 腕善ぜ蹦蹈桶
			Iterator activityCommentListIterator = activityCommentList
					.iterator();// 梢盪蹈桶 �userDao爵梑堤勤茼腔Userㄛ�綴郪蚾汜傖 uc list
			List<UserComment> userCommentList = new ArrayList();// 腕善volist
			while (activityCommentListIterator.hasNext()) {
				ActivityComment ac = (ActivityComment) activityCommentListIterator
						.next();
				UserComment uc = new UserComment();// 羲宎郪蚾uc
				uc.setCommentContent(ac.getCommentContent());
				uc.setUserName(userDao.getUserById(ac.getUserId())
						.getUserAlias());
				uc.setUserPic(userDao.getUserById(ac.getUserId())
						.getUserAlias());// uc郪蚾賦旰
				userCommentList.add(uc);// 氝樓善vo list
			}
			u.setActivity(a);
			while (userCommentList.size() < 2) {
				userCommentList.add(null);
			}
			u.setUserComment(userCommentList);
			UserComment.add(u);

			List<ActivityGoing> activityGoingList = new ArrayList();
			activityGoingList = activityGoingDao.getActivityGoingByCondition(a
					.getActivityId(), 0);// 腕善森魂雄腔userId
			Iterator activityGoingListIterator = activityGoingList.iterator();
			while (activityGoingListIterator.hasNext()) {
				ActivityGoing activityGoing = (ActivityGoing) activityGoingListIterator
						.next();
				followList.add(userDao.getUserById(activityGoing.getUserId()));
			}
		}
		if (UserComment.size() == 0) {// �彆賦彆摩峈諾腔奀緊
			view.addObject("resultSize", 0);

		}
		List result = new ArrayList();// 郔笝腔賦彆摩

		result = getListByPage(UserComment, 6, page);// 6沭珨珜 腕善菴page珜ㄐ

		int pageNumbers = getPageNumber(UserComment, 6);// 6沭珨珜 ㄛ腕善珨僕撓珜
		String[] pages = new String[pageNumbers];
		for (Integer i = 0; i < pageNumbers; i++) {
			pages[i] = i + 1 + "";
		}

		view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
		if (pages != null) {
			view.addObject("maxPage", pages.length);
		}
		List recommendActivity = new ArrayList();
		recommendActivity = activityDao.getAllActivity();// �芢熱魂雄
		int arrowFlag = 0;
		if (recommendActivity != null && recommendActivity.size() > 3) {
			recommendActivity = recommendActivity.subList(0, 3);// �芢熱魂雄
			arrowFlag = 1;
		}

		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserProfile userProfile = (UserProfile) session
				.getAttribute("userProf");
		view.addObject("userProf", userProfile);
		view.addObject("arrowFlag", arrowFlag);// �彆岆1腔趕豢咂珜醱坻鎔腔涴跺List 湮衾3
		// �饒跺蜆侚腔伂排璋芛 珆尨堤懂ㄛ毀眳 祥珆尨
		view.addObject("tagName", tagName);// �堤梓ワ脤戙沭璃 溫��ん換隙珜醱ㄛ隱釬煦珜眳芴
		view.addObject("recommendActivity", recommendActivity);// 蔚recommendActivity溫��ん//
		// // 換隙珜醱ㄐ
		view.addObject("followList", followList);// 蔚followList溫��ん// 換隙珜醱ㄐ
		view.addObject("topTribusCity", topTribusCity);// 蔚�藷傑庈溫��ん// 換隙珜醱ㄐ
		view.addObject("activityList", UserComment);// 蔚activityList溫��ん// 換隙珜醱ㄐ
		view.addObject("activityTagsList", activityTagsList);// 蔚垀衄梓ワlist溫��ん
		view.setViewName("activity/new_tag_Activity_Final");// 扢离勤茼腔弝芞view/activity/showPic.jsp
		return view;
	}

	@RequestMapping("myActivity")
	//
	public ModelAndView myActivity(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		HttpSession session = request.getSession();
		
		User u = (User) session.getAttribute("user");
		if (u == null) {
			// userId = 123;
		} else {
			userId = u.getUserId();
		}

		List<ActivityFollow> activityFollowList = new ArrayList();
		activityFollowList = activityFollowDao.getActivityFollowByCondition(0,
				userId);// 腕善絞ヶ蚚誧follow腔魂雄
		List<MyActivity> activityList = new ArrayList();
		Iterator activityFollowListIterator = activityFollowList.iterator();
		while (activityFollowListIterator.hasNext()) {
			String owner = null;// Host袨怓
			String joined = null;// 樓�袨怓
			String followed = null;// 壽蛁袨怓
			
			ActivityFollow af = (ActivityFollow) activityFollowListIterator
					.next();
			Activity activity = activityDao.getActivityById(af.getActivityId());

			List activityCommentList = new ArrayList();
			activityCommentList = activityCommentDao
					.getActivityCommentByCondition(activity.getActivityId(), 0);// 腕善ぜ蹦蹈桶
			Iterator activityCommentListIterator = activityCommentList
					.iterator();// 梢盪蹈桶 �userDao爵梑堤勤茼腔Userㄛ�綴郪蚾汜傖 uc list
			List<UserComments> userCommentsList = new ArrayList();// 腕善volist
			while (activityCommentListIterator.hasNext()) {
				ActivityComment ac = (ActivityComment) activityCommentListIterator
						.next();
				UserComments uc = new UserComments();// 羲宎郪蚾uc
				uc.setUserComment(ac.getCommentContent());
				uc.setUserId(ac.getUserId());
				uc.setUserPic(userDao.getUserById(ac.getUserId()).getUserPic());// uc郪蚾賦旰
				userCommentsList.add(uc);// 氝樓善vo list

				List followList = new ArrayList();
				followList = activityFollowDao.getActivityFollowByCondition(
						activity.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄
				
				
				if (userId == activity.getUserId()) {// 瓚剿絞ヶ蚚誧岆瘁岆森魂雄腔楷れ氪
					owner = "true";
				} else {
					if (followList.size() > 0) {
						followed = "true";// 桶尨眒壽蛁
					} else {
						followed = "false";// 桶尨帤壽蛁
					}
					List goingList = new ArrayList();
					goingList = activityGoingDao.getActivityGoingByCondition(
							activity.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄

					if (goingList.size() > 0) {
						joined = "true";// 桶尨眒join
					} else {
						joined = "false";// 桶尨帤join
					}
				}
			}

			MyActivity myActivity = new MyActivity();
			String wish=null;
			if (wishListDao.isAddResource("city", activity.getActivityId(), userId)) {
				wish = "ok";
myActivity.setWish("Already Added");
}else{
myActivity.setWish("+ Track List");
}
			myActivity.setActivity(activity);// 郪蚾voㄛ溫輛魂雄濬扽俶
			myActivity.setUser(userDao.getUserById(activity.getUserId()));// 郪蚾vo,溫輛蚚誧濬扽俶
			myActivity.setUserComments(userCommentsList);// 郪蚾voㄛ溫輛ぜ蹦list扽俶
			myActivity.setFollowed(followed);
			myActivity.setJoined(joined);
			myActivity.setOwner(owner);
			activityList.add(myActivity);
		}
		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List recommentActivity = new ArrayList();
		recommentActivity = activityDao.getAllActivity();// �芢熱魂雄
		recommentActivity = recommentActivity.subList(7, 10);// �芢熱魂雄
		List topTribusCity = new ArrayList();
		topTribusCity = activityDao.getTopTribusCity();
		List<User> friends = new ArrayList();
		friends = followDao.getAllFriends(userId);// 腕善森userId垀衄followee勤砓

		List result = new ArrayList();// 郔笝腔賦彆摩

		int page;

		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
		} // 睿 珨跺ぜ蹦list
		view.addObject("current", page);// 溫�絞ヶ珜醱唗杅

		result = getListByPage(activityList, 4, page);// 4沭珨珜 腕善菴page珜ㄐ
if(result.size()==0){
	
	view.addObject("resultSize", 0);
}else{
	view.addObject("resultSize", 1);
	
}
		int pageNumbers = getPageNumber(activityList, 4);// 4沭珨珜 ㄛ腕善珨僕撓珜
		String[] pages = new String[pageNumbers];
		for (Integer i = 0; i < pageNumbers; i++) {
			pages[i] = i + 1 + "";
		}
		UserProfile userProfile = (UserProfile) session
				.getAttribute("userProf");
		view.addObject("maxNumber", pages.length);
		view.addObject("userProf", userProfile);
		User user = userDao.getUserById(userId);// 腕善絞ヶ蚚誧勤砓
		view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
		view.addObject("user", user);// 蔚user溫��ん
		view.addObject("friends", friends);// 蔚friends溫��ん
		view.addObject("topTribusCity", topTribusCity);// 蔚topTribusCity溫��ん
		view.addObject("recommentActivity", recommentActivity);// 蔚recommentActivity溫��ん
		view.addObject("activityList", result);// 蔚activityList溫��ん
		// 換隙珜醱ㄐ
		view.setViewName("activity/new_More_My_Activity_Final");// 扢离勤茼腔弝芞view/activity/myActivity.jsp
		return view;

	}

	@RequestMapping("yourActivity")
	//
	public ModelAndView yourActivity(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		HttpSession session = request.getSession();
		
		userId = Integer.parseInt(request.getParameter("userId"));

		List<ActivityGoing> activityGoingList = new ArrayList();
		activityGoingList = activityGoingDao.getActivityGoingByCondition(0,
				userId);// 腕善絞ヶ蚚誧going腔魂雄
		List<MyActivity> activityList = new ArrayList();
		Iterator activityGoingListIterator = activityGoingList.iterator();
		while (activityGoingListIterator.hasNext()) {
			String owner = null;// Host袨怓
			String joined = null;// 樓�袨怓
			String followed = null;// 壽蛁袨怓
			ActivityGoing af = (ActivityGoing) activityGoingListIterator.next();
			Activity activity = activityDao.getActivityById(af.getActivityId());

			List activityCommentList = activityCommentDao
					.getActivityCommentByCondition(activity.getActivityId(), 0);// 腕善ぜ蹦蹈桶
			Iterator activityCommentListIterator = activityCommentList
					.iterator();// 梢盪蹈桶 �userDao爵梑堤勤茼腔Userㄛ�綴郪蚾汜傖 uc list
			List<UserComments> userCommentsList = new ArrayList();// 腕善volist
			while (activityCommentListIterator.hasNext()) {
				ActivityComment ac = (ActivityComment) activityCommentListIterator
						.next();
				UserComments uc = new UserComments();// 羲宎郪蚾uc
				uc.setUserComment(ac.getCommentContent());
				uc.setUserId(ac.getUserId());
				uc.setUserPic(userDao.getUserById(ac.getUserId()).getUserPic());// uc郪蚾賦旰
				userCommentsList.add(uc);// 氝樓善vo list

				List followList = new ArrayList();
				followList = activityFollowDao.getActivityFollowByCondition(
						activity.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄
				if (userId == activity.getUserId()) {// 瓚剿絞ヶ蚚誧岆瘁岆森魂雄腔楷れ氪
					owner = "true";
				} else {
					if (followList.size() > 0) {
						followed = "true";// 桶尨眒壽蛁
					} else {
						followed = "false";// 桶尨帤壽蛁
					}
					List goingList = new ArrayList();
					goingList = activityGoingDao.getActivityGoingByCondition(
							activity.getActivityId(), userId);// 植杅擂踱跤堤邧沭璃脤戙ㄛ抻聆堤森蚚誧岆瘁壽蛁森魂雄

					if (goingList.size() > 0) {
						joined = "true";// 桶尨眒join
					} else {
						joined = "false";// 桶尨帤join
					}
				}
			}
	
			MyActivity myActivity = new MyActivity();
			String wish=null;
			if (wishListDao.isAddResource("city", activity.getActivityId(), userId)) {
				wish = "ok";
myActivity.setWish("Already Added");
}else{
myActivity.setWish("+ Track List");
}
			myActivity.setActivity(activity);// 郪蚾voㄛ溫輛魂雄濬扽俶
			myActivity.setUser(userDao.getUserById(activity.getUserId()));// 郪蚾vo,溫輛蚚誧濬扽俶
			myActivity.setUserComments(userCommentsList);// 郪蚾voㄛ溫輛ぜ蹦list扽俶
			myActivity.setFollowed(followed);
			myActivity.setJoined(joined);
			myActivity.setOwner(owner);
			activityList.add(myActivity);
		}
		// 陓眊枑倳
		try {
			List unreadInboxmails = messageDao
					.getUserInboxMessageAllUnread(userId);
			view.addObject("unreadMail",
					unreadInboxmails != null ? unreadInboxmails.size() : 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List recommentActivity = new ArrayList();
		recommentActivity = activityDao.getAllActivity();// �芢熱魂雄
		recommentActivity = recommentActivity.subList(7, 10);// �芢熱魂雄
		List topTribusCity = new ArrayList();
		topTribusCity = activityDao.getTopTribusCity();
		List<User> friends = new ArrayList();
		friends = followDao.getAllFriends(userId);// 腕善森userId垀衄followee勤砓

		List result = new ArrayList();// 郔笝腔賦彆摩

		int page;

		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));// 勤茼菴撓珜
		} // 睿 珨跺ぜ蹦list
		result = getListByPage(activityList, 4, page);// 4沭珨珜 腕善菴page珜ㄐ
		
		if(result.size()==0){
			
			view.addObject("resultSize", 0);
		}else{
			view.addObject("resultSize", 1);
			
		}
		
		view.addObject("current", page);// 溫�絞ヶ珜醱唗杅
		int pageNumbers = getPageNumber(activityList, 4);// 4沭珨珜 ㄛ腕善珨僕撓珜
		String[] pages = new String[pageNumbers];
		for (Integer i = 0; i < pageNumbers; i++) {
			pages[i] = i + 1 + "";
		}
		UserProfile userProfile = (UserProfile) session
				.getAttribute("userProf");
		view.addObject("userProf", userProfile);
		view.addObject("maxNumber", pages.length);
		User user = userDao.getUserById(userId);// 腕善絞ヶ蚚誧勤砓
		view.addObject("pageNumbers", pages);// 蔚珨僕嗣屾珜溫��ん換隙珜醱
		view.addObject("user", user);// 蔚user溫��ん
		view.addObject("friends", friends);// 蔚friends溫��ん
		view.addObject("topTribusCity", topTribusCity);// 蔚topTribusCity溫��ん
		view.addObject("recommentActivity", recommentActivity);// 蔚recommentActivity溫��ん
		view.addObject("activityList", result);// 蔚activityList溫��ん
		// 換隙珜醱ㄐ
		view.setViewName("activity/new_More_Friend_Activity_Final");// 扢离勤茼腔弝芞view/activity/myActivity.jsp
		return view;

	}

	@RequestMapping("byHotSearch")
	//
	public ModelAndView hotSearch(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView view = new ModelAndView();
		Integer activityPicId = Integer.parseInt(request
				.getParameter("activityPicId"));
		List<ActivityPic> activityPics = new ArrayList();
		activityPics = activityPicDao.getActivityPicByCondition(activityPicId,
				0);// 腕善眈えid
		view.addObject("activityPics", activityPics.get(0));// 蔚activityPics溫��ん
		// 換隙珜醱ㄐ
		HttpSession session = request.getSession();
		UserProfile userProfile = (UserProfile) session
				.getAttribute("userProf");
		view.addObject("userProf", userProfile);
		view.setViewName("activity/showPic");// 扢离勤茼腔弝芞view/activity/showPic.jsp
		return view;
	}

	// 煦珜滲杅ㄛ藩跺啋匼 衄謗跺扽俶 珨跺岆妗暱扽俶ㄛ珨跺岆垀婓珜杅
	public List paging(List l, int watershed) { // 渾詻腔listㄛ藩珜珆尨嗣屾沭醴.
		int pageNumbers;// 珨僕撓珜
		if (l.size() % watershed == 0) {
			pageNumbers = (int) l.size() / watershed;// �彆羶衄豻杅腔①錶狟ㄛ妀�淕
		} else {
			pageNumbers = (int) l.size() / watershed + 1;// �彆衄豻杅腔①錶狟ㄛ妀�淕+1
		}
		List resultList = new ArrayList();// 賦彆摩
		for (int p = 0; p < pageNumbers; p++) {

			if (p + 1 == pageNumbers) {// 郔綴珨珜

				Iterator iterator1 = l.subList(p * 5, l.size()).iterator();// 諍�郔綴珨珜
				while (iterator1.hasNext()) {
					Ele e = new Ele();
					e.setEle(iterator1.next());
					e.setPageNumber(p + 1);
					resultList.add(e);
				}
			} else {
				Iterator iterator = l.subList(p * 5, (p + 1) * 5).iterator();// from
				// 郔綴珨珜眳ヶ腔
				while (iterator.hasNext()) {
					Ele e = new Ele();
					e.setEle(iterator.next());
					e.setPageNumber(p + 1);
					resultList.add(e);
				}
			}
		}
		return resultList;
	}

	// 煦珜滲杅潠等唳
	// 煦珜滲杅ㄛ藩跺啋匼 衄謗跺扽俶 珨跺岆妗暱扽俶ㄛ珨跺岆垀婓珜杅
	public List getListByPage(List l, int watershed, int p) { // 渾詻腔listㄛ藩珜珆尨嗣屾沭醴.ㄛ砑�菴��腔硉
		int pageNumbers = getPageNumber(l, watershed);

		if (l.size() != 0) {
			if (p == pageNumbers) {// 郔綴珨珜

				return l.subList((p - 1) * watershed, l.size());// 諍�郔綴珨珜

			} else {
				return l.subList((p - 1) * watershed, p * watershed);// from//
				// 郔綴珨珜眳ヶ腔

			}
		} else {
			return l;
		}

	}

	public int getPageNumber(List l, int watershed) {// 腕善珨僕撓珜
		int pageNumbers;// 珨僕撓珜
		if (l.size() % watershed == 0) {
			pageNumbers = (int) (l.size() / watershed);// �彆羶衄豻杅腔①錶狟ㄛ妀�淕
		} else {
			pageNumbers = (int) (l.size() / watershed) + 1;// �彆衄豻杅腔①錶狟ㄛ妀�淕+1
		}
		return pageNumbers;
	}

	public static Map<String, Long> getImgInfo(String imgpath) {
		Map<String, Long> map = new HashMap<String, Long>(3);
		// File imgfile = new File(imgpath);

		try {
			// FileInputStream fis = new FileInputStream(imgfile);
			URL url = new URL(imgpath);
			BufferedInputStream bis = new BufferedInputStream(url.openStream());
			BufferedImage buff = ImageIO.read(url.openStream());
			map.put("picWidth", buff.getWidth() * 1L);
			map.put("picHeight", buff.getHeight() * 1L);
			// map.put("s", imgfile.length());
			bis.close();
		} catch (FileNotFoundException e) {
			// System.err.println("垀跤腔芞え恅璃" + imgfile.getPath() +
			// "祥湔婓ㄐ數呾芞え喜渡湮苤陓洘囮啖ㄐ");
			map = null;
		} catch (IOException e) {
			// System.err.println("數呾芞え" + imgfile.getPath() + "喜渡湮苤陓洘囮啖ㄐ");
			map = null;
		}
		return map;
	}
}
// <fmt:formatDate value="${activityPics.recordDate}" type="date"
// datetyle="medium"/>
