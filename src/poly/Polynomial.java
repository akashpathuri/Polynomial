package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */

	public static Node add(Node poly1, Node poly2) {
		Node answer = null;
		Node first = null;
		if(poly1 == null && poly2 == null){
			return null;
		}else if(poly1 == null){
			return poly2;
		}else if (poly2 == null){
			return poly1;
		}else {
			if (poly1.term.degree == poly2.term.degree) {
				float sum = poly1.term.coeff + poly2.term.coeff;
				if (sum == 0) {
					answer = null;
				} else {
					answer = new Node(sum, poly1.term.degree, null);
				}
				poly1 = poly1.next;
				poly2 = poly2.next;
			} else if (poly1.term.degree < poly2.term.degree) {
				answer = poly1;
				poly1 = poly1.next;
			} else {
				answer = poly2;
				poly2 = poly2.next;
			}
			first = answer;
			Node end = null;
			while (poly1 != null || poly2 != null) {
				if (poly1 == null) {
					if (answer == null) {
						first = poly2;
					} else {
						answer.next = poly2;
					}
					break;
				}
				if (poly2 == null) {
					if (answer == null) {
						first = poly1;
					} else {
						answer.next = poly1;
					}
					break;
				}
				assert poly1 != null;
				assert poly2 != null;
				System.out.println(toString(poly2));
				if (poly1.term.degree == poly2.term.degree) {
					float sum = poly1.term.coeff + poly2.term.coeff;
					if (answer == null && sum != 0) {
						answer = new Node(sum, poly1.term.degree, null);
						first = answer;
					} else if (sum != 0) {
						answer.next = new Node(sum, poly1.term.degree, null);
						answer = answer.next;
					}
					poly1 = poly1.next;
					poly2 = poly2.next;
				} else if (poly1.term.degree < poly2.term.degree) {
					if (answer != null) {
						answer.next = new Node(poly1.term.coeff, poly1.term.degree, null);
						answer = answer.next;
					} else {
						answer = new Node(poly1.term.coeff, poly1.term.degree, null);
						first = answer;
					}
					poly1 = poly1.next;
				} else {
					if (answer != null) {
						answer.next = new Node(poly2.term.coeff, poly2.term.degree, null);
						answer = answer.next;
					} else {
						answer = new Node(poly2.term.coeff, poly2.term.degree, null);
						first = answer;
					}
					poly2 = poly2.next;
				}
			}
		}

		return first;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		Node sum = null;
		Node first;
		Node poly2copy = poly2;
		System.out.println(toString(poly1));
		while (poly1!=null){
			Node current = null;
			first = null;
			poly2 = poly2copy;
			while (poly2!=null) {
				if (current == null){
					current = new Node(poly1.term.coeff * poly2.term.coeff, poly1.term.degree + poly2.term.degree, null);
					first = current;
				}else{
					current.next = new Node(poly1.term.coeff * poly2.term.coeff, poly1.term.degree + poly2.term.degree, null);
					current = current.next;
				}
				poly2 = poly2.next;
			}
			System.out.println(toString(sum) +"\t\t"+ toString(first));
			sum = add(sum,first);
			poly1 = poly1.next;
		}
		return sum;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		float sum = 0;
		while (poly!=null){
			sum += (float) (poly.term.coeff * (Math.pow(x,poly.term.degree)));
			poly = poly.next;
		}
		return sum;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
