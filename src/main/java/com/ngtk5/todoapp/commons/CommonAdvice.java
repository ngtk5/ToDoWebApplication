package com.ngtk5.todoapp.commons;

import java.util.Objects;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import com.ngtk5.todoapp.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controllerの前に行なう共通処理
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CommonAdvice {

    @NonNull
    private final UserService userService;

    /**
     * リクエストの空白文字をnullに変換する共通処理
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        log.info("空白変換の共通処理");
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * 現在ログインしているユーザーのユーザーネームをModelにセットする共通処理
     * @param model Model
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     */
    @ModelAttribute
    public void setLoginUserInfo(Model model, HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getRemoteUser();
        // ログインしていれば
        if (Objects.nonNull(username)) {
            model.addAttribute("currentLoginUsername", this.userService.getUsername(username));
            log.info("{}", model.getAttribute("currentLoginUsername"));
        }
    }
}