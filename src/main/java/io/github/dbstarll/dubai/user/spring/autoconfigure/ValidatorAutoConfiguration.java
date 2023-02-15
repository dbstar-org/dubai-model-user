package io.github.dbstarll.dubai.user.spring.autoconfigure;

import io.github.dbstarll.dubai.user.utils.PasswordProperties;
import io.github.dbstarll.dubai.user.utils.PasswordValidator;
import io.github.dbstarll.dubai.user.utils.UsernameProperties;
import io.github.dbstarll.dubai.user.utils.UsernameValidator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({UsernameProperties.class, PasswordProperties.class})
public class ValidatorAutoConfiguration {
    /**
     * 注入UsernameValidator实例，用于校验用户名是否合规.
     *
     * @param usernameProperties 用户名校验规则
     * @return 用户名校验器实例
     */
    @Bean
    @ConditionalOnMissingBean(UsernameValidator.class)
    UsernameValidator usernameValidator(final UsernameProperties usernameProperties) {
        return new UsernameValidator(usernameProperties);
    }

    /**
     * 注入PasswordValidator实例，用于校验密码是否合规.
     *
     * @param passwordProperties 密码校验规则
     * @return 密码校验器实例
     */
    @Bean
    @ConditionalOnMissingBean(PasswordValidator.class)
    PasswordValidator passwordValidator(final PasswordProperties passwordProperties) {
        return new PasswordValidator(passwordProperties);
    }
}
