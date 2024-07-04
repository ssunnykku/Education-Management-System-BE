package com.kosta.ems.notification;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
	private final NotificationService notification;
	//공지사항 전체 글 search
//	@GetMapping //@AuthenticationPrincipal 사용할수있음.
//    public Map<String,Object> notificationBoard( HttpSession session, @RequestParam(defaultValue = "1") int page) {
//    	log.info((String) session.getAttribute("managerId").toString());
//    	//String managerId="d893bf71-2f8f-11ef-b0b2-0206f94be675";
//    	String managerId = (String) session.getAttribute("managerId");
//    	log.info(notification.searchAll(managerId, page, 10).toString());
//    	log.info(String.valueOf(page));
//    	model.addAttribute(managerId);
//        return Map.of("result",(List<NotificationDTO>)notification.searchAll(managerId,page, 10));
//    }
	@GetMapping
	//@AuthenticationPrincipal 사용할수있음.
	public Map<String, Object> notificationBoard(HttpSession session, @RequestParam(defaultValue = "1") int page) {
	    String managerId = (String) session.getAttribute("managerId");
	    Collection<NotificationDTO> notifications = notification.searchAll(managerId, page, 10);
	    log.info(notification.searchAll(managerId, page, 10).toString());
    	log.info(String.valueOf(page));
	    Map<String, Object> response = new HashMap<>();
	    response.put("sessionId", managerId); // 세션 ID 추가
	    response.put("result", notifications); // 알림 목록 추가
	    return response;
	}
	
	//공지사항 글 추가(O)
	@PostMapping("/write")
	public Map<String, Boolean> addPost(@RequestBody NotificationDTO dto, HttpServletRequest request) {
		//String managerId = (String) session.getAttribute("managerId");
		dto.setManagerId(getManagerIdOfLoginUser(request));
		boolean result= notification.addNotification(dto);
		return Map.of("result",result);
	}
	//공지사항 inactive(delete) (O)
	@PatchMapping("/post/{notificationSeq}")
	public Map<String, Boolean> deletePost(@PathVariable("notificationSeq") int notificationSeq){
		boolean result=notification.deleteNotificaiotn(notificationSeq);
		System.out.println("시작");
		return Map.of("result",result);
	}
	//검색하기(O)
	@GetMapping("/list/{keyword}")
	public Map<String, Collection> searchByKeyword(@PathVariable("keyword") String keyword, HttpSession session) throws NoResultsFoundException{
		String managerId = (String) session.getAttribute("managerId");
		return Map.of("result",notification.searchByKeyword(keyword, managerId));
	}
	//글 content 확인(O) 
	@GetMapping("/post/{notificationSeq}")
	public NotificationDTO getDescription(@PathVariable("notificationSeq") int notificationSeq,HttpSession session) {
		String managerId = (String) session.getAttribute("managerId");
		NotificationDTO notificationDTO = notification.getDescription(notificationSeq);
	    if (managerId.equals(notificationDTO.getManagerId())) {
	        // showButton 속성을 true로 설정합니다.
	        notificationDTO.setSameWriter(true);
	    } else {
	        // showButton 속성을 false로 설정합니다.
	        notificationDTO.setSameWriter(false);
	    }
	    return notificationDTO;
	}
	//공지 업데이트(O)
	@PutMapping("/post/{notificationSeq}")
	public Map<String, Boolean> editNotification(@RequestBody NotificationDTO dto,@PathVariable("notificationSeq") int notificationSeq,HttpServletRequest request){
		dto.setManagerId(getManagerIdOfLoginUser(request));
		System.out.println("시작");
		boolean result= notification.updateNotification(dto);
		//String managerId= "d893bf71-2f8f-11ef-b0b2-0206f94be675";
		return Map.of("result",result);
	}
	
	//SessionID를 받아서 ManagerID 컨트롤하기.
	private String getManagerIdOfLoginUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (String) session.getAttribute("managerId");
	}

}
