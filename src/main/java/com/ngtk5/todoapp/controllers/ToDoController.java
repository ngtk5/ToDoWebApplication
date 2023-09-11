package com.ngtk5.todoapp.controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ngtk5.todoapp.beans.Task;
import com.ngtk5.todoapp.beans.User;
import com.ngtk5.todoapp.forms.NewTaskForm;
import com.ngtk5.todoapp.forms.SignUpUserForm;
import com.ngtk5.todoapp.forms.TaskForm;
import com.ngtk5.todoapp.forms.UserForm;
import com.ngtk5.todoapp.services.TaskService;
import com.ngtk5.todoapp.services.UserService;
import com.ngtk5.todoapp.validations.UniqueTelephoneValid;
import com.ngtk5.todoapp.validations.UniqueUserIdValid;
import com.ngtk5.todoapp.validations.PasswordMatchValid;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j  // ログ部品を使えるようにする
public class ToDoController {

    @NonNull
    private final TaskService taskService;

    @NonNull
    private final UserService userService;

    // 電話番号用バリデーション
    @NonNull
    private final UniqueTelephoneValid uniqueTelephoneValid;

    // パスワード用バリデーション
    @NonNull
    private final PasswordMatchValid passwordMatchValid;

    // ログアウトハンドラ
    @NonNull
    private final SecurityContextLogoutHandler logoutHandler;

    // 任意のタイミングでバリデーションチェックを実行するためのvalidator
    @NonNull
    private final SmartValidator smartValidator;

    @NonNull
    private final UniqueUserIdValid uniqueUserIdValid;

    /**
    * signUpUserFormに自作バリデーション付与
    * POSTリクエストの場合のみカスタムバリデーションを有効にする
    */
    @InitBinder("signUpUserForm")
    public void initSignUpUserFormBinder(WebDataBinder binder, HttpServletRequest request) {
        if (Objects.equals("POST", request.getMethod())) {
            binder.addValidators(this.uniqueTelephoneValid, this.uniqueUserIdValid);
            log.info("SignUpUserFormValidationの共通処理");
        }
    }

    /**
     * ログイン画面の表示
     * @param userId 入力されていたユーザID(HTTPリクエストに入っているユーザーID)
     * @param exception ログインエラー(HTTPリクエストに入っているSPRING_SECURITY_LAST_EXCEPTIONの例外)
     * @param model Model
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @return 遷移先
     */
    @RequestMapping("/login*")
    public String showLoginForm(
        @ModelAttribute(name="userId") String userId,
        @RequestAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) Exception exception,
        Model model,
        HttpServletRequest httpServletRequest
    ) {
        String loginUsername = httpServletRequest.getRemoteUser();
        // ログイン中なら
        if (Objects.nonNull(loginUsername)) {
            log.info("※注:{}がログイン中に/loginにアクセスしました。/todoにリダイレクトします。", loginUsername);
            return "redirect:/todo";
        }

        // ログインエラーがあるなら
		if (Objects.nonNull(exception)) {
            // エラーメッセージを格納
			model.addAttribute("alertMessage", exception.getMessage());
		}
        return "login";
    }

    /**
     * ログアウト処理をする
     * @param redirectAttributes リダイレクト先にパラメータを渡すFlash Scope(Modelに追加される)
     * @param authentication 
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @param httpServletResponse サービス処理本体で受け取るサーバからクライアントへ応答するときに必要なレスポンス情報が格納されているオブジェクト
     * @return 遷移先
     */
    @RequestMapping("/logout")
    public String logout(
        RedirectAttributes redirectAttributes,
        Authentication authentication,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse
    ) {
        redirectAttributes.addFlashAttribute("alertMessage", "ログアウトに成功しました。");
        // ログアウト処理
        this.logoutHandler.logout(httpServletRequest, httpServletResponse, authentication);
        return "redirect:/login";
    }

    /**
     * 新規ユーザ登録画面の表示
     * @param signUpUserForm 入力フォーム
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @return 遷移先
     */
    @GetMapping("/sign_up")
    public String showSignUpForm(@ModelAttribute(name="signUpUserForm") SignUpUserForm signUpUserForm, HttpServletRequest httpServletRequest) {
        // ログイン中なら
        if (Objects.nonNull(httpServletRequest.getRemoteUser())) {
            log.info("※注:{}がログイン中に/sign_upにアクセスしました。/todoにリダイレクトします。", httpServletRequest.getRemoteUser());
            return "redirect:/todo";
        }
        return "sign_up";
    }

    /**
     * ユーザ情報を登録する
     * @param signUpUserForm 入力内容
     * @param bindingResult signUpUserFormのバインド結果を表す
     * @param redirectAttributes リダイレクト先にパラメータを渡すFlash Scope(Modelに追加される)
     * @return 遷移先
     */
    @PostMapping("/sign_up")
    public String addUser(
        @Validated @ModelAttribute(name="signUpUserForm") SignUpUserForm signUpUserForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        HttpServletRequest httpServletRequest
    ) {
        // ログイン中なら
        if (Objects.nonNull(httpServletRequest.getRemoteUser())) {
            log.info("※注:{}がログイン中に/sign_upにアクセスしました。/todoにリダイレクトします。", httpServletRequest.getRemoteUser());
            return "redirect:/todo";
        }

        // 入力チェックエラーがある場合
        if (bindingResult.hasErrors()) {
            log.info("バインドエラー!");
            signUpUserForm.setPassword(null);
            signUpUserForm.setConfirmPassword(null);
            return "sign_up";
        }
        User user = new User();
        // 入力内容を詰め替える
        BeanUtils.copyProperties(signUpUserForm, user);
        log.info("{}", user);
        // ユーザ情報をを登録する
        int cnt = this.userService.add(user);
        if (cnt > 0) {
            redirectAttributes.addFlashAttribute("alertMessage", "新規登録に成功しました。");
        } else if (Objects.equals(cnt, 0)) {
            redirectAttributes.addFlashAttribute("alertMessage", "新規登録に失敗しました。");
        }
        log.info("{}件のユーザ情報の登録", cnt);
        return "redirect:/login";
    }

    /**
     * タスク一覧画面の表示
     * @param model Model
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @return 遷移先
     */
    @GetMapping("/todo")
    public String showTodoList(Model model, HttpServletRequest httpServletRequest) {
        // ソートID取得
        String sortId = String.valueOf(model.getAttribute("sortId"));
        // タスク情報一覧を取得
        List<Task> tasks = this.taskService.sortTaskList(sortId, httpServletRequest.getRemoteUser());
        // Modelに"tasks"として追加
        model.addAttribute("tasks", tasks);
        return "todo_list";
    }

    /**
     * ソートIDをパラメータに追加して/todoにリダイレクト
     * @param sortId ソートID
     * @param redirectAttributes リダイレクト先にパラメータを渡すFlashScope(Modelに追加される)
     * @return 遷移先
     */
    @GetMapping("/todo/sort/{sortId}")
    public String setSortId(
        @PathVariable("sortId") String sortId,
        RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("sortId", sortId);
        return "redirect:/todo";
    }

    /**
     * 新規作成画面の表示
     * @param model Model
     * @return 遷移先
     */
    @GetMapping("/todo/create")
    public String initCreateTaskForm(Model model) {
        // NewTaskFormセット
        model.addAttribute("newTaskForm", new NewTaskForm());
        log.info("{}", model.getAttribute("currentLoginUsername"));
        return "create_task";
    }

    /**
     * タスク情報を登録する
     * @param taskForm 入力内容
     * @param bindingResult taskFormのバインド結果を表す
     * @param model Model
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @param redirectAttributes リダイレクト先にパラメータを渡すFlash Scope(Modelに追加される)
     * @return 遷移先
     */
    @PostMapping("/todo/create")
    public String addTaskForm(
        @Validated @ModelAttribute(name="newTaskForm") NewTaskForm newTaskForm,
        BindingResult bindingResult,
        Model model,
        HttpServletRequest httpServletRequest,
        RedirectAttributes redirectAttributes
    ) {
        // 入力チェックエラーがある場合
        if (bindingResult.hasErrors()) {
            log.info("バインドエラー!");
            return "create_task";
        }

        // 入力内容を詰め替える
        Task task = new Task();
        BeanUtils.copyProperties(newTaskForm, task);

        // 必要な情報をセット(ユーザID, タスクID)
        task.setUserId(httpServletRequest.getRemoteUser());
        task.setTaskId(this.taskService.createNewTaskId());

        // タスク情報を登録
        int cnt = this.taskService.add(task);
        log.info("{}件のタスク情報の登録", cnt);

        // メッセージ変数をセット
        redirectAttributes.addFlashAttribute("alertMessage", "タスク情報を登録しました。");

        // リダイレクト(一覧に遷移)
        return "redirect:/todo";
    }

    /**
     * 編集画面の表示
     * @param taskId タスクID
     * @param taskForm 入力内容
     * @return 遷移先
     */
    @GetMapping("/todo/edit/{taskId}")
    public String initEditTaskForm(
        @PathVariable("taskId") String taskId,
        @ModelAttribute(name="taskForm", binding=false) TaskForm taskForm,
        RedirectAttributes redirectAttributes,
        HttpServletRequest httpServletRequest
    ) {
        // タスクID,ユーザIDを条件にタスク情報取得
        Task task = this.taskService.getTask(taskId, httpServletRequest.getRemoteUser());

        // タスク情報が存在しない場合
        if (Objects.isNull(task)) {
            log.info("{}は存在しません", taskId);
            redirectAttributes.addFlashAttribute("alertMessage", "タスクが存在しないか、取得に失敗しました");
            return "redirect:/todo";
        }

        // 検索結果を入力内容に詰め替える
        BeanUtils.copyProperties(task, taskForm);
        return "edit_task";
    }

    /**
     * タスク情報を更新する
     * @param taskId タスクID
     * @param taskForm 入力内容
     * @param bindingResult taskFormのバインド結果を表す
     * @param model Model
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @param redirectAttributes リダイレクト先にパラメータを渡すFlash Scope(Modelに追加される)
     * @return 遷移先
     */
    @PostMapping("/todo/edit/{taskId}")
    public String updateTask(
        @PathVariable("taskId") String taskId,
        @Validated @ModelAttribute(name="taskForm") TaskForm taskForm,
        BindingResult bindingResult,
        Model model,
        HttpServletRequest httpServletRequest,
        RedirectAttributes redirectAttributes
    ) {
        // 入力チェックエラーがある場合
        if (bindingResult.hasErrors()) {
            log.info("バインドエラー!");
            return "edit_task";
        }
        // 入力内容を詰め替える
        Task task = new Task();
        BeanUtils.copyProperties(taskForm, task);
        // 必要な情報をセット(ユーザID)
        task.setUserId(httpServletRequest.getRemoteUser());
        // taskテーブルの同じtaskIdの情報を更新する
        int cnt = this.taskService.update(task);
        if (Objects.equals(cnt, 1)) {
            // 1件の更新に成功
            redirectAttributes.addFlashAttribute("alertMessage", "タスク情報の更新に成功しました");
            log.info("{}件のタスク情報の更新", cnt);
        } else if (Objects.equals(cnt, 0)) {
            // 更新失敗
            redirectAttributes.addFlashAttribute("alertMessage", "タスク情報の更新に失敗しました");
            log.info("更新失敗");
        }
        // リダイレクト(一覧に遷移)
        return "redirect:/todo";
    }

    /**
     * タスクIDを元にタスク情報を削除する
     * @param taskId タスクID
     * @param redirectAttributes リダイレクト先にパラメータを渡すFlash Scope(Modelに追加される)
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @return 遷移先
     */
    @GetMapping("/todo/delete/{taskId}")
    public String deleteTask(@PathVariable("taskId") String taskId, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        // タスク情報を削除する
        int cnt = this.taskService.delete(taskId, httpServletRequest.getRemoteUser());
        log.info("{}件のタスク情報の削除", cnt);
        if (Objects.equals(cnt, 1)) {
            // 1件の削除に成功
            redirectAttributes.addFlashAttribute("alertMessage", "タスク情報の削除に成功しました");
        } else if (Objects.equals(cnt, 0)) {
            // 削除失敗
            redirectAttributes.addFlashAttribute("alertMessage", "タスク情報の削除に失敗しました");
        }
        // リダイレクト(一覧に遷移)
        return "redirect:/todo";
    }

    /**
     * 完了済みタスク情報をすべて削除する
     * @param redirectAttributes リダイレクト先にパラメータを渡すFlash Scope(Modelに追加される)
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @return 遷移先
     */
    @GetMapping("/todo/completed_all_delete")
    public String completedAllDelete(RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        // taskテーブルの完了済みタスクの情報を全て削除する
        int cnt = this.taskService.completedAllDelete(httpServletRequest.getRemoteUser());
        if (cnt > 0) {
            // 削除成功
            redirectAttributes.addFlashAttribute("alertMessage", cnt+"件の完了済みタスクの削除に成功しました");
            log.info("{}件のタスク情報の削除", cnt);
        } else if (Objects.equals(cnt, 0)) {
            // 削除失敗
            redirectAttributes.addFlashAttribute("alertMessage", "完了済みタスクの削除に失敗しました");
        }
        log.info("{}件のタスク情報の削除", cnt);
        // リダイレクト(一覧に遷移)
        return "redirect:/todo";
    }

    /**
     * アカウント情報編集画面を表示する
     * @param model Model
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @return 遷移先
     */
    @GetMapping("/todo/account")
    public String showAccountInfo(Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        User user = this.userService.getUserAccount(httpServletRequest.getRemoteUser());
        if (Objects.isNull(user)) {
            redirectAttributes.addFlashAttribute("alertMessage", "ユーザー情報の取得に失敗しました");
            log.info("ユーザー情報の取得に失敗");
            return "redirect:/todo";
        }
        // 入力内容を詰め替える
        UserForm userForm = new UserForm();
        BeanUtils.copyProperties(user, userForm);
        model.addAttribute("userForm", userForm);
        return "account_info";
    }

    /**
     * アカウント情報を更新する
     * @param model Model
     * @param userForm 入力内容
     * @param bindingResult userFormのバインド結果を表す
     * @param httpServletRequest サービス処理本体が受け取るクライアントからサーバに送られたリクエスト情報が格納されているオブジェクト
     * @param redirectAttributes リダイレクト先にパラメータを渡すFlash Scope(Modelに追加される)
     * @return 遷移先
     */
    @PostMapping("/todo/account/update")
    public String updateAccountInfo(
        Model model,
        @ModelAttribute(name="userForm") UserForm userForm,
        BindingResult bindingResult,
        HttpServletRequest httpServletRequest,
        RedirectAttributes redirectAttributes
    ) {
        userForm.setUserId(httpServletRequest.getRemoteUser()); // userIdをセット
        // バリデーションチェック
        this.smartValidator.validate(userForm, bindingResult);
        this.uniqueTelephoneValid.validate(userForm, bindingResult);
        this.passwordMatchValid.validate(userForm, bindingResult);
        // 入力チェックエラーがある場合
        if (bindingResult.hasErrors()) {
            log.info("バインドエラー!");
            userForm.setPassword(null);
            return "account_info";
        }
        // 入力内容を詰め替える
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        int cnt = this.userService.update(user); // アカウント情報を更新
        if (Objects.equals(cnt, 1)) {
            // 1件の更新に成功
            redirectAttributes.addFlashAttribute("alertMessage", "アカウント情報の更新に成功しました");
            log.info("{}件のアカウント情報の更新", cnt);
        } else if (Objects.equals(cnt, 0)) {
            // 更新失敗
            redirectAttributes.addFlashAttribute("alertMessage", "アカウント情報の更新に失敗しました");
        }
        return "redirect:/todo/account";
    }
}
