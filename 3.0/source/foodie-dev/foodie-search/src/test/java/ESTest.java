import com.leosanqing.ESApplication;
import com.leosanqing.pojo.Stu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: leosanqing
 * @Date: 2020/3/3 下午11:37
 * @Package: PACKAGE_NAME
 * @Description: ES 测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ESApplication.class)
public class ESTest {
    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public void createIndex() {
        Stu stu = new Stu();
        stu.setStuId(10000L);
        stu.setAge(18);
        stu.setName("leosanqing");

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();

        template.index(indexQuery);
    }
}
