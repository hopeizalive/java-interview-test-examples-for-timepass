package com.example.javads.interview.demos.custom;

/**
 * Stable merge sort on int arrays using a scratch buffer (study implementation).
 *
 * <p><b>Idea:</b> split range in half, sort halves recursively, merge two sorted runs into {@code tmp}.
 * <p><b>Interview:</b> always O(n log n) time, O(n) extra space; stable merge preserves equal-key order.
 */
public final class CustomMergeSort {

    private CustomMergeSort() {}

    /**
     * Sorts {@code a} in ascending order in place using a temporary array of the same length.
     */
    public static void sort(int[] a) {
        if (a == null || a.length < 2) {
            return;
        }
        int[] tmp = new int[a.length];
        mergeSort(a, tmp, 0, a.length - 1);
    }

    private static void mergeSort(int[] a, int[] tmp, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) >>> 1;
        mergeSort(a, tmp, lo, mid);
        mergeSort(a, tmp, mid + 1, hi);
        merge(a, tmp, lo, mid, hi);
    }

    /**
     * Merges sorted {@code [lo..mid]} and {@code [mid+1..hi]} back into {@code a[lo..hi]}.
     */
    private static void merge(int[] a, int[] tmp, int lo, int mid, int hi) {
        int i = lo;
        int j = mid + 1;
        int k = lo;
        // Two-pointer merge: pick smaller head until one side exhausts.
        while (i <= mid && j <= hi) {
            tmp[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        }
        while (i <= mid) {
            tmp[k++] = a[i++];
        }
        while (j <= hi) {
            tmp[k++] = a[j++];
        }
        for (int x = lo; x <= hi; x++) {
            a[x] = tmp[x];
        }
    }
}
