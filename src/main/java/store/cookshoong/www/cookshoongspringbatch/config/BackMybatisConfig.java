package store.cookshoong.www.cookshoongspringbatch.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Mybatis 설정 Config.
 *
 * @author seungyeon (유승연)
 * @since 2023.07.30
 */
@Configuration
@MapperScan(basePackages = "**.mapper.**", sqlSessionFactoryRef = "sqlSessionFactory")
public class BackMybatisConfig extends DefaultBatchConfigurer {

    /**
     * SqlSessionFactory 객체를 생성하여 DataSource 의존성 주입 해준다.
     * Resource에 sql을 입력한 xml파일 위치 정보 입력.
     *
     * @param dataSource the data source
     * @return the sql session factory
     * @throws Exception the exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:**/maps/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }
}
