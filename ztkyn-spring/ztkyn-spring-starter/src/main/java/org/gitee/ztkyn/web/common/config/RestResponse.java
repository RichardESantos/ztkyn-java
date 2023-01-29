package org.gitee.ztkyn.web.common.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author whty
 * @version 1.0
 * @description 实际使用场景中，并不是所有接口都需要统一格式。我们这里使用一个注解作为开关，按需控制接口返回格式。
 * @date 2023/1/29 11:24
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestResponse {

}
