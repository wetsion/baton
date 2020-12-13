package site.wetsion.framework.baton.common.annotation;

import java.lang.annotation.*;

/**
 * service扩展实现类
 * <p>{@link SPI} 标注的接口的实现类上实现，用于标记简称</p>
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 5:32 PM
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SXIC {

    String value();

    boolean selected() default false;
}
