package com.ngtk5.todoapp.configs;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * Securityに関する設定クラス
 * アプリケーション起動時に１度実行される
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                // 非ログイン状態でstaticフォルダ内のcss,jsへアクセス可能
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // 非ログイン状態で/sign_up, /sign_up/save, /login*へアクセス可能
                .requestMatchers("/sign_up", "/sign_up/save", "/login*").permitAll()
                .anyRequest().authenticated()   // 上記以外は非ログイン状態でアクセス不可能
        ).formLogin(login -> login
                .loginProcessingUrl("/login") // リクエストされるとログイン認証を行うURL
                .loginPage("/login")   // ログインURL指定
                .defaultSuccessUrl("/todo", true) // ログイン後に遷移するURL
                .usernameParameter("userId") // ログインページで指定したユーザ名
                .passwordParameter("password")    // ログインページで指定したパスワード
                .failureForwardUrl("/login-fail") // ログイン失敗時に遷移するURL(最初のリクエストの内容が残る)
        ).logout(logout -> logout
                .logoutUrl("/login?logout")
                // ログアウト後にHTTPステータスコード200を返す
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .invalidateHttpSession(true).permitAll()    // ログアウト時にHTTPセッションを無効
        );
        return http.build();
    }

    /**
     * BCryptPasswordEncoderインスタンスを生成する。
     * @return インスタンス
     */
    @Bean
    protected PasswordEncoder PasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    /**
     * よくわからんけどこのメソッドがないとサーバーが起動できない。
     * テストではなくてもログアウト処理を行うことは可能だった。
     * Beanに指定するのが大切？
     */
    @Bean
    protected SecurityContextLogoutHandler SecurityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }
}
