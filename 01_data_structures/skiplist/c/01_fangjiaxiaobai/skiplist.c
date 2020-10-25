//
// Created by wangxiyue on 2020/1/3.
//
#include "skiplist.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

/**
 * 创建一个跳表
 * @return 指向跳表的指针
 */
skiplist *create_skiplist() {

    /// 申请跳表结构内存
    skiplist *sl = (skiplist *) malloc(sizeof(skiplist));

    if (NULL == sl) {
        return NULL;
    }

    sl->max_level = 0;

    node *h = create_node(MAX_LEVEL - 1, 0, 0);

    if (NULL == h) {
        free(sl);
        return NULL;
    }

    sl->head = h;

    int i;

    for (i = 0; i < MAX_LEVEL; i++) {
        h->next[i] = NULL;
    }

    srand(time(0));

    return sl;
}

/**
 * 创建一个节点
 * @param level 节点的层
 * @param k 键
 * @param v 值
 * @return 节点的地址
 */
node *create_node(int level, Key k, Value v) {
    node *p = new_node(level);
    if (!p) {
        return NULL;
    }
    p->k = k;
    p->v = v;
    return p;
}

/**
 * 生成一个随机数
 * @return 一个随机数
 */
int random_level() {
    int level = 1;
    while (rand() % 2) {
        level++;
    }
    level = (level < MAX_LEVEL) ? level : MAX_LEVEL;
    return level;
}

/**
 * 插入一个节点
 *
 * one. 找到要插入的位置. 如果对应的key已经存在,直接更新值后，返回1；
 * two. 如果不存在, 那么随机生成层数. 设置跳表最大的层。
 * three. 创建一个新节点,从上到下,对每一层,像插入普通链表一样，插入数据。
 *
 * and done！only one. two. three. above, see the code!s
 *
 * @param sl 当前跳表
 * @param k 键
 * @param v 值
 * @return 返回是否插入成功
 */
int insert(skiplist *sl, Key k, Value v) {
    node * update[MAX_LEVEL];
    node * q = NULL, *p = sl->head;

    int i = sl->max_level - 1;

    /****** 找到对应的插入的位置 ****/
    for (; i >= 0; i--) {
        while ((q = p->next[i]) && q->k < k) {
            p = q;
        }
        update[i] = p;
    }
    /// 如果对应的节点已经存在.更新值。直接返回。
    if (q && q->k == k) {
        q->v = v;
        return 0;
    }

    /**************** 如果节点不存在的时候。****************/
    /// 产生一个随机层 level
    int level = random_level();

    if (level > sl->max_level) {
        for (i = sl->max_level; i < level; i++) {
            update[i] = sl->head;
        }
        sl->max_level = level;
    }

    /******** 3.从高层至下插入，与普通的链表插入一样 ****/

    q = create_node(level, k, v);
    if (!q) {
        /// 创建失败
        return 0;
    }

    for (i = level - 1; i >= 0; i--) {
        q->next[i] = update[i]->next[i];
        update[i]->next[i] = q;
    }
    ///节点插入成功。
    return 1;
}

/**
 * 删除
 *
 * one. 找到要删除的节点。
 * two. 对每一层,从上到下，删除当前节点.
 * @param sl
 * @param k
 * @return 是否删除成功, 删除成功,1,失败返回0
 */
int del(skiplist *sl, Key k) {

    node *update[MAX_LEVEL];

    node * q = NULL, *p = sl->head;

    /// 找到要删除的Key
    int i = sl->max_level - 1;

    for (; i >= 0; --i) {
        while ((q = p->next[i]) && q->k < k) {
            p = q;
        }
        update[i] = p;
    }


    /// 普通链表的删除。
    int return_flag = 0;
    for (i = sl->max_level - 1; i >= 0; i--) {
        if (q == update[i]->next[i]) {
            update[i]->next[i] = q->next[i];
            return_flag = 1;
            if (sl->head->next[i] == NULL) {
                sl->max_level--;
            }
        }
    }

    free(q);
    q = NULL;
    return return_flag;
}

/**
 * 根据key返回要查找的value
 * @param sl
 * @param k
 * @return
 */
Value *search(skiplist *sl, Key k) {
    node * q, *p = sl->head;
    q = NULL;

    int i = sl->max_level - 1;

    for (; i >= 0; i--) {
        while ((q = p->next[i]) && q->k < k) {
            p = q;
        }
        if (q && k == q->k) {
            return &(q->v);
        }
    }
    return NULL;
}

/**
 * 销毁链表
 * @param sl 链表
 */
void free_skiplist(skiplist *sl) {

    int level = sl->max_level;

    node *p=sl->head;
    node *next;

    for (; level >= 0; level--) {
        while(p){
            next = p->next[level];
            free(p);
            p = next;
        }
    }

    free(sl);


}

/**
 * 从上到下打印跳表
 * @param sl 跳表的首地址
 */
void print_sl(skiplist *sl) {
    node *q;
    int i = sl->max_level - 1;
    printf("\n");
    for (; i >= 0; --i) {
        q = sl->head->next[i];
        printf("level %d:\t", i + 1);
        while (q) {
            printf("(%d,%d)\t", q->k, q->v);
            q = q->next[i];
        }
        printf("\n");
    }
    printf("\n");
}


