package com.fxb.learn.skiplist01;

import java.util.Random;

/**
 * 跳表
 * 1. 排序的顺序.
 * 目前只能是从小到大排序。
 * 2. 跳表的层数和数据量的最优解。
 * 即 数据量和层数的系数比.或者说映射关系.
 * 3. 双向的跳表的实现?
 * 4. 增加跨度字段?
 *
 * @author fangjiaxiaobai@gmail.com
 * @date 2020-01-05
 * @since 1.0.0
 */
public class SkipList<K extends Comparable<K>, V> {

    private final static int MAX_LEVEL = 1 << 4;

    /**
     * 头结点
     */
    public Node<K, V> head;

    /**
     * 最大层数
     */
    private int maxLevel = 1;

    public SkipList() {
        head = new Node<>();
    }

    public SkipList(K key, V value) {
        this.head = new Node<>();
        insert(key, value);
    }

    public SkipList(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    /**
     * 生成一个随机的层数
     *
     * @return 层数
     */
    private int randLevel() {
        int level = new Random().nextInt(MAX_LEVEL) + 1;
//        System.out.println("生成的层数为: " + level);
        return level;
    }

    /**
     * 插入元素
     *
     * @param key   Key
     * @param value Value
     * @return 是否插入成功
     */
    public boolean insert(K key, V value) {
        /// one. 找到带插入位置
        /// 如果 key 相等, 则更新值.
        /// 如果遇到大于key的Node, 找到前一个元素,进行插入.
        Node<K, V> q = null, p = this.head;

        /// 生成一个新节点的层.
        int newNodeLevel = randLevel();

        /// 带插入节点的每一层的前继节点
        Node<K, V>[] previousNodes = new Node[this.maxLevel];
        int level = this.maxLevel - 1;
        for (; level >= 0; level--) {
            while (null != (q = p.next[level]) && q.compareKey(key) < 0) {
                p = q;
            }
            previousNodes[level] = p;
        }

        /// key 相等, 更新value, 直接返回。
        if (null != q && q.compareKey(key) == 0) {
            q.value = value;
            return true;
        }

        // two. 生成一个新的Node
        Node<K, V> newNode = new Node<>(key, value, newNodeLevel);

        // three. 对于每一层, 像普通链表一样插入新节点。
        int l = newNodeLevel - 1;

        if (newNodeLevel >= this.maxLevel) {
            for (; l >= this.maxLevel; l--) {
                this.head.next[l] = newNode;
            }
            this.maxLevel = newNodeLevel;
        }

        for (; l >= 0; l--) {
            newNode.next[l] = previousNodes[l].next[l];
            previousNodes[l].next[l] = newNode;
        }
        return true;
    }

    /**
     * 删除指定的key的元素
     *
     * @param key Key
     * @return 对应的值, 不存在返回 null;
     */
    public V delete(K key) {

        Node<K, V> q = null, p = this.head;
        Node<K, V>[] previousNodes = new Node[this.maxLevel];
        for (int level = this.maxLevel - 1; level >= 0; level--) {
            while (null != (q = p.next[level]) && q.compareKey(key) < 0) {
                p = q;
            }
            previousNodes[level] = p;
        }

        if (null == q || q.compareKey(key) > 0) {
            return null;
        }

        V value = q.value;

        for (int i = previousNodes.length - 1; i >= 0; i--) {
            if (q == previousNodes[i].next[i]) {
                previousNodes[i].next[i] = q.next[i];
                if (this.head.next[i] == null) {
                    this.maxLevel--;
                }
            }
        }

        return value;
    }

    /**
     * 查找
     *
     * @param key Key
     * @return 存在, 返回value，不存在,返回null。
     */
    public V search(K key) {
        Node<K, V> q = null, p = this.head;
        Node<K, V>[] previousNodes = new Node[this.maxLevel];
        for (int level = this.maxLevel - 1; level >= 0; level--) {
            while (null != (q = p.next[level]) && q.compareKey(key) < 0) {
                p = q;
            }
            previousNodes[level] = p;
        }

        if (null == q || q.compareKey(key) > 0) {
            return null;
        }

        V value = q.value;
        return value;
    }

    /**
     * 打印生成的跳表
     */
    public void show() {
        System.out.println("-------------------------");
        for (int level = this.maxLevel - 1; level >= 0; level--) {
            System.out.printf("level: %d\t", level);
            Node<K, V> q = this.head.next[level];
            while (null != q) {
                System.out.print("(" + q.key + "," + q.value + ")\t");
                q = q.next[level];
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    /**
     * 跳表节点
     *
     * @param <K> Key
     * @param <V> Value
     */
    static class Node<K extends Comparable<K>, V> {
        K key;

        V value;

        Node<K, V>[] next;

        private Node() {
            this.next = new Node[MAX_LEVEL];
        }

        public Node(K key, V value, int level) {
            if (key == null || value == null) {
                throw new IllegalArgumentException("Key and Value can not be null.");
            }
            this.key = key;
            this.value = value;
            this.next = new Node[level];
        }

        public int compareKey(K k1) {
            return this.key.compareTo(k1);
        }
    }


}
