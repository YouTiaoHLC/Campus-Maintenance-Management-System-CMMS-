class Node {
    int value;
    Node next;

    Node(int value) {
        this.value = value;
    }
}

public class MergeListTest {

    // 你的融合方法
    static Node[] insert(Node n1, Node n2){
        Node[] res = new Node[2];
        Node cur1 = n1, cur2 = n2;

        while(cur1 != null && cur2 != null){
            Node temp1 = cur1.next, temp2 = cur2.next;
            cur1.next = cur2;
            cur2.next = temp1;
            cur1 = temp1;
            cur2 = temp2;
        }

        res[0] = n1;
        res[1] = cur2;
        return res;
    }

    // 创建链表
    static Node createList(int[] values) {
        if (values == null || values.length == 0) return null;
        Node head = new Node(values[0]);
        Node cur = head;
        for (int i = 1; i < values.length; i++) {
            cur.next = new Node(values[i]);
            cur = cur.next;
        }
        return head;
    }

    // 创建列表用于视觉区分
    static Node createListA(int[] values) {
        return createList(values);
    }

    static Node createListB(int[] values) {
        return createList(values);
    }

    // 打印链表
    static String listToString(Node head) {
        if (head == null) return "null";
        StringBuilder sb = new StringBuilder();
        Node cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(" → ");
            cur = cur.next;
        }
        return sb.toString();
    }

    // 获取链表长度
    static int getLength(Node head) {
        int len = 0;
        Node cur = head;
        while (cur != null) {
            len++;
            cur = cur.next;
        }
        return len;
    }

    // 验证融合结果是否正确
    static boolean verifyMerge(Node merged, Node original1, Node original2) {
        if (merged == null) return original1 == null && original2 == null;

        Node cur = merged;
        Node cur1 = original1;
        Node cur2 = original2;
        boolean fromList1 = true;

        while (cur != null) {
            if (fromList1) {
                if (cur1 == null) return false;
                if (cur.value != cur1.value) return false;
                cur1 = cur1.next;
            } else {
                if (cur2 == null) return false;
                if (cur.value != cur2.value) return false;
                cur2 = cur2.next;
            }
            cur = cur.next;
            fromList1 = !fromList1;
        }

        return cur1 == null; // cur2可能还有剩余，但已处理完list1
    }

    public static void main(String[] args) {
        System.out.println("========== 链表融合测试 ==========\n");

        // 测试用例1: 等长链表
        System.out.println("=== 测试1: 等长链表 ===");
        Node list1 = createListA(new int[]{1, 3, 5});
        Node list2 = createListB(new int[]{2, 4, 6});

        System.out.println("链表A: " + listToString(list1));
        System.out.println("链表B: " + listToString(list2));

        // 创建副本，因为原链表会被修改
        Node list1Copy = copyList(list1);
        Node list2Copy = copyList(list2);

        Node[] result1 = insert(list1Copy, list2Copy);
        System.out.println("融合后: " + listToString(result1[0]));
        System.out.println("剩余B: " + listToString(result1[1]));
        System.out.println("验证: " + (verifyMerge(result1[0], list1, list2) ? "✓" : "✗"));
        System.out.println("预期: 1 → 2 → 3 → 4 → 5 → 6");
        System.out.println();

        // 测试用例2: A比B长
        System.out.println("=== 测试2: A链表比B链表长 ===");
        Node list3 = createListA(new int[]{1, 3, 5, 7, 9});
        Node list4 = createListB(new int[]{2, 4, 6});

        System.out.println("链表A: " + listToString(list3));
        System.out.println("链表B: " + listToString(list4));

        Node list3Copy = copyList(list3);
        Node list4Copy = copyList(list4);

        Node[] result2 = insert(list3Copy, list4Copy);
        System.out.println("融合后: " + listToString(result2[0]));
        System.out.println("剩余B: " + listToString(result2[1]));
        System.out.println("验证: " + (verifyMerge(result2[0], list3, list4) ? "✓" : "✗"));
        System.out.println("预期: 1 → 2 → 3 → 4 → 5 → 6 → 7 → 9");
        System.out.println();

        // 测试用例3: B比A长
        System.out.println("=== 测试3: B链表比A链表长 ===");
        Node list5 = createListA(new int[]{1, 3});
        Node list6 = createListB(new int[]{2, 4, 6, 8, 10});

        System.out.println("链表A: " + listToString(list5));
        System.out.println("链表B: " + listToString(list6));

        Node list5Copy = copyList(list5);
        Node list6Copy = copyList(list6);

        Node[] result3 = insert(list5Copy, list6Copy);
        System.out.println("融合后: " + listToString(result3[0]));
        System.out.println("剩余B: " + listToString(result3[1]));
        System.out.println("验证: " + (verifyMerge(result3[0], list5, list6) ? "✓" : "✗"));
        System.out.println("预期: 1 → 2 → 3 → 4, 剩余: 6 → 8 → 10");
        System.out.println();

        // 测试用例4: A为空链表
        System.out.println("=== 测试4: A为空链表 ===");
        Node list7 = null;
        Node list8 = createListB(new int[]{1, 2, 3});

        System.out.println("链表A: null");
        System.out.println("链表B: " + listToString(list8));

        Node list8Copy = copyList(list8);

        Node[] result4 = insert(list7, list8Copy);
        System.out.println("融合后: " + listToString(result4[0]));
        System.out.println("剩余B: " + listToString(result4[1]));
        System.out.println("注意: A为空时，融合结果应该直接是B，但你的方法返回null");
        System.out.println();

        // 测试用例5: B为空链表
        System.out.println("=== 测试5: B为空链表 ===");
        Node list9 = createListA(new int[]{1, 2, 3});
        Node list10 = null;

        System.out.println("链表A: " + listToString(list9));
        System.out.println("链表B: null");

        Node list9Copy = copyList(list9);

        Node[] result5 = insert(list9Copy, list10);
        System.out.println("融合后: " + listToString(result5[0]));
        System.out.println("剩余B: " + listToString(result5[1]));
        System.out.println("验证: " + (verifyMerge(result5[0], list9, list10) ? "✓" : "✗"));
        System.out.println("预期: 1 → 2 → 3 (B为空，A不变)");
        System.out.println();

        // 测试用例6: 两个都为空
        System.out.println("=== 测试6: 两个都为空 ===");
        Node[] result6 = insert(null, null);
        System.out.println("融合后: " + listToString(result6[0]));
        System.out.println("剩余B: " + listToString(result6[1]));
        System.out.println();

        // 测试用例7: 单节点融合
        System.out.println("=== 测试7: 单节点融合 ===");
        Node list11 = createListA(new int[]{100});
        Node list12 = createListB(new int[]{200});

        System.out.println("链表A: " + listToString(list11));
        System.out.println("链表B: " + listToString(list12));

        Node list11Copy = copyList(list11);
        Node list12Copy = copyList(list12);

        Node[] result7 = insert(list11Copy, list12Copy);
        System.out.println("融合后: " + listToString(result7[0]));
        System.out.println("剩余B: " + listToString(result7[1]));
        System.out.println("验证: " + (verifyMerge(result7[0], list11, list12) ? "✓" : "✗"));
        System.out.println("预期: 100 → 200");
        System.out.println();

        // 测试用例8: 大链表测试
        System.out.println("=== 测试8: 大链表测试 ===");
        int[] bigArray1 = new int[100];
        int[] bigArray2 = new int[100];
        for (int i = 0; i < 100; i++) {
            bigArray1[i] = i * 2;       // 偶数: 0, 2, 4, 6, ...
            bigArray2[i] = i * 2 + 1;   // 奇数: 1, 3, 5, 7, ...
        }

        Node bigList1 = createListA(bigArray1);
        Node bigList2 = createListB(bigArray2);

        System.out.println("链表A: [0, 2, 4, ..., 198] (100个节点)");
        System.out.println("链表B: [1, 3, 5, ..., 199] (100个节点)");

        Node bigList1Copy = copyList(bigList1);
        Node bigList2Copy = copyList(bigList2);

        long startTime = System.currentTimeMillis();
        Node[] result8 = insert(bigList1Copy, bigList2Copy);
        long endTime = System.currentTimeMillis();

        System.out.println("融合完成，耗时: " + (endTime - startTime) + "ms");
        System.out.println("融合后长度: " + getLength(result8[0]));
        System.out.println("剩余B长度: " + getLength(result8[1]));

        // 检查融合结果是否正确
        boolean correct = true;
        Node cur = result8[0];
        for (int i = 0; i < 200 && cur != null; i++) {
            if (cur.value != i) {
                correct = false;
                break;
            }
            cur = cur.next;
        }
        System.out.println("验证结果: " + (correct ? "✓" : "✗"));
        System.out.println("预期: 0 → 1 → 2 → 3 → ... → 199");
        System.out.println();

        // 测试用例9: 可视化演示
        System.out.println("=== 测试9: 可视化演示 ===");
        Node demo1 = createListA(new int[]{1, 2, 3});
        Node demo2 = createListB(new int[]{10, 20, 30, 40, 50});

        System.out.println("演示融合过程:");
        System.out.println("初始:");
        System.out.println("  A: " + listToString(demo1));
        System.out.println("  B: " + listToString(demo2));
        System.out.println();

        System.out.println("执行插入:");
        Node demo1Copy = copyList(demo1);
        Node demo2Copy = copyList(demo2);
        Node curA = demo1Copy;
        Node curB = demo2Copy;
        int step = 1;

        while (curA != null && curB != null) {
            Node tempA = curA.next;
            Node tempB = curB.next;

            curA.next = curB;
            curB.next = tempA;

            System.out.println("  步骤" + step + ":");
            System.out.println("    将 " + curB.value + " 插入到 " + curA.value + " 之后");
            System.out.println("    当前A链表: " + listToString(demo1Copy));
            System.out.println("    当前B链表: " + listToString(tempB));
            System.out.println();

            curA = tempA;
            curB = tempB;
            step++;
        }

        Node[] finalResult = insert(copyList(demo1), copyList(demo2));
        System.out.println("最终结果:");
        System.out.println("  融合链表: " + listToString(finalResult[0]));
        System.out.println("  剩余B: " + listToString(finalResult[1]));
        System.out.println();

        // 总结
        System.out.println("========== 测试总结 ==========");
        System.out.println("你的insert方法功能:");
        System.out.println("  1. 将链表B的节点交叉插入到链表A中");
        System.out.println("  2. 如果A比B长，A的剩余部分保持不变");
        System.out.println("  3. 如果B比A长，B的剩余部分作为第二个返回值");
        System.out.println("  4. 时间复杂度: O(min(m,n))");
        System.out.println("  5. 空间复杂度: O(1)");
        System.out.println();
        System.out.println("需要注意的问题:");
        System.out.println("  1. 当A为空时，融合结果返回null而不是B");
        System.out.println("  2. 此方法会修改原始链表A和B");
        System.out.println("  3. 如果需要保留原链表，需要先复制");
        System.out.println();
        System.out.println("示例修复空链表问题:");
        System.out.println("  static Node[] insertFixed(Node n1, Node n2) {");
        System.out.println("      Node[] res = new Node[2];");
        System.out.println("      if (n1 == null) {");
        System.out.println("          res[0] = n2;");
        System.out.println("          res[1] = null;");
        System.out.println("          return res;");
        System.out.println("      }");
        System.out.println("      if (n2 == null) {");
        System.out.println("          res[0] = n1;");
        System.out.println("          res[1] = null;");
        System.out.println("          return res;");
        System.out.println("      }");
        System.out.println("      // ... 原逻辑");
        System.out.println("  }");
    }

    // 复制链表（深拷贝）
    static Node copyList(Node head) {
        if (head == null) return null;
        Node newHead = new Node(head.value);
        Node newCur = newHead;
        Node cur = head.next;
        while (cur != null) {
            newCur.next = new Node(cur.value);
            newCur = newCur.next;
            cur = cur.next;
        }
        return newHead;
    }
}