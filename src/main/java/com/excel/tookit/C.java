package com.excel.tookit;

import com.excel.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 一切为了从简，不想看到那么长的代码
 *
 * @author darcula
 */
public class C {

    private AnnotationConfigApplicationContext context;

    // 懒汉模式
    private static C c = new C();

    private C() {

    }

    public static C get() {

        if (c.context == null) {
            c.init();
        }
        return c;
    }

    /**
     * 扫描开始
     */
    private void init() {
        this.context = new AnnotationConfigApplicationContext(SpringConfig.class);

    }

    public <T> T g(Class<T> t) {
        return this.context.getBean(t);
    }

    /**
     * 销毁context
     */
    public void d() {
        this.context.destroy();
    }
}
