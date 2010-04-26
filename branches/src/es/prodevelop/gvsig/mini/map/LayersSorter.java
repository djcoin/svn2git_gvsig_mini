package es.prodevelop.gvsig.mini.map;

import java.util.Vector;

import es.prodevelop.gvsig.mini.geom.Point;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

/**
 * Class to sort a Vector of Strings. This class sorts a Vector containing the
 * name of the layers in alphabetical order by MapRenderer type
 * 
 * @author aromeu
 * 
 */
public class LayersSorter {

	private final static Logger log = LoggerFactory
			.getLogger(LayersSorter.class);

	public LayersSorter() {

	}

	public Vector<String> sort(Vector<String> layers) {
		try {
			final int size = layers.size();

			String[] temp = new String[size];

			for (int i = 0; i < size; i++) {
				temp[i] = layers.elementAt(i);
			}
			quicksort(temp);

			Vector<String> temp2 = new Vector<String>(size);
			String layerName = null;
			for (int i = 0; i < size; i++) {
				layerName = temp[i];
				try {
					int index = layerName.indexOf("|");
					layerName = layerName.substring(index + 1, layerName
							.length());
					temp2.add(layerName);
				} catch (Exception e) {
					temp2.addElement(temp[i]);
				}

			}
			return temp2;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	/**
	 * Orders an array of points with the quicksort algorithm
	 * 
	 * @param a
	 *            The array of points to be ordered
	 * @param center
	 *            The point to compare with. The array will be sorted from
	 *            nearest to the center to furthest
	 */
	public static void quicksort(final String[] a) {
		quicksort(a, 0, a.length - 1);
	}

	// quicksort a[left] to a[right]
	private static void quicksort(final String[] a, final int left,
			final int right) {
		if (right <= left) {
			return;
		}
		int i = partition(a, left, right);
		quicksort(a, left, i - 1);
		quicksort(a, i + 1, right);
	}

	// partition a[left] to a[right], assumes left < right
	private static int partition(final String[] a, final int left,
			final int right) {
		int i = left - 1;
		int j = right;
		while (true) {

			while (less(a[++i], a[right]))
				// find item on left to swap
				; // a[right] acts as sentinel

			while (less(a[right], a[right])) // find
			// item
			// on
			// right
			// to
			// swap
			{
				if (j == left) {
					break; // don't go out-of-bounds

				}
			}
			if (i >= j) {
				break; // check if pointers cross

			}
			exch(a, i, j); // swap two elements into place

		}
		exch(a, i, right); // swap with partition element

		return i;
	}

	// is x < y ?
	private static boolean less(final String x, final String y) {
		try {
			final int index = x.indexOf("|");
			final int index2 = y.indexOf("|");

			if (index == -1)
				return false;
			if (index2 == -1)
				return false;

			String num = x.substring(0, index);
			String num2 = y.substring(0, index2);

			int position = Integer.valueOf(num).intValue();
			int position2 = Integer.valueOf(num2).intValue();

			return position < position2;

		} catch (Exception e) {
			return false;
		}

	}

	// exchange a[i] and a[j]
	private static void exch(final String[] a, final int i, final int j) {
		String swap = a[i];

		a[i] = a[j];
		a[j] = swap;
	}

}
