package site.wetsion.framework.baton.datasource;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 4:53 PM
 **/
public interface Store<T> {

    void save(T data);
}
