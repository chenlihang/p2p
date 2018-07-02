package cn.wolfcode.p2p;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Created by wolfcode on 0008.
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:application-core.properties"),
        @PropertySource("classpath:msg.properties"),
        @PropertySource("classpath:email.properties")
})
@MapperScan({"cn.wolfcode.p2p.base.mapper","cn.wolfcode.p2p.bussiness.mapper"})
//@ServletComponentScan
public class CoreConfig {
}
