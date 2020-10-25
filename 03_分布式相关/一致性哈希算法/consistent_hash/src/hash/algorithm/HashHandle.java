package hash.algorithm;

import hash.node.Node;

/**
 * @author fangjiaxiaobai@gmail.com[公众号：方家小白]
 * @date 2020-06-03 15:46
 * @since 1.0.0
 */
@FunctionalInterface
public interface HashHandle<N extends Node> {

    Integer hash(N n);

}
