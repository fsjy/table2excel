package com.excel.beanProcessor;

import com.excel.tag.Tag;
import com.excel.tag.meta.TagAttr;
import com.excel.tag.meta.TagEntity;
import com.excel.tag.TagRepository;
import com.excel.tag.meta.TagRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class TagRepoProcessor implements BeanPostProcessor {


    private ConfigurableListableBeanFactory configurableListableBeanFactory;
    private static Logger log = LoggerFactory.getLogger(TagRepoProcessor.class);

    @Autowired
    public TagRepoProcessor(ConfigurableListableBeanFactory beanFactory) {
        this.configurableListableBeanFactory = beanFactory;
    }


    /**
     * Apply this BeanPostProcessor to the given new bean instance <i>before</i> any bean
     * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
     * or a custom init-method). The bean will already be populated with property values.
     * The returned bean instance may be a wrapper around the original.
     *
     * @param bean     the new bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one;
     * if {@code null}, no subsequent BeanPostProcessors will be invoked
     * @throws BeansException in case of errors
     * @see InitializingBean#afterPropertiesSet
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {


        if (bean.getClass().getAnnotation(TagEntity.class) != null) {
//            if (tags == null) {
//                tags = new ArrayList<>();
//
//                log.debug("TagRepoProcessor#tags new instance.");
//            }
//            tags.add((Tag) bean);
//
//            log.debug("Add tag {} to tags.", bean.getClass().getSimpleName());

            if (TagRepository.Tags == null) {
                TagRepository.Tags = new ArrayList<>();
            }

            TagRepository.Tags.add((Tag) bean);
        }


        ReflectionUtils.doWithFields(bean.getClass(), field -> {

            if (field.getAnnotation(TagAttr.class) != null) {

                if (TagRepository.AttrMaps == null) {
                    TagRepository.AttrMaps = new HashMap<>();
                }

                if (TagRepository.AttrMaps.get(((Tag) bean).getHtmlTagStart()) == null) {
                    List<String> list = new ArrayList<>();
                    list.add(field.getName());
                    TagRepository.AttrMaps.put(((Tag) bean).getHtmlTagStart(), list);
                } else {
                    TagRepository.AttrMaps.get(((Tag) bean).getHtmlTagStart()).add(field.getName());
                }
            }
        });

        return bean;
    }

    /**
     * Apply this BeanPostProcessor to the given new bean instance <i>after</i> any bean
     * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
     * or a custom init-method). The bean will already be populated with property values.
     * The returned bean instance may be a wrapper around the original.
     * <p>In case of a FactoryBean, this callback will be invoked for both the FactoryBean
     * instance and the objects created by the FactoryBean (as of Spring 2.0). The
     * post-processor can decide whether to apply to either the FactoryBean or created
     * objects or both through corresponding {@code bean instanceof FactoryBean} checks.
     * <p>This callback will also be invoked after a short-circuiting triggered by a
     * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation} method,
     * in contrast to all other BeanPostProcessor callbacks.
     *
     * @param bean     the new bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one;
     * if {@code null}, no subsequent BeanPostProcessors will be invoked
     * @throws BeansException in case of errors
     * @see InitializingBean#afterPropertiesSet
     * @see FactoryBean
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        return bean;
    }

    {

    }
}
