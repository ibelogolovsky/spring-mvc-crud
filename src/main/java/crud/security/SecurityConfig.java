package crud.security;

import crud.handler.LoginSuccessHandler;
import crud.model.Role;
import crud.model.User;
import crud.service.RoleService;
import crud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.Set;

@Configuration
@EnableWebSecurity(debug = true)
@ComponentScan("crud")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(SecurityConfig.class.getName());
    private final UserService userService;

    public SecurityConfig(UserService userService, RoleService roleService) {
        this.userService = userService;
        Role rUser = new Role("ROLE_USER");
//        roleService.save(rUser);
//        Role rAdmin = new Role("ROLE_ADMIN");
//        roleService.save(rAdmin);
        User jsmith = new User("jsmith", "1234",
                "John", "Smith", "jsmith@gmail.com");
        jsmith.addRole(rUser);
        userService.save(jsmith);
        User admin = new User("admin", "admin",
                "Igor", "Belogolovsky", "ibelogolovsky@gmail.com");
//        admin.addRole(rUser);
//        admin.addRole(rAdmin);
//        userService.save(admin);
//        userService.addRole();
//        jsmith.getRoles().add(rUser);
//        userService.save(jsmith);
//        admin.getRoles().addAll(Arrays.asList(rUser, rAdmin));
//        userService.addRole(jsmith, rUser);
        userService.addRole(admin, "ROLE_ADMIN");
        userService.addRole(admin, "ROLE_USER");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("ADMIN").password("ADMIN").roles("ADMIN");
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and().formLogin()
                .successHandler(new LoginSuccessHandler())
                .and()
                .logout().logoutSuccessUrl("/")
                ;
        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login?logout")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable();

        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
                //страницы аутентификаци доступна всем
                .antMatchers("/login").anonymous()
                // защищенные URL
                .antMatchers("/hello").access("hasAnyRole('ADMIN')").anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
