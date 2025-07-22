package shop.dodream.book.service;

public interface ProducerService<T> {
    void sendMessage(String userId, T t);
}
