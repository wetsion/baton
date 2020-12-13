package site.wetsion.framework.baton.common.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 5:26 PM
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * 缺省扩展点名。
     */
    String value() default "";
}
