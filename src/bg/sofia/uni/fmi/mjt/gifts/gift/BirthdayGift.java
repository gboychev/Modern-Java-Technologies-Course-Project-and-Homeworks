package bg.sofia.uni.fmi.mjt.gifts.gift;

import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.Collection;
import java.util.List;

public class BirthdayGift<T extends Priceable> implements Gift<T> {
    private double price;
    private Person<?> sender;
    private Person<?> receiver;
    private Collection<T> items;

    public BirthdayGift(Person<?> sender, Person<?> receiver, Collection<T> items) {
        this.sender = sender;
        this.receiver = receiver;
        this.items = items;
        this.price = 0.0;
        for (T t : items) {
            if (t != null) {
                price += t.getPrice();
            }
        }
    }

    @Override
    public Person<?> getSender() {
        return sender;
    }

    @Override
    public Person<?> getReceiver() {
        return receiver;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void addItem(T t) {
        if (t == null) {
            throw new IllegalArgumentException();
        }
        items.add(t);
        price += t.getPrice();
    }

    @Override
    public boolean removeItem(T t) {
        if (t == null) {
            return false;
        }
        boolean foundItem = false;
        foundItem = items.contains(t);
        items.remove(t);
        price -= t.getPrice();
        return foundItem;
    }

    @Override
    public Collection<T> getItems() {
        return List.copyOf(items);
    }

    @Override
    public T getMostExpensiveItem() {
        T mostExpensive = null;
        double highestPrice = Double.MIN_VALUE;
        for (T t : items) {
            if (t.getPrice() > highestPrice ) {
                highestPrice = t.getPrice();
                mostExpensive = t;
            }
        }
        return mostExpensive;
    }
}
