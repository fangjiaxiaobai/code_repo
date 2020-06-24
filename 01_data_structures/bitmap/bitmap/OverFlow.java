package bitmap;

/**
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-22 16:49
 * @since 1.0.0
 */
public enum OverFlow {

    /**
     * 默认，循环设置
     */
    WRAP,
    /**
     * 中断饱和
     */
    SAT,
    /**
     * 设置失败
     */
    FAIL;

}
