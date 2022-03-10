package bg.sofia.uni.fmi.mjt.gifts.person;

import bg.sofia.uni.fmi.mjt.gifts.exception.WrongReceiverException;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;

import java.util.*;

public class DefaultPerson<I> implements Person<I> {
    private I id;
    private Collection<Gift<?>> giftsReceived;

    public DefaultPerson(I id) {
        this.id = id;
        this.giftsReceived = new ArrayList<Gift<?>>();
    }

    @Override
    public Collection<Gift<?>> getNMostExpensiveReceivedGifts(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        Map<Double, Gift<?>> giftMap = new TreeMap<Double, Gift<?>>(Collections.reverseOrder());
        for (Gift<?> g : giftsReceived) {
            giftMap.put(g.getPrice(), g);
        }
        int giftCounter = 0;
        List<Gift<?>> result = new LinkedList<>();
        for (Map.Entry<Double, Gift<?>> e : giftMap.entrySet()) {
            result.add(e.getValue());
            giftCounter++;
            if (giftCounter == n) {
                break;
            }
        }
        return List.copyOf(result);
    }

    @Override
    public Collection<Gift<?>> getGiftsBy(Person<?> person) {
        if (person == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Gift<?>> result = new ArrayList<Gift<?>>();
        for (Gift<?> g : giftsReceived) {
            if (g.getSender().equals(person)) {
                result.add(g);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public I getId() {
        return id;
    }

    @Override
    public void receiveGift(Gift<?> gift) {
        if (gift == null) {
            throw new IllegalArgumentException();
        }
        if (!gift.getReceiver().equals(this)) {
            throw new WrongReceiverException();
        }
        giftsReceived.add(gift);
    }
}
