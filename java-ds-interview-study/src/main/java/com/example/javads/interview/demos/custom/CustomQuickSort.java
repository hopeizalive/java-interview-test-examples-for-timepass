package com.example.javads.interview.demos.custom;

/**
 * In-place quick sort for int arrays (study implementation, not {@code java.util}).
 *
 * <p><b>Idea:</b> partition around a pivot so smaller elements sit left, larger right, then recurse.
 * <p><b>Interview:</b> average O(n log n), worst O(n²) with bad pivots; this demo uses last-element pivot.
 */
public final class CustomQuickSort {

    private CustomQuickSort() {}

    /**
     * Sorts {@code a} in ascending order in place.
     *
     * @param a array to sort; mutated
     */
    public static void sort(int[] a) {
        if (a == null || a.length < 2) {
            return;
        }
        quickSort(a, 0, a.length - 1);
    }

    private static void quickSort(int[] a, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        // Partition: pivot ends at index p; recurse on strictly smaller ranges.
        int p = partition(a, lo, hi);
        quickSort(a, lo, p - 1);
        quickSort(a, p + 1, hi);
    }

    /**
     * Lomuto-style partition: pivot is {@code a[hi]}; elements {@code <= pivot} move left of split.
     */
    private static int partition(int[] a, int lo, int hi) {
        int pivot = a[hi];
        int i = lo;
        // Story: j scans unknown region; i marks end of "less than or equal to pivot" region.
        for (int j = lo; j < hi; j++) {
            if (a[j] <= pivot) {
                swap(a, i, j);
                i++;
            }
        }
        swap(a, i, hi);
        return i;
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
