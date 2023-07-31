package store.cookshoong.www.cookshoongspringbatch.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.07.30
 */
@Configuration
@MapperScan(basePackages = "store.cookshoong.www.cookshoongspringbatch.status.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisBatchConfig extends DefaultBatchConfigurer {
    /**
     * Sql session factory sql session factory.
     *
     * @param dataSource the data source
     * @return the sql session factory
     * @throws Exception the exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        return sqlSessionFactoryBean.getObject();
    }
}
