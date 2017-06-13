package com.baselibrary.utils;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by quantan.liu on 2017/3/10.
 */
public class RxBus {
    /**
     * 参考网址:  http://www.loongwind.com/archives/264.html 看这个学习rxbus怎么用
     *http://gank.io/post/560e15be2dca930e00da1083  看这个学习rxjava
     */
    private static volatile RxBus mDefaultInstance;

    private RxBus() {
    }

    public static RxBus getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    private final Subject<Object> _bus = PublishSubject.create();

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return _bus;
    }
    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return _bus.ofType(eventType);
    }

    /**
     * 提供了一个新的事件,根据code进行分发
     * @param code 事件code
     * @param o
     */
    public void post(int code, Object o){
        _bus.onNext(new Message(code,o));
    }

    /**
     * 提供了一个新的事件,单一类型
     * @param o 事件数据
     */
    public void post (Object o) {
        _bus.onNext(o);
    }

    /**
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     * 对于注册了code为0，class为voidMessage的观察者，那么就接收不到code为0之外的voidMessage。
     * @param code 事件code
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(final int code, final Class<T> eventType) {
        //ofType表示只接收指定的数据类型
        return _bus.ofType(Message.class)
                //只有通过判断的数据才可以被接收
                .filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<Message>() {
                    @Override
                    public boolean test(Message message) {
                        //当接收的code和发送的code对应的时候还有接收的对象和发送的对象对应的时候此条消息才可以被接收
                        return message.getCode() == code && eventType.isInstance(message.getObject());
                    }
                }).map(new Function<Message, Object>() {
                    @Override
                    public Object apply(Message message) throws Exception {
                        return message.getObject();
                    }
                }).cast(eventType);
        //cast将一个发送的数据源转换为指定的类型
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return _bus.hasObservers();
    }
    public class Message {
        private int code;
        private Object object;

        public Message() {}

        public Message(int code, Object o) {
            this.code = code;
            this.object = o;
        }
        //getter and setter

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

}