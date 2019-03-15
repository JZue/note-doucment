package context.annotation;

/**
 * @Author: junzexue
 * @Date: 2019/3/7 上午10:57
 * @Description:
 **/
public interface AnnotationConfigRegistry {

    void register(Class ...var1);

    void scan(String ...var1);
}
