package bg.sofia.uni.fmi.mjt.gifts.service;

import bg.sofia.uni.fmi.mjt.gifts.gift.BirthdayGift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Priceable;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultPackingService<T extends Priceable> implements PackingService<T> {
    @Override
    public Gift<T> pack(Person<?> sender, Person<?> receiver, T item) {
        if (sender == null || receiver == null || item == null) {
            throw new IllegalArgumentException();
        }
        List<T> gift = new ArrayList<>();
        gift.add(item);

        return new BirthdayGift<T>(sender, receiver, gift);
    }

    @Override
    public Gift<T> pack(Person<?> sender, Person<?> receiver, T... items) {
        if (sender == null || receiver == null || items == null) {
            throw new IllegalArgumentException();
        }
        List<T> gift = new ArrayList<>();
        for (var i : items) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
            gift.add(i);
        }
        return new BirthdayGift<T>(sender, receiver, gift);
    }

    @Override
    public Collection<T> unpack(Gift<T> gift) {
        if (gift == null) {
            throw new IllegalArgumentException();
        }
        List<T> giftList = new ArrayList<>();
        for (var i : gift.getItems()) {
            giftList.add(i);
        }
        return List.copyOf(giftList);
    }
}
