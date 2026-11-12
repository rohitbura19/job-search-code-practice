package design;

// Insert Delete GetRandom O(1)  [Medium | ArrayList + HashMap index, swap-with-last remove]
//
// insert(1)=true, remove(2)=false, insert(2)=true, getRandom()=1 or 2,
// remove(1)=true, insert(2)=false, getRandom()=2

public class RandomizedSet {

    public RandomizedSet() {
        // TODO
    }

    boolean insert(int val) {
        // TODO
        return false;
    }

    boolean remove(int val) {
        // TODO
        return false;
    }

    int getRandom() {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        RandomizedSet s = new RandomizedSet();
        System.out.println(s.insert(1));   // true
        System.out.println(s.remove(2));   // false
        System.out.println(s.insert(2));   // true
        System.out.println(s.getRandom()); // 1 or 2
        System.out.println(s.remove(1));   // true
        System.out.println(s.insert(2));   // false
        System.out.println(s.getRandom()); // 2
    }
}
