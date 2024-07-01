package com.kosta.ems.notification;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
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
    @Qualifier("notificationService")
    private final NotificationService notification;

    //공지사항 글 추가(O)
    @PostMapping("/write")
    public Map<String, Boolean> addPost(@RequestBody NotificationDTO dto, HttpServletRequest request) {
        //String managerId = (String) session.getAttribute("managerId");
        dto.setManagerId(getManagerIdOfLoginUser(request));
        System.out.println(dto.getManagerId());
        boolean result = notification.addNotification(dto);
        return Map.of("result", result);
    }

    //공지사항 inactive(delete) (O)
    @PatchMapping(value = {"/post/{notificationSeq}", "/post"})
    public Map<String, Boolean> deletePost(@PathVariable("notificationSeq") int notificationSeq) {
        boolean result = notification.deleteNotificaiotn(notificationSeq);
        return Map.of("result", result);
    }

    //검색하기(O)
    @GetMapping("/list")
    public Map<String, Collection> searchByKeyword(@RequestParam String keyword, HttpServletRequest request) throws NoResultsFoundException {
        String managerId = "d893bf71-2f8f-11ef-b0b2-0206f94be675";//session 메모리
        return Map.of("result", notification.searchByKeyword(keyword, managerId));
    }

    //글 content 확인(O)
    @GetMapping("/post/{notificationSeq}")
    public NotificationDTO getDescription(@PathVariable("notificationSeq") int notificationSeq) {
        return notification.getDescription(notificationSeq);
    }

    //공지 업데이트(O)
    @PutMapping("/post/{notificationSeq}")
    public Map<String, Boolean> editNotification(@RequestBody NotificationDTO dto, @PathVariable("notificationSeq") int notificationSeq, HttpServletRequest request) {
        dto.setManagerId(getManagerIdOfLoginUser(request));
        boolean result = notification.updateNotification(dto);
        //String managerId= "d893bf71-2f8f-11ef-b0b2-0206f94be675";
        return Map.of("result", result);
    }

    //SessionID를 받아서 ManagerID 컨트롤하기.
    private String getManagerIdOfLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("managerId");
    }

}
