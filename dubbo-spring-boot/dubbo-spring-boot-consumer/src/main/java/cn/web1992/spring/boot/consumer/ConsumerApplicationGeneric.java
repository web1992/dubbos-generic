package cn.web1992.spring.boot.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @see DubboAnnotationConfig 配置在 中
 */
@SpringBootApplication
public class ConsumerApplicationGeneric {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ConsumerApplicationGeneric.class, args);
        // dubbo 应用配置
        ApplicationConfig applicationConfig = context.getBean(ApplicationConfig.class);
        // dubbo 注册中心配置
        RegistryConfig registryConfig = context.getBean(RegistryConfig.class);

        // 引用远程服务
        // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        // 弱类型接口名
        reference.setInterface("cn.web1992.dubbo.demo.DemoService");
        //reference.setVersion("1.0.0");
        // 声明为泛化接口
        reference.setGeneric(true);
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);

        // 用org.apache.dubbo.rpc.service.GenericService可以替代所有接口引用
        GenericService genericService = reference.get();

        // 基本类型以及Date,List,Map等不需要转换，直接调用
        Object result = genericService.$invoke("sayHello", new String[]{"java.lang.String"}, new Object[]{"world"});
        System.out.println(" user genericService >>> " + result);

        // 用Map表示POJO参数，如果返回值为POJO也将自动转成Map
        Map<String, Object> person = new HashMap<String, Object>();
        person.put("name", "name >>>>>>");
        // 如果返回POJO将自动转成Map
        result = genericService.$invoke("demo", new String[]{"cn.web1992.dubbo.demo.Demo"}, new Object[]{person});
        System.out.println(" user genericService POJO >>> " + result);

    }
}
