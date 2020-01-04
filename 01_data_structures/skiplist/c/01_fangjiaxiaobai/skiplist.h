//
// Created by wangxiyue on 2020/1/3.
//

#ifndef DEMO03_DATA_STRACTURE_SKIPLIST_H
#define DEMO03_DATA_STRACTURE_SKIPLIST_H

#define new_node(n)((node*)malloc(sizeof(node)+n*sizeof(node*)))
#define MAX_LEVEL 10

typedef int Key;
typedef int Value;

/**
 * 跳表的一个节点
 */
typedef struct node {

    /// K值
    Key k;

    /// value值
    Value v;

    ///后继指针数组，柔性数组 可实现结构体的变长
    struct node *next[1];

} node;

/**
 * 表示一张跳表
 */
typedef struct skiplist {

    /// 最大的层数
    int max_level;

    /// 指向头节点
    node *head;

} skiplist;

/****************skiplist functions***********************/

/**
 * 创建一个跳跃表
 * @return 首地址
 */
skiplist *create_skiplist();

/**
 * 创建一个 skipList的节点
 * @param level 层
 * @param k Key值
 * @param v Value值
 * @return 创建后的节点的位置
 */
node *create_node(int level, Key k, Value v);

/**
 * 插入元素
 * @param sl 跳跃表的地址
 * @param k 键值
 * @param v Value值
 * @return 是否创建成功
 */
int insert(skiplist *sl, Key k, Value v);

/**
 * 删除
 * @param sl
 * @param k
 * @return 是否删除成功
 */
int del(skiplist *sl, Key k);

/**
 * 根据key返回要查找的value
 * @param sl
 * @param k
 * @return
 */
Value *search(skiplist *sl, Key k);

/**
 * 销毁链表
 * @param sl 链表
 */
void free_skiplist(skiplist *sl);


/**
 * 从上到下打印跳表
 * @param sl 跳表的首地址
 */
void print_sl(skiplist *sl);

#endif
