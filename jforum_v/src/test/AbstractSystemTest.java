package test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-13 下午7:15:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-*.xml"})
public abstract class AbstractSystemTest extends AbstractJUnit4SpringContextTests {
//public abstract class AbstractSystemTest extends AbstractTransactionalJUnit4SpringContextTests {
// 使用 AbstractTransactionalJUnit4SpringContextTests 这个 测试方法完毕后会回滚,清洁数据
}
