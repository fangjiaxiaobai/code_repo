//
// Created by wangxiyue on 2020/1/4.
//
#include "skiplist.h"
#include <stdio.h>
#include <stdlib.h>

int main() {

    skiplist *sl = create_skiplist();
    if (sl == NULL) {
        printf("创建 skiplist 失败");
        exit(0);
    }
    insert(sl, 1, 1);
    insert(sl, 2, 2);
    insert(sl, 3, 3);
    insert(sl, 4, 4);
    insert(sl, 5, 5);

    print_sl(sl);
    del(sl, 2);
    print_sl(sl);
    insert(sl, 3, 33);
    print_sl(sl);
    insert(sl, 2, 2);
    insert(sl, 20, 20);
    insert(sl, 14, 14);
    insert(sl, 15, 15);
    print_sl(sl);

    /// 查找
    Value *v = search(sl, 15);
    printf("Key = 15 , Value= %d", *v);

    /// 释放跳表
    free_skiplist(sl);

    return 0;
}