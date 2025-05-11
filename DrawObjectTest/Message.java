class Message<T extends DrawnObject> {
    private final T object;

    public Message(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
