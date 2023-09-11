package com.ngtk5.todoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.ngtk5.todoapp.beans.Task;
import com.ngtk5.todoapp.beans.User;
import com.ngtk5.todoapp.configs.SecurityConfig;
import com.ngtk5.todoapp.forms.NewTaskForm;
import com.ngtk5.todoapp.forms.SignUpUserForm;
import com.ngtk5.todoapp.forms.TaskForm;
import com.ngtk5.todoapp.forms.UserForm;
import com.ngtk5.todoapp.services.TaskService;
import com.ngtk5.todoapp.services.UserDetailsImplService;
import com.ngtk5.todoapp.services.UserService;
import com.ngtk5.todoapp.validations.PasswordMatchValid;
import com.ngtk5.todoapp.validations.UniqueTelephoneValid;
import com.ngtk5.todoapp.validations.UniqueUserIdValid;

/* ToDoController Unit Test Class. */
@WebMvcTest(controllers = ToDoController.class)
@Import(SecurityConfig.class)
public class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsImplService userDetailsImplService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @MockBean
    private UniqueUserIdValid uniqueUserIdValid;

    @MockBean
    private UniqueTelephoneValid uniqueTelephoneValid;

    @MockBean
    private PasswordMatchValid passwordMatchValid;

    @MockBean
    private SmartValidator smartValidator;

    @MockBean
    private SecurityContextLogoutHandler logoutHandler;

/* ---------------------------
    showLoginForm Method Test
   --------------------------- */

    @Test
    @WithMockUser(username = "testUser")
    void showLoginForm_ログイン中_リダイレクトされる() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = get("/login");
        doReturn("testUser").when(userService).getUsername(any());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
    }

    @Test
    void showLoginForm_ログインエラー_エラーメッセージが表示される() throws Exception {
        // Given
        String userId = "testUser";
        String errorMessage = "Invalid credentials.";
        Exception exception = new Exception(errorMessage);
        MockHttpServletRequestBuilder requestBuilder = get("/login-fail")
                .param("userId", userId)
                .requestAttr(WebAttributes.AUTHENTICATION_EXCEPTION, exception);

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("login", modelAndView.getViewName());
        assertEquals(userId, modelAndView.getModel().get("userId"));
        assertEquals(errorMessage, modelAndView.getModel().get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

    @Test
    void showLoginForm_未ログイン_ログイン画面が表示される() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = get("/login");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("login", modelAndView.getViewName());
        assertNull(modelAndView.getModel().get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

/* --------------------
    loguot Method Test
   -------------------- */

    @Test
    void loguot_未ログイン_リダイレクトされる() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = get("/logout");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void logout_ログイン中_GETリクエスト_ログアウトしリダイレクトされる() throws Exception {
        // Given
        String loguotMessage = "ログアウトに成功しました。";
        doReturn("testUser").when(userService).getUsername(any());
        MockHttpServletRequestBuilder requestBuilder = get("/logout");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/login", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals(loguotMessage, flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        // モックユーザーの情報を検証
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "testUser")
    void logout_ログイン中_POSTリクエスト_ログアウトしリダイレクトされる() throws Exception {
        // Given
        String loguotMessage = "ログアウトに成功しました。";
        doReturn("testUser").when(userService).getUsername(any());
        MockHttpServletRequestBuilder requestBuilder = post("/logout").with(csrf());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/login", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals(loguotMessage, flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        // モックユーザーの情報を検証
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
        SecurityContextHolder.clearContext();
    }

/* ----------------------------
    showSignUpForm Method Test
   ---------------------------- */

    /**
     * ログインしている状態で/sign_upにGETリクエストを実行し,
     * /todoにリダイレクトされることを検証
     */
    @Test
    @WithMockUser(username = "testUser")
    void showSignUpForm_ログイン中_リダイレクトされる() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = get("/sign_up");
        doReturn("testUser").when(userService).getUsername(any());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // メソッドの実行回数の検証
        verify(userService, times(1)).getUsername(any());
    }

    /**
     * ログインしていない状態で/sign_upにGETリクエストを実行し,
     * 新規登録画面が表示されることを検証
     */
    @Test
    void showSignUpForm_未ログイン_新規登録画面が表示される() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = get("/sign_up");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        //Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("sign_up", modelAndView.getViewName());
        SignUpUserForm signUpUserForm = (SignUpUserForm) modelAndView.getModel().get("signUpUserForm");
        assertNotNull(signUpUserForm);
        assertNull(signUpUserForm.getUserId());
        assertNull(signUpUserForm.getUsername());
        assertNull(signUpUserForm.getPassword());
        assertNull(signUpUserForm.getConfirmPassword());
        assertNull(signUpUserForm.getTelephone());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

/* ---------------------
    addUser Method Test
   --------------------- */

    @Test
    @WithMockUser(username = "testUser")
    void addUser_ログイン中_リダイレクトされる() throws Exception {
        // Given
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // uniqueTelephoneValidのモック設定
        doReturn(true).when(uniqueTelephoneValid).supports(any());
        // uniqueUserIdValidのモック設定
        doReturn(true).when(uniqueUserIdValid).supports(any());
        // リクエスト準備
        MockHttpServletRequestBuilder requestBuilder = post("/sign_up")
            .with(csrf());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(uniqueTelephoneValid, times(1)).supports(any());
        verify(uniqueUserIdValid, times(1)).supports(any());
        verify(userService, never()).add(any());
        verify(userService, times(1)).getUsername(any());
    }

    /**
     * SignUpUserFormに設定しているバリデーションを誘発させ,
     * ユーザー情報の登録をせずに新規登録画面が表示されるかを検証します.
     */
    @Test
    void addUser_バリデーションエラー_新規登録画面が表示される() throws Exception {
        // Given
        String expectUserId = "testId";
        String expectUsername = "TooLongTestUsername";
        String expectPassword = "testPass";
        String expectConfirmPassword = "testPass";
        String expectTelephone = "09812356724";
        MockHttpServletRequestBuilder requestBuilder = post("/sign_up")
            .with(csrf())
            .param("userId", expectUserId)
            .param("username", expectUsername)
            .param("password", expectPassword)
            .param("confirmPassword", expectConfirmPassword)
            .param("telephone", expectTelephone);
        // uniqueTelephoneValidのモック設定
        doReturn(true).when(uniqueTelephoneValid).supports(any());
        doNothing().when(uniqueTelephoneValid).validate(any(), any());
        // uniqueUserIdValidのモック設定
        doReturn(true).when(uniqueUserIdValid).supports(any());
        doNothing().when(uniqueUserIdValid).validate(any(), any());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("sign_up", modelAndView.getViewName());
        // バリデーションエラーが発生しているか検証
        BindingResult bindingResult = (BindingResult) modelAndView.getModel().get(BindingResult.MODEL_KEY_PREFIX + "signUpUserForm");
        assertTrue(bindingResult.hasErrors());
        assertEquals(1, bindingResult.getErrorCount());
        // usernameフィールドの検証
        FieldError usernameFieldError = bindingResult.getFieldError("username");
        assertNotNull(usernameFieldError);
        assertEquals("length must be between 1 and 10", usernameFieldError.getDefaultMessage());
        // signUpUserFormの各フィールドの検証
        SignUpUserForm signUpUserForm = (SignUpUserForm) modelAndView.getModel().get("signUpUserForm");
        assertEquals(expectUserId, signUpUserForm.getUserId());
        assertEquals(expectUsername, signUpUserForm.getUsername());
        assertNull(signUpUserForm.getPassword());
        assertNull(signUpUserForm.getConfirmPassword());
        assertEquals(expectTelephone, signUpUserForm.getTelephone());
        // メソッド実行回数の検証
        verify(uniqueTelephoneValid, times(1)).supports(any());
        verify(uniqueTelephoneValid, times(1)).validate(any(), any());
        verify(uniqueUserIdValid, times(1)).supports(any());
        verify(uniqueUserIdValid, times(1)).validate(any(), any());
        verify(userService, never()).add(any());
        verify(userService, never()).getUsername(any());
    }


    @Test
    void addUser_ユーザ情報登録成功_リダイレクトされる() throws Exception {
        // Given
        String expectUserId = "testId";
        String expectUsername = "testName";
        String expectPassword = "testPass";
        String expectConfirmPassword = "testPass";
        String expectTelephone = "09812356724";
        String alertMessage = "新規登録に成功しました。";
        MockHttpServletRequestBuilder requestBuilder = post("/sign_up")
            .with(csrf())
            .param("userId", expectUserId)
            .param("username", expectUsername)
            .param("password", expectPassword)
            .param("confirmPassword", expectConfirmPassword)
            .param("telephone", expectTelephone);
        // uniqueTelephoneValidのモック設定
        doReturn(true).when(uniqueTelephoneValid).supports(any());
        doNothing().when(uniqueTelephoneValid).validate(any(), any());
        // uniqueUserIdValidのモック設定
        doReturn(true).when(uniqueUserIdValid).supports(any());
        doNothing().when(uniqueUserIdValid).validate(any(), any());
        // userServiceのモック設定
        doReturn(1).when(userService).add(any());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/login", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals(alertMessage, flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(uniqueTelephoneValid, times(1)).supports(any());
        verify(uniqueTelephoneValid, times(1)).validate(any(), any());
        verify(uniqueUserIdValid, times(1)).supports(any());
        verify(uniqueUserIdValid, times(1)).validate(any(), any());
        verify(userService, times(1)).add(any());
        verify(userService, never()).getUsername(any());
    }

/* --------------------------
    showToDoList Method Test
   -------------------------- */

    @Test
    void showToDoList_未ログイン_リダイレクトされる() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = get("/todo");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void showTodoList_タスク一覧画面表示() throws Exception {
        // Given
        List<Task> taskList = new ArrayList<Task>();
        doReturn(taskList).when(taskService).sortTaskList(any(), any());
        doReturn("testUser").when(userService).getUsername(any());
        MockHttpServletRequestBuilder requestBuilder = get("/todo");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("todo_list", modelAndView.getViewName());
        assertEquals(taskList, modelAndView.getModel().get("tasks"));
        assertEquals("testUser", modelAndView.getModel().get("currentLoginUsername"));
        // メソッド実行回数の検証
        verify(taskService, times(1)).sortTaskList(any(), any());
        verify(userService, times(1)).getUsername(any());
    }

/* -----------------------
    setSortId Method Test
   ----------------------- */

    @Test
    void setSortId_未ログイン_リダイレクトされる() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = get("/todo/sort/testSortId");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void setSortId_タスク一覧画面にリダイレクトされる() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = get("/todo/sort/testSortId");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("testSortId", flashMap.get("sortId"));
    }

/* --------------------------------
    initCreateTaskForm Method Test
   -------------------------------- */

    @Test
    void initCreateTaskForm_未ログイン_リダイレクトされる() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = get("/todo/create");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
    }

    @Test
    @WithMockUser(username = "testUser")
    void initCreateTaskForm_新規作成画面が表示される() throws Exception {
        // Given
        NewTaskForm newTaskForm = new NewTaskForm();
        doReturn("testUser").when(userService).getUsername(any());
        MockHttpServletRequestBuilder requestBuilder = get("/todo/create");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("create_task", modelAndView.getViewName());
        assertEquals(newTaskForm, modelAndView.getModel().get("newTaskForm"));
        assertEquals("testUser", modelAndView.getModel().get("currentLoginUsername"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
    }

/* -------------------------
    addTaskForm Method Test
   ------------------------- */

    @Test
    void addTaskForm_未ログイン_リダイレクトされる() throws Exception {
        // Given
        MockHttpServletRequestBuilder requestBuilder = post("/todo/create")
            .with(csrf());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void addTaskForm_バリデーションエラー_新規作成画面が表示される() throws Exception {
        // Given
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setTitle("TooLongTestTitleValidError!");
        newTaskForm.setDescription("Test Descrption.");
        newTaskForm.setDeadline(LocalDate.of(2023, 10, 01));
        doReturn("testUser").when(userService).getUsername(any());
        MockHttpServletRequestBuilder requestBuilder = post("/todo/create")
            .with(csrf())
            .flashAttr("newTaskForm", newTaskForm);

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("create_task", modelAndView.getViewName());
        assertEquals(newTaskForm, modelAndView.getModel().get("newTaskForm"));
        assertEquals("testUser", modelAndView.getModel().get("currentLoginUsername"));
        // バリデーションエラーが発生しているか検証
        BindingResult bindingResult = (BindingResult) modelAndView.getModel().get(BindingResult.MODEL_KEY_PREFIX + "newTaskForm");
        assertTrue(bindingResult.hasErrors());
        assertEquals(1, bindingResult.getErrorCount());
        // titleフィールドの検証
        FieldError titleFieldError = bindingResult.getFieldError("title");
        assertNotNull(titleFieldError);
        assertEquals("length must be between 1 and 20", titleFieldError.getDefaultMessage());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertNull(flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, never()).createNewTaskId();
        verify(taskService, never()).add(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void addTaskForm_タスク情報を登録_リダイレクトされる() throws Exception {
        // Given
        // newTaskFormの準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setTitle("TestTitle");
        newTaskForm.setDescription("Test Descrption.");
        newTaskForm.setDeadline(LocalDate.of(2023, 10, 01));
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // taskServiceのモック設定
        doReturn("testTaskId").when(taskService).createNewTaskId();
        doReturn(1).when(taskService).add(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = post("/todo/create")
            .with(csrf())
            .flashAttr("newTaskForm", newTaskForm);

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        // バリデーションエラーの検証
        BindingResult bindingResult = (BindingResult) modelAndView.getModel().get(BindingResult.MODEL_KEY_PREFIX + "newTaskForm");
        assertNull(bindingResult);
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("タスク情報を登録しました。", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, times(1)).createNewTaskId();
        verify(taskService, times(1)).add(any());
    }

/* ----------------------------
   initEditTaskForm Method Test
   ---------------------------- */

    @Test
    void initEditTaskForm_未ログイン_リダイレクトされる() throws Exception {
        // Given
        /// リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/edit/testTaskId");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(taskService, never()).getTask(any(), any());
        verify(userService, never()).getUsername(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void initEditTaskForm_タスク情報取得失敗_リダイレクトされる() throws Exception {
        // Given
        // TaskServiceのモック設定
        doReturn(null).when(taskService).getTask(any(), any());
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/edit/testTaskId");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("タスクが存在しないか、取得に失敗しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, times(1)).getTask(any(), any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void initEditTaskForm_タスク情報取得成功_編集画面が表示される() throws Exception {
        // Given
        // Taskの準備
        Task task = new Task();
        task.setTaskId("testTaskId");
        task.setUserId("testUser");
        task.setTitle("testTitle");
        task.setDescription("Test Description.");
        task.setCompletedFlg(false);
        task.setDeadline(LocalDate.of(2023, 10, 01));
        task.setCreationTime(LocalDateTime.of(2023, 01, 01, 0, 0, 0));
        // TaskFormの準備
        TaskForm taskForm = new TaskForm();
        taskForm.setTaskId("testTaskId");
        taskForm.setTitle("testTitle");
        taskForm.setDescription("Test Description.");
        taskForm.setCompletedFlg(false);
        taskForm.setDeadline(LocalDate.of(2023, 10, 01));
        // TaskServiceのモック設定
        doReturn(task).when(taskService).getTask(any(), any());
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/edit/testTaskId");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("edit_task", modelAndView.getViewName());
        assertEquals(taskForm, modelAndView.getModel().get("taskForm"));
        assertEquals("testUser", modelAndView.getModel().get("currentLoginUsername"));
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertNull(flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, times(1)).getTask(any(), any());
    }

/* -----------------------
   updateTask Method Test
   ----------------------- */

    @Test
    void updateTask_未ログイン_リダイレクトされる() throws Exception {
        // Given
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = post("/todo/edit/testTaskId")
            .with(csrf());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void updateTask_バリデーションエラー_編集画面が表示される() throws Exception {
        // Given
        //TaskFormの準備
        TaskForm taskForm = new TaskForm();
        taskForm.setTaskId("testTaskId");
        taskForm.setTitle("TooLongTestTitleValidError!");
        taskForm.setDescription("Test Description.");
        taskForm.setDeadline(LocalDate.of(2023, 10, 01));
        taskForm.setCompletedFlg(false);
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = post("/todo/edit/testTaskId")
            .with(csrf())
            .flashAttr("taskForm", taskForm);

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("edit_task", modelAndView.getViewName());
        // バリデーションエラーの検証
        BindingResult bindingResult = (BindingResult) modelAndView.getModel().get(BindingResult.MODEL_KEY_PREFIX + "taskForm");
        assertNotNull(bindingResult);
        assertEquals(1, bindingResult.getErrorCount());
        // titleフィールドの検証
        FieldError titleFieldError = bindingResult.getFieldError("title");
        assertNotNull(titleFieldError);
        assertEquals("length must be between 1 and 20", titleFieldError.getDefaultMessage());
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, never()).update(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void updateTask_タスク情報更新失敗_リダイレクトされる() throws Exception {
        // Given
        //TaskFormの準備
        TaskForm taskForm = new TaskForm();
        taskForm.setTaskId("testTaskId");
        taskForm.setTitle("testTitle");
        taskForm.setDescription("Test Description.");
        taskForm.setDeadline(LocalDate.of(2023, 10, 01));
        taskForm.setCompletedFlg(false);
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // taskServiceのモック設定
        doReturn(0).when(taskService).update(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = post("/todo/edit/testTaskId")
            .with(csrf())
            .flashAttr("taskForm", taskForm);

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("タスク情報の更新に失敗しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, times(1)).update(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void updateTask_タスク情報更新成功_リダイレクトされる() throws Exception {
        // Given
        //TaskFormの準備
        TaskForm taskForm = new TaskForm();
        taskForm.setTaskId("testTaskId");
        taskForm.setTitle("testTitle");
        taskForm.setDescription("Test Description.");
        taskForm.setDeadline(LocalDate.of(2023, 10, 01));
        taskForm.setCompletedFlg(false);
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // taskServiceのモック設定
        doReturn(1).when(taskService).update(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = post("/todo/edit/testTaskId")
            .with(csrf())
            .flashAttr("taskForm", taskForm);

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("タスク情報の更新に成功しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, times(1)).update(any());
    }

/* -----------------------
   daleteTask Method Test
   ----------------------- */

    @Test
    void daleteTask_未ログイン_リダイレクトされる() throws Exception {
        // Given
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/delete/testTaskId");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void daleteTask_タスク情報削除失敗_リダイレクトされる() throws Exception {
        // Given
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // taskServiceのモック設定
        doReturn(0).when(taskService).delete(any(), any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/delete/testTaskId");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("タスク情報の削除に失敗しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, times(1)).delete(any(), any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void daleteTask_タスク情報削除成功_リダイレクトされる() throws Exception {
        // Given
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // taskServiceのモック設定
        doReturn(1).when(taskService).delete(any(), any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/delete/testTaskId");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("タスク情報の削除に成功しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, times(1)).delete(any(), any());
    }

/* ------------------------------
   completedAllDelete Method Test
   ------------------------------ */

    @Test
    void completedAllDelete_未ログイン_リダイレクトされる() throws Exception {
        // Given
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/completed_all_delete");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void completedAllDelete_完了済みタスク削除失敗_リダイレクトされる() throws Exception {
        // Given
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // taskServiceのモック設定
        doReturn(0).when(taskService).completedAllDelete(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/completed_all_delete");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("完了済みタスクの削除に失敗しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, times(1)).completedAllDelete(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void completedAllDelete_完了済みタスク削除成功_リダイレクトされる() throws Exception {
        // Given
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // taskServiceのモック設定
        doReturn(2).when(taskService).completedAllDelete(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/completed_all_delete");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("2件の完了済みタスクの削除に成功しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(taskService, times(1)).completedAllDelete(any());
    }

/* ---------------------------
   showAccountInfo Method Test
   --------------------------- */

   @Test
   void showAccountInfo_未ログイン_リダイレクトされる() throws Exception {
        // Given
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/account");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
   }

   @Test
   @WithMockUser(username = "testUser")
   void showAccountInfo_ユーザー情報取得失敗_リダイレクトされる() throws Exception {
        // Given
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        doReturn(null).when(userService).getUserAccount(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/account");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("ユーザー情報の取得に失敗しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(userService, times(1)).getUserAccount(any());
   }

   @Test
   @WithMockUser(username = "testUser")
   void showAccountInfo_ユーザー情報取得成功_アカウント情報編集画面が表示される() throws Exception {
        // Given
        // Userの準備
        User user = new User();
        user.setUserId("testUserId");
        user.setUsername("testName");
        user.setCreationTime(LocalDateTime.of(2023, 01, 01, 0, 0, 0));
        user.setTelephone("09019287901");
        // UserFormの準備
        UserForm userForm = new UserForm();
        userForm.setUserId("testUserId");
        userForm.setUsername("testName");
        userForm.setTelephone("09019287901");
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        doReturn(user).when(userService).getUserAccount(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = get("/todo/account");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("account_info", modelAndView.getViewName());
        assertEquals(userForm, modelAndView.getModel().get("userForm"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(userService, times(1)).getUserAccount(any());
   }

/* -----------------------------
   updateAccountInfo Method Test
   ----------------------------- */

   @Test
   void updateAccountInfo_未ログイン_リダイレクトされる() throws Exception {
        // Given
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = post("/todo/account/update")
            .with(csrf());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("http://localhost/login", response.getRedirectedUrl());
        // メソッド実行回数の検証
        verify(userService, never()).getUsername(any());
   }

   @Test
   @WithMockUser(username = "testUser")
   void updateAccountInfo_バリデーションエラー_アカウント情報編集画面が表示される() throws Exception {
        // Given
        // UserFormの準備
        UserForm userForm = new UserForm();
        userForm.setUsername("TooLongTestName");
        userForm.setPassword("testPass");
        userForm.setTelephone("09020191031");
        // smartValidatorのモック設定
        doAnswer(invocation -> {
        BindingResult result = (BindingResult) invocation.getArgument(1);
        result.rejectValue("username", "Length", "length must be between 1 and 10"); // メッセージを指定
        return null;
    }).when(smartValidator).validate(any(), any());
        // uniqueTelephoneValidのモック設定
        doNothing().when(uniqueTelephoneValid).validate(any(), any());
        // passwordMatchValidのモック設定
        doNothing().when(passwordMatchValid).validate(any(), any());
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = post("/todo/account/update")
            .with(csrf())
            .flashAttr("userForm", userForm);

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        // ModelAndViewの検証
        ModelAndView modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("account_info", modelAndView.getViewName());
        // バリデーションエラーの検証
        BindingResult bindingResult = (BindingResult) modelAndView.getModel().get(BindingResult.MODEL_KEY_PREFIX + "userForm");
        assertNotNull(bindingResult);
        assertEquals(1, bindingResult.getErrorCount());
        // usernameフィールドの検証
        FieldError usernameFieldError = bindingResult.getFieldError("username");
        assertNotNull(usernameFieldError);
        assertEquals("length must be between 1 and 10", usernameFieldError.getDefaultMessage());
        // UserFormの検証
        UserForm actualUserForm = (UserForm) modelAndView.getModel().get("userForm");
        assertEquals("testUser", actualUserForm.getUserId());
        assertEquals("TooLongTestName", actualUserForm.getUsername());
        assertNull(actualUserForm.getPassword());
        assertEquals("09020191031", actualUserForm.getTelephone());
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(smartValidator, times(1)).validate(any(), any());
        verify(uniqueTelephoneValid, times(1)).validate(any(), any());
        verify(passwordMatchValid, times(1)).validate(any(), any());
    }

   @Test
   @WithMockUser(username = "testUser")
   void updateAccountInfo_アカウント情報更新失敗_リダイレクトされる() throws Exception {
        // Given
        // UserFormの準備
        UserForm userForm = new UserForm();
        userForm.setUsername("testName");
        userForm.setPassword("testPass");
        userForm.setTelephone("09020191031");
        // smartValidatorのモック設定
        doNothing().when(smartValidator).validate(any(), any());
        // uniqueTelephoneValidのモック設定
        doNothing().when(uniqueTelephoneValid).validate(any(), any());
        // passwordMatchValidのモック設定
        doNothing().when(passwordMatchValid).validate(any(), any());
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        doReturn(0).when(userService).update(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = post("/todo/account/update")
            .with(csrf())
            .flashAttr("userForm", userForm);

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo/account", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("アカウント情報の更新に失敗しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(userService, times(1)).update(any());
        verify(smartValidator, times(1)).validate(any(), any());
        verify(uniqueTelephoneValid, times(1)).validate(any(), any());
        verify(passwordMatchValid, times(1)).validate(any(), any());
   }

   @Test
   @WithMockUser(username = "testUser")
   void updateAccountInfo_アカウント情報更新成功_リダイレクトされる() throws Exception {
        // Given
        // UserFormの準備
        UserForm userForm = new UserForm();
        userForm.setUsername("testName");
        userForm.setPassword("testPass");
        userForm.setTelephone("09020191031");
        // smartValidatorのモック設定
        doNothing().when(smartValidator).validate(any(), any());
        // uniqueTelephoneValidのモック設定
        doNothing().when(uniqueTelephoneValid).validate(any(), any());
        // passwordMatchValidのモック設定
        doNothing().when(passwordMatchValid).validate(any(), any());
        // userServiceのモック設定
        doReturn("testUser").when(userService).getUsername(any());
        doReturn(1).when(userService).update(any());
        // リクエストの準備
        MockHttpServletRequestBuilder requestBuilder = post("/todo/account/update")
            .with(csrf())
            .flashAttr("userForm", userForm);

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();

        // Then
        // MockHttpServletResponseの検証
        MockHttpServletResponse response = mvcResult.getResponse();
        assertNotNull(response);
        assertEquals(302, response.getStatus());
        assertEquals("/todo/account", response.getRedirectedUrl());
        // FlashMapの検証
        FlashMap flashMap = mvcResult.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("アカウント情報の更新に成功しました", flashMap.get("alertMessage"));
        // メソッド実行回数の検証
        verify(userService, times(1)).getUsername(any());
        verify(userService, times(1)).update(any());
        verify(smartValidator, times(1)).validate(any(), any());
        verify(uniqueTelephoneValid, times(1)).validate(any(), any());
        verify(passwordMatchValid, times(1)).validate(any(), any());
   }
}
