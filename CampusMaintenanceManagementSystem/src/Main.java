import java.util.Stack;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    static class ActionEventListener extends JFrame implements ActionListener {
        private JButton button = new JButton();
        private int count = 0;

        public ActionEventListener() {
            initUI();
        }

        private void initUI() {
            setTitle("ActionEventListener");
            setSize(400, 100);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            button.setText(count + "");
            button.addActionListener(this);
            getContentPane().add(button);
            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            button.setText(++count + "");
        }
    }
    public static class Node{
        Integer data;
        Node next;
        public static Node head;
        public Node(int x){
            this.data=x;
//            next=null;
        }
        static void rReverse(){
            rReverse(head);
        }
        static Node rReverse(Node cur){
            if(cur==null) return null;
            if(cur.next!=null)
                rReverse(cur.next).next=cur;
            else head=cur;
//            cur.next=null;
            return cur;
        }
        static Node rotate(Node head,int k){
            if(head==null||head.next==null||k==0) return head;
            Node temp=head;
            int count=1;
            while(temp.next!=null){temp=temp.next;count++;}
                Node cur=head;
            int n=k%count;
            if(n==0){return head;}
                for(int i=1;i<n;i++) {
                    cur = cur.next;
                }
                temp.next=head;
                head=cur.next;
                cur.next=null;
            return head;
        }
        static int nthLast(Node head,int n){
            if(head==null) return -1;
            myQueue list=new myQueue(n);
            Node cur=head;
            while(cur!=null){
                if(!list.isFull()) {
                    list.enqueue(cur.data);
                }else{
                    list.dequeue();
                    list.enqueue(cur.data);
                }
                cur=cur.next;
            }
            if(!list.isFull()){
                return -1;
            }
            return list.dequeue();
        }
        static Node delLastOcc(Node head,int k){
            if(head==null) return null;
            Node cur=head;
            Node prev=null;
            while(cur.next!=null){
                if(cur.next.data==k) prev=cur;
                cur=cur.next;
            }
            if(prev==null) {
                if (head.data == k) {
                    return head.next;
                }
                return head;
            }
            prev.next=prev.next.next;
            return head;
        }
        static Node[] insert(Node n1,Node n2){
            Node[] res=new Node[2];
            Node cur1=n1,cur2=n2;
            while(cur1!=null&&cur2!=null){
                Node temp1=cur1.next,temp2=cur2.next;
                cur1.next=cur2;
                cur2.next=temp1;
                cur1=temp1;
                cur2=temp2;
            }
            res[0]=n1;
            res[1]=cur2;
            return res;
        }
        static void printCircleLinkedList(Node head) {
            if (head == null) return;
            Node cur = head;
            do {
                System.out.println(cur.data);
                cur = cur.next;
            } while (cur!=head);
        }
        static Node swapPair(Node head){
            if(head==null||head.next==null) return head;
            Node temp=head.next;
            head.next=swapPair(temp.next);
            temp.next=head;
            return temp;
        }
    }
    class Example {
        private final int value; // final 变量

        public Example() {
            // 值在此处初始化
            value = 10;
        }

        public Example(int x) {
            // 无法在这里再次赋值，否则会导致编译错误
             value = x; // 编译错误
        }
    }
    public static class myDoublyLL{
        Integer data;
        myDoublyLL next,prev;
        public myDoublyLL(int x){
            this.data=x;
        }
        static myDoublyLL rReverse(myDoublyLL cur){
            if(cur==null) return null;
            if(cur.next!=null) {
                rReverse(cur.next).next = cur;
            }
            else {
                cur.prev=null;
            }
            cur.prev=cur.next;
            cur.next=null;
            return cur;
        }

    }
    public static class myStack{
        int[] data;
        int top;
        public myStack(int size){
            top=0;
            data=new int[size];
        }
        public boolean isEmpty(){
            return top==0;
        }
        public void push(int x){
            if(top<data.length) data[top++]=x;
        }
        public int pop(){
            if(isEmpty()) return -1;
            return data[--top];
        }
        public int peek(){
            if(isEmpty()) return -1;
            return data[top-1];
        }
    }
    public static class myQueue{
        int[] data;
        int front;
        int rear;
        int size;

        public myQueue(int size)  {
            if(size<=0) throw new IllegalArgumentException();
            this.data= new int[size];
            front=0;
            rear=-1;
            this.size=0;
        }
        void enqueue(int k){
            if(isFull()) return;
            if(++rear>= data.length){
                rear-=data.length;
            }
            data[rear]=k;
            size++;
        }
        int dequeue(){
            if(size==0) return -1;
            int res=data[front];
            front=(front+1)>= data.length?0:(front+1);//front=(front+1)%data.length
            size--;
            return res;
        }
        int peek(){
            if (size==0)return -1;
            return data[front];
        }
        boolean isFull(){
            return size== data.length;
        }
        boolean isEmpty(){
            return size==0;
        }
        public static void printQueue(myQueue q) {
            myQueue tempQueue = new myQueue(q.size);  // 创建一个临时队列，容量和原队列一样
            while (!q.isEmpty()) {
                int val = q.dequeue();
                System.out.print(val + " ");
                tempQueue.enqueue(val);
            }
            System.out.println();

            // 把元素从临时队列放回原队列，恢复原队列状态
            while (!tempQueue.isEmpty()) {
                q.enqueue(tempQueue.dequeue());
            }
        }
    }
    public static class s2Queue{
        myStack dataen;
        myStack datade;
        int size;
        public s2Queue(int c){
            dataen=new myStack(c);
            datade=new myStack(c);
            size=0;
        }
        public void enqueue(int x){
            dataen.push(x);
            size++;
        }
        public int dequeue(){
            while(!dataen.isEmpty()){
                datade.push(dataen.pop());
            }
            if(!datade.isEmpty()) {
                size--;
                int res = datade.pop();
                while (!datade.isEmpty()){
                    dataen.push(datade.pop());
                }
                    return res;
            }else{
                return -1;
            }
        }

        public int getSize() {
            return size;
        }
        public int front(){
            if(!dataen.isEmpty()){
                while(!dataen.isEmpty()){
                    datade.push(dataen.pop());
                }
                    int res = datade.peek();
                    while (!datade.isEmpty()) {
                        dataen.push(datade.pop());
                    }
                    return res;
            }
            return -1;
        }
    }

    enum Color {
        // 1️⃣ 这些是枚举常量声明
        //    每个都相当于调用构造器：new Color("RED")
        RED("红色"),
        GREEN("绿色"),
        BLUE("蓝色");

        // 字段
        private final String chineseName;

        // 2️⃣ 构造器：在创建每个枚举常量时调用
        Color(String chineseName) {
            this.chineseName = chineseName;
            System.out.println("创建Color常量: " + chineseName);
        }
    }
    public static int[] pSmaller(int[] a){
        int n= a.length;
        int[] res=new int[n];
        myStack s=new myStack(n-1);
        for(int i=0;i<n;i++){
            while(!s.isEmpty()){
                int r=s.peek();
                if(r<a[i]){
                    res[i]=r;
                    break;
                }
                s.pop();
            }
            if(s.isEmpty()){
                res[i]=-1;
            }
            s.push(a[i]);
        }
        return res;
    }
    public static int[] pSmallerIndex(int[] a){
        int n= a.length;
        int[] res=new int[n];
        myStack s=new myStack(n-1);
        for(int i=0;i<n;i++){
            while(!s.isEmpty()){
                int r=a[s.peek()];
                if(r<a[i]){
                    res[i]=s.peek();
                    break;
                }
                s.pop();
            }
            if(s.isEmpty()) {
                res[i] = -1;
            }
            s.push(i);
        }
        return res;
    }
    public static int[] nSmallerIndex(int[] a){
        myStack s=new myStack(a.length);
        int[] res=new int[a.length];
        s.push(a.length-1);
        res[a.length-1]=-1;
        for(int i=a.length-2;i>=0;i--){
            if(!s.isEmpty()){
                while(!s.isEmpty()&&a[s.peek()]>a[i]){
                    s.pop();
                }
                if(s.isEmpty()){
                    res[i]=-1;
                }else {
                    res[i]= s.peek();
                }
                s.push(i);
            }
        }
        return res;
    }

    public static int[] sortStack(myStack s){
        myStack temp= new myStack(s.data.length);
        temp=rsortStack(s,temp);
        int[] r=new int[s.data.length];
        int count=0;
        while(!temp.isEmpty()){
            r[count++]=temp.pop();
        }
        return r;
    }
    public static myStack rsortStack(myStack s,myStack temp){
        if(s.isEmpty()) return temp;
        int key;
        if(temp.isEmpty()){
            temp.push(s.pop());
            return rsortStack(s,temp);
        }
        key=s.peek();
        if(key<=temp.peek()) {
            temp.push(s.pop());
            return rsortStack(s,temp);
        }else{
            key=s.pop();
            int count=0;
            while(key> temp.peek()&&!temp.isEmpty()){
                s.push(temp.pop());
                count++;
            }
            temp.push(key);
            for(int i=0;i<count;i++){
                temp.push(s.pop());
            }
            return rsortStack(s,temp);
        }
    }

    public static int[] stock(int[] arr){
        int n=arr.length;
        int[] res=new int[n];
        myStack s=new myStack(n);
        myStack s2=new myStack(n);
        res[0]=1;
        s.push(arr[0]);
        s2.push(1);
        for(int i=1;i<n;i++){
            int k=s.peek();
            int count =1;
            while(arr[i]>=k&&!s.isEmpty()){
                s.pop();
                k=s.peek();
                count+=s2.pop();
            }
                s.push(arr[i]);
                res[i]=count;
                s2.push(count);
        }
        return res;
    }

    public static String postfix(String string){
        if(string==null) return null;
        Stack<Character> s=new Stack<>();
        char[] res=new char[string.length()];
        int count=0;
        for(char a:string.toCharArray()){
            if(a>='a'&&a<='z'){
                res[count++]=a;
            } else {
                switch (a){
                    case '(':
                    case '^':
                        s.push(a)
;                        break;
                    case ')':
                        while(s.peek()!='('){
                            res[count++]=s.pop();
                        }
                        s.pop();
                        break;
                    case '*':
                    case '/':
                        while((!s.isEmpty()&&s.peek()!='(')&&s.peek()!='+'&&s.peek()!='-') {
                            res[count++] = s.pop();
                        }
                        s.push(a);
                        break;
                    case '+':
                    case '-':
                        while(!s.isEmpty()&&s.peek()!='('){
                            res[count++]=s.pop();
                        }
                        s.push(a);
                        break;
                }
            }
        }
        while(!s.isEmpty()){
            res[count++]=s.pop();
        }
        return new String(res,0,count);
    }

    public static myQueue revQueue(myQueue q){
        if(q.isEmpty()){return null;}
        if(q.size==1){return q;}
        int n=q.size;
        int temp=q.dequeue();
        myQueue r=revQueue(q);
        myQueue res=new myQueue(n);
        while(!r.isEmpty()){
            res.enqueue(r.dequeue());
        }
        res.enqueue(temp);
        return res;
    }
    public static int count=0;
    public static void langdui(){
        char[] list={'中','边','射','辅','野','新'};
        int i=1;
        int l=2;
        int n=list.length;
        while(count<n){
            System.out.print(list[i++-1]+"强， ");
            count++;
        }
        System.out.println();
        while(count<(int)(Math.pow(2,n)-1)){
            int tl=l;
            int ti=i,min=0;
            while(tl>0){
                int x= (int) (ti/Math.pow(n+1,--tl));
                if(tl!=0&& x ==n-tl){
                    int c=x*(int)(Math.pow(n+1,tl));
                    i+=(int)Math.pow(n+1,tl+1)-c;
                    while(x<n+1){
                        c+=(int)(Math.pow(n+1,tl--)*x);
                        System.out.print(list[(x++)-1]);
                    }

                    break;
                }
                if(x==0||x<=min){
                 ti++;
                 i++;
                 tl++;
                    if(i>=Math.pow(n+1,l)) {l++;
                        tl=l;
                        System.out.println();}
                }else{
                    System.out.print(list[x -1]);
                    min= x;
                    ti%= (int) Math.pow(n+1,tl);
                }
            }
            System.out.print("联动强， ");
            count++;
            i++;
            if(i>=Math.pow(n+1,l)) {l++;
            System.out.println();}
        }
    }

    public static void main(String[] args) {
        langdui();
    }

    // 正向遍历打印
    public static void printForward(myDoublyLL head) {
        myDoublyLL cur = head;
        while (cur != null) {
            System.out.print(cur.data + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    // 反向遍历打印
    public static void printBackward(myDoublyLL tail) {
        myDoublyLL cur = tail;
        while (cur != null) {
            System.out.print(cur.data + " ");
            cur = cur.prev;
        }
        System.out.println();
    }

    private static void printList(Node head) {
        if(head==null){
            return;
        }
        System.out.print(head.data+" ");
        printList(head.next);
    }
    static Node createList(int[] values) {
        if (values == null || values.length == 0) {
            return null;
        }
        Node head = new Node(values[0]);
        Node current = head;
        for (int i = 1; i < values.length; i++) {
            current.next = new Node(values[i]);
            current = current.next;
        }
        return head;
    }

    // 辅助方法：打印链表
    static String listToString(Node head) {
        if (head == null) return "null";
        StringBuilder sb = new StringBuilder();
        Node cur = head;
        while (cur != null) {
            sb.append(cur.data);
            if (cur.next != null) {
                sb.append(" -> ");
            }
            cur = cur.next;
        }
        return sb.toString();
    }

    // 辅助方法：比较两个链表是否相等
    static boolean listsEqual(Node list1, Node list2) {
        while (list1 != null && list2 != null) {
            if (list1.data != list2.data) {
                return false;
            }
            list1 = list1.next;
            list2 = list2.next;
        }
        return list1 == null && list2 == null;
    }

    // 辅助方法：复制链表
    static Node copyList(Node head) {
        if (head == null) return null;
        Node newHead = new Node(head.data);
        Node newCur = newHead;
        Node cur = head.next;
        while (cur != null) {
            newCur.next = new Node(cur.data);
            newCur = newCur.next;
            cur = cur.next;
        }
        return newHead;
    }

    // 测试用例结构
    static class TestCase {
        String name;
        int[] input;
        int k;
        int[] expected;

        TestCase(String name, int[] input, int k, int[] expected) {
            this.name = name;
            this.input = input;
            this.k = k;
            this.expected = expected;
        }
    }


}